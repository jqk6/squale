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
package org.squale.squalecommon.datatransfertobject.config;

import java.io.Serializable;

/**
 *<p>
 * BO for the squale-params. Squale-params are parameters which should be persist.
 *</p>
 *<p>
 * The parameters which come from the squale-config.xml are not managed by this BO but by {@link AdminParamsBO}
 *</p>
 */
public class SqualeParamsDTO
    implements Serializable
{

    /**
     * UID
     */
    private static final long serialVersionUID = -4103480889159318754L;

    /**
     * Object ID
     */
    private long id;

    /**
     * The parameter key
     */
    private String paramKey;

    /**
     * The parameter value
     */
    private String paramValue;

    /**
     * Constructor
     */
    public SqualeParamsDTO()
    {
        id = -1;
        paramKey = "";
        paramValue = "";
    }

    /**
     * Getter method for the paramKey variable.
     * 
     * @return the key of the parameter
     */
    public String getParamKey()
    {
        return paramKey;
    }

    /**
     * Setter method for the paramKey variable
     * 
     * @param pParamKey The new parameter key value
     */
    public void setParamKey( String pParamKey )
    {
        paramKey = pParamKey;
    }

    /**
     * Getter method for the paramValue variable
     * 
     * @return the value of the parameter
     */

    public String getParamValue()
    {
        return paramValue;
    }

    /**
     * Setter method for the paramValue variable
     * 
     * @param pParamValue The new value of the parameter
     */
    public void setParamValue( String pParamValue )
    {
        paramValue = pParamValue;
    }

    /**
     * Getter method for the id of the object
     * 
     * @return the ID of the object
     */
    public long getId()
    {
        return id;
    }

    /**
     * Setter method for the id of the object
     * 
     * @param pId the new id of the object
     */
    public void setId( long pId )
    {
        id = pId;
    }

    /**
     * This method set new values for paramKey and paramVlaue
     * 
     * @param key The new value of paramKey
     * @param value The new value of paramValue
     */
    public void setSqualeParam( String key, String value )
    {
        paramKey = key;
        paramValue = value;
    }

}
