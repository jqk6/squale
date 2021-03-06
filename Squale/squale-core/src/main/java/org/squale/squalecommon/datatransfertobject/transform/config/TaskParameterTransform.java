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
package org.squale.squalecommon.datatransfertobject.transform.config;

import org.squale.squalecommon.datatransfertobject.config.TaskParameterDTO;
import org.squale.squalecommon.enterpriselayer.businessobject.config.TaskParameterBO;

/**
 * Conversion des param�tres de t�che
 */
public class TaskParameterTransform
{

    /**
     * Transformation BO - DTO
     * 
     * @param pTaskParameter param�tre de t�che
     * @return param�tre converti
     */
    static public TaskParameterDTO bo2dto( TaskParameterBO pTaskParameter )
    {
        TaskParameterDTO result = new TaskParameterDTO();
        result.setName( pTaskParameter.getName() );
        result.setValue( pTaskParameter.getValue() );
        return result;
    }

    // Le dto2bo est inutile car on ne modifie pas les param�tres dans le web
}
