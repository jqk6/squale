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
package org.squale.squaleweb.applicationlayer.formbean.component;

/**
 * Donn�es synth�tiques d'un facteur
 */
public class FactorRuleForm
    extends QualityRuleForm
{

    /** L'id de l'objet */
    private long mId;

    /** Criteres */
    private CriteriaListForm mCriteria = new CriteriaListForm();

    /**
     * @return id
     */
    public long getId()
    {
        return mId;
    }

    /**
     * @param pId id
     */
    public void setId( long pId )
    {
        mId = pId;
    }

    /**
     * @return crit�res
     */
    public CriteriaListForm getCriteria()
    {
        return mCriteria;
    }

    /**
     * Ajout d'un crit�re
     * 
     * @param pCriterium crit�re
     */
    public void addCriterium( CriteriumRuleForm pCriterium )
    {
        mCriteria.getList().add( pCriterium );
    }

    /**
     * @param pCriteria modifie les crit�res
     */
    public void setCriteria( CriteriaListForm pCriteria )
    {
        mCriteria = pCriteria;
    }
}
