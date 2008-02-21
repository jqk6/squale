package com.airfrance.squalecommon.datatransfertobject;

import java.io.Serializable;

import com.airfrance.squalecommon.util.messages.BaseMessages;

/**
 * @author M400843
 *
 */
public class DTOMessages extends BaseMessages implements Serializable {
    /** Instance */
    static private DTOMessages mInstance = new DTOMessages();

    /**
     * Constructeur par d�faut. Priv� pour �viter l'instanciation.
     */
    private DTOMessages() {
        super("com.airfrance.squalecommon.datatransfertobject.dto_messages");
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
