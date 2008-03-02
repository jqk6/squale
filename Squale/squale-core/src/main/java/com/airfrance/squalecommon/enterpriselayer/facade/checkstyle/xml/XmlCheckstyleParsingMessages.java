package com.airfrance.squalecommon.enterpriselayer.facade.checkstyle.xml;

import com.airfrance.squalecommon.util.messages.BaseMessages;

/**
 * @author sportorico
 */
class XmlCheckstyleParsingMessages
    extends BaseMessages
{
    /** Instance */
    static private XmlCheckstyleParsingMessages mInstance = new XmlCheckstyleParsingMessages();

    /**
     * Constructeur priv� pour �viter l'instanciation
     */
    private XmlCheckstyleParsingMessages()
    {
        super( "com.airfrance.squalecommon.enterpriselayer.facade.checkstyle.xml.xml_checkstyle_messages" );
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
