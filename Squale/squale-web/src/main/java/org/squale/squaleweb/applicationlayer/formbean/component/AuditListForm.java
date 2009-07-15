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
/*
 * Cr�� le 8 ao�t 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package org.squale.squaleweb.applicationlayer.formbean.component;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import org.squale.squalecommon.datatransfertobject.component.AuditDTO;
import org.squale.squaleweb.applicationlayer.action.accessRights.BaseDispatchAction;
import org.squale.squaleweb.applicationlayer.formbean.RootForm;
import org.squale.squaleweb.resources.WebMessages;

/**
 * Liste les audits � afficher
 * 
 * @author M400842
 */
public class AuditListForm
    extends RootForm
{
    /** Affichage de tous les audits */
    private boolean mAllAudits = true;

    /** Type d'affichage de all.jsp */
    private String mKind = "all";

    /**
     * Liste des audits
     */
    private List mAudits = new ArrayList();

    /**
     * @return la liste des audits
     */
    public List getAudits()
    {
        return mAudits;
    }

    /**
     * @param pAudits la liste des audits
     */
    public void setAudits( List pAudits )
    {
        mAudits = pAudits;
    }

    /**
     * @param pAllAudits affichage de tous les audits
     */
    public void setAllAudits( boolean pAllAudits )
    {
        mAllAudits = pAllAudits;
    }

    /**
     * @return true si tous les audits sont affich�s
     */
    public boolean isAllAudits()
    {
        return mAllAudits;
    }

    /**
     * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping,
     *      javax.servlet.http.HttpServletRequest)
     * @param mapping le mapping
     * @param request la requ�te
     */
    public void reset( ActionMapping mapping, HttpServletRequest request )
    {
        super.reset( mapping, request );
        // Reinitialisation du checkbox
        mAllAudits = false;
    }

    /**
     * @return type d'audit
     */
    public String getKind()
    {
        return mKind;
    }

    /**
     * @param string type d'audit
     */
    public void setKind( String string )
    {
        mKind = string;
    }

    /**
     * permet d'obtenir la date de l'audit courant sur lequel on travaille sous format affichable.
     * 
     * @param pRequest la requete http
     * @return la date format�e
     */
    public String getDate( HttpServletRequest pRequest )
    {
        String auditDate = WebMessages.getString( pRequest, "project.results.last_audit" );
        if ( pRequest.getSession().getAttribute( BaseDispatchAction.CURRENT_AUDIT_DTO ) != null )
        {
            auditDate =
                ( (AuditDTO) pRequest.getSession().getAttribute( BaseDispatchAction.CURRENT_AUDIT_DTO ) ).getFormattedDate();
        }
        return auditDate;
    }

    /**
     * la liste des audits termin�s Sert notamment pour les stats admins
     */
    private List mTerminatedAudits = new ArrayList();

    /**
     * @return la liste des audits termin�s
     */
    public List getTerminatedAudits()
    {
        return mTerminatedAudits;
    }

    /**
     * @param pList la liste des audits interrompus ou en cours
     */
    public void setTerminatedAudits( List pList )
    {
        mTerminatedAudits = pList;
    }

    /**
     * la liste des audits interrompus ou en cours Sert notamment pour les stats admins
     */
    private List mShutDownAudits = new ArrayList();

    /**
     * @return la liste des audits interrompus ou en cours
     */
    public List getShutDownAudits()
    {
        return mShutDownAudits;
    }

    /**
     * @param pList la liste des audits interrompus ou en cours
     */
    public void setShutDownAudits( List pList )
    {
        mShutDownAudits = pList;
    }

    /**
     * la liste des audits interrompus ou en cours Sert notamment pour les stats admins
     */
    private List mNotAttemptedAudits = new ArrayList();

    /**
     * @return la liste des audits interrompus ou en cours
     */
    public List getNotAttemptedAudits()
    {
        return mNotAttemptedAudits;
    }

    /**
     * @param pList la liste des audits interrompus ou en cours
     */
    public void setNotAttemptedAudits( List pList )
    {
        mNotAttemptedAudits = pList;
    }

    /**
     * la liste des audits partiels ou en �chec Sert pour les admins
     */
    private List mFailedOrPartialAudits = new ArrayList();

    /**
     * @return la liste des audits partiels ou en �chec
     */
    public List getFailedOrPartialAudits()
    {
        return mFailedOrPartialAudits;
    }

    /**
     * @param pList la liste des audits partiels ou en �chec
     */
    public void setFailedOrPartialAudits( List pList )
    {
        mFailedOrPartialAudits = pList;
    }

}
