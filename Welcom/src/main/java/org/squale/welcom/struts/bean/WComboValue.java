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
package org.squale.welcom.struts.bean;

import java.util.Collections;
import java.util.Vector;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (31/10/2001 15:40:32)
 * 
 * @author: Fabienne Madaule
 */
public class WComboValue
{
    /** la liste */
    private java.util.Vector liste;

    /**
     * Commentaire relatif au constructeur TypeProcedureListe.
     */
    public WComboValue()
    {
        super();
        liste = new Vector();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (31/10/2001 15:47:30)
     * 
     * @return java.lang.String[]
     */
    public java.util.Vector getListe()
    {
        return liste;
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (31/10/2001 16:49:02)
     * 
     * @return la taille de la liste
     */
    public int getSize()
    {
        return liste.size();
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (31/10/2001 15:47:30)
     * 
     * @param i l'index
     * @return la chaine de caractere a l'index i
     */
    public java.lang.String getValue( final int i )
    {
        return (java.lang.String) liste.elementAt( i );
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (31/10/2001 15:47:30)
     * 
     * @param newListe java.lang.String[]
     */
    public void setListe( final java.util.Vector newListe )
    {
        liste = new Vector( newListe );
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (31/10/2001 15:47:30)
     * 
     * @param newValue la nouvelle value
     */
    public void setValue( final java.lang.String newValue )
    {
        liste.add( newValue );
    }

    /**
     * Ins�rez la description de la m�thode ici. Date de cr�ation : (29/11/2001 10:13:37)
     */
    public void sort()
    {
        Collections.sort( liste );
    }
}