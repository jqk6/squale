package com.airfrance.welcom.outils.pdf;

import java.io.InputStream;
import java.io.OutputStream;

import com.airfrance.welcom.outils.pdf.advanced.WPdfDecoration;

/**
 * @author M327837 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public interface PDFGenerateur
{
    /**
     * @param pdfData le pdfData(utilise pour recuperer la locale et la request)
     */
    public void setPdfData( PDFData pdfData );

    /**
     * Chargement le template
     * 
     * @param is : Steram contenant le template
     * @throws PDFGenerateurException : Erreur PDF sur l'ecture du template
     */
    public void loadTemplate( InputStream is )
        throws PDFGenerateurException;

    /**
     * Gere le PDF
     * 
     * @throws PDFGenerateurException : Probleme a la generation
     */
    public void close()
        throws PDFGenerateurException;

    /**
     * assigne l'outputstream
     * 
     * @param os outputStream � setter
     * @throws PDFGenerateurException exception pouvant etre levee
     */
    public void open( OutputStream os )
        throws PDFGenerateurException;

    /**
     * retourne le document en cours de g�n�ration.
     * 
     * @return report
     */
    public Object getReport();

    /**
     * retourne l'objet d'�criture dans le pdf
     * 
     * @return writer
     */
    public Object getPDFWriter();

    /**
     * Attention cette m�thode n'est utilisable qu'avec un moteur de rendu IText
     * 
     * @param decoration d�coration contenant le header et le footer
     * @throws PDFGenerateurException exception pouvant etre levee
     */
    public void setDecoration( WPdfDecoration decoration )
        throws PDFGenerateurException;

}