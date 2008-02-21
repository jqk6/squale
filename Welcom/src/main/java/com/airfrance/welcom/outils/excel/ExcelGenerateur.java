/*
 * Cr�� le 28 oct. 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.outils.excel;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Interface � impl�menter pour le formatage de la sortie des fichiers Excel.
 * @author R�my Bouquet
 *
 */
public interface ExcelGenerateur {

    /**
     * 
     * @throws ExcelGenerateurException : Level une erreur sur la production du fichier
     */
    public abstract void writeExcel() throws ExcelGenerateurException;

    /**
     * 
     * @throws IOException : Probleme lors de l'ouverture des streams
     */
    public abstract void init() throws IOException;

    /**
     * 
     * assigne l'outputstream
     * @param os outputStream � setter
     * @throws ExcelGenerateurException exception pouvant etre levee
     */
    public void open(OutputStream os) throws ExcelGenerateurException;

}