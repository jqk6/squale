package com.airfrance.squalecommon.enterpriselayer.businessobject.message;

import java.util.Date;

/**
 * News
 * 
 * @hibernate.class 
 * table="News"
 * mutable="true"
 */
public class NewsBO {
    
    /**
     * Identifiant (au sens technique) de l'objet
     */
    private long mId; 
    
    /** la cl� de la news */
    private String key;
 
    /** la date de d�but de validit� de la news */
    private Date beginningDate;
    
    /** la date de fin */
    private Date endDate;
    
    /**
     * @return la cl�
     * 
     * @hibernate.property 
     * name="key" 
     * column="Key" 
     * type="string" 
     * not-null="true" 
     * length="4000"
     */
    public String getKey() {
        return key;
    }

    /**
     * @param newKey la nouvelle cl�
     */
    public void setKey(String newKey) {
        key = newKey;
    }

    /**
     * @return la date de d�but de validit�
     * 
     * @hibernate.property 
     * name="beginningDate" 
     * column="Beginning_Date" 
     * type="timestamp" 
     * not-null="true" 
     * unique="false"
     */
    public Date getBeginningDate() {
        return beginningDate;
    }

    /**
     * @return la date de fin de validit�
     * 
     * @hibernate.property 
     * name="endDate" 
     * column="End_Date" 
     * type="timestamp" 
     * not-null="true" 
     * unique="false"
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param newBeginningDate la nouvelle date de d�but de validit� 
     */
    public void setBeginningDate(Date newBeginningDate) {
        beginningDate = newBeginningDate;
    }

    /**
     * @param newEndDate la nouvelle date de fin
     */
    public void setEndDate(Date newEndDate) {
        endDate = newEndDate;
    }

    /** 
     * @return l'id
     * 
     * @hibernate.id generator-class="native"
     * type="long" 
     * column="Id" 
     * unsaved-value="-1" 
     * length="19"
     * @hibernate.generator-param name="sequence" value="news_sequence" 
     * 
     */
    public long getId() {
        return mId;
    }

    /**
     * @param newId le nouvel id
     */
    public void setId(long newId) {
        mId = newId;
    }

}
