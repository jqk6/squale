/**
 * Copyright (C) 2008-2010, Squale Project - http://www.squale.org
 *
 * This file is part of Squale.
 *
 * Squale is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * Squale is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Squale.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.airfrance.squalix.tools.pmd;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

import com.airfrance.squalecommon.SqualeTestCase;
import com.airfrance.squalecommon.daolayer.component.ProjectDAOImpl;
import com.airfrance.squalecommon.daolayer.component.ProjectParameterDAOImpl;
import com.airfrance.squalecommon.daolayer.result.MeasureDAOImpl;
import com.airfrance.squalecommon.datatransfertobject.rulechecking.PmdRuleSetDTO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ListParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.MapParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.StringParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.config.TaskParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.rulechecking.JavaPmdTransgressionBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.rulechecking.JspPmdTransgressionBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.QualityGridBO;
import com.airfrance.squalecommon.enterpriselayer.facade.pmd.PmdFacade;
import com.airfrance.squalix.core.AbstractTask;
import com.airfrance.squalix.core.TaskData;

/**
 * Test de la t�che Pmd
 */
public class PmdTaskTest
    extends SqualeTestCase
{

    /** r�pertoire des .java */
    public static final String SOURCES_DIR = "data/Project4PmdTest";

    /**
     * Pseudo-chemin vers une vue.
     */
    private static final String VIEW_PATH = ".";

    /** l'application */
    private ApplicationBO mAppli = new ApplicationBO();

    /** le projet � auditer */
    private ProjectBO mProject = new ProjectBO();

    /** l'audit */
    private AuditBO mAudit = new AuditBO();

    /** les t�ches temporaires */
    private TaskData mData = new TaskData();

    /**
     * @see TestCase#setUp()
     */
    protected void setUp()
        throws Exception
    {
        super.setUp();
        getSession().beginTransaction();
        InputStream javaStream = getClass().getClassLoader().getResourceAsStream( "config/pmd_dinb.xml" );
        StringBuffer javaErrors = new StringBuffer();
        // parsing du contenu du fichier pour le code java
        PmdRuleSetDTO javaRuleset = PmdFacade.importPmdConfig( javaStream, javaErrors );
        InputStream jspStream = getClass().getClassLoader().getResourceAsStream( "config/pmd_jsp.xml" );
        StringBuffer jspErrors = new StringBuffer();
        // parsing du contenu du fichier pour le code jsp
        PmdRuleSetDTO jspRuleset = PmdFacade.importPmdConfig( jspStream, jspErrors );
        mAppli = getComponentFactory().createApplication( getSession() );
        QualityGridBO grid = getComponentFactory().createGrid( getSession() );
        mProject = getComponentFactory().createProject( getSession(), mAppli, grid );
        // Enregistrement du ProjectBO dans la base
        ProjectDAOImpl projectDAO = ProjectDAOImpl.getInstance();
        // Les param�tres doivent contenir le chemin du fichier de configuration
        // et le dialect pour les sources Java
        MapParameterBO params = new MapParameterBO();
        StringParameterBO dialect = new StringParameterBO();
        dialect.setValue( ParametersConstants.JAVA1_4 );
        params.getParameters().put( ParametersConstants.DIALECT, dialect );
        ListParameterBO listParam = new ListParameterBO();
        // Traitement des sources Java
        List list = new ArrayList();
        StringParameterBO stringParam = new StringParameterBO();
        stringParam.setValue( SOURCES_DIR );
        list.add( stringParam );
        listParam.setParameters( list );
        params.getParameters().put( ParametersConstants.SOURCES, listParam );
        // Param�tres PMD
        MapParameterBO pmd = new MapParameterBO();
        // Traitement du ruleset JAVA
        StringParameterBO javaParam = new StringParameterBO();
        javaParam.setValue( javaRuleset.getName() );
        pmd.getParameters().put( ParametersConstants.PMD_JAVA_RULESET_NAME, javaParam );
        // Traitement du ruleset JSP
        StringParameterBO jspParam = new StringParameterBO();
        jspParam.setValue( jspRuleset.getName() );
        pmd.getParameters().put( ParametersConstants.PMD_JSP_RULESET_NAME, jspParam );
        params.getParameters().put( ParametersConstants.PMD, pmd );
        // Traitement des sources JSP
        listParam = new ListParameterBO();
        list = new ArrayList();
        stringParam = new StringParameterBO();
        stringParam.setValue( SOURCES_DIR );
        list.add( stringParam );
        listParam.setParameters( list );
        params.getParameters().put( ParametersConstants.JSP, listParam );
        // Pas de r�pertoire exclu
        ProjectParameterDAOImpl.getInstance().create( getSession(), params );
        mProject.setParameters( params );
        ProjectDAOImpl.getInstance().save( getSession(), mProject );
        mAudit = getComponentFactory().createAudit( this.getSession(), mProject );
        // On fait le commit pour permettre l'acc�s aux donn�es dans une autre session
        getSession().commitTransactionWithoutClose();
        File viewFile = new File( VIEW_PATH );
        mData.putData( TaskData.VIEW_PATH, viewFile.getCanonicalPath() );
    }

    /**
     * Cr�ation de la t�che
     * 
     * @param pLanguages langages utilis�s
     * @return t�che correspondante
     */
    private PmdTask createTask( String[] pLanguages )
    {
        PmdTask task = new PmdTask();
        task.setData( mData );
        ArrayList taskParameters = new ArrayList();
        for ( int i = 0; i < pLanguages.length; i++ )
        {
            TaskParameterBO param = new TaskParameterBO();
            param.setName( "language" );
            param.setValue( pLanguages[i] );
            taskParameters.add( param );
        }
        task.setTaskParameters( taskParameters );
        task.setAuditId( new Long( mAudit.getId() ) );
        task.setProjectId( new Long( mProject.getId() ) );
        task.setApplicationId( new Long( mAppli.getId() ) );
        task.setStatus( AbstractTask.NOT_ATTEMPTED );
        return task;
    }

    /**
     * Test d'ex�cution en Java
     */
    public void testJavaExecute()
    {
        try
        {
            PmdTask task = createTask( new String[] { "java" } );
            task.run();
            assertEquals( AbstractTask.TERMINATED, task.getStatus() );

            // On v�rifie les r�sultats
            MeasureDAOImpl dao = MeasureDAOImpl.getInstance();
            assertEquals( 1, dao.count( getSession() ).intValue() );
            // Obtention de la mesure
            JavaPmdTransgressionBO trans = (JavaPmdTransgressionBO) dao.findAll( getSession() ).get( 0 );
            assertEquals( 1, trans.getTotalInfoNumber() );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }

    /**
     * Test d'ex�cution en J2ee
     */
    public void testJ2eeExecute()
    {
        try
        {
            PmdTask task = createTask( new String[] { "java", "jsp" } );
            task.run();
            assertEquals( AbstractTask.TERMINATED, task.getStatus() );

            // On v�rifie les r�sultats
            MeasureDAOImpl dao = MeasureDAOImpl.getInstance();
            assertEquals( 2, dao.count( getSession() ).intValue() );
            // Obtention des mesures
            Long projectId = new Long( mProject.getId() );
            Long auditId = new Long( mAudit.getId() );
            Collection measures = dao.findAll( getSession() );
            // Obtention de la mesure java
            JavaPmdTransgressionBO javaTrans =
                (JavaPmdTransgressionBO) dao.load( getSession(), projectId, auditId, JavaPmdTransgressionBO.class );
            assertEquals( 1, javaTrans.getTotalInfoNumber() );
            // Obtention de la mesure jsp
            JspPmdTransgressionBO jspTrans =
                (JspPmdTransgressionBO) dao.load( getSession(), projectId, auditId, JspPmdTransgressionBO.class );
            assertEquals( 2, jspTrans.getTotalErrorNumber() );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }

    /**
     * Test d'ex�cution en J2ee
     */
    public void testJ2eeWithInclusionExecute()
    {
        try
        {

            // On cr�e les exclusion
            getSession().beginTransaction();
            MapParameterBO params = mProject.getParameters();
            // Les patterns � inclure
            ListParameterBO includedList = new ListParameterBO();
            ArrayList iList = new ArrayList();
            StringParameterBO stringInclude = new StringParameterBO();
            stringInclude.setValue( "**.java" );
            iList.add( stringInclude );
            includedList.setParameters( iList );
            params.getParameters().put( ParametersConstants.INCLUDED_PATTERNS, includedList );

            ProjectParameterDAOImpl.getInstance().create( getSession(), params );
            mProject.setParameters( params );
            ProjectDAOImpl.getInstance().save( getSession(), mProject );
            getSession().commitTransactionWithoutClose();

            PmdTask task = createTask( new String[] { "java", "jsp" } );
            task.run();
            assertEquals( AbstractTask.TERMINATED, task.getStatus() );

            // On v�rifie les r�sultats
            MeasureDAOImpl dao = MeasureDAOImpl.getInstance();
            assertEquals( 2, dao.count( getSession() ).intValue() );
            // Obtention des mesures
            Long projectId = new Long( mProject.getId() );
            Long auditId = new Long( mAudit.getId() );
            Collection measures = dao.findAll( getSession() );
            // Obtention de la mesure java
            JavaPmdTransgressionBO javaTrans =
                (JavaPmdTransgressionBO) dao.load( getSession(), projectId, auditId, JavaPmdTransgressionBO.class );
            assertEquals( 1, javaTrans.getTotalInfoNumber() );
            // Il ne doit pas y avoir de mesures jsp car on exclu que les .jsp
            JspPmdTransgressionBO jspTrans =
                (JspPmdTransgressionBO) dao.load( getSession(), projectId, auditId, JspPmdTransgressionBO.class );
            assertEquals( 0, jspTrans.getTotalErrorNumber() );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }

}
