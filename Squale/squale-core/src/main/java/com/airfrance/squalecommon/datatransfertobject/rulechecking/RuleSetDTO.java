package com.airfrance.squalecommon.datatransfertobject.rulechecking;

import java.util.Date;

/**
 * DTO d'un RuleSet
 */
public class RuleSetDTO {
    /**
     * Identifiant (au sens technique) de l'objet
     */
    protected long mId = -1;
    /** Nom */
    private String mName;
    /**
     * Date de la creation de la regle de calcul d'un r�sultat qualit�
     */
    private Date mDateOfUpdate;
    /**
     * Access method for the mId property.
     * @return   the current value of the mId property
     */
    public long getId() {
        return mId;
    }
    /**
     * Sets the value of the mId property.
     * 
     * @param pId the new value of the mId property
     **/
    
    public void setId(long pId) {
        mId = pId;
    }

    /**
     * @return nom
     */
    public String getName() {
        return mName;
    }

    /**
     * @param pName nom
     */
    public void setName(String pName) {
        mName = pName;
    }
    /**
     * R�cup�re la date de mise � jour
     * @return la date de mise � jour
     */
    public Date getDateOfUpdate() {
        return mDateOfUpdate;
    }
    
    /**
     * Affecte la date de mise � jour
     * @param pDate la date de mise � jour
     */
    public void setDateOfUpdate(Date pDate) {
        mDateOfUpdate = pDate;
    }

}
