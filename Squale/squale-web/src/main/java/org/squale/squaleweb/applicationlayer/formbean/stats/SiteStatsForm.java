/**
 * Copyright (C) 2008-2010, Squale Project - http://www.squale.org
 *
 * This file is part of Squale.
 *
 * Squale is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * Squale is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Squale.  If not, see <http://www.gnu.org/licenses/>.
 */
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
