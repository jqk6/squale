package com.airfrance.welcom.taglib.canvas.impl;

import com.airfrance.welcom.outils.Util;
import com.airfrance.welcom.taglib.canvas.ICanvasLeftMenuTagRenderer;

/**
 * 
 * @author M327837
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class CanvasLeftMenuTagRendererV2002 implements ICanvasLeftMenuTagRenderer {

    /**
    * @see com.airfrance.welcom.taglib.canvas.ICanvasLeftMenuTagRenderer#drawStart()
    */
    public String drawStart() {

        return null;
    }

    /**
      * @see com.airfrance.welcom.taglib.canvas.ICanvasLeftMenuTagRenderer#drawEnd(java.lang.String, int, boolean, boolean)
     */
    public String drawEnd(String body, int width, boolean containsMenu, boolean containsBouton) {
        StringBuffer sb = new StringBuffer();
        if (!containsMenu) {
            sb.append("<div id=\"navigationS\">\n");
        }
        if (containsBouton) {
            sb.append("<div class=\"menuAction\">");
        }
        if (Util.isTrimNonVide(body)) {
            sb.append(body);
        }
        if (containsBouton) {
            sb.append("</div>");
        }

        if (!containsMenu) {
            sb.append("</div>");
        }
        return sb.toString();
    }
}
