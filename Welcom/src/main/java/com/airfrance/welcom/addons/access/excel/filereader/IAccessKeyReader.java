/*
 * Cr�� le 22 juil. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.addons.access.excel.filereader;

import java.util.ArrayList;

/**
 * @author M327837
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public interface IAccessKeyReader {


    /**
     * @return Retourne la liste des clefs des droits d'acc�s definit dans le fichier excel
     */
    public ArrayList getAccessKey();
    
    /**
     * @return Retourne la liste des profils du fichier excel
     */
    public ArrayList getProfile(); 
 

    /**
     * @return Retourne la liste des profils/accesskey du fichier excel
     */
    public ArrayList getProfileAccessKey(); 

}
