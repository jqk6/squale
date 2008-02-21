package com.airfrance.squalecommon.datatransfertobject.config;

/**
 * DTO pour la fr�quence max des audits
 */
public class AuditFrequencyDTO {

    /** L'identifiant (au sens technique) de l'objet */
    private long mId;

    /** Dur�e depuis le dernier acc�s (en nombre de jours) */
    private int mDays;

    /** Fr�quence max d'audit (en nombre de jours) */
    private int mFrequency;

    /**
     * M�thode d'acc�s � mId
     * 
     * @return l'identifiant de l'objet
     */
    public long getId() {
        return mId;
    }

    /**
     * @return la dur�e le depuis dernier acc�s
     */
    public int getDays() {
        return mDays;
    }

    /**
     * @return la fr�quence max d'audit
     */
    public int getFrequency() {
        return mFrequency;
    }

    /**
     * Change la valeur de mId
     * 
     * @param pId la nouvelle valeur de l'identifiant de l'objet
     */
    public void setId(long pId) {
        mId = pId;
    }

    /**
     * @param pDays la dur�e le depuis dernier acc�s
     */
    public void setDays(int pDays) {
        mDays = pDays;
    }

    /**
     * @param pFrequency la fr�quence max d'audit
     */
    public void setFrequency(int pFrequency) {
        mFrequency = pFrequency;
    }
}
