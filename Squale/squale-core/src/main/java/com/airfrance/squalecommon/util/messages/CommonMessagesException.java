/*
 * Cr�� le 21 juil. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.squalecommon.util.messages;

/**
 * @author M400841 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class CommonMessagesException
    extends Exception
{

    /**
     * Constructeur par defaut
     */
    public CommonMessagesException()
    {

    }

    /**
     * Constructeur qui renseigne le message d'erreur
     * 
     * @param pMessage message d'erreur
     */
    public CommonMessagesException( String pMessage )
    {
        super( pMessage );
    }

}
