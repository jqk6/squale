package com.airfrance.squalecommon.datatransfertobject.config;

/**
 * Param�tre de t�che
 */
public class TaskParameterDTO {
    /**
     * Nom du param�tre
     */
    private String mName;
    /**
     * Valeur du param�tre
     */
    private String mValue;

    /**
     * M�thode d'acc�s � mName
     * 
     * @return le nom
     */
    public String getName() {
        return mName;
    }
    
    /**
     * 
     * @param pName nom
     */
    public void setName(String pName) {
        mName = pName;
    }
    
    
    /**
     * M�thode d'acc�s � mValue
     * 
     * @return la valeur
     */
    public String getValue() {
        return mValue;
    }
    
    /**
     * 
     * @param pValue nom
     */
    public void setValue(String pValue) {
        mValue = pValue;
    }

}
