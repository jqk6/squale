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
package org.squale.squalecommon.enterpriselayer.facade.rule.xml;

import java.io.InputStream;
import java.util.Collection;

import org.squale.squalecommon.SqualeTestCase;
import org.squale.squalecommon.enterpriselayer.businessobject.rule.ConditionFormulaBO;
import org.squale.squalecommon.enterpriselayer.businessobject.rule.CriteriumRuleBO;
import org.squale.squalecommon.enterpriselayer.businessobject.rule.FactorRuleBO;
import org.squale.squalecommon.enterpriselayer.businessobject.rule.PracticeRuleBO;
import org.squale.squalecommon.enterpriselayer.businessobject.rule.QualityGridBO;
import org.squale.squalecommon.enterpriselayer.businessobject.rule.SimpleFormulaBO;
import org.squale.squalecommon.enterpriselayer.facade.rule.FormulaMeasureExtractor;

/**
 * Test d'importation au format XML de la grille Le test v�rifie essentiellement que la mapping est correct au niveau du
 * digester en s'assurant que tous les attributs pr�sents dans le fichier XML sont bien mapp�s dans le classes m�tier
 */
public class GridImportTest
    extends SqualeTestCase
{

    /**
     * Importation d'une grille Ce test v�rifie la pr�sence de chacun des attributs pr�sents dans le fichier XML
     */
    public void testImportGrid()
    {
        StringBuffer errors = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream( "data/grid/grid_simple.xml" );
        Collection grids;
        grids = new GridImport().importGrid( stream, errors );
        assertEquals( 1, grids.size() );
        assertTrue( errors.length() == 0 );
        QualityGridBO grid = (QualityGridBO) grids.iterator().next();
        // V�rification des informations de la grille
        assertEquals( "grid1", grid.getName() );
        assertEquals( 1, grid.getFactors().size() );
        // V�rification des informations du facteur
        FactorRuleBO factor = (FactorRuleBO) grid.getFactors().first();
        assertEquals( "factor1", factor.getName() );
        assertEquals( 1, factor.getCriteria().size() );
        // V�rification des informations du crit�re
        CriteriumRuleBO criterium = (CriteriumRuleBO) factor.getCriteria().firstKey();
        assertEquals( "criterium1", criterium.getName() );
        assertEquals( new Float( 1.0 ), (Float) factor.getCriteria().get( criterium ) );
        assertEquals( 3, criterium.getPractices().size() );
        // V�rification des informations des pratiques
        PracticeRuleBO[] practices =
            (PracticeRuleBO[]) criterium.getPractices().keySet().toArray( new PracticeRuleBO[] {} );
        int index = 0;
        PracticeRuleBO practice;

        FormulaMeasureExtractor measureExtactor = new FormulaMeasureExtractor();
        // La premi�re pratique ne contient pas de formule
        practice = practices[index++];
        assertEquals( "practice" + index, practice.getName() );
        assertNull( practice.getFormula() );
        assertEquals( "no measure", 0, measureExtactor.getUsedMeasures( practice.getFormula() ).length );
        // La premi�re pratique contient la fonction de pond�ration par d�faut
        assertEquals( PracticeRuleBO.WEIGHTING_FUNCTION, practice.getWeightFunction() );

        // La deuxi�me pratique contient une formule avec conditions
        practice = practices[index++];
        assertEquals( "practice" + index, practice.getName() );
        assertNotNull( practice.getFormula() );
        assertNotNull( measureExtactor.getUsedMeasures( practice.getFormula() ) );
        // V�rification de la formule
        assertTrue( practice.getFormula() instanceof ConditionFormulaBO );
        ConditionFormulaBO condFormula = (ConditionFormulaBO) practice.getFormula();
        assertEquals( "class", condFormula.getComponentLevel() );
        assertEquals( 1, condFormula.getMeasureKinds().size() );
        assertEquals( "mccabe", condFormula.getMeasureKinds().iterator().next() );
        assertEquals( "mccabe.wmc >= 8", condFormula.getTriggerCondition() );
        assertEquals( "mccabe.maxvg >= 0.5*mccabe.sumvg", condFormula.getMarkConditions().get( 0 ) );
        assertEquals( "mccabe.maxvg >= 0.4*mccabe.sumvg", condFormula.getMarkConditions().get( 1 ) );
        assertEquals( "mccabe.maxvg >= 0.3*mccabe.sumvg", condFormula.getMarkConditions().get( 2 ) );
        // La deuxi�me pratique contient la fonction de pond�ration par d�faut
        assertEquals( PracticeRuleBO.WEIGHTING_FUNCTION, practice.getWeightFunction() );
        // elle a un effort de correction de 2
        assertEquals( 2, practice.getEffort() );

        // La trois�me pratique contient une formule simple
        practice = practices[index++];
        assertEquals( "practice" + index, practice.getName() );
        assertNotNull( practice.getFormula() );
        assertNotNull( measureExtactor.getUsedMeasures( practice.getFormula() ) );
        // La troisi�me pratique contient une fonction de pond�ration
        assertEquals( "lambda x:9**-x", practice.getWeightFunction() );
        // elle a un effort de correction par d�faut
        assertEquals( PracticeRuleBO.EFFORT, practice.getEffort() );
        // V�rification de la formule
        assertTrue( practice.getFormula() instanceof SimpleFormulaBO );
        SimpleFormulaBO simpleFormula = (SimpleFormulaBO) practice.getFormula();
        assertEquals( "class", simpleFormula.getComponentLevel() );
        assertEquals( 1, simpleFormula.getMeasureKinds().size() );
        assertEquals( "mccabe", simpleFormula.getMeasureKinds().iterator().next() );
        assertEquals( "", simpleFormula.getTriggerCondition() );
        assertEquals( "mccabe.maxvg / mccabe.sumvg", simpleFormula.getFormula() );
    }

}
