/*
 * Cr�� le 30 juin 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.struts.bean;

/**
 * @author M327837
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public interface WIEditable {
    /**
     * @return true si l'objet qui implemente l'interface est selectionne
     */
    public boolean isEdited();

    /**
     * change le flag d'edition de l'objet 
     * @param b la nouvelle selection
     */
    public void setEdited(boolean b);
}