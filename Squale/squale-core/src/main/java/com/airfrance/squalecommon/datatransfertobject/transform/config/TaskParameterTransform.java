package com.airfrance.squalecommon.datatransfertobject.transform.config;

import com.airfrance.squalecommon.datatransfertobject.config.TaskParameterDTO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.config.TaskParameterBO;

/**
 * Conversion des param�tres de t�che
 */
public class TaskParameterTransform {

    /**
     * Transformation BO - DTO
     * @param pTaskParameter param�tre de t�che
     * @return param�tre converti
     */
    static public TaskParameterDTO bo2dto(TaskParameterBO pTaskParameter) {
        TaskParameterDTO result = new TaskParameterDTO();
        result.setName(pTaskParameter.getName());
        result.setValue(pTaskParameter.getValue());
        return result;
    }
    
    // Le dto2bo est inutile car on ne modifie pas les param�tres dans le web
}
