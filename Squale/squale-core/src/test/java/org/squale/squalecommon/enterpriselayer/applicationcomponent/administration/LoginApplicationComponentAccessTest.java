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

import java.util.Collection;

import org.squale.jraf.commons.exception.JrafEnterpriseException;
import org.squale.jraf.helper.AccessDelegateHelper;
import org.squale.jraf.spi.accessdelegate.IApplicationComponent;
import org.squale.squalecommon.SqualeTestCase;
import org.squale.squalecommon.daolayer.profile.ProfileDAOImpl;
import org.squale.squalecommon.daolayer.profile.UserDAOImpl;
import org.squale.squalecommon.datatransfertobject.component.UserDTO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;
import org.squale.squalecommon.enterpriselayer.businessobject.profile.ProfileBO;
import org.squale.squalecommon.enterpriselayer.businessobject.profile.UserBO;

/**
 * @author M400841 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class LoginApplicationComponentAccessTest
    extends SqualeTestCase
{
    /** administrateur */
    private UserBO mAdmin;

    /** manager */
    private UserBO mManager;

    /** profil manager */
    private ProfileBO mManagerProfile;

    /** lecteur */
    private UserBO mReader;

    /** profil lecteur */
    private ProfileBO mReaderProfile;

    /** dao pour les utilisateurs */
    private UserDAOImpl userDAO = UserDAOImpl.getInstance();

    /** L'application */
    private ApplicationBO mAppli;

    /**
     * {@inheritDoc}
     * 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp()
        throws Exception
    {
        super.setUp();
        getSession().beginTransaction();

        // On cr�e les profils
        ProfileDAOImpl profileDAO = ProfileDAOImpl.getInstance();
        mManagerProfile = new ProfileBO();
        mManagerProfile.setName( ProfileBO.MANAGER_PROFILE_NAME );
        mReaderProfile = new ProfileBO();
        mReaderProfile.setName( ProfileBO.READER_PROFILE_NAME );
        profileDAO.save( getSession(), mManagerProfile );
        profileDAO.save( getSession(), mReaderProfile );

        // On cr�e l'application
        mAppli = getComponentFactory().createApplication( getSession() );

        // On cr�e les utilisateurs
        mAdmin = getComponentFactory().createUser( getSession() );
        mManager = getComponentFactory().createUser( getSession() );
        mManager.setDefaultProfile( mManagerProfile );
        mManager.getRights().put( mAppli, mManagerProfile );
        mReader = getComponentFactory().createUser( getSession() );
        mReader.setDefaultProfile( mReaderProfile );
        mReader.getRights().put( mAppli, mReaderProfile );
        userDAO.save( getSession(), mManager );
        userDAO.save( getSession(), mReader );
        getSession().commitTransactionWithoutClose();
    }

    /**
     * Teste avec : - un matricule et mot de passe valide - un matricule valide et mot de passe non valide - un
     * matricule et mot de passe non valide
     */
    public void testVerifyUserWithGoodLogin()
    {
        try
        {
            getSession().beginTransaction();
            UserBO user = getComponentFactory().createUser( getSession() );
            user.setMatricule( "usermatricule" );
            userDAO.save( getSession(), user );
            getSession().commitTransactionWithoutClose();
            // Teste si la methode est accessible par AccessDelegateHelper
            // et si l'objet renvoy� n'est pas nul
            IApplicationComponent appComponent;

            // Execution de la methode avec un login et mot de passe valide
            UserDTO dto = new UserDTO();
            dto.setMatricule( user.getMatricule() );
            dto.setPassword( "" );
            Object[] paramIn = { dto, Boolean.TRUE };
            appComponent = AccessDelegateHelper.getInstance( "Login" );
            UserDTO out = (UserDTO) appComponent.execute( "verifyUser", paramIn );
            assertNotNull( out );
            assertEquals( user.getId(), out.getID() );
            // Execution de la methode avec un mot de passe non valide
            dto.setPassword( "robert" );
            appComponent = AccessDelegateHelper.getInstance( "Login" );
            out = (UserDTO) appComponent.execute( "verifyUser", paramIn );
            assertNotNull( out );
            assertEquals( user.getId(), out.getID() );

            // Execution de la methode avec un login et mot de passe valide
            dto.setMatricule( "robert" );
            appComponent = AccessDelegateHelper.getInstance( "Login" );
            out = (UserDTO) appComponent.execute( "verifyUser", paramIn );
            assertNotNull( out );
            assertTrue( "User guest should have another id", user.getId() != out.getID() );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }

    /**
     * Test sur l'accessibilite au composant
     * 
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public void testLoginApplicationComponentAccess()
        throws JrafEnterpriseException
    {
        // Teste si la construction de l'application component par AccessDelegateHelper
        IApplicationComponent appComponent;
        appComponent = AccessDelegateHelper.getInstance( "Login" );
        assertNotNull( appComponent );

    }

    /**
     * Obtention des utilisateurs avec le profil administrateur ayant un email
     */
    public void testGetAdminWithEmails()
    {
        try
        {
            // On r�cup�re les administrateurs
            IApplicationComponent appComponent;
            appComponent = AccessDelegateHelper.getInstance( "Login" );
            Object[] paramIn = { Boolean.FALSE };

            // L'administrateur n'a pas d'email --> 0 r�sultat
            Collection adminsWithEmail = (Collection) appComponent.execute( "getAdminsWithEmails", paramIn );
            assertEquals( 0, adminsWithEmail.size() );

            // On lui donne un email
            mAdmin.setEmail( "admin@junit.com" );
            // On le d�sabonne
            mAdmin.setUnsubscribed( true );
            getSession().beginTransaction();
            userDAO.save( getSession(), mAdmin );
            getSession().commitTransactionWithoutClose();
            // L'administrateur a un email mais est d�sabonn� --> 0 r�sultat
            adminsWithEmail = (Collection) appComponent.execute( "getAdminsWithEmails", paramIn );
            assertEquals( 0, adminsWithEmail.size() );

            // On appel l'application component en acceptant les d�sabonn�s
            paramIn[0] = Boolean.TRUE;
            // L'administrateur a un email --> 1 r�sultat
            adminsWithEmail = (Collection) appComponent.execute( "getAdminsWithEmails", paramIn );
            assertEquals( 1, adminsWithEmail.size() );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }

    /**
     * R�cup�ration des emails des utilisateurs ayant un certain profil sur une application en fonction de leur email et
     * de leur abonnement
     */
    public void testGetEmails()
    {
        try
        {
            // R�cup�ration de l'application component
            IApplicationComponent appComponent;
            appComponent = AccessDelegateHelper.getInstance( "Login" );
            Object[] paramIn = { new Long( mAppli.getId() ), Boolean.FALSE };

            // Le manager n'a pas d'email --> 0 r�sultat
            Collection managersWithEmail = (Collection) appComponent.execute( "getManagersEmails", paramIn );
            assertEquals( 0, managersWithEmail.size() );

            // Le lecteur n'a pas d'email --> 0 r�sultat
            Collection readersWithEmail = (Collection) appComponent.execute( "getReadersEmails", paramIn );
            assertEquals( 0, readersWithEmail.size() );

            // On donne un mail au manager et au lecteur et on les d�sabonne
            mManager.setEmail( "manager@junit.com" );
            mManager.setUnsubscribed( true );
            mReader.setEmail( "reader@junit.com" );
            mReader.setUnsubscribed( true );
            getSession().beginTransaction();
            userDAO.save( getSession(), mManager );
            userDAO.save( getSession(), mReader );
            getSession().commitTransactionWithoutClose();

            // Le manager a un email mais n'est pas abonn� --> 0 r�sultat
            managersWithEmail = (Collection) appComponent.execute( "getManagersEmails", paramIn );
            assertEquals( 0, managersWithEmail.size() );

            // On veut aussi les lecteurs d�sabonn�s
            paramIn[1] = Boolean.TRUE;

            // Le manager a un email --> 1 r�sultat
            readersWithEmail = (Collection) appComponent.execute( "getReadersEmails", paramIn );
            assertEquals( 1, readersWithEmail.size() );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }

    }
}
