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
package org.squale.squalix.util.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tools.ant.DirectoryScanner;

import org.squale.squalecommon.SqualeTestCase;

/**
 * Test du filtre avec r�pertoires exclus
 */
public class ExtensionIncludedFileFilterTest
    extends SqualeTestCase
{

    /**
     * Test du filtre
     */
    public void testFilter()
    {
        // Construction des r�pertories inclus

        DirectoryScanner scanner = new DirectoryScanner();
        // sensibilit� � la casse
        scanner.setCaseSensitive( true );
        String[] excludesTab = { "**/compiler/**" };
        scanner.setExcludes( excludesTab );
        File src = new File( "src" );
        scanner.setBasedir( src );
        scanner.scan();
        String[] t = scanner.getIncludedFiles();
        List includedFileNames = new ArrayList( Arrays.asList( scanner.getIncludedFiles() ) );

        ExtensionsIncludedFileFilter filter =
            new ExtensionsIncludedFileFilter( new String[] { ".java" }, includedFileNames );
        filter.setBaseDir( src );

        // Test de fichier autoris�s
        File f = new File( "src/org\\squale/squalix\\core", "PurgeTest.java" ); // M�lange de / et \ volontaire
        assertTrue( "File ok", filter.accept( f ) );
        f = new File( "src/org/squale/squalix/tools/checkstyle", "AllTests.java" ); // M�lange de / et \ volontaire
        assertTrue( "File ok", filter.accept( f ) );

        // Test de fichiers exclus
        f = new File( "src/org/squale/squalix/tools/compiling/java/compiler/impl", "AllTests.jsp" ); // M�lange de /
                                                                                                        // et \
                                                                                                        // volontaire
        assertFalse( "File rejected", filter.accept( f ) );
        f = new File( "src\\org/squale/squalix\\tools/compiling/java/compiler/impl", "AllTests.java" ); // M�lange
                                                                                                            // de / et \
                                                                                                            // volontaire
        assertFalse( "File rejected", filter.accept( f ) );
    }
}
