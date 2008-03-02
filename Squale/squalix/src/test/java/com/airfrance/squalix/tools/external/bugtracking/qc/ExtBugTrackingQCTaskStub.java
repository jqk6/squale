package com.airfrance.squalix.tools.external.bugtracking.qc;

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
