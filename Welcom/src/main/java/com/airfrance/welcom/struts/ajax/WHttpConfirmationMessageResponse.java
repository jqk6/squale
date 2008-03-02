/*
 * Cr�� le 28 juil. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.struts.ajax;

import javax.servlet.http.HttpServletResponse;

/**
 * @author R�my Bouquet Wrapper de construction de r�ponse xml pour un message de confirmation dynamique.
 */
public class WHttpConfirmationMessageResponse
    extends AjaxXmlResponseWrapper
{

    /**
     * constructeur
     * 
     * @param presponse la response HTTP
     */
    public WHttpConfirmationMessageResponse( final HttpServletResponse presponse )
    {
        super( presponse, "confirm" );
    }

    /**
     * Ajoute un message � la r�ponse XML
     * 
     * @param message message � ajouter
     */
    public void sendMessage( final String message )
    {
        addItem( "message", message );
    }

}
