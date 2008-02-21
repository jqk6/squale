package com.airfrance.squaleweb.applicationlayer.formbean.component;


/**
 * Donn�es synth�tiques d'une formule
 */
public class PracticeRuleForm extends QualityRuleForm {
    
    /** L'id de l'objet */
    private long mId;
    /** Formule */
    private FormulaForm mFormula;
    /** La fonction de pond�ration associ�e */
    private String mWeightingFunction;
    /** L'effort n�cessaire � la correction */
    private int mEffort = 1;
    
    /**
     * @return id
     */
    public long getId() {
        return mId;
    }

    /**
     * @param pId id
     */
    public void setId(long pId) {
        mId = pId;
    }
    /**
     * @return formule
     */
    public FormulaForm getFormula() {
        return mFormula;
    }

    /**
     * @param pForm formule
     */
    public void setFormula(FormulaForm pForm) {
        mFormula = pForm;
    }

    /**
     * @return la fonction de pond�ration associ�e
     */
    public String getWeightingFunction() {
        return mWeightingFunction;
    }

    /**
     * @param pWeightingFunction la fonction de pond�ration associ�e
     */
    public void setWeightingFunction(String pWeightingFunction) {
        mWeightingFunction = pWeightingFunction;
    }

    /**
     * @return l'effort
     */
    public int getEffort() {
        return mEffort;
    }

    /**
     * @param pEffort l'effort
     */
    public void setEffort(int pEffort) {
        mEffort = pEffort;
    }

}
