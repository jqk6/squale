package com.airfrance.squalecommon.datatransfertobject.config;

/**
 * R�cup�rateur de sources
 */
public class SourceManagementDTO
    extends TasksUserDTO
{

    /** Audit de jalon possible */
    private boolean mMilestoneAudit;

    /** Audit de suivi possible */
    private boolean mNormalAudit;

    /**
     * M�thode d'acc�s � mMilestoneAudit
     * 
     * @return true si l'audit de jalon est possible
     */
    public boolean isMilestoneAudit()
    {
        return mMilestoneAudit;
    }

    /**
     * M�thode d'acc�s � mNormalAudit
     * 
     * @return true si l'audit de suivi est possible
     */
    public boolean isNormalAudit()
    {
        return mNormalAudit;
    }

    /**
     * Change la valeur de mMilestoneAudit
     * 
     * @param pMilestoneAudit la nouvelle valeur de mMilestoneAudit
     */
    public void setMilestoneAudit( boolean pMilestoneAudit )
    {
        mMilestoneAudit = pMilestoneAudit;
    }

    /**
     * Change la valeur de mNormalAudit
     * 
     * @param pNormalAudit la nouvelle valeur de mNormalAudit
     */
    public void setNormalAudit( boolean pNormalAudit )
    {
        mNormalAudit = pNormalAudit;
    }

}
