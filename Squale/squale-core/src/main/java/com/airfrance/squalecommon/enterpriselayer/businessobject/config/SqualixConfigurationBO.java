package com.airfrance.squalecommon.enterpriselayer.businessobject.config;

import java.util.ArrayList;
import java.util.Collection;

/**
 * La configuration de Squalix
 */
public class SqualixConfigurationBO {

    /** La liste des dates limites de lancement d'audits */
    private Collection mStopTimes;
    
    /** La liste des fr�quences max des audits en fonction du dernier acc�s utilisateur */
    private Collection mFrequencies;

    /** La liste des r�cup�rateurs de sources */
    private Collection mSourceManagements;

    /** La liste des profils */
    private Collection mProfiles;

    /**
     * Le constructeur par d�faut
     */
    public SqualixConfigurationBO() {
        mStopTimes = new ArrayList();
        mFrequencies = new ArrayList();
        mSourceManagements = new ArrayList();
        mProfiles = new ArrayList();
    }

    /**
     * M�thode d'acc�s pour mStopTimes
     * 
     * @return les dates limites
     */
    public Collection getStopTimes() {
        return mStopTimes;
    }

    /**
     * M�thode d'acc�s pour mProfiles
     * 
     * @return les profils
     */
    public Collection getProfiles() {
        return mProfiles;
    }

    /**
     * M�thode d'acc�s pour mSourceManagements
     * 
     * @return les r�cup�rateurs de sources
     */
    public Collection getSourceManagements() {
        return mSourceManagements;
    }

    /**
     * Change la valeur de mStopTimes
     * 
     * @param pStopTimes les nouvelles dates limites
     */
    public void setStopTimes(Collection pStopTimes) {
        mStopTimes = pStopTimes;
    }

    /**
     * Change la valeur de mProfiles
     * 
     * @param pProfiles les nouveaux profils
     */
    public void setProfiles(Collection pProfiles) {
        mProfiles = pProfiles;
    }

    /**
     * Change la valeur de mSourceManagements
     * 
     * @param pSourceManagements les nouveaux types de r�cup�ration des sources
     */
    public void setSourceManagements(Collection pSourceManagements) {
        mSourceManagements = pSourceManagements;
    }
    
    /**
     * Ajoute une date limite
     * 
     * @param pStopTime la date limite � ajouter
     */
    public void addStopTime(StopTimeBO pStopTime) {
        mStopTimes.add(pStopTime);
    }

    /**
     * Ajoute un profil
     * 
     * @param pProfile le profil � ajouter
     */
    public void addProfile(ProjectProfileBO pProfile) {
        mProfiles.add(pProfile);
    }

    /**
     * Ajoute un type de r�cup�ration des sources
     * 
     * @param pSourceManagement une nouvelle mani�re de r�cup�rer les source
     */
    public void addSourceManagement(SourceManagementBO pSourceManagement) {
        mSourceManagements.add(pSourceManagement);
    }
    
    /**
     * @return la liste des fr�quences max des audits
     */
    public Collection getFrequencies() {
        return mFrequencies;
    }

    /**
     * @param pCollection la liste des fr�quences max des audits
     */
    public void setFrequencies(Collection pCollection) {
        mFrequencies = pCollection;
    }
    
    /**
     * Ajoute une fr�quence max d'audit
     * 
     * @param pFrequency la fr�quence max
     */
    public void addFrequency(AuditFrequencyBO pFrequency) {
        mFrequencies.add(pFrequency);
    }

}
