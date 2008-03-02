package com.airfrance.squalecommon.enterpriselayer.businessobject.result.ckjm;

import com.airfrance.squalecommon.enterpriselayer.businessobject.result.IntegerMetricBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.MeasureBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.MetricBO;

/**
 * @hibernate.subclass discriminator-value="CkjmClassMetrics"
 */
public class CkjmClassMetricsBO
    extends MeasureBO
{

    /**
     * Couplage efferent
     */
    private final static String CBO = "cbo";

    /**
     * Couplage afferent
     */
    private final static String CA = "ca";

    /**
     * Constructeur par d�faut.
     */
    public CkjmClassMetricsBO()
    {
        super();
        getMetrics().put( CBO, new IntegerMetricBO() );
        getMetrics().put( CA, new IntegerMetricBO() );
    }

    /**
     * M�thode d'acc�s � la m�trique couplage efferent
     * 
     * @return la valuer du cbo
     */
    public Integer getCbo()
    {
        return (Integer) ( (MetricBO) getMetrics().get( CBO ) ).getValue();
    }

    /**
     * Change la valuer de la m�trique couplage efferent
     * 
     * @param pCbo la nouvelle valeur du cbo
     */
    public void setCbo( int pCbo )
    {
        ( (IntegerMetricBO) getMetrics().get( CBO ) ).setValue( new Integer( pCbo ) );
    }

    /**
     * M�thode d'acc�s � la m�trique couplage afferent
     * 
     * @return la valuer du ca
     */
    public Integer getCa()
    {
        return (Integer) ( (IntegerMetricBO) getMetrics().get( CA ) ).getValue();
    }

    /**
     * Change la valuer de la m�trique couplage afferent
     * 
     * @param pCa la nouvelle valeur du ca
     */
    public void setCa( int pCa )
    {
        ( (IntegerMetricBO) getMetrics().get( CA ) ).setValue( new Integer( pCa ) );
    }
}
