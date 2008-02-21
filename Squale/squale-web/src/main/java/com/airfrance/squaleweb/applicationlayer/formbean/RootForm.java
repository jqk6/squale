package com.airfrance.squaleweb.applicationlayer.formbean;

import java.util.List;
import java.util.regex.Pattern;

import com.airfrance.squalecommon.datatransfertobject.component.AuditDTO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import com.airfrance.welcom.struts.bean.WActionFormSelectable;

/**
 */
public class RootForm extends WActionFormSelectable {

    /** id de l'application */
    protected String mApplicationId = "";

    /** nom de l'application */
    protected String mApplicationName = "";

    /** Le nombre de projets appartenant � l'application */
    protected String mNumberOfChildren = "";

    /** la date de l'audit courant */
    protected String mAuditDate = "";

    /** la date de l'audit courant */
    protected String mPreviousAuditDate = "";

    /** le label de l'audit courant dans le cas d'un audit de jalon ou date sinon*/
    protected String mAuditName = "";

    /** le label de l'audit pr�c�dent dans le cas d'un audit de jalon ou date sinon */
    protected String mPreviousAuditName = "";

    /** id du projet */
    protected String mProjectId = "";

    /** le nom du projet */
    protected String mProjectName = "";

    /** id de l'audit courant */
    protected String mCurrentAuditId = "";

    /** id de l'audit pr�c�dent*/
    protected String mPreviousAuditId = "";

    /** Indique si les audits sont comparables */
    protected boolean mComparableAudits;

    /** Version de squale pour l'audit courant */
    protected String mAuditSqualeVersion = "";

    /**
     * @return l'id de l'application
     */
    public String getApplicationId() {
        return mApplicationId;
    }

    /**
     * @return l'id de l'audit courant
     */
    public String getCurrentAuditId() {
        return mCurrentAuditId;
    }

    /**
     * @return l'id de l'audit pr�c�dent
     */
    public String getPreviousAuditId() {
        return mPreviousAuditId;
    }

    /**
     * @return l'id du projet
     */
    public String getProjectId() {
        return mProjectId;
    }

    /**
     * @param pAppliId l'id de l'application
     */
    public void setApplicationId(String pAppliId) {
        mApplicationId = pAppliId;
    }

    /**
     * @param pCurrentAuditId l'id de l'audit courant
     */
    public void setCurrentAuditId(String pCurrentAuditId) {
        mCurrentAuditId = pCurrentAuditId;
    }

    /**
     * @param pPreviousAuditId  l'id de l'audit pr�c�dent
     */
    public void setPreviousAuditId(String pPreviousAuditId) {
        mPreviousAuditId = pPreviousAuditId;
    }

    /**
     * @param pProjectId l'id du projet
     */
    public void setProjectId(String pProjectId) {
        mProjectId = pProjectId;
    }

    /**
     * @return le nom de l'application
     */
    public String getApplicationName() {
        return mApplicationName;
    }

    /**
     * @return le nom du projet
     */
    public String getProjectName() {
        return mProjectName;
    }

    /**
     * @param pApplicationName le nom de l'application 
     */
    public void setApplicationName(String pApplicationName) {
        mApplicationName = pApplicationName;
    }

    /**
     * @param pProjectName le nom du projet
     */
    public void setProjectName(String pProjectName) {
        mProjectName = pProjectName;
    }

    /**
     * @return la date sous la forme d'une chaine
     */
    public String getAuditDate() {
        return mAuditDate;
    }

    /**
     * @return le label de l'audit courant
     */
    public String getAuditName() {
        return mAuditName;
    }

    /**
     * @param pLabel le label de l'audit courant
     */
    public void setAuditName(String pLabel) {
        mAuditName = pLabel;
    }

    /**
     * @return le label de l'audit pr�c�dent
     */
    public String getPreviousAuditName() {
        return mPreviousAuditName;
    }

    /**
     * @param pLabel le label de l'audit pr�c�dent
     */
    public void setPreviousAuditName(String pLabel) {
        mPreviousAuditName = pLabel;
    }

    /**
     * @param pDate la date sous la forme d'une chaine
     */
    public void setAuditDate(String pDate) {
        mAuditDate = pDate;
    }

    /**
     * @return le nombre de projets de l'application
     */
    public String getNumberOfChildren() {
        return mNumberOfChildren;
    }

    /**
     * @param pNumber le nombre de projets de l'application
     */
    public void setNumberOfChildren(String pNumber) {
        mNumberOfChildren = pNumber;
    }

    /**
     * @return la date de l'audit pr�c�dent
     */
    public String getPreviousAuditDate() {
        return mPreviousAuditDate;
    }

