package com.airfrance.squalecommon.daolayer.profile;

import com.airfrance.jraf.helper.PersistenceHelper;
import com.airfrance.jraf.provider.persistence.hibernate.facade.FacadeHelper;
import com.airfrance.jraf.spi.persistence.IPersistenceProvider;
import com.airfrance.jraf.spi.persistence.ISession;
import com.airfrance.squalecommon.SqualeTestCase;
import com.airfrance.squalecommon.enterpriselayer.businessobject.profile.ProfileBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.profile.UserBO;

/**
 * @author M400843 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class ProfileDAOImplTest
    extends SqualeTestCase
{
    /** provider de persistence */
    private static IPersistenceProvider PERSISTENT_PROVIDER = PersistenceHelper.getPersistenceProvider();

    /**
     * @see ProfileDAOImpl#loadByKey(ISession, String)
     */
    public void testloadByKey()
    {
        try
        {
            ISession session = PERSISTENT_PROVIDER.getSession();
            session.beginTransaction();
            UserBO user = getComponentFactory().createUser( session );
            String existant_key = ProfileBO.ADMIN_PROFILE_NAME;
            String nonExistant_key = "je_n_existe_pas";
            ProfileDAOImpl profileDAO = ProfileDAOImpl.getInstance();

            assertNotNull( profileDAO.loadByKey( session, existant_key ) );
            assertNull( profileDAO.loadByKey( session, nonExistant_key ) );
            FacadeHelper.closeSession( session, "" );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }
}
