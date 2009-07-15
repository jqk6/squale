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
package org.squale.squalix.tools.rsm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.ListParameterBO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.MapParameterBO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.StringParameterBO;
import org.squale.squalecommon.enterpriselayer.businessobject.result.ErrorBO;
import org.squale.squalix.core.AbstractTask;
import org.squale.squalix.core.TaskData;
import org.squale.squalix.core.TaskException;
import org.squale.squalix.core.exception.ConfigurationException;
import org.squale.squalix.util.buildpath.BuildProjectPath;
import org.squale.squalix.util.csv.CSVParser;
import org.squale.squalix.util.file.FileUtility;
import org.squale.squalix.util.parser.LanguageParser;
import org.squale.squalix.util.process.ProcessErrorHandler;
import org.squale.squalix.util.process.ProcessManager;
import org.squale.squalix.util.process.ProcessOutputHandler;

/**
 */
public abstract class RSMTask
    extends AbstractTask
    implements ProcessErrorHandler, ProcessOutputHandler, CSVParser.CSVHandler
{

    /** Le parser */
    protected CSVParser mParser;

    /**
     * Logger
     */
    protected static final Log LOGGER = LogFactory.getLog( RSMTask.class );

    /**
     * Instance du persisteur RSM
     */
    protected RSMPersistor mPersistor = null;

    /** le parser associ� au language */
    protected LanguageParser mLanguageParser;

    /**
     * Configuration de l'outil d'analyse
     */
    protected RSMConfiguration mConfiguration;

    /** Le processManager * */
    protected ProcessManager mProcess;

    /** le fichier contenant les noms des fichiers � analyser */
    protected File mToAnalyseFileList;

    /**
     * Construction des r�pertoires � analyser
     * 
     * @param pData donn�es de la t�che
     * @param pProjectParams param�tres du projet
     * @throws ConfigurationException si erreur
     * @return la liste des fichiers � analyser
     */
    private List buildFilesToProcess( TaskData pData, MapParameterBO pProjectParams )
        throws ConfigurationException
    {
        // On prend le view path
        String viewPath = (String) pData.getData( TaskData.VIEW_PATH );
        if ( viewPath == null )
        {
            String message = RSMMessages.getString( "exception.variable.not_found", TaskData.VIEW_PATH );
            LOGGER.error( message );
            // Renvoi d'une exception de configuration
            throw new ConfigurationException( message );
        }
        // Pour chaque r�pertoire source on ajoute celui-ci
        // On r�cup�re les chemins relatifs des r�pertoires contenant les sources du projet
        ListParameterBO sources = getSourcesDirs( pProjectParams );
        if ( sources == null )
        {
            String message = RSMMessages.getString( "exception.sources.notset", "" );
            LOGGER.error( message );
            // Renvoi d'une exception de configuration
            throw new ConfigurationException( message );
        }
        List sourcesString = new ArrayList( 0 );
        List sourcesStringBO = sources.getParameters();
        for ( int i = 0; i < sourcesStringBO.size(); i++ )
        {
            sourcesString.add( ( (StringParameterBO) ( sourcesStringBO.get( i ) ) ).getValue() );
        }
        // Prise en compte des r�pertoires exclus
        ListParameterBO excludedDirs = getExcludedDirs( pProjectParams );
        if ( excludedDirs == null )
        {
            // On affecte une liste vide
            excludedDirs = new ListParameterBO();
        }
        // Construction des fichiers � traiter
        List extensions = new ArrayList();
        extensions.addAll( Arrays.asList( mConfiguration.getExtensions() ) );
        extensions.addAll( Arrays.asList( mConfiguration.getHeaders() ) );
        String[] setOfExtensions = (String[]) ( extensions.toArray( new String[extensions.size()] ) );
        List resultSources =
            FileUtility.getIncludedFiles(
                                          viewPath,
                                          BuildProjectPath.buildProjectPath( viewPath, sources.getParameters() ),
                                          (ListParameterBO) mProject.getParameter( ParametersConstants.INCLUDED_PATTERNS ),
                                          (ListParameterBO) mProject.getParameter( ParametersConstants.EXCLUDED_PATTERNS ),
                                          (ListParameterBO) mProject.getParameter( ParametersConstants.EXCLUDED_DIRS ),
                                          setOfExtensions );
        return resultSources;
    }

    /**
     * Obtention des r�pertories exclus
     * 
     * @param pProjectParams param�tres du projet
     * @return r�pertoires exclus sous la forme de ListParameterBO(StringParameterBO)
     */
    protected ListParameterBO getExcludedDirs( MapParameterBO pProjectParams )
    {
        return (ListParameterBO) pProjectParams.getParameters().get( ParametersConstants.EXCLUDED_DIRS );
    }

    /**
     * Obtention des sources
     * 
     * @param pProjectParams param�tres du projet
     * @return sources sous la forme de ListParameterBO(StringParameterBO)
     */
    protected ListParameterBO getSourcesDirs( MapParameterBO pProjectParams )
    {
        return (ListParameterBO) pProjectParams.getParameters().get( ParametersConstants.SOURCES );
    }

    /**
     * lance le parsing des classes, la g�n�ration du rapport et le parsing du rapport
     * 
     * @throws TaskException en cas d'�chec
     */
    public void execute()
        throws TaskException
    {
        try
        {
            // initialise la tache RSM
            initialize();
            // construit la liste des r�pertoires � analyser (fichiers sources - exclus)
            buildDotLstFile( buildFilesToProcess( getData(), mProject.getParameters() ) );
            LOGGER.info( RSMMessages.getString( "logs.analyzing" ) + mProject.getParent().getName() + " - "
                + mProject.getName() );
            int execResult = parseSource();
            // un nombre n�gatif est une erreur d'ex�cution RSM
            if ( execResult < 0 )
            {
                throw new TaskException( RSMMessages.getString( "rsm.exec.erreur",
                                                                new Object[] { new Integer( execResult ) } ) );
            }
            // Si le parsing s'est bien d�roul�, on a g�n�r� le rapport
            // On parse maintenant le rapport
            // si le parsing ne s'est pas bien d�roul�, on va tombre dans l'exception
            // On ne g�re pas le code retour car le comportement de RSM est �trange
            parseReport( mConfiguration.getReportPath() );
            // Une fois que tous les rapports ont �t� g�n�r�s et pars�s,
            // on peut g�n�rer les r�sultats de niveau projet
            mPersistor.persistProjectResult();
            // positionne les donn�es sur la taille du file System
            affectFileSystemSize( mConfiguration.getWorkspace(), true );
            // Lance les op�rations de cloture de la t�che
            FileUtility.deleteRecursivelyWithoutDeleteRootDirectory( mConfiguration.getWorkspace() );
        }
        catch ( Exception e )
        {
            throw new TaskException( e );
        }
    }

    /**
     * Construit le fichier .txt recensant l'ensemble des fichiers que RSM doit analyser
     * 
     * @param pFilesToAnalyseList la liste des fichiers � analyser
     * @throws IOException en cas d'�chec
     */
    private void buildDotLstFile( List pFilesToAnalyseList )
        throws IOException
    {
        mToAnalyseFileList = new File( mConfiguration.getInputFile() );
        BufferedWriter bw = new BufferedWriter( new FileWriter( mToAnalyseFileList ) );
        for ( Iterator it = pFilesToAnalyseList.iterator(); it.hasNext(); )
        {
            String filePath = (String) it.next();
            bw.write( filePath );
            bw.newLine();
        }
        // ferme le buffer
        bw.close();
    }

    /**
     * @see org.squale.squalix.util.process.ProcessErrorHandler#processError(java.lang.String)
     */
    public void processError( String pErrorMessage )
    {
        LOGGER.error( RSMMessages.getString( "logs.error.tools_error" ) + pErrorMessage );
        ErrorBO error = new ErrorBO();
        error.setInitialMessage( pErrorMessage );
        error.setMessage( RSMMessages.getString( "error.processing" ) );
        error.setLevel( ErrorBO.CRITICITY_FATAL );
        mErrors.add( error );
    }

    /**
     * {@inheritDoc}
     * 
     * @param pOutputLine {@inheritDoc}
     * @see org.squale.squalix.util.process.ProcessOutputHandler#processOutput(java.lang.String)
     */
    public void processOutput( String pOutputLine )
    {
        // TODO Auto-generated method stub

    }

    /**
     * Pr�pare l'environnement d'ex�cution de l'analyse :
     * <ul>
     * <li>Cr�ation du dossier destination des r�sultats du parsing</li>
     * <li>Cr�ation du fichier config.pcf</li>
     * </ul>
     * 
     * @exception Exception si un probleme d'initialisation apparait
     */
    private void initialize()
        throws Exception
    {
        // On r�cup�re la configuration du module RSM, personnalis�e
        // avec les param�tres du projet
        mConfiguration = RSMConfiguration.build( mProject, RSMMessages.getString( "configuration.file" ), getData() );
        File workspace = mConfiguration.getWorkspace();
        if ( !workspace.exists() || !workspace.isDirectory() || !workspace.canWrite() || !workspace.canRead() )
        {
            // On va v�rifier que le workspace est disponible
            throw new Exception( RSMMessages.getString( "exception.no_workspace" ) );
        }
        mPersistor = new RSMPersistor( mConfiguration, mAudit, getSession(), getData(), mName, mLanguageParser );
        LOGGER.info( RSMMessages.getString( "logs.initialized" ) + mProject.getParent().getName() + " - "
            + mProject.getName() );
    }

    /**
     * {@inheritDoc}
     * 
     * @param pLine {@inheritDoc}
     * @see org.squale.squalix.util.csv.CSVParser.CSVHandler#processLine(java.util.List)
     */
    public void processLine( List pLine )
    {
        // TODO Auto-generated method stub

    }

    /**
     * Cr�e le ProcessManager. On ne fait pas de new mais un set pour impl�menter le pattern IOC pour pouvoir tester sur
     * un environnement windows
     * 
     * @param pArguments arguments
     * @param pDirectory r�pertoire de lancement
     * @return le ProcessManager normal
     */
    private ProcessManager createProcessManager( String[] pArguments, File pDirectory )
    {
        return new ProcessManager( pArguments, null, pDirectory );
    }

    /**
     * Parse les fichiers sources afin d'en extraire les m�triques.
     * 
     * @return le r�sultat de l'ex�cution
     * @throws Exception si un probl�me de parsing appara�t.
     */
    private int parseSource()
        throws Exception
    {
        // le r�sultat renvoy�
        int resultParse = 0;
        // Execute le parsing des sources avec RSM
        String[] parseCommand = new String[mConfiguration.getParseParameters().length + 1];
        parseCommand[0] = mConfiguration.getExecCommand();
        // Parse tous les r�pertoires sources sauf les r�pertoires exclus un par un
        for ( int i = 1; i < parseCommand.length; i++ )
        {
            String param = mConfiguration.getParseParameters()[i - 1];
            // On construit la commande
            // On met en forme la liste des param�tres pour la passer au process
            if ( "-O".equals( param ) )
            {
                param += mConfiguration.getReportPath();
            }
            else
            {
                if ( "-F".equals( param ) )
                {
                    param += mConfiguration.getInputFile();
                }
            }
            parseCommand[i] = param;
        }
        LOGGER.info( RSMMessages.getString( "logs.running_parsing.command", parseCommand ) );
        LOGGER.info( RSMMessages.getString( "logs.running_parsing", new Object[] { mProject.getParent().getName(),
            mProject.getName() } ) );
        mProcess = createProcessManager( parseCommand, mConfiguration.getWorkspace() );
        // On veut g�rer les informations lanc�es par le processus en sortie
        mProcess.setOutputHandler( this );
        // On cherche � avoir un comportement synchrone pour �tre s�r de ne pas
        // avoir un �tat des donn�es incoh�rent
        resultParse = mProcess.startProcess( this );

        LOGGER.info( RSMMessages.getString( "logs.return_parsing", new Object[] { mProject.getParent().getName(),
            mProject.getName(), new Integer( resultParse ) } ) );
        return resultParse;
    }

    /**
     * /** Analyse un rapport de m�triques RSM
     * 
     * @param pReport rapport � parser
     * @throws Exception si erreur
     */
    private void parseReport( final String pReport )
        throws Exception
    {
        mPersistor.parseReport( pReport, getData() );
    }

    /**
     * @return la configuration
     */
    public RSMConfiguration getConfiguration()
    {
        return mConfiguration;
    }

    /**
     * @param pConfiguration la configuration
     */
    public void setConfiguration( RSMConfiguration pConfiguration )
    {
        mConfiguration = pConfiguration;
    }

}