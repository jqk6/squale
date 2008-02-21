/*
 * Cr�� le 22 juil. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.addons.config;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ModuleConfig;

/**
 * @author M327837
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public interface WIAddOns {

    /**
     * Fermeture de l'addons..
     */
    public void destroy();

    /**
     * la config de l'Add-on est Ok
     * @param servlet : Servlet
     * @param config : Config
     * @throws AddonsException : Probleme sur l'initialisation de l'addons
     */
    public void init(ActionServlet servlet, ModuleConfig config) throws AddonsException;

    /**
     * @return Affichage
     */
    public String getDisplayName();

    /**
     * @return nom de l'addon
     */
    public String getName();

    /**
     * @return Version
     */
    public String getVersion();

    /**
     * @return Retourne le plugin est initialialis�
     */
    public boolean isLoaded();
}
