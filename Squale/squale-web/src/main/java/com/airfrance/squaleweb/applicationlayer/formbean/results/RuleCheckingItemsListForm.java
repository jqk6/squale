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
package com.airfrance.squaleweb.applicationlayer.formbean.results;

import java.util.ArrayList;
import java.util.Collection;

import com.airfrance.squaleweb.applicationlayer.formbean.RootForm;

/**
 * Bean pour la liste des items d'une transgression
 */
public class RuleCheckingItemsListForm
    extends RootForm
{

    /**
     * Nom de la r�gle transgress�e
     */
    private String mRuleName;

    /**
     * D�tails concernant la r�gle
     */
    private Collection mDetails;

    /**
     * Indique si la liste contient au moins un item qui poss�de un lien vers un composant
     */
    private boolean mComponentLink;

    /**
     * Constructeur par d�faut
     */
    public RuleCheckingItemsListForm()
    {
        mDetails = new ArrayList();
    }

    /**
     * @return le nom de la r�gle
     */
    public String getRuleName()
    {
        return mRuleName;
    }

    /**
     * @return les d�tails
     */
    public Collection getDetails()
    {
        return mDetails;
    }

    /**
     * @param pRuleName le nom de la r�gle
     */
    public void setRuleName( String pRuleName )
    {
        mRuleName = pRuleName;
    }

    /**
     * @param pDetails les d�tails
     */
    public void setDetails( Collection pDetails )
    {
        mDetails = pDetails;
    }

    /**
     * @return true si il y a un lien vers un composant
     */
    public boolean getComponentLink()
    {
        return mComponentLink;
    }

    /**
     * @param pComponentLink lien vers composant
     */
    public void setComponentLink( boolean pComponentLink )
    {
        mComponentLink = pComponentLink;
    }

}
