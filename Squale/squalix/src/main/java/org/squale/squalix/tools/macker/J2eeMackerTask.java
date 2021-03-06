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
package org.squale.squalix.tools.macker;

import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.innig.macker.Macker;

import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.ListParameterBO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import org.squale.squalix.core.TaskData;
import org.squale.squalix.core.TaskException;
import org.squale.squalix.util.buildpath.BuildProjectPath;
import org.squale.squalix.util.file.FileUtility;

/**
 * T�che Macker pour un projet J2EE
 */
public class J2eeMackerTask
    extends MackerTask
{

    /**
     * Logger
     */
    private static final Log LOGGER = LogFactory.getLog( J2eeMackerTask.class );

    /** L'extension d'un fichier java */
    public static final String JAR_FILE_EXTENSION = ".jar";

    /** Constructeur par d�faut */
    public J2eeMackerTask()
    {
        super();
        mName = "J2eeMackerTask";
    }

    /**
     * Analyse les fichiers compil�s afin de relever pour chacun les transgressions des r�gles concernant
     * l'architecture.
     * 
     * @throws TaskException si erreur
     */
    public void execute()
        throws TaskException
    {
        // Si a t�che n'a pas �t� annul�e, on va analyser le code source (java et jsp)
        // On modifie la configuration pour charger les jsps � analyser
        // et r�cup�rer les chemins vers les sources jsps r�elles du projet
        if ( init() && initJsp() )
        {
            Macker macker;
            try
            {
                macker = configMacker( mConfiguration.getFilesToAnalyze(), mConfiguration.getConfigFile() );
                // On modifie le classpath pour ex�cuter Macker afin d'avoir le build classpath pour la r�solution
                // de l'ensemble des classes.
                macker.setClassLoader( new URLClassLoader( getClasspathURLs() ) );
                /* On lance l'analyse en attachant notre �v�nement � Macker: */
                mListener =
                    new J2eeStorageListener( getSession(), mProject, mConfiguration,
                                             (Map) getData().getData( TaskData.JSP_MAP_NAMES ) );
                macker.addListener( mListener );
                macker.checkRaw();
                // On fait persister les mesures
                persisteMeasures( mListener.getNbOcc(), mListener.getDetails(), mConfiguration.getRuleSet() );
            }
            catch ( Exception e )
            {
                throw new TaskException( e );
            }
        }
    }

    /**
     * Modifie la configuration pour l'analyse des pages JSPs
     * 
     * @throws TaskException si erreur
     * @return true si l'analyse des pages JSPs est possible
     */
    private boolean initJsp()
        throws TaskException
    {
        boolean can = true;
        // On r�cup�re les chemins relatifs des r�pertoires contenant les .jsp du projet
        ListParameterBO jsps = (ListParameterBO) mProject.getParameter( ParametersConstants.JSP );
        if ( null == jsps )
        {
            String message = MackerMessages.getString( "macker.jsps_not_found" );
            // On affiche un info sans lancer d'exception, l'analyse des JSPs ne sera pas ex�cut�e.
            initError( message );
            LOGGER.info( message );
            can = false;
        }
        else
        {
            mConfiguration.setJsps( BuildProjectPath.buildProjectPath( mConfiguration.getRoot(), jsps.getParameters() ) );
            // On r�cup�re le r�pertoire contenant les .class des JSPs du projet � analyser
            // cr�e par la t�che de compilation des jsps
            String classesDir = (String) this.getData().getData( TaskData.JSP_CLASSES_DIR );
            if ( classesDir == null )
            {
                String message =
                    MackerMessages.getString( "macker.exception.jsp_class_dir_not_found" ) + TaskData.JSP_CLASSES_DIR;
                LOGGER.error( message );
                // Lance une exception de configuration
                throw new TaskException( message );
            }
            // On g�n�re la liste des fichiers compil�s � analyser
            mConfiguration.setJspRoot( classesDir );
            mConfiguration.getFilesToAnalyze().addAll( mConfiguration.getFilesToAnalyze( classesDir ) );

            // Les nom des fichiers qui peuvent �tre persist�s
            List includedFileNames =
                FileUtility.getIncludedFiles(
                                              mConfiguration.getRoot(),
                                              mConfiguration.getJsps(),
                                              (ListParameterBO) mProject.getParameter( ParametersConstants.INCLUDED_PATTERNS ),
                                              (ListParameterBO) mProject.getParameter( ParametersConstants.EXCLUDED_PATTERNS ),
                                              null, new String[] { ".java", ".jsp" } );
            // On ajoute les fichiers possibles � la liste des fichiers
            mConfiguration.getIncludedFiles().addAll( includedFileNames );

        }
        return can;
    }
}
