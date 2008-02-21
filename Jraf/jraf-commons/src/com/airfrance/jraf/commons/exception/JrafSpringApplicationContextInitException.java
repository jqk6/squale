/*
 * Cr�� le 17 f�vr. 06
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.jraf.commons.exception;

/**
 * @author 6391988
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;
 * Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;
 * Code et commentaires
 */
public class JrafSpringApplicationContextInitException extends JrafException {

	/**
	 * Contructeur JrafSpringApplicationContextInitException.
	 */
	public JrafSpringApplicationContextInitException() {
		super();
	}
	
	/**
	 * Ajoute un messge d'exception JRAF en cas d'�chec
	 * de l'initialisation de applicationContext.
	 * @param message
	 */
	public JrafSpringApplicationContextInitException(String message) {
		super(message);
	}
	
	/**
	 * Ajoute une clause Throwable.
	 * @param root
	 */
	public JrafSpringApplicationContextInitException(Throwable root) {
		super(root);
	}
	
	/**
	 * Ajoute une message et une clause Throwable.
	 * @param message
	 * @param root
	 */
	public JrafSpringApplicationContextInitException(String message, Throwable root) {
		super(message,root);
	}
}
