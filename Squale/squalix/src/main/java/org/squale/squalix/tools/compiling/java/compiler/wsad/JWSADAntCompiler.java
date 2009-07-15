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
package org.squale.squalix.tools.compiling.java.compiler.wsad;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;

import org.squale.squalix.tools.compiling.CompilingMessages;
import org.squale.squalix.tools.compiling.java.beans.JWSADProject;

/**
 * Compilation par une t�che ant d'un projet WSAD La compilation se fait en lan�ant une t�che ANT d�di�e � un projet
 */
public class JWSADAntCompiler
    extends Javac
{

    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactory.getLog( JWSADAntCompiler.class );

    /**
     * Projet WSAD � compiler.
     */
    private JWSADProject mProject = null;

    /**
     * Projet ANT. N�cessaire � la compilation.
     */
    private Project mAntProject = new Project();

    /**
     * Constante ".".
     */
    private static final String DOT = ".";

    /**
     * Constante "_".
     */
    private static final String UNDERSCORE = "_";

    /**
     * Encoding que le compilateur doit utiliser.
     */
    private static final String ENCODING = "ISO-8859-1";

    /**
     * Constructeur utilis� uniquement lors du traitement des d�pendances de compilation.
     * 
     * @param pProject projet WSAD.
     */
    public JWSADAntCompiler( JWSADProject pProject )
    {
        mProject = pProject;
    }

    /**
     * This method does the compilation. It uses an Ant task, Javac. <br />
     * <br />
     * When the compilation has ended, the <code>setCompiled()</code> method is called to mark the project as already
     * compiled.
     * 
     * @see org.apache.tools.ant.Project
     * @see Path
     * @see JWSADProject#setCompiled(boolean)
     * @throws Exception lance une exception en cas d'erreur lors de la compilation.
     */
    public void doCompilation()
        throws Exception
    {
        /* Igniting the Ant project. */
        super.setProject( mAntProject );
        super.getProject().init();
        // Ajout du listener pour les traces
        super.getProject().addBuildListener( mProject.getListener() );
        /*
         * Setting debug options in order to add name of source files in the byte code generated
         */
        super.setDebug( true );
        super.setDebugLevel( "source" );
        /* Setting the classpath. */
        Path path = new Path( mAntProject );
        path.setPath( mProject.getClasspath() );
        super.setClasspath( path );
        /* Setting the destination directory. */
        File fDest = new File( mProject.getDestPath() );
        if ( !fDest.exists() )
        {
            fDest.mkdir();
        }
        super.setDestdir( fDest );
        /*
         * Setting the source directory.
         */
        path = new Path( mAntProject );
        String[] paths = mProject.getSrcPath().split( ";" );
        for ( int i = 0; i < paths.length; i++ )
        {
            // On v�rifie qu'un chemin
            path.setLocation( new File( paths[i] ) );
        }
        super.setSrcdir( path );
        /*
         * Traitement des r�pertoires exclus. On cr�e les patterns associ�s et on les s�pare par ", "
         */
        if ( null != mProject.getExcludedDirs() && mProject.getExcludedDirs().size() > 0 )
        {
            String excludes = mProject.getExcludedDirs().get( 0 ) + "/**";
            for ( int i = 1; i < mProject.getExcludedDirs().size(); i++ )
            {
                excludes += ", " + mProject.getExcludedDirs().get( i ) + "/**";
            }
            // On supprime les doublons
            super.setExcludes( excludes.replaceAll( "//", "/" ) );
        }
        /* Setting the JDK version. */
        // super.setTarget(mProject.getJavaVersion().replaceAll(JWSADAntCompiler.UNDERSCORE, JWSADAntCompiler.DOT));
        super.setSource( mProject.getJavaVersion().replaceAll( JWSADAntCompiler.UNDERSCORE, JWSADAntCompiler.DOT ) );
        // le classpath des classes de l'API utilis�
        Path bootClasspath = createBootClasspath();
        super.setBootclasspath( bootClasspath );
        // TODO: en attendant que DINB installe le jdk
        super.setExecutable( CompilingMessages.getString( "javac.executable.1_5" ) );

        /* Setting the encoding. */
        super.setEncoding( JWSADAntCompiler.ENCODING );
        /*
         * Independant process : forks the JAVA compiler. The compiler instance must be forked if one is using
         * simultaneously several JWSADCompiler !
         */
        super.setFork( true );
        /* Setting required memory space */
        super.setMemoryInitialSize( mProject.getRequiredMemory() );
        super.setMemoryMaximumSize( mProject.getRequiredMemory() );
        /* En mode DEBUG, affiche les param�tres de compilation */
        printCompilingParameters();
        /* Executing the Javac Ant task. */
        super.execute();
        /* Setting the project as compiled. */
        mProject.setCompiled( true );
        LOGGER.info( "compilation r�ussie pour le projet " + mProject.getName() );
        path = null;
        fDest = null;
    }

    /**
     * Create bpath for bootclasspath option
     * 
     * @return the bootclasspath javac option
     */
    private Path createBootClasspath()
    {
        Path bootClasspath = new Path( mAntProject );
        // iterate on path to only include .jar and .zip file
        String[] bootPaths = mProject.getBootClasspath().split( ";" );
        for ( int i = 0; i < bootPaths.length; i++ )
        {
            File curBoot = new File( bootPaths[i] );
            if ( curBoot.isDirectory() )
            {
                FileSet jarAndZipFs = new FileSet();
                // Pattern for getting all .jar and .zip
                jarAndZipFs.setIncludes( "**/*.jar, **/*.zip" );
                jarAndZipFs.setDir( curBoot );
                bootClasspath.addFileset( jarAndZipFs );
            }
            else if ( curBoot.isFile() )
            {
                // We add simple file
                bootClasspath.setLocation( curBoot );
            }
            else
            {
                LOGGER.warn( "Cannot find bootclasspath lib: " + curBoot.getAbsolutePath() );
            }
        }
        return bootClasspath;
    }

    /**
     * Cette m�thode affiche les param�tres du compilateur en mode DEBUG.
     */
    private void printCompilingParameters()
    {
        LOGGER.debug( "JWSADCompiler for " + mProject.getName() );

        LOGGER.debug( "Compiler: " + super.getJavacExecutable() );
        LOGGER.debug( "Version: " + super.getCompilerVersion() );
        LOGGER.debug( "Target: " + super.getTarget() );
        LOGGER.debug( "Encoding: " + super.getEncoding() );
        LOGGER.debug( "InitialMemory: " + super.getMemoryInitialSize() );
        LOGGER.debug( "MaxMemory: " + super.getMemoryMaximumSize() );
        LOGGER.debug( "Arguments: " );
        String[] compilArgs = super.getCurrentCompilerArgs();
        int i = 0;
        while ( i < compilArgs.length )
        {
            LOGGER.debug( compilArgs[i] );
        }
    }

}
