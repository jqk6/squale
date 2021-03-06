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
//Source file: D:\\cc_views\\squale_v0_0_act_M400843\\squale\\src\\squaleCommon\\src\\org\\squale\\squalecommon\\enterpriselayer\\businessobject\\component\\ClassBO.java

package org.squale.squalecommon.enterpriselayer.businessobject.component;

import java.util.Collection;

import org.squale.squalecommon.enterpriselayer.businessobject.UnexpectedRelationException;

/**
 * Repr�sente une classe au sens Java et C++
 * 
 * @author m400842
 * @hibernate.subclass lazy="true" discriminator-value="Class"
 */
public class ClassBO
    extends AbstractComplexComponentBO
{

    /**
     * Chemin du fichier � partir du projet
     */
    private String mFileName;

    /**
     * Instancie un nouveau composant.
     * 
     * @param pName Nom du composant.
     * @roseuid 42AFF04102A0
     */
    public ClassBO( final String pName )
    {
        super();
        setName( pName );
    }

    /**
     * Constructeur par d�faut.
     * 
     * @roseuid 42CBA49F0100
     */
    public ClassBO()
    {
        super();
    }

    /**
     * Constructeur complet.
     * 
     * @param pName nom du composant
     * @param pChildren les enfants
     * @param pParent Composant parent
     * @throws UnexpectedRelationException si la relation ne peut etre ajout�
     * @roseuid 42CBA49F010F
     */
    public ClassBO( String pName, Collection pChildren, AbstractComplexComponentBO pParent )
        throws UnexpectedRelationException
    {
        super( pName, pChildren, pParent );
    }

    /**
     * @return le chemin du fichier � partir du projet
     * @hibernate.property name="fileName" column="LongFileName" type="string" length="2048" update="true" insert="true"
     */
    public String getFileName()
    {
        return mFileName;
    }

    /**
     * @param pFileName le nouveau chemin du fichier
     */
    public void setFileName( String pFileName )
    {
        mFileName = pFileName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.squale.squalecommon.enterpriselayer.businessobject.component.AbstractComponentBO#accept(org.squale.squalecommon.enterpriselayer.businessobject.component.ComponentVisitor,
     *      java.lang.Object)
     */
    public Object accept( ComponentVisitor pVisitor, Object pArgument )
    {
        return pVisitor.visit( this, pArgument );
    }
}
