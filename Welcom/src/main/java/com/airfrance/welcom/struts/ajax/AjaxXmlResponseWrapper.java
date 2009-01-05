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
/*
 * Cr�� le 28 juil. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.struts.ajax;

import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.welcom.outils.WelcomConfigurator;

/**
 * @author R�my Bouquet/Arnaud Lehmann Wrapper pour construire une r�ponse Http xml pour l'impl�mentation Ajax.
 */
public class AjaxXmlResponseWrapper
{
    /** logger */
    private static Log log = LogFactory.getLog( AjaxXmlResponseWrapper.class );

    /** D�fini le tag racine de la r�ponse XML */
    protected String rootTag = "roottag";

    /** Le string buffer d'�criture */
    protected StringBuffer buffer;

    /** la response HTTP */
    protected HttpServletResponse response;

    /** vrai si le buffer est vide */
    private boolean bufferEmpty = true;

    /**
     * Construceur
     * 
     * @param presponse reponse HTTP
     */
    public AjaxXmlResponseWrapper( final HttpServletResponse presponse )
    {
        response = presponse;
        buffer = new StringBuffer();
        beginTag( rootTag );
    }

    /**
     * Construceur
     * 
     * @param presponse reponse HTTP
     * @param pRootTag nom du tag racine de la r�ponse XML
     */
    public AjaxXmlResponseWrapper( final HttpServletResponse presponse, final String pRootTag )
    {
        rootTag = pRootTag;
        response = presponse;
        buffer = new StringBuffer();
        beginTag( rootTag );
    }

    /**
     * ajoute un tag de d�but
     * 
     * @param tag tag � formater
     */
    protected void beginTag( final String tag )
    {
        buffer.append( "<" + tag + ">" );
    }

    /**
     * ajoute un tag de fin
     * 
     * @param tag tag � formater
     */
    protected void endTag( final String tag )
    {
        buffer.append( "</" + tag + ">" );
    }

    /**
     * ajoute un item dans le buffer
     * 
     * @param tag le nom du tag item
     * @param content contenu du tag
     */
    public void addItem( final String tag, final String content )
    {
        bufferEmpty = false;
        beginTag( tag );
        buffer.append( content );
        endTag( tag );
    }

    /**
     * ecrit le flux xml dans la response et ferme le flux On envoi systematiquement le tag root pour corriger des
     * problemes IE sous UNIX quand rien ne matche .. (il bloque)
     * 
     * @throws IOException exception d'entr�e sortie
     */
    public void close()
        throws IOException
    {
        final String charset = WelcomConfigurator.getMessage( WelcomConfigurator.ENCODING_CHARSET );
        response.setHeader( "Content-Type", "text/xml; charset=" + charset );
        response.setHeader( "Cache-Control", "no-cache" );

        final OutputStream out = response.getOutputStream();
        endTag( rootTag );
        // if (!bufferEmpty) {
        out.write( buffer.toString().getBytes( charset ) );
        // }
        try
        {
            out.close();
        }
        catch ( final SocketException se )
        {
            log.debug( "requ�te tu�e par le browser" );
        }
    }

    /**
     * @return rootTag
     */
    public String getRootTag()
    {
        return rootTag;
    }

    /**
     * @param string rootTag
     */
    public void setRootTag( final String string )
    {
        rootTag = string;
    }

}
