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
 * Cr�� le 25 juil. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.addons.access.excel.bd;

/**
 * @author M327837 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class BdAccessFactory
{

    /** Instance du bd reader */
    private static BdAccessReader bdReader = null;

    /** Instance du bd create */
    private static BdAccessCreate bdCreate = null;

    /** Instance du bd update */
    private static BdAccessUpdate bdUpdate = null;

    /**
     * Recupere l'instance du BD reader
     * 
     * @return Lecteur de BD
     */
    public static BdAccessReader getBdReader()
    {
        if ( bdReader == null )
        {
            bdReader = new BdAccessReader();
        }
        return bdReader;
    }

    /**
     * Recupere l'instance du BD create
     * 
     * @return Creation de BD
     */
    public static BdAccessCreate getBdCreate()
    {
        if ( bdCreate == null )
        {
            bdCreate = new BdAccessCreate();
        }
        return bdCreate;
    }

    /**
     * Recupere l'instance du BD create
     * 
     * @return Mise a jour BD
     */
    public static BdAccessUpdate getBdUpdate()
    {
        if ( bdUpdate == null )
        {
            bdUpdate = new BdAccessUpdate();
        }
        return bdUpdate;
    }

}
