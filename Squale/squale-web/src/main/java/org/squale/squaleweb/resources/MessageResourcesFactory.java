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
package org.squale.squaleweb.resources;

import org.apache.struts.util.MessageResources;
import org.apache.struts.util.PropertyMessageResourcesFactory;

/**
 * Fabrique de lecture de message
 */
public class MessageResourcesFactory
    extends PropertyMessageResourcesFactory
{

    /**
     * @see org.apache.struts.util.MessageResourcesFactory#createResources(java.lang.String) {@inheritDoc}
     */
    public MessageResources createResources( String config )
    {
        MessageResources mes = super.createResources( config );
        return new MessageResourcesProxy( this, config, mes );
    }

}
