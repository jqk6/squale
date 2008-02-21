package com.airfrance.squaleweb.applicationlayer.formbean.creation;

import com.airfrance.squaleweb.applicationlayer.formbean.RootForm;

/**
 * Contient une r�gle qualit�.
 * 
 * @version 1.0
 * @author m400842
 */
public class QualityRuleForm extends RootForm {
    
    /**
     * Type de la r�gle (facteur, critere, pratique)
     */
    private String mType = "";
    /**
     * Nom du facteur
     */
    private String mName = "";
    
    /**
     * Constructeur par d�faut
     *
     */
    public QualityRuleForm(){
    }
    
    /**
     * Constructeur complet.
     * 
     * @param pName le nom de la r�gle.
     * @param pType le type de la r�gle.
     */
    public QualityRuleForm(final String pName, final String pType){
        mName = pName;
        mType = pType;
    }
    
    /**
     * @return le nom de la r�gle
     */
    public String getName() {
        return mName;
    }

    /**
     * @return le type de la r�gle
     */
    public String getType() {
        return mType;
    }

    /**
     * @param pName le nom de la r�gle
     */
    public void setName(String pName) {
        mName = pName;
    }

    /**
     * @param pType le type de la r�gle
     */
    public void setType(String pType) {
        mType = pType;
    }

}
