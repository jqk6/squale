/*
 * Cr�� le 14 avr. 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.taglib.optimization;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.airfrance.welcom.outils.TrimStringBuffer;

/**
 * @author M327837
 *
 * Suppresion Intelligente des espaces ....
 *
 */
public class CleanSpacesTag extends BodyTagSupport {
    /**
     * 
     */
    private static final long serialVersionUID = -2547959677796929093L;

    /**
      * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
      */
    public int doEndTag() throws JspException {
        String value = getBodyContent().getString();

        try {
            final TrimStringBuffer tsb = new TrimStringBuffer();
            tsb.append(value);
            value = tsb.toString();
            final JspWriter writer = pageContext.getOut();
            writer.print(value);
        } catch (final Exception e) {
            throw new JspException("Clean Spaces : " + e.getMessage());
        }

        return EVAL_PAGE;
    }
}