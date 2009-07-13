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
import org.squale.welcom.taglib.canvas.ICanvasCenterRenderer;


/**
 * @author M327837 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class CanvasCenterRendererV2002
    implements ICanvasCenterRenderer
{

    /**
     * @see org.squale.welcom.taglib.canvas.ICanvasCenterRenderer#drawStart(boolean)
     */
    public String drawStart( boolean hasCanvasLeft )
    {

        return ( "<div id=\"contenu\">\n" );
    }

    /**
     * @see org.squale.welcom.taglib.canvas.ICanvasCenterRenderer#drawTitre(java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    public String drawTitre( String titre, String subTitleKey, String soustitre )
    {
        final StringBuffer sb = new StringBuffer();

        sb.append( "<p class=\"titreGeneral\">" );
        sb.append( titre );
        sb.append( "</p>" );

        // ligne grise
        sb.append( "<div class=\"separateurParagraphe\"></div>" );
        if ( !GenericValidator.isBlankOrNull( subTitleKey ) )
        {
            sb.append( "<p class=\"titreParagraphe \">" );
            sb.append( soustitre );
            sb.append( "</p>" );
        }
        return sb.toString();
    }

    /**
     * @see org.squale.welcom.taglib.canvas.ICanvasCenterRenderer#drawEnd()
     */
    public String drawEnd()
    {
        return ( "</div>\n" );
    }
}
