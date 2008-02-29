package com.airfrance.squalecommon.daolayer.profile;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.airfrance.jraf.provider.persistence.hibernate.facade.FacadeHelper;
import com.airfrance.squalecommon.SqualeTestCase;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.profile.ProfileBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.profile.UserBO;

/**
 * Tests du DAO user
 *
 */
public class UserDAOImplTest extends SqualeTestCase {

    /**
     * 
     */
    final public void testLoadWithMatricule() {
        try {
            getSession().beginTransaction();
            UserBO user = getComponentFactory().createUser(getSession());

            UserDAOImpl userDAO = UserDAOImpl.getInstance();
            user = userDAO.loadWithMatricule(getSession(), user.getMatricule());
            assertNotNull(user);
            user = userDAO.loadWithMatricule(getSession(), "nimportequoi");
            assertNull(user);
            FacadeHelper.closeSession(getSession(), "");
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected exception");
        }
    }

    /**
     * 
     */
    final public void testFindWhereAdmin() {
        try {
            getSession().beginTransaction();
            UserDAOImpl userDAO = UserDAOImpl.getInstance();
            Collection col = userDAO.findWhereAdmin(getSession(), false, true);
            assertEquals(0, col.size());
            UserBO user = getComponentFactory().createUser(getSession());
            // On le d�sabonne de l'envoi des mails
            user.setUnsubscribed(true);
            userDAO.save(getSession(), user);
            col = userDAO.findWhereAdmin(getSession(), false, true);
            // 1 car on on a r�cup�r� aussi les d�sabonn�
            assertEquals(1, col.size());
            // 0 car l'administrateur n'est pas abonn�
            col = userDAO.findWhereAdmin(getSession(), false, false);
            assertEquals(0, col.size());

            FacadeHelper.closeSession(getSession(), "");
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected exception");
        }
    }

    /**
     * Teste la r�cup�ration d'administrateurs ayant un email d�fini
     */
    final public void testFindAdminsWithEmails() {
        try {
            getSession().beginTransaction();
            UserDAOImpl userDAO = UserDAOImpl.getInstance();
            Collection col = userDAO.findWhereAdminAndHaveEmails(getSession(), true);
            assertEquals(0, col.size());
            // on cr�e 2 utilisateurs dont un sans email et on 
            // v�rifie qu'on r�cup�re bien seulement celui qui a un email
            UserBO user = getComponentFactory().createUser(getSession());
            user.setEmail("test@Junit.fr");
            // On le d�sabonne de l'envoi des mails
            user.setUnsubscribed(true);
            UserBO userWithoutMail = getComponentFactory().createUser(getSession());
            col = userDAO.findWhereAdminAndHaveEmails(getSession(), true);
            assertEquals(1, col.size());
            assertTrue(((UserBO) ((Iterator) (col.iterator())).next()).getEmail() != null);
            // On ne r�cup�re pas les d�sabonn�s
            col = userDAO.findWhereAdminAndHaveEmails(getSession(), false);
            assertEquals(0, col.size());
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected exception");
        }
    }
    
    /**
     * test la r�cup�ration des utilisateurs selon les droits et les mails
     */
    final public void testFindWhereApplicationAndProfileAndHaveEmails() {
        try {
            getSession().beginTransaction();
            // On cr�e une application qui aura un manager et un lecteur
            ApplicationBO appli = getComponentFactory().createApplication(getSession());
            // On cr�e les deux profils lecteur et manager
            ProfileDAOImpl profileDAO = ProfileDAOImpl.getInstance();
            ProfileBO managerProfile = new ProfileBO();
            managerProfile.setName(ProfileBO.MANAGER_PROFILE_NAME);
            managerProfile.setRights(new HashMap());
            ProfileBO readerProfile = new ProfileBO();
            readerProfile.setName(ProfileBO.READER_PROFILE_NAME);
            readerProfile.setRights(new HashMap());
            profileDAO.create(getSession(), managerProfile);
            profileDAO.create(getSession(), readerProfile);
            Long appliId = new Long(appli.getId());
            UserDAOImpl userDAO = UserDAOImpl.getInstance();
            Collection col = userDAO.findWhereApplicationAndProfileAndHaveEmails(getSession(), appliId, managerProfile, true);
            assertEquals(0, col.size());
            // on cr�e 3 utilisateurs : 2 managers dont un sans email et 1 lecteur avec mail
            UserBO manager = getComponentFactory().createUser(getSession());
            manager.setDefaultProfile(managerProfile);
            manager.setEmail("manager@Junit.fr");
            // On le d�sabonne de l'envoi des mails
            manager.setUnsubscribed(true);
            // On lui affecte le droit manager sur l'application
            manager.getRights().put(appli, managerProfile);
            UserBO managerWithoutMail = getComponentFactory().createUser(getSession());
            managerWithoutMail.setDefaultProfile(managerProfile);
            // On lui affecte le droit lecteur sur l'application
            managerWithoutMail.getRights().put(appli, managerProfile);
            UserBO reader = getComponentFactory().createUser(getSession());
            reader.setDefaultProfile(managerProfile);
            reader.setEmail("reader@Junit.fr");
            // On le d�sabonne de l'envoi des mails
            reader.setUnsubscribed(true);
            // On lui affecte le droit lecteur sur l'application
            reader.getRights().put(appli, readerProfile);
            
            // On r�cup�re tous les managers de l'application qui ont un mail --> 1 seul
            col = userDAO.findWhereApplicationAndProfileAndHaveEmails(getSession(), appliId, managerProfile, true);
            assertEquals(1, col.size());
            assertEquals("manager@Junit.fr", ((UserBO)col.iterator().next()).getEmail());
            // On r�cup�re tous les managers de l'application qui ont un mail et qui sont abonn� � l'envoi
            // automatique --> 0
            col = userDAO.findWhereApplicationAndProfileAndHaveEmails(getSession(), appliId, managerProfile, false);
            assertEquals(0, col.size());
            // On r�cup�re tous les lecteurs de l'application qui ont un mail --> 1 seul
            col = userDAO.findWhereApplicationAndProfileAndHaveEmails(getSession(), appliId, readerProfile, true);
            assertEquals(1, col.size());
            assertEquals("reader@Junit.fr", ((UserBO)col.iterator().next()).getEmail());
            // On r�cup�re tous les lecteurs de l'application qui ont un mail et qui sont abonn� � l'envoi
            // automatique --> 0
            col = userDAO.findWhereApplicationAndProfileAndHaveEmails(getSession(), appliId, readerProfile, false);
            assertEquals(0, col.size());
        } catch (Exception e) {
            e.printStackTrace();
            fail("unexpected exception");
        }
        
    }
}