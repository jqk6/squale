/*
 * Cr�� le 18 janv. 06
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.addons.spell.bean;

import org.apache.struts.action.ActionForm;

/**
 * @author M327837
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class WSpellCheckerBean extends ActionForm {

    /**
     * 
     */
    private static final long serialVersionUID = -7590830545513474300L;
    /** Liste des champs pour la verification othogrpahique */
    private WSpellFields fields = new WSpellFields();

    /**
     * @return accesseur
     */
    public WSpellFields getFields() {
        return fields;
    }

    /**
     * @param pFields accesseur
     */
    public void setFields(final WSpellFields pFields) {
        this.fields = pFields;
    }

}
