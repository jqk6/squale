package com.airfrance.squaleweb.messages;

/**
 * Fournisseur de message
 * Cette interface est utilis�e pour la fourniture de message
 * venant de la base de donn�es
 */
public interface MessageProvider {
    /**
     * Obtention d'un message
     * Si le message n'est pas trouv� dans la langue demand�e,
     * la langue par d�faut est utilis�e
     * @param pLang langue
     * @param pKey clef
     * @return message ou null si non trouv�
     */
    public String getMessage(String pLang, String pKey);

}
