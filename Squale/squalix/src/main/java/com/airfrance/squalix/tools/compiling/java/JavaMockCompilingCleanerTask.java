package com.airfrance.squalix.tools.compiling.java;

import java.io.File;
import java.io.FileInputStream;

import com.airfrance.squalix.core.AbstractTask;
import com.airfrance.squalix.core.TaskException;
import com.airfrance.squalix.tools.compiling.configuration.MockCompilingConf;
import com.airfrance.squalix.util.file.FileUtility;

/**
 * Supprime le r�pertoire de compilation des sources
 */
public class JavaMockCompilingCleanerTask
    extends AbstractTask
{

    /**
     * Constructeur
     */
    public JavaMockCompilingCleanerTask()
    {
        mName = "JavaMockCompilingCleanerTask";
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.airfrance.squalix.core.AbstractTask#execute()
     */
    public void execute()
        throws TaskException
    {
        try
        {
            // On r�cup�re la configuration de la t�che
            MockCompilingConf conf = new MockCompilingConf();
            conf.parse( new FileInputStream( "config/mockcompiling-config.xml" ) );
            // On supprime le r�pertoire
            File dir = new File( conf.getRootDirectory() );
            FileUtility.deleteRecursively( dir );
        }
        catch ( Exception e )
        {
            throw new TaskException( e );
        }
    }
}
