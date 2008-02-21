package com.airfrance.squalecommon.util.xml;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.xml.sax.SAXException;

import com.airfrance.squalecommon.util.messages.CommonMessages;

/**
 * Importation de donn�es XML
 * Cette calsse factorise l'importation de donn�es XML avec un digester,
 * les donn�es XML sont suppos�es d�finies par une DTD accessible par le classpath
 * que l'on fournit en param�tre � la classe
 * 
 */
public class XmlImport {
    /** Log */
    private Log mLog;
    
    /**
     * Constructeur 
     * @param pLog log
     */
    protected XmlImport(Log pLog) {
        mLog = pLog;
    }
    /**
     * Configuration du digester
     * Le digester est utilis� pour le chargement du fichier XML
     * @param pPublicId identification publique ou null si pas de DTD associ�e
     * @param pLocation ressource correspondante
     * @param pErrors erreurs de traitement
     * @return digester
     */
    protected Digester preSetupDigester(String pPublicId, String pLocation, StringBuffer pErrors) {
        Digester configDigester = new Digester();
        configDigester.setNamespaceAware(true);
        configDigester.setUseContextClassLoader(true);
        // Placement du traitement d'erreur
        configDigester.setErrorHandler(new ParsingHandler(mLog, pErrors));
        // R�solution de DTD
        if (pPublicId!=null) {
            configDigester.setValidating(true);
            configDigester.setPublicId(pPublicId);
            configDigester.setEntityResolver(new XmlResolver(pPublicId, pLocation));
        } else {
            configDigester.setValidating(false);
        }
        return configDigester;
    }
    
    /**
     * Parsing du fichier XML
     * Le parsing est ex�cut�, puis le flux est ferm�
     * @param pConfigDigester digester
     * @param pStream flux de grille
     * @param pErrors erreurs
     */
    protected void parse(Digester pConfigDigester, InputStream pStream, StringBuffer pErrors) {
        try {
            pConfigDigester.parse(pStream);
        } catch (IOException e) {
            // Traitement par d�faut de l'exception
            handleException(e, pErrors);
        } catch (SAXException e) {
            // Traitement par d�faut de l'exception
            handleException(e, pErrors);
        } finally {
            try {
                // Fermeture du flux en entr�e
                pStream.close();
            } catch (IOException e1) {
                // Traitement par d�faut de l'exception
                handleException(e1, pErrors);
            }
        }
    }
    
    /**
     * Traitement d'une exception
     * @param pException exception
     * @param pErrors erreurs
     */
    private void handleException(Exception pException, StringBuffer pErrors) {
        String message = CommonMessages.getString("xml.parsing.error", new Object[]{pException.getMessage()});
        pErrors.append(message);
        pErrors.append('\n');
        mLog.error(message, pException);
    }
}
