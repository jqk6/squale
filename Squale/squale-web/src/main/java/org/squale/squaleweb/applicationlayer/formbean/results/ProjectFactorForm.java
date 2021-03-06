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
package org.squale.squaleweb.applicationlayer.formbean.results;

import org.squale.squaleweb.applicationlayer.formbean.ActionIdForm;

/**
 * Résultat d'un facteur pour un projet
 */
public class ProjectFactorForm
    extends ActionIdForm
{

    /** Nom du facteur */
    private String mName = "";

    /** Note actuelle du facteur */
    private String mCurrentMark = "";

    /** Note précédante du facteur */
    private String mPredeccesorMark = "";

    /** The mean for this factor on the segmentation linked to the application */
    private SharedRepoStatForm statForm;

    /**
     * @return nom
     */
    public String getName()
    {
        return mName;
    }

    /**
     * @return la note courante
     */
    public String getCurrentMark()
    {
        return mCurrentMark;
    }

    /**
     * @return la note courante
     */
    public String getPredecessorMark()
    {
        return mPredeccesorMark;
    }

    /**
     * @param pString nom
     */
    public void setName( String pString )
    {
        mName = pString;
    }

    /**
     * @param pString la nouvelle note courante
     */
    public void setCurrentMark( String pString )
    {
        if ( pString != null )
        {
            mCurrentMark = pString;
        }
    }

    /**
     * @param pString la nouvelle note de l'audit précédant
     */
    public void setPredecessorMark( String pString )
    {
        if ( pString != null )
        {
            mPredeccesorMark = pString;
        }
    }

    /**
     * Getter method for the attribute statForm
     * 
     * @return The statForm
     */
    public SharedRepoStatForm getStatForm()
    {
        return statForm;
    }

    /**
     * Setter method for the attribute statForm
     * 
     * @param pStatForm The new statForm
     */
    public void setStatForm( SharedRepoStatForm pStatForm )
    {
        statForm = pStatForm;
    }

}
