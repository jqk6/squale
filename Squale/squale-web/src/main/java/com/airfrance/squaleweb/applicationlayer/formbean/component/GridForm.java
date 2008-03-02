package com.airfrance.squaleweb.applicationlayer.formbean.component;

import java.util.Date;

import com.airfrance.squaleweb.applicationlayer.formbean.ActionIdFormSelectable;

/**
 * Donn�es synth�tiques d'une grille qualit�
 */
public class GridForm
    extends ActionIdFormSelectable
{
    /**
     * Nom de la grille
     */
    private String mName = "";

    /** Date de mise � jour */
    private Date mUpdateDate;

    /**
     * Constructeur par d�faut.
     */
    public GridForm()
    {
    }

    /**
     * @return le nom.
     */
    public String getName()
    {
        return mName;
    }

    /**
     * @param pName le nom.
     */
    public void setName( String pName )
    {
        mName = pName;
    }

    /**
     * @return date de mise � jour
     */
    public Date getUpdateDate()
    {
        return mUpdateDate;
    }

    /**
     * @param pString date de mise � jour
     */
    public void setUpdateDate( Date pString )
    {
        mUpdateDate = pString;
    }

    /**
     * le text que peut remplir l'administrateur pour indiquer aux gestionnaires d'applications utilisant cette grille
     * les diff�rentes modifications
     */
    private String mAdminText = "";

    /**
     * @return le texte
     */
    public String getAdminText()
    {
        return mAdminText;
    }

    /**
     * @param pText le nouveau texte
     */
    public void setAdminText( String pText )
    {
        mAdminText = pText;
    }

    /**
     * Juste pour l'affichage
     */
    private String mDisplayedName = "";

    /**
     * @param pDisplayedName le nom a afficher
     */
    public void setDisplayedName( String pDisplayedName )
    {
        mDisplayedName = pDisplayedName;
    }

    /**
     * @return le nom utilis� pour l'affichage
     */
    public String getDisplayedName()
    {
        return mDisplayedName;
    }

}
