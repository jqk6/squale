package com.airfrance.squalecommon.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.jraf.commons.exception.JrafEnterpriseException;
import com.airfrance.jraf.spi.accessdelegate.IApplicationComponent;
import com.airfrance.squalecommon.enterpriselayer.applicationcomponent.administration.LoginApplicationComponentAccess;
import com.airfrance.squalecommon.util.mail.IMailerProvider;
import com.airfrance.squalecommon.util.mail.MailException;
import com.airfrance.squalecommon.util.messages.CommonMessages;

/**
 * 
 */
public class SqualeCommonUtils {

    /**
     * Logger
     */
    private static Log log = LogFactory.getLog(SqualeCommonUtils.class);

    /** champ permettant d'acc�der aux �l�ments */
    private static LoginApplicationComponentAccess access = new LoginApplicationComponentAccess();

    /**
     * @param pProvider le fournisser de service mail
     * @param pDestList la liste des destinataires
     * @param pApplicationId l'id de l'application concern�e
     * @param pObject l'objet du mail
     * @param pContent le contenu du mail
     * @param pUnsubscribed true si on veut r�cup�rer aussi les utilisateur qui se sont d�sabonn�s
     * de l'envoi automatique d'email.
     * @return true si le mail a bein �t� envoy�
     */
    public static boolean notifyByEmail(IMailerProvider pProvider, String pDestList, Long pApplicationId, String pObject, String pContent, boolean pUnsubscribed) {
        return notifyByEmail(pProvider, null, pDestList, pApplicationId, pObject, pContent, pUnsubscribed);

    }

    /**
     * @param pProvider le fournisser de service mail
     * @param pDestList la liste des destinataires
     * @param pApplicationId l'id de l'application concern�e
     * @param pObject l'objet du mail
     * @param pContent le contenu du mail
     * @param pSender le nom de l'exp�diteur
     * @param pUnsubscribed true si on veut r�cup�rer aussi les utilisateur qui se sont d�sabonn�s
     * de l'envoi automatique d'email.
     * @return true si le mail a �t� envoy�
     */
    public static boolean notifyByEmail(IMailerProvider pProvider, String pSender, String pDestList, Long pApplicationId, String pObject, String pContent, boolean pUnsubscribed) {
        boolean sent = true;
        Collection destColl = getDestEmails(pDestList, pApplicationId, pUnsubscribed);
        // transformation le liste en tableau de String
        String[] recipients = new String[destColl.size()];
        recipients = (String[])destColl.toArray(recipients);
        // si on a r�ussi � identifier des destinataires
        if (recipients.length != 0) {
            // Envoi du mail
            try {
                pProvider.sendMail(pSender, recipients, pObject, pContent);
            } catch (MailException e) {
                log.error(CommonMessages.getString("exception.sendmail"), e);
                sent = false;
            }
        }
        return sent;
    }

    /**
     * @param pDestList indique quels utilisateurs sont concern�s (constante d�finie dans SqualeCommonConstants)
     * @param pApplicationId l'id de l'application concern�e
     * @param pUnsubscribed true si on veut r�cup�rer aussi les utilisateur qui se sont d�sabonn�s
     * de l'envoi automatique d'email.
     * @return la liste des destinataires
     */
    private static Collection getDestEmails(String pDestList, Long pApplicationId, boolean pUnsubscribed) {
        Collection destColl = new ArrayList();
        if (pDestList.equals(SqualeCommonConstants.ONLY_ADMINS)) {
            // mail seulement pour les admins du portail
            destColl = getAdminsEmails(pUnsubscribed);
        } else {
            if (pDestList.equals(SqualeCommonConstants.ONLY_MANAGERS)) {
                // mail seulement pour les managers de l'application
                destColl = getManagersEmails(pApplicationId, pUnsubscribed);
            } else {
                if (pDestList.equals(SqualeCommonConstants.MANAGERS_AND_ADMINS)) {
                    // mail destin� � la fois aux admins du portail
                    // ainsi qu'aux gestionnaires de l'application
                    destColl = getAdminsAndManagersEmails(pApplicationId, pUnsubscribed);
                } else {
                    if (pDestList.equals(SqualeCommonConstants.MANAGERS_AND_READERS)) {
                        // mail destin� � la fois aux gestionnaires de l'application
                        // et aux diff�rents utilisateurs qui ont le droit de lecture
                        // sur les r�sultats de l'application
                        destColl = getManagersAndReadersEmails(pApplicationId, pUnsubscribed);
                    } else {
                        destColl.add(pDestList);
                    }
                }
            }
        }
        return destColl;
    }

