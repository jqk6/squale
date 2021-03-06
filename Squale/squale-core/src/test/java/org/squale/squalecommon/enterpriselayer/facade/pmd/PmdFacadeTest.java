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
package org.squale.squalecommon.enterpriselayer.facade.pmd;

import java.io.InputStream;
import java.util.Collection;

import org.squale.jraf.commons.exception.JrafDaoException;
import org.squale.jraf.commons.exception.JrafEnterpriseException;
import org.squale.squalecommon.SqualeTestCase;
import org.squale.squalecommon.daolayer.rulechecking.PmdRuleSetDAOImpl;
import org.squale.squalecommon.datatransfertobject.rulechecking.PmdRuleSetDTO;

/**
 * Tests de la facade Pmd
 */
public class PmdFacadeTest
    extends SqualeTestCase
{

    /**
     * Test nominal On importe un fichier dont le contenu est connu, on v�rifie que les donn�es du fichier sont
     * restitu�es
     */
    public void testImportNominal()
    {
        StringBuffer errors = new StringBuffer();
        // Parsing d'un fichier de test
        InputStream stream = getClass().getClassLoader().getResourceAsStream( "data/pmd/pmd.xml" );
        PmdRuleSetDTO ruleset = null;
        try
        {
            ruleset = PmdFacade.importPmdConfig( stream, errors );
        }
        catch ( JrafEnterpriseException e )
        {
            e.printStackTrace();
            fail( "unexpected error" );
        }
        assertEquals( "pas d'erreur de parsing", 0, errors.length() );
        // V�rification des donn�es charg�es
        assertNotNull( ruleset );
        assertEquals( "valeur � v�rifier dans le fichier", "default", ruleset.getName() );
        assertEquals( "valeur � v�rifier dans le fichier", "java", ruleset.getLanguage() );
        try
        {
            assertEquals( 1, PmdRuleSetDAOImpl.getInstance().count( getSession() ).intValue() );
        }
        catch ( JrafDaoException e1 )
        {
            e1.printStackTrace();
            fail( "unexpected error" );
        }
    }

    /**
     * 
     *
     */
    public void testGetAll()
    {
        try
        {
            Collection coll = PmdFacade.getAllPmdConfigs();
            assertEquals( "no config", 0, coll.size() );
            StringBuffer errors = new StringBuffer();
            InputStream stream = getClass().getClassLoader().getResourceAsStream( "data/pmd/pmd.xml" );
            PmdRuleSetDTO ruleset = null;
            ruleset = PmdFacade.importPmdConfig( stream, errors );
            coll = PmdFacade.getAllPmdConfigs();
            assertEquals( "one config", 1, coll.size() );
        }
        catch ( JrafEnterpriseException e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }
}
