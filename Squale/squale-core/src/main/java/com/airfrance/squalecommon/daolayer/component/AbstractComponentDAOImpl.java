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
 * Cr�� le 8 juil. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.squalecommon.daolayer.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.jraf.provider.persistence.hibernate.AbstractDAOImpl;
import com.airfrance.jraf.provider.persistence.hibernate.SessionImpl;
import com.airfrance.jraf.spi.persistence.ISession;
import com.airfrance.squalecommon.daolayer.DAOMessages;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AbstractComponentBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squalecommon.util.mapping.Mapping;

/**
 * @author M400843
 */
public class AbstractComponentDAOImpl
    extends AbstractDAOImpl
{
    /**
     * Instance singleton
     */
    private static AbstractComponentDAOImpl instance = null;

    /** log */
    private static Log LOG;

    /** initialisation du singleton */
    static
    {
        instance = new AbstractComponentDAOImpl();
    }

    /**
     * Constructeur prive
     * 
     * @throws JrafDaoException
     */
    private AbstractComponentDAOImpl()
    {
        initialize( AbstractComponentBO.class );
        if ( null == LOG )
        {
            LOG = LogFactory.getLog( AbstractComponentDAOImpl.class );
        }
    }

    /**
     * Retourne un singleton du DAO
     * 
     * @return singleton du DAO
     */
    public static AbstractComponentDAOImpl getInstance()
    {
        return instance;
    }

    /**
     * R�cup�re les enfants de pProjet selon un audit et un type
     * 
     * @param pSession la session
     * @param pProjet le projet dont on veut les enfants
     * @param pAudit l'audit
     * @param pClass la classe du type dont on veut les enfants
     * @return une collection d'enfants du projet du type demand�
     * @throws JrafDaoException si une erreur � lieu
     */
    public Collection findProjectChildren( ISession pSession, ProjectBO pProjet, AuditBO pAudit, Class pClass )
        throws JrafDaoException
    {
        LOG.debug( DAOMessages.getString( "dao.entry_method" ) );
        Collection children = new ArrayList();

        // si la type d'enfant demand� est ApplicationBO, on retourne le projet
        if ( pClass.isAssignableFrom( ApplicationBO.class ) )
        {
            children.add( pProjet );
        }
        else
        {
            // R�cup�ration de tout les composants �tant rattach� � l'audit concern�
            String whereClause = "where ";
            whereClause += pAudit.getId() + " in elements(" + getAlias() + ".audits)";

            Iterator it = findWhere( pSession, whereClause ).iterator();
            while ( it.hasNext() )
            {
                // Pour chaque enfant
                AbstractComponentBO child = (AbstractComponentBO) it.next();
                if ( pClass.isInstance( child ) )
                {
                    /*
                     * Si l'enfant est du type souhait�, on v�rifie qu'il est bien enfant du projet demand�
                     * (indispensable si l'application � plusieurs projets)
                     */
                    AbstractComponentBO tmpChild = child.getParent();
                    // tant que l'on a pas trouv� le projet et qu'il existe un parent
                    while ( ( null != tmpChild ) )
                    {
                        if ( tmpChild.equals( pProjet ) )
                        {
                            // si on a trouv� le projet, on ajoute l'enfant � la collection de retour
                            children.add( child );
                            // et on met tmpChild pour stopper le while
                            tmpChild = null;
                        }
                        else
                        {
                            // sinon on r�cup�re le parent suivant (parent du parent...)
                            tmpChild = tmpChild.getParent();
                        }
                    }
                }
            }
        }
        LOG.debug( DAOMessages.getString( "dao.exit_method" ) );
        return children;
    }

    /**
     * R�cup�re le composant dont le nom est pName et dont le parent est pParent.
     * 
     * @param pSession la session
     * @param pParent le parent du composant que l'on veut
     * @param pName le nom du composant � chercher
     * @return le composant recherch� ou null si il n'est pas pr�sent en base.
     * @throws JrafDaoException si une erreur � lieu
     */
    public AbstractComponentBO findChild( ISession pSession, AbstractComponentBO pParent, String pName )
        throws JrafDaoException
    {
        LOG.debug( DAOMessages.getString( "dao.entry_method" ) + " findChild" );
        AbstractComponentBO child = null;
        String whereClause = "where ";
        whereClause += getAlias() + ".parent=" + pParent.getId();
        whereClause += "and " + getAlias() + ".name='" + pName + "'";
        Iterator it = findWhere( pSession, whereClause ).iterator();
        if ( it.hasNext() )
        {
            child = (AbstractComponentBO) it.next();
        }
        LOG.debug( DAOMessages.getString( "dao.exit_method" ) + " findChild" );
        return child;
    }

    /**
     * @param pSession la session
     * @return les composants exclus du plan d'action
     * @throws JrafDaoException en cas d'�chec
     */
    public Collection getExcludedFromPlan( ISession pSession )
        throws JrafDaoException
    {
        Collection components = new ArrayList( 0 );
        String whereClause = "where " + getAlias() + ".excludedFromActionPlan=1";
        components = findWhere( pSession, whereClause );
        return components;
    }

    /**
     * @param pSession la session
     * @param pParentId l'id du parent
     * @param pAuditId l'id de l'audit, peut �tre <code>null</code>
     * @param pType le type de composants, peut �tre <code>null</code>
     * @param pFilter le filtre sur le nom, peut �tre <code>null</code>
     * @return les enfants (1000 au max) dont le parent a l'id <code>pParentId</code> de type <code>pType</code>
     *         rattach�s � l'audit d'id <code>pAuditId</code> et dont le nom correspond au pattern
     *         <code>*pFilter*</code>
     * @throws JrafDaoException si erreur
     */
    public Collection findChildrenWhere( ISession pSession, Long pParentId, Long pAuditId, String pType, String pFilter )
        throws JrafDaoException
    {
        final int nbLines = 1000;
        StringBuffer whereClause =
            new StringBuffer( getCommonWhereChildrenRequest( pParentId, pAuditId, pType, pFilter ) );
        whereClause.append( " order by " );
        whereClause.append( getAlias() );
        whereClause.append( ".name" );
        LOG.warn( "findChildrenWhere: " + whereClause );
        return (Collection) findWhereScrollable( pSession, whereClause.toString(), nbLines, 0, false );
    }

    /**
     * @param pSession la session
     * @param pParentId l'id du parent
     * @param pAuditId l'id de l'audit, peut �tre <code>null</code>
     * @param pType le type de composants, peut �tre <code>null</code>
     * @param pFilter le filtre sur le nom, peut �tre <code>null</code>
     * @return le nombre d'enfants dont le parent a l'id <code>pParentId</code> de type <code>pType</code> rattach�s
     *         � l'audit d'id <code>pAuditId</code> et dont le nom correspond au pattern <code>*pFilter*</code>
     * @throws JrafDaoException si erreur
     */
    public Integer countChildrenWhere( ISession pSession, Long pParentId, Long pAuditId, String pType, String pFilter )
        throws JrafDaoException
    {
        return countWhere( pSession, getCommonWhereChildrenRequest( pParentId, pAuditId, pType, pFilter ) );
    }

    /**
     * @param pParentId l'id du parent
     * @param pAuditId l'id de l'audit, peut �tre <code>null</code>
     * @param pType le type de composants, peut �tre <code>null</code>
     * @param pFilter le filtre sur le nom, peut �tre <code>null</code>
     * @return la clause where de recherche des enfants selon les crit�res pass�s en param�tre de type
     *         <code>pType</code> rattach�s � l'audit d'id <code>pAuditId</code> et dont le nom correspond au
     *         pattern <code>*pFilter*</code>
     */
    private String getCommonWhereChildrenRequest( Long pParentId, Long pAuditId, String pType, String pFilter )
    {
        StringBuffer whereClause = new StringBuffer( "where " );
        whereClause.append( getAlias() + ".parent.id=" + pParentId );
        if ( null != pAuditId )
        {
            whereClause.append( " and " );
            whereClause.append( pAuditId + " in elements (" + getAlias() + ".audits)" );
        }
        if ( null != pType )
        {
            // On r�cup�re le nom de la classe
            String className = Mapping.getComponentMappingName( pType );
            whereClause.append( " and " );
            whereClause.append( getAlias() + ".class='" + className + "'" );
        }
        if ( null != pFilter )
        {
            whereClause.append( " and " );
            whereClause.append( getAlias() + ".name like '%" + pFilter + "%'" );
        }
        return whereClause.toString();
    }

    /**
     * Get components which have somme values for some tres
     * 
     * @param pSession session
     * @param pProjectId project id
     * @param pAuditId audit id
     * @param pTreKeys tres
     * @param pTreValues value of tres
     * @param pMax number of result to get
     * @return list of components
     * @throws JrafDaoException if error in database
     */
    public List<AbstractComponentBO> findWhereTres( ISession pSession, long pProjectId, long pAuditId,
                                                    String[] pTreKeys, String[] pTreValues, Integer pMax )
        throws JrafDaoException
    {
        SessionImpl sessionHibernate = (SessionImpl) pSession;
        List<AbstractComponentBO> results = new ArrayList<AbstractComponentBO>();
        String from = "";
        String distinct = "";
        String where = "";
        // Creation de la requete
        // /!\ Requete optimis�e car longue en prod
        // TOUTES MODIF DOIT ETRE TESTEES EN FAISANT LE SELECT EN PROD !!
        // Cette requ�te prend moins d'1 seconde en production

        for ( int i = 0; i < pTreKeys.length; i++ )
        {
            // selecte sur n metric
            // => construction des selects, from, et where de la query
            if ( i > 0 )
            {
                distinct += ",";
            }
            from += ",IntegerMetricBO metric" + i;
            where +=
                " and metric" + i + ".measure.audit.id=" + pAuditId + " and metric" + i + ".measure.component.id="
                    + getAlias() + ".id" + " and metric" + i + ".measure.class="
                    + Mapping.getMetricClass( pTreKeys[i] ).getName() + " and metric" + i + ".name = '"
                    + pTreKeys[i].substring( pTreKeys[i].lastIndexOf( '.' ) + 1 ) + "'" + " and metric" + i + ".value="
                    + pTreValues[i];
        }
        // execution de la requete
        try
        {
            // remonte les valeurs distinctes en une seule requete pour des raisons de perf
            Query q =
                sessionHibernate.getSession().createQuery( "select " + getAlias() + " "
                // On ne recherche pas le type du composant afin
                    // d'optimiser la requ�te car la recherche
                    // est d�j� faite par le type de la m�trique.
                    + getRequete() + from + " where (" + getAlias() + ".project.id =" + pProjectId + ")" + where ).setMaxResults(
                                                                                                                                  pMax );
            results = q.list();
        }
        catch ( HibernateException e )
        {
            throw new JrafDaoException( e );
        }

        return results;
    }
}
