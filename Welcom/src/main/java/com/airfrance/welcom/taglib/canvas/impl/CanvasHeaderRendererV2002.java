package com.airfrance.welcom.taglib.canvas.impl;

import com.airfrance.welcom.outils.WelcomConfigurator;
import com.airfrance.welcom.taglib.canvas.ICanvasHeaderRenderer;

/**
 * 
 * @author M327837
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class CanvasHeaderRendererV2002 implements ICanvasHeaderRenderer {

    /** Initilise le header
     * @see com.airfrance.welcom.taglib.canvas.ICanvasHeaderRenderer#drawHeader(java.lang.String)
     */
    public StringBuffer drawHeader(String headerImageURL) {
        StringBuffer sb = new StringBuffer();
        sb.append("<a name=\"top\"></a>");
        sb.append("<div id=\"identification\"><img src=\"" + headerImageURL + "\">");
        sb.append("<img id=\"logoAF\" src=\"" + WelcomConfigurator.getMessage(WelcomConfigurator.HEADER_LOGOAF_KEY) + "\"></div>");
        sb.append("<div id=\"navigationP\"><img src=\"" + WelcomConfigurator.getMessage(WelcomConfigurator.HEADER_BAYADERE_KEY) + "\"></div>");
        return sb;
    }

}
