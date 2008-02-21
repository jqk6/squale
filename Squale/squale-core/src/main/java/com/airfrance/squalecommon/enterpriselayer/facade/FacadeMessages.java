package com.airfrance.squalecommon.enterpriselayer.facade;

import com.airfrance.squalecommon.util.messages.BaseMessages;

/**
 * Messages sp�cifiques � la couche facade
 *
 */
public class FacadeMessages extends BaseMessages {
    /** Instance */
    static private FacadeMessages mInstance = new FacadeMessages();

    /**
     * Constructeur par d�faut. Priv� pour �viter l'instanciation.
     */
    private FacadeMessages() {
        super("com.airfrance.squalecommon.enterpriselayer.facade.facade_messages");
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
