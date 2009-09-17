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
package org.squale.squalix.tools.jspvolumetry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.plexus.util.DirectoryScanner;
import org.squale.jraf.commons.exception.JrafDaoException;
import org.squale.squalecommon.daolayer.result.MeasureDAOImpl;
import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.ListParameterBO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.StringParameterBO;
import org.squale.squalecommon.enterpriselayer.businessobject.result.ErrorBO;
import org.squale.squalecommon.enterpriselayer.businessobject.result.jspvolumetry.JSPVolumetryProjectBO;
import org.squale.squalix.core.AbstractTask;
import org.squale.squalix.core.TaskData;
import org.squale.squalix.core.TaskException;
import org.squale.squalix.util.process.ProcessErrorHandler;
import org.squale.squalix.util.process.ProcessManager;
import org.squale.squalix.util.process.ProcessOutputHandler;

/**
 * This class is the task for jsp metrics (number of jsp files and jsp loc)
 */
public class JSPVolumetryTask
    extends AbstractTask
    implements ProcessOutputHandler, ProcessErrorHandler
{

    /**
     * Constructeur
     */
    public JSPVolumetryTask()
    {
        mName = "JSPVolumetryTask";
    }

    /** Le processManager * */
    private ProcessManager mProcess;

    /** le nb de jsps */
    private int mNbJsps;

    /** le nombre de lignes de jsps */
    private int mNbJSPLoc;

    /**
     * Logger
     */
    private static final Log LOGGER = LogFactory.getLog( JSPVolumetryTask.class );

    /**
     * La configuration permettant d'avoir les commandes shell pour la volum�trie sur les jsps
     */
    private JSPVolumetryConfiguration mConfiguration;

    /**
     * Pr�pare l'environnement d'ex�cution de l'analyse :
     * 
     * @exception Exception si un probleme d'initialisation apparait
     */
    private void initialize()
        throws Exception
    {
        // On r�cup�re la configuration du module RSM, personnalis�e
        // avec les param�tres du projet
        mConfiguration =
            JSPVolumetryConfiguration.build( mProject, JSPVolumetryMessages.getString( "configuration.file" ),
                                             getData() );
        File workspace = mConfiguration.getWorkspace();
        workspace.mkdirs();
        // On va v�rifier que le workspace est disponible
        if ( !workspace.canWrite() || !workspace.canRead() )
        {
            throw new Exception( JSPVolumetryMessages.getString( "exception.no_workspace" ) );
        }
        LOGGER.info( JSPVolumetryMessages.getString( "logs.initialized" ) + mProject.getParent().getName() + " - "
            + mProject.getName() );
    }

    /**
     * Cr�e le ProcessManager. On ne fait pas de new mais un set pour impl�menter le pattern IOC pour pouvoir tester sur
     * un environnement windows
     * 
     * @param pArguments arguments
     * @param pDirectory r�pertoire de lancement
     * @return le ProcessManager normal
     */
    private ProcessManager createProcessManager( String[] pArguments, File pDirectory )
    {
        return new ProcessManager( pArguments, null, pDirectory );
    }

    /**
     * M�thode r�cup�rant la volum�trie pour les jsps
     * 
     * @throws TaskException en cas d'�chec
     */
    public void execute()
        throws TaskException
    {
        try
        {
            int execResult = 0;
            List jspDir = null;
            initialize();
            // chargement du nom des r�pertoires contenant les pages JSP � analyser
            ListParameterBO lListParameterBO = (ListParameterBO) ( mProject.getParameter( ParametersConstants.JSP ) );
            if ( lListParameterBO != null )
            {
                jspDir = lListParameterBO.getParameters();
            }
            String viewPath = (String) mData.getData( TaskData.VIEW_PATH );
            // ex�cution de la t�che si un r�pertoire JSP au moins a �t� d�fini par l'utilisateur
            if ( jspDir != null )
            {
                for ( int i = 0; i < jspDir.size(); i++ )
                {
                    String jspDirPath = viewPath + ( (StringParameterBO) jspDir.get( i ) ).getValue();
                    //launch the java tool to count : the number of jsp fiels and the jsp loc
                    getJspNumberAndLoc( jspDirPath );
                }
                // cr�e l'objet � persister
                JSPVolumetryProjectBO volumetry = new JSPVolumetryProjectBO();
                // positionne les champs
                volumetry.setComponent( getProject() );
                volumetry.setAudit( getAudit() );
                volumetry.setJSPsLOC( new Integer( mNbJSPLoc ) );
                volumetry.setNumberOfJSPs( new Integer( mNbJsps ) );
                // sauvegarde l'objet
                persists( volumetry );
            }
            else
            {
                throw new TaskException( "exception.missing.dir" );
            }
        }
        catch ( Exception e )
        {
            throw new TaskException( e );
        }
    }

    /**
     * This method is used to save the jsp volumetry results
     * 
     * @param volumetry l'objet � persister
     * @throws JrafDaoException en cas d'�chec de la sauvegarde des r�sultats
     */
    private void persists( JSPVolumetryProjectBO volumetry )
        throws JrafDaoException
    {
        // On ne peut pas utiliser la m�thode save (deprecated) on doit utiliser
        // saveAll donc on a besoin d'une collection
        List result = new ArrayList( 0 );
        result.add( volumetry );
        MeasureDAOImpl.getInstance().saveAll( getSession(), result );
        getSession().commitTransactionWithoutClose();
        getSession().beginTransaction();
    }

    /**
     * {@inheritDoc}
     * 
     * @param pOutputLine {@inheritDoc}
     * @see org.squale.squalix.util.process.ProcessOutputHandler#processOutput(java.lang.String)
     */
    public void processOutput( String pOutputLine )
    {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     * 
     * @param pErrorMessage {@inheritDoc}
     * @see org.squale.squalix.util.process.ProcessErrorHandler#processError(java.lang.String)
     */
    public void processError( String pErrorMessage )
    {
        // TODO Auto-generated method stub
    }
    
    /***
     * This methods is used to read all the jsp files in the specified directory,
     * to calculate and to set the number of jsp files and the jsp LOC
     * 
     * @param pJspDirPath the path for the jsp files directory
     * @throws TaskException could be thrown if the jsp directory does not exist
     */
    private void getJspNumberAndLoc(String pJspDirPath) 
        throws TaskException
    {
        //the directory scanner
        DirectoryScanner ds = new DirectoryScanner();
        //the pattern
        String [] includes = { "**" + File.separator + "*.jsp" };
        //the directory path
        File baseDir = new File( pJspDirPath );
        //Verifying if baseDir exists and is a Directory
        if ( baseDir.exists() && baseDir.isDirectory() )
        {
            //Setting the Directory Scanner parameters
            ds.setBasedir( baseDir );
            ds.setIncludes( includes );
            //launch the scan
            ds.scan();
            //read all the files gathered
            String [] filesToScan = ds.getIncludedFiles();
            for ( String file : filesToScan )
            {
                //start the counter for the jsp loc
                int count = 0;
                //the path to the file to read
                String filePath = baseDir.toString() + File.separator + file;
                try 
                {
                    FileReader fr = new FileReader( new File( filePath ) );
                    BufferedReader br = new BufferedReader( fr );
                    //counting the number of lines
                    String ligne;
                    while ( ( ligne = br.readLine() ) != null )
                    {
                        //excluding the empty lines
                        if( ligne.trim().length() > 0 )
                        {
                            count++;
                        }
                    }
                    //set the jspLoc value. If the last line is empty, it's not taken into account
                    mNbJSPLoc += count;
                    LOGGER.debug( mProject.getName() + " STATS FOR THE FILE " + filePath + " : " + count + " lines" );
                }
                catch ( IOException e )
                {
                    String message = mProject.getName() + ", " + filePath + " : " + JSPVolumetryMessages.getString( "exception.jspFile.not_exists" );
                    // Logging the error
                    LOGGER.error( message );
                    //set the status to failed as the jsp file was not found 
                    mStatus = FAILED;
                    initError( message, ErrorBO.CRITICITY_FATAL );
                }
            }
            //set the jsp number value
            mNbJsps += filesToScan.length;
        }
        else
        {
            String message = mProject.getName() + ", " + baseDir + " : " + JSPVolumetryMessages.getString( "exception.dir.not_exists" ) ;
            // Logging the error
            LOGGER.error( message );
            //set the status to failed as the directory was not correctly set 
            mStatus = FAILED;
            initError( message, ErrorBO.CRITICITY_FATAL );
        }
    }
}
