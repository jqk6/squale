package com.airfrance.squalecommon.datatransfertobject.stats;

import com.airfrance.squalecommon.datatransfertobject.config.ServeurDTO;

/**
 */
public class SiteStatsDTO
    extends CommonStatsDTO
{

    /**
     * Le nom du serveur
     */
    private ServeurDTO mServeurDTO;

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
     * Constructeur par d�faut
     */
    public SiteStatsDTO()
    {
        mServeurDTO = new ServeurDTO();
    }

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
     * @return le nom du serveur
     */
    public ServeurDTO getServeurDTO()
    {
        return mServeurDTO;
    }

    /**
     * @param pServeurDTO le nom du serveur
     */
    public void setServeurDTO( ServeurDTO pServeurDTO )
    {
        mServeurDTO = pServeurDTO;
    }

}
