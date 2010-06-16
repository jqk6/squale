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
package org.squale.squalerest.model;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Module class used for the export
 */
@XStreamAlias( "module" )
public class ModuleRest
{

    /**
     * The name of the module
     */
    @XStreamAsAttribute
    private String name;

    /**
     * The id of the module
     */
    @XStreamAsAttribute
    private String id;

    /**
     * The list of data of the module
     */
    @XStreamImplicit
    private List<FactorRest> datas;

    /**
     * Default constructor
     */
    public ModuleRest()
    {
        datas = new ArrayList<FactorRest>();
    }

    /**
     * Constructor
     * 
     * @param pId The id of the module
     * @param pName The name of the module
     */
    public ModuleRest( String pId, String pName )
    {
        id = pId;
        name = pName;
        datas = new ArrayList<FactorRest>();
    }

    /**
     * Getter method for the attribute id
     * 
     * @return The id of the module
     */
    public String getId()
    {
        return id;
    }

    /**
     * Setter method for the attribute id
     * 
     * @param pId The new id of the module
     */
    public void setId( String pId )
    {
        id = pId;
    }

    /**
     * Getter method for the attribute name
     * 
     * @return The name of the module
     */
    public String getName()
    {
        return name;
    }

    /**
     * Setter method for the attribute name
     * 
     * @param pName The new name of the module
     */
    public void setName( String pName )
    {
        name = pName;
    }

    /**
     * Getter method for the attribute datas
     * 
     * @return The list of data linked to the module
     */
    public List<FactorRest> getDatas()
    {
        return datas;
    }

    /**
     * Add a factor to the list of datas
     * 
     * @param factor The factor to add
     */
    public void addDatas( FactorRest factor )
    {
        datas.add( factor );
    }

}