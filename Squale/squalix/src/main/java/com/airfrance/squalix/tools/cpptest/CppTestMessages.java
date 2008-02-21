package com.airfrance.squalix.tools.cpptest;

import com.airfrance.squalecommon.util.messages.BaseMessages;

/**
 * Messages utilis�s par la t�che CppTest
 */
public class CppTestMessages extends BaseMessages {
    /** Instance */
    static private CppTestMessages mInstance = new CppTestMessages();
    /**
     * Constructeur priv� pour �viter l'instanciation
     * 
     */
    private CppTestMessages() {
        super("com.airfrance.squalix.tools.cpptest.cpptest");
    }

    /**
     * Retourne la cha�ne de caract�re identifi�e par la cl�.
     * @param pKey nom de la cl�.
     * @return la cha�ne associ�e.
     * 
     */
    public static String getString(String pKey) {
        return mInstance.getBundleString(pKey);
    }

    /**
     * 
     * Retourne la cha�ne de caract�re identifi�e par la cl�.
     * @param pKey nom de la cl�.
     * @param pValues les valeurs � ins�rer dans la chaine
     * @return la cha�ne associ�e.
     */
    public static String getString(String pKey, Object[] pValues) {
        return mInstance.getBundleString(pKey, pValues);
    }

    /**
     * @param pKey clef
     * @param pArgument argument
     * @return cha�ne associ�e
     */
    public static String getString(String pKey, Object pArgument) {
        return getString(pKey, new Object[] { pArgument });
    }
}
