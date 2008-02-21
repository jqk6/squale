package com.airfrance.squalecommon.datatransfertobject.result;

import java.io.Serializable;

import com.airfrance.squalecommon.datatransfertobject.rule.FactorRuleDTO;

/**
 * Facteur d'une r�f�rence
 */
public class ReferenceFactorDTO extends FactorRuleDTO implements Serializable {
    /** Valeur associ�e */
    private Float mValue;
    
    /**
     * @return valeur
     */
    public Float getValue() {
        return mValue;
    }

    /**
     * @param pFloat valeur
     */
    public void setValue(Float pFloat) {
        mValue = pFloat;
    }

}
