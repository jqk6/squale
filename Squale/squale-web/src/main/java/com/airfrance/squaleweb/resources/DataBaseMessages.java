/**
 * Copyright (C) 2008-2010, Squale Project - http://www.squale.org
 *
 * This file is part of Squale.
 *
 * Squale is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * Squale is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Squale.  If not, see <http://www.gnu.org/licenses/>.
 */
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
