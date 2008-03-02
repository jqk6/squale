package com.airfrance.squalecommon.enterpriselayer.facade.pmd;

import java.io.InputStream;

import com.airfrance.squalecommon.SqualeTestCase;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking.RuleBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking.pmd.PmdRuleSetBO;

/**
 * Test du parser de configuration PMD
 */
public class PmdConfigParserTest
    extends SqualeTestCase
{

    /**
     * Test nominal On importe un fichier dont le contenu est connu, on v�rifie que les donn�es du fichier sont
     * restitu�es
     */
    public void testImportNominal()
    {
        StringBuffer errors = new StringBuffer();
        PmdConfigParser parser = new PmdConfigParser();
        // Parsing d'un fichier de test
        InputStream stream = getClass().getClassLoader().getResourceAsStream( "data/pmd/pmd.xml" );
        PmdRuleSetBO ruleset = parser.parseFile( stream, errors );
        assertEquals( "pas d'erreur de parsing", 0, errors.length() );
        // V�rification des donn�es charg�es
        assertNotNull( ruleset );
        assertEquals( "valeur � v�rifier dans le fichier", "default", ruleset.getName() );
        assertEquals( "valeur � v�rifier dans le fichier", "java", ruleset.getLanguage() );
        // V�rification du nombre de r�gles lues
        assertEquals( "valeur � v�rifier dans le fichier", 2, ruleset.getRules().size() );
        // V�rification des attributs de la r�gle CyclomaticComplexity
        RuleBO rule = (RuleBO) ruleset.getRules().get( "CyclomaticComplexity" );
        assertEquals( "valeur � v�rifier dans le fichier", "coding", rule.getCategory() );
        assertEquals( "valeur � v�rifier dans le fichier", "CyclomaticComplexity", rule.getCode() );
        assertEquals( "valeur � v�rifier dans le fichier", "error", rule.getSeverity() );
        // V�rification des attributs de la r�gle ExcessiveMethodLength
        rule = (RuleBO) ruleset.getRules().get( "ExcessiveMethodLength" );
        assertEquals( "valeur � v�rifier dans le fichier", "format", rule.getCategory() );
        assertEquals( "valeur � v�rifier dans le fichier", "ExcessiveMethodLength", rule.getCode() );
        assertEquals( "valeur � v�rifier dans le fichier", "warning", rule.getSeverity() );
    }
}
