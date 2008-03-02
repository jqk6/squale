package com.airfrance.squaleweb.applicationlayer.action.export.xml;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;

/**
 * Factory pour la g�n�ration de documents XML
 */
public class XMLFactory
{

    /** Content Type */
    protected final static String CONTENT_TYPE = "text/xml;charset=ISO-8859-1";

    /**
     * @param xmldata les donn�es
     * @param response la r�ponse
     * @param attachementFileName le nom du fichier
     * @throws XMLGeneratorException si erreur
     */
    public static void generateXMLtoHTTPResponse( final XMLData xmldata, final HttpServletResponse response,
                                                  final String attachementFileName )
        throws XMLGeneratorException
    {
        try
        {
            response.setContentType( CONTENT_TYPE );
            // Activer l'attachement provoque une erreur javascript au rechargement de la page
            // Aucun solution n'a �t� trouv�e pour le moment....
            // Code mis en commentaire
            // if (((attachementFileName != null) && (attachementFileName.length() > 0))) {
            // response.setHeader("Content-Disposition", "attachment;filename=" + attachementFileName + ";");
            // }
            generateXML( xmldata, response.getWriter() );
        }
        catch ( final Exception e )
        {
            throw new XMLGeneratorException( e.getMessage() );
        }
    }

    /**
     * @param xmldata les donn�es
     * @param printer pour affichier les r�sultats dans la r�ponse
     * @throws TransformerException si erreur
     */
    private static void generateXML( XMLData xmldata, PrintWriter printer )
        throws TransformerException
    {
        xmldata.fill();
        printer.write( xmldata.print() );
        printer.flush();
        printer.close();
    }

}
