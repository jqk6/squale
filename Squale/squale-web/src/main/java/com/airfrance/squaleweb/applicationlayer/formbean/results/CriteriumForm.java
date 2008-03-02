package com.airfrance.squaleweb.applicationlayer.formbean.results;

import java.util.ArrayList;
import java.util.List;

/**
 * Contient les r�sultats de niveau crit�re
 * 
 * @author M400842
 */
public class CriteriumForm
    extends ResultForm
{

    /**
     * Liste des pratiques contenues
     */
    private List mPractices = new ArrayList();

    /**
     * @return la liste des pratiques
     */
    public List getPractices()
    {
        return mPractices;
    }

    /**
     * @param pPractices la liste des pratiques
     */
    public void setPractices( List pPractices )
    {
        mPractices = pPractices;
    }

    /**
     * Ajoute une pratique � la liste
     * 
     * @param pPractice la pratique � ajouter
     */
    public void addPractice( ResultForm pPractice )
    {
        mPractices.add( pPractice );
    }

}
