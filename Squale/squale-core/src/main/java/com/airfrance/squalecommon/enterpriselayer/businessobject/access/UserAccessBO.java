package com.airfrance.squalecommon.enterpriselayer.businessobject.access;

import java.util.Calendar;
import java.util.Date;

import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;

/**
 * Repr�sente un acc�s utilisateur
 * 
 * @hibernate.class 
 * table="UserAccess"
 * mutable="true"
 * lazy="true"
 */
public class UserAccessBO {

    /**
     * Identifiant (au sens technique) de l'objet
     */
    protected long mId = -1;
    
    /** L'application concern�e par l'acc�s */
    private ApplicationBO mApplication;

    /** Le matricule de l'utilisateur concern� */
    private String mMatricule;

    /** Date � laquelle l'utilisateur a acc�d� � l'application */
    private Date mDate;

    /**
     * Constructeur par d�faut
     */
    public UserAccessBO() {
        this("", Calendar.getInstance().getTime());
    }

    /**
     * Constructeur
     * @param pMatricule le matricule
     * @param pDate la date
     */
    public UserAccessBO(String pMatricule, Date pDate) {
        mMatricule = pMatricule;
        mDate = pDate;
    }

    /**
     * 
     * @return l'id de l'objet
     * 
     * @hibernate.id generator-class="native"
     * type="long" 
     * column="UserAccessId" 
     * unsaved-value="-1" 
     * length="19"
     * @hibernate.generator-param name="sequence" value="userAccess_sequence" 
     * 
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
     * @return l'application
     * 
     * @hibernate.many-to-one 
     * column="ApplicationId"
     * class="com.airfrance.squalecommon.enterpriselayer.businessobject.component.ApplicationBO"
     * cascade="none"
     * not-null="true"
     */
    public ApplicationBO getApplication() {
        return mApplication;
    }

    /**
     * @return la date d'acc�s
     * 
     * @hibernate.property 
     * name="date" 
     * column="accessDate" 
     * type="timestamp" 
     * not-null="true" 
     */
    public Date getDate() {
        return mDate;
    }
    
    /**
     * @param pAppli l'application
     */
    public void setApplication(ApplicationBO pAppli) {
        mApplication = pAppli;
    }

    /**
     * @return le matricule de l'utilisateur
     * 
     * @hibernate.property 
     * name="matricule" 
     * column="matricule" 
     * type="string" 
     * length="1024"
     * not-null="true"
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

    /** 
     * {@inheritDoc}
     * Un UserAccessBO est �gal � un autre UserAccessBO si ils ont le m�me utilisateur
     * avec la m�me date (sans regarder l'heure) 
     * @param obj {@inheritDoc}
     * @return {@inheritDoc}
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        boolean ret = false;
        if (obj instanceof UserAccessBO) {
            UserAccessBO access = (UserAccessBO) obj;
            // m�me matricule
            ret = getMatricule().equals(access.getMatricule());
            Calendar date1 = Calendar.getInstance();
            date1.setTime(getDate());
            Calendar date2 = Calendar.getInstance();
            date2.setTime(access.getDate());
            // m�me jour, mois, ann�e
            ret &= (date1.get(Calendar.DAY_OF_MONTH) == date2.get(Calendar.DAY_OF_MONTH));
            ret &= (date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH));
            ret &= (date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR));
        }
        return ret;
    }

    /** 
     * {@inheritDoc}
     * @return {@inheritDoc}
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        int ret = super.hashCode();
        if (getDate() != null) {
            ret = getDate().hashCode();
        }
        return ret;
    }

}
