package com.airfrance.squalix.tools.external.bugtracking;

import com.airfrance.squalecommon.enterpriselayer.businessobject.result.external.bugtracking.ExtBugTrackingMetricsBO;
import com.airfrance.squalix.tools.external.AbstractExtTask;

/**
 * Classe g�n�rique pour les tache externes relatives au Bug Tracking
 */
public abstract class AbstractExtBugTrackingTask
    extends AbstractExtTask
{

    /**
     * objet ExtBugTrackingMetricsBO pour le transfert en base de donn�e des m�triques
     */
    private ExtBugTrackingMetricsBO mDefects;

    /**
     * @return Rnevoi l'objet ExtBugTrackingMetricsBO de la tache
     */
    public ExtBugTrackingMetricsBO getDefects()
    {
        return mDefects;
    }

    /**
     * @param metricsBO objet ExtBugTrackingMetricsBO � ins�rer
     */
    public void setDefects( ExtBugTrackingMetricsBO metricsBO )
    {
        mDefects = metricsBO;
    }

    /**
     * @return renvoi le nombre total de defects
     */
    public abstract int getTaskNbDefects();

    /**
     * @return renvoi le nombre de defects ouvert mais non assign�s
     */
    public abstract int getTaskOpen();

    /**
     * @return renvoi le nombre de defects assign�s mais non trait�
     */
    public abstract int getTaskAssigned();

    /**
     * @return Renvoi le nombre de defects trait� mais non clot
     */
    public abstract int getTaskTreated();

    /**
     * @return renvoi le nombre de defects clot
     */
    public abstract int getTaskClose();

    /**
     * @return renvoi le nombre de defects en �volution
     */
    public abstract int getTaskEvolution();

    /**
     * @return renvoi le nombre de defects en anomalie
     */
    public abstract int getTaskAnomaly();

    /**
     * @return renvoi le nombre de defects de niveau haut
     */
    public abstract int getTaskHigh();

    /**
     * @return renvoi le nombre de defects de niveau moyen
     */
    public abstract int getTaskMedium();

    /**
     * @return renvoi le nombre de defects de niveau bas
     */
    public abstract int getTaskLow();

}