    /**
     * @param pPreviousAuditDate la date de l'audit
     */
    public void setPreviousAuditDate(String pPreviousAuditDate) {
        mPreviousAuditDate = pPreviousAuditDate;
    }

    /**
     * M�thode utilitaire permettant de copier les donn�es d'un form dans un autre
     * @param pForm le form dont on veut r�cup�rer les valeurs
     */
    public void copyValues(RootForm pForm) {
        mApplicationId = pForm.getApplicationId();
        mApplicationName = pForm.getApplicationName();
        mProjectId = pForm.getProjectId();
        mProjectName = pForm.getProjectName();
        mCurrentAuditId = pForm.getCurrentAuditId();
        mPreviousAuditId = pForm.getPreviousAuditId();
        mAuditDate = pForm.getAuditDate();
        mPreviousAuditDate = pForm.getPreviousAuditDate();
        mAuditName = pForm.getAuditName();
        mPreviousAuditName = pForm.getPreviousAuditName();
        mNumberOfChildren = pForm.getNumberOfChildren();
        mAuditSqualeVersion = pForm.getAuditSqualeVersion();
        mComparableAudits = pForm.getComparableAudits();
    }

    /**
     * V�rifie que le nom correspond bien au pattern
     * @param pName le nom (application ou projet) � v�rifier
     * @return true si le nom est valide par rapport au pattern
     */
    protected boolean isAValidName(String pName) {
        String regexp = "[a-zA-Z0-9]+[a-zA-Z0-9_ \\.]*[a-zA-Z0-9]+$|[a-zA-Z0-9]+$";
        // Si �a matche, alors c'est un nom valide
        return Pattern.matches(regexp, pName);
    }

    /**
     * M�thode permettant de remettre � jour les propri�t�s d'un form avec les audits courants
     * @param pAudits la liste des audits (actuel et pr�c�dent)
     */
    public void resetAudits(List pAudits) {
        // Si il n'y a pas d'audits,ou pas d'audit courant, on reinitialise les 2 audits
        if (pAudits == null || pAudits.size() < 1 || pAudits.get(0) == null) {
            resetAudits();
        } else {
            // ici, l'audit courant ne peut pas etre null
            AuditDTO currentAudit = ((AuditDTO) pAudits.get(0));
            setCurrentAuditId("" + currentAudit.getID());
            setAuditDate("" + currentAudit.getFormattedDate());
            setAuditName(getAuditDate());
            // Formatage du nom dans le cas o� il s'agit d'un audit de jalon
            if (currentAudit.getType().equals(AuditBO.MILESTONE)) {
                setAuditName(currentAudit.getName() + " (" + getAuditDate() + ")");
            }
            // Gere l'audit pr�c�dent
            if (pAudits.size() < 2 || pAudits.get(1) == null) {
                // L'audit pr�s�dant n'existe pas
                setPreviousAuditId("");
                setPreviousAuditDate("");
                setPreviousAuditName("");
            } else {
                AuditDTO previousAudit = ((AuditDTO) pAudits.get(1));
                setPreviousAuditId("" + previousAudit.getID());
                setPreviousAuditDate("" + previousAudit.getFormattedDate());
                setPreviousAuditName(getPreviousAuditDate());
                // Formatage du nom dans le cas o� il s'agit d'un audit de jalon
                if (previousAudit.getType().equals(AuditBO.MILESTONE)) {
                    setPreviousAuditName(previousAudit.getName() + " (" + getPreviousAuditDate() + ")");
                }
            }
        }

    }

    /**
     * Efface les donn�es pour les audits
     */
    private void resetAudits() {
        // L'audit courant
        setCurrentAuditId("");
        setAuditDate("");
        setAuditName("");
        // L'audit pr�c�dent
        setPreviousAuditId("");
        setPreviousAuditDate("");
        setPreviousAuditName("");
        setAuditSqualeVersion("");
    }

    /**
     * Efface les donn�es de l'audit pr�c�dent
     */
    public void resetCache() {
        // L'application
        setApplicationId("");
        setApplicationName("");
        setNumberOfChildren("");
        setAuditSqualeVersion("");
        // L'id du projet
        setProjectId("");
        setProjectName("");
        // Les audits
        resetAudits();
    }

    /**
     * @return la version de squale
     */
    public String getAuditSqualeVersion() {
        return mAuditSqualeVersion;
    }

    /**
     * @param pVersion la version de SQUALE
     */
    public void setAuditSqualeVersion(String pVersion) {
        mAuditSqualeVersion = pVersion;
    }

    /**
     * @return true si les audits sont comparables
     */
    public boolean getComparableAudits() {
        return mComparableAudits;
    }

    /**
     * @param pComparable si les audits sont comparables
     */
    public void setComparableAudits(boolean pComparable) {
        mComparableAudits = pComparable;
    }

}
