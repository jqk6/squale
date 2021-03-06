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
 * Cr�� le 26 mai 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package org.squale.welcom.struts.transformer;

/**
 * @author M325379 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class UtilConvert
{

    /**
     * Convertit un chaine en Float
     * 
     * @param str Chaine a convertir
     * @return Le Float correspondant ou null si la chaine est vide
     */
    public static Float stringToFloat( final String str )
    {
        if ( ( str != null ) && ( str.length() > 0 ) )
        {
            return Float.valueOf( str );
        }
        else
        {
            return null;
        }
    }

    /**
     * Convertit un chaine en Long
     * 
     * @param str Chaine a convertir
     * @return Le Long correspondant ou null si la chaine est vide
     */
    public static Long stringToLong( final String str )
    {
        if ( ( str != null ) && ( str.length() > 0 ) )
        {
            return Long.valueOf( str );
        }
        else
        {
            return null;
        }
    }

    /**
     * Convertit un chaine en Integer
     * 
     * @param str Chaine a convertir
     * @return Le Integer correspondant ou null si la chaine est vide
     */
    public static Integer stringToInteger( final String str )
    {
        if ( ( str != null ) && ( str.length() > 0 ) )
        {
            return Integer.valueOf( str );
        }
        else
        {
            return null;
        }
    }

    /**
     * Convertit un chaine en Character
     * 
     * @param str Chaine a convertir
     * @return Le Character correspondant ou null si la chaine est vide
     */
    public static Character stringToCharacter( final String str )
    {
        if ( ( str != null ) && ( str.length() > 0 ) )
        {
            return new Character( str.charAt( 0 ) );
        }
        else
        {
            return null;
        }
    }
}