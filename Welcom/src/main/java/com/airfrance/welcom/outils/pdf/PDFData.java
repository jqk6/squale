package com.airfrance.welcom.outils.pdf;

import java.util.Locale;

import org.apache.struts.util.MessageResources;

/**
 * 
 * @author M327837
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */

public abstract class PDFData {
    
    /** Locale */
    protected Locale locale = null;
    
    /** Message Ressources pour internationnalisation */
    protected MessageResources messageResources = null;

    /**
     * Contructeur
     * @param pLocale : locale
     * @param pMessages : messages
     */
    public PDFData(final Locale pLocale, final MessageResources pMessages) {
        this.locale = pLocale;
        this.messageResources = pMessages;
    }

    /** 
     * Accesseur 
     * @return Locale
     * */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Nom du template a utilser (.srt)
     * @return Nom du fichier template 
     */
    public abstract String getTemplateName();

    /**
     * Definiton du templissage du document
     * @param pdfGenerateur Moteur pour le rendu PDF
     * @throws PDFGenerateurException : Probleme a la generation
     */
    public abstract void fill(PDFGenerateur pdfGenerateur) throws PDFGenerateurException;

    /**
     * Simplification de l'acc�s aux messages;
     * @param key Key du message
     * @return libell�
     */ 
    public String getMessage(final String key) {
        return messageResources.getMessage(locale, key);
    }
    /**
     * @return le message ressource
     */
    public MessageResources getMessageResources() {
        return messageResources;
    }

}