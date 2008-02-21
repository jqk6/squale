package com.airfrance.welcom.taglib.canvas;

/**
 * 
 * @author M327837
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public interface ICanvasPopupRenderer {

    /**
     * debut du head
     * @param event : event
     * @param titreBar : barre de titre
     * @return debut du canvas popup
     */
    public abstract String drawStartHead(String titreBar);

    /**
     * fin du head
     * @param event : event
     * @param titreBar : barre de titre
     * @return debut du canvas popup
     */
    public abstract String drawEndHead();

    /**
     * debut du canvas popup
     * @param event : event
     * @param titreBar : barre de titre
     * @return debut du canvas popup
     */
    public abstract String drawStartBody(String event, String titreBar);

    /**
     * titre du canvas popup
     * @param titre titre
     * @return titre du canvas popup
     */
    public abstract String drawTitre(String titre);

    /**
     * le fin du canvas popup
     * @param closeLabel nom du bouton close
     * @return la fin du canvas popup
     */
    public abstract String drawEndBody(String closeLabel);

}
