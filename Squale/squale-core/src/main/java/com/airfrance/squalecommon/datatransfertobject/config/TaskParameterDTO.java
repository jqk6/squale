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
package com.airfrance.squalecommon.datatransfertobject.config;

/**
 * Param�tre de t�che
 */
public class TaskParameterDTO
{
    /**
     * Nom du param�tre
     */
    private String mName;

    /**
     * Valeur du param�tre
     */
    private String mValue;

    /**
     * M�thode d'acc�s � mName
     * 
     * @return le nom
     */
    public String getName()
    {
        return mName;
    }

    /**
     * @param pName nom
     */
    public void setName( String pName )
    {
        mName = pName;
    }

    /**
     * M�thode d'acc�s � mValue
     * 
     * @return la valeur
     */
    public String getValue()
    {
        return mValue;
    }

    /**
     * @param pValue nom
     */
    public void setValue( String pValue )
    {
        mValue = pValue;
    }

}
