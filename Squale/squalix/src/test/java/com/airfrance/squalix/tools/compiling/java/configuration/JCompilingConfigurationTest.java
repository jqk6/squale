package com.airfrance.squalix.tools.compiling.java.configuration;

import com.airfrance.squalecommon.SqualeTestCase;

/**
 *
 */
public class JCompilingConfigurationTest
    extends SqualeTestCase
{

    /**
     * Test du parsing des param�tres de configuration de compilation
     */
    public void testJCompilingConfiguration()
    {
        try
        {
            JCompilingConfiguration conf = new JCompilingConfiguration();
            // Les param�tres sont stock�s en dur dans le fichier
            // config/compiling-config.xml
            assertEquals( conf.getClasspathSeparator(), ";" );
            assertEquals( conf.getDestDir(), "bin/" );
            assertEquals( conf.getJDKDefaultVersion(), "1_4" );
            assertEquals( conf.getRequiredMemory(), "1024m" );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "Unexpected exception" );
        }
    }
}
