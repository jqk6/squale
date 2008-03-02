package com.airfrance.welcom.taglib.canvas;

/**
 * @author M327837 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public interface ICanvasHeaderRenderer
{

    /**
     * Dessine l'entete .. it�tiere etc
     * 
     * @param headerImageURL imga e du header
     * @return l'entete
     */
    public abstract StringBuffer drawHeader( String headerImageURL );

}