package com.airfrance.squaleweb.applicationlayer.formbean.access;

import java.util.Date;

import com.airfrance.squaleweb.applicationlayer.formbean.RootForm;

/**
 * Bean pour les acc�s utilisateur
 */
public class AccessForm
    extends RootForm
{

    /** Le matricule de l'utilisateur concern� */
    private String mMatricule;

    /** Date � laquelle l'utilisateur a acc�d� � l'application */
    private Date mDate;

    /**
     * @return la date d'acc�s
     */
    public Date getDate()
    {
        return mDate;
    }

    /**
     * @return le matricule de l'utilisateur
     */
    public String getMatricule()
    {
        return mMatricule;
    }

    /**
     * @param pDate la date d'acc�s
     */
    public void setDate( Date pDate )
    {
        mDate = pDate;
    }

    /**
     * @param pMatricule le matricule utilisateur
     */
    public void setMatricule( String pMatricule )
    {
        mMatricule = pMatricule;
    }

}
