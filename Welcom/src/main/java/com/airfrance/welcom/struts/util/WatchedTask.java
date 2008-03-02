/*
 * Cr�� le 20 sept. 07
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.struts.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

/**
 * @author 6361371 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public abstract class WatchedTask
    implements Runnable
{
    public abstract void init( ActionForm form, HttpServletRequest request );

    private String status = "init";

    private Throwable errors;

    private TaskProgress progress;

    /**
     * @return
     */
    public TaskProgress getProgress()
    {
        return progress;
    }

    /**
     * @param progress
     */
    void setProgress( TaskProgress progress )
    {
        this.progress = progress;
    }

    /**
     * @return
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * @param string
     */
    public void setStatus( String string )
    {
        status = string;
    }

    /**
     * @return
     */
    public Throwable getErrors()
    {
        return errors;
    }

    /**
     * @param throwable
     */
    public void setErrors( Throwable throwable )
    {
        errors = throwable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return this.getClass().getName() + " [" + getProgress() + "] " + getStatus() + " " + getErrors();
    }

}
