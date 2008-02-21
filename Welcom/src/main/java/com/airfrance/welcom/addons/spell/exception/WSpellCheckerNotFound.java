/*
 * Cr�� le 3 f�vr. 06
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.addons.spell.exception;

/**
 * @author M327837
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class WSpellCheckerNotFound extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 4750174715019154209L;

    /**
     * Contructeur si le SpellChecker n'est pas trouv� 
     */
    public WSpellCheckerNotFound() {
        super();
    }

    /**
     * Contructeur si le SpellChecker n'est pas trouv�
     * @param message Message
     */
    public WSpellCheckerNotFound(final String message) {
        super(message);
    }

    /**
     * Contructeur si le SpellChecker n'est pas trouv�
     * @param message Message
     * @param cause Cause
     */
    public WSpellCheckerNotFound(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Contructeur si le SpellChecker n'est pas trouv�
     * @param cause Cause
     */
    public WSpellCheckerNotFound(final Throwable cause) {
        super(cause);
    }

}
