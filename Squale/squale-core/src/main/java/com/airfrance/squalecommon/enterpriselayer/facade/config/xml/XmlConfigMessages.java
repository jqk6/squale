package com.airfrance.squalecommon.enterpriselayer.facade.config.xml;

import com.airfrance.squalecommon.util.messages.BaseMessages;

/**
 * Messages pour la configuration du portail.
 */
public class XmlConfigMessages
    extends BaseMessages
{
    /** Instance */
    static private XmlConfigMessages mInstance = new XmlConfigMessages();

    /**
     * Constructeur par d�faut. Priv� pour �viter l'instanciation.
     */
    private XmlConfigMessages()
    {
        super( "com.airfrance.squalecommon.enterpriselayer.facade.config.xml.xml_config_messages" );
    }

    /**
     * Retourne la cha�ne de caract�re identifi�e par la cl�.
     * 
     * @param pKey nom de la cl�.
     * @return la cha�ne associ�e.
     */
    public static String getString( String pKey )
    {
        return mInstance.getBundleString( pKey );
    }

    /**
     * Retourne la cha�ne de caract�re identifi�e par la cl�.
     * 
     * @param pKey nom de la cl�.
     * @param pValues les valeurs � ins�rer dans la chaine
     * @return la cha�ne associ�e.
     */
    public static String getString( String pKey, Object[] pValues )
    {
        return mInstance.getBundleString( pKey, pValues );
    }

}
