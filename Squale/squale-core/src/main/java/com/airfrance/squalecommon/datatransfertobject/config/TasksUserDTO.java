package com.airfrance.squalecommon.datatransfertobject.config;

import java.util.List;

/**
 * Utilisateur de t�ches Squalix
 */
public class TasksUserDTO
{

    /**
     * Identifiant (au sens technique) de l'objet
     */
    protected long mId;

    /** Le nom associ� */
    protected String mName;

    /** La liste des t�ches d'analyses */
    protected List mAnalysisTasks;

    /** La liste des t�ches finales */
    protected List mTerminationTasks;

    /**
     * M�thode d'acc�s pour mId
     * 
     * @return la l'identifiant (au sens technique) de l'objet
     */
    public long getId()
    {
        return mId;
    }

    /**
     * Change la valeur de mId
     * 
     * @param pId le nouvel identifiant
     */
    public void setId( long pId )
    {
        mId = pId;
    }

    /**
     * M�thode d'acc�s � mAnalysisTasks
     * 
     * @return la liste des t�ches d'analyses
     */
    public List getAnalysisTasks()
    {
        return mAnalysisTasks;
    }

    /**
     * M�thode d'acc�s � mName
     * 
     * @return le nom du profile
     */
    public String getName()
    {
        return mName;
    }

    /**
     * M�thode d'acc�s � mTerminationTasks
     * 
     * @return la liste des t�ches finales
     */
    public List getTerminationTasks()
    {
        return mTerminationTasks;
    }

    /**
     * Change la valeur de mAnalysisTasks
     * 
     * @param pAnalysisTasks la nouvelle liste des t�ches d'analyses
     */
    public void setAnalysisTasks( List pAnalysisTasks )
    {
        mAnalysisTasks = pAnalysisTasks;
    }

    /**
     * Change la valeur de mName
     * 
     * @param pName le nouveau nom du profile
     */
    public void setName( String pName )
    {
        mName = pName;
    }

    /**
     * Change la valeur de mTerminationTasks
     * 
     * @param pTerminationTasks la nouvelle liste de t�ches finales
     */
    public void setTerminationTasks( List pTerminationTasks )
    {
        mTerminationTasks = pTerminationTasks;
    }

}
