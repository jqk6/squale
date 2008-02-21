package com.airfrance.jraf.commons.exception;


/**
 * Erreur couche de donn�es
 * 
 * @author Fabrice P�pin
 * @author C�dric Torcq
 */
public class JrafDaoException extends JrafException {

    public JrafDaoException(String msg) {
		super(msg);
	}
	
	public JrafDaoException(Throwable root) {
            super(root);
	}
	
	public JrafDaoException(String msg, Throwable root) {
		super(msg, root);
	}
	
}
