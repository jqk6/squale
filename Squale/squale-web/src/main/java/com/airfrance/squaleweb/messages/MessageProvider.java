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
package com.airfrance.squaleweb.messages;

/**
 * Fournisseur de message Cette interface est utilis�e pour la fourniture de message venant de la base de donn�es
 */
public interface MessageProvider
{
    /**
     * Obtention d'un message Si le message n'est pas trouv� dans la langue demand�e, la langue par d�faut est utilis�e
     * 
     * @param pLang langue
     * @param pKey clef
     * @return message ou null si non trouv�
     */
    public String getMessage( String pLang, String pKey );

}
