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
 * Cr�� le 6 juil. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.squalecommon.daolayer.profile;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.jraf.provider.persistence.hibernate.AbstractDAOImpl;
import com.airfrance.squalecommon.enterpriselayer.businessobject.profile.AtomicRightsBO;

/**
 * @author M400843
 */
public class AtomicRightsDAOImpl
    extends AbstractDAOImpl
{
    /**
     * Instance singleton
     */
    private static AtomicRightsDAOImpl instance = null;

    /** initialisation du singleton */
    static
    {
        instance = new AtomicRightsDAOImpl();
    }

    /**
     * Constructeur prive
     * 
     * @throws JrafDaoException
     */
    private AtomicRightsDAOImpl()
    {
        initialize( AtomicRightsBO.class );
    }

    /**
     * Retourne un singleton du DAO
     * 
     * @return singleton du DAO
     */
    public static AtomicRightsDAOImpl getInstance()
    {
        return instance;
    }
}
