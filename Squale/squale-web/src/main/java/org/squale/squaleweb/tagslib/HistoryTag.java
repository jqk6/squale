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
package org.squale.squaleweb.tagslib;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.ResponseUtils;

import org.squale.squaleweb.resources.WebMessages;

/**
 */
public class HistoryTag
    extends TagSupport
{
    /** Attribute name for selected tab */
    public static final String SELECTED_TAB_KEY = "selectedTab";

    /** le nom du (de la) facteur/crit�re/pratique ou du composant */
    private String name;

    /** l'id de la r�gle */
    private String ruleId;

    /** l'id du composant (optionnel, seulement si on vient d'une vue composant) */
    private String componentId;

    /** l'id du projet */
    private String projectId;

    /** L'id de l'audit courant */
    private String auditId;

    /** L'id de l'audit pr�c�dent */
    private String previousAuditId;

    /**
     * indique si la valeur � mettre dans le which= de la requete est un nombre ou une regle
     */
    private String kind;

    /** Image title attribute */
    private String toolTip = "tooltips.history";

    /** Tab selected (if empty, first tab is selected) */
    private String selectedTab = "";

    /**
     * @see javax.servlet.jsp.tagext.TagSupport#doEndTag() {@inheritDoc} M�thode de lancement du tag
     */
    public int doStartTag()
        throws JspException
    {
        ResponseUtils.write( pageContext, getHistory( (HttpServletRequest) pageContext.getRequest() ) );
        return SKIP_BODY;
    }

    /**
     * construit l'url de l'action permettant d'avoir l'historique
     * 
     * @param pRequest la requete Http
     * @return l'url
     */
    private String getHistory( HttpServletRequest pRequest )
    {
        // attention: le rule id est rempli dynamiquement depuis la jsp,
        // il ne faut surtout pas le passer en param�tre de cette p�thode apr�s avoir fait un lookup
        // On cr�e les param�tres � passer dans la requ�te
        StringBuffer complementaryRequest = new StringBuffer( "" );
        // type de la r�gle
        if ( kind != null )
        {
            complementaryRequest.append( "&kind=" );
            complementaryRequest.append( kind );
        }
        // L'id du composant
        if ( componentId != null )
        {
            complementaryRequest.append( "&component=" );
            complementaryRequest.append( componentId );
        }
        // L'id de l'audit courant
        if ( auditId != null )
        {
            complementaryRequest.append( "&currentAuditId=" );
            complementaryRequest.append( auditId );
        }
        // L'id de la'audit pr�c�dent
        if ( previousAuditId != null )
        {
            complementaryRequest.append( "&previousAuditId=" );
            complementaryRequest.append( previousAuditId );
        }
        // L'action appelante
        StringBuffer oldAction = new StringBuffer( pRequest.getContextPath() );
        oldAction.append( ( (ActionMapping) pRequest.getAttribute( Globals.MAPPING_KEY ) ).getPath() );
        oldAction.append( ".do?" );
        // On ajoute les param�tres
        Enumeration enumParams = pRequest.getParameterNames();
        while ( enumParams.hasMoreElements() )
        {
            String paramName = (String) enumParams.nextElement();
            // seleted tab key changed with each tag
            if ( !paramName.equals( SELECTED_TAB_KEY ) )
            {
                oldAction.append( paramName );
                oldAction.append( "=" );
                oldAction.append( pRequest.getParameter( paramName ) );
                oldAction.append( "&" );
            }
        }
        // We add selectedTab attribute
        oldAction.append( SELECTED_TAB_KEY );
        oldAction.append( "=" );
        oldAction.append( selectedTab );
        try
        {
            // On encode par pr�caution
            complementaryRequest.append( "&oldAction=" );
            complementaryRequest.append( URLEncoder.encode( oldAction.toString(), "UTF-8" ) );
        }
        catch ( UnsupportedEncodingException e )
        {
            // On encode pas
            complementaryRequest.append( "&oldAction=" );
            complementaryRequest.append( oldAction );
        }
        String help = WebMessages.getString( pRequest, toolTip );
        // Cr�ation du lien
        StringBuffer link = new StringBuffer( "review.do?action=review&projectId=" );
        link.append( projectId );
        link.append( complementaryRequest );
        link.append( "&which=" );
        link.append( ruleId );
        link.append( "\"" );
        // Cr�ation du tag image
        String image = "<img src=\"images/pictos/icon_history.gif\" title=\"" + help + "\" border=\"0\" />";
        // Cr�ation du tag repr�sentant l'appel � l'historique
        String result = "<a href=\"" + link + "\" class=\"nobottom\">" + image + "</a>";

        return result;
    }

    /**
     * @see javax.servlet.jsp.tagext.TagSupport#doEndTag() {@inheritDoc} M�thode de lancement du tag
     */
    public int doEndTag()
        throws JspException
    {
        return EVAL_PAGE;
    }

    /**
     * @return l'id du composant
     */
    public String getComponentId()
    {
        return componentId;
    }

    /**
     * @return le nom du composant
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return l'id du composant parent
     */
    public String getRuleId()
    {
        return ruleId;
    }

    /**
     * @param newComponentID la nouvelle valeur de l'id du composant
     */
    public void setComponentId( String newComponentID )
    {
        componentId = newComponentID;
    }

    /**
     * @param newName la nouvelle valeur du nom
     */
    public void setName( String newName )
    {
        name = newName;
    }

    /**
     * @param newRuleId la nouvelle valeur de l'id de la r�gle
     */
    public void setRuleId( String newRuleId )
    {
        ruleId = (String) newRuleId;
    }

    /**
     * @return l'attribut kind
     */
    public String getKind()
    {
        return kind;
    }

    /**
     * change le type
     * 
     * @param newKind le nouveau type
     */
    public void setKind( String newKind )
    {
        kind = newKind;
    }

    /**
     * @return l'id du projet
     */
    public String getProjectId()
    {
        return projectId;
    }

    /**
     * @param newId la nouvelle id du projet
     */
    public void setProjectId( String newId )
    {
        projectId = newId;
    }

    /**
     * @return l'id de l'audit courant
     */
    public String getAuditId()
    {
        return auditId;
    }

    /**
     * @return l'id de l'audit pr�c�dent
     */
    public String getPreviousAuditId()
    {
        return previousAuditId;
    }

    /**
     * @param pAuditId l'id de l'audit courant
     */
    public void setAuditId( String pAuditId )
    {
        auditId = pAuditId;
    }

    /**
     * @param pPreviousAuditId l'id de l'audit pr�c�dent
     */
    public void setPreviousAuditId( String pPreviousAuditId )
    {
        previousAuditId = pPreviousAuditId;
    }

    /**
     * @return tooltip
     */
    public String getToolTip()
    {
        return toolTip;
    }

    /**
     * @param pTooltip new tooltip
     */
    public void setToolTip( String pTooltip )
    {
        toolTip = pTooltip;
    }

    /**
     * @return selected tab name
     */
    public String getSelectedTab()
    {
        return selectedTab;
    }

    /**
     * @param pTab selected tab name
     */
    public void setSelectedTab( String pTab )
    {
        selectedTab = pTab;
    }
}
