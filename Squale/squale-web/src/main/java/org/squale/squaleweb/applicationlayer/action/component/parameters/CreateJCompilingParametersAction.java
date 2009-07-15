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
package org.squale.squaleweb.applicationlayer.action.component.parameters;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import org.squale.squaleweb.applicationlayer.action.component.CreateProjectAction;
import org.squale.squaleweb.applicationlayer.formbean.component.parameters.EclipseUserLibForm;
import org.squale.squaleweb.applicationlayer.formbean.component.parameters.EclipseVarForm;
import org.squale.squaleweb.applicationlayer.formbean.component.parameters.JCompilingForm;
import org.squale.squaleweb.applicationlayer.formbean.component.parameters.JavaCompilationForm;
import org.squale.squaleweb.applicationlayer.formbean.creation.CreateProjectForm;
import org.squale.squaleweb.transformer.component.parameters.JCompilingConfTransformer;
import org.squale.welcom.struts.transformer.WTransformerFactory;

/**
 * Configuration des param�tres de la t�che JCompiling
 */
public class CreateJCompilingParametersAction
    extends CreateParametersAction
{

    /**
     * Ajoute des crit�res de compilation au projet Java
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward addProjectJavaCompiling( ActionMapping pMapping, ActionForm pForm,
                                                  HttpServletRequest pRequest, HttpServletResponse pResponse )
    {
        ActionMessages errors = new ActionMessages();
        ActionForward forward = null;
        try
        {
            forward = pMapping.findForward( "success_add" );
            // Affectation au projet courant
            CreateProjectForm project = (CreateProjectForm) pRequest.getSession().getAttribute( "createProjectForm" );
            // R�cup�ration du bean pour les param�tres java:
            JCompilingForm jCompilingForm = (JCompilingForm) pRequest.getSession().getAttribute( "jCompilingForm" );
            // On ajoute la r�gle au formulaire
            jCompilingForm.getCompilationRules().add( pForm );
            // Transformation des param�tres donn�s dans le formulaire pour le projet
            WTransformerFactory.formToObj( JCompilingConfTransformer.class, jCompilingForm, project.getParameters() );
            // On sauvegarde le projet
            new CreateProjectAction().saveProject( pMapping, project, pRequest, pResponse );
            // On supprime le type de compilation des param�tres de la requ�te

            // pRequest.getParameterMap().remove("kindOfTask");

        }
        catch ( Exception e )
        {
            handleException( e, errors, pRequest );
            saveMessages( pRequest, errors );
            // erreurs non pr�vues --> traitement sp�cifique
            forward = pMapping.findForward( "total_failure" );
        }
        if ( !errors.isEmpty() )
        {
            // Affichage des erreurs sur la page initiale
            saveMessages( pRequest, errors );
            forward = pMapping.findForward( "failure" );
        }
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );
        return forward;
    }

    /**
     * Retire une r�gle de compilation de la liste
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward removeCompilingRules( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                               HttpServletResponse pResponse )
    {

        ActionMessages errors = new ActionMessages();
        ActionForward forward = null;

        try
        {
            // Affectation au projet courant
            CreateProjectForm project = (CreateProjectForm) pRequest.getSession().getAttribute( "createProjectForm" );
            // R�cup�ration du bean courant
            JCompilingForm jCompilingForm = (JCompilingForm) pForm;
            Iterator iter = jCompilingForm.getCompilationRules().iterator();
            // Suppression des r�gles de compilation s�lectionn�es
            // directement depuis l'it�rateur
            boolean noSelection = true; // indique qu'aucune r�gle n'a �t� s�lectionn�e
            while ( iter.hasNext() )
            {
                if ( ( (JavaCompilationForm) iter.next() ).isSelected() )
                {
                    iter.remove();
                    noSelection = false;
                }
            }
            // Si il n'y a pas eu de s�lection, on affiche un message
            if ( noSelection )
            {
                ActionMessage message = new ActionMessage( "error.java.compiling.rule_selection" );
                errors.add( "ruleSelection", message );
                saveMessages( pRequest, errors );
            }
            // Transformation des param�tres donn�s dans le formulaire pour le projet
            WTransformerFactory.formToObj( JCompilingConfTransformer.class, jCompilingForm, project.getParameters() );
            // On sauvegarde le projet si il y reste au moins une r�gle
            if ( jCompilingForm.getCompilationRules().size() > 0 )
            {
                new CreateProjectAction().saveProject( pMapping, project, pRequest, pResponse );
            }
            forward = pMapping.findForward( "success" );
        }
        catch ( Exception e )
        {
            // Traitement factoris� des exceptions
            handleException( e, errors, pRequest );
            saveMessages( pRequest, errors );
            // Renvoi vers la page d'erreur
            forward = pMapping.findForward( "total_failure" );
        }
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );
        return forward;
    }

    /**
     * Ajoute une variable eclipse
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward addEclipseVar( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                        HttpServletResponse pResponse )
    {

        ActionMessages errors = new ActionMessages();
        ActionForward forward = null;
        try
        {
            forward = pMapping.findForward( "success" );
            // Affectation au projet courant
            CreateProjectForm project = (CreateProjectForm) pRequest.getSession().getAttribute( "createProjectForm" );
            // R�cup�ration du bean pour les param�tres java:
            JCompilingForm jCompilingForm = (JCompilingForm) pRequest.getSession().getAttribute( "jCompilingForm" );
            // On ajoute la r�gle au formulaire
            jCompilingForm.getEclipseVars().add( pForm );
            // Transformation des param�tres donn�s dans le formulaire pour le projet
            WTransformerFactory.formToObj( JCompilingConfTransformer.class, jCompilingForm, project.getParameters() );
            // On sauvegarde le projet
            new CreateProjectAction().saveProject( pMapping, project, pRequest, pResponse );
            // On supprime le type de compilation des param�tres de la requ�te
            pRequest.getParameterMap().remove( "kindOfTask" );

        }
        catch ( Exception e )
        {
            handleException( e, errors, pRequest );
            saveMessages( pRequest, errors );
            // erreurs non pr�vues --> traitement sp�cifique
            forward = pMapping.findForward( "total_failure" );
        }
        if ( !errors.isEmpty() )
        {
            // Affichage des erreurs sur la page initiale
            saveMessages( pRequest, errors );
            forward = pMapping.findForward( "failure" );
        }
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );
        return forward;
    }

    /**
     * Retire des variables eclipse
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward removeEclipseVars( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                            HttpServletResponse pResponse )
    {

        ActionMessages errors = new ActionMessages();
        ActionForward forward = null;

        try
        {
            // Affectation au projet courant
            CreateProjectForm project = (CreateProjectForm) pRequest.getSession().getAttribute( "createProjectForm" );
            // R�cup�ration du bean courant
            JCompilingForm jCompilingForm = (JCompilingForm) pForm;
            Iterator iter = jCompilingForm.getEclipseVars().iterator();
            // Suppression des variables s�lectionn�es
            // directement depuis l'it�rateur
            boolean noSelection = true; // indique qu'aucune variable n'a �t� s�lectionn�e
            while ( iter.hasNext() )
            {
                if ( ( (EclipseVarForm) iter.next() ).isSelected() )
                {
                    iter.remove();
                    noSelection = false;
                }
            }
            // Si il n'y a pas eu de s�lection, on affiche un message
            if ( noSelection )
            {
                ActionMessage message = new ActionMessage( "error.java.compiling.var_selection" );
                errors.add( "ruleSelection", message );
                saveMessages( pRequest, errors );
            }
            // Transformation des param�tres donn�s dans le formulaire pour le projet
            WTransformerFactory.formToObj( JCompilingConfTransformer.class, jCompilingForm, project.getParameters() );
            // On sauvegarde le projet
            new CreateProjectAction().saveProject( pMapping, project, pRequest, pResponse );
            forward = pMapping.findForward( "success" );
        }
        catch ( Exception e )
        {
            // Traitement factoris� des exceptions
            handleException( e, errors, pRequest );
            saveMessages( pRequest, errors );
            // Renvoi vers la page d'erreur
            forward = pMapping.findForward( "total_failure" );
        }
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );
        return forward;
    }

    /**
     * Ajoute une libraire eclipse
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward addEclipseLib( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                        HttpServletResponse pResponse )
    {

        ActionMessages errors = new ActionMessages();
        ActionForward forward = null;
        try
        {
            forward = pMapping.findForward( "success" );
            // Affectation au projet courant
            CreateProjectForm project = (CreateProjectForm) pRequest.getSession().getAttribute( "createProjectForm" );
            // R�cup�ration du bean pour les param�tres java:
            JCompilingForm jCompilingForm = (JCompilingForm) pRequest.getSession().getAttribute( "jCompilingForm" );
            // On ajoute la r�gle au formulaire
            jCompilingForm.getEclipseLibs().add( pForm );
            // Transformation des param�tres donn�s dans le formulaire pour le projet
            WTransformerFactory.formToObj( JCompilingConfTransformer.class, jCompilingForm, project.getParameters() );
            // On sauvegarde le projet
            new CreateProjectAction().saveProject( pMapping, project, pRequest, pResponse );
            // On supprime le type de compilation des param�tres de la requ�te
            pRequest.getParameterMap().remove( "kindOfTask" );

        }
        catch ( Exception e )
        {
            handleException( e, errors, pRequest );
            saveMessages( pRequest, errors );
            // erreurs non pr�vues --> traitement sp�cifique
            forward = pMapping.findForward( "total_failure" );
        }
        if ( !errors.isEmpty() )
        {
            // Affichage des erreurs sur la page initiale
            saveMessages( pRequest, errors );
            forward = pMapping.findForward( "failure" );
        }
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );
        return forward;
    }

    /**
     * Retire des librairies eclipse
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward removeEclipseLibs( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                            HttpServletResponse pResponse )
    {

        ActionMessages errors = new ActionMessages();
        ActionForward forward = null;

        try
        {
            // Affectation au projet courant
            CreateProjectForm project = (CreateProjectForm) pRequest.getSession().getAttribute( "createProjectForm" );
            // R�cup�ration du bean courant
            JCompilingForm jCompilingForm = (JCompilingForm) pForm;
            Iterator iter = jCompilingForm.getEclipseLibs().iterator();
            // Suppression des variables s�lectionn�es
            // directement depuis l'it�rateur
            boolean noSelection = true; // indique qu'aucune variable n'a �t� s�lectionn�e
            while ( iter.hasNext() )
            {
                if ( ( (EclipseUserLibForm) iter.next() ).isSelected() )
                {
                    iter.remove();
                    noSelection = false;
                }
            }
            // Si il n'y a pas eu de s�lection, on affiche un message
            if ( noSelection )
            {
                ActionMessage message = new ActionMessage( "error.java.compiling.var_selection" );
                errors.add( "ruleSelection", message );
                saveMessages( pRequest, errors );
            }
            // Transformation des param�tres donn�s dans le formulaire pour le projet
            WTransformerFactory.formToObj( JCompilingConfTransformer.class, jCompilingForm, project.getParameters() );
            // On sauvegarde le projet
            new CreateProjectAction().saveProject( pMapping, project, pRequest, pResponse );
            forward = pMapping.findForward( "success" );
        }
        catch ( Exception e )
        {
            // Traitement factoris� des exceptions
            handleException( e, errors, pRequest );
            saveMessages( pRequest, errors );
            // Renvoi vers la page d'erreur
            forward = pMapping.findForward( "total_failure" );
        }
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );
        return forward;
    }
}
