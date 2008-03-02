package com.airfrance.squalix.tools.mccabe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.squalecommon.enterpriselayer.businessobject.result.ErrorBO;
import com.airfrance.squalix.core.AbstractTask;
import com.airfrance.squalix.core.TaskData;
import com.airfrance.squalix.core.TaskException;
import com.airfrance.squalix.util.file.FileUtility;
import com.airfrance.squalix.util.parser.LanguageParser;
import com.airfrance.squalix.util.process.ProcessErrorHandler;
import com.airfrance.squalix.util.process.ProcessManager;
import com.airfrance.squalix.util.process.ProcessOutputHandler;

/**
 * Ex�cute l'analyse McCabe. L'environnement McCabe doit �tre correctement initialis� avant le lancement de la t�che, ou
 * la commande "cli" appel�e doit pointer vers un script qui le met en place.<br>
 * <br>
 * Il est important que la g�n�ration des rapports d�bute par celle du niveau m�thode, afin de pouvoir correctement
 * cr�er les composants ClassBO si n�cessaire.<br>
 * L'ordre des rapports dans le configuration est resp�ct�.
 */
public abstract class AbstractMcCabeTask
    extends AbstractTask
    implements ProcessErrorHandler, ProcessOutputHandler
{

    /**
     * Configuration de l'outil d'analyse
     */
    protected McCabeConfiguration mConfiguration;

    /** Le processManager * */
    protected ProcessManager mProcess;

    /**
     * Logger
     */
    private static final Log LOGGER = LogFactory.getLog( AbstractMcCabeTask.class );

    /**
     * Instance du persisteur McCabe
     */
    protected McCabePersistor mPersistor = null;

    /** Le parser */
    protected LanguageParser mParser;

    /** Le template de la classe � utiliser */
    protected String mClassTemplate = "csv.template.class";

    /** Buffer pour �crire les erreurs */
    protected BufferedWriter mErrorWriter;

    /** Nom du composant courant que McCabe parse (sert pour la remont�e des warning) */
    protected String mCurrentComponent = "";

    /**
     * L'analyse compl�te consiste en :
     * <ul>
     * <li>Lancement du parsing sur l'application</li>
     * <li>Cr�ation des rapports</li>
     * <li>Cr�ation des beans � partir des rapports</li>
     * <li>Transformation des beans en beans persistants</li>
     * <li>Persistance des beans</li>
     * </ul>
     * A la fin de l'analyse on effectue les op�rations de cloture de tache
     * 
     * @throws TaskException Si un probl�me d'ex�cution appara�t.
     */

    public void execute()
        throws TaskException
    {
        try
        {
            initialize();
            LOGGER.info( McCabeMessages.getString( "logs.analyzing" ) + mProject.getParent().getName() + " - "
                + mProject.getName() );
            int resultParse = parseSource();
            if ( 0 == resultParse )
            {
                // Si le parsing s'est bien d�roul�, on g�n�re les rapports
                // Creation de la base de rapports
                createSpcFile( mConfiguration );
                String reportName = null;
                for ( int i = 0; i < mConfiguration.getReports().size(); i++ )
                {
                    // Pour chaque nom de rapport configur�s dans le fichier de configuration
                    // McCabe, on va le cr�er, le parser et le mapper sur les objets
                    reportName = (String) mConfiguration.getReports().get( i );
                    createReport( reportName );
                }
                // On parse maintenant les rapports
                for ( int i = 0; i < mConfiguration.getReports().size(); i++ )
                {
                    // Pour chaque nom de rapport configur�s dans le fichier de configuration
                    // McCabe, on va le cr�er, le parser et le mapper sur les objets
                    reportName = (String) mConfiguration.getReports().get( i );
                    parseReport( reportName );
                }

                // Une fois que tous les rapports ont �t� g�n�r�s et pars�s,
                // on peut g�n�rer les r�sultats de niveau projet
                mPersistor.persistProjectResult();
            }

            // positionne les donn�es sur la taille du file System
            affectFileSystemSize( mConfiguration.getSubWorkspace(), true );

            // Lance les op�rations de cloture de la t�che
            FileUtility.deleteRecursively( mConfiguration.getSubWorkspace() );
            if ( 0 != resultParse )
            {
                // Si le parsing ne s'est pas bien d�roul�, on lance une exception
                throw new Exception( McCabeMessages.getString( "exception.parsing_error" ) );
            }
            if ( mErrorWriter != null )
            {
                mErrorWriter.close();
            }
        }
        catch ( Exception e )
        {
            throw new TaskException( e );
        }
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
        // On r�cup�re la configuration du module McCabe, personnalis�e
        // avec les param�tres du projet
        mConfiguration =
            McCabeConfiguration.build( mProject, McCabeMessages.getString( "configuration.file" ), getData() );
        try
        {
            // On tente de le cr�er si il n'existe pas
            mConfiguration.getErrorLogger().getParentFile().mkdirs();
            mErrorWriter = new BufferedWriter( new FileWriter( mConfiguration.getErrorLogger() ) );
        }
        catch ( IOException ioe )
        {
            // On log l'erreur
            LOGGER.warn( "Erreur sur le fichier de log des erreurs : "
                + mConfiguration.getErrorLogger().getAbsolutePath() );
            LOGGER.warn( ioe );
        }
        File workspace = mConfiguration.getWorkspace();
        if ( !workspace.exists() || !workspace.isDirectory() || !workspace.canWrite() || !workspace.canRead() )
        {
            // On va v�rifier que le workspace est disponible
            throw new Exception( McCabeMessages.getString( "exception.no_workspace" ) );
        }
        // On cr�e le fichier de description de l'analyse dans le workspace
        McCabePCFFile pcfFile = new McCabePCFFile( mConfiguration, getData() );
        createProjectConfigurationFile( pcfFile );
        // On cr�e une instance de persisteur et du template utilisant la session d�j� ouverte
        setParser();
        setClassTemplate();
        mPersistor =
            new McCabePersistor( mParser, mConfiguration, mAudit, getSession(), getData(), mName, mClassTemplate );
        LOGGER.info( McCabeMessages.getString( "logs.initialized" ) + mProject.getParent().getName() + " - "
            + mProject.getName() );
    }

    /**
     * Cr�ation du fichier de configuration McCabe Ce fichier de configuration contient la liste des fichiers
     * 
     * @param pFile fichier
     * @throws Exception si erreur
     */
    protected void createProjectConfigurationFile( McCabePCFFile pFile )
        throws Exception
    {
        pFile.build();
        // On copie le PCF sous le nom <projet>.pcf si le nombre de fichiers .pcf dans le workspace
        // est < 10
        File backupDir =
            new File( mConfiguration.getWorkspace() + File.separator + McCabeMessages.getString( "pcf.log.dir" ) );
        backupDir.mkdirs();
        File[] backupDirFiles = backupDir.listFiles();
        if ( backupDirFiles != null )
        {
            // On tri selon le nom du fichier d�croissant pour ne pas �craser les fichiers qui porte sur le
            // m�me projet.
            List files = Arrays.asList( backupDirFiles );
            Collections.sort( files );
            Collections.reverse( files );
            // On renomme tous les fichiers avec l'extension ext+1
            String fileName;
            String newFileName;
            File backUpFile = null;
            int ext;
            for ( int i = 0; i < files.size(); i++ )
            {
                fileName = ( (File) files.get( i ) ).getName();
                // Si un fichier porte l'extension max, on le supprime
                if ( fileName.matches( ".*\\." + ( McCabeConfiguration.MAX_PCF_SAVED - 1 ) ) )
                {
                    ( (File) files.get( i ) ).delete();
                }
                else
                {
                    ext = 0;
                    // On r�cup�re l'extension
                    int lastDot = fileName.lastIndexOf( "." );
                    if ( lastDot > 0 )
                    {
                        try
                        {
                            ext = Integer.parseInt( fileName.substring( lastDot + 1 ) ) + 1;
                            // On renomme le fichier
                            newFileName = fileName.substring( 0, lastDot + 1 ) + ext;
                            ( (File) files.get( i ) ).renameTo( new File(
                                                                          backupDirFiles[i].getParentFile().getAbsoluteFile()
                                                                              + File.separator + newFileName ) );
                        }
                        catch ( NumberFormatException nbfe )
                        {
                            // On renommera le fichier avec l'extension 1 (pour ne pas �craser celui
                            // qui existe)
                            backUpFile = (File) files.get( i );
                        }
                    }
                }
            }
            // On copie le fichier pcf dans le r�pertoire de sauvegarde
            String backupName = backupDir.getAbsolutePath() + File.separator + mProject.getName() + ".pcf";
            // On renomme le fichier extension.1 si il existe
            if ( backUpFile != null )
            {
                backUpFile.renameTo( new File( backUpFile.getAbsolutePath() + ".1" ) );
            }
            FileUtility.copyFile( pFile.getPcfFile(), new File( backupName ) );
        }
    }

    /**
     * Cr�e le fichier contenant le chemin vers les mod�les de rapports McCabe et celui avec les param�tres sp�ciaux
     * utilisateurs.
     * 
     * @param pConfiguration configuration � utiliser.
     * @throws IOException Si un probl�me d'�criture du fichier appara�t.
     */
    protected void createSpcFile( final McCabeConfiguration pConfiguration )
        throws IOException
    {
        String spcFileName =
            pConfiguration.getSubWorkspace().getAbsolutePath() + File.separator
                + McCabeMessages.getString( "reports_db.name" );
        BufferedWriter bw = new BufferedWriter( new FileWriter( spcFileName ) );
        bw.write( McCabeMessages.getString( "spc.header" ) );
        bw.write( pConfiguration.getReportsPath().getAbsolutePath() );
        bw.close();
        File repository =
            new File( pConfiguration.getSubWorkspace().getAbsolutePath() + File.separator
                + McCabeMessages.getString( "file.user.def.header" ) );
        repository.mkdir();
        String userFileName =
            pConfiguration.getSubWorkspace().getAbsolutePath() + File.separator
                + McCabeMessages.getString( "user_def.name" );
        bw = new BufferedWriter( new FileWriter( userFileName ) );
        bw.write( McCabeMessages.getString( "file.user.def.header" ) );
        bw.write( repository.getAbsolutePath() );
        bw.newLine();
        bw.close();
    }

    /**
     * Parse les fichiers sources afin d'en extraire les m�triques.
     * 
     * @return le r�sultat du process de parsing.
     * @throws Exception si un probl�me de parsing appara�t.
     */
    protected int parseSource()
        throws Exception
    {
        // Execute le parsing des sources avec McCabe
        String[] parseCommand = new String[mConfiguration.getParserCommand().length + 1];
        parseCommand[0] = mConfiguration.getCliCommand();
        for ( int i = 1; i < parseCommand.length; i++ )
        {
            // On met en forme la liste des param�tres pour la passer au process
            parseCommand[i] = mConfiguration.getParserCommand()[i - 1];
        }
        LOGGER.info( McCabeMessages.getString( "logs.running_parsing", new Object[] { mProject.getParent().getName(),
            mProject.getName() } ) );
        mProcess = createProcessManager( parseCommand, mConfiguration.getSubWorkspace(), mConfiguration.getLogger() );
        // On veut g�rer les informations lanc�es par le processus en sortie
        mProcess.setOutputHandler( this );
        // On cherche � avoir un comportement synchrone pour �tre s�r de ne pas
        // avoir un �tat des donn�es incoh�rent
        int resultParse = mProcess.startProcess( this );
        LOGGER.info( McCabeMessages.getString( "logs.return_parsing", new Object[] { mProject.getParent().getName(),
            mProject.getName(), new Integer( resultParse ) } ) );

        return resultParse;
    }

    /**
     * G�n�re un rapport de m�triques McCabe.
     * 
     * @param pReport le nom de rapport � g�n�rer.
     * @throws Exception si un probl�me d'ex�cution appara�t.
     */
    protected void createReport( final String pReport )
        throws Exception
    {
        LOGGER.info( McCabeMessages.getString( "logs.create.report", pReport ) );
        // Lancement de la cr�ation de tous les rapports
        String reportFileName = computeReportFileName( pReport );
        ArrayList params = new ArrayList();
        params.add( mConfiguration.getCliCommand() );
        String param = null;
        for ( int j = 0; j < mConfiguration.getMetricsCommand().length; j++ )
        {
            // Remplacer les param�tres configurables (%...%), ce sont des param�tres
            // qui remplacent des cl�s dans les param�tres de configuration
            // des analyses McCabe (classpath pour Java,...)
            param =
                mConfiguration.getMetricsCommand()[j].replaceAll(
                                                                  McCabeMessages.getString( "key.substition.report_name" ),
                                                                  pReport );
            param = param.replaceAll( McCabeMessages.getString( "key.substition.report_out" ), reportFileName );
            params.add( param );
        }
        String type[] = new String[0];
        String modifiedParams[] = (String[]) params.toArray( type );

        mProcess = createProcessManager( modifiedParams, mConfiguration.getSubWorkspace(), mConfiguration.getLogger() );
        // On veut g�rer les informations lanc�es par le processus en sortie
        mProcess.setOutputHandler( this );
        int resultMetrics = mProcess.startProcess( this );
        if ( resultMetrics != 0 )
        {
            throw new McCabeException( McCabeMessages.getString( "error.processing" ) + " #" + resultMetrics );
        }
    }

    /**
     * Analyse un rapport de m�triques McCabe
     * 
     * @param pReport rapport � parser
     * @throws Exception si erreur
     */
    private void parseReport( final String pReport )
        throws Exception
    {
        String reportFileName = computeReportFileName( pReport );
        // Parser le rapport g�n�r�
        // Il y a deux m�thodes de parsing des rapports, une pour les rapports de m�thodes
        // et l'autre pour les rapports de classe. Ainsi, il suffit que le nom du rapport
        // d�bute par METHOD ou CLASS pour que la bonne m�thode soit s�lectionn�, le reste
        // est laiss� � l'appr�ciation de l'utilisateur. Ceci permet de versionner, dater
        // les noms de rapports.

        if ( pReport.startsWith( McCabeMessages.getString( "reports.profile.class" ) ) )
        {
            mPersistor.parseClassReport( reportFileName );
        }
        else if ( pReport.startsWith( McCabeMessages.getString( "reports.profile.module" ) ) )
        {
            mPersistor.parseMethodReport( reportFileName, mData );
        }
    }

    /**
     * D�termination du fichier de rapport
     * 
     * @param pReport rapport
     * @return nom complet du rapport
     * @throws IOException si erreur
     */
    protected String computeReportFileName( final String pReport )
        throws IOException
    {
        String reportFileName = pReport + McCabeMessages.getString( "reports_extension" );
        reportFileName = mConfiguration.getSubWorkspace().getCanonicalPath() + File.separator + reportFileName;
        return reportFileName;
    }

    /**
     * @see com.airfrance.squalix.util.process.ProcessErrorHandler#processError(java.lang.String)
     */
    public void processError( String pErrorMessage )
    {
        // cette m�thode n'est effectu� que si le message n'est pas � ignorer
        if ( !mConfiguration.ignoreMsg( pErrorMessage ) )
        {
            LOGGER.error( McCabeMessages.getString( "logs.error.tools_error" ) + pErrorMessage );
            // Certaines erreurs sonst connues, on va alors essayer de les mapper avec
            // un message plus explicite
            ErrorBO error = new ErrorBO();
            error.setInitialMessage( pErrorMessage );
            // On modifie le message en rempla�ant par un message plus explicite si il est d�fini
            error.setMessage( mConfiguration.getReplacingMsg( pErrorMessage ) );
            // sinon on met un message g�n�rique
            if ( pErrorMessage.equals( error.getMessage() ) )
            {
                error.setMessage( McCabeMessages.getString( "error.processing" ) );
            }
            error.setLevel( ErrorBO.CRITICITY_FATAL );
            if ( !mConfiguration.ignoreMsg( pErrorMessage ) && !mConfiguration.changeErrorMsgToWarning( pErrorMessage ) )
            {
                mErrors.add( error );
            }
            // Et on ins�re la cha�ne dans le fichir de log des erreurs si le buffer a �t� cr�e
            if ( mErrorWriter != null )
            {
                try
                {
                    mErrorWriter.write( pErrorMessage + "\n" );
                }
                catch ( IOException e )
                {
                    // On log juste l'erreur
                    LOGGER.warn( e );
                }
            }
        }
    }

    /**
     * Ajoute la configuration � la t�che.
     * 
     * @param pConfiguration la configuration � ajouter.
     * @roseuid 42DE1C2E01CC
     */
    public void setConfiguration( McCabeConfiguration pConfiguration )
    {
        mConfiguration = pConfiguration;
    }

    /**
     * R�cup�re la configuration.
     * 
     * @return la configuration
     */
    public McCabeConfiguration getConfiguration()
    {
        return mConfiguration;
    }

    /**
     * Cr�e le ProcessManager. On ne fait pas de new mais un set pour impl�menter le pattern IOC pour pouvoir tester sur
     * un environnement windows
     * 
     * @param pArguments arguments
     * @param pDirectory r�pertoire de lancement
     * @param pLogger le logger
     * @return le ProcessManager normal
     */
    protected ProcessManager createProcessManager( String[] pArguments, File pDirectory, File pLogger )
    {
        return new ProcessManager( pArguments, null, pDirectory, pLogger );
    }

    /**
     * Modifie le parser
     */
    public abstract void setParser();

    /**
     * Modifie le template niveau classe
     */
    public abstract void setClassTemplate();

    /**
     * @see com.airfrance.squalix.util.process.ProcessOutputHandler#processOutput(java.lang.String)
     */
    public void processOutput( String pOutputLine )
    {
        // On filtre une erreur connue "Can't create directory '/OUTILS/McCabe/current/repos'."
        // qui ne g�ne en rien le bon d�roulement de McCabe.
        if ( mConfiguration != null && !mConfiguration.ignoreMsg( pOutputLine ) )
        {
            // On ajoute le nom du fichier au message s'il le faut
            mConfiguration.addFileNameToMsg( pOutputLine, mCurrentComponent );
            // CAS ERREUR
            if ( pOutputLine.startsWith( "ERR" ) )
            {
                // On teste si le message d'erreur ne fait pas parti des messages � consid�rer comme warning
                if ( mConfiguration.changeErrorMsgToWarning( pOutputLine ) )
                {
                    // On log un warning
                    initError( pOutputLine, ErrorBO.CRITICITY_WARNING );
                }
                else
                {
                    // Il s'agit d'une erreur
                    initError( pOutputLine, ErrorBO.CRITICITY_FATAL );
                    mStatus = FAILED;
                }
                // CAS WARNING
            }
            else if ( pOutputLine.startsWith( "WARN" ) )
            {
                initError( pOutputLine, ErrorBO.CRITICITY_WARNING );
            }
            else if ( pOutputLine.matches( "cli: Processing " + (String) mData.getData( TaskData.VIEW_PATH ) + ".*" ) )
            {
                // On r�cup�re le nom du fichier en coupant la cha�ne du d�but et le viewPath
                mCurrentComponent =
                    pOutputLine.replaceFirst( "cli: Processing " + (String) mData.getData( TaskData.VIEW_PATH ), "" );
            }

            // On ne r�cup�re pas les autres traces
        }
    }
}
