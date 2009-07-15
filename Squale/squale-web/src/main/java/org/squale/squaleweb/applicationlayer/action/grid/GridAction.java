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
package com.airfrance.squaleweb.applicationlayer.action.grid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.airfrance.jraf.helper.AccessDelegateHelper;
import com.airfrance.jraf.spi.accessdelegate.IApplicationComponent;
import com.airfrance.squalecommon.datatransfertobject.rule.QualityGridConfDTO;
import com.airfrance.squalecommon.datatransfertobject.rule.QualityGridDTO;
import com.airfrance.squaleweb.applicationlayer.action.accessRights.AdminAction;
import com.airfrance.squaleweb.transformer.GridConfTransformer;
import com.airfrance.squaleweb.transformer.GridListTransformer;
import com.airfrance.welcom.struts.bean.WActionForm;
import com.airfrance.welcom.struts.transformer.WTransformerFactory;

/**
 * Manipulation des grilles qualit�
 */
public class GridAction
    extends AdminAction
{
    /**
     * Affichage de la liste des grilles
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward list( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                               HttpServletResponse pResponse )
    {

        ActionErrors errors = new ActionErrors();
        ActionForward forward = pMapping.findForward( "total_failure" );
        try
        {
            IApplicationComponent ac = AccessDelegateHelper.getInstance( "QualityGrid" );
            Collection grids = (Collection) ac.execute( "getGrids", new Object[] { Boolean.FALSE } );
            // On r�cup�re la liste des noms des grilles qui ne sont li�es � aucun profil ni �
            // aucun audit
            Collection unlinkedGrids = (Collection) ac.execute( "getUnlinkedGrids" );
            WTransformerFactory.objToForm( GridListTransformer.class, (WActionForm) pForm, new Object[] { grids,
                unlinkedGrids } );
            forward = pMapping.findForward( "list" );
        }
        catch ( Exception e )
        {
            handleException( e, errors, pRequest );
        }
        if ( !errors.isEmpty() )
        {
            saveMessages( pRequest, errors );
        }
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );
        return ( forward );
    }

    /**
     * D�tail d'une grille qualit�
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

        ActionErrors errors = new ActionErrors();
        ActionForward forward = pMapping.findForward( "total_failure" );
        Locale local= pRequest.getLocale();
        try
        {
            IApplicationComponent ac = AccessDelegateHelper.getInstance( "QualityGrid" );
            String param = pRequest.getParameter( "gridId" );
            QualityGridDTO dto = new QualityGridDTO();
            dto.setId( Long.parseLong( param ) );
            QualityGridConfDTO conf = (QualityGridConfDTO) ac.execute( "getGrid", new Object[] { dto } );
            WActionForm form = WTransformerFactory.objToForm( GridConfTransformer.class, new Object[] {conf,local} );
            pRequest.getSession().setAttribute( "gridConfigForm", form );
            forward = pMapping.findForward( "detail" );
        }
        catch ( Exception e )
        {
            handleException( e, errors, pRequest );
        }
        if ( !errors.isEmpty() )
        {
            saveMessages( pRequest, errors );
        }
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );
        return ( forward );
    }

    /**
     * Purge d'une grille qualit�
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

        ActionErrors errors = new ActionErrors();
        ActionForward forward = pMapping.findForward( "total_failure" );
        try
        {
            // Transformation en DTO de chaque grille s�lectionn�e
            ArrayList grids =
                (ArrayList) WTransformerFactory.formToObj( GridListTransformer.class, (WActionForm) pForm )[0];
            if ( grids.isEmpty() )
            {
                // Affichage d'une erreur car rien n'est s�lectionn�
                ActionMessage error = new ActionError( "error.invalidSelection" );
                errors.add( ActionErrors.GLOBAL_ERROR, error );
            }
            else
            {
                IApplicationComponent ac = AccessDelegateHelper.getInstance( "QualityGrid" );
                Collection usedGrids = (Collection) ac.execute( "deleteGrids", new Object[] { grids } );
                if ( usedGrids.size() > 0 )
                {
                    // Affichage d'une erreur si des grilles sont utilis�es
                    // avec les noms des grilles non supprim�es
                    ActionMessage error = new ActionError( "error.gridUsed" );
                    errors.add( ActionErrors.GLOBAL_ERROR, error );
                    WActionForm form = WTransformerFactory.objToForm( GridListTransformer.class, usedGrids );
                    pRequest.getSession().setAttribute( "usedGrids", form );
                }
                // On remet � jour la liste des grilles
                Collection newgrids = (Collection) ac.execute( "getGrids", new Object[] { Boolean.FALSE } );
                Collection unlinkedGrids = (Collection) ac.execute( "getUnlinkedGrids" );
                WTransformerFactory.objToForm( GridListTransformer.class, (WActionForm) pForm, new Object[] { newgrids,
                    unlinkedGrids } );
            }
            forward = pMapping.findForward( "list" );
        }
        catch ( Exception e )
        {
            handleException( e, errors, pRequest );
        }
        // Sauvegarde des erreurs
        if ( !errors.isEmpty() )
        {
            saveMessages( pRequest, errors );
        }
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );
        return ( forward );
    }

    /**
     * Modification d'une grille qualit�
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     * @deprecated � ne pas utiliser, il reste des probl�mes de gestion. En attente des specs pour la 2.2
     */
    public ActionForward update( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                 HttpServletResponse pResponse )
    {
        ActionMessages errors = new ActionMessages();
        ActionForward forward = pMapping.findForward( "detail" );
        try
        {
            IApplicationComponent ac = AccessDelegateHelper.getInstance( "QualityGrid" );
            QualityGridConfDTO gridDto =
                (QualityGridConfDTO) WTransformerFactory.formToObj( GridConfTransformer.class, (WActionForm) pForm )[0];
            StringBuffer buf = new StringBuffer();
            ac.execute( "updateGrid", new Object[] { gridDto, buf } );
            if ( buf.length() > 0 )
            {
                ActionMessage error = new ActionError( "error.cannot_update_grid" );
                ActionMessage msg = new ActionError( "grid_import.errors", buf.toString() );
                errors.add( ActionErrors.GLOBAL_ERROR, error );
                errors.add( ActionErrors.GLOBAL_ERROR, msg );
            }
            else
            {
                // mise � jour effectu�e
                ActionMessage msg = new ActionError( "grid_updated", gridDto.getName() );
                errors.add( ActionErrors.GLOBAL_ERROR, msg );
                pRequest.getSession().removeAttribute( "gridConfigForm" );
                forward = pMapping.findForward( "list" );
            }
        }
        catch ( Exception e )
        {
            // Affichage d'une erreur
            ActionMessage error = new ActionError( "error.cannot_update_grid" );
            errors.add( ActionErrors.GLOBAL_ERROR, error );
        }
        // Sauvegarde des erreurs
        if ( !errors.isEmpty() )
        {
            saveMessages( pRequest, errors );
        }
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );
        return ( forward );
    }
}
