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
import com.airfrance.squalix.util.file.FileUtility;
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
        File movedJsp;
        File renamingFile = null;
        // On compile JSP par JSP pour pouvoir maitriser le nom des sources Java g�n�r�es
        for ( int i = 0; i < mJ2eeProject.getJspPaths().length; i++ )
        {
            jspList = (List) mJ2eeProject.getJspPaths()[i][J2eeWSADProject.JSP_LIST_ID];
            for ( int j = 0; j < jspList.size(); j++ )
            {
                currentJsp = (String) jspList.get( j );
                // on construit le chemin absolu de la jsp
                currentFile = new File( currentJsp );
                // On copie la jsp � la racine du r�pertoire d'application car sinon le nom du package
                // n'est pas toujours respect� (prend aussi en compte le chemin sous le r�pertoire WebContent
                // ou tout le chemin si il n'est pas relatif au WebContent)
                jspName = currentFile.getName().replaceFirst( "\\.jsp", "" );
                movedJsp = new File( mJ2eeProject.getPath() + File.separator + jspName + ".jsp" );
                // Si un fichier existe d�j� avec ce nom dans le r�pertoire on renomme la jsp pour
                // �viter les conflits
                if ( movedJsp.exists() )
                {
                    // On renomme le fichier
                    movedJsp = new File( mJ2eeProject.getPath() + File.separator + JSP_RENAMING + jspName + ".jsp" );
                }
                // On copie la jsp
                FileUtility.copyFile( currentFile, movedJsp );

                // On r�cup�re la ligne de commande java
                java =
                    getJavaExecutable(
                                       getPackage( i, currentFile.getParent().replaceAll( "\\\\", "/" ) ),
                                       JspFileUtility.convertWithJspNameMangler( currentFile.getName().replaceAll(
                                                                                                                   "\\.jsp",
                                                                                                                   "" ) ),
                                       movedJsp );
                // On ex�cute
                java.execute();
                // On supprime la jsp cr�ee
                movedJsp.delete();
            }
        }
    }

    /**
     * @param pId l'index du r�pertoire source
     * @param pJspDir le r�pertoire parent de la jsp en cours de g�n�ration
     * @return le package � donner au .java g�n�r�
     */
    private String getPackage( int pId, String pJspDir )
    {
        // On donne un nom de package par d�faut permettant de r�soudre le r�pertoire
        // source (ex jsp -> index 0; jsp1 -> index 1) et les conflits �ventuels
        // dans les noms des jsps
        String packageName = JspCompilingConfiguration.FIRST_PACKAGE;
        if ( pId > 0 )
        {
            packageName += pId;
        }
        // On prend le chemin relatif par rapport au r�pertoire donn� dans les param�tres de configuration
        String relativePath =
            pJspDir.replaceAll( "\\\\", "/" ).replaceFirst(
                                                            ( (String) mJ2eeProject.getJspPaths()[pId][J2eeWSADProject.DIR_ID] ).replace(
                                                                                                                                          '\\',
                                                                                                                                          '/' )
                                                                + "/*", "" );
        relativePath = relativePath.trim();
        if ( relativePath.length() > 0 )
        {
            String[] packages = relativePath.split( "/" );
            for ( int i = 0; i < packages.length; i++ )
            {
                // On construit le package en convertissant le nom pour que la classe puisse compiler
                // dans le cas o� le nom serait un mot r�serv� � java ou poss�dant des caract�res interdits
                packageName += "." + JspFileUtility.convertWithJspNameMangler( packages[i] );
            }
        }
        return packageName;
    }

    /**
     * Modifie les arguments d'ex�cution du compilateur Jsp
     * 
     * @param packageName le nom du package � donner � la classe g�n�r�e
     * @param className le nom de la classe � donner au .java g�n�r�
     * @param jspFile la page JSP
     * @return l'ex�cutable java
     */
    protected Java getJavaExecutable( String packageName, String className, File jspFile )
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
        setJavaArgs( java, packageName, className, jspFile );
        return java;
    }

    /**
     * @param java l'ex�cutable
     * @param packageName le nom du package � donner � la classe g�n�r�e
     * @param className le nom de la classe � donner au .java g�n�r�
     * @param jspFile la page JSP
     */
    protected abstract void setJavaArgs( Java java, String packageName, String className, File jspFile );

    /**
     * @return le nom de la classe � ex�cuter
     */
    public String getClassname()
    {
        return COMPILER_CLASSNAME;
    }
}
