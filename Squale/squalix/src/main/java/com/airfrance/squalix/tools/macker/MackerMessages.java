package com.airfrance.squalix.tools.macker;

import com.airfrance.squalecommon.util.messages.BaseMessages;

/**
 * Messages pour la t�che Macker
 */
public class MackerMessages extends BaseMessages {
    /** Instance */
    static private MackerMessages mInstance = new MackerMessages();

    /**
     * Constructeur par d�faut. Priv� pour �viter l'instanciation.
     */
    private MackerMessages() {
        super("com.airfrance.squalix.tools.macker.macker");
    }

    /**
     * Retourne la cha�ne de caract�re identifi�e par la cl�.
     * @param pKey nom de la cl�.
     * @return la cha�ne associ�e.
     */
    public static String getString(String pKey) {
        return mInstance.getBundleString(pKey);
    }
}
