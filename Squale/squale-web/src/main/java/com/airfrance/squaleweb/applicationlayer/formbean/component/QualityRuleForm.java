package com.airfrance.squaleweb.applicationlayer.formbean.component;

import com.airfrance.squaleweb.applicationlayer.formbean.RootForm;

/**
 * Donn�es synth�tiques d'un facteur
 */
public class QualityRuleForm
    extends RootForm
{
    /** s�parateur des pond�rartions */
    public static final String SEPARATOR = ", ";

    /** Nom du facteur */
    private String mName;

    /** Pond�ration */
    private String mPonderation;

    /** l'id */
    private long id;

    /**
     * @return nom
     */
    public String getName()
    {
        return mName;
    }

    /**
     * @param pString nom
     */
    public void setName( String pString )
    {
        mName = pString;
    }

    /**
     * @return pond�ration
     */
    public String getPonderation()
    {
        return mPonderation;
    }

    /**
     * @param pString pond�ration
     */
    public void setPonderation( String pString )
    {
        mPonderation = pString;
    }

    /**
     * @return l'id
     */
    public long getId()
    {
        return id;
    }

    /**
     * @param newId la nouvelle valeur de l'id
     */
    public void setId( long newId )
    {
        id = newId;
    }

}
