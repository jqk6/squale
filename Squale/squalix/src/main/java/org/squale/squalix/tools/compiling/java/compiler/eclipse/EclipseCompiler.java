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
package org.squale.squalix.tools.compiling.java.compiler.eclipse;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.squale.jraf.commons.exception.JrafEnterpriseException;
import org.squale.squalecommon.datatransfertobject.message.MessagesDTO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.ListParameterBO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.MapParameterBO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.StringParameterBO;
import org.squale.squalecommon.enterpriselayer.businessobject.result.ErrorBO;
import org.squale.squalecommon.enterpriselayer.facade.message.MessageFacade;
import org.squale.squalix.tools.compiling.CompilingMessages;
import org.squale.squalix.tools.compiling.java.beans.JWSADProject;
import org.squale.squalix.tools.compiling.java.configuration.EclipseCompilingConfiguration;
import org.squale.squalix.util.file.FileUtility;
import org.squale.squalix.util.process.ProcessErrorHandler;
import org.squale.squalix.util.process.ProcessManager;
import org.squale.squalix.util.process.ProcessOutputHandler;

/**
 * Compilation avec le plugin eclipse 3.2 (test� sous Eclipse 3.2.1 et 3.3.0)
 */
public class EclipseCompiler
    implements ProcessErrorHandler, ProcessOutputHandler
{

    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactory.getLog( EclipseCompiler.class );

    /** Le s�parateur entre les variables */
    private static String ECLIPSE_VAR_VAR_SEP = "|";

    /** Le s�parateur entre le nom de la vrariables et ses jars */
    private static String ECLIPSE_VAR_PATHS_SEP = "=";

    /** Le s�parateur entre les jars */
    private static String ECLIPSE_PATH_SEP = ";";

    /** Log de niveau WARNING */
    private static final String WARNING_LOG = "ATTENTION: ";
    
    /** Log de niveau WARNING */
    private static final String[] WARNING_LOG_ARRAY = {"ATTENTION: ","WARNING: "};

    /** Log de niveau GRAVE */
    private static final String ERROR_LOG = "GRAVE: ";
    
    /** Log de niveau GRAVE */
    private static final String[] ERROR_LOG_ARRAY = {"GRAVE: ","SEVERE: ","ERROR: "};

    /** Log de niveau INFO */
    private static final String INFO_LOG = "INFO: ";
    
    /** Log de niveau INFO */
    private static final String[] INFO_LOG_ARRAY = {"INFO: ","INFORMATION: "};

    /** Le nombre max d'erreurs � sauvegarder par type */
    private static final int MAX_ERRORS = 100;

    /** Log pour r�cup�rer le classpath */
    private static final String CLASSPATH_LOG = "INFO: CLASSPATH=";

    /** Log pour r�cup�rer les r�pertoires de compilation */
    private static final String OUTPUTS_LOG = "INFO: OUTPUT_DIRS=";

    /** Default advanced options */
    private static final String DEFAULT_ADVANCED_OPTIONS = "-os win32 -arch x86 -ws win32";

    /**
     * Liste des projets eclipse.
     */
    private List projectList = null;

    /** Le view path */
    private String mViewPath;

    /** La liste des erreurs */
    private List errors;

    /** Le nombre d'erreur */
    private int nbErrors;

    /** Le nombre de warning */
    private int nbWarning;

    /** Le nomdre d'info */
    private int nbInfo;

    /** Le classpath */
    private String classpath = "";

    /** Les r�pertoires de compilation */
    private List outputDirs;

    /** L'option -vars */
    private String varsOption = "";

    /** L'option -userLibs */
    private String userLibsOption = "";

    /** Advanced eclipse launcher options (like -os, -arch, -ws) */
    private String advancedOptions = DEFAULT_ADVANCED_OPTIONS;

    /**
     * Constructeur.
     * 
     * @param pProjectList liste des projets eclipse � compiler.
     * @param pViewPath le viewPath
     * @param eclipseParams Eclipse parameters defined by user
     */
    public EclipseCompiler( List pProjectList, String pViewPath, MapParameterBO eclipseParams )
    {
        projectList = pProjectList;
        mViewPath = pViewPath;
        errors = new ArrayList();
        outputDirs = new ArrayList();
        if ( null != eclipseParams )
        {
            buildVarsOption( (MapParameterBO) eclipseParams.getParameters().get( ParametersConstants.ECLIPSE_VARS ) );
            buildUserLibsOption( (MapParameterBO) eclipseParams.getParameters().get( ParametersConstants.ECLIPSE_LIBS ) );
            setAdvancedOptions( (StringParameterBO) eclipseParams.getParameters().get(
                                                                                       ParametersConstants.ECLIPSE_ADVANCED_OPTIONS ) );
        }
        else
        {
            setAdvancedOptions( null );
        }

    }

    /**
     * Launch Java Compiling with Eclipse bundle
     * 
     * @throws Exception in case of error
     * @return 0 if the compilation has been successfully done
     */
    public int runCompilation()
        throws Exception
    {
        // Creation of the Eclipse compiling command
        String curPath;
        File currentPath;
        // Values for -projects option of Eclipse command
        String projectsOption = "";

        // Parsing Eclipse compiling configuration file
        EclipseCompilingConfiguration conf = new EclipseCompilingConfiguration();
        conf.parse( new FileInputStream( new File( "config/eclipsecompiling-config.xml" ) ) );

        // Full clean of directories to prevent interaction with the previous tasks
        File workspaceDir = new File( conf.getWorkspace() );
        FileUtility.deleteRecursively( new File( conf.getWorkspace() ) );
        FileUtility.deleteRecursively( conf.getEclipseHome() );

        // Workspace directory creation
        workspaceDir.mkdirs();

        // Adding write rights on the project folders
        // Collecting projects paths for the compiling command
        for ( int i = 0; i < projectList.size(); i++ )
        {
            curPath = ( (JWSADProject) projectList.get( i ) ).getPath();
            currentPath = new File( curPath );
            FileUtility.setWriteRights( currentPath, "both" );
            // Adding project path for the compiling command
            projectsOption += curPath;
            projectsOption += ECLIPSE_PATH_SEP;
        }

        // Creating Eclipse Base directory
        conf.getEclipseHome().mkdirs();
        // Getting the path of the minimal Eclipse environment and the destination path
        File squaleEclipsePluginsPath = new File( conf.getSqualeEclipsePlugins().getAbsolutePath() );
        File eclipseHomePath = new File( conf.getEclipseHome().getAbsolutePath() );
        // Copying the minimal Eclipse environment
        LOGGER.info( "Minimal Eclipse environment install from : "
            + conf.getSqualeEclipsePlugins().getAbsolutePath() );
        FileUtility.copyDirContentIntoDir( squaleEclipsePluginsPath, eclipseHomePath );

        // Getting the reference (archive file or directory) to the choosen Eclipse bundle
        File userPluginsPath = new File( ( (JWSADProject) projectList.get( 0 ) ).getBundleDir().getAbsolutePath() );
        // Getting the path to the plugins directory of the minimal Eclipse environment
        File bundlePluginsPath = new File( new File( conf.getEclipseHome(), "plugins" ).getAbsolutePath() );

        // Copying the given bundle into the Eclipse Environment
        LOGGER.info( "Choosen Eclipse bundle install from : " + userPluginsPath );
        FileUtility.copyDirContentIntoDir( userPluginsPath, bundlePluginsPath );

        // Collecting Java version for compiling command
        String dialect = ( (JWSADProject) projectList.get( 0 ) ).getJavaVersion().replaceAll( "_", "." );
        // Building Exclude patterns
        String excludedPatterns = buildExcludedPatterns();
        // Building Sun JAR list
        String sunLibs =
            buildSunLibs( ( (JWSADProject) projectList.get( 0 ) ).getBootClasspath(),
                          ( (JWSADProject) projectList.get( 0 ) ).getJavaVersion() );

        // Setting mandatory arguments
        List command = new ArrayList();
        command.add( conf.getCommand() );
        // On ajoute la liste des options obligatoires en rempla�ant les
        // param�tres de substitution
        // (sous la forme [valeur])
        for ( int i = 0; i < conf.getOptions().size(); i++ )
        {
            // On effectue tous les remplacement possible
            String curOption = ( (String) conf.getOptions().get( i ) ).replaceAll( "\\[dialect\\]", dialect );
            curOption = curOption.replaceAll( "\\[squale_eclipse_home\\]", conf.getEclipseHome().getAbsolutePath() );
            curOption = curOption.replaceAll( "\\[sun_libs\\]", sunLibs );
            curOption = curOption.replaceAll( "\\[projects_list\\]", projectsOption );
            curOption = curOption.replaceAll( "\\[var_libs\\]", varsOption );
            curOption = curOption.replaceAll( "\\[user_libs\\]", userLibsOption );
            curOption = curOption.replaceAll( "\\[workspace\\]", conf.getWorkspace() );
            curOption = curOption.replaceAll( "\\[excluded_patterns\\]", excludedPatterns );
            // Particular case of advanced options which may contain more than
            // one option
            if ( curOption.matches( "\\[advanced_options\\]" ) )
            {
                String[] advancedOptionsTab = advancedOptions.split( " " );
                for ( int o = 0; o < advancedOptionsTab.length; o++ )
                {
                    command.add( advancedOptionsTab[o] );
                }
            }
            else
            {
                command.add( curOption );
            }
        }
        
        // On supprime le fichier org.eclipse.wst.common.project.facet.core.xml
        FileUtility.deleteFilesinPath( new File ( mViewPath ) , "org.eclipse.wst.common.project.facet.core.xml" );
        FileUtility.deleteFilesinPath( new File ( mViewPath ) , "org.eclipse.jst.common.project.facet.core.prefs" );
        
        // On cr�e le process
        ProcessManager compileProcess =
            new ProcessManager( (String[]) command.toArray( new String[command.size()] ), null, workspaceDir );
        compileProcess.setOutputHandler( this );

        LOGGER.info( "On lance la compilation avec eclipse : " + printCommand( command ) );

        // On demarre le processus et on retourne 0 si l'execution s'est bien
        // d�roul�e
        return compileProcess.startProcess( this );
    }

    /**
     * Set advanced options for launching plugin. Advanced options are necessary to specify os configuration in order to
     * get plugins linked to OS like org.eclipse.swt.win32.win32.x86_3.2.0.v3232m.jar (see
     * http://help.eclipse.org/help21/index.jsp?topic=/org.eclipse.platform.doc.user/tasks/running_eclipse.htm)
     * 
     * @param advancedOptionsParam advanced options define in project parameters
     */
    private void setAdvancedOptions( StringParameterBO advancedOptionsParam )
    {
        // Get user parameter if exists
        if ( null != advancedOptionsParam )
        {
            advancedOptions = advancedOptionsParam.getValue();
        }
        else
        {
            // get key configuration for advanced options
            try
            {
                MessagesDTO webMsg = MessageFacade.getMessages();
                String advancedKey = webMsg.getMessage( "fr", "eclipse.compilation.advanced.options" );
                if ( null != advancedKey )
                {
                    advancedOptions = advancedKey;
                }
                else
                {
                    // Log warning for administrator
                    LOGGER.warn( "Advanced options configuration key (eclipse.compilation.advanced.options ) "
                        + "is not set!! Reload message.xml file." );

                }
            }
            catch ( JrafEnterpriseException e )
            {
                // Log warning and take default advanced options
                LOGGER.warn( "Cannot get advanced options configuration key: " + e.getMessage() );
            }
        }
    }

    /**
     * Met en forme l'option -vars de la commande
     * 
     * @param pVarsParams les param�tres � mettre en forme
     */
    private void buildVarsOption( MapParameterBO pVarsParams )
    {
        StringBuffer varLibs = new StringBuffer( "" );
        if ( null != pVarsParams )
        {
            for ( Iterator it = pVarsParams.getParameters().keySet().iterator(); it.hasNext(); )
            {
                String curVarName = (String) it.next();
                String lib = ( (StringParameterBO) pVarsParams.getParameters().get( curVarName ) ).getValue();
                // On v�rifie que le fichier existe sinon on log un warning
                File libFile = FileUtility.getAbsoluteFile( mViewPath, new File( lib ) );
                if ( null == libFile )
                {
                    LOGGER.warn( "File for variable " + curVarName + " doesn't exist: " + lib );
                }
                else
                {
                    varLibs.append( curVarName );
                    varLibs.append( ECLIPSE_VAR_PATHS_SEP );
                    varLibs.append( libFile.getAbsolutePath() );
                    varLibs.append( ECLIPSE_VAR_VAR_SEP );
                }
            }
            varsOption = varLibs.toString();
        }
    }

    /**
     * Met en forme l'option -userLibs de la commande
     * 
     * @param pEclipseLibs les param�tres � mettre en forme
     */
    private void buildUserLibsOption( MapParameterBO pEclipseLibs )
    {
        StringBuffer libsBuf = new StringBuffer( "" );
        if ( null != pEclipseLibs )
        {
            for ( Iterator it = pEclipseLibs.getParameters().keySet().iterator(); it.hasNext(); )
            {
                String curLibName = (String) it.next();
                List libs = ( (ListParameterBO) pEclipseLibs.getParameters().get( curLibName ) ).getParameters();
                // On met en forme les libs
                String libsForm = buildEclipseLibPaths( libs );
                if ( libsForm.length() == 0 )
                {
                    LOGGER.warn( "Can't add " + curLibName + " because of no existing paths" );
                }
                else
                {
                    libsBuf.append( curLibName );
                    libsBuf.append( ECLIPSE_VAR_PATHS_SEP );
                    libsBuf.append( libsForm );
                    libsBuf.append( ECLIPSE_VAR_VAR_SEP );
                }
            }
        }
        userLibsOption = libsBuf.toString();
    }

    /**
     * @param libPaths les chemins vers les libraries d'un librairie utilistateur
     * @return les chemins vers toutes les librairies format�s
     */
    private String buildEclipseLibPaths( List libPaths )
    {
        StringBuffer value = new StringBuffer( "" );
        for ( int i = 0; i < libPaths.size(); i++ )
        {
            String lib = ( (StringParameterBO) libPaths.get( i ) ).getValue();
            File libFile = FileUtility.getAbsoluteFile( mViewPath, new File( lib ) );
            if ( null == libFile )
            {
                LOGGER.warn( "File for library doesn't exist: " + lib );
            }
            else
            {
                // On l'ajoute
                value.append( libFile.getAbsolutePath() );
                value.append( ECLIPSE_PATH_SEP );
            }
        }
        return value.toString();
    }

    /**
     * @return les patterns s�par�s par ";"
     */
    private String buildExcludedPatterns()
    {
        // On parcours tous les r�pertoires � exclure de tous les projets puis
        // on les cr�e et on les s�pare avec ";"
        StringBuffer value = new StringBuffer( "" );
        for ( int p = 0; p < projectList.size(); p++ )
        {
            JWSADProject project = (JWSADProject) projectList.get( p );
            if ( null != project.getExcludedDirs() )
            {
                for ( int i = 0; i < project.getExcludedDirs().size(); i++ )
                {
                    value.append( project.getExcludedDirs().get( i ) );
                    value.append( ECLIPSE_PATH_SEP );
                }
            }
        }
        // On supprime les doublons
        return value.toString().replaceAll( "//", "/" );
    }

    /**
     * @param bootClasspath le chemin vers la jre de sun
     * @param dialect le dialect afin de r�cup�rer les jar du J2EE de sun
     * @return la liste des librairies sun � ajouter au classpath des projets sous la forme jar1;jar2
     */
    private String buildSunLibs( String bootClasspath, String dialect )
    {
        StringBuffer libs = new StringBuffer( "" );
        String[] bootClasspathSplitting = bootClasspath.split( ECLIPSE_PATH_SEP );
        // Parcours du bootclasspath et r�cup�ration des .jars et .zip
        for ( int i = 0; i < bootClasspathSplitting.length; i++ )
        {
            addCompilingRessources( new File( bootClasspathSplitting[i].trim() ), libs );
        }

        // On cr�e le descripteur vers le dossier contenant les jars � ajouter
        // pour la compilation
        StringBuffer path = new StringBuffer( CompilingMessages.getString( "dir.root.java" ) );
        path.append( "/" );
        path.append( dialect );
        path.append( "/" );
        File javaRoot = new File( path.toString().replace( '.', '_' ) );
        addCompilingRessources( javaRoot, libs );
        return libs.toString();
    }

    /**
     * @param pFile le fichier (ou r�pertoire contenant les fichiers) � ajouter
     * @param jarsList le buffer contenant la liste des jars
     * @return la liste des jars ou zip
     */
    private String addCompilingRessources( File pFile, StringBuffer jarsList )
    {
        // Buffer qui contiendra la liste des jars
        // Parcours du r�pertoire
        if ( pFile.isDirectory() )
        {
            File[] fileList = pFile.listFiles();
            for ( int i = 0; i < fileList.length; i++ )
            {
                // s'il s'agit d'un fichier
                if ( fileList[i].isFile() )
                {
                    // si l'extension est autoris�e
                    if ( fileList[i].getAbsolutePath().endsWith( ".jar" )
                        || fileList[i].getAbsolutePath().endsWith( ".zip" ) )
                    {
                        // on ajoute le fichier au buffer
                        jarsList.append( fileList[i].getAbsolutePath() );
                        jarsList.append( ECLIPSE_PATH_SEP );
                    }
                    // s'il s'agit d'un r�pertoire
                }
                else
                {
                    // on appelle r�cursivement la m�thode
                    addCompilingRessources( fileList[i], jarsList );
                }
            }
        }
        else if ( pFile.isFile() )
        {
            // on ajoute le fichier au buffer
            jarsList.append( pFile.getAbsolutePath() );
            jarsList.append( ECLIPSE_PATH_SEP );
        }
        else
        {
            // On log une warning
            LOGGER.warn( CompilingMessages.getString( "exception.jar_not_found", pFile.getAbsolutePath() ) );
        }
        return jarsList.toString();
    }

    /**
     * @param commandAndArgs la commande et ses arguments sous forme de liste
     * @return la comande qui va �tre ex�cut�e
     */
    private String printCommand( List commandAndArgs )
    {
        StringBuffer display = new StringBuffer( "" );
        for ( int i = 0; i < commandAndArgs.size(); i++ )
        {
            display.append( commandAndArgs.get( i ) );
            display.append( " " );
        }
        return display.toString();
    }

    /**
     * {@inheritdoc}
     * 
     * @see org.squale.squalix.util.process.ProcessErrorHandler#processError(java.lang.String)
     */
    public void processError( String pErrorMessage )
    {
        createError( pErrorMessage );
    }

    /**
     * {@inheritdoc}
     * 
     * @see org.squale.squalix.util.process.ProcessOutputHandler#processOutput(java.lang.String)
     */
    public void processOutput( String pOutputLine )
    {
        createError( pOutputLine );
    }

    /**
     * @param pMessage le message de sortie
     */
    private void createError( String pMessage )
    {
        LOGGER.info( pMessage );
        // Le niveau de criticit� d�pend du niveau de criticit� du log
        if ( arrayStartsWith( pMessage, WARNING_LOG_ARRAY ) )
        {
            createError( pMessage.replaceFirst( WARNING_LOG, "" ), ErrorBO.CRITICITY_WARNING, nbWarning );
            nbWarning++;
        }
        else if ( arrayStartsWith( pMessage, ERROR_LOG_ARRAY ) )
        {
            createError( pMessage.replaceFirst( ERROR_LOG, "" ), ErrorBO.CRITICITY_FATAL, nbErrors );
            nbErrors++;
        }
        else if ( arrayStartsWith( pMessage, INFO_LOG_ARRAY ) )
        {
            // On r�cup�re le classpath �crit en log
            if ( pMessage.startsWith( CLASSPATH_LOG ) )
            {
                classpath = pMessage.replaceFirst( CLASSPATH_LOG, "" );
            }
            else if ( pMessage.startsWith( OUTPUTS_LOG ) )
            {
                buildOutputDirs( pMessage.replaceFirst( OUTPUTS_LOG, "" ) );
            }
            else
            {
                createError( pMessage.replaceFirst( INFO_LOG, "" ), ErrorBO.CRITICITY_LOW, nbInfo );
                nbInfo++;
            }
        }
        // On ne traite pas les autres
    }

    /**
     * Permet de construire la liste des r�pertoires de compilation par le log sous la forme output1;output2
     * 
     * @param pOutputLog le log repr�sentant les r�pertoires de compilation
     */
    private void buildOutputDirs( String pOutputLog )
    {
        String[] outputDirsSplitting = pOutputLog.split( ECLIPSE_PATH_SEP );
        for ( int i = 0; i < outputDirsSplitting.length; i++ )
        {
            // If path name has spaces, we move all classes in an another
            // directory
            // without spaces (see CkjmTask) and we replace classes directory in
            // tempory classpath
            if ( outputDirsSplitting[i].indexOf( ' ' ) != -1 )
            {
                // List classes
                File[] classesToMove = new File( outputDirsSplitting[i] ).listFiles();
                // Change outputdir (unique name to be sure the classes are
                // renamming)
                String curDir = outputDirsSplitting[i];
                outputDirsSplitting[i] = ( (JWSADProject) projectList.get( 0 ) ).getDestPath() + i;
                // Replace it in classpath
                classpath = classpath.replaceAll( curDir, outputDirsSplitting[i] );
                File cleanOutputFile = new File( outputDirsSplitting[i] );
                cleanOutputFile.mkdirs();
                for ( int f = 0; f < classesToMove.length; f++ )
                {
                    classesToMove[f].renameTo( new File( outputDirsSplitting[i], classesToMove[f].getName() ) );
                }
            }
            outputDirs.add( outputDirsSplitting[i] );
        }
    }

    /**
     * Cr�ation d'une erreur et ajout dans la liste des erreurs
     * 
     * @param pErrorMessage le message
     * @param pErrorLevel la criticit�
     * @param pNbErrors the number of created errors
     */
    private void createError( String pErrorMessage, String pErrorLevel, int pNbErrors )
    {
        if ( pNbErrors < MAX_ERRORS )
        {
            ErrorBO error = new ErrorBO();
            error.setMessage( pErrorMessage );
            error.setInitialMessage( pErrorMessage );
            error.setLevel( pErrorLevel );
            errors.add( error );
        }
    }

    /**
     * @return le classpath
     */
    public String getClasspath()
    {
        return classpath;
    }

    /**
     * @return les erreurs trouv�es
     */
    public List getErrors()
    {
        return errors;
    }

    /**
     * @return la liste des r�pertoires de compilation
     */
    public List getOutputDirs()
    {
        return outputDirs;
    }
    
    /**
     * checks that Classpath and OutputDirs are correctly set
     */
    public void checkOutputVariables()
    {
        // V�rif de la longueur du Classpath
        if (classpath.length() == 0)
        {
            ErrorBO error = new ErrorBO();
            error.setMessage( "Compiled ClassPath is empty." );
            error.setInitialMessage( "Compiled ClassPath is empty." );
            error.setLevel( ErrorBO.CRITICITY_WARNING );
            errors.add( error );
            nbWarning++;   
        }
        // V�rif de la liste des r�pertoires de compilation
        if (outputDirs.isEmpty())
        {
            ErrorBO error = new ErrorBO();
            error.setMessage( "Compiled Output Directories are empty." );
            error.setInitialMessage( "Compiled Output Directories are empty." );
            error.setLevel( ErrorBO.CRITICITY_WARNING );
            errors.add( error );
            nbWarning++; 
        }
        // V�rif inverse et affichage d'une info si c'est bon
        if ( (classpath.length()!=0) && (!outputDirs.isEmpty()) )
        {
            LOGGER.info( "Compiled Variables have been fulfilled." );
        }
    }
    
    /**
     * arrayStartsWith checks if pStringToCheck starts with at least one of the pPatternArray elements
     * 
     * @param pStringToCheck string to be checked
     * @param pPatternArray String[] with the list of start pattern to check
     * @return boolean true if pStringToCheck starts with any of the pPatternArray element
     */
    private boolean arrayStartsWith(String pStringToCheck ,String[] pPatternArray)
    {
        boolean output = false;
        for (int i=0 ; i < pPatternArray.length ; i++)
        {
            if (pStringToCheck.startsWith( pPatternArray[i] ))
            {
                output = true;
                break;
            }
        }
        return output;
    }

}
