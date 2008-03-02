package com.airfrance.welcom.taglib.canvas;

/**
 * @author M327837 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public interface ICanvasRenderer
{

    /**
     * Le debut du contenu de canvas
     * 
     * @param event : event
     * @return debut du contenu de canvas
     */
    public String drawStart( String event );

    /**
     * le footer canvas
     * 
     * @param menuBuffer menu
     * @return le footer
     */
    public String drawFooter( String menuBuffer );

    /**
     * le fin du canvas
     * 
     * @return le fin du canvas
     */
    public String drawEnd();

    /**
     * le traceur
     * 
     * @param menuName nom du menu
     * @param itemName nom de l'item
     * @return le traceur
     */
    public String drawTraceur( String menuName, String itemName );

}
