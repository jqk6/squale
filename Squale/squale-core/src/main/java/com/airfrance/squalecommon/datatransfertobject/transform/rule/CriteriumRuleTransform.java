package com.airfrance.squalecommon.datatransfertobject.transform.rule;

import com.airfrance.squalecommon.datatransfertobject.rule.CriteriumRuleDTO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.CriteriumRuleBO;

/**
 * Transformation d'un crit�re
 */
public class CriteriumRuleTransform {
    /**
     * Conversion
     * @param pCriteriumRule objet � convertir
     * @return r�sultat de la conversion
     */
    public static CriteriumRuleDTO bo2Dto(CriteriumRuleBO pCriteriumRule) {
        CriteriumRuleDTO result = new CriteriumRuleDTO();
        QualityRuleTransform.bo2Dto(result, pCriteriumRule);
        return result;
    }

}
