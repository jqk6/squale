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
package com.airfrance.squalecommon.util.messages;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Classe de manipulation des messages
 */
public class BaseMessages
{
    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactory.getLog( BaseMessages.class );

    /**
     * Instance de ResourceBudle utilis�e.
     */
    private ResourceBundle mResourceBundle;

    /**
     * Constructeur
     * 
     * @param pBundleName du bundle � charger
     */
    protected BaseMessages( String pBundleName )
    {
        mResourceBundle = ResourceBundle.getBundle( pBundleName );
    }

    /**
     * Retourne la cha�ne de caract�re identifi�e par la cl�.
     * 
     * @param pKey nom de la cl�.
     * @return la cha�ne associ�e.
     */
    protected String getBundleString( String pKey )
    {
        String value;
        try
        {
            value = mResourceBundle.getString( pKey );
        }
        catch ( MissingResourceException e )
        {
            String message = CommonMessages.getString( "exception.messages.missing" ) + pKey;
            LOGGER.error( message, e );
            value = "???" + pKey + "???";
        }
        return value;
    }

    /**
     * Retourne la cha�ne de caract�re identifi�e par la cl�.
     * 
     * @param pKey nom de la cl�.
     * @param pValues les valeurs � ins�rer dans la chaine
     * @return la cha�ne associ�e.
     */
    protected String getBundleString( String pKey, Object[] pValues )
    {
        MessageFormat format = new MessageFormat( getBundleString( pKey ) );
        return format.format( pValues );
    }

}
