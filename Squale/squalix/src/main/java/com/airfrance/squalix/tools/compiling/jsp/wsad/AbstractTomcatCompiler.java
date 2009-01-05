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
package com.airfrance.squalix.tools.compiling.jsp.wsad;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Java;
import org.apache.tools.ant.types.Path;

import com.airfrance.squalix.tools.compiling.CompilingMessages;
import com.airfrance.squalix.tools.compiling.jsp.bean.J2eeWSADProject;
import com.airfrance.squalix.tools.compiling.jsp.configuration.JspCompilingConfiguration;
import com.airfrance.squalix.util.file.JspFileUtility;

/**
 * Classe abstraite du compilateur Jsp
 */
public abstract class AbstractTomcatCompiler
{

    /** Nom de la classe par d�faut � ex�cuter pour compiler les JSPs */
    public static final String COMPILER_CLASSNAME = "org.apache.jasper.JspC";

    /** Le nom du r�pertoire WEB-INF */
    public static final String WEB_INF_DIRNAME = "WEB-INF";

    /** Le nom � ajouter aux jsps qui existent d�j� dans le WebContent lors de la copie des jsps */
    public static final String JSP_RENAMING = "renamingSquale_";

    /**
     * Projet Ant
     */
    private Project antProject = new Project();

    /**
     * Projet J2ee WSAD � compiler.
     */
    protected J2eeWSADProject mJ2eeProject = null;

    /**
     * Constructeur
     * 
     * @param pProject le projet wsad
     */
    public AbstractTomcatCompiler( J2eeWSADProject pProject )
    {
        mJ2eeProject = pProject;
        antProject.init();
    }

    /**
     * Compile les JSPs du projet <code>mJ2eeProject</code> Les jsps contenues dans les r�pertoires dont le nom
     * contient des caract�res sp�ciaux (#, �,...) seront ignor�es de la compilation car Java ne reconnait pas certains
     * caract�res pour les noms de packages.
     * 
     * @throws BuildException si erreur lors de la compilation
     * @throws IOException si erreur au niveau du traitement des fichiers
     */
    public void compileJsp()
        throws BuildException, IOException
    {
        Java java = null;
        List jspList;
        String currentJsp;
        String jspName;
        File currentFile;
        // On compile JSP par JSP pour pouvoir maitriser le nom des sources Java g�n�r�es
        for ( int i = 0; i < mJ2eeProject.getJspPaths().length; i++ )
        {
            jspList = (List) mJ2eeProject.getJspPaths()[i][J2eeWSADProject.JSP_LIST_ID];
            String rootPackage = getPackage( i );
            for ( int j = 0; j < jspList.size(); j++ )
            {
                currentJsp = ( (String) jspList.get( j ) ).replaceAll( "\\\\", "/" );
                // on construit le chemin absolu de la jsp
                currentFile = new File( currentJsp );

                // On r�cup�re la ligne de commande java
                java = getJavaExecutable( rootPackage, currentFile );

                // On ex�cute
                java.execute();
                // On map le nom de la classe g�n�r�e avec son fichier jsp
                mJ2eeProject.addGeneratedClasseName( JspFileUtility.getJspFullClassName( rootPackage,
                                                                                         getJspUri( currentJsp ) ),
                                                     currentJsp );
            }
        }
    }

    /**
     * Build relative jsp path
     * 
     * @param jspPath absolute jsp path
     * @return relative jsp path
     */
    private String getJspUri( String jspPath )
    {
        String result = jspPath;
        if ( jspPath.startsWith( mJ2eeProject.getPath() ) )
        {
            result = jspPath.replaceFirst( mJ2eeProject.getPath().replaceAll( "\\\\", "/" ) + "/*", "" );
        }
        if ( result.startsWith( "/" ) )
        {
            result = result.substring( 1 );
        }
        return result;
    }

    /**
     * @param pId l'index du r�pertoire source
     * @return le package root � donner au .java g�n�r�
     */
    private String getPackage( int pId )
    {
        // On donne un nom de package par d�faut permettant de r�soudre le r�pertoire
        // source (ex jsp -> index 0; jsp1 -> index 1) et les conflits �ventuels
        // dans les noms des jsps
        String packageName = JspCompilingConfiguration.FIRST_PACKAGE;
        if ( pId > 0 )
        {
            packageName += pId;
        }
        return packageName;
    }

    /**
     * Modifie les arguments d'ex�cution du compilateur Jsp
     * 
     * @param packageName le nom du package � donner � la classe g�n�r�e
     * @param jspFile la page JSP
     * @return l'ex�cutable java
     */
    protected Java getJavaExecutable( String packageName, File jspFile )
    {
        Java java = (Java) ( antProject.createTask( "java" ) );
        antProject.addBuildListener( mJ2eeProject.getListener() );
        Path classpath = new Path( antProject, mJ2eeProject.getClasspath() );
        // On fork l'ex�cution
        java.setFork( true );
        // JVM � utiliser
        // TODO: en attendant que DINB installe le jdk
        java.setJvm( CompilingMessages.getString( "java.executable.1_5" ) );
        // Le classpath
        java.setClasspath( classpath );
        // Le nom de la classe � ex�cuter
        java.setClassname( getClassname() );
        setJavaArgs( java, packageName, jspFile );
        return java;
    }

    /**
     * @param java l'ex�cutable
     * @param packageName le nom du package � donner � la classe g�n�r�e
     * @param jspFile la page JSP
     */
    protected abstract void setJavaArgs( Java java, String packageName, File jspFile );

    /**
     * @return le nom de la classe � ex�cuter
     */
    public String getClassname()
    {
        return COMPILER_CLASSNAME;
    }
}
