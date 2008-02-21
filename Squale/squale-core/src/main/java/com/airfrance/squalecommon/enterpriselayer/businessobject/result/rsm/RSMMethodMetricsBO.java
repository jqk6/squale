package com.airfrance.squalecommon.enterpriselayer.businessobject.result.rsm;

import com.airfrance.squalecommon.enterpriselayer.businessobject.result.IntegerMetricBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.StringMetricBO;

/**
 * @hibernate.subclass
 * discriminator-value="RSMMethodMetrics"
 */
public class RSMMethodMetricsBO extends RSMMetricsBO {

    /**
     * les commentaires
     */
    private final static String COMMENTS = "comments";

    /**
     * Le nombre de lignes de codes
     */
    private final static String SLOC = "sloc";

    /**
     * Le nom du fichier associ�
      */
    private final static String FILENAME = "fileName";

    /**
      * M�thode d'acc�s � la m�trique commentaires
      * 
      * @return la valeur sur le nombre de lignes de commentaires
      * 
      */
    public Integer getComments() {
        return (Integer) ((IntegerMetricBO) getMetrics().get(COMMENTS)).getValue();
    }

    /**
     * Change la valeur de la m�trique commentaires
     * 
     * @param pComments la nouvelle valeur du nombre de commentaires
     */
    public void setComments(Integer pComments) {
        ((IntegerMetricBO) getMetrics().get(COMMENTS)).setValue(pComments);
    }

    /**
     * Change la valeur de la m�trique nom de fichier
     * 
     * @param pFileName la nouvelle valeur du nom du fichier
     */
    public void setFileName(String pFileName) {
        ((StringMetricBO) getMetrics().get(FILENAME)).setValue(pFileName);
    }

    /**
     * M�thode d'acc�s � la m�trique nom du fichier
     * 
     * @return le nom du fichier
     * 
     */
    public String getFileName() {
        return (String) ((StringMetricBO) getMetrics().get(FILENAME)).getValue();
    }

    /**
     * M�thode d'acc�s � la m�trique SLOC
     * 
     * @return la valeur du sloc
     * 
     */
    public Integer getSloc() {
        return (Integer) ((IntegerMetricBO) getMetrics().get(SLOC)).getValue();
    }

    /**
     * Change la valeur de la m�trique SLOC
     * 
     * @param pSLOC la nouvelle valeur du SLOC
     */
    public void setSloc(Integer pSLOC) {
        ((IntegerMetricBO) getMetrics().get(SLOC)).setValue(pSLOC);
    }

    /**
     * Constructeur par d�faut.
     */
    public RSMMethodMetricsBO() {
        super();
        getMetrics().put(SLOC, new IntegerMetricBO());
        getMetrics().put(COMMENTS, new IntegerMetricBO());
        getMetrics().put(FILENAME, new StringMetricBO());
    }

}
