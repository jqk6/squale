package com.airfrance.squalix.tools.compiling;

import com.airfrance.squalecommon.util.messages.BaseMessages;

/**
 * Classe g�rant le RessourceBundle pour la t�che de compilation.
 * 
 * @author m400832 (by rose)
 * @version 1.0
 */
public class CompilingMessages
    extends BaseMessages
{
    /** Instance */
    static private CompilingMessages mInstance = new CompilingMessages();

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

    /**
     * Constructeur par d�faut. Priv� pour �viter l'instanciation directe.
     */
    private CompilingMessages()
    {
        super( "com.airfrance.squalix.tools.compiling.compiling" );
    }
}
