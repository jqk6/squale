package com.airfrance.squalix.tools.umlquality;

import com.airfrance.squalecommon.util.messages.BaseMessages;

/**
 * @author sportorico
 */
public class UMLQualityMessages
    extends BaseMessages
{
    /** Instance */
    static private UMLQualityMessages mInstance = new UMLQualityMessages();

    /**
     * Constructeur priv� pour �viter l'instanciation
     */
    private UMLQualityMessages()
    {
        super( "com.airfrance.squalix.tools.umlquality.umlquality" );
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

    /**
     * @param pKey clef
     * @param pArgument argument
     * @return cha�ne associ�e
     */
    public static String getString( String pKey, Object pArgument )
    {
        return getString( pKey, new Object[] { pArgument } );
    }
}
