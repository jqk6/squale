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
package org.squale.squalecommon.daolayer.rule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import org.squale.jraf.commons.exception.JrafDaoException;
import org.squale.jraf.provider.persistence.hibernate.AbstractDAOImpl;
import org.squale.jraf.spi.persistence.ISession;
import org.squale.squalecommon.enterpriselayer.businessobject.rule.CriteriumRuleBO;
import org.squale.squalecommon.enterpriselayer.businessobject.rule.FactorRuleBO;
import org.squale.squalecommon.enterpriselayer.businessobject.rule.PracticeRuleBO;
import org.squale.squalecommon.enterpriselayer.businessobject.rule.QualityGridBO;

/**
 * Persistance d'un grille qualit�
 */
public class QualityGridDAOImpl
    extends AbstractDAOImpl
{

    /**
     * Instance singleton
     */
    private static QualityGridDAOImpl instance = null;

    /** initialisation du singleton */
    static
    {
        instance = new QualityGridDAOImpl();
    }

    /**
     * Constructeur prive
     * 
     * @throws JrafDaoException
     */
    private QualityGridDAOImpl()
    {
        initialize( QualityGridBO.class );
    }

    /**
     * Retourne un singleton du DAO
     * 
     * @return singleton du DAO
     */
    public static QualityGridDAOImpl getInstance()
    {
        return instance;
    }

    /**
     * Cr�ation de l'objet m�tier persistent
     * 
     * @param pSession la session
     * @param pGrid la grille � persister
     * @return la grille cr��e si tout s'est bien pass�
     * @throws JrafDaoException si une erreur � lieu
     */
    public QualityGridBO createGrid( ISession pSession, QualityGridBO pGrid )
        throws JrafDaoException
    {
        // il faut cr�er les pratiques et crit�res avant
        QualityRuleDAOImpl ruleDAO = QualityRuleDAOImpl.getInstance();
        SortedSet factors = pGrid.getFactors();
        for ( Iterator itF = factors.iterator(); itF.hasNext(); )
        {
            FactorRuleBO factor = (FactorRuleBO) itF.next();
            // Les crit�res
            SortedMap criteria = factor.getCriteria();
            for ( Iterator itC = criteria.keySet().iterator(); itC.hasNext(); )
            {
                CriteriumRuleBO criterium = (CriteriumRuleBO) itC.next();
                // Les pratiques
                SortedMap practices = criterium.getPractices();
                for ( Iterator itP = practices.keySet().iterator(); itP.hasNext(); )
                {
                    PracticeRuleBO practice = (PracticeRuleBO) itP.next();
                    ruleDAO.create( pSession, practice );
                }
                ruleDAO.create( pSession, criterium );
            }
        }

        super.create( pSession, pGrid );
        return pGrid;
    }

    /**
     * Obtention de la derni�re grille correspondant � un nom
     * 
     * @param pSession session
     * @param pName nom de la grille
     * @return grille correspondante la plus r�cente ou null si elle n'existe pas
     * @throws JrafDaoException si erreur
     */
    public QualityGridBO findWhereName( ISession pSession, String pName )
        throws JrafDaoException
    {
        QualityGridBO result = null;
        String whereClause = "where ";
        whereClause += getAlias() + ".name = '" + pName + "'";
        whereClause +=
            " AND not exists (from QualityGridBO as oldgrid where " + getAlias()
                + ".name=oldgrid.name and oldgrid.dateOfUpdate > " + getAlias() + ".dateOfUpdate)";
        whereClause += "order by " + getAlias() + ".dateOfUpdate";
        Collection col = findWhere( pSession, whereClause );
        if ( col.size() > 0 )
        {
            // On prend le premier �l�ment de la collection qui peut
            // en contenir plusieurs
            result = (QualityGridBO) col.iterator().next();
        }
        return result;
    }

    /**
     * R�cup�re les grilles associ�es � un profil
     * 
     * @param pSession la session
     * @param pProfileId l'id du profil
     * @return les grilles tri�es par ordre alphab�tique
     * @throws JrafDaoException si erreur
     */
    public Collection findWhereProfile( ISession pSession, long pProfileId )
        throws JrafDaoException
    {
        Collection results = new ArrayList();
        String whereClause = "where ";
        whereClause += pProfileId + "in elements(" + getAlias() + ".profiles)";
        whereClause +=
            " AND not exists (from QualityGridBO as oldgrid where " + getAlias()
                + ".name=oldgrid.name and oldgrid.dateOfUpdate > " + getAlias() + ".dateOfUpdate)";
        whereClause += "order by " + getAlias() + ".name";
        return findWhere( pSession, whereClause );
    }

    /**
     * Obtention des grilles
     * 
     * @param pSession session
     * @param pLastVersions indique si seulement les derni�res versions sont requises
     * @return grilles qualit� tri�es par nom et par date
     * @throws JrafDaoException si erreur
     */
    public Collection findGrids( ISession pSession, boolean pLastVersions )
        throws JrafDaoException
    {
        String whereClause = "";
        if ( pLastVersions )
        {
            whereClause =
                "where not exists (from QualityGridBO as oldgrid where " + getAlias()
                    + ".name=oldgrid.name and oldgrid.dateOfUpdate > " + getAlias() + ".dateOfUpdate)";
        }
        whereClause += "order by " + getAlias() + ".name," + getAlias() + ".dateOfUpdate";
        Collection col = findWhere( pSession, whereClause );
        return col;
    }

    /**
     * Obtention des grilles sans profil
     * 
     * @param pSession session
     * @return grilles qualit� tri�es par nom sans profil
     * @throws JrafDaoException si erreur
     */
    public Collection findGridsWithoutProfiles( ISession pSession )
        throws JrafDaoException
    {
        String whereClause =
            "where not exists (from QualityGridBO as oldgrid where " + getAlias()
                + ".name=oldgrid.name and oldgrid.dateOfUpdate > " + getAlias() + ".dateOfUpdate)";
        whereClause += " and " + getAlias() + ".profiles.size = 0";
        whereClause += "order by " + getAlias() + ".name";
        Collection col = findWhere( pSession, whereClause );
        return col;
    }

    /**
     * Destruction de grilles
     * 
     * @param pSession session
     * @param gridsId ids des grilles
     * @throws JrafDaoException si erreur
     */
    @SuppressWarnings( "unchecked" )
    public void removeGrids( ISession pSession, ArrayList<Long> gridsId )
        throws JrafDaoException
    {

        Iterator<QualityGridBO> gridsBoIt = findGridsByIds( pSession, gridsId ).iterator();
        // Deletion of the grids
        while ( gridsBoIt.hasNext() )
        {
            QualityGridBO grid = gridsBoIt.next();
            SortedSet<FactorRuleBO> sortFact = grid.getFactors();
            Set<CriteriumRuleBO> critSet = new HashSet<CriteriumRuleBO>();
            for ( FactorRuleBO factor : sortFact )
            {
                Iterator<CriteriumRuleBO> critIt = factor.getCriteria().keySet().iterator();
                while ( critIt.hasNext() )
                {
                    CriteriumRuleBO criteriumRuleBO = (CriteriumRuleBO) critIt.next();
                    critSet.add( criteriumRuleBO );
                }
            }
            Set<PracticeRuleBO> pracSet = new HashSet<PracticeRuleBO>();
            Iterator<CriteriumRuleBO> critIt = critSet.iterator();
            while ( critIt.hasNext() )
            {
                CriteriumRuleBO criteriumRuleBO = (CriteriumRuleBO) critIt.next();
                Iterator<PracticeRuleBO> pracIt = criteriumRuleBO.getPractices().keySet().iterator();
                while ( pracIt.hasNext() )
                {
                    PracticeRuleBO practiceRuleBO = (PracticeRuleBO) pracIt.next();
                    pracSet.add( practiceRuleBO );
                }

            }

            QualityRuleDAOImpl ruleDao = QualityRuleDAOImpl.getInstance();

            Iterator<PracticeRuleBO> pracIt = pracSet.iterator();
            while ( pracIt.hasNext() )
            {
                PracticeRuleBO practiceRuleBO = (PracticeRuleBO) pracIt.next();
                ruleDao.remove( pSession, practiceRuleBO );
            }

            critIt = critSet.iterator();
            while ( critIt.hasNext() )
            {
                CriteriumRuleBO criteriumRuleBO = (CriteriumRuleBO) critIt.next();
                ruleDao.remove( pSession, criteriumRuleBO );
            }
            remove( pSession, grid );
        }
    }

    /**
     * This method retrieve the {@link QualityGridBO} corresponding to the list of id given in argument
     * 
     * @param pSession tjhe hibernate session
     * @param gridsId The list of grids id
     * @return The list of corresponding {@link QualityGridBO}
     * @throws JrafDaoException Exception occurs during the retrieve
     */
    @SuppressWarnings( "unchecked" )
    public List<QualityGridBO> findGridsByIds( ISession pSession, List<Long> gridsId )
        throws JrafDaoException
    {
        List<QualityGridBO> gridList = new ArrayList<QualityGridBO>();
        StringBuffer whereClause = new StringBuffer( "where " );
        whereClause.append( getAlias() );
        whereClause.append( ".id in(" );
        Iterator gridsIdIt = gridsId.iterator();
        boolean comma = false;
        while ( gridsIdIt.hasNext() )
        {
            if ( comma )
            {
                whereClause.append( ", " );
            }
            else
            {
                comma = true;
            }
            whereClause.append( gridsIdIt.next() );
        }
        whereClause.append( ")" );
        gridList = findWhere( pSession, whereClause.toString() );
        return gridList;
    }

    /**
     * @param pSession session
     * @param pGridId l'id de la grid qu'on veut r�cup�rer
     * @return la grille ou null si elle n'existe pas
     * @throws JrafDaoException si erreur
     */
    public QualityGridBO loadById( ISession pSession, long pGridId )
        throws JrafDaoException
    {
        QualityGridBO result = null;
        String whereClause = "where " + getAlias() + ".id=" + pGridId;
        Collection col = findWhere( pSession, whereClause );
        // Il ne peut y avoir qu'un seul �l�ment, l'id �tant la cl� primaire
        if ( col.iterator().hasNext() )
        {
            result = (QualityGridBO) col.iterator().next();
        }
        return result;
    }

    /**
     * This method indicate if the grid associate to the id give in argument is linked to a profile
     * 
     * @param session hibernate session
     * @param gridId id of the grid
     * @return true if the grid associate to the id is linked to a profile
     * @throws JrafDaoException Exception happened during the search in the database
     */
    public boolean hasProfile( ISession session, long gridId )
        throws JrafDaoException
    {
        StringBuffer whereClause = new StringBuffer( "where " );
        whereClause.append( getAlias() );
        whereClause.append( ".id= " );
        whereClause.append( gridId );
        whereClause.append( " AND " );
        whereClause.append( getAlias() );
        whereClause.append( ".profiles.size > 0" );
        Collection col = findWhere( session, whereClause.toString() );
        return col.size() > 0;
    }

}
