package com.airfrance.squalecommon.enterpriselayer.facade.pmd;

import java.io.InputStream;
import java.util.Collection;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.jraf.commons.exception.JrafEnterpriseException;
import com.airfrance.squalecommon.SqualeTestCase;
import com.airfrance.squalecommon.daolayer.rulechecking.PmdRuleSetDAOImpl;
import com.airfrance.squalecommon.datatransfertobject.rulechecking.PmdRuleSetDTO;

/**
 * Tests de la facade Pmd
 */
public class PmdFacadeTest extends SqualeTestCase {

    /**
     * Test nominal
     * On importe un fichier dont le contenu est connu, on v�rifie
     * que les donn�es du fichier sont restitu�es
     */
    public void testImportNominal() {
        StringBuffer errors = new StringBuffer();
        // Parsing d'un fichier de test
        InputStream stream = getClass().getClassLoader().getResourceAsStream("data/pmd/pmd.xml");
        PmdRuleSetDTO ruleset = null;
        try {
            ruleset = PmdFacade.importPmdConfig(stream, errors);
        } catch (JrafEnterpriseException e) {
            e.printStackTrace();
            fail("unexpected error");
        }
        assertEquals("pas d'erreur de parsing", 0, errors.length());
        // V�rification des donn�es charg�es
        assertNotNull(ruleset);
        assertEquals("valeur � v�rifier dans le fichier", "default",ruleset.getName());
        assertEquals("valeur � v�rifier dans le fichier", "java", ruleset.getLanguage());
        try {
            assertEquals(1, PmdRuleSetDAOImpl.getInstance().count(getSession()).intValue());
        } catch (JrafDaoException e1) {
            e1.printStackTrace();
            fail("unexpected error");
        }
    }
    
    /**
     * 
     *
     */
    public void testGetAll() {
        try {
            Collection coll = PmdFacade.getAllPmdConfigs();
            assertEquals("no config", 0, coll.size());
            StringBuffer errors = new StringBuffer();
            InputStream stream = getClass().getClassLoader().getResourceAsStream("data/pmd/pmd.xml");
            PmdRuleSetDTO ruleset = null;
            ruleset = PmdFacade.importPmdConfig(stream, errors);
            coll = PmdFacade.getAllPmdConfigs();
            assertEquals("one config", 1, coll.size());
        } catch (JrafEnterpriseException e) {
            e.printStackTrace();
            fail("unexpected exception");
        } 
    }
}
