/*
 * Cr�� le 1 ao�t 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.struts.action;

/**
 * @author M327837
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class TimeOutException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -8273349274234979805L;

    /** Message de timout 
     * @param m Message
     * */
    public TimeOutException(final String m) {
        super(m);
    }

}
