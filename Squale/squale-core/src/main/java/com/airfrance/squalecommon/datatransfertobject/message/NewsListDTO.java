package com.airfrance.squalecommon.datatransfertobject.message;

import java.util.Collection;

/**
 * 
 */
public class NewsListDTO
{

    /** la liste des nouveautÚs */
    private Collection newsList;

    /**
     * @return la liste des nouveautÚs
     */
    public Collection getNewsList()
    {
        return newsList;
    }

    /**
     * @param newList la nouvelle liste
     */
    public void setNewsList( Collection newList )
    {
        newsList = newList;
    }

}
