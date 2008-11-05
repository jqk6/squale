package com.airfrance.squalecommon.datatransfertobject.config;

import java.io.Serializable;
import java.util.Collection;

import com.airfrance.squalecommon.enterpriselayer.businessobject.config.AdminParamsBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.config.AuditFrequencyBO;

/**
 * Configuration Squalix
 */
public class SqualixConfigurationDTO
    implements Serializable
{

    /** La liste des dates limites de lancement d'audits */
    private Collection mStopTimes;

    /** La liste des fr�quences max des audits en fonction du dernier acc�s utilisateur */
    private Collection mFrequencies;

    /** La liste des r�cup�rateurs de sources */
    private Collection mSourceManagements;

    /** La liste des profils */
    private Collection mProfiles;

    /**
     * The list of a configuration parameter (adminParamsBO)
     */
    private Collection<AdminParamsDTO> adminParams;

    /**
     * M�thode d'acc�s pour mStopTimes
     * 
     * @return les dates limites
     */
    public Collection getStopTimes()
    {
        return mStopTimes;
    }

    /**
     * M�thode d'acc�s pour mProfiles
     * 
     * @return les profils
     */
    public Collection getProfiles()
    {
        return mProfiles;
    }

    /**
     * M�thode d'acc�s pour mSourceManagements
     * 
     * @return les r�cup�rateurs de sources
     */
    public Collection getSourceManagements()
    {
        return mSourceManagements;
    }

    /**
     * Change la valeur de mStopTimes
     * 
     * @param pStopTimes les nouvelles dates limites
     */
    public void setStopTimes( Collection pStopTimes )
    {
        mStopTimes = pStopTimes;
    }

    /**
     * Change la valeur de mProfiles
     * 
     * @param pProfiles les nouveaux profils
     */
    public void setProfiles( Collection pProfiles )
    {
        mProfiles = pProfiles;
    }

    /**
     * Change la valeur de mSourceManagements
     * 
     * @param pSourceManagements les nouveaux types de r�cup�ration des sources
     */
    public void setSourceManagements( Collection pSourceManagements )
    {
        mSourceManagements = pSourceManagements;
    }

    /**
     * Setter method for the Frequencies Collection
     * 
     * @param pCollection The new Collection of max frequencies for the audit
     */
    public void setFrequencies( Collection pCollection )
    {
        mFrequencies = pCollection;
    }

    /**
     * Getter method for the frequencies Collection
     * 
     * @return the collection of max frequencies for the audit
     */
    public Collection getFrequencies()
    {
        return mFrequencies;
    }

    /**
     * Getter method for the Collection of adminParamsDTO
     * 
     * @return the adminParamsDTO Collection
     */
    public Collection<AdminParamsDTO> getAdminParams()
    {
        return adminParams;
    }

    /**
     * Setter method for the adminParams Collection
     * 
     * @param pAdminParams the new Collection of adminParamsDTO
     */
    public void setAdminParams( Collection<AdminParamsDTO> pAdminParams )
    {
        adminParams = pAdminParams;
    }
}
