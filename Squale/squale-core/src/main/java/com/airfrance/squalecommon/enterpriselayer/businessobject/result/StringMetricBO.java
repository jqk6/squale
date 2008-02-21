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
 * 
 * @hibernate.subclass
 * discriminator-value="Str"
 */
public class StringMetricBO extends MetricBO {

    /**
     * Valeur chaine du m�rique
     */
    protected String mValue;

    /**
     * Access method for the mValue property.
     * 
     * @return   the current value of the mName property
     * 
     * @hibernate.property 
     * name="Value" 
     * column="String_val" 
     * type="string" 
     * length=4000
     * not-null="false" 
     * unique="false"
     * 
     */
    public Object getValue() {
        return mValue;
    }

    /**
     * Sets the value of the mValue property.
     * 
     * @param pValue the new value of the mValue property
     */
    public void setValue(Object pValue) {
        mValue = (String) pValue;
    }
    /** (non-Javadoc)
     * @see com.airfrance.squalecommon.enterpriselayer.businessobject.result.MetricBO#isPrintable()
     */
    public boolean isPrintable() {
        return true;
    }   


}
