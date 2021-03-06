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
package org.squale.squalecommon.util.mapping;

import java.util.Hashtable;
import java.util.Map;

/**
 * Map pour le mapping entre un nom et un nom de classe
 */
public class MappingMap
{
    /** Map avec le nom comme clef et la nom de classe comme valeur */
    private Map mNameMap = new Hashtable();

    /** Map avec le nom de classe commeclef et la nom comme valeur */
    private Map mClassNameMap = new Hashtable();

    /**
     * Ajout d'une entr�e
     * 
     * @param pName nom
     * @param pClass nom de la classe
     */
    public void put( String pName, Class pClass )
    {
        mNameMap.put( pName, pClass );
        mClassNameMap.put( pClass, pName );
    }

    /**
     * Obtention de la classe correspondant � un nom
     * 
     * @param pName nom
     * @return classe correspondante
     */
    public Class getClassNameForName( String pName )
    {
        return (Class) mNameMap.get( pName );
    }

    /**
     * Obtention du nom correspondant � un nom de classe
     * 
     * @param pClass nom de la classe
     * @return nom correspondant
     */
    public String getNameForClass( Class pClass )
    {
        return (String) mClassNameMap.get( pClass );
    }
}
