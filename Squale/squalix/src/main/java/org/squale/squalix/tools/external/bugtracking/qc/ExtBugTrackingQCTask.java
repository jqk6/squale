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
package com.airfrance.squalix.tools.external.bugtracking.qc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.squalecommon.daolayer.result.MeasureDAOImpl;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.external.bugtracking.ExtBugTrackingMetricsBO;
import com.airfrance.squalix.core.TaskException;
import com.airfrance.squalix.tools.external.bugtracking.AbstractExtBugTrackingTask;

/**
 *
 * 
 */
public class ExtBugTrackingQCTask
    extends AbstractExtBugTrackingTask
{

    /**
     * Logger
     */
    private static final Log LOGGER = LogFactory.getLog( ExtBugTrackingQCTask.class );

    /**
     * Constructeur par d�faut
     */
    public ExtBugTrackingQCTask()
    {
        mName = "ExtBugTrackingQCTask";
    }

    /**
     * C'est cette m�thode qui est appel� pour l'ex�cution de la tache.
     * 
     * @throws TaskException exception lev�e par la tache
     */
    public void execute()
        throws TaskException
    {
        try
        {
            recup();
            getDefects().setAudit( getAudit() );
            getDefects().setComponent( getProject() );
            getDefects().setTaskName( getName() );
            MeasureDAOImpl.getInstance().create( getSession(), getDefects() );
        }
        catch ( Exception e )
        {
            throw new TaskException( e );
        }
    }

    /**
     * Cette m�thode lance la r�cup�ration des donn�es aupr�s du logiciel externe. Les donn�es r�cup�r�es sont plac�es
     * dans un objet ExtBugTrackingMetricsBO
     * 
     * @throws TaskException exception lev�e par la tache
     */
    public void recup()
        throws TaskException
    {
        ExtBugTrackingMetricsBO measure = new ExtBugTrackingMetricsBO();
        measure.setNumberOfDefects( getTaskNbDefects() );
        measure.setDefectsOpen( getTaskOpen() );
        measure.setDefectsAssigned( getTaskAssigned() );
        measure.setDefectsTreated( getTaskTreated() );
        measure.setDefectsClose( getTaskClose() );
        measure.setDefectsAnomaly( getTaskAnomaly() );
        measure.setDefectsEvolution( getTaskEvolution() );
        measure.setDefectsHigh( getTaskHigh() );
        measure.setDefectsMedium( getTaskMedium() );
        measure.setDefectsLow( getTaskLow() );
        setDefects( measure );
    }

    /**
     * R�cup�ration aupr�s du logiciel externe du nombre total de defects
     * 
     * @return le nombre total de defects
     */
    public int getTaskNbDefects()
    {
        int nbDef = 200;
        LOGGER.info( "" );
        return nbDef;
    }

    /**
     * R�cup�ration aupr�s du logiciel externe du nombre de defects ouvert
     * 
     * @return le nombre de defects ouvert
     */
    public int getTaskOpen()
    {
        int defOpen = 20;
        LOGGER.info( "" );
        return defOpen;
    }

    /**
     * R�cup�ration aupr�s du logiciel externe du nombre de defects assign�
     * 
     * @return le nombre de defects assign�s
     */
    public int getTaskAssigned()
    {
        int defAssigned = 40;
        LOGGER.info( "" );
        return defAssigned;
    }

    /**
     * R�cup�ration aupr�s du logiciel externe du nombre de defects trait�
     * 
     * @return le nombre de defects trait�s
     */
    public int getTaskTreated()
    {
        int defTreated = 60;
        LOGGER.info( "" );
        return defTreated;
    }

    /**
     * R�cup�ration aupr�s du logiciel externe du nombre de defects clot
     * 
     * @return le nombre de defcts clot
     */
    public int getTaskClose()
    {
        int defClose = 80;
        LOGGER.info( "" );
        return defClose;
    }

    /**
     * R�cup�ration aupr�s du logiciel externe du nombre de defects en �volution
     * 
     * @return le nombre de defects en �volution
     */
    public int getTaskEvolution()
    {
        int defEvolution = 150;
        LOGGER.info( "" );
        return defEvolution;
    }

    /**
     * R�cup�ration aupr�s du logiciel externe du nombre de defects en anomalie
     * 
     * @return le nombre de defects en anomalie
     */
    public int getTaskAnomaly()
    {
        int defAnomaly = 50;
        LOGGER.info( "" );
        return defAnomaly;
    }

    /**
     * R�cup�ration aupr�s du logiciel externe du nombre de defects de niveau haut
     * 
     * @return le nombre de defects de niveau haut
     */
    public int getTaskHigh()
    {
        int defHigh = 30;
        LOGGER.info( "" );
        return defHigh;
    }

    /**
     * R�cup�ration aupr�s du logiciel externe du nombre de defects de niveau moyen
     * 
     * @return le nombre de defcts de niveau moyen
     */
    public int getTaskMedium()
    {
        int defMedium = 70;
        LOGGER.info( "" );
        return defMedium;
    }

    /**
     * R�cup�ration aupr�s du logiciel externe du nombre de defects de niveau bas
     * 
     * @return le nombre de defects de niveau bas
     */
    public int getTaskLow()
    {
        int defLow = 100;
        LOGGER.info( "" );
        return defLow;
    }

}
