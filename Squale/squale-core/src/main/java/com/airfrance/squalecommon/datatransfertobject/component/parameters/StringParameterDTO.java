package com.airfrance.squalecommon.datatransfertobject.component.parameters;

/**
 */
public class StringParameterDTO
{

    /**
     * La valeur du param�tre
     */
    private String mValue;

    /**
     * Identifiant de l'objet
     */
    private long mId;

    /**
     * Constructeur
     * 
     * @param pValue la valeur du param�tre
     */
    public StringParameterDTO( String pValue )
    {
        mId = -1;
        mValue = pValue;
    }

    /**
     * Constructeur
     */
    public StringParameterDTO()
    {
        mId = -1;
        mValue = "";
    }

    /**
     * @return la valeur du param�tre
     */
    public String getValue()
    {
        return mValue;
    }

    /**
     * @param pValue la nouvelle valeur du param�tre
     */
    public void setValue( String pValue )
    {
        mValue = pValue;
    }

    /**
     * Access method for the mId property.
     * 
     * @return the current value of the mId property
     */
    public long getId()
    {
        return mId;
    }

    /**
     * Sets the value of the mId property.
     * 
     * @param pId the new value of the mId property
     */
    public void setId( long pId )
    {
        mId = pId;
    }

}
