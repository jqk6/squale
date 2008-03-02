package com.airfrance.squalix.util.file;

import com.airfrance.squalecommon.util.messages.BaseMessages;

/**
 * Messages pour FileUtility
 */
public class FileMessages
    extends BaseMessages
{
    /** Instance */
    static private FileMessages mInstance = new FileMessages();

    /**
     * Retourne la cha�ne de caract�re sp�cifi�e par la cl�.
     * 
     * @param pKey nom de la cl�.
     * @return la cha�ne associ�e.
     */
    public static String getString( String pKey )
    {
        return mInstance.getBundleString( pKey );
    }

    /**
     * Constructeur priv� pour �viter l'instanciation
     */
    private FileMessages()
    {
        super( "com.airfrance.squalix.util.file.file" );
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
