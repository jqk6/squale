/*
 * Cr�� le 27 juil. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.squalecommon.enterpriselayer.businessobject;

/**
 * @author M400843
 *
 */
public class UnexpectedRelationException extends Exception {
    
    /**
     * 
     */
    public UnexpectedRelationException() {
        super();
    }

    /**
     * Affecte une chaine � l'exception
     * @param pArg0 la chaine
     */
    public UnexpectedRelationException(String pArg0) {
        super(pArg0);
    }
    
//    /**
//     * Affecte une chaine � l'exception
//     * @param pChildClass le type de la classe enfant
//     * @param pParentClass le type de la classe parent
//     */
//    public UnexpectedRelationException(Class pChildClass, Class pParentClass) {
//        super();
//    }

}
