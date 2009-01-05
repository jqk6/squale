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
 * Cr�� le 6 mai 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.struts.util;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.util.RequestUtils;

/**
 * @author M327837 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class WRequestUtils
{

    /** Logger */
    private final static Log logger = LogFactory.getLog( WRequestUtils.class );

    /**
     * Look up and return a message string, based on the specified parameters.
     * 
     * @param pageContext The PageContext associated with this request
     * @param key Message key to be looked up and returned
     * @return message string
     */
    public static String message( final PageContext pageContext, final String key )
    {
        return ( message( pageContext, key, null ) );
    }

    /**
     * Look up and return a message string, based on the specified parameters.
     * 
     * @param pageContext The PageContext associated with this request
     * @param key Message key to be looked up and returned
     * @param args Replacement parameters for this message
     * @return message string saved in the request already)
     */
    public static String message( final PageContext pageContext, final String key, final Object args[] )
    {
        try
        {
            logger.debug( "Message : "
                + RequestUtils.message( pageContext, Globals.MESSAGES_KEY, Globals.LOCALE_KEY, key, args ) );
            logger.debug( "Message locale : " + pageContext.getSession().getAttribute( Globals.LOCALE_KEY ) );
            return RequestUtils.message( pageContext, Globals.MESSAGES_KEY, Globals.LOCALE_KEY, key, args );
        }
        catch ( final JspException e )
        {
            return "";
        }
    }
}