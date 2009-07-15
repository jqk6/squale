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
package org.squale.squalix.tools.mccabe;

import org.squale.squalecommon.util.messages.BaseMessages;

/**
 * @author m400842 (by rose)
 * @version 1.0
 */
public class McCabeMessages
    extends BaseMessages
{
    /** Instance */
    static private McCabeMessages mInstance = new McCabeMessages();

    /**
     * Constructeur priv� pour �viter l'instanciation
     * 
     * @roseuid 42D3C09200D8
     */
    private McCabeMessages()
    {
        super( "org.squale.squalix.tools.mccabe.mccabe" );
    }

    /**
     * Retourne la cha�ne de caract�re identifi�e par la cl�.
     * 
     * @param pKey nom de la cl�.
     * @return la cha�ne associ�e.
     * @roseuid 42D3C09200D9
     */
    public static String getString( String pKey )
    {
        return mInstance.getBundleString( pKey );
    }

    /**
     * Obtention d'une cha�ne
     * 
     * @param pKey clef
     * @param pArgument argument
     * @return cha�ne valu�e
     */
    public static String getString( String pKey, Object pArgument )
    {
        return getString( pKey, new Object[] { pArgument } );
    }

    /**
     * @param pKey clef
     * @param pObjects objets
     * @return cha�ne valu�e
     */
    public static String getString( String pKey, Object[] pObjects )
    {
        return mInstance.getBundleString( pKey, pObjects );
    }
}
