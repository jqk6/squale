package com.airfrance.welcom.taglib.canvas.impl;

import org.apache.commons.validator.GenericValidator;

import com.airfrance.welcom.taglib.canvas.ICanvasCenterRenderer;

/**
 * 
 * @author M327837
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class CanvasCenterRendererV3001 implements ICanvasCenterRenderer {

    /**
     * @see com.airfrance.welcom.taglib.canvas.ICanvasCenterRenderer#drawStart(boolean)
     */
    public String drawStart(boolean hasCanvasLeft) {

        final StringBuffer sb = new StringBuffer();
        if (!hasCanvasLeft) {
            sb.append("<div id=\"conteneur\">\n");
            sb.append("<div id=\"traceur\"></div>");
        }
        sb.append("<div id=\"contenu\">\n");
        return sb.toString();

    }

    /**
     * @see com.airfrance.welcom.taglib.canvas.ICanvasCenterRenderer#drawTitre(java.lang.String, java.lang.String, java.lang.String)
     */
    public String drawTitre(String titre, String subTitleKey, String soustitre) {
        final StringBuffer sb = new StringBuffer();

        if (!GenericValidator.isBlankOrNull(titre)) {
            sb.append("<h1>");
            sb.append(titre);
            sb.append("</h1>");
        }

        if (!GenericValidator.isBlankOrNull(subTitleKey)) {
            sb.append("<h3>");
            sb.append(soustitre);
            sb.append("</h3>");
        }
        return sb.toString();
    }

    /**
     * @see com.airfrance.welcom.taglib.canvas.ICanvasCenterRenderer#drawEnd()
     */
    public String drawEnd() {
        return ("</div>\n");
    }
}
