/*
 * Cr�� le 26 mars 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.struts.bean;

/**
 * @author M327837 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class WActionFormSelectable
    extends WActionForm
    implements WISelectable
{

    /**
     * 
     */
    private static final long serialVersionUID = -1966368476890825150L;

    /** boolean selected */
    private boolean selected = false;

    /**
     * @return true si le form est selectionne
     */
    public boolean isSelected()
    {
        return selected;
    }

    /**
     * @param b la nouvelle selection
     */
    public void setSelected( final boolean b )
    {
        selected = b;
    }

}