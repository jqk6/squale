/*
 * Cr�� le 13 mai 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.struts.action;

import javax.servlet.http.HttpServletRequest;

import com.airfrance.welcom.outils.Util;
import com.airfrance.welcom.outils.WelcomConfigurator;
import com.airfrance.welcom.struts.util.WConstants;

/**
 * @author M327837
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class WActionUtil {

    /**
     * Verifie sur le Bean WConstant.USER_KEY n'est pas present dans la session
     * @param request : request
     * @throws TimeOutException : Mise en time out
     */
    public static void checkSessionTimeout(final HttpServletRequest request) throws TimeOutException {        
        if (Util.isTrue(WelcomConfigurator.getMessage(WelcomConfigurator.CHECKTIMEOUT_ENABLED)) && (request.getSession().getAttribute(WConstants.USER_KEY) == null)) {

            throw new TimeOutException("welcom.internal.error.session.timeout");
            
        }

    }
}