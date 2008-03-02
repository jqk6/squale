package com.airfrance.squaleweb.applicationlayer.formbean.component;

import java.util.ArrayList;
import java.util.List;

import com.airfrance.welcom.struts.bean.WActionForm;

/**
 * Liste des crit�res
 */
public class CriteriaListForm
    extends WActionForm
{
    /**
     * Liste des crit�res
     */
    private List mList = new ArrayList();

    /**
     * @return la liste des crit�res
     */
    public List getList()
    {
        return mList;
    }

    /**
     * @param pList la liste des crit�res
     */
    public void setList( List pList )
    {
        mList = pList;
    }

}
