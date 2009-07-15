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
package org.squale.squalix.tools.compiling.java;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.StringParameterBO;
import org.squale.squalix.core.TaskData;
import org.squale.squalix.core.TaskException;
import org.squale.squalix.core.exception.ConfigurationException;
import org.squale.squalix.tools.compiling.MockCompilingTask;
import org.squale.squalix.tools.compiling.CompilingMessages;
import org.squale.squalix.tools.compiling.configuration.MockCompilingConf;
import org.squale.squalix.util.file.FileUtility;

/**
 * Met � jour les param�tres temporaires li�s � la t�che de compilation Java : <br/>
 * <ul>
 * <li> CLASSES_DIR : le r�pertoire racine contenant les .class du projet
 * <li> CLASSPATH : le classpath du projet
 * </ul>
 */
public class JavaMockCompilingTask
    extends MockCompilingTask
{
    /**
     * Logger
     */
    private static final Log LOGGER = LogFactory.getLog( JavaMockCompilingTask.class );

    /** configuration de la t�che */
    private MockCompilingConf mConfiguration;

    /** Le classpath */
    private StringBuffer mClasspath;

    /**
     * Constructeur par d�faut
     */
    public JavaMockCompilingTask()
    {
        mName = "JavaMockCompilingTask";
        mConfiguration = new MockCompilingConf();
        mClasspath = new StringBuffer();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.squale.squalix.core.AbstractTask#execute()
     */
    public void execute()
        throws TaskException
    {
        try
        {
            // on ex�cute la partie commune
            super.execute();
            // on construit le classpath
            String userClasspath = mClasspath.toString();
            mClasspath = buildClasspath( userClasspath );
            // On l'ajoute aux param�tres temporaires
            mData.putData( TaskData.CLASSPATH, mClasspath.toString() );
            LOGGER.debug( "classpath = " + mClasspath.toString() );
            // positionne les donn�es sur la taille du file System
            affectFileSystemSize( mData.getData( TaskData.CLASSES_DIRS ), true );
        }
        catch ( Exception e )
        {
            throw new TaskException( e );
        }
    }

    /**
     * Il faut parcourir tout le classpath afin de mettre les chemins en absolu et v�rifier qu'ils existent
     * 
     * @param pUserClasspath le classpath que l'utilisateur a donn�
     * @return le classpath bien construit et v�rifi�
     * @throws ConfigurationException si le classpath n'est pas bien construit
     */
    private StringBuffer buildClasspath( String pUserClasspath )
        throws ConfigurationException
    {
        // On r�cup�re le view path
        String viewPath = (String) mData.getData( TaskData.VIEW_PATH );
        if ( viewPath == null )
        {
            String message = CompilingMessages.getString( "exception.view_path.not_found" );
            LOGGER.error( message );
            // Renvoi d'une exception de configuration
            throw new ConfigurationException( message );
        }
        StringBuffer classpath = new StringBuffer();
        // On s�pare les �l�ments
        String[] elements = pUserClasspath.split( ";" );
        // Pour chaque �l�ment, on construit son chemin absolu, on v�rifie qu'il existe
        // et on construit le classpath
        for ( int i = 0; i < elements.length; i++ )
        {
            if ( elements[i].length() > 0 )
            {
                File file = new File( elements[i] );
                file = FileUtility.getAbsoluteFile( viewPath, file );
                if ( file.exists() )
                {
                    classpath.append( file.getAbsolutePath() );
                    classpath.append( ";" );
                }
                else
                {
                    // On log un warning un car cela peut perturber l'ex�cution de mccabe
                    String message = CompilingMessages.getString( "exception.element_does_not_exist", elements[i] );
                    LOGGER.warn( message );
                    initError( message );
                }
            }
        }
        /*
         * on cr�e un buffer pour d�finir le chemin du dossier contenant les ressources n�cessaires � la compilation
         */
        // On r�cup�re la version de java
        StringParameterBO versionParam =
            (StringParameterBO) mProject.getParameters().getParameters().get( ParametersConstants.DIALECT );
        if ( versionParam == null )
        {
            String message = CompilingMessages.getString( "java.exception.task.dialect_not_set" );
            LOGGER.error( message );
            // Renvoi d'une exception de configuration
            throw new ConfigurationException( message );
        }
        StringBuffer path = new StringBuffer( CompilingMessages.getString( "dir.root.java" ) );
        path.append( "/" );
        path.append( versionParam.getValue() );
        path.append( "/" );
        addCompilingRessourcesToClasspath( classpath, new File( path.toString().replace( '.', '_' ) ) );
        return classpath;
    }

    /**
     * {@inheritDoc} Place le param�tre temporaire <code>CLASSES_DIR</code>
     * 
     * @see org.squale.squalix.tools.compiling.MockCompilingTask#setCompiledDirInTempMap(java.util.List)
     */
    protected void setCompiledDirInTempMap( List pDirs )
        throws TaskException
    {
        try
        {
            // R�cup�re le classpath
            StringParameterBO classpathParam =
                (StringParameterBO) mTaskParam.getParameters().get( ParametersConstants.CLASSPATH );
            if ( classpathParam == null )
            {
                String message = CompilingMessages.getString( "exception.classpath_not_set" );
                LOGGER.error( message );
                // Renvoi d'une exception de configuration
                throw new ConfigurationException( message );
            }
            // On remplace les s�parateurs unix par ceux de windows en cas
            // car c'est de cette fa�on qu'il est r�cup�r� par la t�che de compilation Java
            mClasspath = new StringBuffer( classpathParam.getValue().replace( ':', ';' ) );
            if ( mClasspath.length() > 0 && !mClasspath.toString().endsWith( ";" ) )
            {
                mClasspath.append( ";" );
            }
            // On r�cup�re le chemin absolu du r�pertoire qui va contenir
            // nos .class
            mConfiguration.parse( new FileInputStream( "config/mockcompiling-config.xml" ) );
            // On le cr�e
            File root = new File( mConfiguration.getRootDirectory() );
            root.mkdirs();
            // On copie tous les .class qui se trouvent dans la liste donn�e
            // en param�tre dans le r�pertoire racine
            for ( int i = 0; i < pDirs.size(); i++ )
            {
                StringParameterBO pathParam = (StringParameterBO) pDirs.get( i );
                String path = pathParam.getValue();
                // On enl�ve ce chemin du classpath car on va rajouter le r�pertorie global
                mClasspath = new StringBuffer( mClasspath.toString().replaceAll( path, "" ) );
                // On r�cup�re le fichier si il existe et qu'il peut �tre lu
                File compiledDir = verifyPath( new File( path ) );
                // On copie tous les .class de ce r�pertoire (ou fichier compress�)
                // dans le r�pertoire de la configuration
                if ( compiledDir.isDirectory() )
                {
                    // On copie tous les fichiers
                    File[] files = compiledDir.listFiles();
                    for ( int j = 0; j < files.length; j++ )
                    {
                        FileUtility.copyIntoDir( files[j], root );
                    }
                }
                else
                {
                    FileUtility.copyAndExtractInto( compiledDir, root );
                }
            }
            // On ajoute le r�pertoire au classpath
            mClasspath.append( root.getAbsolutePath() );
            mClasspath.append( ";" );
            // On ajoute le param�tre � la map temporaire
            List classesDirs = new ArrayList();
            classesDirs.add( root.getAbsolutePath() );
            mData.putData( TaskData.CLASSES_DIRS, classesDirs );

        }
        catch ( Exception e )
        {
            throw new TaskException( e );
        }
    }

    /**
     * @param pPath le chemin relatif ou absolu d'un r�pertoire de fichiers compil�s
     * @return le r�pertoire de fichiers compil�s si il existe et peut-�tre lu
     * @throws ConfigurationException si erreur de configuration
     */
    private File verifyPath( File pPath )
        throws ConfigurationException
    {
        File result = null;
        if ( pPath.isAbsolute() && pPath.exists() )
        {
            result = pPath;
        }
        else
        {
            // Le fichier est suppos� �tre relatif � la vue
            String viewPath = (String) getData().getData( TaskData.VIEW_PATH );
            if ( viewPath == null )
            {
                String message = CompilingMessages.getString( "exception.view_path.not_found" );
                LOGGER.error( message );
                // Renvoi d'une exception de configuration
                throw new ConfigurationException( message );
            }
            result = new File( viewPath, pPath.getPath() );
            if ( !result.exists() )
            {
                String message = CompilingMessages.getString( "error.file.not_found", result.getAbsolutePath() );
                LOGGER.error( message );
                // Renvoi d'une exception de configuration
                throw new ConfigurationException( message );
            }
        }
        // Si on a pas le droit de lecture sur le fichier, on lance une exception
        if ( !result.canRead() )
        {
            String message = CompilingMessages.getString( "error.cannot_read_file", result.getAbsolutePath() );
            LOGGER.error( message );
            // Renvoi d'une exception de configuration
            throw new ConfigurationException( message );
        }
        return result;
    }

    /**
     * Ajoute tous les .jar, .zip et .class au classpath contenu dans le r�pertoire pass� en param�tre
     * 
     * @param pClasspath le classpath
     * @param pRessource le fichier � ajouter ou le r�pertoire � parcourir
     */
    private void addCompilingRessourcesToClasspath( StringBuffer pClasspath, File pRessource )
    {
        // V�rification du classpath
        if ( pClasspath.length() > 0 && !pClasspath.toString().endsWith( ";" ) )
        {
            pClasspath.append( ";" );
        }
        // On va r�cup�rer tous les .jar, .zip ou .classes
        if ( null != pRessource && pRessource.isDirectory() && pRessource.canRead() )
        {
            File[] files = pRessource.listFiles( new ExtensionsFileFilter() );
            for ( int i = 0; i < files.length; i++ )
            {
                addCompilingRessourcesToClasspath( pClasspath, files[i] );
            }
        }
        else if ( pRessource.exists() )
        {
            // On l'ajoute directement au classpath
            pClasspath.append( pRessource.getAbsolutePath() );
        }
    }

    /**
     * Classe de filtre sur les extensions .jar, .zip et .class
     */
    private class ExtensionsFileFilter
        implements FileFilter
    {

        /** La liste des extensions */
        private List mExtensions;

        /**
         * Le constructeur par d�faut
         */
        public ExtensionsFileFilter()
        {
            mExtensions = new ArrayList();
            mExtensions.add( "jar" );
            mExtensions.add( "zip" );
            mExtensions.add( "class" );
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.io.FileFilter#accept(java.io.File)
         */
        public boolean accept( File pFile )
        {
            boolean result = pFile.isDirectory();
            if ( pFile.isFile() )
            {
                // on r�cup�re l'extension
                String fileExtension = pFile.getName();
                fileExtension = fileExtension.substring( fileExtension.lastIndexOf( "." ) + 1, fileExtension.length() );
                result = mExtensions.contains( fileExtension );
            }
            return result;
        }

    }
}
