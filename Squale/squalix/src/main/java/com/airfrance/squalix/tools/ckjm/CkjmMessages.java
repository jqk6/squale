package com.airfrance.squalix.tools.ckjm;

import com.airfrance.squalecommon.util.messages.BaseMessages;

/**
 * Messages pour la t�che ckjm
 */
public class CkjmMessages
    extends BaseMessages
{
    /** Instance */
    static private CkjmMessages mInstance = new CkjmMessages();

    /**
     * Constructeur par d�faut. Priv� pour �viter l'instanciation.
     */
    private CkjmMessages()
    {
        super( "com.airfrance.squalix.tools.ckjm.ckjm" );
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
}
