package com.airfrance.welcom.struts.bean;

/*
 * Cr�� le 6 mai 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */

/**
 * @author M327837 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public interface WILogonBeanSecurity
    extends WILogonBean
{
    /**
     * Fonction retounant l'acc�s a la page
     * 
     * @param accessKey l'accessKey
     * @return : READWRITE : page en lecture / ecriture READONLY : page en lecture seule NONE : Aucun acc�s a la page
     *         YES : Accessible NO : Non acc�ssible
     */
    public String getAccess( String accessKey );
}