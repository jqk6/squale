/*
 * Cr�� le 8 mars 06
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.jraf.commons.exception;

/**
 * @author 6391988
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class JrafIllegalStateException extends JrafRuntimeException {

	/**
	 * Constructeur par defaut. 
	 */
	public JrafIllegalStateException() {
		super();
	}
	
	/**
	 * Exception.
	 * @param msg
	 */
	public JrafIllegalStateException(String msg) {
		super(msg);
	}
	
	/**
	 * Exception.
	 * @param msg
	 * @param th
	 */
	public JrafIllegalStateException(String msg, Throwable th) {
		super(msg, th);
	}
	
	/**
	 * Renvoie une exception .
	 * @param th
	 */
	public JrafIllegalStateException(Throwable th) {
		super(th);
	}
}
