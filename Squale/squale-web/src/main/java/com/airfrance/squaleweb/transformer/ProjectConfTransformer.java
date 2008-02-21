package com.airfrance.squaleweb.transformer;

import com.airfrance.squalecommon.datatransfertobject.component.ProjectConfDTO;
import com.airfrance.squalecommon.datatransfertobject.component.parameters.MapParameterDTO;
import com.airfrance.squalecommon.datatransfertobject.config.ProjectProfileDTO;
import com.airfrance.squalecommon.datatransfertobject.config.SourceManagementDTO;
import com.airfrance.squalecommon.datatransfertobject.rule.QualityGridDTO;
import com.airfrance.squaleweb.applicationlayer.formbean.creation.CreateProjectForm;
import com.airfrance.welcom.struts.bean.WActionForm;
import com.airfrance.welcom.struts.transformer.WITransformer;
import com.airfrance.welcom.struts.transformer.WTransformerException;

/**
 * Transformer pour le bean CreateProjectForm
 */
public class ProjectConfTransformer implements WITransformer {

    /**
      * @param pObject le tableau de ProjectDTO � transformer en formulaires.
      * @throws WTransformerException si un pb appara�t.
      * @return le formulaire associ�
      */
    public WActionForm objToForm(Object[] pObject) throws WTransformerException {
        CreateProjectForm form = new CreateProjectForm();
        objToForm(pObject, form);
        return form;
    }

    /**
     * @param pObject le tableau de ProjectDTO � transformer en formulaires.
     * @param pForm le formulaire � remplir.
     * @throws WTransformerException si un pb appara�t.
     */
    public void objToForm(Object[] pObject, WActionForm pForm) throws WTransformerException {
        ProjectConfDTO projectConf = (ProjectConfDTO) pObject[0];
        CreateProjectForm form = (CreateProjectForm) pForm;
        MapParameterDTO params = projectConf.getParameters();
        /* Param�tres g�n�raux */
        form.setProjectName(projectConf.getName());
        form.setProjectId("" + projectConf.getId());
        form.setStatus(projectConf.getStatus());
        // Place le nom (le nom est unique)
        form.setQualityGrid(projectConf.getQualityGrid().getName());
        form.setProfile(projectConf.getProfile().getName());
        form.setSourceManagement(projectConf.getSourceManager().getName());
        /* Affectation des param�tres */
        form.setParameters(projectConf.getParameters());
    }

    /**
     * 
     * @param pForm le formulaire � lire.
     * @throws WTransformerException si un pb appara�t.
     * @return le formulaire associ�
     */
    public Object[] formToObj(WActionForm pForm) throws WTransformerException {
        ProjectConfDTO projectConf = new ProjectConfDTO();
        Object obj[] = { projectConf };
        formToObj(pForm, obj);
        return obj;
    }

    /**
     * @param pForm le formulaire � lire.
     * @param pObject le tableau de ProjectDTO qui r�cup�re les donn�es du formulaire.
     * @throws WTransformerException si un pb appara�t.
     */
    public void formToObj(WActionForm pForm, Object[] pObject) throws WTransformerException {

        // Initialisation
        ProjectConfDTO project = (ProjectConfDTO) pObject[0];
        CreateProjectForm form = (CreateProjectForm) pForm;
        if(form.getProjectId().equals("")){
            project.setId(-1);
        }else{
            project.setId(new Long(form.getProjectId()).longValue());
        }
        project.setName(form.getProjectName());
        project.setStatus(form.getStatus());
        // Placement du profile
        ProjectProfileDTO profileDTO = new ProjectProfileDTO();
        profileDTO.setName(form.getProfile());
        project.setProfile(profileDTO);
        // Placement du source manager
        SourceManagementDTO managerDTO = new SourceManagementDTO();
        managerDTO.setName(form.getSourceManagement());
        project.setSourceManager(managerDTO);
        // Placement de la grille qualit�
        QualityGridDTO gridDTO = new QualityGridDTO();
        gridDTO.setName(form.getQualityGrid());
        project.setQualityGrid(gridDTO);
        project.setParameters(form.getParameters());
    }
}
