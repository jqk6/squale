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
package org.squale.squaleweb.comparator;

import java.util.Comparator;

import org.squale.squaleweb.applicationlayer.formbean.results.ComponentForm;

/**
 * Compare deux ComponentForm en fonction d'une m�trique index�e.
 * 
 * @author M400842
 */
public class ComponentMetricsComparator
    implements Comparator
{

    /**
     * Index de la m�trique � comparer
     */
    private int mIndex = -1;

    /**
     * Constructeur.<br />
     * Si l'index de la m�trique est �gal � -1, alors les composants sont compar�s par rapport � leurs noms.
     * 
     * @param pIndex l'index de la metrique � comparer.
     */
    public ComponentMetricsComparator( int pIndex )
    {
        mIndex = pIndex;
    }

    /**
     * Compare deux objets composants (instances de ComponentForm)
     * 
     * @param pO1 le premier composant � comparer.
     * @param pO2 le second composant � comparer.
     * @return la valeur de la comparaison.
     */
    public int compare( Object pO1, Object pO2 )
    {
        int value = 0;
        if ( -1 == mIndex )
        {
            value = ( (ComponentForm) pO1 ).getName().compareTo( ( (ComponentForm) pO2 ).getName() );
        }
        else
        {
            value = compare( (ComponentForm) pO1, (ComponentForm) pO2 );
        }
        return value;
    }

    /**
     * Compare les m�triques index�s par mIndex des formulaires.
     * 
     * @param pForm1 le premier composant � comparer.
     * @param pForm2 le second composant � comparer.
     * @return la valeur de la comparaison.
     */
    private int compare( final ComponentForm pForm1, final ComponentForm pForm2 )
    {
        Number value1 = (Number) pForm1.getMetrics().get( mIndex );
        Number value2 = (Number) pForm2.getMetrics().get( mIndex );
        // System.err.println("Comparaison de : " + value1.floatValue() + " et " + value2.floatValue() + " donne : " +
        // (int)(value1.floatValue() - value2.floatValue()));
        return (int) ( value1.floatValue() - value2.floatValue() );
    }

}
