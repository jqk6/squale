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
package com.airfrance.squalecommon.enterpriselayer.facade.rule;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.AbstractFormulaBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.ConditionFormulaBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.FormulaVisitor;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.SimpleFormulaBO;

/**
 * Extraction des param�tres d'une formule Une formule est compos�e de param�tres avec une syntaxe du type xxx.yyy o�
 * xxx est le type de mesure et yyy l'attribut de cette mesure (par exemple mccabe.sumvg)
 */
public class ParameterExtraction
    implements FormulaVisitor
{
    /**
     * Obtention des param�tres d'une formule
     * 
     * @param pFormula formule
     * @return liste des param�tres intervenant dans la formule
     */
    public Collection getFormulaParameters( AbstractFormulaBO pFormula )
    {
        Set result = new HashSet();
        if ( pFormula != null )
        {
            // Extraction des param�tres du trigger
            if ( pFormula.getTriggerCondition() != null )
            {
                extractParameters( pFormula.getMeasureKinds(), pFormula.getTriggerCondition(), result );
            }
            // Extraction des param�tres pour une condition de type
            pFormula.accept( this, result );
        }
        return result;
    }

    /**
     * Extraction des param�tres d'une expression
     * 
     * @param pMeasureKinds types de mesures
     * @param pCondition condition
     * @param pParameters collectiond es param�tres
     */
    private void extractParameters( Collection pMeasureKinds, String pCondition, Collection pParameters )
    {
        StringBuffer buf = new StringBuffer();
        buf.append( "(" );
        Iterator measureKinds = pMeasureKinds.iterator();
        int i = 0;
        while ( measureKinds.hasNext() )
        {
            if ( i > 0 )
            {
                buf.append( '|' );
            }
            buf.append( (String) measureKinds.next() );
            i++;
        }
        // Un param�tre est du type xxx.attribut et pas xxx.methode(
        buf.append( ").([a-zA-Z0-9]+)([^a-zA-Z0-9(]|$)" );
        Pattern pattern = Pattern.compile( buf.toString() );
        Matcher matcher = pattern.matcher( pCondition );
        // On cherche toutes les occurrences de xxx.yyy
        // dans la cha�ne de caract�res pass�e en param�tre
        // xxx �tant un type de mesure et yyy un attribut de cette mesure
        while ( matcher.find() )
        {
            FormulaParameter parameter = new FormulaParameter();
            parameter.setMeasureKind( matcher.group( 1 ) );
            parameter.setMeasureAttribute( matcher.group( 2 ) );
            pParameters.add( parameter );
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.airfrance.squalecommon.enterpriselayer.businessobject.rule.practice.FormulaVisitor#visit(com.airfrance.squalecommon.enterpriselayer.businessobject.rule.practice.ConditionFormulaBO,
     *      java.lang.Object)
     */
    public Object visit( ConditionFormulaBO pConditionFormula, Object pArgument )
    {
        Iterator conditions = pConditionFormula.getMarkConditions().iterator();
        while ( conditions.hasNext() )
        {
            String condition = (String) conditions.next();
            extractParameters( pConditionFormula.getMeasureKinds(), condition, (Collection) pArgument );
        }
        return null;
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.airfrance.squalecommon.enterpriselayer.businessobject.rule.practice.FormulaVisitor#visit(com.airfrance.squalecommon.enterpriselayer.businessobject.rule.practice.SimpleFormulaBO,
     *      java.lang.Object)
     */
    public Object visit( SimpleFormulaBO pSimpleFormula, Object pArgument )
    {
        extractParameters( pSimpleFormula.getMeasureKinds(), pSimpleFormula.getFormula(), (Collection) pArgument );
        return null;
    }

}
