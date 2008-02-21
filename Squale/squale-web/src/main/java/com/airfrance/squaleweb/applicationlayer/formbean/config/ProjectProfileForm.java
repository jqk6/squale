package com.airfrance.squaleweb.applicationlayer.formbean.config;

import com.airfrance.squalecommon.datatransfertobject.config.ProjectProfileDTO;
import com.airfrance.squaleweb.applicationlayer.formbean.RootForm;

/**
 * Contient les donn�es d'un profile Squalix
 */
public class ProjectProfileForm extends RootForm {

    /** Nom du profile */
    private String mName;

    /** Id du profile en String pour le r�cup�rer dans l'option */
    private String mId;

    /**
     * Constructeur pas d�faut
     */
    public ProjectProfileForm() {
        mId = "-1";
    }

    /**
     * Constructeur
     * @param pProfile le DTO
     */
    public ProjectProfileForm(ProjectProfileDTO pProfile) {
        mName = pProfile.getName();
        mId = Long.toString(pProfile.getId());
    }

    /**
     * M�thode d'acc�s � mId
     * @return l'identifiant du profile
     */
    public String getId() {
        return mId;
    }

    /**
     * M�thode d'acc�s � mName
     * @return le nom du profile
     */
    public String getName() {
        return mName;
    }

    /**
     * Change la valeur de mId
     * @param pId le nouvel identifiant
     */
    public void setId(long pId) {
        mId = Long.toString(pId);
    }

    /**
     * Change la valeur de mId
     * @param pId le nouvel identifiant
     */
    public void setId(String pId) {
        mId = pId;
    }

    /**
     * Change la valeur de mName
     * @param pName le nouveau nom du profile
     */
    public void setName(String pName) {
        mName = pName;
    }
}
