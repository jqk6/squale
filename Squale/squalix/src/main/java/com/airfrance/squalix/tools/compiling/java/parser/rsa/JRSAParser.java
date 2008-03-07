package com.airfrance.squalix.tools.compiling.java.parser.rsa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.squalix.core.exception.ConfigurationException;
import com.airfrance.squalix.tools.compiling.CompilingMessages;
import com.airfrance.squalix.tools.compiling.java.beans.JRSAProject;
import com.airfrance.squalix.tools.compiling.java.beans.JWSADProject;
import com.airfrance.squalix.tools.compiling.java.parser.wsad.JWSADParser;

/**
 * Parser de fichier <code>.classpath</code> pour RSA7
 */
public class JRSAParser
    extends JWSADParser
{

    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactory.getLog( JRSAParser.class );

    /** Mot cl� du manifest pour trouver les librairies */
    private static final String CLASS_PATH = "Class-Path:";

    /** Container faisant r�f�rence au manifest.mf du projet */
    private static final String MANIFEST_CON = "org.eclipse.jst.j2ee.internal.module.container";

    /** Container faisant r�f�rence au librairies du contenues dans le r�pertoire WEB-INF/lib */
    private static final String WEB_LIB_CON = "org.eclipse.jst.j2ee.internal.web.container";

    /** Chemin vers le fichier contenant le nom du r�pertoire Web du projet */
    private static final String WEB_SETTINGS_PATH = "/.settings/org.eclipse.wst.common.component";

    /** Nom du r�pertoire contenant les librairies d'application Web */
    private static final String WEB_LIB = "/WEB-INF/lib";

    /**
     * @param pProjectList liste de projets RSA � parser.
     * @throws Exception excpetion
     */
    public JRSAParser( List pProjectList )
        throws Exception
    {
        super( pProjectList );
        mConfiguration = new JRSAParserConfiguration();
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.airfrance.squalix.tools.compiling.java.parser.wsad.JWSADParser#mapKeyValues(com.airfrance.squalix.tools.compiling.java.beans.JWSADProject,
     *      java.lang.String, java.lang.String, boolean)
     */
    protected void mapKeyValues( JWSADProject pProject, String pKeyName, String pKeyValue, boolean pExported )
        throws Exception
    {
        // Sous RSA7, il faut g�r� le cas des connecteurs
        if ( ( (JRSAParserConfiguration) mConfiguration ).getCon().equals( pKeyName ) )
        {
            processCon( pProject, pKeyValue );
        }
        super.mapKeyValues( pProject, pKeyName, pKeyValue, pExported );
    }

    /**
     * Il y a deux connecteurs � traiter : 1. celui faisant r�f�rence au META-INF/Manifest.mf du projet (-->
     * org.eclipse.jst.j2ee.internal.module.container) 2. Celui faisant r�f�rence auw librairies sous WEB-INF/lib dans
     * le cas de projets J2EE (--> org.eclipse.jst.j2ee.internal.web.container)
     * 
     * @param pProject le projet RSA
     * @param pKeyValue la valuer de la cl�
     * @throws Exception si erreur
     */
    private void processCon( JWSADProject pProject, String pKeyValue )
        throws Exception
    {
        /* CAS 1 : META-INF/Manifest.mf */
        if ( MANIFEST_CON.equals( pKeyValue ) )
        {
            processManifestCon( pProject );
        }
        else if ( WEB_LIB_CON.equals( pKeyValue ) )
        {
            /* CAS 2 : WEB-INF/lib */
            processWebInfLibCon( pProject );
        }
    }

    /**
     * @param pProject le projet
     * @throws FileNotFoundException si erreur de fichier
     * @throws ConfigurationException si erreur de configuration
     */
    private void processWebInfLibCon( JWSADProject pProject )
        throws FileNotFoundException, ConfigurationException
    {
        // Dans ce cas on va parser le fichier org.eclipse.wst.common.component du r�pertoire .settings
        // du projet pour nous informer sur le nom du r�pertoire contenant la partie Web
        // (par d�faut WebContent)
        File webSettings = new File( pProject.getPath() + WEB_SETTINGS_PATH );
        if ( !webSettings.exists() )
        {
            LOGGER.warn( webSettings.getAbsolutePath() + " n'existe pas!!" );
        }
        else
        {
            JRSAWebSettingsParser parser = new JRSAWebSettingsParser();
            parser.parse( new FileInputStream( webSettings ) );
            if ( null != parser.getWebContentFolder() )
            {
                // Une fois le nom du r�pertoire, on ajoute les libraries au classpath
                File webLibDir = new File( pProject.getPath() + parser.getWebContentFolder() + WEB_LIB );
                if ( webLibDir.exists() && webLibDir.isDirectory() )
                {
                    File[] webLibs = webLibDir.listFiles();
                    for ( int i = 0; i < webLibs.length; i++ )
                    {
                        if ( webLibs[i].isFile() )
                        {
                            addToClasspath( pProject, webLibs[i].getAbsolutePath() );
                        }
                    }
                }
            }
        }
    }

    /**
     * @param pProject le projet
     * @throws Exception si erreur
     */
    private void processManifestCon( JWSADProject pProject )
        throws Exception
    {
        // Dans le manifest, le projet EAR n'est pas renseign�, il n'y a que les
        // noms des libraries, il faut donc que l'utilisateur ait rentrer le nom
        // de son projet EAR
        File manifest = getManifest( pProject );
        if ( null == manifest )
        {
            String message = CompilingMessages.getString( "java.warning.manifest_not_found" );
            LOGGER.error( message );
            // Renvoi d'une exception de configuration
            throw new ConfigurationException( message );
        }
        // On v�rifie que le chemin donn� par l'utilisateur existe, sinon on lance un warning
        // On r�cup�re le projet EAR associ�
        // on place le nom du projet entre "/" pour utiliser la m�thode lors d'une d�pendance WSAD
        StringBuffer earProject = new StringBuffer( "/" );
        earProject.append( ( (JRSAProject) pProject ).getEARProjectName() );
        earProject.append( "/" );
        if ( earProject.length() == 2 )
        {
            LOGGER.warn( "EAR non configur�" );
        }
        else
        {
            // On parse le manifest selon les sp�cifications
            // (http://java.sun.com/j2se/1.3/docs/guide/jar/jar.html)
            parseManifest( pProject, manifest, earProject );
        }
    }

    /**
     * Parse le manifest
     * 
     * @param pProject le projet
     * @param pManifest le manifest � parser
     * @param earProject le nom du projet EAR
     * @throws Exception si erreur
     */
    public void parseManifest( JWSADProject pProject, File pManifest, StringBuffer earProject )
        throws Exception
    {
        LOGGER.info( "On parse le manifest de " + pProject.getName() + " pour trouver les librairies EAR" );
        // Chaque ligne peut-�tre CR LF | LF | CR(not followed by LF) code ASCII 13 et 10";
        // On r�cup�re toutes les valeurs (commence par un espace et se termine par un espace)
        BufferedReader reader = new BufferedReader( new FileReader( pManifest ) );
        
        StringBuffer value = new StringBuffer();
        String line = reader.readLine() ;
        while ( null != line  && !line.startsWith( CLASS_PATH ) )
        {
            // on parse jusqu'� trouver la partie qui nous int�resse
            line = reader.readLine();
        }
        if ( null != line )
        {
            // On r�cup�re chaque lib
            line = line.replaceFirst( CLASS_PATH, "" );
            String[] libs = line.split( " +" ); // On s�pare les libs qui sont s�par�es par un ou plsr espaces
            for ( int i = 0; i < libs.length; i++ )
            {
                if ( i == libs.length - 1 && !line.endsWith( " " ) )
                {
                    value = earProject;
                    value.append( line.trim() );
                }
                else if ( libs[i].trim().length() > 0 )
                {
                    // On ajoute la valeur au classpath
                    processLib( pProject, earProject + libs[i].trim(), false );
                    value.setLength( 0 );
                }
            }
            // On parcours jusqu'� ce qu'in ligne ne commence pas par un espace
            line = reader.readLine();
            while ( null != line  && line.matches( " +.*" ) )
            {
                // Si il y a plus d'un espace en d�but de ligne
                // ou un espace sans rien derri�re
                if ( line.matches( "  +| " ) && value.length() > 0 )
                {
                    // nouvelle ligne
                    value.insert( 0, earProject );
                    processLib( pProject, value.toString(), false );
                    value.setLength( 0 );
                }
                libs = line.split( " +" ); // On s�pare les libs qui sont s�par�es par un ou plsr espaces
                for ( int i = 0; i < libs.length; i++ )
                {
                    if ( i == libs.length - 1 && !line.endsWith( " " ) )
                    {
                        value.append( libs[i].trim() );
                    }
                    else if ( libs[i].trim().length() > 0 )
                    {
                        if ( value.length() > 0 )
                        {
                            value.append( libs[i].trim() );
                        }
                        // On ajoute la valeur au classpath
                        processLib( pProject, earProject + libs[i].trim(), false );
                        value.setLength( 0 );
                    }
                }
                line = reader.readLine();
            }
        }
    }
}
