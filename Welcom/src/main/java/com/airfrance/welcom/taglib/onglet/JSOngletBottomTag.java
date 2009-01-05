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
 * Cr�� le 17 mai 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.taglib.onglet;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

/**
 * @author M327837 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class JSOngletBottomTag
    extends BodyTagSupport
{

    /**
     * 
     */
    private static final long serialVersionUID = 6696913801428322803L;

    /**
     * Constructeur
     */
    public JSOngletBottomTag()
    {
        super();
    }

    /**
     * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
     */
    public int doEndTag()
        throws JspException
    {
        // Recherche si un parent est du bon type
        Tag curParent = null;

        for ( curParent = getParent(); ( curParent != null ) && !( curParent instanceof JSOngletTag ); )
        {
            curParent = curParent.getParent();
        }

        final JSOngletTag ongletTag = (JSOngletTag) curParent;

        if ( ongletTag == null )
        {
            throw new JspException( "JSOngletBottomTag  must be used between JSOngletTag." );
        }

        ongletTag.addBottomValue( getBodyContent().getString() );

        return EVAL_PAGE;
    }
}