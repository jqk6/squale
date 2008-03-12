package com.airfrance.squalecommon.util.mail;

import com.airfrance.squalecommon.SqualeTestCase;
import com.airfrance.squalecommon.util.SqualeCommonUtils;

/**
 * @author M400841 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class MailTest
    extends SqualeTestCase
{

    /**
     * Test d'envoi de mail
     */
    public void testMail()
    {
        // Obtention du mailer
        IMailerProvider mail = MailerHelper.getMailerProvider();
        // V�rification de la pr�sence du mail
        assertNotNull( "V�rifier le fichier de configuration providers-config.xml", mail );
        // Test d'envoi de mail avec une adresse en dur
        String title = "Test unitaire de mail";
        String content = "Test unitaire de mail";
        SqualeCommonUtils.notifyByEmail( mail, "bfranchet@qualixo.com", null, title, content, false );
        // avec un exp�diteur
        SqualeCommonUtils.notifyByEmail( mail, "bfranchet@qualixo.com", null, title + "_expediteur", content, false );
    }

}
