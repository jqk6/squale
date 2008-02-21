package com.airfrance.squalecommon.daolayer;

import com.airfrance.squalecommon.util.messages.BaseMessages;

/**
 * @author M400843
 *
 */
public class DAOMessages extends BaseMessages {
    /** Instance */
    static private DAOMessages mInstance = new DAOMessages();

    /**
     * Constructeur par d�faut. Priv� pour �viter l'instanciation.
     */
    private DAOMessages() {
        super("com.airfrance.squalecommon.daolayer.dao_messages");
    }

    /**
     * Retourne la cha�ne de caract�re identifi�e par la cl�.
     * @param pKey nom de la cl�.
     * @return la cha�ne associ�e.
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
    public static String getString(String pKey, String[] pValues) {
        return mInstance.getBundleString(pKey, pValues);
    }
}
