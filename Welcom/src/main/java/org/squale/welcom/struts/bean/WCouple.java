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
 * Cr�� le 24 mai 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package org.squale.welcom.struts.bean;

import java.util.Comparator;

/**
 * Classe WCouple
 */
public class WCouple
    implements Comparator
{
    /** la value */
    private String value = "";

    /** le label */
    private String label = "";

    /**
     * Constructeur
     * 
     * @param pValue la value
     * @param pLabel le label
     */
    public WCouple( final String pValue, final String pLabel )
    {
        value = pValue;
        label = pLabel;
    }

    /**
     * Returns the label.
     * 
     * @return String
     */
    public String getLabel()
    {
        return label;
    }

    /**
     * Returns the value.
     * 
     * @return String
     */
    public String getValue()
    {
        return value;
    }

    /**
     * Sets the label.
     * 
     * @param pLabel The label to set
     */
    public void setLabel( final String pLabel )
    {
        label = pLabel;
    }

    /**
     * Sets the value.
     * 
     * @param pValue The value to set
     */
    public void setValue( final String pValue )
    {
        value = pValue;
    }

    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare( final Object o1, final Object o2 )
    {
        return ( (String) o1 ).compareTo( (String) o2 );
    }
}