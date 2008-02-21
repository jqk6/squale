package com.airfrance.squalecommon.datatransfertobject.rule;

import java.io.Serializable;
import java.util.Date;

/**
 * Grille qualit�
 * Toutes les informations de la grille sont disponibles
 */
public class QualityGridConfDTO extends QualityGridDTO implements Serializable {
    /** Date de mise � jour */ 
    private Date mDateOfUpdate;
    /**
     * @return date de mise � jour
     */
    public Date getDateOfUpdate() {
        return mDateOfUpdate;
    }

    /**
     * @param pDate date de mise � jour
     */
    public void setDateOfUpdate(Date pDate) {
        mDateOfUpdate = pDate;
    }

}
