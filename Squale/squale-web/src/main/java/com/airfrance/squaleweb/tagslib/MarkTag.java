package com.airfrance.squaleweb.tagslib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;

import com.airfrance.squaleweb.util.SqualeWebActionUtils;

/**
 * Juste pour afficher la note sous une forme plus agr�able avec un formatage
 * Ce tag a �t� cr�� car l'algo de tendance travaille sur les float complets
 */
public class MarkTag extends TagSupport {

    /** le param�tre "nom" du tag */
    private String name;

    /** la note du tag */
    private String mark;

    /**
     * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
     * {@inheritDoc}
     * M�thode de lancement du tag
     */
    public int doStartTag() throws JspException {
        // Publie
        ResponseUtils.write(pageContext, format((String) RequestUtils.lookup(pageContext, name, mark, null)));
        return SKIP_BODY;
    }

    /**
     * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
     * {@inheritDoc}
     * M�thode de lancement du tag
     */
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    /** 
     * formate la chaine correspondant � la note 
     * @param markToFormat la note � formater
     * @return la note format�e
     */
    private String format(String markToFormat) {
        // Si la note est nulle on affiche un "-"
        String result = "<img src=\"" + "/squale/images/pictos/na.gif" + "\" border=\"0\" />";
        String markResult = SqualeWebActionUtils.formatFloat(markToFormat);
        // Si on a pu r�cup�r�r une note correcte, on la renvoie sinon on affiche le "-"
        if (!SqualeWebActionUtils.DASH.equals(markResult)) {
            result = markResult;
        }
        return result;
    }

    /**
     * @return la note
     */
    public String getMark() {
        return mark;
    }

    /**
     * @return le nom
     */
    public String getName() {
        return name;
    }

    /**
     * @param newMark la nouvelle note
     */
    public void setMark(String newMark) {
        mark = newMark;
    }

    /**
     * @param newName le nouveau nom
     */
    public void setName(String newName) {
        name = newName;
    }

}
