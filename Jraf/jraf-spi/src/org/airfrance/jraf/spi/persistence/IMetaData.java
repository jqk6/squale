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
 * Cr�� le 8 janv. 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package org.squale.jraf.spi.persistence;

import org.squale.jraf.commons.exception.JrafConfigException;
import org.squale.jraf.commons.exception.JrafDaoException;

/**
 * @author M312644
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public interface IMetaData {
	
	/**
	 * Retourne le nom de l'attribut identifiant pour la classe pass�e en param�tre.
	 * @param clazz
	 * @return
	 */
	public String getIdentifierName(Class clazz) throws JrafDaoException;

	/**
	 * Retourne la taille de la colonne
	 * @param clazz Nom de la classe
	 * @param columnName Nom de la colonne
	 * @return Taille de la colonne
	 * @throws JrafDaoException
	 */
	public int getColumnMaxSize(Class clazz, String columnName) throws JrafConfigException;
}
