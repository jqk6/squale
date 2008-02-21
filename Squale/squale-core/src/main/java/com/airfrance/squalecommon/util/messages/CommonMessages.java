package com.airfrance.squalecommon.util.messages;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author m400832
 * @version 1.0
 */
public abstract class CommonMessages {

    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactory.getLog(CommonMessages.class);

    /**
     * Chemin du fichier de propri�t�s.
     */
    private static final String BUNDLE_NAME = "com.airfrance.squalecommon.util.messages.common_messages";

    /**
     * Instance de ResourceBudle utilis�e.
     */
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    /**
     * Constructeur par d�faut. Priv� pour �viter l'instanciation.
     */
    private CommonMessages() {
    }


    /**
     * Retourne la cha�ne de caract�re identifi�e par la cl�.
     * @param pKey nom de la cl�.
     * @return la cha�ne associ�e.
     */
    public static String getString(String pKey) {
        String value = null;
        ResourceBundle goodBundle = RESOURCE_BUNDLE;
        try {
            value = goodBundle.getString(pKey); 
        } catch (MissingResourceException e) {
            String message = getString("exception.messages.missing") + pKey;
            LOGGER.error(message, e);
        }
        return value;
    }

    /**
     * Retourne le nombre (entier) identifi�e par la cl�.
     * @param pKey nom de la cl�.
     * @return la cha�ne associ�e.
     */
    public static int getInt(String pKey) {
        String value = getString(pKey);
        int ret = -1;
        try {
            ret = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            LOGGER.error(getString("exception.messages") + pKey);
            LOGGER.error(
                getString("exception.messages.castToInt1") + value + getString("exception.messages.castToInt2"),
                e);
        }
        return ret;
    }


    /**
     * Obtention d'un message variable
     * @param pKey clef
     * @param pObjects objets
     * @return cha�ne r�sultante
     */
    public static String getString(String pKey, Object[] pObjects) {
        MessageFormat format = new MessageFormat(getString(pKey));
        return format.format(pObjects);
    }

}
