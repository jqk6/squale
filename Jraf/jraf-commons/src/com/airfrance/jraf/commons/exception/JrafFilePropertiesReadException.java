/*
 * Cr�� le 21 f�vr. 06
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
public class JrafFilePropertiesReadException extends JrafDaoException {
	
	public JrafFilePropertiesReadException(String message) {
		super(message);
	}
		
	public JrafFilePropertiesReadException(Throwable root) {
			super(root);
		}
		
	public JrafFilePropertiesReadException(String msg, Throwable root) {
			super(msg, root);
	}
}
