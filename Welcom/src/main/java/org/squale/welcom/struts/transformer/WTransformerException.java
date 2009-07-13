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
 * Cr�� le 25 mai 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package org.squale.welcom.struts.transformer;

import javax.servlet.ServletException;

/**
 * @author M325379 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class WTransformerException
    extends ServletException
{

    /**
     * 
     */
    private static final long serialVersionUID = -2151208544577413416L;

    /**
     * Probleme sur la conversion du BO/DTO en AcftionForm (struts) ou inverssement
     */
    public WTransformerException()
    {
        super();
    }

    /**
     * Probleme sur la conversion du BO/DTO en AcftionForm (struts) ou inverssement
     * 
     * @param arg0 : Message
     */
    public WTransformerException( final String arg0 )
    {
        super( arg0 );
    }

    /**
     * Probleme sur la conversion du BO/DTO en AcftionForm (struts) ou inverssement
     * 
     * @param arg0 Message
     * @param arg1 throwable
     */
    public WTransformerException( final String arg0, final Throwable arg1 )
    {
        super( arg0, arg1 );
    }

    /**
     * Probleme sur la conversion du BO/DTO en AcftionForm (struts) ou inverssement
     * 
     * @param arg0 throwable
     */
    public WTransformerException( final Throwable arg0 )
    {
        super( arg0 );
    }
}