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
package com.airfrance.squaleweb.applicationlayer.action.component.parameters;

import java.io.InputStream;

import com.airfrance.jraf.commons.exception.JrafEnterpriseException;
import com.airfrance.squalecommon.datatransfertobject.component.parameters.MapParameterDTO;
import com.airfrance.squalecommon.datatransfertobject.component.parameters.StringParameterDTO;
import com.airfrance.squalecommon.datatransfertobject.config.TaskDTO;
import com.airfrance.squalecommon.datatransfertobject.config.TaskParameterDTO;
import com.airfrance.squalecommon.datatransfertobject.rulechecking.PmdRuleSetDTO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import com.airfrance.squalecommon.enterpriselayer.businessobject.profile.UserBO;
import com.airfrance.squalecommon.enterpriselayer.facade.pmd.PmdFacade;
import com.airfrance.squaleweb.SqualeWebTestCase;
import com.airfrance.squaleweb.applicationlayer.formbean.component.parameters.PmdForm;
import com.airfrance.squaleweb.applicationlayer.formbean.creation.CreateProjectForm;

/**
 * Test de l'action de param�trage pmd d'un projet
 */
public class CreatePmdParametersActionTest
    extends SqualeWebTestCase
{

    /**
     * (non-Javadoc)
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp()
        throws Exception
    {
        super.setUp();
        // On cr�e une application
        ApplicationBO appli = getComponentFactory().createApplication( getJRAFSession() );
        UserBO user = getComponentFactory().createUser( getJRAFSession() );
        setupLogonBean( user.getMatricule(), true );
        addRequestParameter( "applicationId", "" + appli.getId() );
    }

    /**
     * Cr�ation d'un ruleset
     * 
     * @param pResourceName nom de la ressource � charger
     * @return ruleset correspondant
     * @throws JrafEnterpriseException si exception
     */
    protected PmdRuleSetDTO createRuleSet( String pResourceName )
        throws JrafEnterpriseException
    {
        InputStream stream = getClass().getClassLoader().getResourceAsStream( pResourceName );
        // Parsing du contenu du fichier
        StringBuffer errors = new StringBuffer();
        PmdRuleSetDTO versionRes = PmdFacade.importPmdConfig( stream, errors );
        return versionRes;
    }

    /**
     * Cr�ation du DTO de la t�che
     * 
     * @param pLanguages langages � inclure
     * @return t�che correspondante
     */
    protected TaskDTO createTaskDTO( String[] pLanguages )
    {
        TaskDTO task = new TaskDTO();
        task.setName( "PmdTask" );
        for ( int i = 0; i < pLanguages.length; i++ )
        {
            TaskParameterDTO taskParam = new TaskParameterDTO();
            taskParam.setName( "language" );
            taskParam.setValue( pLanguages[i] );
            task.addParameter( taskParam );
        }
        return task;
    }

    /**
     * Test de remplissage du formulaire de configuration Pmd en configuration java On charge plusieurs rulesets pmd de
     * m�me nom et on positionne les param�tres du projet sur le nom du ruleset Le formulaire en sortie doit contenir un
     * seul ruleset et ne doit pas contenir de donn�es jsp
     */
    public void testFillJava()
    {
        try
        {
            // Chargement du ruleset
            PmdRuleSetDTO ruleSet = createRuleSet( "config/pmd.xml" );
            // on charge une deuxi�me fois le ruleset
            ruleSet = createRuleSet( "config/pmd.xml" );

            // Positionnement des param�tres pour l'action
            StringParameterDTO dialect = new StringParameterDTO();
            dialect.setValue( ParametersConstants.JAVA1_4 );
            MapParameterDTO pmdParams = new MapParameterDTO();
            StringParameterDTO param = new StringParameterDTO();
            param.setValue( ruleSet.getName() );
            pmdParams.getParameters().put( ParametersConstants.PMD_JAVA_RULESET_NAME, param );
            CreateProjectForm form = new CreateProjectForm();
            form.getParameters().getParameters().put( ParametersConstants.DIALECT, dialect );
            form.getParameters().getParameters().put( ParametersConstants.PMD, pmdParams );
            form.getAdvancedTasks().add( createTaskDTO( new String[] { "java" } ) );
            addRequestParameter( "action", "fill" );
            getSession().setAttribute( "createProjectForm", form );

            setRequestPathInfo( "/PmdTask.do" );
            actionPerform();

            // V�rification des informations
            PmdForm pmdForm = (PmdForm) getSession().getAttribute( "pmdForm" );
            assertEquals( "un seul ruleset filtr� par son nom", 1, pmdForm.getJavaRuleSets().length );
            assertEquals( "le ruleset s�lectionn�", ruleSet.getName(), pmdForm.getSelectedJavaRuleSet() );
            assertFalse( "masque jsp d�sactiv�", pmdForm.isJspSourcesRequired() );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }

    /**
     * Test de remplissage du formulaire de configuration Pmd en configuration java et jsp On charge plusieurs rulesets
     * pmd de m�me nom et on positionne les param�tres du projet sur le nom du ruleset Le formulaire en sortie doit
     * contenir un seul ruleset java et un seul ruleset jsp
     */
    public void testFillJsp()
    {
        try
        {
            // Chargement du ruleset java
            PmdRuleSetDTO javaRuleSet = createRuleSet( "config/pmd.xml" );
            // on charge une deuxi�me fois le ruleset
            javaRuleSet = createRuleSet( "config/pmd.xml" );
            // Chargement du ruleset jsp
            PmdRuleSetDTO jspRuleSet = createRuleSet( "config/pmd_jsp.xml" );
            // on charge une deuxi�me fois le ruleset
            jspRuleSet = createRuleSet( "config/pmd_jsp.xml" );

            // Positionnement des param�tres pour l'action
            StringParameterDTO dialect = new StringParameterDTO();
            dialect.setValue( ParametersConstants.JAVA1_4 );
            MapParameterDTO pmdParams = new MapParameterDTO();
            StringParameterDTO param = new StringParameterDTO();
            param.setValue( javaRuleSet.getName() );
            pmdParams.getParameters().put( ParametersConstants.PMD_JAVA_RULESET_NAME, param );
            param = new StringParameterDTO();
            param.setValue( jspRuleSet.getName() );
            pmdParams.getParameters().put( ParametersConstants.PMD_JSP_RULESET_NAME, param );
            CreateProjectForm form = new CreateProjectForm();
            form.getParameters().getParameters().put( ParametersConstants.DIALECT, dialect );
            form.getParameters().getParameters().put( ParametersConstants.PMD, pmdParams );
            form.getAdvancedTasks().add( createTaskDTO( new String[] { "java", "jsp" } ) );
            addRequestParameter( "action", "fill" );
            getSession().setAttribute( "createProjectForm", form );

            setRequestPathInfo( "/PmdTask.do" );
            actionPerform();

            // V�rification des informations
            PmdForm pmdForm = (PmdForm) getSession().getAttribute( "pmdForm" );
            assertEquals( "un seul ruleset java filtr� par son nom", 1, pmdForm.getJavaRuleSets().length );
            assertEquals( "le ruleset s�lectionn�", javaRuleSet.getName(), pmdForm.getSelectedJavaRuleSet() );
            assertTrue( "masque jsp activ�", pmdForm.isJspSourcesRequired() );
            assertEquals( "un seul ruleset jsp filtr� par son nom", 1, pmdForm.getJspRuleSets().length );
            assertEquals( "le ruleset s�lectionn�", jspRuleSet.getName(), pmdForm.getSelectedJspRuleSet() );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }

    /**
     * Test d'ajout de la configuration Pmd dans un contexte java On positionne les param�tres avec les sources et le
     * ruleset pmd/java En sortie on doit obtenir les param�tres du projet en cons�quence
     */
    public void testAddProjectPmdJava()
    {
        try
        {
            // On positionne les param�tres de la la session
            String javaRuleSetName = "javaruleset";
            PmdForm pmdForm = new PmdForm();
            pmdForm.setSelectedJavaRuleSet( javaRuleSetName );
            getSession().setAttribute( "pmdForm", pmdForm );

            addRequestParameter( "action", "addParameters" );
            CreateProjectForm form = new CreateProjectForm();
            getSession().setAttribute( "createProjectForm", form );
            setRequestPathInfo( "/add_project_pmd_config.do" );

            actionPerform();
            verifyForward( "configure" );

            // V�rification des informations positionn�es sur le projet
            MapParameterDTO params = form.getParameters();
            MapParameterDTO pmdParams = (MapParameterDTO) params.getParameters().get( ParametersConstants.PMD );
            assertNotNull( "map PMD devrait �tre renseign�e", pmdParams );
            StringParameterDTO javaParam =
                (StringParameterDTO) pmdParams.getParameters().get( ParametersConstants.PMD_JAVA_RULESET_NAME );
            assertNotNull( "ruleset renseign�", javaParam );
            assertEquals( "ruleset", javaRuleSetName, javaParam.getValue() );
            StringParameterDTO jspParam =
                (StringParameterDTO) pmdParams.getParameters().get( ParametersConstants.PMD_JSP_RULESET_NAME );
            assertNull( "le param�tre jsp ne devrait pas �tre renseign�", jspParam );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }

    /**
     * Test d'ajout de la configuration Pmd dans un contexte java/jsp On positionne les param�tres avec les sources et
     * le ruleset pmd/jsp et java En sortie on doit obtenir les param�tres du projet en cons�quence
     */
    public void testAddProjectJspJava()
    {
        try
        {
            // On positionne les param�tres de la session
            String javaRuleSetName = "javaruleset";
            String jspRuleSetName = "jspruleset";
            PmdForm pmdForm = new PmdForm();
            // On force le masque jsp
            pmdForm.setJspSourcesRequired( true );
            pmdForm.setSelectedJavaRuleSet( javaRuleSetName );
            pmdForm.setSelectedJspRuleSet( jspRuleSetName );
            getSession().setAttribute( "pmdForm", pmdForm );
            addRequestParameter( "action", "addParameters" );
            CreateProjectForm form = new CreateProjectForm();
            getSession().setAttribute( "createProjectForm", form );
            setRequestPathInfo( "/add_project_pmd_config.do" );

            actionPerform();
            verifyForward( "configure" );

            // V�rification des informations positionn�es sur le projet
            MapParameterDTO params = form.getParameters();
            MapParameterDTO pmdParams = (MapParameterDTO) params.getParameters().get( ParametersConstants.PMD );
            assertNotNull( "map PMD devrait �tre renseign�e", pmdParams );
            StringParameterDTO javaParam =
                (StringParameterDTO) pmdParams.getParameters().get( ParametersConstants.PMD_JAVA_RULESET_NAME );
            assertNotNull( "ruleset java renseign�", javaParam );
            assertEquals( "ruleset java", javaRuleSetName, javaParam.getValue() );
            StringParameterDTO jspParam =
                (StringParameterDTO) pmdParams.getParameters().get( ParametersConstants.PMD_JSP_RULESET_NAME );
            assertNotNull( "ruleset jsp renseign�", jspParam );
            assertEquals( "ruleset jsp", jspRuleSetName, jspParam.getValue() );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }
}
