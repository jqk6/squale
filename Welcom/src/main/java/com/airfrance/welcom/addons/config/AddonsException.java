/*
 * Cr�� le 25 juil. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.addons.config;

/**
 * @author M327837 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class AddonsException
    extends Exception
{

    /**
     * 
     */
    private static final long serialVersionUID = 1174187716606242032L;

    /**
     * Contructeur
     */
    public AddonsException()
    {
        super();
    }

    /**
     * @param s : message
     */
    public AddonsException( final String s )
    {
        super( s );
    }

    /**
     * @param message Message
     * @param cause Cause
     */
    public AddonsException( final String message, final Throwable cause )
    {
        super( message, cause );
    }

    /**
     * @param cause Cause
     */
    public AddonsException( final Throwable cause )
    {
        super( cause );
    }

}
