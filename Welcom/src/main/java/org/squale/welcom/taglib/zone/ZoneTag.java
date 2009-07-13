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
 * Cr�� le 16 janv. 06
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package org.squale.welcom.taglib.zone;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.squale.welcom.struts.lazyLoading.WLazyLoadingPersistance;
import org.squale.welcom.struts.lazyLoading.WLazyLoadingType;


/**
 * @author M325379 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class ZoneTag
    extends BodyTagSupport
{
    /**
     * 
     */
    private static final long serialVersionUID = -5331108653338911374L;

    /** le nom de la zone */
    private String name = "";

    /**
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
     */
    public int doEndTag()
        throws JspException
    {
        // Recupere le body evalu�
        final BodyContent bodyContent = getBodyContent();

        if ( bodyContent != null )
        {
            final String value = bodyContent.getString().trim();
            bodyContent.clearBody();

            if ( value.length() > 0 )
            {
                WLazyLoadingPersistance.find( pageContext.getSession() ).add( WLazyLoadingType.ZONE, name, value );
            }
        }

        // Continue processing this page
        release();
        return ( EVAL_PAGE );
    }

    /**
     * @return name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param string name
     */
    public void setName( final String string )
    {
        name = string;
    }

}
