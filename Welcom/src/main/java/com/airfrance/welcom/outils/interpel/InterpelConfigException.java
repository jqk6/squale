/*
 * Cr�� le 7 oct. 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.outils.interpel;

/** 
 *
 * Erreur dans la configuration d'interpel
 *
 *
 * @deprecated
 * @author M327837
 */
public class InterpelConfigException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7166498723527494368L;

	/**
     * Constructeur
     */
    public InterpelConfigException() {
        super();
    }

    /**
     * Contructeur avec msg
     * @param arg0 : message
     */
    public InterpelConfigException(final String arg0) {
        super(arg0);
    }
}