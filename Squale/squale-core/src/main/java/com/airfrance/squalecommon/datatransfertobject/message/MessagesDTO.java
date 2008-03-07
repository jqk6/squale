package com.airfrance.squalecommon.datatransfertobject.message;

import java.util.Map;
import java.util.TreeMap;

/**
 * DTO pour les messages Cette classe encapsule une collection de messages
 */
public class MessagesDTO
{
    /** Langue parf d�faut */
    private static final String DEFAULT_LANG = "en";

    /** Messages */
    private TreeMap mMessages = new TreeMap();

    /**
     * Obtention d'un message Si le message n'est pas trouv� dans la langue demand�e, la langue par d�faut est utilis�e
     * 
     * @param pLang langue
     * @param pKey clef
     * @return message ou null si non trouv�
     */
    public String getMessage( String pLang, String pKey )
    {
        String result = null;
        Map langMap = (Map) mMessages.get( pLang );
        // On prend la langue par d�faut si besoin
        if ( langMap == null )
        {
            langMap = (Map) mMessages.get( DEFAULT_LANG );
        }
        if ( langMap != null )
        {
            result = (String) langMap.get( pKey );
        }
        return result;
    }

    /**
     * Ajout d'un message
     * 
     * @param pLang langue
     * @param pKey clef
     * @param pText texte
     */
    public void addMessage( String pLang, String pKey, String pText )
    {
        if ( pLang == null )
        {
            //CHECKSTYLE:OFF
            pLang = "";
            //CHECKSTYLE:ON
        }
        Map langMap = (Map) mMessages.get( pLang );
        // Cr�ation de la map pour la langue si besoin
        if ( langMap == null )
        {
            langMap = new TreeMap();
            mMessages.put( pLang, langMap );
        }
        langMap.put( pKey, pText );
    }
}
