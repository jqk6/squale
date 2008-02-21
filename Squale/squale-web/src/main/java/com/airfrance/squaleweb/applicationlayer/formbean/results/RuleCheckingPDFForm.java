package com.airfrance.squaleweb.applicationlayer.formbean.results;

/**
 * Bean repr�sentant les transgressions pour l'export PDF
 */
public class RuleCheckingPDFForm extends RulesCheckingForm {

    /** D�tails de la r�gles */
    private RuleCheckingItemsListForm mDetails;
    
    /**
     * Constructeur par d�faut
     */
    public RuleCheckingPDFForm() {
        this("", "", 0, new RuleCheckingItemsListForm());
    }
    
    /**
     * @param pNameRule le nom de la r�gle
     * @param pSeverity la s�v�rit� de la r�gle
     * @param pTransgressionsNumber le nombre de transgressions
     * @param pItemsForm les d�tails de la r�gles
     */
    public RuleCheckingPDFForm(String pNameRule, String pSeverity, int pTransgressionsNumber, RuleCheckingItemsListForm pItemsForm) {
        mNameRule = pNameRule;
        mSeverity = pSeverity;
        mTransgressionsNumber = pTransgressionsNumber;
        mDetails = pItemsForm;
    }

    /**
     * @return les d�tails de la r�gles
     */
    public RuleCheckingItemsListForm getDetails() {
        return mDetails;
    }

    /**
     * @param pItems les d�tails de la r�gles
     */
    public void setDetails(RuleCheckingItemsListForm pItems) {
        mDetails = pItems;
    }

}
