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
package org.squale.squalecommon.daolayer.rulechecking;

import java.util.Collection;

import org.squale.squalecommon.SqualeTestCase;
import org.squale.squalecommon.enterpriselayer.businessobject.rulechecking.checkstyle.CheckstyleRuleSetBO;

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

}
