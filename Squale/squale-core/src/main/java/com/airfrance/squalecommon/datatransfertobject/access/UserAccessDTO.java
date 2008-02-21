package com.airfrance.squalecommon.datatransfertobject.access;

import java.util.Date;

/**
 * 
 */
public class UserAccessDTO {

    /**
     * Identifiant (au sens technique) de l'objet
     */
    protected long mId;
    
    /** Le matricule de l'utilisateur concern� */
    private String mMatricule;
    
    /** Date � laquelle l'utilisateur a acc�d� � l'application */
    private Date mDate;

    /**
     * @return l'id de l'objet
     */
    public long getId() {
        return mId;
    }

    /**
     * @param pId l'id de l'objet
     */
    public void setId(long pId) {
        mId = pId;
    }

    /**
     * @return la date d'acc�s
     */
    public Date getDate() {
        return mDate;
    }

    /**
     * @return le matricule de l'utilisateur
     */
    public String getMatricule() {
        return mMatricule;
    }

    /**
     * @param pDate la date d'acc�s
     */
    public void setDate(Date pDate) {
        mDate = pDate;
    }

    /**
     * @param pMatricule le matricule utilisateur
     */
    public void setMatricule(String pMatricule) {
        mMatricule = pMatricule;
    }

}
