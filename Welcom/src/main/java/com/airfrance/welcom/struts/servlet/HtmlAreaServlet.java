/*
 * Cr�� le 30 juin 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.struts.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.airfrance.welcom.outils.WelcomConfigurator;
import com.airfrance.welcom.struts.webServer.WebEngine;

/**
 * @author M327837
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class HtmlAreaServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -4108756563480677586L;

    /** 
     * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final WebEngine webEngine = new WebEngine(this, request, response);
        final String path = WelcomConfigurator.getMessage(WelcomConfigurator.HTMLAREA_DEFAULT_PATH) + request.getPathInfo();
        webEngine.process(path);
    }

}
