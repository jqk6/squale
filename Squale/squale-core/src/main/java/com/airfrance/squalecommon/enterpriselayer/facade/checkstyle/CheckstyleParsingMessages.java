package com.airfrance.squalecommon.enterpriselayer.facade.checkstyle;

import com.airfrance.squalecommon.util.messages.BaseMessages;

/**
 * @author sportorico
 */
public class CheckstyleParsingMessages
    extends BaseMessages
{
    /** Instance */
    static private CheckstyleParsingMessages mInstance = new CheckstyleParsingMessages();

    /**
     * Constructeur priv� pour �viter l'instanciation
     */
    private CheckstyleParsingMessages()
    {
        super( "com.airfrance.squalecommon.enterpriselayer.facade.checkstyle.checkstyle_messages" );
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
