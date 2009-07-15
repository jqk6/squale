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
package org.squale.squaleweb.applicationlayer.action.component;

import javax.servlet.http.HttpServletRequest;

import org.squale.jraf.commons.exception.JrafEnterpriseException;
import org.squale.jraf.helper.AccessDelegateHelper;
import org.squale.jraf.spi.accessdelegate.IApplicationComponent;
import org.squale.squalecommon.datatransfertobject.component.ApplicationConfDTO;
import org.squale.squalecommon.datatransfertobject.component.ComponentDTO;
import org.squale.squalecommon.datatransfertobject.component.AuditDTO;
import org.squale.squaleweb.applicationlayer.formbean.creation.CreateApplicationForm;
import org.squale.squaleweb.transformer.ApplicationConfTransformer;
import org.squale.welcom.struts.transformer.WTransformerException;
import org.squale.welcom.struts.transformer.WTransformerFactory;

/**
 */
public class ManageApplicationUtils
{

    /**
     * @param pApplicationId l'id de l'application dont on est en train de modifier la conf
     * @return l'objet d�crivant la conf de l'appli
     * @throws JrafEnterpriseException en cas d'�chec de r�cup�ration des donn�es
     */
    public static ApplicationConfDTO getApplication( String pApplicationId )
        throws JrafEnterpriseException
    {
        ApplicationConfDTO dto = new ApplicationConfDTO();
        dto.setId( Long.parseLong( pApplicationId ) );
        IApplicationComponent ac = AccessDelegateHelper.getInstance( "ApplicationAdmin" );
        Object[] paramIn = { dto };
        // Ex�cution de l'AC
        dto = (ApplicationConfDTO) ac.execute( "getApplicationConf", paramIn );
        return dto;
    }

    /**
     * @param pApplicationId l'id de l'application
     * @param pRequest la requete http
     * @return le createApplicationForm
     * @throws JrafEnterpriseException en cas d'�chec de r�cup�ration
     * @throws WTransformerException en cas de probl�me de transformation
     */
    public static CreateApplicationForm getCreateApplicationForm( String pApplicationId, HttpServletRequest pRequest )
        throws JrafEnterpriseException, WTransformerException
    {
        // Recharge le form
        ApplicationConfDTO appliDto = getApplication( pApplicationId );
        // R�cup�ration de l'audit de jalon programm� si il existe
        Object[] paramIn = new Object[] { new Long( appliDto.getId() ) };
        IApplicationComponent ac = AccessDelegateHelper.getInstance( "ApplicationAdmin" );
        AuditDTO milestoneAudit = (AuditDTO) ac.execute( "getMilestoneAudit", paramIn );
        // Cr�ation du form pour l'application � confirmer
        Object[] paramIn2 = { appliDto, milestoneAudit };
        CreateApplicationForm form =
            (CreateApplicationForm) WTransformerFactory.objToForm( ApplicationConfTransformer.class, paramIn2 );
        // Mise � jour de la form dans la session
        pRequest.getSession().setAttribute( "createApplicationForm", form );
        return form;

    }

    /**
     * Retourne le dernier audit de suivi programm� d'une application. (<code>null</code> si aucun audit de suivi
     * n'est programm� pour l'application)
     * 
     * @param pApplicationID l'id de l'application
     * @return le dernier audit de suivi programm� d'une application
     * @throws JrafEnterpriseException si erreur
     */
    public static AuditDTO getLastNotAttemptedAudit( long pApplicationID )
        throws JrafEnterpriseException
    {
        AuditDTO lAuditDTO = null;

        ComponentDTO lApplicationDTO = new ComponentDTO();
        lApplicationDTO.setID( pApplicationID );

        IApplicationComponent ac = AccessDelegateHelper.getInstance( "Component" );
        Object[] paramIn = new Object[] { lApplicationDTO };
        lAuditDTO = (AuditDTO) ac.execute( "getLastNotAttemptedAudit", paramIn );

        return lAuditDTO;
    }
}
