package com.airfrance.squaleweb.applicationlayer.formbean.results;

import java.util.ArrayList;
import java.util.Collection;

import com.airfrance.squaleweb.applicationlayer.formbean.RootForm;

/**
 * Bean pour la liste des items d'une transgression
 */
public class RuleCheckingItemsListForm extends RootForm {
    
    /**
     * Nom de la r�gle transgress�e
     */
    private String mRuleName;

    /**
     * D�tails concernant la r�gle
     */
    private Collection mDetails;
    
    /**
     * Indique si la liste contient au moins
     * un item qui poss�de un lien vers un composant
     */
    private boolean mComponentLink;
    
    /**
     * Constructeur par d�faut
     */
    public RuleCheckingItemsListForm() {
        mDetails = new ArrayList();
    }

    /**
     * @return le nom de la r�gle
     */
    public String getRuleName() {
        return mRuleName;
    }

    /**
     * @return les d�tails
     */
    public Collection getDetails() {
        return mDetails;
    }

    /**
     * @param pRuleName le nom de la r�gle
     */
    public void setRuleName(String pRuleName) {
        mRuleName = pRuleName;
    }

    /**
     * @param pDetails les d�tails
     */
    public void setDetails(Collection pDetails) {
        mDetails = pDetails;
    }
    /**
     * @return true si il y a un lien vers un composant
     */
    public boolean getComponentLink() {
        return mComponentLink;
    }

    /**
     * @param pComponentLink lien vers composant
     */
    public void setComponentLink(boolean pComponentLink) {
        mComponentLink = pComponentLink;
    }

}
