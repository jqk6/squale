package com.airfrance.squaleweb.applicationlayer.formbean.access;

import java.util.ArrayList;
import java.util.List;

import com.airfrance.squaleweb.applicationlayer.formbean.RootForm;

/**
 * Bean pour une liste d'acc�s utilisateur
 */
public class AccessListForm
    extends RootForm
{
    /**
     * Liste des acc�s
     */
    private List mList = new ArrayList();

    /**
     * @return la liste des acc�s
     */
    public List getList()
    {
        return mList;
    }

    /**
     * @param pList la liste des acc�s
     */
    public void setList( List pList )
    {
        mList = pList;
    }

}
