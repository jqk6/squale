package com.airfrance.squaleweb.applicationlayer.formbean.stats;

import com.airfrance.squaleweb.applicationlayer.formbean.RootForm;

/**
 * Bean pour les statistiques par application
 */
public class ApplicationStatsForm extends RootForm {
    /** Indique si l'application est valid�e */
    private boolean mValidatedApplication;
    /** Indique si l'application est valid�e sous forme de cha�ne */
    private String mValidatedApplicationStr;
    /** Ok si le derniers audits ex�cut�s est r�ussis, En �chec sinon */
    private String mLastAuditIsTerminated;
    /** o si aucun audit r�ussis pr�sent dans les n derniers jours (90 par d�faut), X sinon */
    private String mActivatedApplication;
    /** date du dernier audit r�ussis */
    private String mLastTerminatedAuditDate;
    /** la dur�e en hh:mm du dernier audit */
    private String mLastAuditDuration;
    /** le nombre d'audit ( quelque soit leur �tat ) r�alis�s dutant les n derniers jours (10 par d�faut) */
    private int mNbAudits;
    /** le nombre audits en r�ussis */
    private int mNbTerminatedAudits;
    /** le nombre audits partiel ou en �chec */
    private int mNbPartialOrFaliedAudits;
    /** la date du dernier audit en �chec */
    private String mLastFailedAuditDate;
    /** la date du plus ancien audit r�ussis */
    private String mFirstTerminatedAuditDate;
    /** le nom du serveur de l'application */
    private String mServerName;
    /** la fr�quence de purge */
    private String mPurgeFrequency;
    /** Date du dernier acc�s utilisateur */
    private String mLastAccess;

    /**
     * @return X si aucun audit r�ussis pr�sent dans les n derniers jours
     */
    public String getActivatedApplication() {
        return mActivatedApplication;
    }

    /**
     * @return la date du plus ancien audit r�ussis
     */
    public String getFirstTerminatedAuditDate() {
        return mFirstTerminatedAuditDate;
    }

    /**
     * @return la date du dernier acc�s utilisateur
     */
    public String getLastAccess() {
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
    public String getLastFailedAuditDate() {
        return mLastFailedAuditDate;
    }

    /**
     * @return la date du dernier audit r�ussis
     */
    public String getLastTerminatedAuditDate() {
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
     * @return Ok si le derniers audits ex�cut�s est r�ussis
     */
    public String getLastAuditIsTerminated() {
        return mLastAuditIsTerminated;
    }

    /**
     * @return true si l'application est valid�e
     */
    public boolean getValidatedApplication() {
        return mValidatedApplication;
    }

    /**
     * @return la cha�ne correspondant � l'affichage d'une application valid�e ou non
     */
    public String getValidatedApplicationStr() {
        return mValidatedApplicationStr;
    }

    /**
     * @param pActivated X si aucun audit r�ussis pr�sent dans les n derniers jours
     */
    public void setActivatedApplication(String pActivated) {
        mActivatedApplication = pActivated;
    }

    /**
     * @param pDate la date du plus ancien audit r�ussis
     */
    public void setFirstTerminatedAuditDate(String pDate) {
        mFirstTerminatedAuditDate = pDate;
    }

    /**
     * @param pDate la date du dernier acc�s utilisateur
     */
    public void setLastAccess(String pDate) {
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
    public void setLastFailedAuditDate(String pDate) {
        mLastFailedAuditDate = pDate;
    }

    /**
     * @param pDate la date du dernier audit r�ussis
     */
    public void setLastTerminatedAuditDate(String pDate) {
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
     * @param pTerminated Ok si le derniers audit ex�cut� est r�ussi
     */
    public void setLastAuditIsTerminated(String pTerminated) {
        mLastAuditIsTerminated = pTerminated;
    }

    /**
     * @param pValidated true si l'application est valid�e
     */
    public void setValidatedApplication(boolean pValidated) {
        mValidatedApplication = pValidated;
    }

    /**
     * modifie la cha�ne correspondant � l'affichage d'une application valid�e ou non
     * @param pValidatedApplicationStr la nouvelle cha�ne
     */
    public void setValidatedApplicationStr(String pValidatedApplicationStr) {
        mValidatedApplicationStr = pValidatedApplicationStr;
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
    public void setPurgeFrequency(String pFreq) {
        mPurgeFrequency = "" + pFreq;
    }
}
