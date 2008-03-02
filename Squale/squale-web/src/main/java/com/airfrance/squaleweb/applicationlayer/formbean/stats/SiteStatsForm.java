package com.airfrance.squaleweb.applicationlayer.formbean.stats;

import com.airfrance.squaleweb.applicationlayer.formbean.config.ServeurForm;

/**
 */
public class SiteStatsForm
    extends CommonStatsForm
{

    /**
     * Pr�cise le site auquel appartient l'application
     */
    private ServeurForm mServeurForm = new ServeurForm();

    /**
     * Le nombre d'applications avec des audits r�ussis
     */
    private int nbAppliWithAuditsSuccessful;

    /**
     * Le nombre d'applications avec des audits mais aucun r�ussi
     */
    private int nbAppliWithoutSuccessfulAudits;

    /**
     * Le nombre d'applications valid�es (<=> sans audits)
     */
    private int nbValidatedApplis;

    /**
     * Le nombre d'applications � valider
     */
    private int nbAppliToValidate;

    /**
     * @return le nombre d'appli � valider
     */
    public int getNbAppliToValidate()
    {
        return nbAppliToValidate;
    }

    /**
     * @return le nombre d'appli avec des audits r�ussis
     */
    public int getNbAppliWithAuditsSuccessful()
    {
        return nbAppliWithAuditsSuccessful;
    }

    /**
     * @return le nombre d'applis avec des audits mais aucun r�ussi
     */
    public int getNbAppliWithoutSuccessfulAudits()
    {
        return nbAppliWithoutSuccessfulAudits;
    }

    /**
     * @return le nombre d'applis valid�s
     */
    public int getNbValidatedApplis()
    {
        return nbValidatedApplis;
    }

    /**
     * @param pNbAppli le nombre d'applis � valider
     */
    public void setNbAppliToValidate( int pNbAppli )
    {
        nbAppliToValidate = pNbAppli;
    }

    /**
     * @param pNbAppli le nombre d'appli avec des audits r�ussis
     */
    public void setNbAppliWithAuditsSuccessful( int pNbAppli )
    {
        nbAppliWithAuditsSuccessful = pNbAppli;
    }

    /**
     * @param pNbAppli le nombre d'appli sans audits r�ussis
     */
    public void setNbAppliWithoutSuccessfulAudits( int pNbAppli )
    {
        nbAppliWithoutSuccessfulAudits = pNbAppli;
    }

    /**
     * @param pNbAppli le nombre d'applis valid�s
     */
    public void setNbValidatedApplis( int pNbAppli )
    {
        nbValidatedApplis = pNbAppli;
    }

    /**
     * @return le formulaire du Serveur
     */
    public ServeurForm getServeurForm()
    {
        return mServeurForm;
    }

    /**
     * @param pServeurForm le formulaire du Serveur
     */
    public void setServeurForm( ServeurForm pServeurForm )
    {
        mServeurForm = pServeurForm;
    }

}
