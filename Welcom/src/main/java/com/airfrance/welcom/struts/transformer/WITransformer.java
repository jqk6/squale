/*
 * Cr�� le 25 mai 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.struts.transformer;

import com.airfrance.welcom.struts.bean.WActionForm;

/**
 * @author M325379
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public interface WITransformer {
    /**
     * Transforme un object ou une collection de la couche metier a la couche presentation
     * @param object object de la couche metier (BO/DTO ou collection de BO/DTO)
     * @return object de la couche presentation
     * @throws WTransformerException Probleme a la transformation
     */
    public WActionForm objToForm(Object object[]) throws WTransformerException;

    /**
     * Transforme un object ou une collection de la couche metier a la couche presentation
     * @param object object de la couche metier (BO/DTO ou collection de BO/DTO)
     * @param form object de la couche presentation
     * @throws WTransformerException Probleme a la transformation
     */
    public void objToForm(Object object[], WActionForm form) throws WTransformerException;

    /**
     * Transforme un object ou une collection de la couche presentation a la couche metier
     * @param form object de la couche presentation
     * @return object de la couche metier (BO/DTO ou collection de BO/DTO)
     * @throws WTransformerException Probleme a la transformation
     */
    public Object[] formToObj(WActionForm form) throws WTransformerException;

    /**
     * Transforme un object ou une collection de la couche presentation a la couche metier
     * @param form object de la couche presentation
     * @param object object de la couche metier (BO/DTO ou collection de BO/DTO)
     * @throws WTransformerException Probleme a la transformation
     */
    public void formToObj(WActionForm form, Object object[]) throws WTransformerException;
}