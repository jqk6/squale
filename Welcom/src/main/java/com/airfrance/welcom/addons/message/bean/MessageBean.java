/*
 * Cr�� le 24 ao�t 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.addons.message.bean;

import java.util.HashMap;
import java.util.Iterator;

import com.airfrance.welcom.outils.Util;
import com.airfrance.welcom.struts.bean.WActionForm;

/**
 *
 */
public class MessageBean
    extends WActionForm
{

    /**
     * 
     */
    private static final long serialVersionUID = 3317401158823274755L;

    /** clef du message */
    private String messageKey = "";

    /** Valeur par defaut */
    private String defaultValue = "";

    /** Liste des valeurs */
    private final HashMap values = new HashMap();

    /** Liste des nouvelles valeurs */
    private final HashMap valuesNew = new HashMap();

    /**
     * @return la valeur par defaut
     */
    public String getDefaultValue()
    {
        return defaultValue;
    }

    /**
     * @return la clef du message
     */
    public String getMessageKey()
    {
        return messageKey;
    }

    /**
     * @param string Speficie la nouvelle valeur
     */
    public void setDefaultValue( final String string )
    {
        defaultValue = string;
    }

    /**
     * @param string sp�cifie la nouvelle key
     */
    public void setMessageKey( final String string )
    {
        messageKey = string;
    }

    /**
     * Retourn la veleur en fonction de la locale
     * 
     * @param locale : locale
     * @return Valeur
     */
    public String getValue( final String locale )
    {
        return (String) values.get( locale );
    }

    /**
     * Speficie une valeur pour une locale donn�e
     * 
     * @param locale : locale
     * @param value : valeur
     */
    public void setValue( final String locale, final String value )
    {
        values.put( locale, value );
        valuesNew.put( locale, value );
    }

    /**
     * Recupere la nouvelle valeur pour une locale donn�e
     * 
     * @param locale : locale
     * @return : valeur
     */
    public String getValueNew( final String locale )
    {
        return (String) valuesNew.get( locale );
    }

    /**
     * Speficie une nouvelle valeur pour une locale donn�e
     * 
     * @param locale : locale
     * @param value : valeur
     */
    public void setValueNew( final String locale, final String value )
    {
        valuesNew.put( locale, value );
    }

    /**
     * Retourne vrai si la valeur a �t� modifi�
     * 
     * @param locale : locale
     * @return vrais sir value et valueNew <>
     */
    public boolean isChanged( final String locale )
    {
        return Util.isNonEquals( getValue( locale ), getValueNew( locale ) );
    }

    /**
     * Retourn vrais si le mesages est d�ja cohntenu
     * 
     * @param message : message
     * @return : vrai si deja existant
     */
    public boolean contain( final String message )
    {
        if ( ( message.length() == 0 ) || ( defaultValue.indexOf( message ) != -1 ) )
        {
            return true;
        }
        else
        {
            final Iterator iter = values.values().iterator();
            while ( iter.hasNext() )
            {
                final String result = (String) iter.next();
                if ( result.indexOf( message ) != -1 )
                {
                    return true;
                }
            }
        }
        return false;
    }
}
