package com.airfrance.squalecommon.enterpriselayer.facade.rule.xml;

import com.airfrance.squalecommon.util.messages.BaseMessages;

/**
 * Messages pour les r�gles qualit�
 */
class XmlRuleMessages
    extends BaseMessages
{
    /** Instance */
    static private XmlRuleMessages mInstance = new XmlRuleMessages();

    /**
     * Constructeur par d�faut. Priv� pour �viter l'instanciation.
     */
    private XmlRuleMessages()
    {
        super( "com.airfrance.squalecommon.enterpriselayer.facade.rule.xml.xml_rule_messages" );
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
