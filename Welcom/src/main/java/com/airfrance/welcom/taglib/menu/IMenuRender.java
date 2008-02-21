/*
 * Cr�� le 13 sept. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.taglib.menu;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

/**
 *
 */
public interface IMenuRender {
    
    /** The RecursiceNumber */
    public static final int RECUSIVE_MAGIC_NUMBER = 100;
    
    /**
     * @param menu le menu
     * @param level le niveau
     * @return le resultat du print
     */
    public String doPrintBase(JSMenu menu, int level);

    /**
     * @param menuItem le menuItem
     * @param parent le parent
     * @param menuName le nom du menu
     * @param level le niveau
     * @param tab le tab
     * @return le js resultant associe
     */
    public String doPrint(JSMenuItem menuItem, JSMenuBase parent, String menuName, int level, int tab);

    /**
     * @param tag le tag
     * @param pageContext le pageContext 
     * @return le header
     * @throws JspException exception pouvant etre levee
     */
    public String doPrintHeader(Tag tag, PageContext pageContext) throws JspException;

    /**
     * 
     * @return le footer
     */
    public String doPrintFooter();

    /**
     * 
     * @param action l'action
     * @return l'action
     */
    public String getAction(String action);
}
