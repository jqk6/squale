/*
 * Cr�� le 18 avr. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.struts.lazyLoading;

import java.io.Serializable;

/**
 * @author M327837 Type de persitance du LazyLoading
 */
public class WLazyLoadingType
    implements Serializable
{

    /** ID pour la serialization */
    static final long serialVersionUID = -782162519563806393L;

    /** Constante */
    public final static WLazyLoadingType ONGLET = new WLazyLoadingType( "ONGLET" );

    /** Constante */
    public final static WLazyLoadingType COMBO = new WLazyLoadingType( "COMBO" );

    /** Constante */
    public final static WLazyLoadingType DROPDOWNPANEL = new WLazyLoadingType( "DROPDOWNPANEL" );

    /** Constante */
    public final static WLazyLoadingType ZONE = new WLazyLoadingType( "ZONE" );

    /** Type d'objet */
    private String s;

    /**
     * Constructeur pS
     * 
     * @param pS le type
     */
    private WLazyLoadingType( final String pS )
    {
        s = pS;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return s;
    }
}