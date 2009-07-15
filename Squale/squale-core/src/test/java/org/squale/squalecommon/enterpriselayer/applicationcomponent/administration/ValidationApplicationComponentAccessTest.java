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
import java.util.Collection;

import org.squale.jraf.commons.exception.JrafEnterpriseException;
import org.squale.jraf.helper.AccessDelegateHelper;
import org.squale.jraf.spi.accessdelegate.IApplicationComponent;
import org.squale.squalecommon.SqualeTestCase;

/**
 * @author M400841 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class ValidationApplicationComponentAccessTest
    extends SqualeTestCase
{

    /**
     * teste la validation d'un composant de niveau application
     * 
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public void testValidationApplicationComponentAccess()
        throws JrafEnterpriseException
    {
        // Teste si la construction de l'application component par AccessDelegateHelper
        IApplicationComponent appComponent;
        // Execution de la methode
        appComponent = AccessDelegateHelper.getInstance( "Validation" );
        assertNotNull( appComponent );
    }

    /**
     * Teste sur la suppression d'une application venant d'etre cr��e
     * 
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public void testRemoveApplicationsCreation()
        throws JrafEnterpriseException
    {
        // Teste si la methode est accessible par AccessDelegateHelper
        // et si l'objet renvoy� n'est pas nul
        IApplicationComponent appComponent;

        // Initialisation des parametres de la methode
        Collection applicationsToRemove = new ArrayList(); // � initialiser

        // Initialisation des parametres sous forme de tableaux d'objets
        Object[] paramIn = new Object[1];
        paramIn[0] = applicationsToRemove;

        // Execution de la methode
        appComponent = AccessDelegateHelper.getInstance( "Validation" );
        appComponent.execute( "removeApplicationsCreation", paramIn );

    }

    /**
     * teste l'enregistrement d'une application venant d'etre cr��e
     * 
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public void testValidateApplicationsCreation()
        throws JrafEnterpriseException
    {
        // Teste si la methode est accessible par AccessDelegateHelper
        // et si l'objet renvoy� n'est pas nul
        IApplicationComponent appComponent;

        // Initialisation des parametres de la methode
        Collection applicationsToValidate = new ArrayList(); // � initialiser

        // Initialisation des parametres sous forme de tableaux d'objets
        Object[] paramIn = new Object[1];
        paramIn[0] = applicationsToValidate;

        // Execution de la methode
        appComponent = AccessDelegateHelper.getInstance( "Validation" );
        appComponent.execute( "validateApplicationsCreation", paramIn );
    }

    /**
     * test la r�cup�ration d'une application cr��e
     * 
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public void testGetApplicationsCreated()
        throws JrafEnterpriseException
    {
        // Teste si la methode est accessible par AccessDelegateHelper
        // et si l'objet renvoy� n'est pas nul
        IApplicationComponent appComponent;

        // Execution de la methode
        appComponent = AccessDelegateHelper.getInstance( "Validation" );
        appComponent.execute( "getApplicationsCreated" );

    }

    /**
     * teste la suppression des r�f�rences sur une application
     * 
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public void testRemoveApplicationsReference()
        throws JrafEnterpriseException
    {
        // Teste si la methode est accessible par AccessDelegateHelper
        // et si l'objet renvoy� n'est pas nul
        IApplicationComponent appComponent;

        // Initialisation des parametres de la methode
        Collection applicationsToRemove = new ArrayList(); // � initialiser

        // Initialisation des parametres sous forme de tableaux d'objets
        Object[] paramIn = new Object[1];
        paramIn[0] = applicationsToRemove;

        // Execution de la methode
        appComponent = AccessDelegateHelper.getInstance( "Validation" );
        appComponent.execute( "removeApplicationsReference", paramIn );
    }

    /**
     * Teste la r�cup�ration des r�f�rences d'une application
     * 
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public void tesGetReference()
        throws JrafEnterpriseException
    {
        // Teste si la methode est accessible par AccessDelegateHelper
        // et si l'objet renvoy� n'est pas nul
        IApplicationComponent appComponent;

        // Initialisation du retour de la methode
        Collection references = new ArrayList();

        // Initialisation des parametres de la methode
        Integer nbLignes = new Integer( 20 ); // � initialiser
        Integer indexDepart = new Integer( 0 ); // � initialiser

        // Initialisation des parametres sous forme de tableaux d'objets
        Object[] paramIn = new Object[2];
        paramIn[0] = nbLignes;
        paramIn[1] = indexDepart;
        // utilisateur admin
        paramIn[2] = new Boolean( true );
        paramIn[3] = new ArrayList( 0 );
        // Execution de la methode
        appComponent = AccessDelegateHelper.getInstance( "Results" );
        appComponent.execute( "getReference", paramIn );
    }

}
