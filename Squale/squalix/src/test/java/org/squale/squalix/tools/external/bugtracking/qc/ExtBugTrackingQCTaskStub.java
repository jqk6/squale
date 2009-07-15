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
package org.squale.squalix.tools.external.bugtracking.qc;

/**
 * 
 */
public class ExtBugTrackingQCTaskStub
    extends ExtBugTrackingQCTask
{

    /**
     * R�cup�ration aupr�s du logiciel externe du nombre total de defects
     * 
     * @return le nombre total de defects
     */
    public int getTaskNbDefects()
    {
        int nbDef = 20;
        return nbDef;
    }

    /**
     * R�cup�ration aupr�s du logiciel externe du nombre de defects ouvert
     * 
     * @return le nombre de defects ouvert
     */
    public int getTaskOpen()
    {
        int defOpen = 2;
        return defOpen;
    }

    /**
     * R�cup�ration aupr�s du logiciel externe du nombre de defects assign�
     * 
     * @return le nombre de defects assign�s
     */
    public int getTaskAssigned()
    {
        int defAssigned = 4;
        return defAssigned;
    }

    /**
     * R�cup�ration aupr�s du logiciel externe du nombre de defects trait�
     * 
     * @return le nombre de defects trait�s
     */
    public int getTaskTreated()
    {
        int defTreated = 6;
        return defTreated;
    }

    /**
     * R�cup�ration aupr�s du logiciel externe du nombre de defects clot
     * 
     * @return le nombre de defcts clot
     */
    public int getTaskClose()
    {
        int defClose = 8;
        return defClose;
    }

    /**
     * R�cup�ration aupr�s du logiciel externe du nombre de defects en �volution
     * 
     * @return le nombre de defects en �volution
     */
    public int getTaskEvolution()
    {
        int defEvolution = 15;
        return defEvolution;
    }

    /**
     * R�cup�ration aupr�s du logiciel externe du nombre de defects en anomalie
     * 
     * @return le nombre de defects en anomalie
     */
    public int getTaskAnomaly()
    {
        int defAnomaly = 5;
        return defAnomaly;
    }

    /**
     * R�cup�ration aupr�s du logiciel externe du nombre de defects de niveau haut
     * 
     * @return le nombre de defects de niveau haut
     */
    public int getTaskHigh()
    {
        int defHigh = 3;
        return defHigh;
    }

    /**
     * R�cup�ration aupr�s du logiciel externe du nombre de defects de niveau moyen
     * 
     * @return le nombre de defcts de niveau moyen
     */
    public int getTaskMedium()
    {
        int defMedium = 7;
        return defMedium;
    }

    /**
     * R�cup�ration aupr�s du logiciel externe du nombre de defects de niveau bas
     * 
     * @return le nombre de defects de niveau bas
     */
    public int getTaskLow()
    {
        int defLow = 10;
        return defLow;
    }

}
