/*
 * Cr�� le 16 d�c. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.squalecommon.enterpriselayer.businessobject.result;

/**
 * @author m401540
 * @version 1.0
 * @hibernate.subclass discriminator-value="Int"
 */
public class IntegerMetricBO
    extends NumberMetricBO
{

    /**
     * Valeur enti�re du m�rique
     */
    protected Integer mValue;

    /**
     * Access method for the mValue property.
     * 
     * @return the current value of the mName property
     * @hibernate.property name="Value" column="Number_val" type="integer" not-null="false" unique="false" update="true"
     *                     insert="true" 
     */
    public Object getValue()
    {
        return mValue;
    }

    /**
     * Sets the value of the mValue property.
     * 
     * @param pValue the new value of the mValue property
     */
    public void setValue( Object pValue )
    {
        mValue = (Integer) pValue;
    }

    /**
     * Sets the value of the mValue property as an int.
     * 
     * @param pValue the new value of the mValue property
     */
    public void setValue( int pValue )
    {
        mValue = new Integer( pValue );
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.airfrance.squalecommon.enterpriselayer.businessobject.result.MetricBO#isPrintable()
     */
    public boolean isPrintable()
    {
        return true;
    }

}
