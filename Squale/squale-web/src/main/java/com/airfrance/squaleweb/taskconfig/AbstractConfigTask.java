package com.airfrance.squaleweb.taskconfig;

import java.util.Collection;

/**
 * 
 */
public abstract class AbstractConfigTask
{

    /**
     * Nom de la tache
     */
    private String mTaskName;

    /**
     * Titre de la rubrique contenant les infos de configuration de la tache
     */
    private String mHelpKeyTask;

    /**
     * Collection des infos de configuration n�cessaire pour effectuer la tache Cette collection contient des objets
     * InfoConfig
     */
    private Collection mInfoConfigTask;

    /**
     * @return La collection des infos de configuration n�cessaire
     */
    public Collection getInfoConfigTask()
    {
        return mInfoConfigTask;
    }

    /**
     * @return Le tire de la rubrique
     */
    public String getHelpKeyTask()
    {
        return mHelpKeyTask;
    }

    /**
     * @param pInfoConfigTask Infos de configuration � ins�rer
     */
    public void setInfoConfigTask( Collection pInfoConfigTask )
    {
        mInfoConfigTask = pInfoConfigTask;
    }

    /**
     * @param pHelpKeyTask Titre � ins�rer
     */
    public void setHelpKeyTask( String pHelpKeyTask )
    {
        mHelpKeyTask = pHelpKeyTask;
    }

    /**
     * @return retourne le nom de la tache
     */
    public String getTaskName()
    {
        return mTaskName;
    }

    /**
     * @param pTaskName ins�re le nom de la tache
     */
    public void setTaskName( String pTaskName )
    {
        mTaskName = pTaskName;
    }

    /**
     * Tache abstraite qui servira � initialiser la liste des champs n�cessaire dans la JSP pour pouvoir configurer la
     * tache
     */
    public abstract void init();

}
