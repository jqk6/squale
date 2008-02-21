package com.airfrance.squaleweb.resources;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;

import com.airfrance.squalecommon.util.messages.BaseMessages;

/**
 * @author m400832
 * @version 1.0
 */
public class WebMessages extends BaseMessages{

    /**
     * @param pBundleName le nom du bundle
     */
    private WebMessages(String pBundleName) {
        super(pBundleName);
    }

    /**
     * Logger.
     */
    private static final Log LOGGER =
        LogFactory.getLog(WebMessages.class);

    /**
     * Chemin du fichier de propri�t�s.
     */
    private static final String BUNDLE_NAME =
        "com.airfrance.squaleweb.resources.ApplicationResources";

    /**
     * Instance de ResourceBudle utilis�e par defaut
     */
    private static final ResourceBundle RESOURCE_BUNDLE =
        ResourceBundle.getBundle(BUNDLE_NAME, new Locale(""));


    /**
     * Retourne la cha�ne de caract�re identifi�e par la cl�.
     * @param pKey nom de la cl�.
     * @return la cha�ne associ�e.
     */
    public static String getString(String pKey) {
        String value = null;
        try {
            value = RESOURCE_BUNDLE.getString(pKey);
        } catch (MissingResourceException e) {
            LOGGER.warn(e,e);
            value = pKey;
        }
        return value;
    }

    /**
     * Retourne la cha�ne de caract�re identifi�e par la cl�.
     * @param pLocale locale
     * @param pKey nom de la cl�.
     * @return la cha�ne associ�e.
     */
    public static String getString(Locale pLocale, String pKey) {
        String value = null;
        try {
            // On modifie la locale par d�faut pour r�cup�rer
            // notre fichier properties par d�faut (ApplicationResources.properties) dans le cas
            // o� il n'y a pas de ressource pour la locale pass�e en param�tre
            Locale.setDefault(new Locale(""));
            ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, pLocale);
            value = bundle.getString(pKey);
            if (value==null) {
                value = DataBaseMessages.getMessage(pLocale, pKey);
            }
        } catch (MissingResourceException e) {
            value = DataBaseMessages.getMessage(pLocale, pKey);
        }
        return value;
    }
    
    /**
     * Retourne la cha�ne de caract�re identifi�e par la cl�.
     * @param pRequest requ�te
     * @param pKey nom de la cl�.
     * @return la cha�ne associ�e.
     */
    public static String getString(HttpServletRequest pRequest, String pKey) {
        String value = null;
        Locale locale = (Locale) pRequest.getSession().getAttribute(Globals.LOCALE_KEY);
        if (locale==null) {
            locale = Locale.getDefault();
        }
        return getString(locale, pKey);
    }

    /**
     * @param pKey la cl�
     * @return le r�sultat apr�s conversion dans le int associ�
     */
    public static int getInt(String pKey) {
        return Integer.decode(getString(pKey)).intValue();
    }

    /**
     * @param pKey la cl�
     * @return le r�sultat apr�s conversion dans le bool�en associ�
     */
    public static boolean getBool(String pKey) {
        return Boolean.valueOf(getString(pKey)).booleanValue();
    }

    /**
     * @param pKey la cl�
     * @return le r�sultat apr�s conversion dans le double associ�
     */
    public static double getDouble(String pKey) {
        return Double.parseDouble(getString(pKey));
    }

    /**
     * @param pKey la cl�
     * @param pValues les param�tres du message
     * @return le texte
     */
    public static Object getString(String pKey, String[] pValues) {
        MessageFormat format = new MessageFormat(getString(pKey));
        return format.format(pValues);
    }

    /**
     * @param pLocale la locale
     * @param pKey la cl�
     * @param pValues les param�tres du message
     * @return le texte
     */
    public static Object getString(Locale pLocale, String pKey, String[] pValues) {
        MessageFormat format = new MessageFormat(getString(pLocale, pKey));
        return format.format(pValues);
    }
}
