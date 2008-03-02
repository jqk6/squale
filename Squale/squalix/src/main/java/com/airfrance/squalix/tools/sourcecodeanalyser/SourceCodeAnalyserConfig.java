package com.airfrance.squalix.tools.sourcecodeanalyser;

import java.io.InputStream;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.squalecommon.util.xml.XmlImport;
import com.airfrance.squalix.core.exception.ConfigurationException;

/**
 * Configuration pour la r�cup�ration des sources via une arborescence de fichiers. La configuration associ�e est
 * d�finie dans un fichier XML (<code>sourcecodeanalyser-config.xml</code>), celui-ci est lu par cette classe.
 */
public class SourceCodeAnalyserConfig
    extends XmlImport
{
    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactory.getLog( SourceCodeAnalyserConfig.class );

    /** R�pertoire racine */
    private String mRootDirectory;

    /**
     * Constructeur par d�faut
     */
    public SourceCodeAnalyserConfig()
    {
        super( LOGGER );
    }

    /**
     * @return le r�pertoire racine
     */
    public String getRootDirectory()
    {
        return mRootDirectory;
    }

    /**
     * @param pRootDirectory le r�pertoire racine
     */
    public void setRootDirectory( String pRootDirectory )
    {
        // On ajoute �ventuellement un / � la fin si il n'est
        // pas d�j� pr�sent
        if ( !pRootDirectory.endsWith( "/" ) )
        {
            pRootDirectory += "/";
        }
        mRootDirectory = pRootDirectory;
    }

    /**
     * Lecture du fichier de configuration
     * 
     * @param pStream flux
     * @throws ConfigurationException si erreur
     */
    public void parse( InputStream pStream )
        throws ConfigurationException
    {
        StringBuffer errors = new StringBuffer();
        Digester digester =
            preSetupDigester( "-//SourceCodeAnalyser Configuration DTD //EN",
                              "/config/sourcecodeanalyser-config-1.0.dtd", errors );
        // Traitement du r�pertoire racine
        digester.addCallMethod( "sourcecodeanalyser-configuration/rootPath", "setRootDirectory", 1,
                                new Class[] { String.class } );
        digester.addCallParam( "sourcecodeanalyser-configuration/rootPath", 0 );
        digester.push( this );
        // Appel du parser
        parse( digester, pStream, errors );
        if ( errors.length() > 0 )
        {
            throw new ConfigurationException( SourceCodeAnalyserMessages.getString( "exception.configuration",
                                                                                    new Object[] { errors.toString() } ) );
        }
    }

}
