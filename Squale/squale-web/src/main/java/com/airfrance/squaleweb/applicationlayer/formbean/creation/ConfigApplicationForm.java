package com.airfrance.squaleweb.applicationlayer.formbean.creation;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.airfrance.squaleweb.applicationlayer.formbean.RootForm;

/**
 * Form bean for a Struts application.
 * 
 * @version 1.0
 * @author
 */
public class ConfigApplicationForm
    extends RootForm
{
    /**
     * D�lai de purge par d�faut
     */
    private static final int DEFAULT_PURGE_DELAY = 180;

    /**
     * D�lai entre deux purges de l'application
     */
    private int mPurgeDelay = DEFAULT_PURGE_DELAY;

    /**
     * Liste des matricules
     */
    private String[] mMatricules = null;

    /**
     * Liste des profils associ�s
     */
    private int[] mProfiles = null;

    /**
     * R�initialise le formulaire.
     * 
     * @param pMapping le mapping.
     * @param pRequest la requ�te.
     */
    public void reset( ActionMapping pMapping, HttpServletRequest pRequest )
    {
        mPurgeDelay = DEFAULT_PURGE_DELAY;
        mMatricules = null;
        mProfiles = null;
    }

    /**
     * @return la liste des matricules
     */
    public String[] getMatricules()
    {
        return mMatricules;
    }

    /**
     * @return la liste des profils
     */
    public int[] getProfiles()
    {
        return mProfiles;
    }

    /**
     * @return le d�lai de purge
     */
    public int getPurgeDelay()
    {
        return mPurgeDelay;
    }

    /**
     * @param pMatricules la liste des matricules
     */
    public void setMatricules( String[] pMatricules )
    {
        mMatricules = pMatricules;
    }

    /**
     * @param pProfiles la liste des profils
     */
    public void setProfiles( int[] pProfiles )
    {
        mProfiles = pProfiles;
    }

    /**
     * @param pPurgeDelay le d�lai de purge (en jour)
     */
    public void setPurgeDelay( int pPurgeDelay )
    {
        mPurgeDelay = pPurgeDelay;
    }
}
