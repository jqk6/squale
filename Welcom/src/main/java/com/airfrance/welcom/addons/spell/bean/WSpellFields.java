/*
 * Cr�� le 30 janv. 06
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.addons.spell.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author M327837
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class WSpellFields extends ArrayList implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3044434052786346027L;

    /** 
     * Retourne un fields, si n'existe pas alors l'intancie 
     * @param index Index
     * @return Retourne un fiels, sinon l'instancie
     * */
    public Object get(final int index) {
        if (super.size() <= index) {
            for (int i = size(); i <= index; i++) {
                super.add(new WSpellField());
            }
        }
        return super.get(index);
    }
}
