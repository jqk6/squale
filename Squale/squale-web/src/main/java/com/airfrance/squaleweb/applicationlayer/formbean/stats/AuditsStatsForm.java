package com.airfrance.squaleweb.applicationlayer.formbean.stats;

import com.airfrance.squaleweb.applicationlayer.formbean.RootForm;

/**
 */
public class AuditsStatsForm extends RootForm {
    
    /**
     * Le nombre d'audits partiels
     */
    private int mNbPartial;

    /**
     * Le nombre total d'audits
     */
    private int mNbTotal;
    
    /**
     * Le nombre d'audits failed
     */
    private int mNbFailed;
    
    /**
     * Le nombre d'audits r�ussis
     */
    private int mNbSuccessfuls;
    
    /**
     * Le nombre d'audits programm�s
     */
    private int mNbNotAttempted;

    /**
     * @return Le nombre d'audits failed
     */
    public int getNbFailed() {
        return mNbFailed;
    }

    /**
     * @return Le nombre d'audits partiels
     */
    public int getNbPartial() {
        return mNbPartial;
    }

    /**
     * @return Le nombre d'audits programm�s
     */
    public int getNbNotAttempted() {
        return mNbNotAttempted;
    }

    /**
     * @return Le nombre d'audits r�ussis
     */
    public int getNbSuccessfuls() {
        return mNbSuccessfuls;
    }

    /**
     * @return Le nombre total d'audits
     */
    public int getNbTotal() {
        return mNbTotal;
    }

    /**
     * @param pNbFailed le nouveau nombre d'audits en �chec
     */
    public void setNbFailed(int pNbFailed) {
        mNbFailed = pNbFailed;
    }

    /**
     * @param pNbNotAttempted le nouveau nombre d'audits programm�s
     */
    public void setNbNotAttempted(int pNbNotAttempted) {
        mNbNotAttempted = pNbNotAttempted;
    }

    /**
     * @param pNbSuccessfuls le nouveau nombre d'audits r�ussis
     */
    public void setNbSuccessfuls(int pNbSuccessfuls) {
        mNbSuccessfuls = pNbSuccessfuls;
    }

    /**
     * @param pNbTotal le nouveau nombre d'audits en �chec
     */
    public void setNbTotal(int pNbTotal) {
        mNbTotal = pNbTotal;
    }

    /**
     * @param pNbPartial le nouveau nombre d'audits partiels
     */
    public void setNbPartial(int pNbPartial) {
        mNbPartial = pNbPartial;
    }

}
