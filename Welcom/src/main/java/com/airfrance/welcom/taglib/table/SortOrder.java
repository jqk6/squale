/*
 * Cr�� le 2 avr. 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.taglib.table;

import java.io.Serializable;

/**
 * @author M327837
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class SortOrder implements Serializable {
   /** ID pour la serialization */
   static final long serialVersionUID = 7170418898610183139L;

    /** Constante */
    public static final SortOrder NONE = new SortOrder("sort-none");
    /** Constante */
    public static final SortOrder ASC = new SortOrder("sort-asc");
    /** Constante */
    public static final SortOrder DESC = new SortOrder("sort-desc");
    /** string identifiant le tri */
    private String ssortOrder = "";

    /**
     * Cosntructeur 
     * @param sortOrder tri (NONE, ASC ou DESC)
     */
    private SortOrder(final String sortOrder) {
        this.ssortOrder = sortOrder;
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return ssortOrder;
    }

    /**
     * 
     * @param sort le tri voulu
     * @return le SortOrder associe
     */
    public static SortOrder getSortOrder(String sort) {
        if (sort == null) {
            sort = NONE.toString();
        }

        if (sort.equals(DESC.toString())) {
            return DESC;
        } else if (sort.equals(ASC.toString())) {
            return ASC;
        } else {
            return NONE;
        }
    }

    /**
     *  
     * @param so l'actuel SortOrder
     * @return le SortOrder suivant
     */
    public static SortOrder next(final SortOrder so) {
        if (so == NONE) {
            return ASC;
        }

        if (so == ASC) {
            return DESC;
        }

        return NONE;
    }
}