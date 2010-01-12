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
package org.squale.squaleweb.applicationlayer.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import org.squale.jraf.helper.AccessDelegateHelper;
import org.squale.jraf.spi.accessdelegate.IApplicationComponent;
import org.squale.squalecommon.datatransfertobject.component.UserDTO;
import org.squale.squalecommon.util.mail.MailException;
import org.squale.squalecommon.util.mail.MailerHelper;
import org.squale.squaleweb.applicationlayer.action.accessRights.DefaultAction;
import org.squale.squaleweb.applicationlayer.formbean.LogonBean;
import org.squale.squaleweb.applicationlayer.formbean.component.ApplicationForm;
import org.squale.squaleweb.applicationlayer.formbean.component.ProfileForm;
import org.squale.squaleweb.applicationlayer.formbean.component.UserForm;
import org.squale.squaleweb.resources.WebMessages;
import org.squale.squaleweb.transformer.LogonBeanTransformer;
import org.squale.squaleweb.transformer.UserTransformer;
import org.squale.welcom.struts.bean.WActionForm;
import org.squale.welcom.struts.transformer.WTransformerFactory;
import org.squale.welcom.struts.util.WConstants;

/**
 * Administration du compte de l'utilisateur Cette action permet � un utilisateur d'obtenir la liste des informations
 * rattach�es et leur modification
 */
public class ManageAccountAction
    extends DefaultAction
{

    /** Cl� pour r�cup�rer l'ancien email */
    public static final String OLD_EMAIL_KEY = "oldEmail";

    /**
     * Informations sur un utilisateur
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward detail( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                 HttpServletResponse pResponse )
    {

        ActionMessages errors = new ActionMessages();
        ActionForward forward = null;

        try
        {
            // On r�cup�re les informations du user connect�
            LogonBean logonBeanSecurity = (LogonBean) pRequest.getSession().getAttribute( WConstants.USER_KEY );
            // On r�cup�re les informations de l'utilisateur et remplit le formbean           
            UserForm form = (UserForm) pForm;
            form.setEmail(logonBeanSecurity.getEmail());
            form.setMatricule(logonBeanSecurity.getMatricule());
            form.setName(logonBeanSecurity.getUserName());
            form.setId(logonBeanSecurity.getId());
            form.setUnsubscribed(logonBeanSecurity.getUnsubscribed());
            form.setApplicationsList(logonBeanSecurity.getApplicationsList());
            form.setOnlyAdminApplicationsList(logonBeanSecurity.getInCreationList());
            
            Iterator profilesEntries = logonBeanSecurity.getProfilesFullApp().entrySet().iterator();
            form.setProfiles(new HashMap());
            while ( profilesEntries.hasNext() )
            {
            	Map.Entry entry = (Entry) profilesEntries.next();
            	ApplicationForm aForm = (ApplicationForm) entry.getKey();
            	String profileName = (String) entry.getValue();
            	ProfileForm prForm = new ProfileForm();
            	prForm.setName(profileName);
            	form.getProfiles().put(aForm, prForm);
            }
            
            forward = pMapping.findForward( "detail" );
        }
        catch ( Exception e )
        {
            handleException( e, errors, pRequest );
            saveMessages( pRequest, errors );
            forward = pMapping.findForward( "total_failure" );
        }
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );
        return ( forward );
    }

    /**
     * Mise � jour des informations de l'utilisateur
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward update( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                 HttpServletResponse pResponse )
    {

        ActionMessages errors = new ActionMessages();
        ActionForward forward = null;

        try
        {
            // R�cup�ration des informations
            UserDTO user = new UserDTO();
            WTransformerFactory.formToObj( UserTransformer.class, (WActionForm) pForm, user );
            // Les informations sur le matricule et l'id sont en session
            // pour des raisons de s�curit�
            LogonBean logonBeanSecurity = (LogonBean) pRequest.getSession().getAttribute( WConstants.USER_KEY );
            user.setMatricule( logonBeanSecurity.getMatricule() );
            user.setID( logonBeanSecurity.getId() );
            // Mise � jour des informations dans la base de donn�es
            IApplicationComponent ac = AccessDelegateHelper.getInstance( "Login" );
            Object[] paramIn = { user, Boolean.valueOf( logonBeanSecurity.isAdmin() ) };
            user = (UserDTO) ac.execute( "createOrUpdateUser", paramIn );
            // Mise � jour du formulaire
            WTransformerFactory.objToForm( UserTransformer.class, (WActionForm) pForm, user );
            // Mise � jour du LogonBean en cons�quence
            boolean isAdmin = logonBeanSecurity.isAdmin();
            logonBeanSecurity = new LogonBean();
            WTransformerFactory.formToObj( LogonBeanTransformer.class, (WActionForm) pForm, new Object[] {
                logonBeanSecurity, Boolean.valueOf( isAdmin ) } );
            pRequest.getSession().setAttribute( WConstants.USER_KEY, logonBeanSecurity );
            addConfirmationMessage( pForm, pRequest, errors );
            // Renvoi vers le d�tail
            forward = pMapping.findForward( "detail" );
        }
        catch ( Exception e )
        {
            handleException( e, errors, pRequest );
            saveMessages( pRequest, errors );
            forward = pMapping.findForward( "total_failure" );
        }
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );
        return ( forward );
    }

    /**
     * Permet d'ajouter un message de confirmation ou d'erreur
     * 
     * @param pForm le formulaire
     * @param pRequest la requ�te
     * @param pErrors les erreurs
     */
    private void addConfirmationMessage( ActionForm pForm, HttpServletRequest pRequest, ActionMessages pErrors )
    {
        // On r�cup�re l'ancien email
        String oldEmail = pRequest.getParameter( OLD_EMAIL_KEY );
        String newMail = ( (UserForm) pForm ).getEmail();
        // Si l'email a chang� et qu'il n'est pas vide
        if ( newMail.length() > 0 && !newMail.equals( oldEmail ) )
        {
            // On va essay� d'envoyer un email de confirmation.
            // Si le message n'aboutit pas, on indiquera � l'utilisateur de v�rifier son email
            // L'email est quand m�me modifi� car l'erreur d'envoi peut provenir de l'outil d'envoi d'email...
            try
            {
                String object = WebMessages.getString( pRequest, "mail.update.confirm.object" );
                String content = WebMessages.getString( pRequest, "mail.update.confirm.content" );
                // si l'envoie de mail produit une erreur, c'est probablement que l'adresse
                // qui a �t� fourni est invalide donc on affiche le message d'avertissement
                MailerHelper.getMailerProvider().sendMail( null, new String[] { newMail }, object, content );
            }
            catch ( MailException e )
            {
                ActionMessage error = new ActionMessage( "user_management.error.email" );
                pErrors.add( ActionMessages.GLOBAL_MESSAGE, error );
                saveMessages( pRequest, pErrors );
            }
        }
    }
}
