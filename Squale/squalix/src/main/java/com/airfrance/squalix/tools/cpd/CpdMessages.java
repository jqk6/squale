package com.airfrance.squalix.tools.cpd;

import com.airfrance.squalecommon.util.messages.BaseMessages;

/**
 * Messages pour la t�che CopyPasteDetection
 */
public class CpdMessages
    extends BaseMessages
{
    /** Instance */
    static private CpdMessages mInstance = new CpdMessages();

    /**
     * Constructeur priv� pour �viter l'instanciation
     */
    private CpdMessages()
    {
        super( "com.airfrance.squalix.tools.cpd.cpd" );
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
