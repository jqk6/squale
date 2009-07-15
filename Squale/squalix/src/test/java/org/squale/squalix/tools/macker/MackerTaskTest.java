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
package com.airfrance.squalix.tools.macker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.squalecommon.SqualeTestCase;
import com.airfrance.squalecommon.daolayer.component.ProjectDAOImpl;
import com.airfrance.squalecommon.daolayer.component.ProjectParameterDAOImpl;
import com.airfrance.squalecommon.daolayer.result.MeasureDAOImpl;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ListParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.MapParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.StringParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.rulechecking.RuleCheckingTransgressionBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.rulechecking.RuleCheckingTransgressionItemBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.QualityGridBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking.RuleBO;
import com.airfrance.squalecommon.enterpriselayer.facade.macker.RuleFactory;
import com.airfrance.squalix.core.AbstractTask;
import com.airfrance.squalix.core.TaskData;

/**
 * Test de la t�che Macker
 */
public class MackerTaskTest
    extends SqualeTestCase
{

    /** r�pertoire des .class */
    public static final String CLASSES_DIR = "data/Project4MackerTest/bin";

    /** r�pertoire des .java */
    public static final String SOURCES_DIR = "data/Project4MackerTest";

    /**
     * Pseudo-chemin vers une vue.
     */
    private static final String VIEW_PATH = ".";

    /** chemin du fichier de configuration Macker */
    public static final String CONFIG_PATH = "data/Project4MackerTest/macker.xml";

    /** le nombre .class dans le r�pertoire */
    public static final int NB_CLASSES = 1;

    /** le nombre d'erreur trouv�es pour l'unique classe */
    public static final int NB_ERRORS = 4;

    /** l'application */
    private ApplicationBO mAppli = new ApplicationBO();

    /** le projet � auditer */
    private ProjectBO mProject = new ProjectBO();

    /** l'audit */
    private AuditBO mAudit = new AuditBO();

    /** les t�ches temporaires */
    private TaskData mData = new TaskData();

    /**
     * Constructeur pour MackerTaskTest.
     * 
     * @param arg0 nom
     */
    public MackerTaskTest( String arg0 )
    {
        super( arg0 );

    }

    /**
     * @see TestCase#setUp()
     */
    protected void setUp()
        throws Exception
    {
        super.setUp();
        getSession().beginTransaction();
        mAppli = getComponentFactory().createApplication( getSession() );
        QualityGridBO grid = getComponentFactory().createGrid( getSession() );
        mProject = getComponentFactory().createProject( getSession(), mAppli, grid );
        // Enregistrement du ProjectBO dans la base
        ProjectDAOImpl projectDAO = ProjectDAOImpl.getInstance();
        // Les param�tres doivent contenir le chemin du fichier de configuration
        MapParameterBO params = new MapParameterBO();
        MapParameterBO mackerParams = new MapParameterBO();
        Map mackerMap = new HashMap();
        StringParameterBO filePath = new StringParameterBO();
        filePath.setValue( CONFIG_PATH );
        mackerMap.put( ParametersConstants.MACKER_CONFIGURATION, filePath );
        mackerParams.setParameters( mackerMap );
        params.getParameters().put( ParametersConstants.MACKER, mackerParams );
        ListParameterBO listParam = new ListParameterBO();
        List list = new ArrayList();
        StringParameterBO stringParam = new StringParameterBO();
        stringParam.setValue( SOURCES_DIR );
        list.add( stringParam );
        listParam.setParameters( list );
        params.getParameters().put( ParametersConstants.SOURCES, listParam );
        ProjectParameterDAOImpl.getInstance().create( getSession(), params );
        mProject.setParameters( params );
        ProjectDAOImpl.getInstance().save( getSession(), mProject );
        mAudit = getComponentFactory().createAudit( this.getSession(), mProject );
        // On fait le commit pour permettre l'acc�s aux donn�es dans une autre session
        getSession().commitTransactionWithoutClose();
        List classesDirs = new ArrayList();
        classesDirs.add( CLASSES_DIR );
        mData.putData( TaskData.CLASSES_DIRS, classesDirs );
        File viewFile = new File( VIEW_PATH );
        mData.putData( TaskData.VIEW_PATH, viewFile.getCanonicalPath() );
        // Le classpath tempoiraire ne contient que le chemin vers les fichiers compil�s
        mData.putData( TaskData.CLASSPATH, CLASSES_DIR );
    }

    /**
     * V�rifie la correcte ex�cution de la t�che Macker.
     * 
     * @throws JrafDaoException si erreur
     * @throws IOException si erreur
     */
    public void testRun()
        throws JrafDaoException, IOException
    {
        MackerTask task = new MackerTask();
        task.setData( mData );
        task.setAuditId( new Long( mAudit.getId() ) );
        task.setProjectId( new Long( mProject.getId() ) );
        task.setApplicationId( new Long( mAppli.getId() ) );
        task.setStatus( AbstractTask.NOT_ATTEMPTED );
        task.run();
        assertEquals( AbstractTask.TERMINATED, task.getStatus() );
        try
        {
            // On v�rifie les r�sultats
            MeasureDAOImpl dao = MeasureDAOImpl.getInstance();
            assertEquals( 1, dao.count( getSession() ).intValue() );
            // On r�cup�re la mesure sauv�e
            Collection measures = dao.findAll( getSession() );
            assertEquals( 1, measures.size() );
            RuleCheckingTransgressionBO measure = (RuleCheckingTransgressionBO) measures.iterator().next();
            assertEquals( 1, measure.getMetrics().size() );
            int size = measure.getMetrics().size();
            measure.getMetrics().entrySet();
            Collection details = measure.getDetails();
            assertEquals( NB_ERRORS, details.size() );
            for ( Iterator it = details.iterator(); it.hasNext(); )
            {
                RuleCheckingTransgressionItemBO item = (RuleCheckingTransgressionItemBO) it.next();
                RuleBO rule = item.getRule();
                String message = item.getMessage();
                assertEquals( RuleFactory.SEVERITY, rule.getSeverity() );
            }
            assertEquals( "Simple example", measure.getRuleSet().getName() );
        }
        catch ( JrafDaoException e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }

    /**
     * V�rifie la correcte ex�cution de la t�che Macker.
     * 
     * @throws JrafDaoException si erreur
     * @throws IOException si erreur
     */
    public void testRunWithExclusion()
        throws JrafDaoException, IOException
    {
        // On cr�e les exclusion
        getSession().beginTransaction();
        MapParameterBO params = mProject.getParameters();

        // Les patterns � exclure
        ListParameterBO excludedList = new ListParameterBO();
        ArrayList eList = new ArrayList();
        StringParameterBO stringExclude = new StringParameterBO();
        stringExclude.setValue( "**/Print*/**" );
        eList.add( stringExclude );
        excludedList.setParameters( eList );
        params.getParameters().put( ParametersConstants.EXCLUDED_PATTERNS, excludedList );

        ProjectParameterDAOImpl.getInstance().create( getSession(), params );
        mProject.setParameters( params );
        ProjectDAOImpl.getInstance().save( getSession(), mProject );
        getSession().commitTransactionWithoutClose();
        MackerTask task = new MackerTask();
        task.setData( mData );
        task.setAuditId( new Long( mAudit.getId() ) );
        task.setProjectId( new Long( mProject.getId() ) );
        task.setApplicationId( new Long( mAppli.getId() ) );
        task.setStatus( AbstractTask.NOT_ATTEMPTED );
        task.run();
        assertEquals( AbstractTask.TERMINATED, task.getStatus() );
        try
        {
            // On v�rifie les r�sultats
            MeasureDAOImpl dao = MeasureDAOImpl.getInstance();
            assertEquals( 1, dao.count( getSession() ).intValue() );
            // On r�cup�re la mesure sauv�e
            Collection measures = dao.findAll( getSession() );
            assertEquals( 1, measures.size() );
            RuleCheckingTransgressionBO measure = (RuleCheckingTransgressionBO) measures.iterator().next();
            assertEquals( 1, measure.getMetrics().size() );
            int size = measure.getMetrics().size();
            measure.getMetrics().entrySet();
            Collection details = measure.getDetails();
            // 0 erreur car on a exclu l'unique classe
            assertEquals( 0, details.size() );
            assertEquals( "Simple example", measure.getRuleSet().getName() );
        }
        catch ( JrafDaoException e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }
}
