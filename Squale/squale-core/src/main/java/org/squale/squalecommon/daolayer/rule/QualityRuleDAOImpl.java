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
 * Cr�� le 29 juin 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package org.squale.squalecommon.daolayer.rule;

import org.squale.jraf.commons.exception.JrafDaoException;
import org.squale.jraf.provider.persistence.hibernate.AbstractDAOImpl;
import org.squale.squalecommon.enterpriselayer.businessobject.rule.QualityRuleBO;

/**
 * @author M400843
 */
public class QualityRuleDAOImpl
    extends AbstractDAOImpl
{
    /**
     * Instance singleton
     */
    private static QualityRuleDAOImpl instance = null;

    /** initialisation du singleton */
    static
    {
        instance = new QualityRuleDAOImpl();
    }

    /**
     * Constructeur prive
     * 
     * @throws JrafDaoException
     */
    private QualityRuleDAOImpl()
    {
        initialize( QualityRuleBO.class );
    }

    /**
     * Retourne un singleton du DAO
     * 
     * @return singleton du DAO
     */
    public static QualityRuleDAOImpl getInstance()
    {
        return instance;
    }
}
