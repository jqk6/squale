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
package org.squale.squalix;

import org.squale.squalix.core.PartitionRotationTest;
import org.squale.squalix.tools.ckjm.CkjmTaskTest;
import org.squale.squalix.tools.clearcase.ClearCaseConfigurationTest;
import org.squale.squalix.tools.compiling.java.JavaMockCompilingTaskTest;
import org.squale.squalix.tools.compiling.java.configuration.JCompilingConfigurationTest;
import org.squale.squalix.tools.compiling.java.parser.rsa.JRSAWebSettingsParserTest;
import org.squale.squalix.tools.computing.project.ComputeResultTaskTest;
import org.squale.squalix.tools.cpptest.CppTestAllTests;
import org.squale.squalix.tools.jdepend.JDependTaskTest;
import org.squale.squalix.tools.jspvolumetry.JSPVolumetryConfigurationTest;
import org.squale.squalix.tools.macker.MackerAllTests;
import org.squale.squalix.tools.pmd.PmdTaskTest;
import org.squale.squalix.tools.sourcecodeanalyser.SourceCodeAnalyserTaskTest;
import org.squale.squalix.util.file.FileUtilAllTests;
import org.squale.squalix.util.parser.ParserAllTests;
import org.squale.squalix.util.process.ProcessManagerTest;
import org.squale.squalix.util.repository.ComponentRepositoryTest;
import org.squale.squalix.util.stoptime.StopTimeHelperTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests de Squalix
 */
public class AllSqualixTests
{

    /**
     * @return la suite de tests
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite( "Test for SQUALIX project" );
        // $JUnit-BEGIN$
        /* script de partition */
        suite.addTest( new TestSuite( PartitionRotationTest.class ) );
        /* checkstyle */
        suite.addTest( org.squale.squalix.tools.checkstyle.AllTests.suite() );
        /* ckjm */
        suite.addTest( new TestSuite( CkjmTaskTest.class ) );
        /* clearcase (il manque un test) */
        suite.addTest( new TestSuite( ClearCaseConfigurationTest.class ) );
        /* Compilation java --> � compl�ter lorsque DINB aura install� le 1.5 */
        suite.addTest( new TestSuite( JCompilingConfigurationTest.class ) );
        suite.addTest( org.squale.squalix.tools.compiling.java.parser.configuration.AllTests.suite() );
        suite.addTest( org.squale.squalix.tools.compiling.java.parser.impl.AllTests.suite() );
        // RSA
        suite.addTest( new TestSuite( JRSAWebSettingsParserTest.class ) );
        // Mock
        suite.addTest( new TestSuite( JavaMockCompilingTaskTest.class ) );
        /* Compilation JSP */
        // suite.addTest(new TestSuite(JWSADJspTomcatCompilerTest.class));
        /* Calcul des r�sultats */
        suite.addTest( new TestSuite( ComputeResultTaskTest.class ) );
        /* cppTest */
        suite.addTest( CppTestAllTests.suite() );
        /* JDepend */
        suite.addTest( new TestSuite( JDependTaskTest.class ) );
        /* volum�trie des JSPs */
        suite.addTest( new TestSuite( JSPVolumetryConfigurationTest.class ) );
        /* macker */
        suite.addTest( MackerAllTests.suite() );
        /* mccabe */
        suite.addTest( org.squale.squalix.tools.mccabe.AllTests.suite() );
        /* pmd */
        suite.addTest( new TestSuite( PmdTaskTest.class ) );
        /* r�cup�rateur de source en local */
        suite.addTest( new TestSuite( SourceCodeAnalyserTaskTest.class ) );
        /* UMLQuality */
        suite.addTest( org.squale.squalix.tools.umlquality.AllTests.suite() );
        /* Utilitaires */
        // CSV
        suite.addTest( org.squale.squalix.util.csv.AllTests.suite() );
        // manipulation des fichiers
        suite.addTest( FileUtilAllTests.suite() );
        // parser
        suite.addTest( ParserAllTests.suite() );
        // process
        suite.addTest( new TestSuite( ProcessManagerTest.class ) );
        // persistance des composants
        suite.addTest( new TestSuite( ComponentRepositoryTest.class ) );
        // arr�t du batch
        suite.addTest( new TestSuite( StopTimeHelperTest.class ) );
        // $JUnit-END$
        return suite;
    }
}
