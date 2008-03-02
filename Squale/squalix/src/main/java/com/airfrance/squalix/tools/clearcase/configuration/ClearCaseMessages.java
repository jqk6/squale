package com.airfrance.squalix.tools.clearcase.configuration;

import com.airfrance.squalecommon.util.messages.BaseMessages;

/**
 * @author m400832 (by rose)
 * @version 1.0
 */
public class ClearCaseMessages
    extends BaseMessages
{
    /** Instance */
    static private ClearCaseMessages mInstance = new ClearCaseMessages();

    /**
     * Constructeur par d�faut. Priv� pour �viter l'instanciation.
     */
    private ClearCaseMessages()
    {
        super( "com.airfrance.squalix.tools.clearcase.clearcase" );
    }

    /**
     * Retourne la cha�ne de caract�re identifi�e par la cl�.
     * 
     * @param pKey nom de la cl�.
     * @return la cha�ne associ�e.
     * @roseuid 42D3C0FE0325
     */
    public static String getString( String pKey )
    {
        return mInstance.getBundleString( pKey );
    }
}
