/*
 * Cr�� le 25 ao�t 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.addons.message.bean;

import java.util.ArrayList;

import com.airfrance.welcom.struts.bean.WActionForm;

/**
 *
 */
public class MessagesBean extends WActionForm {

    /**
     * 
     */
    private static final long serialVersionUID = 5510456587043354722L;

    /** Liste des message */
    private ArrayList listMessages = new ArrayList();

    /** liest des locales */
    private ArrayList listLocales = new ArrayList();

    /** clef de filtre*/
    private String messageKeyFiltre = "";

    /** message de filtre */
    private String messageFiltre = "";

    /**
     * @return accesseur
     */
    public ArrayList getListMessages() {
        return listMessages;
    }

    /**
     * @param list accesseur
     */
    public void setListMessages(final ArrayList list) {
        listMessages = list;
    }

    /**
     * @return accesseur
     */
    public ArrayList getListLocales() {
        return listLocales;
    }

    /**
     * @param list accesseur
     */
    public void setListLocales(final ArrayList list) {
        listLocales = list;
    }

    /**
     * @return accesseur
     */
    public String getMessageKeyFiltre() {
        return messageKeyFiltre;
    }

    /**
     * @param string accesseur
     */
    public void setMessageKeyFiltre(final String string) {
        messageKeyFiltre = string;
    }

    /**
     * @return accesseur
     */
    public String getMessageFiltre() {
        return messageFiltre;
    }

    /**
     * @param string accesseur
     */
    public void setMessageFiltre(final String string) {
        messageFiltre = string;
    }

}
