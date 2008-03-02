package com.airfrance.squalecommon.daolayer.rulechecking;

import java.util.Collection;
import java.util.Iterator;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.jraf.provider.persistence.hibernate.AbstractDAOImpl;
import com.airfrance.jraf.spi.persistence.ISession;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking.cpptest.CppTestRuleSetBO;

/**
 * DAO pour les r�gles CppTest
 */
public class CppTestRuleSetDAOImpl
    extends AbstractDAOImpl
{

    /**
     * Instance singleton
     */
    private static CppTestRuleSetDAOImpl instance = null;

    /**
     * initialisation du singleton
     */
    static
    {
        instance = new CppTestRuleSetDAOImpl();
    }

    /**
     * Constructeur priv�
     * 
     * @throws JrafDaoException
     */
    private CppTestRuleSetDAOImpl()
    {
        initialize( CppTestRuleSetBO.class );
    }

    /**
     * Retourne un singleton du DAO
     * 
     * @return singleton du DAO
     */
    public static CppTestRuleSetDAOImpl getInstance()
    {
        return instance;
    }

    /**
     * Cr�ation d'une version de configuration CppTest
     * 
     * @param pSession session
     * @param pRuleSet jeu de r�gles
     * @return CppTestRuleSetBO l'objet cr�� dans la base ou null si l'unicit� n'est pas respect�e
     * @throws JrafDaoException exception JRAF
     */

    public CppTestRuleSetBO createCppTestRuleSet( ISession pSession, CppTestRuleSetBO pRuleSet )
        throws JrafDaoException
    {
        create( pSession, pRuleSet );
        return pRuleSet;
    }

    /**
     * R�cup�ration d'un ruleset � partir du nom
     * 
     * @param pSession session
     * @param pName nom
     * @return rulset ou null si non trouv�
     * @throws JrafDaoException si erreur
     */
    public CppTestRuleSetBO findRuleSet( ISession pSession, String pName )
        throws JrafDaoException
    {
        CppTestRuleSetBO result = null;
        StringBuffer whereClause = new StringBuffer();
        CppTestRuleSetBO version = null;
        whereClause.append( "where " ).append( getAlias() ).append( ".name='" ).append( pName ).append( "'" );
        whereClause.append( " order by " ).append( getAlias() ).append( ".dateOfUpdate DESC" );
        Collection col = findWhere( pSession, whereClause.toString() );
        if ( col.size() > 0 )
        {
            // On renvoie le premier, normalement la taille de la collection
            // est de 1
            result = (CppTestRuleSetBO) col.iterator().next();
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
    public void removeCppTestRuleSets( ISession pSession, Collection pRuleSetsId )
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
