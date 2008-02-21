package com.airfrance.squalecommon.enterpriselayer.businessobject.config;

/**
 * Param�tre de t�che
 * Une t�che Squalix peut �tre configur�e par des param�tres dynamiques,
 * ceux-ci sont d�finis dans le ficheir de configuration de squalix.
 * Un param�tre est d�fini par un nom et une valeur
 * @hibernate.class 
 * table="TaskParameter"
 * lazy="true"
 */
public class TaskParameterBO {
    /**
     * Identifiant (au sens technique) de l'objet
     */
    protected long mId = -1;
    /**
     * Nom du param�tre
     */
    private String mName;
    /**
     * Valeur du param�tre
     */
    private String mValue;

    /**
     * M�thode d'acc�s pour mId
     * 
     * @return la l'identifiant (au sens technique) de l'objet
     * 
     * @hibernate.id generator-class="native"
     * type="long" 
     * column="TaskParameterId" 
     * unsaved-value="-1" 
     * length="19"
     * @hibernate.generator-param name="sequence" value="taskParameter_sequence" 
     * 
     */
    public long getId() {
        return mId;
    }

    /**
     * Change la valeur de mId
     * 
     * @param pId le nouvel identifiant
     */
    public void setId(long pId) {
        mId = pId;
    }

    /**
     * M�thode d'acc�s � mName
     * 
     * @return le nom
     * 
     * @hibernate.property 
     * name="name" 
     * column="Name" 
     * type="string" 
     * length="255"
     * not-null="true" 
     * 
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
     * 
     * @hibernate.property 
     * name="name" 
     * column="Value" 
     * type="string" 
     * length="255"
     * not-null="true" 
     * 
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
