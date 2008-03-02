/*
 * Cr�� le 31 janv. 06
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.taglib.access;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.validator.GenericValidator;

import com.airfrance.welcom.outils.Access;
import com.airfrance.welcom.struts.bean.WILogonBeanSecurity;
import com.airfrance.welcom.struts.util.WConstants;
import com.airfrance.welcom.taglib.field.util.LayoutUtils;

/**
 * @author M325379 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class ChangeAccessTag
    extends TagSupport
{
    /**
     * 
     */
    private static final long serialVersionUID = -5439124180702475906L;

    /** attribut */
    private String accessKey = "";

    /** attribut */
    private String name = "";

    /** attribut */
    private String property = "";

    /** le page access (pour le stocker) */
    private String pageAccess = null;

    /**
     * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
     */
    public int doStartTag()
        throws JspException
    {
        if ( GenericValidator.isBlankOrNull( accessKey ) && GenericValidator.isBlankOrNull( name )
            && GenericValidator.isBlankOrNull( property ) )
        {
            throw new JspException( "Aucun attribut sp�cifi� pour le ChangeAccessTag" );
        }

        if ( !GenericValidator.isBlankOrNull( accessKey ) && !GenericValidator.isBlankOrNull( name )
            && !GenericValidator.isBlankOrNull( property ) )
        {
            throw new JspException( "Avec le tag ChangeAccessTag, utiliser soit accessKey, soit name et property" );
        }

        pageAccess = (String) pageContext.getAttribute( "access" );
        String access = null;
        if ( !GenericValidator.isBlankOrNull( accessKey ) )
        {
            final WILogonBeanSecurity lb =
                (WILogonBeanSecurity) pageContext.getSession().getAttribute( WConstants.USER_KEY );
            access = Access.getMultipleSecurityPage( lb, accessKey );
        }
        else
        { // on utilise name et property
            access = (String) LayoutUtils.getBeanFromPageContext( pageContext, name, property );
        }

        if ( access != null )
        {
            pageContext.setAttribute( "access", access );
        }

        return EVAL_PAGE;
    }

    /**
     * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
     */
    public int doEndTag()
        throws JspException
    {
        if ( pageAccess != null )
        {
            pageContext.setAttribute( "access", pageAccess );
        }
        else if ( pageContext.getAttribute( "access" ) != null )
        {
            pageContext.removeAttribute( "access" );
        }
        return EVAL_PAGE;
    }

    /**
     * @see javax.servlet.jsp.tagext.TagSupport#release()
     */
    public void release()
    {
        accessKey = "";
        name = "";
        property = "";
        pageAccess = null;
    }

    /**
     * @return accessKey
     */
    public String getAccessKey()
    {
        return accessKey;
    }

    /**
     * @return name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return property
     */
    public String getProperty()
    {
        return property;
    }

    /**
     * @param string accessKey
     */
    public void setAccessKey( final String string )
    {
        accessKey = string;
    }

    /**
     * @param string name
     */
    public void setName( final String string )
    {
        name = string;
    }

    /**
     * @param string property
     */
    public void setProperty( final String string )
    {
        property = string;
    }

}
