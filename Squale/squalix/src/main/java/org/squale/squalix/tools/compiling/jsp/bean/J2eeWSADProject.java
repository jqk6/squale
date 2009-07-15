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
package com.airfrance.squalix.tools.compiling.jsp.bean;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.apache.tools.ant.BuildListener;

import com.airfrance.squalix.tools.compiling.java.beans.JWSADProject;
import com.airfrance.squalix.util.file.ExtensionFileFilter;
import com.airfrance.squalix.util.file.FileUtility;

/**
 * Projet J2EE sous WSAD
 */
public class J2eeWSADProject
    extends JWSADProject
{

    /** Extension des fichiers jars */
    public static final String JAR_FILE_EXTENSION = ".jar";

    /** Le nom par d�faut du r�pertoire destin� � recevoir les .java des JSPs compil�es */
    public static final String JAVA_DEST = "jspToJava";

    /** Le nom par d�faut du r�pertoire contenant les .class des jsps */
    public static final String CLASSES_DEST = JAVA_DEST + "/jsp_classes";

    /** nombre d'index de niveau 2 dans le tableau des chemins vers les jsps */
    public static final int NB_ID = 2;

    /** index dans le tableau des chemins vers les jsps repr�sentant le chemin vers le r�pertoire source */
    public static final int DIR_ID = 0;

    /** index dans le tableau des chemins vers les jsps repr�sentant la liste des jsps � compiler */
    public static final int JSP_LIST_ID = 1;

    /** Le classpath dont le s�parateur est ";" */
    private String mClasspath;

    /**
     * Map between generated .java and .jsp path ex: p1.p2.myClass -> /myPath/p1/p2/myClass.jsp
     */
    protected Map mGeneratedClassesName = new HashMap();

    /**
     * Chemins vers les JSPs de la forme mJspPaths[i][0] -> chemin absolu du r�pertoire source jsp d'index i dans les
     * param�tres de configuration mJspPaths[i][1] -> liste des .jsp � compiler du r�pertoire mJspPaths[i][0]
     */
    private Object[][] mJspPaths;

    /** Le r�pertoire detin� � recevoir les .java des JSPs compil�es */
    private String mJspDestPath;

    /** L'�couteur */
    private BuildListener mListener;

    /**
     * Constructeur par d�faut
     */
    public J2eeWSADProject()
    {
        mJspPaths = new Object[0][0];
        mJspDestPath = JAVA_DEST;
    }

    /**
     * Add a link
     * 
     * @param fullClassName full java className
     * @param absolutePath jsp absolute path
     */
    public void addGeneratedClasseName( String fullClassName, String absolutePath )
    {
        mGeneratedClassesName.put( fullClassName, absolutePath );
    }

    /**
     * @return les chemins vers les JSPs
     */
    public Object[][] getJspPaths()
    {
        return mJspPaths;
    }

    /**
     * @param pJspPaths les chemins vers les JSPs
     */
    public void setJspPaths( Object[][] pJspPaths )
    {
        mJspPaths = pJspPaths;
    }

    /**
     * @return le r�pertoire de destination
     */
    public String getJspDestPath()
    {
        return mJspDestPath;
    }

    /**
     * @param pJspDestPath le r�pertoire de destination
     */
    public void setJspDestPath( String pJspDestPath )
    {
        mJspDestPath = pJspDestPath;
    }

    /**
     * @return le classpath du projet
     */
    public String getClasspath()
    {
        return mClasspath;
    }

    /**
     * @param pClasspath le classpath du projet
     */
    public void setClasspath( String pClasspath )
    {
        mClasspath = pClasspath;
    }

    /**
     * @return l'�couteur
     */
    public BuildListener getListener()
    {
        return mListener;
    }

    /**
     * @param pListener l'�couteur
     */
    public void setListener( BuildListener pListener )
    {
        mListener = pListener;
    }

    /**
     * Ajoute les jars contenus dans le r�pertoire en d�but de classpath <code>pDir</code>
     * 
     * @param pDir le r�pertoire contenant les jars
     */
    public void addJarDirToClasspath( File pDir )
    {
        HashSet jars = new HashSet();
        FileUtility.createRecursiveListOfFiles( pDir, new ExtensionFileFilter( JAR_FILE_EXTENSION ), jars );
        for ( Iterator it = jars.iterator(); it.hasNext(); )
        {
            String jar = (String) it.next();
            mClasspath = jar + File.pathSeparator + mClasspath;
        }
    }

    /**
     * @return map between generated .java and .jsp path
     */
    public Map getGeneratedClassesName()
    {
        return mGeneratedClassesName;
    }
}
