package com.airfrance.squalix.tools.jdepend;

import com.airfrance.squalecommon.util.messages.BaseMessages;

/**
 * Messages pour la t�che ckjm
 */
public class JDependMessages extends BaseMessages {
    /** Instance */
    static private JDependMessages mInstance = new JDependMessages();

    /**
     * Constructeur par d�faut. Priv� pour �viter l'instanciation.
     */
    private JDependMessages() {
        super("com.airfrance.squalix.tools.jdepend.jdepend");
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
