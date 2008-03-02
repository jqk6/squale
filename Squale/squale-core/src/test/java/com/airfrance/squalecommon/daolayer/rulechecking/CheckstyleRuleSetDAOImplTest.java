package com.airfrance.squalecommon.daolayer.rulechecking;

import java.util.Collection;

import com.airfrance.squalecommon.SqualeTestCase;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking.checkstyle.CheckstyleRuleSetBO;

/**
 * @author Henix
 */
/**
 * Test de la couche DAO pour checkstyle
 */

public class CheckstyleRuleSetDAOImplTest
    extends SqualeTestCase
{

    /**
     * Test la cr�ation d'une vesrion
     */
    public void testCreateCheckstyleRuleSet()
    {

        try
        {

            CheckstyleRuleSetDAOImpl daoImpl = CheckstyleRuleSetDAOImpl.getInstance();

            // instanciation de la premi�re VersionBO
            CheckstyleRuleSetBO v1 = new CheckstyleRuleSetBO();
            v1.setValue( "n'import quoi".getBytes() );
            v1 = daoImpl.createCheckstyleRuleSet( getSession(), v1 );

            // instanciationde la di�re VersionBO
            CheckstyleRuleSetBO v2 = new CheckstyleRuleSetBO();
            v2.setValue( "n'import quoi".getBytes() );
            v2 = daoImpl.createCheckstyleRuleSet( getSession(), v2 );
            assertTrue( "premi�re version obsol�te", v1.getDateOfUpdate().before( v2.getDateOfUpdate() ) );

            daoImpl.remove( getSession(), v1 );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "Exception inattendue" );
        }
    }

    /**
     * Test findAll
     */
    public void testFindAll()
    {

        try
        {

            CheckstyleRuleSetDAOImpl daoImpl = CheckstyleRuleSetDAOImpl.getInstance();

            // instanciation de la premi�re VersionBO
            CheckstyleRuleSetBO v1 = new CheckstyleRuleSetBO();
            v1.setValue( "n'import quoi".getBytes() );
            v1 = daoImpl.createCheckstyleRuleSet( getSession(), v1 );

            // instanciationde la di�re VersionBO
            CheckstyleRuleSetBO v2 = new CheckstyleRuleSetBO();
            v2.setValue( "n'import quoi".getBytes() );
            v2 = daoImpl.createCheckstyleRuleSet( getSession(), v2 );

            Collection col = daoImpl.findAll( getSession() );

            assertEquals( "liste des versions", 2, col.size() );

            daoImpl.remove( getSession(), v2 );
            daoImpl.remove( getSession(), v1 );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "Exception inattendue" );
        }

    }

    /**
     * Test load
     */

    public void testLoad()
    {
        try
        {

            CheckstyleRuleSetDAOImpl daoImpl = CheckstyleRuleSetDAOImpl.getInstance();

            // instanciation de la premi�re VersionBO
            CheckstyleRuleSetBO v1 = new CheckstyleRuleSetBO();
            v1.setValue( "n'import quoi".getBytes() );
            v1 = daoImpl.createCheckstyleRuleSet( getSession(), v1 );

            daoImpl.remove( getSession(), v1 );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "Exception inattendue" );
        }

    }

    /**
     * Test GetLastVersion
     */
    public void testGetLastVersion()
    {
        try
        {

            CheckstyleRuleSetDAOImpl daoImpl = CheckstyleRuleSetDAOImpl.getInstance();

            // instanciation de la premi�re CheckstyleRuleSetBO
            CheckstyleRuleSetBO v1 = new CheckstyleRuleSetBO();
            v1.setValue( "n'import quoi".getBytes() );
            v1.setName( "name" );
            v1 = daoImpl.createCheckstyleRuleSet( getSession(), v1 );

            // instanciationde la deuxi�me CheckstyleRuleSetBO
            CheckstyleRuleSetBO v2 = new CheckstyleRuleSetBO();
            v2.setValue( "n'import quoi".getBytes() );
            v2.setName( v1.getName() );
            v2 = daoImpl.createCheckstyleRuleSet( getSession(), v2 );

            CheckstyleRuleSetBO lastVer = daoImpl.getLastVersion( getSession(), v1.getName() );
            assertEquals( v2.getId(), lastVer.getId() );
            daoImpl.remove( getSession(), v1 );
            daoImpl.remove( getSession(), v2 );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "Exception inattendue" );
        }

    }

}
