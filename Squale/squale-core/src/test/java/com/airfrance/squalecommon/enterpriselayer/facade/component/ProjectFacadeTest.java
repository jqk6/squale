package com.airfrance.squalecommon.enterpriselayer.facade.component;

import java.util.Map;

import com.airfrance.jraf.helper.PersistenceHelper;
import com.airfrance.jraf.provider.persistence.hibernate.facade.FacadeHelper;
import com.airfrance.jraf.spi.persistence.IPersistenceProvider;
import com.airfrance.jraf.spi.persistence.ISession;
import com.airfrance.squalecommon.SqualeTestCase;
import com.airfrance.squalecommon.datatransfertobject.component.ApplicationConfDTO;
import com.airfrance.squalecommon.datatransfertobject.component.ProjectConfDTO;
import com.airfrance.squalecommon.datatransfertobject.component.parameters.MapParameterDTO;
import com.airfrance.squalecommon.datatransfertobject.component.parameters.StringParameterDTO;
import com.airfrance.squalecommon.datatransfertobject.config.ProjectProfileDTO;
import com.airfrance.squalecommon.datatransfertobject.config.SourceManagementDTO;
import com.airfrance.squalecommon.datatransfertobject.rule.QualityGridDTO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.MapParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.config.ProjectProfileBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.config.SourceManagementBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.QualityGridBO;

/**
 * Test de la facade project
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class ProjectFacadeTest extends SqualeTestCase {
    /**
     * provider de persistence
     */
    private static IPersistenceProvider PERSISTENTPROVIDER = PersistenceHelper.getPersistenceProvider();

    /**
     * Test du get
     */
    public void testGet() {
        try {
            ISession session = PERSISTENTPROVIDER.getSession();
            ApplicationBO application = getComponentFactory().createApplication(session);
            QualityGridBO grid = getComponentFactory().createGrid(session);
            ProjectProfileBO profile = getComponentFactory().createProjectProfile(session);
            SourceManagementBO manager = getComponentFactory().createSourceManagement(session);
            MapParameterBO parameters = getComponentFactory().createParameters(session);
            ProjectBO project = getComponentFactory().createProject(session, application, grid, profile, manager, parameters);
            ProjectConfDTO projectConf = new ProjectConfDTO();
            projectConf.setId(project.getId());
            ProjectConfDTO out = ProjectFacade.get(projectConf);
            FacadeHelper.closeSession(session, "");
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected exception");
        }
    }

    /**
     * Test du update
     */
    public void testUpdate() {
        try {
            ISession session = PERSISTENTPROVIDER.getSession();
            ApplicationBO application = getComponentFactory().createApplication(session);
            QualityGridBO grid = getComponentFactory().createGrid(session);
            ProjectProfileBO profile = getComponentFactory().createProjectProfile(session);
            SourceManagementBO manager = getComponentFactory().createSourceManagement(session);
            MapParameterBO parameters = getComponentFactory().createParameters(session);
            ProjectBO project = getComponentFactory().createProject(session, application, grid, profile, manager, parameters);
            assertEquals(0, project.getParameters().getParameters().size());
            ApplicationConfDTO applicationConf = new ApplicationConfDTO();
            applicationConf.setId(application.getId());
            ProjectConfDTO projectConf = new ProjectConfDTO();
            projectConf.setId(project.getId());
            projectConf.setName(project.getName());
            // Cr�ation des param�tres
            MapParameterDTO params = new MapParameterDTO();
            StringParameterDTO strParam = new StringParameterDTO("strParam");
            params.getParameters().put("strParam", strParam);
            projectConf.setParameters(params);
            // La grille
            QualityGridDTO gridDTO = new QualityGridDTO();
            gridDTO.setName(grid.getName());
            projectConf.setQualityGrid(gridDTO);
            // Le profil
            ProjectProfileDTO profileDTO = new ProjectProfileDTO();
            profileDTO.setName(profile.getName());
            projectConf.setProfile(profileDTO);
            // Le source manager
            SourceManagementDTO managerDTO = new SourceManagementDTO();
            managerDTO.setName(manager.getName());
            projectConf.setSourceManager(managerDTO);
            ProjectFacade.update(projectConf, applicationConf, getSession());
            ProjectConfDTO projectconfGetting = ProjectFacade.get(projectConf);
            MapParameterDTO paramsDTOGetting = projectconfGetting.getParameters();
            Map mapGetting = paramsDTOGetting.getParameters();
            assertEquals(1, mapGetting.size());
            FacadeHelper.closeSession(session, "");
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected exception");
        }
    }

}
