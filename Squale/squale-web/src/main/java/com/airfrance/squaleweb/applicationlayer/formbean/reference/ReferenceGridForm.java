package com.airfrance.squaleweb.applicationlayer.formbean.reference;

import java.util.Date;

import com.airfrance.squaleweb.applicationlayer.formbean.RootForm;

/**
 * Donn�es du r�f�rentiel par grille
 */
public class ReferenceGridForm extends RootForm {

    /** Date de mise � jour */
    private Date mUpdateDate;

    /** Date sous la forme de String */
    private String mFormattedDate = "";

    /** nom de la grille */
    private String mName = "";

    /**
     * @return date de mise � jour format�e
     */
    public String getFormattedDate() {
        return mFormattedDate;
    }

    /**
     * @param pString date de mise � jour format�e
     */
    public void setFormattedDate(String pString) {
        mFormattedDate = pString;
    }

    /**
     * Liste des r�f�rences (referencesForm)
     */
    private ReferenceListForm mReferenceListForm = new ReferenceListForm();
    /**
     * @return la liste des r�f�rences
     */
    public ReferenceListForm getReferenceListForm() {
        return mReferenceListForm;
    }

    /**
     * @param pReferenceListForm la liste des r�f�rences
     */
    public void setReferenceListForm(ReferenceListForm pReferenceListForm) {
        mReferenceListForm = pReferenceListForm;
    }

    /**
     * @return date de mise � jour
     */
    public Date getUpdateDate() {
        return mUpdateDate;
    }

    /**
     * @param pString date de mise � jour
     */
    public void setUpdateDate(Date pString) {
        mUpdateDate = pString;
    }

    /**
     * @return le nom de la grille
     */
    public String getName() {
        return mName;
    }

    /**
     * @param pName le nom de la grille
     */
    public void setName(String pName) {
        mName = pName;
    }

}
