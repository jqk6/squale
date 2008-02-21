/*
 * Cr�� le 2 mars 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.outils.pdf.advanced;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;

/**
 * @author M327837
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public interface WIPdfHeaderFooter {
    /**
     * Impression sur le doc du header
     * @param over le pdfcontentbyte
     * @param pageSize la taille de la page
     * @param currentPage la page courante
     * @param totalPage le nombre total de page
     */
    public abstract void fill(PdfContentByte over, Rectangle pageSize, int currentPage, int totalPage);
}