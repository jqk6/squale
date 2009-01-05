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
package com.airfrance.squalecommon.enterpriselayer.facade.cpptest;

import java.io.InputStream;

import com.airfrance.squalecommon.SqualeTestCase;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking.RuleBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking.cpptest.CppTestRuleSetBO;

/**
 * Test de l'importation de fichier de configuration
 */
public class CppTestConfigParserTest
    extends SqualeTestCase
{

    /**
     * Test nominal On importe un fichier dont le contenu est connu, on v�rifie que les donn�es du fichier sont
     * restitu�es
     */
    public void testImportNominal()
    {
        StringBuffer errors = new StringBuffer();
        CppTestConfigParser parser = new CppTestConfigParser();
        // Parsing d'un fichier de test
        InputStream stream = getClass().getClassLoader().getResourceAsStream( "data/cpptest/cpptest.xml" );
        CppTestRuleSetBO ruleset = parser.parseFile( stream, errors );
        assertEquals( "pas d'erreur de parsing", 0, errors.length() );
        // V�rification des donn�es charg�es
        assertNotNull( ruleset );
        assertEquals( 1, ruleset.getRules().size() );
        assertEquals( "valeur � v�rifier dans le fichier", "default", ruleset.getName() );
        assertEquals( "valeur � v�rifier dans le fichier", "builtin://MustHaveRules", ruleset.getCppTestName() );
        // V�rification des attributs de la r�gle
        RuleBO rule = (RuleBO) ruleset.getRules().values().iterator().next();
        assertEquals( "valeur � v�rifier dans le fichier", "coding", rule.getCategory() );
        assertEquals( "valeur � v�rifier dans le fichier", "DeleteNonPointer.rule", rule.getCode() );
        assertEquals( "valeur � v�rifier dans le fichier", "error", rule.getSeverity() );
    }

    /**
     * Importation d'un mauvais fichier XML
     */
    public void testImportBadFile()
    {
        StringBuffer errors = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream( "config/hibernate.cfg.xml" );
        try
        {
            CppTestConfigParser parser = new CppTestConfigParser();
            CppTestRuleSetBO ruleset = parser.parseFile( stream, errors );
            assertEquals( 0, ruleset.getRules().size() );
            assertTrue( errors.length() > 0 );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }
}
