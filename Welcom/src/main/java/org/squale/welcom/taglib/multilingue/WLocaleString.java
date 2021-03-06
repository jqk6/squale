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
 * Cr�� le 10 janv. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package org.squale.welcom.taglib.multilingue;

import java.util.HashMap;

/**
 * @author M327837 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class WLocaleString
    implements WILocaleString
{
    /** les valeurs (key=locale, value= string dans la locale) */
    private final HashMap strings = new HashMap();

    /** la cle pour retrouver la valeur dans l'ApplicationResource */
    private String key = "";

    /**
     * @see org.squale.welcom.taglib.multilingue#getString()
     */
    public String getString( final String locale )
    {
        return (String) strings.get( locale );
    }

    /**
     * @see org.squale.welcom.taglib.multilingue#setString()
     */
    public void setString( final String locale, final String string )
    {
        strings.put( locale, string );
    }

    /**
     * @return key
     */
    public String getKey()
    {
        return key;
    }

    /**
     * @param string key
     */
    public void setKey( final String string )
    {
        key = string;
    }

}
