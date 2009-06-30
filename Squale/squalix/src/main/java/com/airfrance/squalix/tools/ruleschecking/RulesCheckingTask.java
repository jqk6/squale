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
package com.airfrance.squalix.tools.ruleschecking;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.squalecommon.daolayer.result.MeasureDAOImpl;
import com.airfrance.squalecommon.daolayer.rulechecking.CheckstyleRuleSetDAOImpl;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ListParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.StringParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.rulechecking.CheckstyleTransgressionBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking.checkstyle.CheckstyleRuleSetBO;
import com.airfrance.squalecommon.util.file.FileUtility;
import com.airfrance.squalix.core.AbstractTask;
import com.airfrance.squalix.core.TaskData;
import com.airfrance.squalix.core.TaskException;
import com.airfrance.squalix.core.exception.ConfigurationException;
import com.airfrance.squalix.util.buildpath.BuildProjectPath;

/**
 * Ex�cute le rulesChecking sur un sous-projet grace au connecteur rulesChecking<code>rulesCheckingFacade<code>.<br>
 * Si aucun fichier de configuration rulesChecking n'est sp�cifier dans les param�tres du sous projet,<br>
 * elle prend celui par d�faut(stock� sur le serveur)
 * 
 * @author sportorico
 */

public class RulesCheckingTask
    extends AbstractTask
{

    /**
     * Logger
     */
    private static final Log LOGGER = LogFactory.getLog( RulesCheckingTask.class );

    /**
     * Constructeur par defaut
     */
    public RulesCheckingTask()
    {
        mName = "RulesCheckingTask";
    }

    /**
     * L'analyse compl�te consiste en : <ul> <li>lancement du connecteur rulesChecking</li> <li>recup�ration des
     * resultats g�ner�s par rulesChecking</li> <li>Persistance des beans</li> </ul>
     * 
     * @throws RulesCheckingException Si un probl�me d'ex�cution appara�t.
     * @throws JrafDaoException Si un probl�me d'ex�cution appara�t.
     * @throws IOException Si un probl�me d'ex�cution appara�t.
     * @throws RulesCheckingConnectorException Si un probl�me d'ex�cution appara�t.
     * @throws ConfigurationException si erreur
     * @throws FileNotFoundException si erreur
     */
    private void analyze()
        throws JrafDaoException, RulesCheckingException, FileNotFoundException, IOException,
        RulesCheckingConnectorException, ConfigurationException
    {
        LOGGER.info( RulesCheckingMessages.getString( "logs.analyzing" ) + mProject.getParent().getName() + " - "
            + mProject.getName() );
        // On r�cup�re le nom du ruleset � appliquer
        StringParameterBO param =
            (StringParameterBO) getProject().getParameter( ParametersConstants.CHECKSTYLE_RULESET_NAME );
        if ( param == null )
        {
            String message = RulesCheckingMessages.getString( "exception.rulesChecking.parameter.missing" );
            // On affiche un warning sans lancer d'exception, la t�che ne sera pas ex�cut�e.
            initError( message );
            LOGGER.warn( message );
            // Les param�tres sont mal configur�s, on annule la t�che
            mStatus = CANCELLED;
        }
        else
        {
            CheckstyleRuleSetBO versionBo =
                CheckstyleRuleSetDAOImpl.getInstance().getLastVersion( getSession(), param.getValue() );
            // On recup�re la version du fichier de configuration checkstyle r�ferentile � utiliser
            // Cas peu probable si la configuration est mal faite
            if ( versionBo == null )
            {
                throw new RulesCheckingException(
                                                  RulesCheckingMessages.getString( "exception.rulesChecking.version.unfound" ) );
            }
            // On r�cup�re la version de java
            StringParameterBO javaVersion = (StringParameterBO) getProject().getParameter( ParametersConstants.DIALECT );
            if ( javaVersion == null )
            {
                throw new RulesCheckingException(
                                                  RulesCheckingMessages.getString( "exception.rulesChecking.dialect.unfound" ) );
            }
            // On ne calcule pas les transgressions si aucune r�gle n'est d�finie
            if ( versionBo.getRules().size() > 0 )
            {
                CheckstyleTransgressionBO transgression = ckeck( versionBo, javaVersion.getValue() );
                if ( null != transgression )
                {
                    transgression.setAudit( getAudit() );
                    transgression.setTaskName( getName() );
                    transgression.setComponent( getProject() );
                    MeasureDAOImpl.getInstance().create( getSession(), transgression );
                }
                else
                {
                    throw new RulesCheckingException( RulesCheckingMessages.getString( "exception.during.audit" ) );
                }
            }
        }
    }

    /**
     * Recup�re les param�tres checkstyle du projet; Fait appel au connecteur et recup�re l'ensemble des violations
     * g�n�r�es par le connector checkstyle.
     * 
     * @param pVersion La version du fichier de configuration
     * @param pJavaVersion la version de java
     * @return la liste des transgressions
     * @throws IOException si l'analyse ne s'est pas d�roul�e correctement
     * @throws RulesCheckingConnectorException si l'analyse ne s'est pas d�roul�e correctement
     * @throws ConfigurationException si erreur
     * @throws FileNotFoundException si erreur
     */
    public CheckstyleTransgressionBO ckeck( CheckstyleRuleSetBO pVersion, String pJavaVersion )
        throws IOException, RulesCheckingConnectorException, FileNotFoundException, ConfigurationException
    {
        /*
         * Permet de pr�ciser le pays et le language de l'ordi ... cela permet de contourner un probl�me de messages
         * avec Checkstyle 3.5
         */
        Locale bufferLocal = Locale.getDefault(); // memoriser la valeur courante
        try
        {
            Locale.setDefault( Locale.US ); // changer cette valeur avec celle des USA

            File file = FileUtility.byteToFile( pVersion.getValue() );
            // Configuration Checkstyle
            CheckstyleConfiguration config = new CheckstyleConfiguration();
            config.parse( new FileInputStream( "config/checkstyle-config.xml" ) );
            // r�cup�ration des sources du project
            List srcs =
                ( (ListParameterBO) mProject.getParameters().getParameters().get( ParametersConstants.SOURCES ) ).getParameters();
            List paths = BuildProjectPath.buildProjectPath( (String) mData.getData( TaskData.VIEW_PATH ), srcs );
            // On r�cup�re les sources qui peuvent �tre analys�es
            List includedFileNames =
                com.airfrance.squalix.util.file.FileUtility.getIncludedFiles(
                                                                              (String) mData.getData( TaskData.VIEW_PATH ),
                                                                              paths,
                                                                              (ListParameterBO) mProject.getParameter( ParametersConstants.INCLUDED_PATTERNS ),
                                                                              (ListParameterBO) mProject.getParameter( ParametersConstants.EXCLUDED_PATTERNS ),
                                                                              null, new String[] { ".java" } );

            File tempDir = new File( config.getTempSourceDir() );
            tempDir.mkdir();

            for ( Object fl : includedFileNames )
            {
                File fileToCopy = new File( fl.toString() );
                File vPath = new File( (String) mData.getData( TaskData.VIEW_PATH ) );
                String path = fileToCopy.getCanonicalPath().replace( vPath.getCanonicalPath(), tempDir.getCanonicalPath() );
                File destFile = new File( path );
                FileUtils.copyFile( fileToCopy, destFile );
            }

            CheckStyleProcess process =
                new CheckStyleProcess( new File( config.getJarDirectory() ), new File( config.getReportDirectory() ),
                                       pJavaVersion );
            File report = process.analyseSources( file, tempDir, "checkstyle-report" + getProject().getId() );
            CheckstyleReportParser parser = new CheckstyleReportParser( tempDir.getAbsolutePath() );
            CheckStylePersistor persistor = new CheckStylePersistor( pVersion );
            parser.parse( new FileInputStream( report ), persistor );
            // On r�cup�re le transgression
            CheckstyleTransgressionBO transgression = persistor.computeTransgression();

            // positionne les donn�es sur la taille du file System
            ArrayList<Object[]> listDirectory = new ArrayList<Object[]>();
            Object[] obj1 = { config.getReportDirectory(), Boolean.FALSE };
            Object[] obj2 = { tempDir, Boolean.FALSE };
            listDirectory.add( obj1 );
            listDirectory.add( obj2 );
            affectFileSystemSize( listDirectory );

            // Destruction du rapport
            report.delete();
            FileUtils.deleteDirectory( tempDir );
            return transgression;
        }
        finally
        {
            Locale.setDefault( bufferLocal ); // remettre � la valeur initiale
        }
    }

    /**
     * Ex�cute la m�thode init(), puis une analyse compl�te, et enfin la m�thode close().
     * 
     * @throws TaskException en cas de probl�mes li�s � la base
     */
    public void execute()
        throws TaskException
    {
        try
        {
            analyze();
        }
        catch ( Exception e )
        {
            throw new TaskException( e );
        }
    }

    /**
     * Acces au nom de la t�che CheckStyleTask
     * 
     * @return le nom de la t�che
     */
    public String getName()
    {
        return mName;
    }

}