    /**
     * @param pId l'application dont on veut les managers
     * @param pUnsubscribed true si on veut r�cup�rer aussi les utilisateur qui se sont d�sabonn�s
     * de l'envoi automatique d'email.
     * @return une collection contenant l'ensemble des admins du portail ainsi
     * que les managers de l'application.
     */
    private static Collection getAdminsAndManagersEmails(Long pId, boolean pUnsubscribed) {
        Collection managers = getManagersEmails(pId, pUnsubscribed);
        Collection admins = getAdminsEmails(pUnsubscribed);
        return merge(managers, admins);
    }

    /**
     * @param pId l'application dont on veut les managers
     * @param pUnsubscribed true si on veut r�cup�rer aussi les utilisateur qui se sont d�sabonn�s
     * de l'envoi automatique d'email.
     * @return une collection contenant l'ensemble des admins du portail ainsi
     * que les managers de l'application.
     */
    private static Collection getManagersAndReadersEmails(Long pId, boolean pUnsubscribed) {
        Collection managers = getManagersEmails(pId, pUnsubscribed);
        Collection readers = getReadersEmails(pId, pUnsubscribed);
        return merge(managers, readers);
    }

    /**
     * Fusionne 2 collections d'emails en �vitant les doublons
     * @param pColl1 la premi�re collection d'emails
     * @param pColl2 la deuxi�me collection d'emails
     * @return une collection r�sultat de la fusion sans doublons
     */
    private static Collection merge(Collection pColl1, Collection pColl2) {
        ArrayList result = new ArrayList(0);
        result.addAll(pColl1);
        Iterator it = pColl2.iterator();
        while (it.hasNext()) {
            String currentMail = (String) it.next();
            // on n'utilise pas la m�thode contains car on veut juste v�rifier les emails
            boolean needToAdd = true;
            for (int i = 0; needToAdd && i < result.size(); i++) {
                String comparedEmail = ((String) result.get(i));
                if (currentMail.equals(comparedEmail)) {
                    needToAdd = false;
                }
            }
            if (needToAdd) {
                result.add(currentMail);
            }
        }
        return result;
    }

    /**
     * @param pId l'id de l'application confirm�e
     * @param pUnsubscribed true si on veut r�cup�rer aussi les utilisateur qui se sont d�sabonn�s
     * de l'envoi automatique d'email.
     * @return la liste des emails des managers de l'application
     */
    private static Collection getManagersEmails(Long pId, boolean pUnsubscribed) {
        // On cherche les utilisateurs avec le profil gestionnaire de l'application
        IApplicationComponent ac;
        Collection coll = new ArrayList(0);
        try {
            // Obtention de la liste des administrateurs
            coll = access.getManagersEmails(pId, new Boolean(pUnsubscribed));
        } catch (JrafEnterpriseException e) {
            log.error(CommonMessages.getString("exception.sendmail"), e);
        }
        return coll;
    }

    /**
     * @param pUnsubscribed true si on veut r�cup�rer aussi les utilisateur qui se sont d�sabonn�s
     * de l'envoi automatique d'email.
     * @return la liste des emails des administrateurs
     */
    private static Collection getAdminsEmails(boolean pUnsubscribed) {
        // On cherche les utilisateurs avec le profil administrateur
        IApplicationComponent ac;
        String[] recipients = null;
        Collection coll = new ArrayList(0);
        try {
            // Obtention de la liste des administrateurs
            coll = access.getAdminsEmails(new Boolean(pUnsubscribed));
        } catch (JrafEnterpriseException e) {
            log.error(CommonMessages.getString("exception.sendmail"), e);
        }
        return coll;
    }

    /**
     * @param pId l'id de l'application confirm�e
     * @param pUnsubscribed true si on veut r�cup�rer aussi les utilisateur qui se sont d�sabonn�s
     * de l'envoi automatique d'email.
     * @return la liste des emails des utilisateurs ayant un droit de lecture sur l'application
     */
    private static Collection getReadersEmails(Long pId, boolean pUnsubscribed) {
        // On cherche les utilisateurs avec le profil administrateur
        IApplicationComponent ac;
        String[] recipients = null;
        Collection coll = new ArrayList(0);
        try {
            // Obtention de la liste des administrateurs
            coll = access.getReadersEmails(pId, new Boolean(pUnsubscribed));
        } catch (JrafEnterpriseException e) {
            log.error(CommonMessages.getString("exception.sendmail"), e);
        }
        return coll;
    }
}
