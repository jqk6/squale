package com.airfrance.squaleweb.applicationlayer.formbean.messages;

import java.util.Collection;

import com.airfrance.squaleweb.applicationlayer.formbean.RootForm;

/**
 * 
 */
public class NewsListForm extends RootForm{

    /** la liste des nouveaut�s */
    private Collection newsList;

    /**
     * @return la liste des nouveaut�s
     */
    public Collection getNewsList() {
        return newsList;
    }

    /**
     * @param newList la nouvelle liste
     */
    public void setNewsList(Collection newList) {
        newsList = newList;
    }
    
}
