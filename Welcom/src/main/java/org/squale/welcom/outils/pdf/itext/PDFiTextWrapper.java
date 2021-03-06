/**
 * Copyright (C) 2008-2010, Squale Project - http://www.squale.org
 *
 * This file is part of Squale.
 *
 * Squale is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * Squale is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Squale.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.squale.welcom.outils.pdf.itext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.squale.welcom.outils.pdf.PDFData;
import org.squale.welcom.outils.pdf.PDFGenerateur;
import org.squale.welcom.outils.pdf.PDFGenerateurException;
import org.squale.welcom.outils.pdf.advanced.WPdfDecoration;
import org.squale.welcom.outils.pdf.advanced.WPdfMerge;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfWriter;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class PDFiTextWrapper
    implements PDFGenerateur
{

    /** La Pdf generateur */
    private PDFData pdfData;

    /** Stream de sortie pour la generation */
    private OutputStream ios = null;

    /** Le Byte array de stockage */
    private ByteArrayOutputStream baos = null;

    /** Taille du flux de sortie */
    protected int outputSize = 0;

    /** Les entete / en pied */
    private WPdfDecoration decoration = null;

    /** Le report Itext */
    protected Document report = null;

    /** Le writter */
    protected PdfWriter writer = null;

    /**
     * (non-Javadoc)
     * 
     * @see org.squale.welcom.outils.pdf.PDFGenerateur#open(java.io.OutputStream)
     */
    public void open( final OutputStream os )
        throws PDFGenerateurException
    {
        report = new Document();
        if ( os == null )
        {
            throw new PDFGenerateurException( "Le stream de sortie est null sur le print PDF" );
        }
        else
        {
            ios = os;
            try
            {

                baos = new ByteArrayOutputStream();
                writer = PdfWriter.getInstance( report, baos );

            }
            catch ( final DocumentException de )
            {
                throw new PDFGenerateurException( "Erreur lors de la cr�ation du document" + de.getMessage() );
            }

        }
    }

    /**
     * Chargement du template
     * 
     * @param is : Liste des InputStream
     * @throws PDFGenerateurException : Leve l'exception si le fichier est null
     */
    public void loadTemplate( final InputStream is )
        throws PDFGenerateurException
    {

    }

    /**
     * @see org.squale.welcom.outils.pdf.PDFGenerateur#close()
     */
    public void close()
        throws PDFGenerateurException
    {

        report.close();

        final WPdfMerge pdfMerge = new WPdfMerge( ios );
        if ( decoration != null )
        {
            pdfMerge.setDecoration( decoration );
        }

        try
        {
            pdfMerge.add( baos.toByteArray() );
            pdfMerge.close();
        }
        catch ( final DocumentException e )
        {
            e.printStackTrace();
        }
        catch ( final IOException e )
        {
            e.printStackTrace();
        }

    }

    /**
     * Sp�cifie le flux de sortie
     * 
     * @param pOs : Flux
     */
    public void setOutputStream( final OutputStream pOs )
    {
        this.ios = pOs;
    }

    /**
     * fabrication du rapport et envoi du rapport dans le outputStream
     * 
     * @throws PDFGenerateurException : Verifie si le template a charger ou stream nulle
     */
    public void printPDF()
        throws PDFGenerateurException
    {

        close();
    }

    /**
     * @return Report ...
     */
    public Object getReport()
    {
        return report;
    }

    /**
     * @see org.squale.welcom.outils.pdf.PDFGenerateur#getPDFWriter()
     */
    public Object getPDFWriter()
    {
        return writer;
    }

    /**
     * @see org.squale.welcom.outils.pdf.PDFGenerateur#setDecoration(org.squale.welcom.outils.pdf.advanced.WPdfDecoration)
     */
    public void setDecoration( final WPdfDecoration pdecoration )
        throws PDFGenerateurException
    {
        decoration = pdecoration;

    }

    /**
     * @param data le PDF Data
     */
    public void setPdfData( final PDFData data )
    {
        pdfData = data;
    }

}