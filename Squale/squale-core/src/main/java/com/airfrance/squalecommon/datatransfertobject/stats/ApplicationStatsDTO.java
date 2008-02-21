package com.airfrance.squalecommon.datatransfertobject.stats;

import java.util.Date;

/**
 * Permet de r�cup�rer les statistiques par application
 */
public class ApplicationStatsDTO {
    /** Le nom de l'application concern�e */
    private String mApplicationName;
    /** Indique si l'application est valid�e */
    private boolean mValidatedApplication;
    /** true si le derniers audits ex�cut�s est r�ussis */
    private boolean mLastAuditIsTerminated;
    /** false si aucun audit r�ussis pr�sent dans les n derniers jours (90 par d�faut) */
    private boolean mActivatedApplication;
    /** date du dernier audit r�ussis */
    private Date mLastTerminatedAuditDate;
    /** la dur�e en hh:mm du dernier audit */
    private String mLastAuditDuration;
    /** le nombre d'audit ( quelque soit leur �tat ) r�alis�s dutant les n derniers jours (10 par d�faut) */
    private int mNbAudits;
    /** le nombre audits en r�ussis */
    private int mNbTerminatedAudits;
    /** le nombre audits partiel ou en �chec */
    private int mNbPartialOrFaliedAudits;
    /** la date du dernier audit en �chec */
    private Date mLastFailedAuditDate;
    /** la date du plus ancien audit r�ussis */
    private Date mFirstTerminatedAuditDate;
    /** le nom du serveur de l'application */
    private String mServerName;
    /** la fr�quence de purge */
    private String mPurgeFrequency = "-";
    /** Date du dernier acc�s utilisateur */
    private Date mLastAccess;

    /**
     * @return false si aucun audit r�ussis pr�sent dans les n derniers jours
     */
    public boolean isActivatedApplication() {
        return mActivatedApplication;
    }

    /**
     * @return le nom de l'application concern�e
     */
    public String getApplicationName() {
        return mApplicationName;
    }

    /**
     * @return la date du plus ancien audit r�ussis
     */
    public Date getFirstTerminatedAuditDate() {
        return mFirstTerminatedAuditDate;
    }

    /**
     * @return la date du dernier acc�s utilisateur
     */
    public Date getLastAccess() {
        return mLastAccess;
    }

    /**
     * @return la dur�e en hh:mm du dernier audit
     */
    public String getLastAuditDuration() {
        return mLastAuditDuration;
    }

    /**
     * @return la date du dernier audit en �chec
     */
    public Date getLastFailedAuditDate() {
        return mLastFailedAuditDate;
    }

    /**
     * @return la date du dernier audit r�ussis
     */
    public Date getLastTerminatedAuditDate() {
        return mLastTerminatedAuditDate;
    }

    /**
     * @return le nombre d'audit ( quelque soit leur �tat ) r�alis�s dutant les n derniers jours
     */
    public int getNbAudits() {
        return mNbAudits;
    }

    /**
     * @return le nombre audits partiel ou en �chec
     */
    public int getNbPartialOrFaliedAudits() {
        return mNbPartialOrFaliedAudits;
    }

    /**
     * @return le nombre audits en r�ussis
     */
    public int getNbTerminatedAudits() {
        return mNbTerminatedAudits;
    }

    /**
     * @return le nom du serveur de l'application
     */
    public String getServerName() {
        return mServerName;
    }

    /**
     * @return true si le derniers audits ex�cut�s est r�ussis
     */
    public boolean isLastAuditIsTerminated() {
        return mLastAuditIsTerminated;
    }

    /**
     * @return true si l'application est valid�e
     */
    public boolean isValidatedApplication() {
        return mValidatedApplication;
    }

    /**
     * @param pActivated false si aucun audit r�ussis pr�sent dans les n derniers jours
     */
    public void setActivatedApplication(boolean pActivated) {
        mActivatedApplication = pActivated;
    }

    /**
     * @param pName le nom de l'application
     */
    public void setApplicationName(String pName) {
        mApplicationName = pName;
    }

    /**
     * @param pDate la date du plus ancien audit r�ussis
     */
    public void setFirstTerminatedAuditDate(Date pDate) {
        mFirstTerminatedAuditDate = pDate;
    }

    /**
     * @param pDate la date du dernier acc�s utilisateur
     */
    public void setLastAccess(Date pDate) {
        mLastAccess = pDate;
    }

    /**
     * @param pDuration la dur�e en hh:mm du dernier audit
     */
    public void setLastAuditDuration(String pDuration) {
        mLastAuditDuration = pDuration;
    }

    /**
     * @param pDate la date du dernier audit en �chec
     */
    public void setLastFailedAuditDate(Date pDate) {
        mLastFailedAuditDate = pDate;
    }

    /**
     * @param pDate la date du dernier audit r�ussis
     */
    public void setLastTerminatedAuditDate(Date pDate) {
        mLastTerminatedAuditDate = pDate;
    }

    /**
     * @param pNbAudits le nombre d'audit ( quelque soit leur �tat ) r�alis�s dutant les n derniers jours
     */
    public void setNbAudits(int pNbAudits) {
        mNbAudits = pNbAudits;
    }

    /**
     * @param pNbAudits le nombre audits partiel ou en �chec
     */
    public void setNbPartialOrFaliedAudits(int pNbAudits) {
        mNbPartialOrFaliedAudits = pNbAudits;
    }

    /**
     * @param pNbAudits le nombre audits en r�ussis
     */
    public void setNbTerminatedAudits(int pNbAudits) {
        mNbTerminatedAudits = pNbAudits;
    }

    /**
     * @param pServerName le nom du serveur de l'application
     */
    public void setServerName(String pServerName) {
        mServerName = pServerName;
    }

    /**
     * @param pTerminated true si le derniers audit ex�cut� est r�ussi
     */
    public void setLastAuditIsTerminated(boolean pTerminated) {
        mLastAuditIsTerminated = pTerminated;
    }

    /**
     * @param pValidated true si l'application est valid�e
     */
    public void setValidatedApplication(boolean pValidated) {
        mValidatedApplication = pValidated;
    }
    /**
     * @return la fr�quence de purge
     */
    public String getPurgeFrequency() {
        return mPurgeFrequency;
    }

    /**
     * @param pFreq la fr�quence de purge
     */
    public void setPurgeFrequency(int pFreq) {
        if(pFreq >= 0) {
            mPurgeFrequency = "" + pFreq;
        }
    }

}
