/*
 * Cr�� le 8 janv. 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.jraf.spi.persistence;

import com.airfrance.jraf.commons.exception.JrafConfigException;
import com.airfrance.jraf.commons.exception.JrafDaoException;

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
