package com.airfrance.squalix.tools.compiling.java.compiler.impl;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 */
public class AllTests
{

    /**
     * Suite de tests
     * 
     * @return tests
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite( "Test for com.airfrance.squalix.tools.compiling.java.compiler.impl" );
        // $JUnit-BEGIN$
        suite.addTest( new TestSuite( JWSADCompilerImplTest.class ) );
        suite.addTest( new TestSuite( JXMLCompilerImplTest.class ) );
        // $JUnit-END$
        return suite;
    }
}