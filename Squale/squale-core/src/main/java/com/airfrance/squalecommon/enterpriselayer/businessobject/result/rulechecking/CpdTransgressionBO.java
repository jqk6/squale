package com.airfrance.squalecommon.enterpriselayer.businessobject.result.rulechecking;

import com.airfrance.squalecommon.enterpriselayer.businessobject.result.IntegerMetricBO;

/**
 * R�sultat de transgression de Cpd
 * Une transgression de copy/paste contient le nombre total de lignes dupliqu�es,
 * et pour chaque langage le d�tail des transgressions ainsi que le nombre total
 * de lignes dupliqu�es pour un langage.
 * @hibernate.subclass
 * discriminator-value="CpdTransgression"
 */
public class CpdTransgressionBO extends RuleCheckingTransgressionBO {
    /**
     * Nombre de lignes dupliqu�es au total
     */
    private final static String DUPLICATE_LINES_NUMBER="dupLinesNumber";

    /**
     * Constructeur
     * @roseuid 42B9751A0293
     */
    public CpdTransgressionBO() {
        super();
        IntegerMetricBO metric = new IntegerMetricBO();
        metric.setValue(new Integer(0));
        metric.setMeasure(this);
        getMetrics().put(DUPLICATE_LINES_NUMBER, metric);
    }

    /**
     * Access method for the mLinesNumber property.
     * 
     * @return   the current value of the mLinesNumber property
     * 
     */
    public Integer getDuplicateLinesNumber() {
        return (Integer) ((IntegerMetricBO) getMetrics().get(DUPLICATE_LINES_NUMBER)).getValue();
    }

    /**
     * Sets the value of the mLinesNumber property.
     * 
     * @param pLinesNumber the new value of the mLinesNumber property
     */
    public void setDuplicateLinesNumber(Integer pLinesNumber) {
        ((IntegerMetricBO) getMetrics().get(DUPLICATE_LINES_NUMBER)).setValue(pLinesNumber);
    }
    
    /**
     * Incr�ment du nombre de lignes dupliqu�es
     * @param pLineNumber nombre de lignes � ajouter
     */
    public void incrementDuplicateLinesNumber(int pLineNumber) {
        setDuplicateLinesNumber(new Integer(pLineNumber+getDuplicateLinesNumber().intValue()));
    }
    
    /**
     * Cr�ation lazy de la m�trique
     * Les m�triques de lignes de code dupliqu�es sont d�pendantes du langage, elles sont donc
     * cr�es de fa�on dynamique suivant le besoin
     * @param pLanguage langage
     * @return m�trique asscoci�e
     */
    private IntegerMetricBO getMetricForLanguage(String pLanguage) {
        String pMetricKey = pLanguage+".nb";
        IntegerMetricBO result = (IntegerMetricBO) getMetrics().get(pMetricKey);
        if (result==null) {
            result = new IntegerMetricBO();
            getMetrics().put(pMetricKey, result);
        }
        return result;
    }
    
    /**
     * 
     * @param pLanguage langage
     * @return nombre de lignes dupliqu�es pour le langage
     */
    public Integer getDuplicateLinesForLanguage(String pLanguage) {
        return (Integer) getMetricForLanguage(pLanguage).getValue();
    }

    /**
     * 
     * @param pLanguage langage
     * @param pValue valeur
     */
    public void setDuplicateLinesForLanguage(String pLanguage, Integer pValue) {
        getMetricForLanguage(pLanguage).setValue(pValue);
    }
}
