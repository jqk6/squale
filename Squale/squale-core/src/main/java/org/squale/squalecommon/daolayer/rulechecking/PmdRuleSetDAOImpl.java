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
package org.squale.squalecommon.daolayer.rulechecking;

import java.util.Collection;
import java.util.Iterator;

import org.squale.jraf.commons.exception.JrafDaoException;
import org.squale.jraf.provider.persistence.hibernate.AbstractDAOImpl;
import org.squale.jraf.spi.persistence.ISession;
import org.squale.squalecommon.enterpriselayer.businessobject.rulechecking.pmd.PmdRuleSetBO;

/**
 * DAO pour le ruleset PMD
 */
public class PmdRuleSetDAOImpl
    extends AbstractDAOImpl
{

    /**
     * Instance singleton
     */
    private static PmdRuleSetDAOImpl instance = null;

    /**
     * initialisation du singleton
     */
    static
    {
        instance = new PmdRuleSetDAOImpl();
    }

    /**
     * Constructeur priv�
     * 
     * @throws JrafDaoException
     */
    private PmdRuleSetDAOImpl()
    {
        initialize( PmdRuleSetBO.class );
    }

    /**
     * Retourne un singleton du DAO
     * 
     * @return singleton du DAO
     */
    public static PmdRuleSetDAOImpl getInstance()
    {
        return instance;
    }

    /**
     * Cr�ation d'une version de configuration PMD
     * 
     * @param pSession session
     * @param pRuleSet jeu de r�gles
     * @return PmdRuleSetBO l'objet cr�� dans la base ou null si l'unicit� n'est pas respect�e
     * @throws JrafDaoException exception JRAF
     */

    public PmdRuleSetBO createPmdRuleSet( ISession pSession, PmdRuleSetBO pRuleSet )
        throws JrafDaoException
    {
        create( pSession, pRuleSet );
        return pRuleSet;
    }

    /**
     * R�cup�ration d'un ruleset � partir du nom et de la version
     * 
     * @param pSession session
     * @param pName nom
     * @param pLanguage version
     * @return ruleset ou null si non trouv�
     * @throws JrafDaoException si erreur
     */
    public PmdRuleSetBO findRuleSet( ISession pSession, String pName, String pLanguage )
        throws JrafDaoException
    {
        PmdRuleSetBO result = null;
        StringBuffer whereClause = new StringBuffer();
        whereClause.append( "where " );
        PmdRuleSetBO version = null;
        whereClause.append( getAlias() ).append( ".name = '" ).append( pName ).append( "'" );
        whereClause.append( " and " ).append( getAlias() ).append( ".language= '" ).append( pLanguage ).append( "'" );
        whereClause.append( " order by " ).append( getAlias() ).append( ".dateOfUpdate DESC" );
        Collection col = findWhere( pSession, whereClause.toString() );
        if ( col.size() > 0 )
        {
            // On renvoie le premier, normalement la taille de la collection
            // est de 1
            result = (PmdRuleSetBO) col.iterator().next();
        }
        return result;
    }

    /**
     * Destruction de rulesets
     * 
     * @param pSession session
     * @param pRuleSetsId ids des rulesets
     * @throws JrafDaoException si erreur
     */
    public void removePmdRuleSets( ISession pSession, Collection pRuleSetsId )
        throws JrafDaoException
    {
        StringBuffer whereClause = new StringBuffer( "where " );
        whereClause.append( getAlias() );
        whereClause.append( ".id in(" );
        Iterator ruleSetsIdIt = pRuleSetsId.iterator();
        boolean comma = false;
        // Parcours des ids de ruleset pour construire la liste dans la requ�te
        while ( ruleSetsIdIt.hasNext() )
        {
            if ( comma )
            {
                whereClause.append( ", " );
            }
            else
            {
                comma = true;
            }
            whereClause.append( ruleSetsIdIt.next() );
        }
        whereClause.append( ")" );
        Iterator ruleSetsIt = findWhere( pSession, whereClause.toString() ).iterator();
        // Suppression de chaque jeu de r�gles
        while ( ruleSetsIt.hasNext() )
        {
            remove( pSession, ruleSetsIt.next() );
        }
    }
}
