package com.airfrance.squalecommon.util.mail;

import com.airfrance.jraf.spi.provider.IProvider;

/**
 * Fournisseur de mail
 */
public interface IMailerProvider
    extends IProvider
{

    /**
     * Cl� pour retrouver le provider de mail Cette cl� correspond � l'id du bean de provider de mail dans le fichier de
     * configuration SPRING
     */
    public final static String MAILER_PROVIDER_KEY = "mailer";

    /**
     * Envoi de mail
     * 
     * @param pSender l'exp�diteur - nul si r�cup�r� par configuration
     * @param pRecipients les destinataires
     * @param pSubject sujet du mail
     * @param pContent contenu du mail
     * @throws MailException en cas d'erreur lors de l'envoi du mail
     */
    public void sendMail( String pSender, String[] pRecipients, String pSubject, String pContent )
        throws MailException;
}
