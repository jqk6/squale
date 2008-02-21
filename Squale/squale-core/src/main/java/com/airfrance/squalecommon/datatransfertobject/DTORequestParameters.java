package com.airfrance.squalecommon.datatransfertobject;

import com.airfrance.squalecommon.datatransfertobject.component.AuditDTO;
import com.airfrance.squalecommon.datatransfertobject.component.ComponentDTO;


/**
 * Classe pour transf�rer les valeurs des id de projet,
 * d'application et d'audits entre les diff�rentes pages
 */
public class DTORequestParameters {
    
    /** le projet - peut etre null */
    private ComponentDTO projectDto;
    
    /** l'application - peut etre null */
    private ComponentDTO applicationDto;
    
    /** l'audit courant - peut etre null */
    private AuditDTO currentAuditDto;
    
    /** l'audit pr�c�dent sur le projet - peut etre null */
    private AuditDTO previousAuditDto;
    
    /**
     * @return l'id de l'application
     */
    public ComponentDTO getApplicationDto() {
        return applicationDto;
    }

    /**
     * @return l'id de l'audit courant
     */
    public AuditDTO getCurrentAuditDto() {
        return currentAuditDto;
    }

    /**
     * @return l'id de l'audit pr�c�dent
     */
    public AuditDTO getPreviousAuditDto() {
        return previousAuditDto;
    }

    /**
     * @return l'id du projet
     */
    public ComponentDTO getProjectDto() {
        return projectDto;
    }

    /**
     * @param newApplicationDto le nouvel id de l'application
     */
    public void setApplicationDto(ComponentDTO newApplicationDto) {
        applicationDto = newApplicationDto;
    }

    /**
      * @param newCurrentAuditDto le nouvel id de l'application
      */
    public void setCurrentAuditDto(AuditDTO newCurrentAuditDto) {
        currentAuditDto = newCurrentAuditDto;
    }

    /**
      * @param newPreviousAuditDto le nouvel id de l'application
      */
    public void setPreviousAuditDto(AuditDTO newPreviousAuditDto) {
        previousAuditDto = newPreviousAuditDto;
    }

    /**
     * @param newProjectDto le nouvel id de l'application
     */
    public void setProjectDto(ComponentDTO newProjectDto) {
        projectDto = newProjectDto;
    }

    /**
     * @return l'id de l'application ou -1 si elle n'est pas d�finie
     */
    public String getApplicationId() {
        String result = "-1";
        if(applicationDto != null){
            result = ""+ applicationDto.getID();
        }
        return result;
    }

    /**
     * @return l'id du projet ou -1 si il n'est pas d�fini
     */
    public String getProjectId() {
        String result = "-1";
        if(projectDto != null){
            result = ""+ projectDto.getID();
        }
        return result;
    }

    /**
     * @return l'id de l'audit courant ou -1 si il n'est pas d�fini
     */
    public String getCurrentAuditId() {
        String result = "-1";
        if(currentAuditDto != null){
            result = ""+ currentAuditDto.getID();
        }
        return result;
    }

}