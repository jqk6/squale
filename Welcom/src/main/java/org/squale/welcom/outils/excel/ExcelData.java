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
package org.squale.welcom.outils.excel;

import java.util.Locale;

import org.apache.struts.util.MessageResources;

/**
 * Classe de donn�es � exporter dans un ficheir Excel.
 * 
 * @author R�my Bouquet
 */
public abstract class ExcelData
{

    /**
     * Locale Interne
     */
    protected Locale locale = null;

    /**
     * Bundle pour la traduction
     */
    protected MessageResources messages = null;

    /**
     * @param pLocale : c'est la locale de l'application pour l'internationnalisation
     * @param pMessages :un MessageResources initialis� avec le fichier ressource contenant les labels � utiliser dans
     *            le ficheir excel.
     */
    public ExcelData( final Locale pLocale, final MessageResources pMessages )
    {
        this.locale = pLocale;
        this.messages = pMessages;
    }

    /**
     * @return retourne la locale.
     */
    public Locale locale()
    {
        return locale;
    }

    /**
     * @param xlGenerateur : Un Excel g�n�rateur qui permet de formater la sortie du fichier excel.
     * @throws ExcelGenerateurException : Level une erreur sur la production du fichier
     */
    public abstract void fill( ExcelGenerateur xlGenerateur )
        throws ExcelGenerateurException;

    /**
     * permet d'aller chercher un message dans le MessageResources.
     * 
     * @param key : cl� du libell� � aller chercher.
     * @return retourne le libell� correspondant � la cl� pass�e en param�tre.
     */
    public String getMessage( final String key )
    {
        return messages.getMessage( locale, key );
    }
}