/*
 * Cr�� le 11 ao�t 06
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.squalix.tools.umlquality;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author E6400802 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class AllTests
{

    /**
     * @return le test g�n�ral pour UMLQuality
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite( "Test for com.airfrance.squalix.tools.umlquality" );
        // $JUnit-BEGIN$
        suite.addTest( new TestSuite( UMLQualityConfigurationTest.class ) );
        suite.addTest( new TestSuite( UMLQualityPersistorTest.class ) );
        suite.addTest( new TestSuite( UMLQualityTaskTest.class ) );
        // $JUnit-END$
        return suite;
    }
}
