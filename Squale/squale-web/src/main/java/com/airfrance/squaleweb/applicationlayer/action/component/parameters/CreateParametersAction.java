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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.airfrance.squaleweb.applicationlayer.action.accessRights.ReaderAction;
import com.airfrance.squaleweb.applicationlayer.action.component.CreateProjectAction;
import com.airfrance.squaleweb.applicationlayer.formbean.component.parameters.AbstractParameterForm;
import com.airfrance.squaleweb.applicationlayer.formbean.creation.CreateProjectForm;
import com.airfrance.welcom.struts.bean.WActionForm;
import com.airfrance.welcom.struts.transformer.WTransformerFactory;

/**
 * Interface pour les actions associ�es � la configuration d'une t�che
 */
public class CreateParametersAction
    extends ReaderAction
{

    /**
     * Permet de remplir le bean de la t�che et de l'enregistrer en session si besoin.
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward fill( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                               HttpServletResponse pResponse )
    {
        ActionForward forward = null;
        ActionMessages errors = new ActionMessages();
        try
        {
            forward = pMapping.findForward( "config" );
            // Affectation au projet courant
            CreateProjectForm project = (CreateProjectForm) pRequest.getSession().getAttribute( "createProjectForm" );
            pForm.reset( pMapping, pRequest );
            // Indicate if it's a new project
            if ( null != pRequest.getSession().getAttribute( "modification" ) )
            {
                ( (AbstractParameterForm) pForm ).setNewConf( false );
            }
            WTransformerFactory.objToForm( ( (AbstractParameterForm) pForm ).getTransformer(), ( (WActionForm) pForm ),
                                           getTransformerParameters( project, pRequest ) );
            // On ajout le nom du formulaire pour pouvoir le r�cup�rer
            project.getTaskForms().add( ( (AbstractParameterForm) pForm ).getNameInSession() );
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
            saveMessages( pRequest, errors );
        }
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );
        return null;
    }

    /**
     * Ajoute les param�tres au projet
     * 
     * @param pMapping le mapping
     * @param pForm le formulaire
     * @param pRequest la requ�te
     * @param pResponse la r�ponse
     * @return l'action � r�aliser
     */
    public ActionForward addParameters( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                        HttpServletResponse pResponse )
    {
        ActionForward forward = null;
        ActionMessages errors = new ActionMessages();
        // Affectation au projet courant
        CreateProjectForm project = (CreateProjectForm) pRequest.getSession().getAttribute( "createProjectForm" );
        try
        {
            forward = pMapping.findForward( "configure" );

            // On modifie les param�tres du projet pour cette t�che
            WTransformerFactory.formToObj( ( (AbstractParameterForm) pForm ).getTransformer(), (WActionForm) pForm,
                                           getTransformerParameters( project, pRequest ) );

            // On sauvegarde le projet
            new CreateProjectAction().saveProject( pMapping, project, pRequest, pResponse );
        }
        catch ( Exception e )
        {
            // Traitement factoris� des exceptions
            handleException( e, errors, pRequest );
        }
        if ( !errors.isEmpty() )
        {
            saveMessages( pRequest, errors );
        }
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );
        return forward;
    }

    /**
     * This method return the parameters of the task necessary for the transformer
     * 
     * @param pProject The project form
     * @param pRequest The servlet request
     * @return The task parameters necessary for the transformer
     * @throws Exception Exception occured
     */
    public Object[] getTransformerParameters( CreateProjectForm pProject, HttpServletRequest pRequest ) throws Exception    
    {
        String taskName=  pRequest.getParameter( "taskName" );
        return new Object[] { pProject.getParameters(),taskName };
    }

    /**
     * Permet de d�configurer la t�che.
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward removeParameters( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                           HttpServletResponse pResponse )
    {
        ActionForward forward = null;
        ActionMessages errors = new ActionMessages();
        try
        {
            forward = pMapping.findForward( "configure" );
            // Affectation au projet courant
            CreateProjectForm project = (CreateProjectForm) pRequest.getSession().getAttribute( "createProjectForm" );
            // On supprime toutes les entr�es li�es � la t�che dans les param�tres du projet
            String[] constants = ( (AbstractParameterForm) pForm ).getParametersConstants();
            // Modification asked by user so it's not a new configuration
            ( (AbstractParameterForm) pForm ).setNewConf( false );
            for ( int i = 0; i < constants.length; i++ )
            {
                project.getParameters().getParameters().remove( constants[i] );
            }
            // On sauvegarde le projet
            new CreateProjectAction().saveProject( pMapping, project, pRequest, pResponse );
            // Change status for configuration
            ( (AbstractParameterForm) pForm ).setNewConf( false );
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
            saveMessages( pRequest, errors );
        }
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );
        return forward;
    }
}
