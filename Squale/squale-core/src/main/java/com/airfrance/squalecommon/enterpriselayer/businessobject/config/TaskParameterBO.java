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
package com.airfrance.squalecommon.enterpriselayer.businessobject.config;

/**
 * Param�tre de t�che Une t�che Squalix peut �tre configur�e par des param�tres dynamiques, ceux-ci sont d�finis dans le
 * ficheir de configuration de squalix. Un param�tre est d�fini par un nom et une valeur
 * 
 * @hibernate.class table="TaskParameter" lazy="true"
 */
public class TaskParameterBO
{
    /**
     * Identifiant (au sens technique) de l'objet
     */
    protected long mId = -1;

    /**
     * Nom du param�tre
     */
    private String mName;

    /**
     * Valeur du param�tre
     */
    private String mValue;

    /**
     * M�thode d'acc�s pour mId
     * 
     * @return la l'identifiant (au sens technique) de l'objet
     * @hibernate.id generator-class="native" type="long" column="TaskParameterId" unsaved-value="-1" length="19"
     * @hibernate.generator-param name="sequence" value="taskParameter_sequence"
     */
    public long getId()
    {
        return mId;
    }

    /**
     * Change la valeur de mId
     * 
     * @param pId le nouvel identifiant
     */
    public void setId( long pId )
    {
        mId = pId;
    }

    /**
     * M�thode d'acc�s � mName
     * 
     * @return le nom
     * @hibernate.property name="name" column="Name" type="string" length="255" not-null="true" update="true"
     *                     insert="true"
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
     * @hibernate.property name="name" column="Value" type="string" length="255" not-null="true" update="true"
     *                     insert="true"
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
