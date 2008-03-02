package com.airfrance.squalecommon.datatransfertobject.stats;

/**
 */
public class FactorsStatsDTO
{

    /** le nombre de facteurs accept�s */
    private int mNbFactorsAccepted;

    /** le nombre de facteurs accept�s avec r�serves */
    private int mNbFactorsReserved;

    /** le nombre de facteurs refus�s */
    private int mNbFactorsRefused;

    /**
     * Total
     */
    private int mNbTotal;

    /**
     * @return le nombre de facteurs accept�s
     */
    public int getNbFactorsAccepted()
    {
        return mNbFactorsAccepted;
    }

    /**
     * @return le nombre de facteurs refus�s
     */
    public int getNbFactorsRefused()
    {
        return mNbFactorsRefused;
    }

    /**
     * @return le nombre de facteurs accept�s avec r�serves
     */
    public int getNbFactorsReserved()
    {
        return mNbFactorsReserved;
    }

    /**
     * @param pNbAccepted le nombre de facteurs accept�s
     */
    public void setNbFactorsAccepted( int pNbAccepted )
    {
        mNbFactorsAccepted = pNbAccepted;
    }

    /**
     * @param pNbRefused le nombre de facteurs refus�s
     */
    public void setNbFactorsRefused( int pNbRefused )
    {
        mNbFactorsRefused = pNbRefused;
    }

    /**
     * @param pReserved le nombre de facteurs r�serv�s
     */
    public void setNbFactorsReserved( int pReserved )
    {
        mNbFactorsReserved = pReserved;
    }

    /**
     * @return le total
     */
    public int getNbTotal()
    {
        return mNbTotal;
    }

    /**
     * @param pTotal le total
     */
    public void setNbTotal( int pTotal )
    {
        mNbTotal = pTotal;
    }

}
