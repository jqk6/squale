/**
 * Copyright (C) 2008-2010, Squale Project - http://www.squale.org
 *
 * This file is part of Squale.
 *
 * Squale is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * Squale is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Squale.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.squale.squalix.tools.external.bugtracking;

import org.squale.squalecommon.enterpriselayer.businessobject.result.external.bugtracking.ExtBugTrackingMetricsBO;
import org.squale.squalix.tools.external.AbstractExtTask;

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
