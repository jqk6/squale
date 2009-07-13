/**
 * Copyright (C) 2008-2010, Squale Project - http://www.squale.org
 *
 * This file is part of Squale.
 *
 * Squale is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * Squale is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Squale.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Cr�� le 28 juil. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package org.squale.welcom.struts.ajax;

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
