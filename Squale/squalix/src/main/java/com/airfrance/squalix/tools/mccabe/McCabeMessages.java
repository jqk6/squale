package com.airfrance.squalix.tools.mccabe;

import com.airfrance.squalecommon.util.messages.BaseMessages;

/**
 * @author m400842 (by rose)
 * @version 1.0
 */
public class McCabeMessages extends BaseMessages {
    /** Instance */
    static private McCabeMessages mInstance = new McCabeMessages();

    /**
     * Constructeur priv� pour �viter l'instanciation
     * @roseuid 42D3C09200D8
     */
    private McCabeMessages() {
        super("com.airfrance.squalix.tools.mccabe.mccabe");
    }

    /**
     * Retourne la cha�ne de caract�re identifi�e par la cl�.
     * @param pKey nom de la cl�.
     * @return la cha�ne associ�e.
     * @roseuid 42D3C09200D9
     */
    public static String getString(String pKey) {
        return mInstance.getBundleString(pKey);
    }

    /**
     * Obtention d'une cha�ne
     * @param pKey clef
     * @param pArgument argument
     * @return cha�ne valu�e
     */
    public static String getString(String pKey, Object pArgument) {
        return getString(pKey, new Object[] { pArgument });
    }

    /**
     * @param pKey clef
     * @param pObjects objets
     * @return cha�ne valu�e
     */
    public static String getString(String pKey, Object[] pObjects) {
        return mInstance.getBundleString(pKey, pObjects);
    }
}
