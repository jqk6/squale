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
package org.squale.squalix.messages;

import org.squale.squalecommon.util.messages.BaseMessages;
import java.util.TreeMap;

/**
 * Permet l'externalisation des cha�nes de caract�res
 * 
 * @author M400842
 */
public class Messages
    extends BaseMessages
{
    /** Instance */
    static private Messages mInstance = new Messages();

    /**
     * Constructeur priv� pour �viter l'instanciation
     * 
     * @roseuid 42C1678502C4
     */
    private Messages()
    {
        super( "org.squale.squalix.messages.squalix" );
    }

    /**
     * Retourne la cha�ne de caract�re identifi�e par la cl�.
     * 
     * @param pKey nom de la cl�.
     * @return la cha�ne associ�e.
     * @roseuid 42C1678502C5
     */
    public static String getString( String pKey )
    {
        return mInstance.getBundleString( pKey );
    }

    /**
     * Retourne la cha�ne de caract�re identifi�e par la cl�.
     * 
     * @param pKey nom de la cl�.
     * @param pValues les param�tres
     * @return la cha�ne associ�e.
     * @roseuid 42C1678502C5
     */
    public static String getString( String pKey, String[] pValues )
    {
        return mInstance.getBundleString( pKey, pValues );
    }
    
    /**
     * Indique si une cha�ne existe ou pas pour une cl� donn�e sans g�n�rer d'exception
     * @param pKey nom de la cl�
     * @return Vrai si la cl� existe, False sinon
     */
    public static Boolean existString ( String pKey )
    {
        return mInstance.existBundleString( pKey );
    }
    
    /**
     * Retourne la cha�ne de caract�re identifi�e par la cl�
     * @param pKey nom de la cl�
     * @param pValues valeurs � supl�er dans la cha�ne
     * @return la cha�ne associ�e avec les valeurs supl�es si n�cessaire
     */
    public static String getMessage ( String pKey, String[] pValues )
    {
        if (pValues==null)
        {
            return getString(pKey);
        }
        else
        {
            return getString(pKey,pValues);
        }
    }

}
