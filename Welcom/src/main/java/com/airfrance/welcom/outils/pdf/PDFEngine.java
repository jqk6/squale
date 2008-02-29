/*
 * Cr�� le 9 nov. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.outils.pdf;

/**
 * @author M327836
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class PDFEngine {
    /** constante*/
    public final static PDFEngine ITEXT = new PDFEngine("iText");
    /** constante*/
    public final static PDFEngine JASPERREPORTS = new PDFEngine("jasperReports");
    
    /** identifiant*/
    private String name="";
    
    /**
     * Constructeur
     * @param engineName identifiant
     */
    private PDFEngine(final String engineName){
        name=engineName;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
       
        return name;
    }


}
