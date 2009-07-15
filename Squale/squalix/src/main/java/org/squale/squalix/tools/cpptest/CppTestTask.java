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
package org.squale.squalix.tools.cpptest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.squale.jraf.commons.exception.JrafEnterpriseException;
import org.squale.squalecommon.datatransfertobject.rulechecking.CppTestRuleSetDTO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.MapParameterBO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.StringParameterBO;
import org.squale.squalecommon.enterpriselayer.facade.cpptest.CppTestFacade;
import org.squale.squalix.core.AbstractTask;
import org.squale.squalix.core.TaskData;
import org.squale.squalix.core.TaskException;
import org.squale.squalix.core.exception.ConfigurationException;
import org.squale.squalix.util.file.FileUtility;
import org.squale.squalix.util.process.ProcessErrorHandler;
import org.squale.squalix.util.process.ProcessManager;
import org.squale.squalix.util.process.ProcessOutputHandler;

/**
 * T�che CppTest Cette t�che r�alise l'interface avec l'outil CppTest pour collecter les donn�es concernant les r�gles
 * de codage non respect�es. Les donn�es de configuration indiquent la liste des scripts de compilation � lancer
 */
public class CppTestTask
    extends AbstractTask
    implements ProcessErrorHandler, ProcessOutputHandler
{
    /**
     * Logger
     */
    private static final Log LOGGER = LogFactory.getLog( CppTestTask.class );

    /** Filtre d'erreur */
    private ErrorFilter mErrorFilter;

    /**
     * Constructeur
     */
    public CppTestTask()
    {
        mName = "CppTestTask";
        mErrorFilter = new ErrorFilter();
    }

    /**
     * Ex�cution de la t�che CppTest
     * 
     * @throws TaskException en cas d'�chec
     */
    public void execute()
        throws TaskException
    {
        try
        {
            // Lecture de la configuration du projet
            CppTestConfiguration conf = new CppTestConfiguration();
            conf.parse( new FileInputStream( "config/cpptest-config.xml" ) );
            // Les donn�es du projet doivent fournir le nom du fichier
            // de configuration
            MapParameterBO cppTestMap = (MapParameterBO) getProject().getParameter( ParametersConstants.CPPTEST );
            if ( cppTestMap == null )
            {
                String message =
                    CppTestMessages.getString( "exception.variable.not_found", ParametersConstants.CPPTEST );
                LOGGER.error( message );
                // Renvoi d'une exception de configuration
                throw new ConfigurationException( message );
            }
            // Obtention du ruleset
            CppTestRuleSetDTO ruleset = getRuleSet( cppTestMap );

            // Espace de travail pour CppTest
            CppTestWorkSpace workspace =
                new CppTestWorkSpace( new File( conf.getReportDirectory(), "project" + getProjectId() + "_audit"
                    + getAuditId() ) );

            // Lancement de la commande externe de g�n�ration du rapport
            generateReport( workspace, ruleset, cppTestMap, conf.getLogger() );

            // Exploitation des donn�es du rapport
            parseReport( workspace, ruleset );

            // positionne les donn�es sur la taille du file System
            affectFileSystemSize( conf.getReportDirectory(), true );

            // Nettoyage du workspace
            workspace.cleanup();
        }
        catch ( Exception e )
        {
            throw new TaskException( e );
        }
    }

    /**
     * G�n�ration du rapport XML Le rapport XML est g�n�r� en une seule �tape par le lancement d'une commande cpptest
     * qui r�alise l acr�ation du projet et le lancement de l'audit sur les r�gles
     * 
     * @param pWorkSpace espace de travail CppTest
     * @param pCppTestMap param�tres de la t�che
     * @param pRuleSet ruleset utilis�
     * @param pLogger le fichier de log
     * @throws Exception si erreur
     */
    protected void generateReport( CppTestWorkSpace pWorkSpace, CppTestRuleSetDTO pRuleSet, MapParameterBO pCppTestMap,
                                   String pLogger )
        throws Exception
    {
        // R�cup�ration des informations de configuration du projet
        String ruleSet = pRuleSet.getCppTestName();
        File script;
        StringParameterBO scriptConf =
            (StringParameterBO) pCppTestMap.getParameters().get( ParametersConstants.CPPTEST_SCRIPT );
        if ( scriptConf == null )
        {
            String message =
                CppTestMessages.getString( "exception.variable.not_found", ParametersConstants.CPPTEST_SCRIPT );
            LOGGER.error( message );
            // Renvoi d'une exception de configuration
            throw new ConfigurationException( message );
        }
        else
        {
            script = new File( scriptConf.getValue() );
        }
        // D�termination du script � ex�cuter
        script = computeScriptLocation( script );
        // Lancement de la commande
        String viewPath = (String) getData().getData( TaskData.VIEW_PATH );
        String[] command =
            { script.getAbsolutePath(), viewPath, ruleSet, pWorkSpace.getProjectFile().getAbsolutePath(),
                pWorkSpace.getReportDirectory().getAbsolutePath() };
        LOGGER.info( CppTestMessages.getString( "report.command", command ) );
        executeCommand( command, pLogger );
    }

    /**
     * Obtention du DTO du ruleset
     * 
     * @param pCppTestMap param�tres de la t�che
     * @return nom du ruleset CppTest
     * @throws ConfigurationException si erreur
     * @throws JrafEnterpriseException si erreur
     */
    private CppTestRuleSetDTO getRuleSet( MapParameterBO pCppTestMap )
        throws ConfigurationException, JrafEnterpriseException
    {
        // R�cup�ration des informations de configuration du projet
        StringParameterBO ruleSetName =
            (StringParameterBO) pCppTestMap.getParameters().get( ParametersConstants.CPPTEST_RULESET_NAME );
        if ( ruleSetName == null )
        {
            String message =
                CppTestMessages.getString( "exception.variable.not_found", ParametersConstants.CPPTEST_RULESET_NAME );
            LOGGER.error( message );
            // Renvoi d'une exception de configuration
            throw new ConfigurationException( message );
        }
        // Obtention du ruleset dans la base
        CppTestRuleSetDTO dto = CppTestFacade.getCppTestConfiguration( ruleSetName.getValue() );
        if ( dto == null )
        {
            String message = CppTestMessages.getString( "exception.ruleset.not_found", ruleSetName.getValue() );
            LOGGER.error( message );
            // Renvoi d'une exception de configuration
            throw new ConfigurationException( message );
        }
        return dto;
    }

    /**
     * Localisation du script
     * 
     * @param pScript script � tester
     * @return script � lancer
     * @throws ConfigurationException si erreur
     */
    private File computeScriptLocation( File pScript )
        throws ConfigurationException
    {
        File result;
        // Si le script a un nom absolue et existe, on prend celui-ci
        if ( pScript.isAbsolute() && pScript.exists() )
        {
            result = pScript;
        }
        else
        {
            // Le script est suppos� �tre relatif � la vue
            String viewPath = (String) getData().getData( TaskData.VIEW_PATH );
            if ( viewPath == null )
            {
                String message = CppTestMessages.getString( "exception.variable.not_found", TaskData.VIEW_PATH );
                LOGGER.error( message );
                // Renvoi d'une exception de configuration
                throw new ConfigurationException( message );
            }
            result = new File( viewPath, pScript.getPath() );
            if ( !result.exists() )
            {
                String message = CppTestMessages.getString( "error.script.not_found", result.getAbsolutePath() );
                LOGGER.error( message );
                // Renvoi d'une exception de configuration
                throw new ConfigurationException( message );
            }
        }
        return result;
    }

    /**
     * Ex�cution de la commande
     * 
     * @param command commande � lancer
     * @param pLogger le fichier de log
     * @throws IOException si erreur
     * @throws InterruptedException si erreur
     */
    protected void executeCommand( String[] command, String pLogger )
        throws IOException, InterruptedException
    {
        // la sortie va �tre redirig�e vers un fichier de log et non vers squalix.txt
        ProcessManager mgr = new ProcessManager( command, null, null, FileUtility.getLogFile( pLogger ) );
        mgr.setOutputHandler( this );
        int result = mgr.startProcess( this );
        if ( result != 0 )
        {
            String message = CppTestMessages.getString( "exception.command", result + "" );
            LOGGER.error( message );
            throw new IOException( message );
        }
    }

    /**
     * Lecture du rapport CppTest Le rapport est lu dans le r�pertoire contenant celui-ci, puis les donn�es associ�es au
     * rapport sont enregistr�es dans la base
     * 
     * @param pWorkSpace espace de travail CppTest
     * @param pRuleSet ruleset
     * @throws Exception si erreur
     */
    protected void parseReport( CppTestWorkSpace pWorkSpace, CppTestRuleSetDTO pRuleSet )
        throws Exception
    {
        // Recherche des rapports
        Collection files = pWorkSpace.getReportFiles();
        if ( files.size() == 0 )
        {
            String message = CppTestMessages.getString( "error.noreport", pWorkSpace.getReportDirectory() );
            LOGGER.error( message );
            // Aucun rapport n'a �t� g�n�r�, lev�e d'une exception
            throw new Exception( message );
        }
        // Traitement des rapports
        Iterator it = files.iterator();
        ReportParser rp = new ReportParser();
        HashMap rules = new HashMap();
        while ( it.hasNext() )
        {
            File file = (File) it.next();
            Map result = rp.parse( new FileInputStream( file ), (String) getData().getData( TaskData.VIEW_PATH ) );
            // On fusionne les r�sultats avec ceux obtenus jusqu'alors
            mergeResults( rules, result );
        }

        // Sauvegarde des donn�es dans la base
        CppTestPersistor persistor = new CppTestPersistor();
        persistor.storeResults( getSession(), getProject(), getAudit(), rules, pRuleSet );
    }

    /**
     * Fusion des r�sultats dans la MAP
     * 
     * @param pResult r�sultat de la fusion
     * @param pMerged donn�es � fusionner
     */
    protected void mergeResults( Map pResult, Map pMerged )
    {
        Iterator entries = pMerged.entrySet().iterator();
        while ( entries.hasNext() )
        {
            Map.Entry entry = (Entry) entries.next();
            Collection val = (Collection) pResult.get( entry.getKey() );
            if ( val == null )
            {
                val = (Collection) entry.getValue();
                pResult.put( entry.getKey(), val );
            }
            else
            {
                val.addAll( (Collection) entry.getValue() );
            }
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.squale.squalix.util.process.ProcessErrorHandler#processError(java.lang.String)
     */
    public void processError( String pErrorMessage )
    {
        initError( pErrorMessage );
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.squale.squalix.util.process.ProcessOutputHandler#processOutput(java.lang.String)
     */
    public void processOutput( String pOutputLine )
    {
        // CPPTEST ecrit ses erreurs sur le flux stdout !
        // Le script est suppos� �crit comme ayant le param�tre -Zoe
        // cens� limiter les traces
        mErrorFilter.processLine( pOutputLine );
        if ( mErrorFilter.errorAvailable() )
        {
            initError( mErrorFilter.consumeError() );
        }
    }

}
