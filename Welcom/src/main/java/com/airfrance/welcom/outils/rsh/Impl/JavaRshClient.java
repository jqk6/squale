/*
 * Cr�� le 7 oct. 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.outils.rsh.Impl;

import java.io.IOException;

import org.apache.commons.io.CopyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.bsd.RCommandClient;

import com.airfrance.welcom.outils.rsh.RshClient;

/**
 *
 * Class effectuant l'execution des commandes en rsh
 *
 * @author M327837
 *
 */
public class JavaRshClient extends RshClient {
    
    /**
     * 
     * @param serveur : Serveur
     * @param loginDistant : login distant
     * @param loginLocal  : login local
     */
    public JavaRshClient(final String serveur, final String loginDistant, final String loginLocal) {
        super(serveur, loginDistant, loginLocal);
    }

    /**
     * Retourne le resultat d'un commande Unix en rsh
     * Attention : le buffer est limit� a 1024.
     * @param cmd : Commande unix
     * @param buff : Ecrit dans l'entree standard le contenu
     * @throws IOException : Retourne le buffer rcp ou bien une erreur d'execution
     * @return resultat unix
     */
    public int executecmd(final String cmd, final byte buff[]) throws IOException {
        lastReturnStream = null;
        lastErrorStream = null;

        final RCommandClient rsh = new RCommandClient();

        // Stocke ce que l'on a envoyer
        addMessage(">" + cmd + "\n");

        try {
            rsh.connect(serveur);
            rsh.rexec(loginLocal, loginDistant, cmd + "\n", true);

            // Si on a quelquechose dans le buffer
            if (buff != null) {
                CopyUtils.copy(buff, rsh.getOutputStream());
                addMessage(buff);
                rsh.getOutputStream().close();

                // Faut etre d�connecter avant de lire
                if ((rsh != null) && rsh.isConnected()) {
                    rsh.disconnect();
                }
            }

            if (rsh.getInputStream() != null) {
                lastReturnStream = IOUtils.toString(rsh.getInputStream());
                addMessage(lastReturnStream);
            }

            if (rsh.getErrorStream() != null) {
                lastErrorStream = IOUtils.toString(rsh.getErrorStream());

                if (lastErrorStream.length() > 0) {
                    addMessage(lastErrorStream);

                    return 1;
                }
            }
        } catch (final IOException ioe) {
            addMessage(ioe.getMessage());
            throw ioe;
        } finally {
            if ((rsh != null) && rsh.isConnected()) {
                rsh.disconnect();
            }
        }

        addMessage(">OK\n");

        return 0;
    }
}