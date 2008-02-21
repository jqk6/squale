package com.airfrance.squalix.tools.compiling.jsp.wsad;

import java.io.File;

import org.apache.tools.ant.taskdefs.Java;

import com.airfrance.squalix.tools.compiling.jsp.bean.J2eeWSADProject;

/**
 * Compilation des JSPs avec Tomcat 5 pour les sp�cifications servlet 2.4
 * (Test� avec tomcat 5.5.25)
 * Test� avec tomcat 3.3.2, 4.1.36, 5.5.25 et 6.0.14
 */
public class JWSADJspTomcat2_4Compiler extends AbstractTomcatCompiler {

    /**
     * @param pProject le projet
     */
    public JWSADJspTomcat2_4Compiler(J2eeWSADProject pProject) {
        super(pProject);
    }

    /** 
     * {@inheritDoc}
     * @see com.airfrance.squalix.tools.compiling.jsp.wsad.AbstractTomcatCompiler#setJavaArgs(org.apache.tools.ant.taskdefs.Java, java.lang.String, java.lang.String, java.io.File)
     */
    protected void setJavaArgs(Java java, String packageName, String className, File jspFile) {
        // Version des sources
        java.createArg().setValue("-source");
        java.createArg().setValue(mJ2eeProject.getJavaVersion());
        // Version des sources g�n�r�es
        java.createArg().setValue("-target");
        java.createArg().setValue(mJ2eeProject.getJavaVersion());
        // R�pertoire de destination
        java.createArg().setValue("-d");
        java.createArg().setValue(mJ2eeProject.getJspDestPath());
        // Package : On indique un nom de package g�n�r�
        java.createArg().setValue("-p");
        java.createArg().setValue(packageName);
        // Package : On indique un nom de la classe g�n�r�e
        java.createArg().setValue("-c");
        java.createArg().setValue(className);
        // On indique le chemin racine vers l'application web
        java.createArg().setValue("-webapp");
        java.createArg().setValue(mJ2eeProject.getPath());
        // La jsp � g�n�rer
        java.createArg().setValue(jspFile.getAbsolutePath());
    }

}
