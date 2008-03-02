package com.airfrance.squalix.util.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tools.ant.DirectoryScanner;

import com.airfrance.squalecommon.SqualeTestCase;

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
        File f = new File( "src/com\\airfrance/squalix\\core", "PurgeTest.java" ); // M�lange de / et \ volontaire
        assertTrue( "File ok", filter.accept( f ) );
        f = new File( "src/com/airfrance/squalix/tools/checkstyle", "AllTests.java" ); // M�lange de / et \ volontaire
        assertTrue( "File ok", filter.accept( f ) );

        // Test de fichiers exclus
        f = new File( "src/com/airfrance/squalix/tools/compiling/java/compiler/impl", "AllTests.jsp" ); // M�lange de /
                                                                                                        // et \
                                                                                                        // volontaire
        assertFalse( "File rejected", filter.accept( f ) );
        f = new File( "src\\com/airfrance/squalix\\tools/compiling/java/compiler/impl", "AllTests.java" ); // M�lange
                                                                                                            // de / et \
                                                                                                            // volontaire
        assertFalse( "File rejected", filter.accept( f ) );
    }
}
