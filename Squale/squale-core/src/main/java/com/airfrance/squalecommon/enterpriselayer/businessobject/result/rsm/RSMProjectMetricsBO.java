package com.airfrance.squalecommon.enterpriselayer.businessobject.result.rsm;

import com.airfrance.squalecommon.enterpriselayer.businessobject.result.IntegerMetricBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.MeasureBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.MetricBO;

/**
 * @hibernate.subclass discriminator-value="RSMProjectMetrics"
 */
public class RSMProjectMetricsBO
    extends MeasureBO
{

    /**
     * Le nombre de lignes de codes
     */
    private final static String SLOC = "sloc";

    /**
     * Les commentaires
     */
    private final static String COMMENTS = "comments";

    /**
     * Nombre de classes dans le sous-projet
     */
    private final static String NUMBER_OF_CLASSES = "numberOfClasses";

    /**
     * Nombre de m�thodes dans le sous-projet.
     */
    private final static String NUMBER_OF_METHODS = "numberOfMethods";

    /**
     * Nombre lignes de code total = SLOC + COMMENTS
     */
    private final static String NUMBER_OF_CODE_LINES = "numberofcodelines";

    /**
     * Constructeur par d�faut.
     */
    public RSMProjectMetricsBO()
    {
        super();
        getMetrics().put( NUMBER_OF_CLASSES, new IntegerMetricBO() );
        getMetrics().put( NUMBER_OF_METHODS, new IntegerMetricBO() );
        getMetrics().put( SLOC, new IntegerMetricBO() );
        getMetrics().put( COMMENTS, new IntegerMetricBO() );
        getMetrics().put( NUMBER_OF_CODE_LINES, new IntegerMetricBO() );
    }

    /**
     * M�thode d'acc�s � la m�trique SLOC
     * 
     * @return la valeur du sloc
     */
    public Integer getSloc()
    {
        return (Integer) ( (MetricBO) getMetrics().get( SLOC ) ).getValue();
    }

    /**
     * Change la valeur de la m�trique SLOC
     * 
     * @param pSLOC la nouvelle valeur du SLOC
     */
    public void setSloc( Integer pSLOC )
    {
        ( (IntegerMetricBO) getMetrics().get( SLOC ) ).setValue( pSLOC );
        // gere le total, nouveau de comportement de RSM par rapport � PureComments
        manageTotal();

    }

    /**
     * gere le total, nouveau de comportement de RSM par rapport � PureComments
     */
    private void manageTotal()
    {
        // Met � jour le nombre total de lignes de code
        // en faisant la somme
        // On ajoute a chaque fois car on ne sait pas dans quel ordre sont calcul�s les diff�rentes mesures,
        // mais on sait quelles ne sont calcul�es qu'une seule fois
        int newValue = 0;
        if ( ( (IntegerMetricBO) getMetrics().get( SLOC ) ) != null
            && ( (IntegerMetricBO) getMetrics().get( SLOC ) ).getValue() != null )
        {
            newValue += ( (Integer) ( (IntegerMetricBO) getMetrics().get( SLOC ) ).getValue() ).intValue();
        }
        if ( ( (IntegerMetricBO) getMetrics().get( COMMENTS ) ) != null
            && ( (IntegerMetricBO) getMetrics().get( COMMENTS ) ).getValue() != null )
        {
            newValue += ( (Integer) ( (IntegerMetricBO) getMetrics().get( COMMENTS ) ).getValue() ).intValue();
        }
        ( (IntegerMetricBO) getMetrics().get( NUMBER_OF_CODE_LINES ) ).setValue( new Integer( newValue ) );
    }

    /**
     * M�thode d'acc�s � la m�trique commentaires
     * 
     * @return la valeur sur le nombre de lignes de commentaires
     */
    public Integer getComments()
    {
        return (Integer) ( (IntegerMetricBO) getMetrics().get( COMMENTS ) ).getValue();
    }

    /**
     * Change la valeur de la m�trique commentaires
     * 
     * @param pComments la nouvelle valeur du nombre de commentaires
     */
    public void setComments( Integer pComments )
    {
        ( (IntegerMetricBO) getMetrics().get( COMMENTS ) ).setValue( pComments );
        // gere le total, nouveau de comportement de RSM par rapport � PureComments
        manageTotal();
    }

    /**
     * Sets the value of the mNumberOfClasses property.
     * 
     * @param pNumberOfClasses the new value of the mNumberOfClasses property
     * @roseuid 42C416B702DB
     */
    public void setNumberOfClasses( Integer pNumberOfClasses )
    {
        ( (IntegerMetricBO) getMetrics().get( NUMBER_OF_CLASSES ) ).setValue( pNumberOfClasses );
    }

    /**
     * Access method for the mNumberOfMethods property.
     * 
     * @return the current value of the mNumberOfMethods property
     * @roseuid 42C416B70339
     */
    public Integer getNumberOfMethods()
    {
        return (Integer) ( (IntegerMetricBO) getMetrics().get( NUMBER_OF_METHODS ) ).getValue();
    }

    /**
     * Sets the value of the mNumberOfMethods property.
     * 
     * @param pNumberOfMethods the new value of the mNumberOfMethods property
     * @roseuid 42C416B70367
     */
    public void setNumberOfMethods( Integer pNumberOfMethods )
    {
        ( (IntegerMetricBO) getMetrics().get( NUMBER_OF_METHODS ) ).setValue( pNumberOfMethods );
    }

    /**
     * @return le nombre de lignes
     */
    public Integer getNumberofcodelines()
    {
        return (Integer) ( (IntegerMetricBO) getMetrics().get( NUMBER_OF_CODE_LINES ) ).getValue();
    }

}
