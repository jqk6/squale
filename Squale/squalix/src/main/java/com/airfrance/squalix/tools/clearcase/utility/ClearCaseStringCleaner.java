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
 * Cr�� le 6 sept. 05, par m400832.
 */
package com.airfrance.squalix.tools.clearcase.utility;

import com.airfrance.squalix.tools.clearcase.configuration.ClearCaseConfiguration;

/**
 * @author m400832
 * @version 1.0
 */
public class ClearCaseStringCleaner
{

    /**
     * Cette m�thode retourne une cha�ne en minuscules, sans caract�res sp�ciaux ; i.e. uniquement des lettres, des
     * chiffres et des "<code>_</code>" (qui est utilis� comme caract�re de remplacement).<br />
     * Typiquement :<br />
     * 
     * <pre>
     * a0�&tilde;&amp;�������1245cacahu�es ' hihi#{((ldkqsmiopnncwx,ncxcwx)\\|-||
     * </pre>
     * 
     * deviendra : <br />
     * 
     * <pre>
     * a0__________1245cacahu_es___hihi____ldkqsmiopnncwx_ncxcwx______
     * </pre>
     * 
     * @param pStringToBeCleaned cha�ne � nettoyer.
     * @return la cha�ne nettoy�e.
     */
    public static String getCleanedStringFrom( String pStringToBeCleaned )
    {
        StringBuffer tmp = new StringBuffer();
        char car;

        int i = 0;
        /* tant que l'on est pas arriv� au bout de la cha�ne */
        while ( i < pStringToBeCleaned.length() )
        {

            /* on r�cup�re le caract�re � traiter */
            car = pStringToBeCleaned.charAt( i );

            /* s'il s'agit d'un caract�re sp�cial et/ou accentu� */
            if ( Character.isJavaIdentifierPart( car ) && Character.getNumericValue( car ) >= 0 )
            {

                tmp.append( car );
                /* sinon */
            }
            else
            {
                tmp.append( ClearCaseConfiguration.UNDERSCORE );
            }
            i++;
        }

        /* on retourne la cha�ne nettoy�e */
        return tmp.toString().toLowerCase();
    }
}
