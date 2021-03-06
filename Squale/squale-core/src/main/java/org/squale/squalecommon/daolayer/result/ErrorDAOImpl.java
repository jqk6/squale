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
package org.squale.squalecommon.daolayer.result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.squale.jraf.commons.exception.JrafDaoException;
import org.squale.jraf.provider.persistence.hibernate.AbstractDAOImpl;
import org.squale.jraf.spi.persistence.ISession;
import org.squale.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import org.squale.squalecommon.enterpriselayer.businessobject.result.ErrorBO;

/**
 * @author M400843
 */
public class ErrorDAOImpl
    extends AbstractDAOImpl
{
    /**
     * Instance singleton
     */
    private static ErrorDAOImpl instance = null;

    /** initialisation du singleton */
    static
    {
        instance = new ErrorDAOImpl();
    }

    /**
     * Constructeur prive
     * 
     * @throws JrafDaoException
     */
    private ErrorDAOImpl()
    {
        initialize( ErrorBO.class );
    }

    /**
     * Retourne un singleton du DAO
     * 
     * @return singleton du DAO
     */
    public static ErrorDAOImpl getInstance()
    {
        return instance;
    }

    /**
     * Supprime toutes les erreur li�es � un audit
     * 
     * @param pSession la session
     * @param pAudit l'audit
     * @throws JrafDaoException si une erreur � lieu
     */
    public void removeWhereAudit( ISession pSession, AuditBO pAudit )
        throws JrafDaoException
    {
        String whereClause = "where ";
        whereClause += getAlias() + ".audit.id = " + pAudit.getId();

        removeWhere( pSession, whereClause );
    }

    /**
     * Supprime toutes les erreurs li�es � un projet
     * 
     * @param pSession la session
     * @param pProjet le projet
     * @throws JrafDaoException si une erreur � lieu
     */
    public void removeWhereProject( ISession pSession, ProjectBO pProjet )
        throws JrafDaoException
    {
        String whereClause = "where ";
        whereClause += getAlias() + ".project.id = " + pProjet.getId();

        removeWhere( pSession, whereClause );
    }

    /**
     * Permet de recuperer une collection de ErrorBO pour un audit et une liste de noms de taches donn�s
     * 
     * @param pSession session Hibernate
     * @param pAuditID identifiant de l'audit
     * @param pProjectId identifiant du projet
     * @param pTasks liste de nom de taches, si <code>null</code> recherche pour toutes les taches
     * @param pNbLignes nombre de lignes, si <code>null</code> retourne toutes les erreurs trouv�es
     * @param pIndexDepart index de depart
     * @return Collection de ErrorDTO complete (vide mais instanci�e si pas d'errorBO en base)
     * @throws JrafDaoException exception Dao
     */
    public Collection findWhere( ISession pSession, Long pAuditID, Long pProjectId, Collection pTasks,
                                 Integer pNbLignes, Integer pIndexDepart )
        throws JrafDaoException
    {
        List errors;
        int startId = 0;
        if ( null != pIndexDepart )
        {
            startId = pIndexDepart.intValue();
        }
        // Cr�ation de la requete :
        // s�lection pour l'audit demand� et le projet
        String whereClause = getWhereProjectAndAudit( pAuditID, pProjectId );
        if ( null == pTasks )
        {
            // si aucune tache n'est pr�cis� on r�cup�re les erreurs
            errors = (List) findWhere( pSession, whereClause );
        }
        else
        {
            // sinon on ajoute dans la requete le nom de la tache :
            whereClause += " and ";
            whereClause += getAlias() + ".taskName = ";
            errors = new ArrayList();
            Iterator it = pTasks.iterator();
            while ( it.hasNext() )
            {
                // Pour chaque tache : on r�cup�re les erreurs concern�es
                List col;
                String taskName = (String) it.next();
                String newWhereClause = whereClause + "'" + taskName + "'";
                // On tri par id --> ordre d'enregistrement
                newWhereClause += " order by " + getAlias() + ".id";
                if ( null != pNbLignes )
                {
                    col = (List) findWhereScrollable( pSession, newWhereClause, pNbLignes, startId, false );
                }
                else
                {
                    col = (List) findWhere( pSession, newWhereClause );
                }
                if ( null != col && col.size() != 0 )
                {
                    // on ajoute les erreurs qui viennent d'etre r�cup�r�es � la collection
                    errors.addAll( col );
                }
            }
        }
        return errors;
    }

    /**
     * Permet de recuperer une collection de ErrorBO pour un audit et une liste de noms de taches donn�s
     * 
     * @param pSession session Hibernate
     * @param pAuditID identifiant de l'audit
     * @param pProjetId identifiant du projet
     * @param pTasks liste de nom de taches, si <code>null</code> recherche pour toutes les taches
     * @return Collection de ErrorDTO complete
     * @throws JrafDaoException exception Dao
     */
    public Collection findWhereAuditAndTasks( ISession pSession, Long pAuditID, Long pProjetId, Collection pTasks )
        throws JrafDaoException
    {
        Collection ret = findWhere( pSession, pAuditID, pProjetId, pTasks, null, null );
        return ret;
    }

    /**
     * Cr�e l'erreur en v�rifiant que son projet est bien reli� � un Audit
     * 
     * @see org.squale.jraf.spi.persistence.IPersistenceDAO#create(org.squale.jraf.spi.persistence.ISession,
     *      java.lang.Object)
     */
    public void create( ISession pSession, ErrorBO pError )
        throws JrafDaoException
    {
        super.create( pSession, pError );
    }

    /**
     * @param pSession la session
     * @param pAuditId l'id de l'audit sur lequel on compte les erreurs
     * @param pProjectId l'id du projet sur lequel on compte les erreurs
     * @param pLevel le niveau des erreurs (peut �tre nul si on veut le nombre total des erreur)
     * @return le nombre d'erreurs par type
     * @throws JrafDaoException en cas d'�chec
     */
    public Integer getNumberOfErrorsWhere( ISession pSession, Long pAuditId, Long pProjectId, String pLevel )
        throws JrafDaoException
    {
        StringBuffer whereClause = new StringBuffer( getWhereProjectAndAudit( pAuditId, pProjectId ) );
        if ( null != pLevel )
        {
            whereClause.append( " and " );
            whereClause.append( getAlias() );
            whereClause.append( ".level='" );
            whereClause.append( pLevel );
            whereClause.append( "'" );
        }
        return countWhere( pSession, whereClause.toString() );
    }

    /**
     * @param pSession la session
     * @param pAuditId l'id de l'audit sur lequel on compte les erreurs
     * @param pProjectId l'id du projet sur lequel on compte les erreurs
     * @param pLevel le niveau des erreurs (peut �tre nul si on veut les noms des t�ches en erreur)
     * @return les t�ches qui ont au moins une erreur avec le niveau de criticit� <code>pLevel</code>
     * @throws JrafDaoException en cas d'�chec
     */
    public List getTasksNameWhere( ISession pSession, Long pAuditId, Long pProjectId, String pLevel )
        throws JrafDaoException
    {
        StringBuffer query = new StringBuffer( "select distinct " );
        query.append( getAlias() );
        query.append( ".taskName " );
        query.append( getRequete() );
        query.append( getWhereProjectAndAudit( pAuditId, pProjectId ) );
        if ( null != pLevel )
        {
            query.append( " and " );
            query.append( getAlias() );
            query.append( ".level='" );
            query.append( pLevel );
            query.append( "'" );
        }
        query.append( "order by " );
        query.append( getAlias() );
        query.append( ".taskName" );
        return find( pSession, query.toString() );
    }

    /**
     * @param pAuditId l'id de l'audit
     * @param pProjectId l'id du projet
     * @return les conditions par rapport � l'audit et au projet � mettre dans une clause Where
     */
    private String getWhereProjectAndAudit( Long pAuditId, Long pProjectId )
    {
        StringBuffer commonWhereClause = new StringBuffer( "where " );
        commonWhereClause.append( getAlias() );
        commonWhereClause.append( ".audit.id=" );
        commonWhereClause.append( pAuditId.longValue() );
        commonWhereClause.append( " and " );
        commonWhereClause.append( getAlias() );
        commonWhereClause.append( ".project.id=" );
        commonWhereClause.append( pProjectId );
        return commonWhereClause.toString();
    }

    /**
     * Get all errors for an audit, a project and a criticity (facultative)
     * @param pSession hibernate session
     * @param pAuditId audit id condition
     * @param pProjectId project id condition
     * @param pLevel level of error criticity (error, warning, info) or null if level has not have to be
     * a condition for search
     * @return list of ErrorBO occured while executing audit on the project
     * @throws JrafDaoException if error occured
     */
    public List findAllWhere( ISession pSession, Long pAuditId, Long pProjectId, String pLevel ) throws JrafDaoException
    {
        StringBuffer whereClause = new StringBuffer( getWhereProjectAndAudit( pAuditId, pProjectId ) );
        if ( pLevel != null )
        {
            whereClause.append( " and " );
            whereClause.append( getAlias() );
            whereClause.append( ".level='" );
            whereClause.append( pLevel );
            whereClause.append( "'" );
        }
        return findWhere( pSession, whereClause.toString() );
    }
}
