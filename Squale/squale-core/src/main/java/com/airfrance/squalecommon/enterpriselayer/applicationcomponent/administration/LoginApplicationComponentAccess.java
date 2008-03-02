//Source file: D:\\CC_VIEWS\\SQUALE_V0_0_ACT\\SQUALE\\SRC\\squaleCommon\\src\\com\\airfrance\\squalecommon\\enterpriselayer\\applicationcomponent\\LoginApplicationComponentAccess.java

package com.airfrance.squalecommon.enterpriselayer.applicationcomponent.administration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.airfrance.jraf.commons.exception.JrafEnterpriseException;
import com.airfrance.jraf.provider.accessdelegate.DefaultExecuteComponent;
import com.airfrance.squalecommon.datatransfertobject.component.UserDTO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.profile.ProfileBO;
import com.airfrance.squalecommon.enterpriselayer.facade.component.UserFacade;

/**
 * <p>
 * Title : LoginApplicationComponentAccess.java
 * </p>
 * <p>
 * Description : Application component login
 * </p>
 * <p>
 * Copyright : Copyright (c) 2005
 * </p>
 * <p>
 * Company : AIRFRANCE
 * </p>
 */
public class LoginApplicationComponentAccess
    extends DefaultExecuteComponent
{

    /**
     * Permet d'authentifier une utilisateur au sein d'AF V�rifie aussi si l'utilisateur est administrateur portail
     * 
     * @param pUser objet UserDTO avec login et mot de passe � v�rifier
     * @param pAdmin indique si l'utilisateur a un privil�ge administrateur
     * @return <code>true</code> si l'utilisateur est correctement authentifi�, sinon <code>false</code>
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFC0001EE
     */
    public UserDTO verifyUser( UserDTO pUser, Boolean pAdmin )
        throws JrafEnterpriseException
    {

        UserDTO userDTO = null; // Initialisation du retour
        userDTO = UserFacade.getUser( pUser, pAdmin );
        return userDTO;
    }

    public UserDTO userAuthentication( UserDTO pUser )
        throws JrafEnterpriseException
    {

        UserDTO userDTO = null; // Initialisation du retour
        userDTO = UserFacade.getUserByMatriculeAndPassword( pUser );
        return userDTO;
    }

    /**
     * Permet de mettre � jour les informations d'un utilisateur Cr�e l'utilisateur dans la base de donn�es le cas
     * �ch�ant
     * 
     * @param pUser objet UserDTO avec nom et email �ventuellementy mis � jour
     * @param pAdmin indique si l'utilisateur a un privil�ge administrateur
     * @return <code>true</code> si l'utilisateur est correctement authentifi�, sinon <code>false</code>
     * @throws JrafEnterpriseException exception JRAF
     */
    public UserDTO createOrUpdateUser( UserDTO pUser, Boolean pAdmin )
        throws JrafEnterpriseException
    {

        UserDTO userDTO = null; // Initialisation du retour
        userDTO = UserFacade.createOrUpdateUser( pUser, pAdmin );
        return userDTO;
    }

    /**
     * Obtention des utilisateurs avec le profil administrateur
     * 
     * @param pUnsubscribed true si on veut r�cup�rer aussi les utilisateur qui se sont d�sabonn�s de l'envoi
     *            automatique d'email.
     * @return utilisateurs avec le profil administrateur
     * @throws JrafEnterpriseException en cas de pb JRAF
     */
    public Collection getAdminsWithEmails( Boolean pUnsubscribed )
        throws JrafEnterpriseException
    {
        return UserFacade.getAdminsWithEmails( pUnsubscribed.booleanValue() );
    }

    /**
     * @param pUnsubscribed true si on veut r�cup�rer aussi les utilisateur qui se sont d�sabonn�s de l'envoi
     *            automatique d'email.
     * @return les emails des utilisateurs avec le profil manager pour l'application concern�e
     * @throws JrafEnterpriseException en cas de pb JRAF
     */
    public Collection getAdminsEmails( Boolean pUnsubscribed )
        throws JrafEnterpriseException
    {
        return getEmails( getAdminsWithEmails( pUnsubscribed ) );
    }

    /**
     * Obtention des utilisateurs avec le profil manager pour l'application
     * 
     * @param pId l'application concern�e
     * @param pUnsubscribed true si on veut r�cup�rer aussi les utilisateur qui se sont d�sabonn�s de l'envoi
     *            automatique d'email.
     * @return utilisateurs avec le profil manager pour l'application concern�e
     * @throws JrafEnterpriseException en cas de pb JRAF
     */
    public Collection getManagersWithEmails( Long pId, Boolean pUnsubscribed )
        throws JrafEnterpriseException
    {
        return UserFacade.getUsersWithEmails( pId, ProfileBO.MANAGER_PROFILE_NAME, pUnsubscribed.booleanValue() );
    }

    /**
     * @param pId l'application concern�e
     * @param pUnsubscribed true si on veut r�cup�rer aussi les utilisateur qui se sont d�sabonn�s de l'envoi
     *            automatique d'email.
     * @return les emails des utilisateurs avec le profil manager pour l'application concern�e
     * @throws JrafEnterpriseException en cas de pb JRAF
     */
    public Collection getManagersEmails( Long pId, Boolean pUnsubscribed )
        throws JrafEnterpriseException
    {
        return getEmails( getManagersWithEmails( pId, pUnsubscribed ) );
    }

    /**
     * Obtention des utilisateurs avec le profil manager pour l'application
     * 
     * @param pId l'application concern�e
     * @param pUnsubscribed true si on veut r�cup�rer aussi les utilisateur qui se sont d�sabonn�s de l'envoi
     *            automatique d'email.
     * @return utilisateurs avec le profil manager pour l'application concern�e
     * @throws JrafEnterpriseException en cas de pb JRAF
     */
    public Collection getReadersWithEmails( Long pId, Boolean pUnsubscribed )
        throws JrafEnterpriseException
    {
        return UserFacade.getUsersWithEmails( pId, ProfileBO.READER_PROFILE_NAME, pUnsubscribed.booleanValue() );
    }

    /**
     * @param pId l'application concern�e
     * @param pUnsubscribed true si on veut r�cup�rer aussi les utilisateur qui se sont d�sabonn�s de l'envoi
     *            automatique d'email.
     * @return les emails des utilisateurs avec le profil manager pour l'application concern�e
     * @throws JrafEnterpriseException en cas de pb JRAF
     */
    public Collection getReadersEmails( Long pId, Boolean pUnsubscribed )
        throws JrafEnterpriseException
    {
        return getEmails( getReadersWithEmails( pId, pUnsubscribed ) );
    }

    /**
     * r�cup�re une liste d'emails sur une collection d'utilisateurs
     * 
     * @param collUsers la collection des utilisateurs
     * @return la liste des mails d�fini pour ces utilisateurs
     */
    private Collection getEmails( Collection collUsers )
    {
        Collection result = new ArrayList( 0 );
        Iterator it = collUsers.iterator();
        while ( it.hasNext() )
        {
            result.add( ( (UserDTO) it.next() ).getEmail() );
        }
        return result;
    }

    /**
     * Constructeur par d�faut
     * 
     * @roseuid 42CBFC00039C
     */
    public LoginApplicationComponentAccess()
    {
    }
}
