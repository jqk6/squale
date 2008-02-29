package com.airfrance.squalecommon.daolayer.component;

import java.util.Collection;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.jraf.spi.persistence.ISession;
import com.airfrance.squalecommon.SqualeTestCase;
import com.airfrance.squalecommon.datatransfertobject.component.ComponentDTO;
import com.airfrance.squalecommon.datatransfertobject.transform.component.ComponentTransform;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.config.ProjectProfileBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.profile.UserBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.QualityGridBO;

/**
 * @author M400843
 *
 */
public class ProjectDAOImplTest extends SqualeTestCase {

    /**
     * Test pour void remove(ISession, ProjectBO)
     * @throws JrafDaoException en cas de probl�me avec la base
     */
    final public void testRemoveISessionProjectBO() throws JrafDaoException {
        ISession session = getSession();
        ProjectDAOImpl projectDAO = ProjectDAOImpl.getInstance();

        session.beginTransaction();
        ApplicationBO appli = getComponentFactory().createApplication(session);
        ProjectBO project = getComponentFactory().createProject(session, appli, null);

        session.commitTransactionWithoutClose();

        assertNotNull(project);

        session.beginTransaction();
        projectDAO.remove(session, project);
        session.commitTransactionWithoutClose();

        session.beginTransaction();
        project = (ProjectBO) projectDAO.load(session, new Long(project.getId()));
        session.commitTransactionWithoutClose();

        assertNull(project);
        /* Pour bien tester il faudrait v�rifier que les composant fils ainsi 
         * que les r�sultats li�s n'�xistent plus..
         */
    }

    /**
         * test la r�cup�ration de tous les projets d'un audit
         * @throws JrafDaoException en cas d'�chec
         */
    public void testFindAllProjects() throws JrafDaoException {
        ApplicationDAOImpl applicationDAO = ApplicationDAOImpl.getInstance();
        ISession session = getSession();
        // on utilise une transaction pour cr�er l'application
        session.beginTransaction();
        ApplicationBO application = getComponentFactory().createApplication(session);

        // on cr�e 1 projet fils
        ProjectBO projet1 = getComponentFactory().createProject(getSession(), application, null);
        session.commitTransactionWithoutClose();
        

        // on v�rifie qu'on le r�cup�re bien
        Collection col = ProjectDAOImpl.getInstance().findAllProjects(getSession(), new Long(application.getId()));
        assertEquals(col.size(), 1);
    }
    
    /**
     * Test la r�cup�ration des projets en fonction de leur nom, du nom de leur application
     * et de la liste des applications visibles par l'utilisateur
     * @throws JrafDaoException en cas d'�chec
     */
    public void testfindProjects() throws JrafDaoException {
        ProjectDAOImpl dao = ProjectDAOImpl.getInstance();
        UserBO user = getComponentFactory().createUser(getSession());
        ApplicationBO application = getComponentFactory().createApplication(getSession());
        QualityGridBO grid = getComponentFactory().createGrid(getSession());
        ProjectBO project = getComponentFactory().createProject(getSession(), application, grid);
        ComponentDTO appliDTO = ComponentTransform.bo2Dto(application);
        long[] userAppli = new long[]{appliDTO.getID()};
        Collection projects = dao.findProjects(getSession(), userAppli, "", "proj");
        assertEquals(1, projects.size());
        projects = dao.findProjects(getSession(), userAppli, "test", "proj");
        assertEquals(0, projects.size());
        projects = dao.findProjects(getSession(), userAppli, "", "test");
        assertEquals(0, projects.size());
        
    }
    
    /**
     * Teste la r�cup�ration des projets en fonction de leur statut et du site de leur application
     * @throws JrafDaoException si erreur
     */
    public void testFindWhereStatusAndSite() throws JrafDaoException {
        ProjectDAOImpl dao = ProjectDAOImpl.getInstance();
        ApplicationBO application = getComponentFactory().createApplicationWithSite(getSession(), "qvi");
        QualityGridBO grid = getComponentFactory().createGrid(getSession());
        ProjectBO project = getComponentFactory().createProject(getSession(), application, grid);
        ApplicationBO applicationWithoutSite = getComponentFactory().createApplication(getSession());
        ProjectBO projectDeleted = getComponentFactory().createProject(getSession(), applicationWithoutSite, grid);
        projectDeleted.setStatus(ProjectBO.DELETED);
        dao.save(getSession(), projectDeleted);
        Collection projects = dao.findWhereStatusAndSite(getSession(), ProjectBO.DELETED, applicationWithoutSite.getServeurBO().getServeurId());
        assertEquals(1, projects.size());
    }
    
    /**
     * Teste la r�cup�ration des projets en fonction de leur statut et du site de leur application
     * @throws JrafDaoException si erreur
     */
    public void testCountBySiteAndProfil() throws JrafDaoException {
        getSession().beginTransaction();
        ProjectDAOImpl dao = ProjectDAOImpl.getInstance();
        ApplicationBO application = getComponentFactory().createApplicationWithSite(getSession(), "qvi");
        QualityGridBO grid = getComponentFactory().createGrid(getSession());
        ProjectProfileBO profil = getComponentFactory().createProjectProfile(getSession());
        profil.setName("java");
        ProjectBO project = getComponentFactory().createProject(getSession(), application, grid);
        project.setProfile(profil);
        int nbProjects = dao.countBySiteAndProfil(getSession(), application.getServeurBO().getServeurId(), "java");
        assertEquals(1, nbProjects);
        getSession().commitTransactionWithoutClose();
    }

}
