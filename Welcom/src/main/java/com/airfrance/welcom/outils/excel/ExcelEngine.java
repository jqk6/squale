/*
 * Cr�� le 9 nov. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.outils.excel;

/**
 * @author M327836
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class ExcelEngine {
    
    /** Declration du moteur JEXCEL */    
    public final static ExcelEngine JEXCEL = new ExcelEngine("jexcel");
    
    /** nom du moteur */
    private String name="";
    
    /**
     * Constructeur
     * @param engineName engine excel
     */
    private ExcelEngine(final String engineName){
        name=engineName;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
       
        return name;
    }


}
