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
 * Cr�� le 16 juin 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.struts.easycomplete;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author M327836 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class WEasyCompleteUtil
{
    /**
     * Filtre une chaine de caract�re pour �chapper tous les carract�re de commande d'expressions r�guli�res
     * 
     * @param ch la chaine � filtrer
     * @return Chaine �chapp�
     */
    public static String filter( String ch )
    {
        final Pattern regexp = Pattern.compile( "(\\p{Punct})" );
        final Matcher m = regexp.matcher( ch );
        final StringBuffer buf = new StringBuffer();
        int start = 0;
        int end = 0;

        while ( m.find() )
        {
            end = m.start();
            buf.append( ch.substring( start, end ) );
            buf.append( "\\" );
            buf.append( m.group() );
            start = m.end();
        }

        buf.append( ch.substring( start ) );
        ch = buf.toString();

        return ch;
    }
}