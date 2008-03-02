package com.airfrance.squalix.tools.computing;

import com.airfrance.squalecommon.util.messages.BaseMessages;

/**
 * @author M400843
 */
public class ComputingMessages
    extends BaseMessages
{
    /** Instance */
    static private ComputingMessages mInstance = new ComputingMessages();

    /**
     * Constructeur par d�faut. Priv� pour �viter l'instanciation.
     */
    private ComputingMessages()
    {
        super( "com.airfrance.squalix.tools.computing.computing_messages" );
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
    public static String getString( String pKey, String[] pValues )
    {
        return mInstance.getBundleString( pKey, pValues );
    }

}
