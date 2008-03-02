package com.airfrance.squalix.tools.umlquality;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.airfrance.squalecommon.SqualeTestCase;
import com.airfrance.squalix.core.exception.ConfigurationException;

/**
 * Test de la configuration UMLQuality
 */
public class UMLQualityConfigurationTest
    extends SqualeTestCase
{

    /**
     * Test de chargement
     */
    public void testParse()
    {
        UMLQualityConfiguration cfg = new UMLQualityConfiguration();
        InputStream stream;
        try
        {
            stream = new FileInputStream( new File( "../squalix/config/umlquality-config.xml" ) );
            cfg.parse( stream );
            // V�rification des donn�es lues dans le fichier
            assertEquals( "/app/SQUALE/squalix/umlquality/report", cfg.getReportDirectory() );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }

    /**
     * Chargement d'un fichier erron�
     */
    public void testParseBadFile()
    {
        UMLQualityConfiguration cfg = new UMLQualityConfiguration();
        InputStream stream;
        try
        {
            stream = new FileInputStream( new File( "build.properties" ) );
            cfg.parse( stream );
            // V�rification des donn�es lues dans le fichier
            fail( "unexpected exception" );
        }
        catch ( Exception e )
        {
            assertTrue( "expected exception", true );
            assertTrue( "Exception lanc�e par la m�thode parse", e instanceof ConfigurationException );
        }
    }

}
