/*
 * Cr�� le 4 sept. 06
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.jraf.commons.exception;

/**
 * @author M312645
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class JrafAdhesionException extends JrafDaoException {

	private int errorCode;
	public JrafAdhesionException(String msg,int code) {
		 super(msg);
	 }
	
	 public JrafAdhesionException(Throwable root) {
			 super(root);
	 }
	
	 public JrafAdhesionException(String msg, Throwable root) {
		 super(msg, root);
	 }

	/**
	 * @return
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * @param pI
	 */
	public void setErrorCode(int pI) {
		errorCode = pI;
	}

}
