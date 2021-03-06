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
package org.squale.squalecommon.enterpriselayer.businessobject.config.web;

import java.util.HashSet;
import java.util.Set;

/**
 * Configuration pour l'affichage de la volum�trie
 * 
 * @hibernate.subclass lazy="true" discriminator-value="volumetry"
 */
public class VolumetryConfBO
    extends AbstractDisplayConfBO
{

    /** Le type du composant (application, projet) */
    private String mComponentType;

    /** L'ensemble des TREs correspondant � une mesure de la volum�trie */
    private Set mTres = new HashSet();

    /**
     * @return le type du composant
     * @hibernate.property name="componentType" column="componentType" type="string" insert="true" update="true"
     */
    public String getComponentType()
    {
        return mComponentType;
    }

    /**
     * @return les TREs
     * @hibernate.set table="Volumetry_Measures" lazy="false" inverse="false" cascade="none" sort="unsorted"
     * @hibernate.key column="VolumetryId" 
     * @hibernate.element column="Measure" type="string" not-null="true" unique="false"
     */
    public Set getTres()
    {
        return mTres;
    }

    /**
     * @param pType le type du composant
     */
    public void setComponentType( String pType )
    {
        mComponentType = pType;
    }

    /**
     * @param pTres les TREs
     */
    public void setTres( Set pTres )
    {
        mTres = pTres;
    }

    /**
     * Ajoute un nom de tre
     * 
     * @param pTre le nom du tre
     */
    public void addTre( String pTre )
    {
        mTres.add( pTre );
    }

    /**
     * {@inheritDoc}
     * 
     * @param pVisitor {@inheritDoc}
     * @param pArgument {@inheritDoc}
     * @return {@inheritDoc}
     * @see org.squale.squalecommon.enterpriselayer.businessobject.config.web.AbstractDisplayConfBO#accept(org.squale.squalecommon.enterpriselayer.businessobject.config.web.DisplayConfVisitor,
     *      java.lang.Object)
     */
    public Object accept( DisplayConfVisitor pVisitor, Object pArgument )
    {
        return pVisitor.visit( this, pArgument );
    }

    /**
     * {@inheritDoc}
     * 
     * @param obj {@inheritDoc}
     * @return {@inheritDoc}
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals( Object obj )
    {
        boolean result = false;
        if ( obj instanceof VolumetryConfBO )
        {
            VolumetryConfBO volum = (VolumetryConfBO) obj;
            result = volum.getComponentType().equals( getComponentType() );
            result &= volum.getTres().equals( getTres() );
        }
        return result;
    }

    /**
     * Redefinition of the hashCode method {@inheritDoc}
     * 
     * @return return the hash number of the object
     */
    public int hashCode()
    {
        return super.hashCode();
    }

}
