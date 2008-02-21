package com.airfrance.squalecommon.enterpriselayer.businessobject.result.rsm;

import com.airfrance.squalecommon.enterpriselayer.businessobject.result.MeasureBO;

/** 
 * @hibernate.subclass
 * discriminator-value="RSMMetrics"
 */
public class RSMMetricsBO extends MeasureBO{

    /**
     * Cet attribut n'est pas persistant, il repr�sente le nom du composant auquel 
     * sont li�es les donn�es tel que remont� par RSM.<br>
     * L'adaptateur utilise ce nom pour d�terminer l'instance de AbstractComponentBO 
     * correspondante et cr�er la relation.
     */
    protected String mComponentName;

    /**
     * Access method for the mComponentName property.
     * 
     * @return   the current value of the mComponentName property
     * @roseuid 42C416B60319
     */
    public String getComponentName() {
        return mComponentName;
    }

    /**
     * Sets the value of the mComponentName property.
     * 
     * @param pComponentName the new value of the mComponentName property
     * @roseuid 42C416B60329
     */
    public void setComponentName(String pComponentName) {
        mComponentName = pComponentName;
    }

}
