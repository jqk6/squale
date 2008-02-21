package com.airfrance.squalecommon.datatransfertobject.rule;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * DTO d'un facteur qualit�
 */
public class FactorRuleDTO extends QualityRuleDTO {
    
    /** Criteres */
    private SortedMap mCriteria;
    
    /**
     * Ajout d'un crit�re
     * @param pCriterium crit�re
     * @param pWeight la pond�ration associ�e
     */
    public void addCriterium(CriteriumRuleDTO pCriterium, Float pWeight) {
        if (mCriteria==null) {
            mCriteria = new TreeMap();
        }
        mCriteria.put(pCriterium, pWeight);
    }
    
    /**
     * @return crit�res
     */
    public SortedMap getCriteria() {
        return mCriteria;
    }
}
