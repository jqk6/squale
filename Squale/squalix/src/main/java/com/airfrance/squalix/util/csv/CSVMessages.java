package com.airfrance.squalix.util.csv;

import com.airfrance.squalecommon.util.messages.BaseMessages;

/**
 * @author m400842 (by rose)
 * @version 1.0
 */
public class CSVMessages
    extends BaseMessages
{
    /** Instance */
    static private CSVMessages mInstance = new CSVMessages();

    /**
     * Retourne la cha�ne de caract�re sp�cifi�e par la cl�.
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
     * Constructeur priv� pour �viter l'instanciation
     * 
     * @roseuid 42CE768B01BC
     */
    private CSVMessages()
    {
        super( "com.airfrance.squalix.util.csv.csv" );
    }
}
