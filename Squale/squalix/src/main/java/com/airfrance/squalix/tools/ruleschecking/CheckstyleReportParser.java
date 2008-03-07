package com.airfrance.squalix.tools.ruleschecking;

import java.io.InputStream;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.squalecommon.util.xml.XmlImport;
import com.airfrance.squalix.core.exception.ConfigurationException;

/**
 * Parseur de r�sultat cppTest Checkstyle g�n�re ses r�sultats sous la forme de fichier XML, cette classe permet de lire
 * le contenu d'un tel fichier. Le fichier de rapport est organis� en deux sections : la premi�re donne la liste des
 * r�gles, la seconde la liste des violations rencontr�es
 */
public class CheckstyleReportParser
    extends XmlImport
{
    /** Pr�fixe de nom de fichier */
    private String mFilePrefix;

    /**
     * Logger
     */
    private static final Log LOGGER = LogFactory.getLog( CheckstyleReportParser.class );

    /**
     * Constructeur
     * 
     * @param pFilePrefix pr�fixe de nom de fichier
     */
    public CheckstyleReportParser( String pFilePrefix )
    {
        super( LOGGER );
        mFilePrefix = pFilePrefix;
    }

    /**
     * Parsing d'un fichier de r�sultat
     * 
     * @param pStream stream d'une map avec en clef le nom de la r�gle et en valeur un integer qui donne le nombre de
     *            violations
     * @param pReportHandler handler de rapport
     * @throws ConfigurationException si erreur
     */
    public void parse( InputStream pStream, ReportHandler pReportHandler )
        throws ConfigurationException
    {
        StringBuffer errors = new StringBuffer();
        Digester digester = preSetupDigester( null, null, errors );
        // Traitement du nom de fichier
        digester.addSetProperties( "checkstyle/file" );
        // Traitement des d�tails d'erreur
        int index = 0;
        Class[] params = { String.class, String.class, String.class, String.class, String.class };
        digester.addCallMethod( "checkstyle/file/error", "setError", params.length, params );
        digester.addCallParam( "checkstyle/file/error", index++, "line" );
        digester.addCallParam( "checkstyle/file/error", index++, "column" );
        digester.addCallParam( "checkstyle/file/error", index++, "severity" );
        digester.addCallParam( "checkstyle/file/error", index++, "message" );
        digester.addCallParam( "checkstyle/file/error", index++, "source" );
        digester.push( new ResultWrapper( pReportHandler ) );
        parse( digester, pStream, errors );
        if ( errors.length() != 0 )
        {
            // Erreurs rencontr�es pendant le parsing
            String message = RulesCheckingMessages.getString( "logs.parsing.error", errors.toString() );
            LOGGER.error( message );
            throw new ConfigurationException( message );
        }
    }

    /**
     * Interface de r�sultat
     */
    public class ResultWrapper
    {
        /** Handler de rapport */
        private ReportHandler mReportHandler;

        /** Nom du fichier courant */
        private String mName;

        /**
         * Constructeur
         * 
         * @param pReportHandler handler de rapport
         */
        ResultWrapper( ReportHandler pReportHandler )
        {
            mReportHandler = pReportHandler;
        }

        /**
         * @param pFileName nom de fichier
         */
        public void setName( String pFileName )
        {
            String newFileName = pFileName;
            if ( newFileName.startsWith( mFilePrefix ) )
            {
                newFileName = newFileName.substring( mFilePrefix.length() );
            }
            mName = newFileName;
        }

        /**
         * @param pLine ligne
         * @param pColumn colonne
         * @param pSeverity nom
         * @param pMessage valeur
         * @param pSource source
         */
        public void setError( String pLine, String pColumn, String pSeverity, String pMessage, String pSource )
        {
            String newColumn = pColumn;
            if (newColumn == null )
            {
                newColumn = "0";
            }
            mReportHandler.processError( mName, pLine, newColumn, pSeverity, pMessage, pSource );
        }

        /**
         * @return nom du fichier
         */
        public String getName()
        {
            return mName;
        }

    }
}
