package com.airfrance.welcom.outils.pdf.advanced;

import java.util.Enumeration;
import java.util.Hashtable;

/*
 * Cr�� le 3 nov. 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */

/**
 * @author M327837 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class WPdfFields
{
    /** hashtable */
    private final Hashtable h = new Hashtable();

    /**
     * 
     */
    public WPdfFields()
    {
        super();
    }

    /**
     * Ajoute le pdfField a la hashtable
     * 
     * @param pdfField le pdfField a ajouter
     */
    public void add( final WPdfField pdfField )
    {
        h.put( pdfField.getName(), pdfField );
    }

    /**
     * @param pdfFields les pdfFields a ajouter
     */
    public void addAll( final WPdfFields pdfFields )
    {
        final Enumeration enumeration = elements();
        while ( enumeration.hasMoreElements() )
        {
            add( (WPdfField) enumeration.nextElement() );
        }
    }

    /**
     * @return une enumeration des pdffields
     */
    public Enumeration elements()
    {
        return h.elements();
    }

    /**
     * @param name la chainee teste
     * @return true si on contient une cle avec le nom name
     */
    public boolean contains( final String name )
    {
        return h.containsKey( name );
    }

    /**
     * @param name le nom du field
     * @return le WPdfField
     * @throws WPdfFieldException exception pouvant etre levee
     */
    public WPdfField getField( final String name )
        throws WPdfFieldException
    {
        if ( h.containsKey( name ) )
        {
            return (WPdfField) h.get( name );
        }
        else
        {
            throw new WPdfFieldException( "Champs '" + name + "' non disponible dans le document" );
        }
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return h.toString();
    }

}
