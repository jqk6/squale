package com.airfrance.welcom.taglib.onglet.impl;

import org.apache.commons.validator.GenericValidator;

import com.airfrance.welcom.outils.TrimStringBuffer;
import com.airfrance.welcom.outils.Util;
import com.airfrance.welcom.outils.WelcomConfigurator;
import com.airfrance.welcom.taglib.button.ButtonBarTag;
import com.airfrance.welcom.taglib.onglet.IJSOngletRenderer;

/**
 * 
 * @author M327837
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class JSOngletRendererV3001 implements IJSOngletRenderer {

    /**
     * @see com.airfrance.welcom.taglib.onglet.IJSOngletRenderer#drawTableBottom(java.lang.String)
     */
    public String drawTableBottom(String bottomValue) {

        final TrimStringBuffer sb = new TrimStringBuffer();
        sb.append("<table class=\"formulaire 100pc\"> \n");
        sb.append("<tfoot><tr><td>");
        if (Util.isTrimNonVide(ButtonBarTag.purgeBodyHidden(bottomValue))) {
            sb.append(bottomValue);
        }
        sb.append("</td></tr></tfoot>");
        // buf.append("<!-- Fin de la G�n�ration de la barre de boutons -->\n");
        sb.append("</table> \n");

        return sb.toString();

    }

    /**
     * @see com.airfrance.welcom.taglib.onglet.IJSOngletRenderer#drawTitleBar(java.lang.String)
     */
    public String drawTitleBar(String titles) {

        final StringBuffer buf = new StringBuffer();

        buf.append("<div class=\"onglet\">");

        buf.append(titles);

        buf.append("</div>");

        return buf.toString();
    }

    /**
     * @see com.airfrance.welcom.taglib.onglet.IJSOngletRenderer#drawBodyStart(java.lang.String, boolean, boolean)
     */
    public String drawBodyStart(String name, boolean ongletSelected, boolean lazyLoading) {
        final StringBuffer buf = new StringBuffer();
        if (ongletSelected) {
            buf.append("<div class=\"onglet_contenu\" ID=\"" + name + "\" STYLE=''");
        } else {
            buf.append("<div class=\"onglet_contenu\" ID=\"" + name + "\" STYLE='display: none'");
        }

        if (lazyLoading) {
            buf.append(" load=\"true\"");
        }

        buf.append(">\n");
        //width:auto !important; width:100%;
        buf.append("<table style=\"width:100%\"><tr><td>"); // resoud pb de style ie

        return buf.toString();
    }

    /**
     * @see com.airfrance.welcom.taglib.onglet.IJSOngletRenderer#drawBodyEnd()
     */
    public String drawBodyEnd() {
        final StringBuffer buf = new StringBuffer();
        buf.append("</td></tr></table>"); // resoud pb de style ie
        buf.append("</div>");
        return buf.toString();
    }

    /**
     * @see com.airfrance.welcom.taglib.onglet.IJSOngletRenderer#drawTitle(java.lang.String, java.lang.String, java.lang.String, int, boolean)
     */
    public String drawTitle(final String name, final String titre, final String parentName, final int indice, final boolean ongletSelected,final String onClickAfterShow) {
        final StringBuffer buf = new StringBuffer();
        buf.append("<a href=\"#\"");
        buf.append(" onclick=\"F_OngletSelectionner3( '" + parentName + "', '" + name + "', this )");
        if (!GenericValidator.isBlankOrNull(onClickAfterShow)) {
            buf.append(";"+onClickAfterShow);
        }
        buf.append("\"");
        if (ongletSelected) {
            buf.append(" class='" + WelcomConfigurator.getMessage(WelcomConfigurator.CHARTEV3_ONGLET_STYLE_SELECTIONNER) + "'");
        }

        buf.append(" >" + titre + "</a>");

        return buf.toString();
    }
}
