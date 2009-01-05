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
package com.airfrance.welcom.taglib.canvas.impl;

import com.airfrance.welcom.outils.Util;
import com.airfrance.welcom.taglib.canvas.ICanvasLeftMenuTagRenderer;

/**
 * @author M327837 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class CanvasLeftMenuTagRendererV3001
    implements ICanvasLeftMenuTagRenderer
{

    /**
     * @see com.airfrance.welcom.taglib.canvas.ICanvasLeftMenuTagRenderer#drawStart()
     */
    public String drawStart()
    {

        return null;
    }

    /**
     * @see com.airfrance.welcom.taglib.canvas.ICanvasLeftMenuTagRenderer#drawEnd(java.lang.String, int, boolean,
     *      boolean)
     */
    public String drawEnd( String body, int width, boolean containsMenu, boolean containsBouton )
    {
        StringBuffer sb = new StringBuffer();
        sb.append( "<div id=\"conteneur\">\n" );
        sb.append( "<div id=\"traceur\"></div>" );
        sb.append( "<div id=\"gauche\">\n" );
        if ( containsBouton )
        {
            sb.append( "<div class=\"menu_action\">" );
        }
        if ( Util.isTrimNonVide( body ) )
        {
            sb.append( body );
        }
        if ( containsBouton )
        {
            sb.append( "</div>" );
        }

        sb.append( "</div>" );

        return sb.toString();
    }

}
