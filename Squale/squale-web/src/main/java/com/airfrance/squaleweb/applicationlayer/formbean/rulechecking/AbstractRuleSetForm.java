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
package com.airfrance.squaleweb.applicationlayer.formbean.rulechecking;

import java.util.Date;

import com.airfrance.squaleweb.applicationlayer.formbean.ActionIdFormSelectable;

/**
 * Formulaire d'un ruleset
 */
public class AbstractRuleSetForm
    extends ActionIdFormSelectable
{
    /** Date de mise � jour */
    private Date mDateOfUpdate;

    /**
     * Nom du RuleSet
     */
    private String mName = "";

    /**
     * @return date de mise � jour
     */
    public Date getDateOfUpdate()
    {
        return mDateOfUpdate;
    }

    /**
     * @return le nom
     */
    public String getName()
    {
        return mName;
    }

    /**
     * @param pDateOfUpdate date de mise � jour
     */
    public void setDateOfUpdate( Date pDateOfUpdate )
    {
        mDateOfUpdate = pDateOfUpdate;

    }

    /**
     * @param pName le nom
     */
    public void setName( String pName )
    {
        mName = pName;
    }

}
