package com.airfrance.squalecommon.enterpriselayer.businessobject.result;

import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.QualityRuleBO;

/**
 * R�sultat d'une pratique
 * 
 * @author m400842
 * @hibernate.subclass discriminator-value="PracticeResult"
 */
public class PracticeResultBO
    extends QualityResultBO
{

    /** La valeur minimale pour qu'une note soit refus�e */
    public static final int REFUSED_MIN = 0;

    /** La valeur jusqu'� laquelle une note est refus�e */
    public static final int REFUSED_MAX = 1;

    /** La valeur minimale pour qu'une note soit accept�e avec r�serve */
    public static final int NEARLY_ACCEPTED_MIN = 1;

    /** La valeur jusqu'� laquelle une note est accept�e avec r�serve */
    public static final int NEARLY_ACCEPTED_MAX = 2;

    /** La valeur minimale pour qu'une note soit accept�e */
    public static final int ACCEPTED_MIN = 2;

    /** La valeur jusqu'� laquelle une note est accept�e */
    public static final int ACCEPTED_MAX = 3;

    /** La valeur qu'une note soit consid�r�e comme excellente */
    public static final int EXCELLENT = 3;

    /**
     * L'index du tableau contenant le nombre de composants qui ont une note=EXCELLENT
     */
    private static final int EXCELLENT_ID = 30;

    /**
     * Contient le nombre de composants ayant chacune des notes.
     */
    private Integer mFloatRepartition[];

    /**
     * Access method for the mRepartition property.
     * 
     * @return the current value of the mRepartition property
     * @hibernate.array table="PracticeResult_Repartition" cascade="none"
     * @hibernate.key column="PracticeResultId"
     * @hibernate.index column="Repartition" type="long" length="19"
     * @hibernate.element column="Repartition_value" type="java.lang.Integer" length="10" not-null="false"
     *                    unique="false"
     * @roseuid 42BACEE30323
     */
    public Integer[] getFloatRepartition()
    {
        return mFloatRepartition;
    }

    /**
     * Sets the value of the mFloatRepartition property.
     * 
     * @param pRepartition the new value of the mRepartition property
     * @roseuid 42BACEE30324
     */
    public void setFloatRepartition( Integer[] pRepartition )
    {
        mFloatRepartition = pRepartition;
    }

    /**
     * Constructeur par d�faut
     * 
     * @roseuid 42C92F120186
     */
    public PracticeResultBO()
    {
        super();
        mFloatRepartition = new Integer[QualityRuleBO.NUMBER_OF_FLOAT_INTERVALS];
        resetRepartitions();
    }

    /**
     * Augmente de un la valeur de la r�partition pour la note pMark : mRepartition[findIndex(pMark)] =
     * mRepartition[findIndex(pMark)] + 1
     * 
     * @param pMark la note permettant de trouver l'index du tableau � incr�menter
     */
    public void incrementRepartition( float pMark )
    {
        // Rempli le tableau de r�partition pour des intervalles de pas = 0.1
        int floatIndex = findFloatIntervalsIndex( pMark );
        Integer newFloatValue = new Integer( mFloatRepartition[floatIndex].intValue() + 1 );
        mFloatRepartition[floatIndex] = newFloatValue;
    }

    /**
     * Retourne l'index correspondant � la note dans le tableau de r�partition
     * 
     * @param pMark la note
     * @return l'index correspondant � la note dans le tableau de r�partition
     */
    private int findFloatIntervalsIndex( float pMark )
    {
        // Les composants non not�s sont plac�s � la fin du tableau de r�partition
        int index = QualityRuleBO.NUMBER_OF_FLOAT_INTERVALS - 1;
        if ( pMark >= 0 )
        {
            if ( pMark >= EXCELLENT )
            {
                index = EXCELLENT_ID;
            }
            else
            { // Note appartient [0,3[
                // Formule qui donne la position dans le tableau
                final int coeff = 10;
                index = (int) ( pMark * coeff );
            }
        }
        return index;
    }

    /**
     * Initialisation des r�partitions
     */
    public void resetRepartitions()
    {
        for ( int i = 0; i < mFloatRepartition.length; i++ )
        {
            mFloatRepartition[i] = new Integer( 0 );
        }
    }

    /**
     * Retourne un tableau de r�partitions pour des intervalles de notes de pas = 1 5 cases: 0 <= note <1, 1<= note <2,
     * 2<= note <3 , note = 3, composants non not�s
     * 
     * @return le tableau d'integers
     */
    public Integer[] getIntRepartition()
    {
        Integer[] tab = new Integer[QualityRuleBO.NUMBER_OF_MARKS];
        int[] intTab = new int[QualityRuleBO.NUMBER_OF_MARKS];
        // Effectue les calculs de cumuls sur des ints
        // change d'indice dans le tableau de cumul toutes les 10 valeurs
        final int bound = 10;
        for ( int i = 0; i < mFloatRepartition.length - 2; i++ )
        {
            intTab[( i / bound )] += mFloatRepartition[i].intValue();
        }
        // Proc�dure sp�ciale pour les composants excellents et les non not�s
        intTab[intTab.length - 2] = mFloatRepartition[mFloatRepartition.length - 2].intValue();
        intTab[intTab.length - 1] = mFloatRepartition[mFloatRepartition.length - 1].intValue();
        // transforme les ints en Integer
        for ( int i = 0; i < intTab.length; i++ )
        {
            tab[i] = new Integer( intTab[i] );
        }
        return tab;
    }

}
