package com.airfrance.squalecommon.datatransfertobject.rule;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * DTO de crit�re qualit�
 */
public class CriteriumRuleDTO extends QualityRuleDTO {
    /** Pratiques */
    private SortedMap mPractices;
    
    /**
     * @return pratiques
     */
    public SortedMap getPractices() {
        return mPractices;
    }

    /**
     * Ajout d'une pratique
     * @param pPractice pratique
     * @param pWeight la pond�ration
     */
    public void addPractice(PracticeRuleDTO pPractice, Float pWeight) {
        if (mPractices==null) {
            mPractices = new TreeMap();
        }
        mPractices.put(pPractice, pWeight);
    }
}
