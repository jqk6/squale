package com.airfrance.welcom.taglib.canvas;

/**
 * 
 * @author M327837
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public interface ICanvasLeftMenuTagRenderer {

    /**
     * Le debut du contenu de canvas gauche
     * @return debut du contenu de canvas gauche
     */
    public abstract String drawStart();

    /**
     * le fin du canvas gauche
     * @param body body
     * @param width taille
     * @param containsBouton si contient des boutons
     * @param containsMenu si contien un menu
     * @return fin du canvas gauche
     */
    public abstract String drawEnd(String body, int width, boolean containsMenu, boolean containsBouton);

}
