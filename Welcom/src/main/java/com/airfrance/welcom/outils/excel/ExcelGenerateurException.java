/*
 * Cr�� le 28 oct. 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.outils.excel;

/**
 *
 * Classe d'exception retourn�e par l' ExcelGenerateur
 * @author R�my Bouquet
 *
 */
public class ExcelGenerateurException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 5604953364154760625L;

    /**
     * L�v�e un exception si un probleme a la generation Excel
     * @param arg0 : Libelle du message
     */
    public ExcelGenerateurException(final String arg0) {
        super(arg0);
    }
    
    /**
     * L�v�e un exception si un probleme a la generation Excel
     * @param message : Libelle du message
     * @param cause : Cause
     */
    public ExcelGenerateurException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * L�v�e un exception si un probleme a la generation Excel
     * @param cause : Cause
     */
    public ExcelGenerateurException(Throwable cause) {
        super(cause);
    }
}