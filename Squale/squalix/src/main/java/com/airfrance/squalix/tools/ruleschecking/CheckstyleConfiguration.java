package com.airfrance.squalix.tools.ruleschecking;

import java.io.InputStream;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.squalecommon.util.xml.XmlImport;
import com.airfrance.squalix.core.exception.ConfigurationException;

/**
 * Configuration Checkstyle La configuration Checkstyle est d�finie dans un fichier XML, celui-ci est lu par cette
 * classe.
 */
public class CheckstyleConfiguration
    extends XmlImport
{
    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactory.getLog( CheckstyleConfiguration.class );

    /** R�pertoire de g�n�ration de rapport */
    private String mReportDirectory;

    /** R�pertoire contenant les jars */
    private String mJarDirectory;

    /**
     * Constructeur
     */
    public CheckstyleConfiguration()
    {
        super( LOGGER );
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
            preSetupDigester( "-//Checkstyle Configuration DTD 1.0//EN",
                              "/com/airfrance/squalix/tools/ruleschecking/checkstyle-config-1.0.dtd", errors );
        // Traitement du r�pertoire de g�n�ration des reports
        digester.addCallMethod( "checkstyle-configuration/reportDirectory", "setReportDirectory", 1,
                                new Class[] { String.class } );
        digester.addCallParam( "checkstyle-configuration/reportDirectory", 0 );
        // Traitement du r�pertoire des jars
        digester.addCallMethod( "checkstyle-configuration/jarDirectory", "setJarDirectory", 1,
                                new Class[] { String.class } );
        digester.addCallParam( "checkstyle-configuration/jarDirectory", 0 );
        digester.push( this );
        // Appel du parser
        parse( digester, pStream, errors );
        if ( errors.length() > 0 )
        {
            throw new ConfigurationException( RulesCheckingMessages.getString( "error.configuration",
                                                                               new Object[] { errors.toString() } ) );
        }
    }

    /**
     * @return directory
     */
    public String getReportDirectory()
    {
        return mReportDirectory;
    }

    /**
     * @param pDirectory r�pertoire
     */
    public void setReportDirectory( String pDirectory )
    {
        mReportDirectory = pDirectory;
    }

    /**
     * @return r�pertoire des jars
     */
    public String getJarDirectory()
    {
        return mJarDirectory;
    }

    /**
     * @param pJarDirectory r�pertoire des jars
     */
    public void setJarDirectory( String pJarDirectory )
    {
        mJarDirectory = pJarDirectory;
    }
}
