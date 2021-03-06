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
package org.squale.squalix.tools.abstractgenerictask;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.StreamConsumer;
import org.squale.jraf.commons.exception.JrafDaoException;
import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.ListParameterBO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.MapParameterBO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.StringParameterBO;
import org.squale.squalecommon.enterpriselayer.businessobject.result.ErrorBO;
import org.squale.squalix.core.AbstractTask;
import org.squale.squalix.core.TaskData;
import org.squale.squalix.core.TaskException;
import org.squale.squalix.core.exception.ConfigurationException;

/**
 * <p>
 * This abstract class provides an execution environment for any tool (jar, *.sh scripts, etc...) needed to process an
 * audit.
 * </p>
 * <p>
 * Keep in mind that this class is particularly needed when trying to connect to any tool which does not offer any Maven
 * or Ant plug-in.
 * </p>
 * <p>
 * This set of classes works a bit like a <i><b>light weight framework</b></i>.
 * </p>
 */
public abstract class AbstractGenericTask
    extends AbstractTask
{
    /** SeparatorChar from java.io.File (system-dependent) */
    public static final char SEPARATOR_CHAR = java.io.File.separatorChar;

    /** Initiating the logger */
    private static final Log LOGGER = LogFactory.getLog( AbstractGenericTask.class );

    /** Location of the tool which has to be executed */
    private StringParameterBO toolLocation;

    /**
     * Working directory of the tool launched by this generic task. This working directory allows the independence of
     * the class-loaders as the processes are not executed in the same JVM
     */
    private StringParameterBO workingDirectory;

    /**
     * This String represents the storage object which will keep in memory the user inputs. It is thus the raw input as
     * in the presentation layer
     */
    private ListParameterBO toolCommands;

    /** This collection stores URI's where the tool's results will be kept in memory after processing */
    private ListParameterBO resultFiles;

    /** Declaration of the configuration object which will be used to configure the generic task */
    private AbstractGenericTaskConfiguration configurator;

    /** Declaration of the map that will store the parameters for any GenericTask */
    private MapParameterBO genericTaskParam;

    /** Getting the viewPath of the project */
    private String viewPath;

    /**
     * Default constructor
     */
    public AbstractGenericTask()
    {
        /*
         * All task default constructors must give a name to a "mName attribute" while instantiating. This allows for
         * example automatic logging by the super class.
         */
        mName = "GenericTask";
        configurator = new AbstractGenericTaskConfiguration();
    }

    /**
     * This method initiate the GenericTask by calling a configurator and getting all the needed parameters ready
     * 
     * @return false if no configuration informations have been set
     */
    public boolean init()
    {
        /* Getting the parameters of the GenericTask by recovering the map of parameters for it */
        this.genericTaskParam = (MapParameterBO) mProject.getParameter( mName );

        /* If all values mapped in the map are null */
        boolean isConfigured = isTaskConfigured();

        if ( isConfigured )
        {
            /* Logging the beginning of the init sequence [! in debug mode only !] */
            LOGGER.debug( AbstractGenericTaskMessages.getMessage( "logs.agt.init.start", mProject.getName() ) );
            /* Setting one by one the required parameters */
            setToolLocation();
            setCommands();
            setResultLocations();
            setWorkingDirectory();
            setViewPath();
            /* Will log the main parameters values of the task if Squalix is launched in debug mode */
            printParameters();
            /* Logging the end of the init sequence */
            LOGGER.info( AbstractGenericTaskMessages.getMessage( "logs.agt.init.end", mProject.getName() ) );
        }
        else
        {
            String message = AbstractGenericTaskMessages.getMessage( "logs.agt.warn.project.notConfigured", mName );
            initError( message );
            /* if no parameters have been recovered , preparing an error message and logging it */
            LOGGER.warn( message );
            /* Cancelling the task as no parameters have been entered by the user and logging the info */
            mStatus = CANCELLED;
        }
        return isConfigured;
    }

    /**
     * This method establish if the task has been configured
     * 
     * @return true if the task has been configured
     */
    private boolean isTaskConfigured()
    {
        if ( genericTaskParam == null )
        {
            return false;
        }
        Map paramMap = genericTaskParam.getParameters();

        boolean isToolLocation =
            ( (StringParameterBO) paramMap.get( ParametersConstants.GENERICTASK_TOOLLOCATION ) ).getValue() != null;

        List resultLocationList =
            ( (ListParameterBO) genericTaskParam.getParameters().get( ParametersConstants.GENERICTASK_RESULTSLOCATIONS ) ).getParameters();
        boolean isResultLocation =
            !( resultLocationList.size() == 1 && ( (StringParameterBO) resultLocationList.get( 0 ) ).getValue() == null );

        return isToolLocation || isResultLocation;

    }

    /**
     * <p>
     * This method executes the tool. It is final so as to prevent overriding of this method by classes which extends
     * this class.
     * </p>
     * 
     * @throws TaskException exception during execution of the Task. Please consider having a look at
     *             {@link AbstractTask} for more information.
     * @throws JrafDaoException if there's a problem with the database
     */
    public final void execute()
        throws TaskException, JrafDaoException
    {
        /* Initiating the task */

        boolean isConfigured = init();

        if ( isConfigured )
        {
            /* Getting the result files from the execution of the task */
            List<File> scannedResultFiles = perform();
            /* The perform method never returns null as it instantiates an empty list with an initial capacity of zero */
            if ( scannedResultFiles.size() > 0 )
            {
                /* Calling the method implemented by tasks extending the AbstractGenericTask */
                parseResults( scannedResultFiles );
            }
            else
            {
                /* Logging an error as there are no result locations */
                String message =
                    AbstractGenericTaskMessages.getMessage( "logs.agt.error.noResultFile", mProject.getName() );
                LOGGER.error( message );
                mStatus = FAILED;
                initError( message, ErrorBO.CRITICITY_FATAL );
                /*
                 * Please keep in mind that if there are any problem encountered while executing the commandline they
                 * have been handled up stream here we are handling only exception if there are no file in the list.
                 */
            }
        }
    }

    /**
     * <p>
     * Getting the user inputs to prepare processing of Command line arguments and execution of the tool. Please note
     * that <b>the shell is auto-detected</b> by the Plexus Util classes.
     * </p>
     * <p>
     * Please keep in mind that if one or more attributes of the <b>pGenericTask</b> is/are null when calling this
     * method, it won't necessarily return <b>null</b> as the {@link AbstractGenericTask} could recover
     * informations/metrics from tool launched thanks to a Maven, Ant or any external tool.
     * </p>
     * 
     * @return {@link List} : the java.io.file object(s)
     * @throws TaskException Exception occur
     */
    private List<File> perform()
        throws TaskException
    {
        /* Collection of result files */
        List<File> results = new ArrayList<File>( 0 );
        try
        {
            int exitValue = runSpecifiedCommand();
            /* Checking if the process was executed successfully */
            if ( 0 == exitValue )
            {
                /* Preparing the result files processing */
                DirectoryScanner ds =
                    configurator.prepareResultProcessing( this.getResultLocations(), this.getViewPath() );
                /* Checking if a basedir is effectively set */
                if ( null != ds.getBasedir() )
                {
                    /* The returned array of result files */
                    String[] scannedResultFiles = null;
                    /* Scanning the directories */
                    ds.scan();
                    /* Getting the result files names */
                    scannedResultFiles = ds.getIncludedFiles();
                    for ( String resultTmpPath : scannedResultFiles )
                    {
                        /* Rebuilding the complete String value of the File that has to be created */
                        String fileTocreate = ds.getBasedir().toString() + SEPARATOR_CHAR + resultTmpPath;
                        File file = new File( fileTocreate );
                        results.add( file );
                    }
                    /*
                     * No else block as the exception has been handled in the configurator. If the baseDir is null it
                     * means that the result processing has failed up stream.
                     */
                }
            }
            else
            {
                /*
                 * the exitValue of the process is different from zero indicating an abnormal termination. Informing the
                 * user and aborting the task
                 */
                String message = AbstractGenericTaskMessages.getMessage( "logs.agt.fatal.exitValue", exitValue );
                LOGGER.fatal( message );
                mStatus = FAILED;
                initError( message, ErrorBO.CRITICITY_FATAL );
            }
        }
        catch ( CommandLineException cmdLineExcep )
        {
            /* Exception is generated by the executeCommandLine method call the task has to be cancelled */
            String message = "CommandLineException : " + cmdLineExcep;
            mStatus = FAILED;
            LOGGER.fatal( message );
            LOGGER.fatal( AbstractGenericTaskMessages.getMessage( "logs.agt.fatal.task.aborded" ) );
            initError( message, ErrorBO.CRITICITY_FATAL );
        }
        catch ( ConfigurationException confExcep )
        {
            /* Catching the configuration exception thrown by the prepareResultProcessing method */
            String message = AbstractGenericTaskMessages.getMessage( "logs.agt.error.fileSpecification" );
            mStatus = FAILED;
            LOGGER.fatal( message );
            LOGGER.fatal( AbstractGenericTaskMessages.getMessage( "logs.agt.fatal.task.aborded" ) );
            initError( message, ErrorBO.CRITICITY_FATAL );
        }
        return results;
    }

    /**
     * Run the command that the user specified.
     * 
     * @return the return code of that command
     * @throws TaskException if there's a problem while configuring the command line to execute
     * @throws CommandLineException if an error occurs while executing the command
     */
    private int runSpecifiedCommand()
        throws TaskException, CommandLineException
    {
        if ( StringUtils.isEmpty( this.getToolLocation().getValue() ) )
        {
            // no command to run, let's do as if the process was OK
            LOGGER.debug( "No command specified for execution." );
            return 0;
        }

        /* Extracting the parameters */
        String parameters = configurator.extractParameters( this.getCommands() );
        /* Preparing the command and the tool */
        Commandline cmdLine =
            configurator.prepareToolExecution( this.getToolLocation(), this.getWorkingDirectory(), parameters,
                                               this.getViewPath() );
        LOGGER.debug( "Executing command: " + cmdLine );
        /* InputStream of bytes -> output of the external tool execution */
        InputStream systemIn = null;
        /* InputStream of bytes -> input of the external tool execution */
        StreamConsumer systemOut = new OutStreamConsumer();
        /* InputStream of bytes -> error output of the external tool */
        StreamConsumer systemErr = new ErrorStreamConsumer();
        /*
         * CommandLineUtils.executeCommandLine method executes the commandLine and returns the exitValue of the process.
         * Please keep in mind that the executeCommandLine method uses the waitFor() method of the java.Lang.Process
         * class to wait for the end of the process. The I/O are redirected so as to avoid process freeze(s) due to host
         * limited buffer size.
         */
        int exitValue = CommandLineUtils.executeCommandLine( cmdLine, systemIn, systemOut, systemErr );
        return exitValue;
    }

    /**
     * <p>
     * This abstract method has to be implemented by each Task inheriting form the {@link AbstractGenericTask}.<br />
     * </p>
     * 
     * @throws TaskException Any exception occurring in the extending task have to be properly handled. If a
     *             {@link TaskException} occurs it could be thrown to the parseResults method
     * @param pResults the results provided by the execution of the task
     * @throws JrafDaoException This exception comes from the data layer
     */
    public abstract void parseResults( List<File> pResults )
        throws TaskException, JrafDaoException;

    /**
     * When launched in debug mode one could use this method to log the values listed bellow
     */
    public void printParameters()
    {
        LOGGER.debug( AbstractGenericTaskMessages.getMessage( "logs.agt.info.project.name", mProject.getName() ) );
        LOGGER.debug( AbstractGenericTaskMessages.getMessage( "logs.agt.info.task.toolLocation",
                                                              getToolLocation().getValue() ) );
        LOGGER.debug( AbstractGenericTaskMessages.getMessage( "logs.agt.info.task.workingDir",
                                                              getWorkingDirectory().getValue() ) );
    }

    /* -------------------------------------------- ACCESSORS -------------------------------------------- */

    /**
     * Getter for the mToolLocation
     * 
     * @return the location of the tool (path/to/the/tool)
     */
    public StringParameterBO getToolLocation()
    {
        return toolLocation;
    }

    /** Setter of the tool location */
    public void setToolLocation()
    {
        toolLocation =
            (StringParameterBO) genericTaskParam.getParameters().get( ParametersConstants.GENERICTASK_TOOLLOCATION );
    }

    /**
     * Getter of the working directory
     * 
     * @return the directory in which the tool will be executed
     */
    public StringParameterBO getWorkingDirectory()
    {
        return workingDirectory;
    }

    /** Setter of the working directory */
    public void setWorkingDirectory()
    {
        workingDirectory =
            (StringParameterBO) genericTaskParam.getParameters().get( ParametersConstants.GENERICTASK_WORKDIR );
    }

    /**
     * Getter of the commands the user has inputed
     * 
     * @return the commands stored in a {@link ListParameterBO}
     */
    public ListParameterBO getCommands()
    {
        return toolCommands;
    }

    /** Setter of the commands the user has inputed */
    public void setCommands()
    {
        toolCommands =
            (ListParameterBO) genericTaskParam.getParameters().get( ParametersConstants.GENERICTASK_COMMANDS );
    }

    /**
     * Getter of the location(s) of result files
     * 
     * @return a collection of location the executed tool has stored the result files
     */
    public ListParameterBO getResultLocations()
    {
        return resultFiles;
    }

    /** Setter of the location(s) of result files */
    public void setResultLocations()
    {
        resultFiles =
            (ListParameterBO) genericTaskParam.getParameters().get( ParametersConstants.GENERICTASK_RESULTSLOCATIONS );
    }

    /**
     * Getter of the configuration tool
     * 
     * @return the configurator used with the task
     */
    public AbstractGenericTaskConfiguration getTaskConfiguration()
    {
        return configurator;
    }

    /**
     * Getter of the viewPath of the project
     * 
     * @return the viewPath of the project
     */
    public String getViewPath()
    {
        return viewPath;
    }

    /**
     * Setter of the viewPath
     */
    public void setViewPath()
    {
        viewPath = mData.getData( TaskData.VIEW_PATH ).toString();
    }

    /**
     * Writer that consumes the CommandLine standard output and writes it into the LOGGER
     */
    class OutStreamConsumer
        implements StreamConsumer
    {
        public void consumeLine( String line )
        {
            LOGGER.info( line );
        }
    }

    /**
     * Writer that consumes the CommandLine error output and writes it into the LOGGER
     */
    class ErrorStreamConsumer
        implements StreamConsumer
    {
        public void consumeLine( String line )
        {
            LOGGER.error( line );
        }
    }
}
