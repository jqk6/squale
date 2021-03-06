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
package org.squale.welcom.taglib.security;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.squale.welcom.outils.Access;
import org.squale.welcom.struts.bean.WILogonBeanSecurity;
import org.squale.welcom.struts.util.WConstants;


/**
 * @author user
 */
public class CheckPageTag
    extends TagSupport
{
    /**
     * 
     */
    private static final long serialVersionUID = -7021042161423569301L;

    /** logger */
    private static Log log = LogFactory.getLog( CheckPageTag.class );

    /** Nom du bean Welcom */
    private final String name = WConstants.USER_KEY;

    /** Cle d'acc�es */
    protected String accessKey = "";

    /** Contructeur */
    public CheckPageTag()
    {
        super();
    }

    /**
     * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
     */
    public int doEndTag()
        throws JspException
    {
        boolean valid = false;
        final HttpSession session = super.pageContext.getSession();

        // if(session != null && session.getAttribute(name) != null)
        if ( session != null )
        {
            valid = true;
        }

        if ( valid )
        {

            computeMode();

            return EVAL_PAGE;
        }

        try
        {
            session.invalidate();

            final JspWriter out = super.pageContext.getOut();
            out.println( "<html><body><table class=\"wide\"><tr><td bgcolor=\"#e0e0e0\"><tr><td><table class=\"wide\"><tr><td align=\"center\" bgcolor=\"#cacaca\">"
                + "<font face=\"Arial, Helvetica, sans-serif\"><b>Erreurs</b></font></td></tr><tr><td class=\"boxtext\" bgcolor=\"#dae6e6\">&nbsp;-&nbsp;"
                + "<a href=\"/index.jsp\">Session invalid\351e</a></td></tr></table></td></tr></table></body></html>" );
        }
        catch ( final Exception e )
        {
            log.error( e, e );
            throw new JspException( e.toString() );
        }

        return SKIP_PAGE;
    }

    /**
     * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
     */
    public int doStartTag()
        throws JspException
    {
        return 0;
    }

    /**
     * Release
     */
    public void release()
    {
        accessKey = "";
    }

    /**
     * Sp�cifie dans quelmode se trouve la page
     */
    public void computeMode()
    {
        // Recupere le doits stock� dans le profil
        final WILogonBeanSecurity lb = (WILogonBeanSecurity) pageContext.getSession().getAttribute( name );
        final String pageAccess = Access.getMultipleSecurityPage( lb, accessKey );

        // Stocke dans la page le droit
        pageContext.setAttribute( "access", pageAccess );
    }

    /**
     * Returns the access.
     * 
     * @return String
     */
    public String getAccessKey()
    {
        return accessKey;
    }

    /**
     * Sets the access.
     * 
     * @param pAccessKey The access to set
     */
    public void setAccessKey( final String pAccessKey )
    {
        this.accessKey = pAccessKey;
    }
}