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
package org.squale.squalecommon.enterpriselayer.applicationcomponent.administration;

import java.util.ArrayList;

import org.squale.jraf.commons.exception.JrafEnterpriseException;
import org.squale.jraf.helper.AccessDelegateHelper;
import org.squale.jraf.spi.accessdelegate.IApplicationComponent;
import org.squale.jraf.spi.persistence.ISession;
import org.squale.squalecommon.SqualeTestCase;
import org.squale.squalecommon.datatransfertobject.component.ApplicationConfDTO;
import org.squale.squalecommon.datatransfertobject.component.AuditDTO;
import org.squale.squalecommon.datatransfertobject.component.ProjectConfDTO;
import org.squale.squalecommon.datatransfertobject.component.UserDTO;
import org.squale.squalecommon.datatransfertobject.transform.component.ApplicationConfTransform;
import org.squale.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import org.squale.squalecommon.enterpriselayer.businessobject.rule.QualityGridBO;

/**
 * @author M400841 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class ApplicationAdminApplicationComponentAccessTest
    extends SqualeTestCase
{

    /**
     * Test d'acc�s au componentAccess
     */
    public void testApplicationAdminApplicationComponentAccess()
    {
        // Teste si la construction de l'application component par AccessDelegateHelper
        IApplicationComponent appComponent;
        try
        {
            appComponent = AccessDelegateHelper.getInstance( "ApplicationAdmin" );
            assertNotNull( appComponent );
        }
        catch ( JrafEnterpriseException e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }

    /**
     * Test d'ajout de projet
     */
    public void testAddProject()
    {
        try
        {
            ApplicationBO application = getComponentFactory().createApplication( getSession() );
            // Teste si la methode est accessible par AccessDelegateHelper
            // et si l'objet renvoy� n'est pas nul
            IApplicationComponent appComponent;

            // Initialisation des parametres de la methode
            ProjectConfDTO projectConf = new ProjectConfDTO(); // � initialiser
            projectConf.setName( "project" );
            ApplicationConfDTO applicationConf = ApplicationConfTransform.bo2Dto( application, new ArrayList() );

            // Initialisation des parametres sous forme de tableaux d'objets
            Object[] paramIn = new Object[2];
            paramIn[0] = projectConf;
            paramIn[1] = applicationConf;

            // Execution de la methode
            appComponent = AccessDelegateHelper.getInstance( "ApplicationAdmin" );
            appComponent.execute( "addProject", paramIn );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }

    /**
     * Test d'enregistrement d'application
     */
    public void testSaveApplication()
    {
        // Teste si la methode est accessible par AccessDelegateHelper
        // et si l'objet renvoy� n'est pas nul
        IApplicationComponent appComponent;

        // Initialisation des parametres de la methode
        ApplicationConfDTO applicationConf = new ApplicationConfDTO(); // � initialiser

        // Initialisation des parametres sous forme de tableaux d'objets
        Object[] paramIn = new Object[1];
        paramIn[0] = applicationConf;

        // Execution de la methode
        try
        {
            appComponent = AccessDelegateHelper.getInstance( "ApplicationAdmin" );
            appComponent.execute( "saveApplication", paramIn );
        }
        catch ( JrafEnterpriseException e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }

    /**
     * Test de cr�ation d'une application
     */
    public void testCreateApplication()
    {
        // Teste si la methode est accessible par AccessDelegateHelper
        // et si l'objet renvoy� n'est pas nul
        IApplicationComponent appComponent;

        // Initialisation des parametres de la methode
        ApplicationConfDTO applicationConf = new ApplicationConfDTO(); // � initialiser
        UserDTO user = new UserDTO();
        applicationConf.setName( "squale1" );

        // Initialisation des parametres sous forme de tableaux d'objets
        Object[] paramIn = new Object[2];
        paramIn[0] = applicationConf;
        paramIn[1] = user;

        // Execution de la methode
        try
        {
            appComponent = AccessDelegateHelper.getInstance( "ApplicationAdmin" );
            appComponent.execute( "createApplication", paramIn );
            assertNotNull( "buzz" );
        }
        catch ( JrafEnterpriseException e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }

    /**
     * Test de sauvegarde d'un projet
     */
    public void testSaveProject()
    {
        // Teste si la methode est accessible par AccessDelegateHelper
        // et si l'objet renvoy� n'est pas nul
        IApplicationComponent appComponent;

        // Initialisation des parametres de la methode
        ProjectConfDTO projectConf = new ProjectConfDTO(); // � initialiser
        ApplicationConfDTO applicationConf = new ApplicationConfDTO(); // � initialiser

        // Initialisation des parametres sous forme de tableaux d'objets
        Object[] paramIn = new Object[2];
        paramIn[0] = projectConf;
        paramIn[1] = applicationConf;

        // Execution de la methode
        try
        {
            appComponent = AccessDelegateHelper.getInstance( "ApplicationAdmin" );
        }
        catch ( JrafEnterpriseException e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }

    /**
     * Test d'ajout d'un audit de jalon
     */
    public void testAddMilestone()
    {
        // Teste si la methode est accessible par AccessDelegateHelper
        // et si l'objet renvoy� n'est pas nul
        IApplicationComponent appComponent;

        // Initialisation des parametres de la methode
        ApplicationConfDTO applicationConf = new ApplicationConfDTO(); // � initialiser
        AuditDTO audit = new AuditDTO(); // � initialiser

        // Initialisation des parametres sous forme de tableaux d'objets
        Object[] paramIn = new Object[1];
        paramIn[0] = audit;

        // Execution de la methode
        try
        {
            appComponent = AccessDelegateHelper.getInstance( "ApplicationAdmin" );
            appComponent.execute( "addMilestone", paramIn );
        }
        catch ( JrafEnterpriseException e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }

    /**
     * Test d'obtention de la configuration d'une application
     */
    public void testGetApplicationConf()
    {
        try
        {
            ApplicationBO application = getComponentFactory().createApplication( getSession() );
            // Teste si la methode est accessible par AccessDelegateHelper
            // et si l'objet renvoy� n'est pas nul
            IApplicationComponent appComponent;

            // Initialisation du retour
            ApplicationConfDTO applicationConfOut;

            // Initialisation des parametres de la methode
            ApplicationConfDTO applicationConfIn = new ApplicationConfDTO(); // � initialiser
            applicationConfIn.setId( application.getId() );

            // Initialisation des parametres sous forme de tableaux d'objets
            Object[] paramIn = new Object[1];
            paramIn[0] = applicationConfIn;

            // Execution de la methode
            appComponent = AccessDelegateHelper.getInstance( "ApplicationAdmin" );
            Object conf = appComponent.execute( "getApplicationConf", paramIn );
            assertNotNull( conf );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }

}
