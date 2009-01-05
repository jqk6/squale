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
 * Cr�� le 28 oct. 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.outils.excel;

/**
 * Classe d'exception retourn�e par l' ExcelGenerateur
 * 
 * @author R�my Bouquet
 */
public class ExcelGenerateurException
    extends Exception
{

    /**
     * 
     */
    private static final long serialVersionUID = 5604953364154760625L;

    /**
     * L�v�e un exception si un probleme a la generation Excel
     * 
     * @param arg0 : Libelle du message
     */
    public ExcelGenerateurException( final String arg0 )
    {
        super( arg0 );
    }

    /**
     * L�v�e un exception si un probleme a la generation Excel
     * 
     * @param message : Libelle du message
     * @param cause : Cause
     */
    public ExcelGenerateurException( String message, Throwable cause )
    {
        super( message, cause );
    }

    /**
     * L�v�e un exception si un probleme a la generation Excel
     * 
     * @param cause : Cause
     */
    public ExcelGenerateurException( Throwable cause )
    {
        super( cause );
    }
}