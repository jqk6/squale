package com.airfrance.squalecommon.enterpriselayer.applicationcomponent.administration;

import java.util.ArrayList;
import java.util.Collection;

import com.airfrance.jraf.commons.exception.JrafEnterpriseException;
import com.airfrance.jraf.helper.AccessDelegateHelper;
import com.airfrance.jraf.spi.accessdelegate.IApplicationComponent;
import com.airfrance.squalecommon.SqualeTestCase;

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
