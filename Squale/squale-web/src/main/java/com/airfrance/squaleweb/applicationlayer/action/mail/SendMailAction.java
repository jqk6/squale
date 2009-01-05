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
package com.airfrance.squaleweb.applicationlayer.action.mail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.airfrance.jraf.helper.AccessDelegateHelper;
import com.airfrance.jraf.spi.accessdelegate.IApplicationComponent;
import com.airfrance.squalecommon.util.SqualeCommonConstants;
import com.airfrance.squalecommon.util.SqualeCommonUtils;
import com.airfrance.squalecommon.util.mail.IMailerProvider;
import com.airfrance.squalecommon.util.mail.MailerHelper;
import com.airfrance.squaleweb.applicationlayer.action.accessRights.AdminAction;
import com.airfrance.squaleweb.applicationlayer.formbean.component.ApplicationForm;
import com.airfrance.squaleweb.applicationlayer.formbean.mails.MailForm;
import com.airfrance.squaleweb.comparator.ComponentComparator;
import com.airfrance.squaleweb.resources.WebMessages;
import com.airfrance.squaleweb.transformer.mails.MailTransformer;
import com.airfrance.welcom.struts.bean.WActionForm;
import com.airfrance.welcom.struts.transformer.WTransformerFactory;

/**
 * 
 */
public class SendMailAction
    extends AdminAction
{

    /**
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward init( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                               HttpServletResponse pResponse )
    {
        ActionMessages errors = new ActionMessages();
        ActionForward forward = null;
        MailForm form = (MailForm) pForm;
        try
        {
            IApplicationComponent ac = AccessDelegateHelper.getInstance( "ApplicationAdmin" );
            Collection appliDtoColl = (Collection) ac.execute( "listAll" );
            // On trie la applications par ordre alphab�tique (sans tenit compte de la casse)
            ArrayList sortedAppliList = new ArrayList( appliDtoColl );
            Collections.sort( sortedAppliList, new ComponentComparator() );
            // Transformation en formulaire
            WTransformerFactory.objToForm( MailTransformer.class, ( (WActionForm) pForm ), sortedAppliList );
            forward = pMapping.findForward( "success" );
        }
        catch ( Exception e )
        {
            // Traitement factoris� des exceptions et transfert vers la page d'erreur
            handleException( e, errors, pRequest );
            saveMessages( pRequest, errors );
            forward = pMapping.findForward( "failure" );
        }
        return forward;
    }

    /**
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward sendMail( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                   HttpServletResponse pResponse )
    {
        // On envoie un email m�me aux utilisateurs d�sabonn�s car il ne s'agit pas d'un email automatique
        ActionMessages errors = new ActionMessages();
        ActionForward forward = null;
        // on enl�ve l'�l�ment qui permettait de ne pas v�rifier que les champs
        // �taient remplis suite � l'initialisation pr�alable n�cessaire
        pRequest.removeAttribute( "fromMenu" );
        try
        {

            /*
             * En attendant un refonte de la gestion de la v�rification des mails on affiche tous les mails des
             * gestionnaires pour chaque application
             */
            IApplicationComponent acLogin = AccessDelegateHelper.getInstance( "Login" );
            ActionMessage listEmailsMsg = new ActionMessage( "mail.send.list_emails_title" );
            errors.add( "listEmailsMsg", listEmailsMsg );

            MailForm mailForm = (MailForm) pForm;
            String appliName = mailForm.getAppliName();
            Collection applis = getApplisForEmail( appliName, mailForm.getApplicationFormsList().getList() );
            // on pr�fixe le sujet choisis par l'utilisateur par [Squale - <nom application>]
            // pour qu'un gestionnaire qui g�re plusieurs applications sache sur quelle application
            // le mail porte.
            String content =
                WebMessages.getString( pRequest, "mail.admin.start.content" ) + mailForm.getContent()
                    + WebMessages.getString( pRequest, "mail.admin.end.content" );
            // On envoie � toutes les applications ou � celle s�lectionn�e
            Long appliId = new Long( -1 );
            IMailerProvider mailer = MailerHelper.getMailerProvider();
            Iterator appliIt = applis.iterator();
            while ( appliIt.hasNext() )
            {
                // Pour chaque application, on envoie un mail aux managers
                ApplicationForm curAppli = (ApplicationForm) appliIt.next();
                appliId = new Long( curAppli.getId() );

                // On r�cup�re les mails des gestionnaires
                Collection managerEmails =
                    (Collection) acLogin.execute( "getManagersEmails", new Object[] { appliId, Boolean.TRUE } );
                // On ajoute au message
                listEmailsMsg =
                    new ActionMessage( "mail.send.list_emails_by_application", new Object[] {
                        curAppli.getApplicationName(), getFormattedEmails( managerEmails ) } );
                errors.add( "listEmailsMsg", listEmailsMsg );

                // On ajoute le message de confirmation
                String object = "[Squale - <" + curAppli.getApplicationName() + ">] " + mailForm.getObject();
                if ( !SqualeCommonUtils.notifyByEmail( mailer, SqualeCommonConstants.ONLY_MANAGERS, appliId, object,
                                                       content, true ) )
                {
                    // On le nom de l'application � la liste des non envoy�es pour construire le message
                    // d'erreur
                    ActionMessage error = new ActionMessage( "mail.admin.appli", curAppli.getApplicationName() );
                    errors.add( "errorMsg", error );
                }

            }
            if ( !errors.isEmpty() )
            {
                // On ajoute le titre pour la liste des applications non envoy�es
                ActionMessage errorTitle = new ActionMessage( "mail.admin.no.sent" );
                errors.add( "errorTitleMsg", errorTitle );
            }
            saveMessages( pRequest, errors );
            forward = pMapping.findForward( "success" );
        }
        catch ( Exception e )
        {
            // Traitement factoris� des exceptions et transfert vers la page d'erreur
            handleException( e, errors, pRequest );
            saveMessages( pRequest, errors );
            forward = pMapping.findForward( "failure" );
        }
        return forward;
    }

    /**
     * @param managerEmails les emails
     * @return les emails format�s sous la forme : email1, email2
     */
    private String getFormattedEmails( Collection managerEmails )
    {
        final String sep = ", ";
        String result = "";
        StringBuffer emails = new StringBuffer();
        for ( Iterator it = managerEmails.iterator(); it.hasNext(); )
        {
            emails.append( (String) it.next() );
            emails.append( sep );
        }
        result = emails.toString();
        if ( result.endsWith( sep ) )
        {
            int cutId = result.lastIndexOf( sep );
            result = result.substring( 0, cutId );
        }
        return result;
    }

    /**
     * @param appliName le nom de l'application s�lectionn�e
     * @param allApplis toutes les applications
     * @return les applications qui doivent recevoir l'email
     */
    private Collection getApplisForEmail( String appliName, List allApplis )
    {
        ArrayList applis = new ArrayList( 1 );
        Iterator allApplisIt = allApplis.iterator();
        if ( !MailForm.ALL_APPLICATIONS.equals( appliName ) )
        {
            // Il faut rechercher l'application
            while ( allApplisIt.hasNext() && applis.size() == 0 )
            {
                ApplicationForm appliForm = (ApplicationForm) ( allApplisIt.next() );
                if ( appliForm.getApplicationName().equals( appliName ) )
                {
                    applis.add( appliForm );
                }
            }
        }
        else
        {
            // On prend toutes les applications
            applis = new ArrayList( allApplis );
        }
        return applis;
    }
}