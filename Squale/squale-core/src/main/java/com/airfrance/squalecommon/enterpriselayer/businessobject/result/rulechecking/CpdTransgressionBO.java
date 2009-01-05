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
package com.airfrance.squalecommon.enterpriselayer.businessobject.result.rulechecking;

import com.airfrance.squalecommon.enterpriselayer.businessobject.result.IntegerMetricBO;

/**
 * R�sultat de transgression de Cpd Une transgression de copy/paste contient le nombre total de lignes dupliqu�es, et
 * pour chaque langage le d�tail des transgressions ainsi que le nombre total de lignes dupliqu�es pour un langage.
 * 
 * @hibernate.subclass discriminator-value="CpdTransgression"
 */
public class CpdTransgressionBO
    extends RuleCheckingTransgressionBO
{
    /**
     * Nombre de lignes dupliqu�es au total
     */
    private final static String DUPLICATE_LINES_NUMBER = "dupLinesNumber";

    /**
     * Constructeur
     * 
     * @roseuid 42B9751A0293
     */
    public CpdTransgressionBO()
    {
        super();
        IntegerMetricBO metric = new IntegerMetricBO();
        metric.setValue( new Integer( 0 ) );
        metric.setMeasure( this );
        getMetrics().put( DUPLICATE_LINES_NUMBER, metric );
    }

    /**
     * Access method for the mLinesNumber property.
     * 
     * @return the current value of the mLinesNumber property
     */
    public Integer getDuplicateLinesNumber()
    {
        return (Integer) ( (IntegerMetricBO) getMetrics().get( DUPLICATE_LINES_NUMBER ) ).getValue();
    }

    /**
     * Sets the value of the mLinesNumber property.
     * 
     * @param pLinesNumber the new value of the mLinesNumber property
     */
    public void setDuplicateLinesNumber( Integer pLinesNumber )
    {
        ( (IntegerMetricBO) getMetrics().get( DUPLICATE_LINES_NUMBER ) ).setValue( pLinesNumber );
    }

    /**
     * Incr�ment du nombre de lignes dupliqu�es
     * 
     * @param pLineNumber nombre de lignes � ajouter
     */
    public void incrementDuplicateLinesNumber( int pLineNumber )
    {
        setDuplicateLinesNumber( new Integer( pLineNumber + getDuplicateLinesNumber().intValue() ) );
    }

    /**
     * Cr�ation lazy de la m�trique Les m�triques de lignes de code dupliqu�es sont d�pendantes du langage, elles sont
     * donc cr�es de fa�on dynamique suivant le besoin
     * 
     * @param pLanguage langage
     * @return m�trique asscoci�e
     */
    private IntegerMetricBO getMetricForLanguage( String pLanguage )
    {
        String pMetricKey = pLanguage + ".nb";
        IntegerMetricBO result = (IntegerMetricBO) getMetrics().get( pMetricKey );
        if ( result == null )
        {
            result = new IntegerMetricBO();
            getMetrics().put( pMetricKey, result );
        }
        return result;
    }

    /**
     * @param pLanguage langage
     * @return nombre de lignes dupliqu�es pour le langage
     */
    public Integer getDuplicateLinesForLanguage( String pLanguage )
    {
        return (Integer) getMetricForLanguage( pLanguage ).getValue();
    }

    /**
     * @param pLanguage langage
     * @param pValue valeur
     */
    public void setDuplicateLinesForLanguage( String pLanguage, Integer pValue )
    {
        getMetricForLanguage( pLanguage ).setValue( pValue );
    }
}
