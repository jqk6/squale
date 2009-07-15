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
package com.airfrance.squaleweb.applicationlayer.action.component;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.airfrance.jraf.commons.exception.JrafEnterpriseException;
import com.airfrance.jraf.helper.AccessDelegateHelper;
import com.airfrance.jraf.spi.accessdelegate.IApplicationComponent;
import com.airfrance.squalecommon.datatransfertobject.component.ApplicationConfDTO;
import com.airfrance.squalecommon.datatransfertobject.component.AuditDTO;
import com.airfrance.squalecommon.datatransfertobject.component.ProjectConfDTO;
import com.airfrance.squalecommon.datatransfertobject.component.UserDTO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.profile.ProfileBO;
import com.airfrance.squalecommon.util.SqualeCommonConstants;
import com.airfrance.squalecommon.util.SqualeCommonUtils;
import com.airfrance.squalecommon.util.mail.MailerHelper;
import com.airfrance.squaleweb.applicationlayer.action.ActionUtils;
import com.airfrance.squaleweb.applicationlayer.action.accessRights.ReaderAction;
import com.airfrance.squaleweb.applicationlayer.formbean.LogonBean;
import com.airfrance.squaleweb.applicationlayer.formbean.component.AuditForm;
import com.airfrance.squaleweb.applicationlayer.formbean.config.ServeurListForm;
import com.airfrance.squaleweb.applicationlayer.formbean.creation.ApplicationRightsForm;
import com.airfrance.squaleweb.applicationlayer.formbean.creation.CreateApplicationForm;
import com.airfrance.squaleweb.applicationlayer.formbean.creation.CreateProjectForm;
import com.airfrance.squaleweb.connection.UserBeanAccessorHelper;
import com.airfrance.squaleweb.resources.WebMessages;
import com.airfrance.squaleweb.transformer.ApplicationConfTransformer;
import com.airfrance.squaleweb.transformer.AuditTransformer;
import com.airfrance.squaleweb.transformer.ProjectConfTransformer;
import com.airfrance.squaleweb.transformer.ServeurListTransformer;
import com.airfrance.squaleweb.util.InputFieldDataChecker;
import com.airfrance.squaleweb.util.SqualeWebActionUtils;
import com.airfrance.welcom.struts.ajax.WHttpEasyCompleteResponse;
import com.airfrance.welcom.struts.bean.WActionForm;
import com.airfrance.welcom.struts.easycomplete.WEasyCompleteUtil;
import com.airfrance.welcom.struts.transformer.WTransformerException;
import com.airfrance.welcom.struts.transformer.WTransformerFactory;
import com.airfrance.welcom.struts.util.WConstants;

/**
 * Struts Action used to handle applications.
 */
