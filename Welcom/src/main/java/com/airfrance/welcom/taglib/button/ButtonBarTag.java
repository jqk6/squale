package com.airfrance.welcom.taglib.button;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import com.airfrance.welcom.taglib.formulaire.FormulaireBottomTag;
import com.airfrance.welcom.taglib.onglet.JSOngletBottomTag;
import com.airfrance.welcom.taglib.onglet.JSOngletItemTag;
import com.airfrance.welcom.taglib.onglet.JSOngletTag;
import com.airfrance.welcom.taglib.table.TableBottomTag;
import com.airfrance.welcom.taglib.table.TableTag;

/**
 * ButtonBarTag
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class ButtonBarTag extends BodyTagSupport {
    /**
     * 
     */
    private static final long serialVersionUID = -4874608576214137078L;
    /** le bodyTagSupport*/
    private BodyTagSupport tag = null;

    /**
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
     */
    public int doStartTag() throws JspException {
        //Recherche si un parent est du bon type
        Tag curParent = null;

        for (curParent = getParent();
            (curParent != null) && !((curParent instanceof JSOngletTag) || (curParent instanceof JSOngletItemTag) || (curParent instanceof TableTag));
            curParent = curParent.getParent()) {
            ;
        }

        // INstancie le tage en fonction
        if (curParent instanceof JSOngletTag) {
            tag = new JSOngletBottomTag();
        } else if (curParent instanceof TableTag) {
            tag = new TableBottomTag();
        } else {
            tag = new FormulaireBottomTag();
        }

        tag.setParent(getParent());

        tag.setPageContext(pageContext);

        return tag.doStartTag();
    }

    /**
     * @param s chaine a parser
     * @return supperssion de <input type=hidden ....>
     */
    public static String purgeBodyHidden(final String s) {

        final Pattern reg = Pattern.compile("(<\\s*input[^>]*>)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        final Matcher matcher = reg.matcher(s);
        final StringBuffer sb = new StringBuffer();
        boolean result = matcher.find();
        while (result) {
            if (matcher.group(0).toLowerCase().indexOf("hidden") > -1) {
                matcher.appendReplacement(sb, "");
            } else {
                matcher.appendReplacement(sb, matcher.group(0));
            }
            result = matcher.find();
        }
        matcher.appendTail(sb);
        return sb.toString();

    }

    /**
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
     */
    public int doEndTag() throws JspException {
        tag.setBodyContent(getBodyContent());

        return tag.doEndTag();
    }
    /**
     * @see javax.servlet.jsp.tagext.BodyTagSupport#release()
     */
    public void release() {
        super.release();
        tag = null;
    }

}