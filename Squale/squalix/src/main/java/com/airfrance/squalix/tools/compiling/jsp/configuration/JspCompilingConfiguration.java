package com.airfrance.squalix.tools.compiling.jsp.configuration;

import java.io.InputStream;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.squalecommon.util.xml.XmlImport;
import com.airfrance.squalix.configurationmanager.ConfigUtility;
import com.airfrance.squalix.core.exception.ConfigurationException;
import com.airfrance.squalix.tools.compiling.CompilingMessages;

/**
 * Configuration pour la compilation des JSPs. La configuration de la compilation des JSPs est d�finie dans un fichier
 * XML (<code>jspcompiling-config.xml</code>), celui-ci est lu par cette classe.
 */
public class JspCompilingConfiguration
    extends XmlImport
{
    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactory.getLog( JspCompilingConfiguration.class );

    /**
     * Nom du premier package pour les jsps Ce nom peut �tre suivi du num�ro d'index+1 du r�pertoire de la liste des
     * sources JSP donn�e en param�tre de configuration si la jsp est contenue dans ce r�pertoire (si index = 0 on ne
     * met pas de num�ro)
     */
    public static final String FIRST_PACKAGE = "jsp";

    /** R�pertoire contenant les jars */
    private String mJarDirectory;

    /**
     * Constructeur par d�faut
     */
    public JspCompilingConfiguration()
    {
        super( LOGGER );
    }

    /**
     * @return le r�pertoire contenant les jars
     */
    public String getJarDirectory()
    {
        return mJarDirectory;
    }

    /**
     * @param pJarDirectory le r�pertoire contenant les jars
     */
    public void setJarDirectory( String pJarDirectory )
    {
        mJarDirectory = ConfigUtility.filterStringWithSystemProps( pJarDirectory );
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
            preSetupDigester( "-//JspCompiling Configuration DTD //EN", "/config/jspcompiling-config.dtd", errors );
        // Traitement du r�pertoire des jars
        digester.addCallMethod( "jspcompiling-configuration/jarDirectory", "setJarDirectory", 1,
                                new Class[] { String.class } );
        digester.addCallParam( "jspcompiling-configuration/jarDirectory", 0 );
        digester.push( this );
        // Appel du parser
        parse( digester, pStream, errors );
        if ( errors.length() > 0 )
        {
            throw new ConfigurationException( CompilingMessages.getString( "error.configuration",
                                                                           new Object[] { errors.toString() } ) );
        }
    }

}
