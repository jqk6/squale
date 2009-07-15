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
package com.airfrance.squalix.tools.jspvolumetry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.squalecommon.daolayer.result.MeasureDAOImpl;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ListParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.StringParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.jspvolumetry.JSPVolumetryProjectBO;
import com.airfrance.squalix.core.AbstractTask;
import com.airfrance.squalix.core.TaskData;
import com.airfrance.squalix.core.TaskException;
import com.airfrance.squalix.util.process.ProcessErrorHandler;
import com.airfrance.squalix.util.process.ProcessManager;
import com.airfrance.squalix.util.process.ProcessOutputHandler;

/**
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
    private int mNbJsps = 0;

    /** le nombre de lignes de jsps */
    private int mNbJSPLoc = 0;

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
            // String nbJspsCommand = mConfiguration.getNbJspsCommand();
            // String nbJspsLocCommand = mConfiguration.getNbJspsLocCommand();
            File script = new File( mConfiguration.getScriptPath() );
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
                    // ex�cute le script
                    execResult =
                        process( new String[] { script.getAbsolutePath(), jspDirPath,
                            mConfiguration.getResultFilePath() } );
                    // Les r�sultats sont inscrits dans le fichier de r�sultats
                    parseFile();
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
     * Parse le fichier pour r�cup�rer les r�sultats
     * 
     * @throws NumberFormatException si le r�sultat n'est pas un entier
     * @throws IOException si le fichier n'est pas trouv� ou si la lecture des r�sultats se passe mal
     * @throws TaskException en cas d'�chec de r�cup�ration des r�sultats
     */
    private void parseFile()
        throws NumberFormatException, IOException, TaskException
    {
        File resultFile = mConfiguration.getResultFile();
        if ( resultFile.exists() )
        {
            BufferedReader br = new BufferedReader( new FileReader( resultFile ) );
            // on ajoute les valeurs obtenus sur les diff�rents r�pertoires
            // contenant les jsps fourni
            mNbJsps += Integer.parseInt( br.readLine().trim() );
            mNbJSPLoc += Integer.parseInt( br.readLine().trim() );
            br.close();
            // delete file
            resultFile.delete();
        }
        else
        {
            throw new TaskException( "exception.resultFile.not_exists" );
        }

    }

    /**
     * @param pCommand la commande a ex�cuter
     * @return le r�sultat de l'ex�cution de la commande
     * @throws IOException en cas d'�che de lecture
     * @throws InterruptedException si le processus est uinterrompu anormalement
     */
    private int process( String[] pCommand )
        throws IOException, InterruptedException
    {
        mProcess = createProcessManager( pCommand, mConfiguration.getWorkspace() );
        // On veut g�rer les informations lanc�es par le processus en sortie
        mProcess.setOutputHandler( this );
        // On cherche � avoir un comportement synchrone pour �tre s�r de ne pas
        // avoir un �tat des donn�es incoh�rent
        return mProcess.startProcess( this );
    }

    /**
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
     * @see com.airfrance.squalix.util.process.ProcessOutputHandler#processOutput(java.lang.String)
     */
    public void processOutput( String pOutputLine )
    {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     * 
     * @param pErrorMessage {@inheritDoc}
     * @see com.airfrance.squalix.util.process.ProcessErrorHandler#processError(java.lang.String)
     */
    public void processError( String pErrorMessage )
    {
        // TODO Auto-generated method stub
    }
}
