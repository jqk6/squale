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
package com.airfrance.squalix.tools.cpptest;

import java.io.File;
import java.io.IOException;

import com.airfrance.squalecommon.SqualeTestCase;
import com.airfrance.squalecommon.datatransfertobject.rulechecking.CppTestRuleSetDTO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.MapParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.StringParameterBO;
import com.airfrance.squalix.core.TaskData;
import com.airfrance.squalix.util.file.FileUtility;

/**
 * Test de la t�che CppTest
 */
public class CppTestTaskTest
    extends SqualeTestCase
{

    /**
     * Test de g�n�ration de rapport
     */
    public void testGenerateReport()
    {
        try
        {
            // La m�thode generateReport doit g�n�rer une commande avec
            // quatre param�tres du genre <script> <viewdir> <ruleset> <reportdir>
            CppTestTaskStub task = new CppTestTaskStub();
            // On g�n�re un nom li� � la date pour �viter la duplication des donn�es
            File rootDir = new File( "report" + File.separator + System.currentTimeMillis() );
            assertFalse( "r�pertoire cr�� par la m�thode", rootDir.exists() );
            CppTestWorkSpace workspace = new CppTestWorkSpace( rootDir );
            MapParameterBO cppMap = new MapParameterBO();
            String ruleSetValue = "ruleset";
            CppTestRuleSetDTO ruleset = new CppTestRuleSetDTO();
            ruleset.setCppTestName( ruleSetValue );
            StringParameterBO script = new StringParameterBO();
            String scriptValue = ".classpath";
            script.setValue( scriptValue );
            cppMap.getParameters().put( ParametersConstants.CPPTEST_SCRIPT, script );
            TaskData data = new TaskData();
            task.setData( data );
            String viewValue = new File( "." ).getAbsolutePath();
            data.putData( TaskData.VIEW_PATH, viewValue );
            // Test nominal avec la configuration compl�te
            task.generateReport( workspace, ruleset, cppMap, "cpptest.log" );
            String[] command = task.getCommand();
            final int nbArgs = 5;
            assertEquals( nbArgs, command.length );
            int index = 0;
            // Le premier argument de la commande est le nom du script
            assertTrue( "nom du script attendu", command[index].indexOf( scriptValue ) >= 0 );
            // Le deuxi�me argument de la commande est l'emplacement de la vue
            index++;
            assertTrue( "emplacement de la vue attendu", command[index].indexOf( viewValue ) >= 0 );
            // Le troisi�me argument est le ruleSet
            index++;
            assertTrue( "ruleset attendu", command[index].indexOf( ruleSetValue ) >= 0 );
            // Le quatri�me argument est le nom du projet CppTest
            index++;
            assertTrue( "r�pertoire de g�n�ration du rapport attendu",
                        command[index].indexOf( workspace.getProjectFile().getAbsolutePath() ) >= 0 );
            // Le cinqui�me argument est le r�pertoire de g�n�ration du rapport
            index++;
            assertTrue( "r�pertoire de g�n�ration du rapport attendu",
                        command[index].indexOf( workspace.getReportDirectory().getAbsolutePath() ) >= 0 );
            // Suppression du r�pertoire
            FileUtility.deleteRecursively( rootDir );
            assertFalse( "r�pertoire cr�� par la m�thode", rootDir.exists() );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }

    /**
     * Stub de classe CppTestTask Le stub permet de r�cup�rer la commande lanc�e
     */
    class CppTestTaskStub
        extends CppTestTask
    {
        /** Commande */
        private String[] mCommand;

        /** Le fichier de log */
        private String mLogger;

        /**
         * {@inheritDoc}
         * 
         * @param command
         * @param pLogger
         * @throws IOException
         * @throws InterruptedException {@inheritDoc}
         * @see com.airfrance.squalix.tools.cpptest.CppTestTask#executeCommand(java.lang.String[], java.lang.String)
         */
        protected void executeCommand( String[] pCommand, String pLogger )
            throws IOException, InterruptedException
        {
            mCommand = pCommand;
            mLogger = pLogger;
        }

        /**
         * @return commande
         */
        public String[] getCommand()
        {
            return mCommand;
        }
    }

}
