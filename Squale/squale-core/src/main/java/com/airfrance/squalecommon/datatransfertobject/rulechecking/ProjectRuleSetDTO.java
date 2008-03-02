package com.airfrance.squalecommon.datatransfertobject.rulechecking;

import com.airfrance.squalecommon.datatransfertobject.component.ComponentDTO;

/**
 * DTO pour ProjectRuleSetBO
 */
public class ProjectRuleSetDTO
    extends RuleSetDTO
{

    /** Projet sur lequel s'appliquent les r�gles */
    private ComponentDTO mProject;

    /**
     * @return le projet sur lequel s'appliquent les r�gles
     */
    public ComponentDTO getProject()
    {
        return mProject;
    }

    /**
     * Modifie mProject
     * 
     * @param pProject le projet sur lequel s'appliquent les r�gles
     */
    public void setProject( ComponentDTO pProject )
    {
        mProject = pProject;
    }

}
