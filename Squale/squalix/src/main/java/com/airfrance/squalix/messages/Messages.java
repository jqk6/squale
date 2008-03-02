package com.airfrance.squalix.messages;

import com.airfrance.squalecommon.util.messages.BaseMessages;

/**
 * Permet l'externalisation des cha�nes de caract�res
 * 
 * @author M400842
 */
public class Messages
    extends BaseMessages
{
    /** Instance */
    static private Messages mInstance = new Messages();

    /**
     * Constructeur priv� pour �viter l'instanciation
     * 
     * @roseuid 42C1678502C4
     */
    private Messages()
    {
        super( "com.airfrance.squalix.messages.squalix" );
    }

    /**
     * Retourne la cha�ne de caract�re identifi�e par la cl�.
     * 
     * @param pKey nom de la cl�.
     * @return la cha�ne associ�e.
     * @roseuid 42C1678502C5
     */
    public static String getString( String pKey )
    {
        return mInstance.getBundleString( pKey );
    }

    /**
     * Retourne la cha�ne de caract�re identifi�e par la cl�.
     * 
     * @param pKey nom de la cl�.
     * @param pValues les param�tres
     * @return la cha�ne associ�e.
     * @roseuid 42C1678502C5
     */
    public static String getString( String pKey, String[] pValues )
    {
        return mInstance.getBundleString( pKey, pValues );
    }

}
