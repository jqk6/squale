package com.airfrance.squalecommon.enterpriselayer.applicationcomponent;

import com.airfrance.squalecommon.util.messages.BaseMessages;

/**
 * @author M400843
 */
public class ACMessages
    extends BaseMessages
{
    /** Instance */
    static private ACMessages mInstance = new ACMessages();

    /**
     * Constructeur par d�faut. Priv� pour �viter l'instanciation.
     */
    private ACMessages()
    {
        super( "com.airfrance.squalecommon.enterpriselayer.applicationcomponent.ac_messages" );
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
