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
package org.squale.welcom.taglib.canvas.impl;

import org.apache.commons.validator.GenericValidator;
import org.squale.welcom.outils.WelcomConfigurator;
import org.squale.welcom.taglib.canvas.ICanvasPopupRenderer;


/**
 * @author M327837 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class CanvasPopupRendererV2002
    implements ICanvasPopupRenderer
{

    /**
     * (non-Javadoc)
     * 
     * @see org.squale.welcom.taglib.canvas.ICanvasPopupRenderer#drawStartHead(java.lang.String)
     */
    public String drawStartHead( String titre )
    {
        final StringBuffer sb = new StringBuffer();
        sb.append( "<HTML>" );
        sb.append( "<HEAD>" );
        if ( !GenericValidator.isBlankOrNull( titre ) )
        {
            sb.append( "<TITLE>" );
            sb.append( titre );
            sb.append( "</TITLE>" );
        }
        return sb.toString();
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.squale.welcom.taglib.canvas.ICanvasPopupRenderer#drawEndHead()
     */
    public String drawEndHead()
    {
        return "</head>";
    }

    /**
     * @see org.squale.welcom.taglib.canvas.ICanvasPopupRenderer#drawStartBody(String, String)
     */
    public String drawStartBody( String event, String titreBar )
    {

        final StringBuffer sb = new StringBuffer();
        sb.append( "<BODY id=\"popup\" " );
        if ( WelcomConfigurator.getMessage( WelcomConfigurator.CHARTEV2_VERSION ).equals( "002" ) )
        {
            sb.append( " class=\"infoCompagnie\"" );
        }

        sb.append( event );

        sb.append( ">" );

        sb.append( "<div><img src=\"" + WelcomConfigurator.getMessage( WelcomConfigurator.POPUP_IMG_BANDEAU )
            + "\"></div>" );

        sb.append( titreBar );

        sb.append( "<div style=\"padding-left=20px;padding-right=20px\">" );

        return sb.toString();
    }

    /**
     * @see org.squale.welcom.taglib.canvas.ICanvasPopupRenderer#drawTitre(java.lang.String)
     */
    public String drawTitre( String titre )
    {
        final StringBuffer sb = new StringBuffer();
        sb.append( "<div class=\"titreGeneral\" style=\"padding-left=20px;\">" + titre + "</div>" );
        sb.append( "<div class= \"separateurParagraphe\" style=\"margin-left=20px;\"></div>" );

        return sb.toString();
    }

    /**
     * @see org.squale.welcom.taglib.canvas.ICanvasPopupRenderer#drawEndBody()
     */
    public String drawEndBody( String closeLabel )
    {
        final StringBuffer sb = new StringBuffer();

        sb.append( "</div>" );
        sb.append( "<div id=\"barrePopup\" style=\"padding-top: 2px;padding-right: 5px;\">" + closeLabel
            + "<a href=\"#\"><img align=\"absmiddle\" src=\""
            + WelcomConfigurator.getMessage( WelcomConfigurator.POPUP_IMG_ICON_CLOSE )
            + "\" border=\"0\" onclick=\"javascript:window.close()\"></a></div>" );
        sb.append( "</BODY>" );
        sb.append( "</HTML>" );

        return sb.toString();

    }

}
