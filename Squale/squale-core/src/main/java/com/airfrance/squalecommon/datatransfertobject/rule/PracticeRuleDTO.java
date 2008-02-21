package com.airfrance.squalecommon.datatransfertobject.rule;

/**
 * DTO d'une pratique qualit�
 */
public class PracticeRuleDTO extends QualityRuleDTO {
    
    /** Indication d'une pratique de type violation de r�gle */
    private boolean mRuleChecking = false;
    /** Formule */
    private AbstractFormulaDTO mFormula;
    /** La fonction de pond�ration */
    private String mWeightingFunction;
    /** parfois on a seulement besoin du type de la formule (graph de r�partition) */
    private String mFormulaType;
    /** l'effort � fournie pour la correction */
    private int mEffort;
    
    
    
    /**
     * @return formule
     */
    public AbstractFormulaDTO getFormula() {
        return mFormula;
    }

    /**
     * @param pFormulaDTO formule
     */
    public void setFormula(AbstractFormulaDTO pFormulaDTO) {
        mFormula = pFormulaDTO;
    }

    /**
     * @return indicateur de rulechecking
     */
    public boolean isRuleChecking() {
        return mRuleChecking;
    }

    /**
     * @param pRuleChecking indicateur de r�gle
     */
    public void setRuleChecking(boolean pRuleChecking) {
        mRuleChecking = pRuleChecking;
    }

    /**
     * @return la fonction de pond�ration
     */
    public String getWeightingFunction() {
        return mWeightingFunction;
    }

    /**
     * @param pWeightingFormula la fonction de pond�ration
     */
    public void setWeightingFunction(String pWeightingFormula) {
        mWeightingFunction = pWeightingFormula;
    }

    /**
     * @return le type de la formule
     */
    public String getFormulaType() {
        return mFormulaType;
    }

    /**
     * @param pFormulaType le type de la formule
     */
    public void setFormulaType(String pFormulaType) {
        mFormulaType = pFormulaType;
    }

    /**
     * @return l'effort de correction
     */
    public int getEffort() {
        return mEffort;
    }

    /**
     * @param pEffort l'effort de correction
     */
    public void setEffort(int pEffort) {
        mEffort = pEffort;
    }

}
