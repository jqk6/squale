package com.airfrance.squalecommon.enterpriselayer.facade.rule;

import java.util.Collection;
import java.util.Iterator;

import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.AbstractFormulaBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.PracticeRuleBO;

/**
 * Extracteur des mesures utilis�es par une formule Une formule de r�gle qualit� est exprim�e � l'aide de mesures qui
 * sont utilis�es pour la construction du menu top ou du d�tail d'une pratique pour afficher les valeurs des mesures
 * li�es � cette pratique
 */
public class FormulaMeasureExtractor
    implements PracticeRuleBO.MeasureExtractor
{

    /**
     * (non-Javadoc)
     * 
     * @see com.airfrance.squalecommon.enterpriselayer.businessobject.rule.PracticeRuleBO.MeasureExtractor#getUsedMeasures(com.airfrance.squalecommon.enterpriselayer.businessobject.rule.AbstractFormulaBO)
     */
    public String[] getUsedMeasures( AbstractFormulaBO pFormula )
    {
        String[] mUsedMeasures;
        ParameterExtraction extracter = new ParameterExtraction();
        Collection params = extracter.getFormulaParameters( pFormula );
        mUsedMeasures = new String[params.size()];
        Iterator paramsIt = params.iterator();
        int i = 0;
        while ( paramsIt.hasNext() )
        {
            FormulaParameter param = (FormulaParameter) paramsIt.next();
            mUsedMeasures[i] =
                param.getMeasureKind() + "." + pFormula.getComponentLevel() + "." + param.getMeasureAttribute();
            i++;
        }
        return mUsedMeasures;
    }

}
