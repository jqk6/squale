/*
 * Cr�� le 11 ao�t 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.taglib.table;

import javax.servlet.jsp.JspException;

/**
 * @author M327837
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public interface ICol {

    /**
      * Definit le rendu de la colonne ... si sp�cifique
      * @param bean : Bean
      * @param position la position de la colonne
      * @param idIndex Index
      * @param style Style
      * @param styleSelect Style selectionne
      * @return le html g�n�r�
      */
    public String getCurrentValue(int position,Object bean,int idIndex,String style,String styleSelect, int pageLength) throws JspException;
  

}
