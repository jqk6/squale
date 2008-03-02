package com.airfrance.squaleweb.resources;

import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import com.airfrance.squaleweb.messages.MessageProvider;

/**
 * Messages enregistr�s dans la base de donn�es Cette classe permet de stocker sous la forme d'un cache les messages
 * dynamiques enregistr�s dans la base de donn�es
 */
public class DataBaseMessages
    extends Observable
{
    /** Objet observable */
    static private DataBaseMessages mInstance = new DataBaseMessages();

    /** Messages */
    static private MessageProvider mMessages = new MessageProvider()
    {
        public String getMessage( String pLang, String pKey )
        {
            return null;
        }
    };

    /**
     * Constructeur priv�
     */
    private DataBaseMessages()
    {
    }

    /**
     * @see org.apache.struts.util.MessageResources#getMessage(java.util.Locale, java.lang.String) {@inheritDoc}
     */
    static public String getMessage( Locale locale, String key )
    {
        return mMessages.getMessage( locale.getLanguage(), key );
    }

    /**
     * Mise � jour des messages
     * 
     * @param pMessages messages
     */
    static public void update( MessageProvider pMessages )
    {
        mMessages = pMessages;
        mInstance.setChanged();
        mInstance.notifyObservers();
    }

    /**
     * @param pObserver observer
     */
    static public void registerObserver( Observer pObserver )
    {
        mInstance.addObserver( pObserver );
    }
}
