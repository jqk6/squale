package com.airfrance.squaleweb.applicationlayer.formbean.rulechecking;

/**
 * Formulaire d'un ruleset CppTest
 */
public class CppTestRuleSetForm extends AbstractRuleSetForm {

    /**
     * Constructeur par d�faut.
     */
    public CppTestRuleSetForm() {
    }

    /** Nom des r�gles CppTest */
    private String mCppTestName;
    /**
     * @return nom
     */
    public String getCppTestName() {
        return mCppTestName;
    }

    /**
     * @param pName nom
     */
    public void setCppTestName(String pName) {
        mCppTestName = pName;
    }
}