public class ManageApplicationAction
    extends ReaderAction
{

    /**
     * Action called to configure the application.
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward config( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                 HttpServletResponse pResponse )
    {

        ActionMessages errors = new ActionMessages();
        ActionForward forward = null;

        try
        {
            // On r�cup�re l'aplication courante
            CreateApplicationForm application = (CreateApplicationForm) pForm;
            validateConfig( pForm, errors );
            // Renvoi vers la page d'attribution des droits
            forward = pMapping.findForward( "add_rights" );
            if ( pRequest.getParameter( "modification" ) != null && errors.isEmpty() )
            {
                // Obtention de l'AC
                IApplicationComponent ac = AccessDelegateHelper.getInstance( "ApplicationAdmin" );
                ApplicationConfDTO applicationDTO =
                    (ApplicationConfDTO) WTransformerFactory.formToObj( ApplicationConfTransformer.class, application )[0];

                // On change le nom de l'utilisateur et la date de derni�re modification
                applicationDTO.setLastUser( ( (LogonBean) pRequest.getSession().getAttribute( WConstants.USER_KEY ) ).getMatricule() );
                applicationDTO.setLastUpdate( Calendar.getInstance().getTime() );
                Object[] paramIn = { applicationDTO };
                forward = pMapping.findForward( "application_summary" );
                if ( ( (Integer) ac.execute( "saveApplication", paramIn ) ).intValue() != 0 )
                {
                    // Renvoi d'un message g�n�rique suite � l'�chec de sauvegarde de l'application
                    ActionMessage error = new ActionMessage( "error.application_not_saved" );
                    errors.add( ActionMessages.GLOBAL_MESSAGE, error );
                    saveMessages( pRequest, errors );
                    forward = pMapping.findForward( "total_failure" );
                }
                else
                {
                    // l'application est recharg�e afin de mettre � jour le nom du serveur
                    applicationDTO = (ApplicationConfDTO) ac.execute( "getApplicationConf", paramIn );
                    application =
                        (CreateApplicationForm) WTransformerFactory.objToForm( ApplicationConfTransformer.class,
                                                                               applicationDTO );
                    pRequest.getSession().setAttribute( "createApplicationForm", application );
                    loadLastBranchAuditInSession( pRequest, applicationDTO.getId() );
                }
                ActionUtils.refreshUser( pRequest );
            }
        }
        catch ( Exception e )
        {
            // Traitement factoris� des exceptions
            handleException( e, errors, pRequest );
            saveMessages( pRequest, errors );
            // Transfert vers la page d'erreur
            forward = pMapping.findForward( "total_failure" );
        }
        if ( !errors.isEmpty() )
        {
            // On sauvegarde les erreurs pour les afficher sur la page initiale
            saveMessages( pRequest, errors );
            // On renvoie vers la page initiale avec message d'erreur
            try
            {
                IApplicationComponent ac = AccessDelegateHelper.getInstance( "Serveur" );
                Collection lListeServeurDTO = (Collection) ac.execute( "listeServeurs" );
                ServeurListForm lListeServeurForm = new ServeurListForm();
                WTransformerFactory.objToForm( ServeurListTransformer.class, lListeServeurForm, lListeServeurDTO );
                pRequest.setAttribute( "listeServeur", lListeServeurForm );
                forward = pMapping.findForward( "config_application" );
            }
            catch ( Exception e )
            {
                // Traitement factoris� des exceptions
                handleException( e, errors, pRequest );
            }
        }
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );

        return forward;
    }

    /**
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward configAndForward( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                           HttpServletResponse pResponse )
    {

        ActionMessages errors = new ActionMessages();
        ActionForward forward = null;

        try
        {
            validateConfig( pForm, errors );
            forward = pMapping.findForward( "add_rights" );
        }
        catch ( Exception e )
        {
            // Traitement factoris� des exceptions et transfert vers la page d'erreur
            handleException( e, errors, pRequest );
            saveMessages( pRequest, errors );
            forward = pMapping.findForward( "total_failure" );
        }
        if ( !errors.isEmpty() )
        {
            // On sauvegarde les erreurs pour les afficher sur la page initiale
            saveMessages( pRequest, errors );
            // On renvoie vers la page initiale avec message d'erreur
            try
            {
                IApplicationComponent ac = AccessDelegateHelper.getInstance( "Serveur" );
                Collection lListeServeurDTO = (Collection) ac.execute( "listeServeurs" );
                ServeurListForm lListeServeurForm = new ServeurListForm();
                WTransformerFactory.objToForm( ServeurListTransformer.class, lListeServeurForm, lListeServeurDTO );
                pRequest.setAttribute( "listeServeur", lListeServeurForm );

                forward = pMapping.findForward( "config_application" );
            }
            catch ( Exception e )
            {
                // Factorisation du traitement des exceptions
                handleException( e, errors, pRequest );
            }
        }
        return forward;
    }

    /**
     * @param pForm le formulaire
     * @param pErrors les erreurs
     */
    private void validateConfig( ActionForm pForm, ActionMessages pErrors )
    {
        // On r�cup�re l'aplication courante
        CreateApplicationForm application = (CreateApplicationForm) pForm;
        // V�rification de la validit� de la saisie en fonction de l'audit
        if ( ( !application.isMilestone() && application.getAuditFrequency() < 1 ) || application.getPurgeDelay() < 1 )
        {
            ActionMessage error = null;
            // On r�affecte la valeur par d�faut au champ qui n'est pas bien rempli
            if ( application.getAuditFrequency() < 1 )
            {
                error = new ActionMessage( "error.invalid_audit_frequency" );
                application.setAuditFrequency( CreateApplicationForm.DEFAULT_AUDIT_FREQUENCY );
                pErrors.add( ActionMessages.GLOBAL_MESSAGE, error );
            } // Eventuellement les 2
            if ( application.getPurgeDelay() < 1 )
            {
                application.setPurgeDelay( CreateApplicationForm.DEFAULT_PURGE_DELAY );
                error = new ActionMessage( "error.invalid_purge_delay" );
                pErrors.add( ActionMessages.GLOBAL_MESSAGE, error );
            }
        }
        // V�rification de la validit� de saisie du site
        if ( application.getServeurForm().getServeurId() == 0 )
        {
            // Le site est un obligatoire --> erreur si pas renseign�
            ActionMessage error = new ActionMessage( "error.invalid_site" );
            pErrors.add( ActionMessages.GLOBAL_MESSAGE, error );
        }
        // v�rification que la fr�quence de purge est sup�rieur � celle d'audits de suivi
        if ( application.getPurgeDelay() < application.getAuditFrequency() )
        {
            ActionMessage error = new ActionMessage( "error.purge_frequency" );
            pErrors.add( ActionMessages.GLOBAL_MESSAGE, error );
        }
    }

    /**
     * Finalise la modification du projet en base de donn�es, apr�s avoir ajout� les utilisateurs.
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward addRights( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                    HttpServletResponse pResponse )
    {

        ActionMessages errors = new ActionMessages();
        ActionForward forward = null;
        // Cette action est la derni�re � �tre appel�e apr�s
        // chaque �tape de configuration d'application
        // Elle r�alise la sauvegarde dans la base des donn�es collect�es
        try
        {
            ApplicationRightsForm form = (ApplicationRightsForm) pForm;
            CreateApplicationForm createApplicationForm =
                (CreateApplicationForm) pRequest.getSession().getAttribute( "createApplicationForm" );
            // R�cup�ration de la liste des utilisateurs dans le formulaire
            HashMap users = new HashMap();
            boolean hasManager = false;
            for ( int i = 0; null != form.getMatricule() && i < form.getMatricule().length; i++ )
            {
                // On v�rifie que l'utilisateur � ajouter poss�de un matricule valide
                String matricule = form.getMatricule()[i].trim();
                if ( matricule.length() > 0 )
                {
                    if ( InputFieldDataChecker.USER_ID.check( matricule ) )
                    {
                        String right = form.getRightProfile()[i];
                        if ( ProfileBO.MANAGER_PROFILE_NAME.equals( right ) )
                        {
                            hasManager = true;
                        }
                        // On v�rifie que l'utilisateur n'existe pas d�j� avec des droits diff�rents
                        String userRight = (String) users.get( matricule );
                        if ( userRight != null && !userRight.equals( right ) )
                        {
                            // Erreur de configuration
                            ActionMessage error = new ActionMessage( "error.application_more_than_one_right" );
                            errors.add( ActionMessages.GLOBAL_MESSAGE, error );
                            saveMessages( pRequest, errors );
                            forward = pMapping.findForward( "failure" );
                        }
                        users.put( matricule, right );
                    }
                    else
                    {
                        // Message d'erreur
                        ActionMessage error = new ActionMessage( "error.application.not_valid_user", matricule );
                        errors.add( ActionMessages.GLOBAL_MESSAGE, error );
                        saveMessages( pRequest, errors );
                        forward = pMapping.findForward( "failure" );
                    }
                }
            }
            if ( !hasManager )
            {
                // Message d'erreur
                ActionMessage error = new ActionMessage( "error.application_without_manager" );
                errors.add( ActionMessages.GLOBAL_MESSAGE, error );
                saveMessages( pRequest, errors );
                forward = pMapping.findForward( "failure" );
            }
            // On nettoie les matricules
            form.setMatricule( SqualeWebActionUtils.cleanValues( form.getMatricule() ) );
            // si il n'y a pas d'erreur on enregistre les modifications
            if ( errors.isEmpty() )
            {
                // On ajoute la liste des droits � l'instance de l'application en cours de modification
                createApplicationForm.setRights( users );
                // On v�rifie en base que le nom est disponible
                ApplicationConfDTO application =
                    (ApplicationConfDTO) WTransformerFactory.formToObj( ApplicationConfTransformer.class,
                                                                        createApplicationForm )[0];
                // Obtention de l'AC
                IApplicationComponent ac = AccessDelegateHelper.getInstance( "ApplicationAdmin" );
                // On change le nom de l'utilisateur et la date de derni�re modification
                application.setLastUser( ( (LogonBean) pRequest.getSession().getAttribute( WConstants.USER_KEY ) ).getMatricule() );
                application.setLastUpdate( Calendar.getInstance().getTime() );
                Object[] paramIn = { application };
                forward = pMapping.findForward( "success" );
                if ( ( (Integer) ac.execute( "saveApplication", paramIn ) ).intValue() != 0 )
                {
                    // Renvoi d'un message g�n�rique suite � l'�chec de sauvegarde de l'application
                    ActionMessage error = new ActionMessage( "error.application_not_saved" );
                    errors.add( ActionMessages.GLOBAL_MESSAGE, error );
                    saveMessages( pRequest, errors );
                    forward = pMapping.findForward( "total_failure" );
                }
                else
                {
                    // l'application est recharg�e afin de mettre � jour le nom du serveur
                    application = (ApplicationConfDTO) ac.execute( "getApplicationConf", paramIn );
                    createApplicationForm =
                        (CreateApplicationForm) WTransformerFactory.objToForm( ApplicationConfTransformer.class,
                                                                               application );
                    pRequest.getSession().setAttribute( "createApplicationForm", createApplicationForm );
                    ActionUtils.refreshUser( pRequest );
                }

            }
        }
        catch ( Exception e )
        {
            // Traitement factoris� des exceptions et transfert vers la page d'erreur
            handleException( e, errors, pRequest );
            saveMessages( pRequest, errors );
            forward = pMapping.findForward( "total_failure" );
        }
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );

        return ( forward );
    }

    /**
     * S�lectionne l'application � configurer
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward selectApplicationToConfig( ActionMapping pMapping, ActionForm pForm,
                                                    HttpServletRequest pRequest, HttpServletResponse pResponse )
    {

        ActionMessages errors = new ActionMessages();
        ActionForward forward = null;

        try
        {
            // R�cup�ration de l'application depuis la requ�te
            String applicationId = pRequest.getParameter( "applicationId" );
            ApplicationConfDTO dto = ManageApplicationUtils.getApplication( applicationId );
            if ( null == dto )
            {
                // Message g�n�rique si l'application n'existe pas
                ActionMessage error = new ActionMessage( "error.application_not_got" );
                errors.add( ActionMessages.GLOBAL_MESSAGE, error );
            }
            else
            {
                ManageApplicationUtils.getCreateApplicationForm( applicationId, pRequest );
                loadLastBranchAuditInSession( pRequest, dto.getId() );
                // Add an user access for this application
                addUserAccess( pRequest, dto.getId() );
                forward = pMapping.findForward( "application_summary" );
            }
        }
        catch ( Exception e )
        {
            // Traitement factoris� des exceptions
            handleException( e, errors, pRequest );
        }
        if ( !errors.isEmpty() )
        {
            // Sauvegarde des messages
            saveMessages( pRequest, errors );
            // Transfert vers la page d'erreur
            forward = pMapping.findForward( "total_failure" );
        }
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );
        return forward;
    }

    /**
     * Ajoute un projet � l'application actuellement en cours de configuration
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward addProject( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                     HttpServletResponse pResponse )
    {

        ActionMessages errors = new ActionMessages();
        ActionForward forward = null;
        try
        {
            CreateProjectForm project = (CreateProjectForm) pForm;
            // R�cup�re la config du projet
            Collection tasksDTO = getConfig( project, pRequest, errors, true );
            if ( !errors.isEmpty() )
            {
                saveMessages( pRequest, errors );
                forward = pMapping.findForward( "save_failure" );
            }
            else
            {
                // on sauvegarde le projet
                forward = new CreateProjectAction().saveProject( pMapping, project, pRequest, pResponse );
                if ( tasksDTO.size() != 0 )
                { // Il reste les outils � configurer
                    forward = pMapping.findForward( "configure" );
                }
            }
        }
        catch ( Exception e )
        {
            // Traitement factoris� des exceptions et transfert vers la page d'erreur
            handleException( e, errors, pRequest );
            saveMessages( pRequest, errors );
            forward = pMapping.findForward( "total_failure" );
        }
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );
        return forward;
    }

    /**
     * Sauvegarde un projet de l'application actuellement en cours de configuration
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward saveProject( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                      HttpServletResponse pResponse )
    {
        ActionMessages errors = new ActionMessages();
        ActionForward forward = null;
        try
        {
            // On r�cup�re le formulaire de cr�ation du projet
            CreateProjectForm project = (CreateProjectForm) pForm;

            // On sauvegarde le projet seulement si il n'y a pas eu d'erreurs
            if ( isNameAvailable( project, pRequest ) )
            {
                forward = new CreateProjectAction().saveProject( pMapping, project, pRequest, pResponse );
            }
            else
            {
                ActionMessage error = new ActionMessage( "project_creation.name.already_used" );
                errors.add( ActionMessages.GLOBAL_MESSAGE, error );
                forward = pMapping.findForward( "save_failure" );
                saveMessages( pRequest, errors );
            }
        }
        catch ( Exception e )
        {
            // Traitement factoris� des exceptions et transfert vers la page d'erreur
            handleException( e, errors, pRequest );
            saveMessages( pRequest, errors );
            forward = pMapping.findForward( "total_failure" );
        }
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );
        return forward;
    }

    /**
     * M�thode de factorisation
     * 
     * @param pForm le formulaire
     * @param pRequest la requete http
     * @param errors les messages � stocker
     * @param pCheckNeeded un bool�en indiquant si on est dans un cas de lecture ou de modification Si false, on ne
     *            v�rifie pas, si true on effectue les v�rifications sur le nom
     * @return la liste des taches que l'on a r�cup�r�
     * @throws Exception en cas d'�chec
     */
    private Collection getConfig( CreateProjectForm pForm, HttpServletRequest pRequest, ActionMessages errors,
                                  boolean pCheckNeeded )
        throws Exception
    {
        // On ajout le projet � l'application
        if ( pCheckNeeded && !isNameAvailable( pForm, pRequest ) )
        {
            ActionMessage error = new ActionMessage( "project_creation.name.already_used" );
            errors.add( ActionMessages.GLOBAL_MESSAGE, error );
        }
        // Il faut peut-�tre configurer les t�ches
        IApplicationComponent ac = AccessDelegateHelper.getInstance( "SqualixConfig" );
        Object[] paramIn = { pForm.getSourceManagement(), pForm.getProfile() };
        // Ex�cution de l'AC
        Collection tasksDTO = (Collection) ac.execute( "getConfigurableTasks", paramIn );
        ArrayList configurableTasks = new ArrayList();
        configurableTasks.addAll( tasksDTO );
        pForm.setTasks( configurableTasks );
        return tasksDTO;
    }

    /**
     * Action tr�s simple juste dans le cas on on veut visualiser la configuration d'un projet
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward viewProjectConfig( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                            HttpServletResponse pResponse )
    {
        ActionMessages errors = new ActionMessages();
        ActionForward forward = pMapping.findForward( "configure" );
        try
        {
            getConfig( (CreateProjectForm) pForm, pRequest, errors, false );
            if ( !errors.isEmpty() )
            {
                saveMessages( pRequest, errors );
                forward = pMapping.findForward( "failure" );
            }
        }
        catch ( Exception e )
        {
            // Traitement factoris� des exceptions et transfert vers la page d'erreur
            handleException( e, errors, pRequest );
            saveMessages( pRequest, errors );
            forward = pMapping.findForward( "total_failure" );
        }
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );
        return forward;
    }

    /**
     * Supprime les projets s�lectionn�s de l'application
     * 
     * @param pMapping le mapping
     * @param pForm le formulaire
     * @param pRequest la requ�te
     * @param pResponse la r�ponse
     * @return l'action � r�aliser
     */
    public ActionForward deleteProjects( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                         HttpServletResponse pResponse )
    {
        return changeProjectsStatus( pMapping, pForm, pRequest, ProjectBO.DELETED, "removeProject" );
    }

    /**
     * D�sactive ou r�active les projets s�lectionn�s de l'application
     * 
     * @param pMapping le mapping
     * @param pForm le formulaire
     * @param pRequest la requ�te
     * @param pResponse la r�ponse
     * @return l'action � r�aliser
     */
    public ActionForward disactiveOrReactiveProjects( ActionMapping pMapping, ActionForm pForm,
                                                      HttpServletRequest pRequest, HttpServletResponse pResponse )
    {
        return changeProjectsStatus( pMapping, pForm, pRequest, ProjectBO.DISACTIVATED, "disactiveOrReactiveProject" );
    }

    /**
     * @param pMapping le mapping
     * @param pForm le formulaire
     * @param pRequest la requ�te
     * @param pStatus le statut du projet
     * @param pMethod le nom de la m�thode � appeler
     * @return l'action � r�aliser
     */
    private ActionForward changeProjectsStatus( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                                int pStatus, String pMethod )
    {
        ActionForward forward = null;
        ActionErrors errors = new ActionErrors();
        try
        {
            // R�cup�ration des projets s�lectionn�s par checkbox
            CreateApplicationForm applicationForm =
                (CreateApplicationForm) pRequest.getSession().getAttribute( "createApplicationForm" );
            List projects = applicationForm.getProjects();
            List newProjects = new ArrayList();
            // On d�fini une variable permettant de compter les modifications de statut
            int cpt = 0;
            ActionErrors messages = new ActionErrors();
            // Parcours de la s�lection et suppression de chaque projet
            IApplicationComponent ac = AccessDelegateHelper.getInstance( "ApplicationAdmin" );
            Object[] paramIn = new Object[1];
            for ( int i = 0; i < projects.size(); i++ )
            {
                CreateProjectForm projectForm = (CreateProjectForm) projects.get( i );
                CreateProjectForm newProjectForm = projectForm;
                if ( projectForm.isSelected() )
                {
                    cpt++;
                    paramIn[0] = new Long( projectForm.getProjectId() );
                    ProjectConfDTO result = (ProjectConfDTO) ac.execute( pMethod, paramIn );
                    // Construction d'un message de confirmation pour chaque
                    // projet modifi�
                    ActionMessage message =
                        new ActionMessage( "info.update_project_" + pStatus + "_" + ( result != null ),
                                           projectForm.getProjectName() );
                    messages.add( "projectsMessages", message );
                    if ( null != result )
                    {
                        // On transforme
                        newProjectForm =
                            (CreateProjectForm) WTransformerFactory.objToForm( ProjectConfTransformer.class, result );
                        // on affecte l'id de l'application
                        newProjectForm.setApplicationId( applicationForm.getApplicationId() );
                    }
                }
                // Si il n'est pas supprim�, on l'ajoute
                if ( newProjectForm.getStatus() != ProjectBO.DELETED )
                {
                    newProjects.add( newProjectForm );
                }
                saveMessages( pRequest, messages );
            }
            // V�rification de la s�lection
            if ( cpt < 1 )
            {
                ActionError error = new ActionError( "error.invalid_projects_selection" );
                errors.add( "invalid.selection", error );
            }
            else
            {
                LogonBean user = (LogonBean) getWILogonBean( pRequest );
                // On enregistre les param�tres de derni�res modifications pour l'application
                ac.execute( "updateLastModifParams", new Object[] {
                    new Long( Long.parseLong( applicationForm.getApplicationId() ) ), user.getMatricule(),
                    Calendar.getInstance().getTime() } );
                // On recharge l'application en session
                ApplicationConfDTO applicationDTO =
                    (ApplicationConfDTO) WTransformerFactory.formToObj( ApplicationConfTransformer.class,
                                                                        applicationForm )[0];
                applicationDTO =
                    (ApplicationConfDTO) ac.execute( "getApplicationConf", new Object[] { applicationDTO } );
                applicationForm =
                    (CreateApplicationForm) WTransformerFactory.objToForm( ApplicationConfTransformer.class,
                                                                           applicationDTO );
                pRequest.getSession().setAttribute( "createApplicationForm", applicationForm );
                ActionUtils.refreshUser( pRequest );
            }
            applicationForm.setProjects( newProjects );
            forward = pMapping.findForward( "application_summary" );
        }
        catch ( Exception e )
        {
            // Traitement factoris� des exceptions
            handleException( e, errors, pRequest );
            // Routage vers la page d'erreur
            forward = pMapping.findForward( "total_failure" );
        }
        if ( !errors.isEmpty() )
        {
            // Enregistrement du message et routage vers la page d'erreur
            saveErrors( pRequest, errors );
        }
        return forward;
    }

    /**
     * Ajoute un audit de suivi � r�aliser sur l'application.
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward addMilestone( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                       HttpServletResponse pResponse )
    {

        ActionMessages errors = new ActionMessages();
        ActionForward forward = null;
        try
        {
            // Pr�paration de l'audit
            AuditDTO audit = (AuditDTO) WTransformerFactory.formToObj( AuditTransformer.class, (AuditForm) pForm )[0];
            // Placement des champs non d�finis dans le formulaire
            audit.setType( AuditBO.MILESTONE );
            if ( null == audit.getHistoricalDate() )
            {
                // On affecte la date du jour
                Calendar today = GregorianCalendar.getInstance();
                audit.setHistoricalDate( today.getTime() );
            }
            CreateApplicationForm application =
                (CreateApplicationForm) pRequest.getSession().getAttribute( "createApplicationForm" );
            audit.setApplicationId( new Long( application.getApplicationId() ).longValue() );
            // Obtention de l'AC
            IApplicationComponent ac = AccessDelegateHelper.getInstance( "ApplicationAdmin" );
            Object[] paramIn = { audit };
            forward = pMapping.findForward( "success" );
            // Ex�cution de l'AC en v�rifiant que l'insertion s'est bien pass�e
            AuditDTO result = (AuditDTO) ac.execute( "addMilestone", paramIn );
            if ( null == result )
            {
                // L'audit n'a pas un nom correct ou l'ex�cution dans la base pose probl�me
                // On renvoie un message d'erreur neutre
                ActionMessage error = new ActionMessage( "error.application.audit.not_saved" );
                errors.add( "milestoneMsg", error );
                saveMessages( pRequest, errors );
                forward = pMapping.findForward( "failure" );
            }
            else
            {
                ActionMessage message = new ActionMessage( "info.audit_created", result.getName() );
                errors.add( "milestoneMsg", message );
                saveMessages( pRequest, errors );
                // Mise � jour du form dans la session
                application.setMilestoneAudit( result );
                pRequest.getSession().setAttribute( "createApplicationForm", application );
            }
        }
        catch ( Exception e )
        {
            // Traitement factoris� des exceptions et transfert vers la page d'erreur
            handleException( e, errors, pRequest );
            saveMessages( pRequest, errors );
            forward = pMapping.findForward( "total_failure" );
        }
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );
        return forward;
    }

    /**
     * Ajoute un audit de suivi � r�aliser sur l'application. Les �ventuels audits de suivi programm�s sont supprim�s au
     * profit du nouvel audit cr��
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward addBranch( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                    HttpServletResponse pResponse )
    {
        ActionMessages errors = new ActionMessages();
        ActionForward forward = null;
        try
        {
            AuditDTO lAuditDTO =
                (AuditDTO) WTransformerFactory.formToObj( AuditTransformer.class, (WActionForm) pForm )[0];
            IApplicationComponent ac = AccessDelegateHelper.getInstance( "ApplicationAdmin" );
            Object[] paramIn = { lAuditDTO };
            lAuditDTO = (AuditDTO) ac.execute( "addBranch", paramIn );
            if ( lAuditDTO != null )
            {
                // mise � jour de la date de l'audit de suivi programm� dans le formulaire
                AuditForm lAuditForm =
                    (AuditForm) WTransformerFactory.objToForm( AuditTransformer.class, new Object[] { lAuditDTO } );
                pRequest.getSession().setAttribute( "auditForm2", lAuditForm );
                forward = pMapping.findForward( "success" );
                errors.add( "branchMsg", new ActionMessage( "info.audit_branch_created" ) );
            }
            else
            {
                forward = pMapping.findForward( "failure" );
                errors.add( "branchMsg", new ActionMessage( "error.application.audit.not_saved" ) );
            }
            saveMessages( pRequest, errors );
        }
        catch ( Exception e )
        {
            handleException( e, errors, pRequest );
            saveMessages( pRequest, errors );
            forward = pMapping.findForward( "total_failure" );
        }
        return forward;
    }

    /**
     * Supprime l'audit de jalon programm� pour l'application.
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward deleteMilestone( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                          HttpServletResponse pResponse )
    {

        ActionMessages errors = new ActionMessages();
        ActionForward forward = null;
        try
        {
            // R�cup�ration de l'application
            CreateApplicationForm applicationForm =
                (CreateApplicationForm) pRequest.getSession().getAttribute( "createApplicationForm" );
            Long auditId = new Long( applicationForm.getMilestoneAudit().getId() ); // Obtention de l'AC
            IApplicationComponent ac = AccessDelegateHelper.getInstance( "ApplicationAdmin" );
            Object[] paramIn = { auditId };
            forward = pMapping.findForward( "success" );
            // Ex�cution de l'AC en v�rifiant que la suppression c'est bien effectu�e
            if ( ( (Integer) ac.execute( "removeAudit", paramIn ) ).intValue() == 1 )
            {
                // L'ex�cution dans la base pose probl�me
                // On renvoie un message d'erreur neutre
                ActionMessage error = new ActionMessage( "error.application.audit.not_deleted" );
                errors.add( "milestoneMsg", error );
                saveMessages( pRequest, errors );
                forward = pMapping.findForward( "failure" );
            }
            else
            {
                ActionMessage message =
                    new ActionMessage( "info.audit_removed", applicationForm.getMilestoneAudit().getName() );
                // Mise � jour du form dans la session
                applicationForm.resetMilestoneAudit();
                pRequest.getSession().setAttribute( "createApplicationForm", applicationForm );
                errors.add( "milestoneMsg", message );
                saveMessages( pRequest, errors );
            }
        }
        catch ( Exception e )
        {
            // Traitement factoris� des exceptions et transfert vers la page d'erreur
            handleException( e, errors, pRequest );
            saveMessages( pRequest, errors );
            forward = pMapping.findForward( "total_failure" );
        }
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );
        return forward;
    }

    /**
     * Modifie l'audit de jalon programm� pour l'application.
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward modifyMilestone( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                          HttpServletResponse pResponse )
    {

        ActionMessages errors = new ActionMessages();
        ActionForward forward = null;
        try
        {
            // R�cup�ration de l'id de l'audit
            CreateApplicationForm applicationForm =
                ManageApplicationUtils.getCreateApplicationForm( ( (AuditForm) pForm ).getApplicationId(), pRequest );
            long auditId = applicationForm.getMilestoneAudit().getId();
            // Pr�paration de l'audit
            AuditDTO auditDTO =
                (AuditDTO) WTransformerFactory.formToObj( AuditTransformer.class, (WActionForm) pForm )[0];
            auditDTO.setID( auditId );
            auditDTO.setStatus( AuditBO.NOT_ATTEMPTED );
            auditDTO.setType( AuditBO.MILESTONE );
            if ( null == auditDTO.getHistoricalDate() )
            {
                // On affecte la date du jour
                Calendar today = GregorianCalendar.getInstance();
                auditDTO.setHistoricalDate( today.getTime() );
            }
            // Obtention de l'AC
            IApplicationComponent ac = AccessDelegateHelper.getInstance( "ApplicationAdmin" );
            Object[] paramIn = { auditDTO };
            forward = pMapping.findForward( "success" );
            // Ex�cution de l'AC
            AuditDTO result = (AuditDTO) ac.execute( "modifyAudit", paramIn );
            if ( null == result )
            {
                // L'ex�cution dans la base pose probl�me
                // On renvoie un message d'erreur neutre
                ActionMessage error = new ActionMessage( "error.application.audit.not_modified" );
                errors.add( "milestoneMsg", error );
                saveMessages( pRequest, errors );
                forward = pMapping.findForward( "failure" );
            }
            else
            {
                ActionMessage message = new ActionMessage( "info.audit_modified", result.getName() );
                errors.add( "milestoneMsg", message );
                saveMessages( pRequest, errors );
                // Mise � jour du form dans la session
                applicationForm.setMilestoneAudit( result );
                pRequest.getSession().setAttribute( "createApplicationForm", applicationForm );
            }
        }
        catch ( Exception e )
        {
            // Traitement factoris� des exceptions et transfert vers la page d'erreur
            handleException( e, errors, pRequest );
            saveMessages( pRequest, errors );
            forward = pMapping.findForward( "total_failure" );
        }
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );
        return forward;
    }

    /**
     * Permet de v�rifier que tous les champs sont renseign�s dans le premier formulaire d'ajout de projets.
     * 
     * @param pProjectForm formulaire de projet en cr�ation
     * @return la validit� du formulaire.
     */
    private boolean checkProjectForm( final CreateProjectForm pProjectForm )
    {
        // V�rification du nom de projet non vide,
        // du profil et du source management du projet
        return ( pProjectForm.getProjectName().trim().length() > 0 )
            && ( pProjectForm.getProfile().trim().length() > 0 )
            && ( pProjectForm.getSourceManagement().trim().length() > 0 );
    }

    /**
     * V�rifie si le projet existe d�j�, ou sinon, si son nom est disponible.
     * 
     * @param pProject le projet.
     * @param pRequest la requete http
     * @return la validit� du projet
     * @throws WTransformerException si erreur dans la transformation
     * @throws JrafEnterpriseException si erreur JRAF
     */
    private boolean isNameAvailable( CreateProjectForm pProject, HttpServletRequest pRequest )
        throws JrafEnterpriseException, WTransformerException
    {
        // On r�cup�re l'application courant
        CreateApplicationForm appliForm =
            ManageApplicationUtils.getCreateApplicationForm( pProject.getApplicationId(), pRequest );
        List projects = appliForm.getProjects();
        boolean isNameAvailable = true;
        // D�ja si le nom du projet n'est pas correct, on renvoie true pour que le projet ne soit pas persist�
        if ( checkProjectForm( pProject ) )
        {
            // Si le projet n'a jamais �t� persist�e, on v�rifie que son nom est disponible
            CreateProjectForm project = null;
            for ( int i = 0; i < projects.size() && isNameAvailable; i++ )
            {
                project = (CreateProjectForm) projects.get( i );
                // le nom est disponible si :
                // ce n'est pas le meme nom
                // ou c'est le meme nom mais avec le meme id (on modifie un projet d�j� existant)
                isNameAvailable =
                    ( !project.getProjectName().equals( pProject.getProjectName() ) )
                        || ( project.getProjectName().equals( pProject.getProjectName() ) && project.getProjectId().equals(
                                                                                                                            pProject.getProjectId() ) );
                if ( project.getProjectId().equals( "-1" ) )
                {
                    // Le nom est disponible puisque le projet n'a pas persist�.
                    // Par contre il faut supprimer le projet dans la liste
                    isNameAvailable = !isNameAvailable;
                    projects.remove( i );
                }
            }
        }
        // par apport au nom de la m�thode faut renvoyer le contraire
        return isNameAvailable;
    }

    /**
     * Confirmation fot deleting (not physically) an application
     * 
     * @param pMapping mapping.
     * @param pForm bean
     * @param pRequest HTTP request.
     * @param pResponse servlet response.
     * @return action to realize.
     */
    public ActionForward deleteConfirm( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                        HttpServletResponse pResponse )
    {
        pRequest.setAttribute( "hide", "true" );
        return pMapping.findForward( "application_confirm_purge" );
    }

    /**
     * Confirmation de purge d'une application. La purge d'une application se fait apr�s avoir eu une confirmation de
     * celle-ci
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward purgeConfirm( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                       HttpServletResponse pResponse )
    {
        return pMapping.findForward( "application_confirm_purge" );
    }

    /**
     * Hide an application for not admin users without delete it physically: - remove users - if it's public, becomes
     * private - disactive audits - delete current not attempted audits - rename application like
     * applicationName(year)(month)(day)(hour)(minute)
     * 
     * @param pMapping mapping
     * @param pForm bean
     * @param pRequest request
     * @param pResponse response
     * @return action to do
     */
    public ActionForward deleteApplication( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                            HttpServletResponse pResponse )
    {
        ActionMessages errors = new ActionMessages();
        ActionForward forward;
        try
        {
            // Pr�paration de l'application
            CreateApplicationForm form = (CreateApplicationForm) pForm;
            ApplicationConfDTO applicationConf = new ApplicationConfDTO();
            applicationConf.setId( new Long( form.getApplicationId() ).longValue() );
            applicationConf.setName( form.getApplicationName() );
            // Purge de l'application
            IApplicationComponent ac = AccessDelegateHelper.getInstance( "Purge" );
            Object[] paramIn = { applicationConf };
            ac.execute( "hideApplication", paramIn );

            // Envoi d'un mail aux administrateurs et aux gestionnaires de l'application pour leur signaler
            // qu'une application a �t� supprim�e
            String sender = WebMessages.getString( getLocale( pRequest ), "mail.sender.squale" );
            String header = WebMessages.getString( getLocale( pRequest ), "mail.header" );
            String object = sender + WebMessages.getString( pRequest, "mail.appli.deleted.object" );
            SimpleDateFormat formator =
                new SimpleDateFormat( WebMessages.getString( getLocale( pRequest ), "date.format.simple" ) );
            String today = formator.format( Calendar.getInstance().getTime() );
            LogonBean identifiedUser = (LogonBean) getWILogonBean( pRequest );
            Object[] params = { form.getApplicationName(), today, identifiedUser.getMatricule() };
            String content =
                header + MessageFormat.format( WebMessages.getString( pRequest, "mail.appli.deleted.content" ), params );
            content += "\n\n" + WebMessages.getString( pRequest, "mail.appli.deleted.content.users" );
            // On affiche les utilisateurs de l'application
            for ( Iterator it = form.getRights().keySet().iterator(); it.hasNext(); )
            {
                String user = (String) it.next();
                content +=
                    "\n" + user + " - "
                        + WebMessages.getString( getLocale( pRequest ), (String) form.getRights().get( user ) );
            }
            SqualeCommonUtils.notifyByEmail( MailerHelper.getMailerProvider(),
                                             SqualeCommonConstants.MANAGERS_AND_ADMINS,
                                             new Long( applicationConf.getId() ), object, content, false );

            forward = applicationEndPurge( pMapping, pRequest, errors, form, applicationConf );
        }
        catch ( Exception e )
        {
            // Traitement factoris� des exceptions et transfert vers la page d'erreur
            handleException( e, errors, pRequest );
            saveMessages( pRequest, errors );
            forward = pMapping.findForward( "total_failure" );
        }
        return forward;
    }

    /**
     * Purge d'une application.
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward purge( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                HttpServletResponse pResponse )
    {
        ActionMessages errors = new ActionMessages();
        ActionForward forward;
        try
        {
            // Pr�paration de l'application
            CreateApplicationForm form = (CreateApplicationForm) pForm;
            ApplicationConfDTO applicationConf = new ApplicationConfDTO();
            applicationConf.setId( new Long( form.getApplicationId() ).longValue() );
            applicationConf.setName( form.getApplicationName() );
            // Purge de l'application
            IApplicationComponent ac = AccessDelegateHelper.getInstance( "Purge" );
            Object[] paramIn = { applicationConf };
            ac.execute( "purgeApplication", paramIn );

            // Envoi d'un mail aux administrateurs et aux gestionnaires de l'application pour leur signaler
            // qu'une application a �t� supprim�e
            String sender = WebMessages.getString( getLocale( pRequest ), "mail.sender.squale" );
            String header = WebMessages.getString( getLocale( pRequest ), "mail.header" );
            String object = sender + WebMessages.getString( pRequest, "mail.appli.deleted.object" );
            SimpleDateFormat formator =
                new SimpleDateFormat( WebMessages.getString( getLocale( pRequest ), "date.format.simple" ) );
            String today = formator.format( Calendar.getInstance().getTime() );
            LogonBean identifiedUser = (LogonBean) getWILogonBean( pRequest );
            Object[] params = { form.getApplicationName(), today, identifiedUser.getMatricule() };
            String content =
                header + MessageFormat.format( WebMessages.getString( pRequest, "mail.appli.deleted.content" ), params );
            content += "\n\n" + WebMessages.getString( pRequest, "mail.appli.deleted.content.users" );
            // On affiche les utilisateurs de l'application
            for ( Iterator it = form.getRights().keySet().iterator(); it.hasNext(); )
            {
                String user = (String) it.next();
                content +=
                    "\n" + user + " - "
                        + WebMessages.getString( getLocale( pRequest ), (String) form.getRights().get( user ) );
            }
            SqualeCommonUtils.notifyByEmail( MailerHelper.getMailerProvider(),
                                             SqualeCommonConstants.MANAGERS_AND_ADMINS,
                                             new Long( applicationConf.getId() ), object, content, false );

            forward = applicationEndPurge( pMapping, pRequest, errors, form, applicationConf );
        }
        catch ( Exception e )
        {
            // Traitement factoris� des exceptions et transfert vers la page d'erreur
            handleException( e, errors, pRequest );
            saveMessages( pRequest, errors );
            forward = pMapping.findForward( "total_failure" );
        }
        return forward;
    }

    /**
     * Common forward for purge
     * 
     * @param pMapping mapping
     * @param pRequest request
     * @param pErrors errors
     * @param pForm bean
     * @param pApplicationConf application configuration (DTO)
     * @return action forward
     * @throws JrafEnterpriseException if JRAF error
     * @throws WTransformerException if Transformation error
     */
    private ActionForward applicationEndPurge( ActionMapping pMapping, HttpServletRequest pRequest,
                                               ActionMessages pErrors, CreateApplicationForm pForm,
                                               ApplicationConfDTO pApplicationConf )
        throws JrafEnterpriseException, WTransformerException
    {
        ActionForward forward;
        // On recharge les profils de l'utilisateur
        ActionUtils.refreshUser( pRequest );
        // On place un message de confirmation de la purge
        ActionMessage message = new ActionMessage( "info.purge_application", pApplicationConf.getName() );
        pErrors.add( ActionMessages.GLOBAL_MESSAGE, message );
        saveMessages( pRequest, pErrors );
        // On redirige vers la page de fin de purge d�pendant du statut de l'application et de l'utilisateurs
        // lors de la suppression
        LogonBean user = (LogonBean) pRequest.getSession().getAttribute( WConstants.USER_KEY );
        if ( ApplicationBO.IN_CREATION == pForm.getStatus() && user.isAdmin() )
        {
            forward = pMapping.findForward( "ack_application_end_purge" );
        }
        else
        {
            forward = pMapping.findForward( "application_end_purge" );
        }
        return forward;
    }

    /**
     * Enregistrement en session du dernier audit de suivi programm�
     * 
     * @param pRequest la requ�te HTTP
     * @param pApplicationId l'id de l'application
     * @throws Exception une exception
     */
    private void loadLastBranchAuditInSession( HttpServletRequest pRequest, long pApplicationId )
        throws Exception
    {
        AuditForm lAuditForm = null;
        final AuditDTO lAuditDTO = ManageApplicationUtils.getLastNotAttemptedAudit( pApplicationId );
        if ( lAuditDTO != null )
        {
            lAuditForm = (AuditForm) WTransformerFactory.objToForm( AuditTransformer.class, new Object[] { lAuditDTO } );
        }
        pRequest.getSession().setAttribute( "auditForm2", lAuditForm );
    }

    /**
     * Method called to fill the suggest input field when adding a user to an application. The "ch" parameter passed
     * along the request gives the string that the user has just typed in.
     * 
     * @param mapping the mapping.
     * @param form the form
     * @param request the HTTP request.
     * @param response the servlet response.
     * @return null (because the response is an XML stream)
     */
    public ActionForward findUserForAutocomplete( final ActionMapping mapping, final ActionForm form,
                                                  final HttpServletRequest request, final HttpServletResponse response )
    {
        // retrieves the string that the user has just typed in
        String stringFirstChars = request.getParameter( "ch" );
        stringFirstChars = WEasyCompleteUtil.filter( stringFirstChars ).toLowerCase();

        // create the response object
        WHttpEasyCompleteResponse easyComplete = new WHttpEasyCompleteResponse( response );

        // and fill it with the users' information
        if ( stringFirstChars.length() > 0 )
        {
            Collection<UserDTO> foundUsers = UserBeanAccessorHelper.getUserBeanAccessor().getUsers( stringFirstChars );
            for ( UserDTO user : foundUsers )
            {
                String value = user.getMatricule();
                String label = user.getFullName();
                easyComplete.addValueLabel( value, label );
            }
        }

        // now return the response
        try
        {
            easyComplete.close();
        }
        catch ( IOException e )
        {
            // there's nothing we can do about it, forget it
        }

        return null;
    }

}
