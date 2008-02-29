package com.airfrance.squalecommon.enterpriselayer.applicationcomponent;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.airfrance.squalecommon.enterpriselayer.applicationcomponent.administration.ApplicationAdminApplicationComponentAccessTest;
import com.airfrance.squalecommon.enterpriselayer.applicationcomponent.administration.LoginApplicationComponentAccessTest;
import com.airfrance.squalecommon.enterpriselayer.applicationcomponent.administration.PurgeApplicationComponentAccessTest;
import com.airfrance.squalecommon.enterpriselayer.applicationcomponent.administration.ValidationApplicationComponentAccessTest;
import com.airfrance.squalecommon.enterpriselayer.applicationcomponent.display.ComponentApplicationComponentAccessTest;
import com.airfrance.squalecommon.enterpriselayer.applicationcomponent.display.ErrorApplicationComponentAccessTest;
import com.airfrance.squalecommon.enterpriselayer.applicationcomponent.display.ResultsApplicationComponentAccessTest;

/**
 * @author M400841
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class ApplicationComponentsTests {

    /**
     * Suite de tests JUnits pour les composants de niveau application
     * @return la suite de tests
     */
    public static Test suite() {
        TestSuite suite =
            new TestSuite("Test for com.airfrance.squalecommon.enterpriselayer.applicationcomponent");
        //$JUnit-BEGIN$
        suite.addTest(new TestSuite(LoginApplicationComponentAccessTest.class));
        suite.addTest(
            new TestSuite(ApplicationAdminApplicationComponentAccessTest.class));
        suite.addTest(new TestSuite(PurgeApplicationComponentAccessTest.class));
        suite.addTest(
            new TestSuite(ValidationApplicationComponentAccessTest.class));
        suite.addTest(
            new TestSuite(ComponentApplicationComponentAccessTest.class));
        suite.addTest(new TestSuite(ErrorApplicationComponentAccessTest.class));
        suite.addTest(
            new TestSuite(ResultsApplicationComponentAccessTest.class));
        //$JUnit-END$
        return suite;
    }
}
