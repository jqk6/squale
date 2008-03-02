/*
 * Cr�� le 12 juil. 06
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.squalecommon.enterpriselayer.facade.checkstyle.xml;

import java.io.InputStream;

import java.util.Map;
import java.util.Set;
import com.airfrance.squalecommon.SqualeTestCase;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking.checkstyle.CheckstyleModuleBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking.checkstyle.CheckstyleRuleBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking.checkstyle.CheckstyleRuleSetBO;

/**
 * Test d'importation au format XML du fichier de configuration checkstyle Le test v�rifie essentiellement que la
 * mapping est correct au niveau du digester en s'assurant que tous les attributs pr�sents dans le fichier XML sont bien
 * mapp�s dans le classes m�tier
 */
public class CheckstyleImportTest
    extends SqualeTestCase
{

    /**
     * Parsing nominale d'un fichier de configuration checkstyle
     */
    public void testParseFile()
    {
        try
        {
            InputStream stream = getClass().getClassLoader().getResourceAsStream( "config/AFcheckStyle_parsing.xml" );
            StringBuffer errors = new StringBuffer();
            // parsing du contenue du fichier
            CheckstyleImport importch = new CheckstyleImport();
            CheckstyleRuleSetBO versionRes = importch.importCheckstyle( stream, errors );

            assertTrue( "Erreur pendant le parsing", errors.length() == 0 );
            assertNotNull( "echec de parsing", versionRes );
            assertEquals( "Version", versionRes.getName(), "default" );

            Map regles = versionRes.getRules();
            assertEquals( "nb r�gle", 23, regles.size() );
            CheckstyleRuleBO rule = (CheckstyleRuleBO) regles.get( "COD01" );

            assertNotNull( "rule null", rule );
            assertNotNull( "rule null", rule.getSeverity() );
            assertEquals( "severity", rule.getSeverity(), "warning" );
            assertEquals( "practise", rule.getCategory(), "programmingstandard" );
            Set moduleSet = rule.getModules();
            assertEquals( "nb r�gle", 4, moduleSet.size() );
            CheckstyleModuleBO module = new CheckstyleModuleBO();
            module.setName( "com.puppycrawl.tools.checkstyle.checks.imports.AvoidStarImportCheck" );
            assertTrue( "module contains", moduleSet.contains( module ) );

            module.setName( "com.puppycrawl.tools.checkstyle.checks.imports.IllegalImportCheck" );
            assertTrue( "module contains", moduleSet.contains( module ) );

            module.setName( "com.puppycrawl.tools.checkstyle.checks.imports.RedundantImportCheck" );
            assertTrue( "module contains", moduleSet.contains( module ) );

            module.setName( "com.puppycrawl.tools.checkstyle.checks.imports.UnusedImportsCheck" );
            assertTrue( "module contains", moduleSet.contains( module ) );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "Exception inattendue" );
        }
    }

    /**
     * Test d'importation avec mauvais regroupement Le fichier de test comporte une incoh�rence sur la s�v�rit� entre un
     * module checkstyle et la r�gle checkstyle
     */
    public void testImportWithBadRegrouping()
    {
        try
        {
            InputStream stream =
                getClass().getClassLoader().getResourceAsStream(
                                                                 "data/checkstyle/AFcheckStyle_new_parsing_bad_regrouping.xml" );
            StringBuffer errors = new StringBuffer();
            // parsing du contenue du fichier
            CheckstyleImport importch = new CheckstyleImport();
            CheckstyleRuleSetBO versionRes = importch.importCheckstyle( stream, errors );
            assertTrue( "Erreurs attendues", errors.length() > 0 );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "Exception inattendue" );
        }
    }
}
