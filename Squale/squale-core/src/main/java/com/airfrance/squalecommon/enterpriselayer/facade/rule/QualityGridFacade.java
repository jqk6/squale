package com.airfrance.squalecommon.enterpriselayer.facade.rule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.jraf.commons.exception.JrafEnterpriseException;
import com.airfrance.jraf.helper.PersistenceHelper;
import com.airfrance.jraf.provider.persistence.hibernate.facade.FacadeHelper;
import com.airfrance.jraf.spi.persistence.IPersistenceProvider;
import com.airfrance.jraf.spi.persistence.ISession;
import com.airfrance.squalecommon.daolayer.component.AuditGridDAOImpl;
import com.airfrance.squalecommon.daolayer.component.ProjectDAOImpl;
import com.airfrance.squalecommon.daolayer.rule.QualityGridDAOImpl;
import com.airfrance.squalecommon.daolayer.rule.QualityRuleDAOImpl;
import com.airfrance.squalecommon.datatransfertobject.rule.CriteriumRuleDTO;
import com.airfrance.squalecommon.datatransfertobject.rule.FactorRuleDTO;
import com.airfrance.squalecommon.datatransfertobject.rule.PracticeRuleDTO;
import com.airfrance.squalecommon.datatransfertobject.rule.QualityGridConfDTO;
import com.airfrance.squalecommon.datatransfertobject.rule.QualityGridDTO;
import com.airfrance.squalecommon.datatransfertobject.rule.QualityRuleDTO;
import com.airfrance.squalecommon.datatransfertobject.transform.rule.GridMetricsTransform;
import com.airfrance.squalecommon.datatransfertobject.transform.rule.QualityGridTransform;
import com.airfrance.squalecommon.datatransfertobject.transform.rule.QualityRuleTransform;
import com.airfrance.squalecommon.datatransfertobject.transform.rule.RuleMetricsTransform;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.CriteriumRuleBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.FactorRuleBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.PracticeRuleBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.QualityGridBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.QualityRuleBO;

/**
 * Facade pour la grille qualit�
 */
public class QualityGridFacade
{

    /**
     * provider de persistence
     */
    private static final IPersistenceProvider PERSISTENTPROVIDER = PersistenceHelper.getPersistenceProvider();

    /**
     * Obtention des grilles
     * 
     * @param pLastVersions indique si seulement les derni�res versions sont r�cup�r�es
     * @return grilles qualit�
     * @throws JrafEnterpriseException si erreur
     */
    public static Collection getGrids( boolean pLastVersions )
        throws JrafEnterpriseException
    {
        Collection result = new ArrayList(); // retour de la facade

        ISession session = null;

        try
        {
            // r�cup�ration d'une session
            session = PERSISTENTPROVIDER.getSession();

            QualityGridDAOImpl qualityGridDAO = QualityGridDAOImpl.getInstance();
            // Obtention de toutes les grilles
            Collection grids = qualityGridDAO.findGrids( session, pLastVersions );
            Iterator it = grids.iterator();
            // transformation de BO en DTO
            while ( it.hasNext() )
            {
                QualityGridDTO userDTO = QualityGridTransform.bo2Dto( (QualityGridBO) it.next() );
                result.add( userDTO );
            }
        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, QualityGridFacade.class.getName() + ".getGrids" );
        }
        finally
        {
            FacadeHelper.closeSession( session, QualityGridFacade.class.getName() + ".getGrids" );
        }

        return result;
    }

    /**
     * Obtention des grilles sans profil ni audit
     * 
     * @return grilles qualit� sans profil ni audit
     * @throws JrafEnterpriseException si erreur
     */
    public static Collection getUnlinkedGrids()
        throws JrafEnterpriseException
    {
        Collection result = new ArrayList(); // retour de la facade

        ISession session = null;

        try
        {
            // r�cup�ration d'une session
            session = PERSISTENTPROVIDER.getSession();

            QualityGridDAOImpl qualityGridDAO = QualityGridDAOImpl.getInstance();
            AuditGridDAOImpl auditGridDAO = AuditGridDAOImpl.getInstance();
            // Obtention de toutes les derni�res version des grilles sans profil
            Collection grids = qualityGridDAO.findGridsWithoutProfiles( session );
            Iterator it = grids.iterator();
            // transformation de BO en DTO
            QualityGridBO curGrid = null;
            while ( it.hasNext() )
            {
                curGrid = (QualityGridBO) it.next();
                // On ajoute le nom de la grille seulement si elle est inutilis�e
                if ( !auditGridDAO.isGridUsed( session, new Long( curGrid.getId() ) ) )
                {
                    result.add( curGrid.getName() );
                }
            }
        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, QualityGridFacade.class.getName() + ".getUnlinkedGrids" );
        }
        finally
        {
            FacadeHelper.closeSession( session, QualityGridFacade.class.getName() + ".getUnlinkedGrids" );
        }

        return result;
    }

    /**
     * Permet de r�cup�rer une grille par son identifiant
     * 
     * @param pGridId l'identifiant
     * @return la grille
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public static QualityGridDTO loadGridById( Long pGridId )
        throws JrafEnterpriseException
    {
        QualityGridDTO result = null;
        QualityGridBO bo = null;
        ISession session = null;
        try
        {
            // r�cup�ration d'une session
            session = PERSISTENTPROVIDER.getSession();
            QualityGridDAOImpl qualityGridDAO = QualityGridDAOImpl.getInstance();
            if ( pGridId != null )
            {
                bo = qualityGridDAO.loadById( session, pGridId.longValue() );
            }
            if ( bo != null )
            {
                result = QualityGridTransform.bo2Dto( bo );
            }
        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, QualityGridFacade.class.getName() + ".get" );
        }
        finally
        {
            FacadeHelper.closeSession( session, QualityGridFacade.class.getName() + ".get" );
        }
        return result;
    }

    /**
     * update une grille en base
     * 
     * @param pGrid la grille
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public static void updateGrid( QualityGridConfDTO pGrid )
        throws JrafEnterpriseException
    {
        ISession session = null;
        try
        {
            // r�cup�ration d'une session
            session = PERSISTENTPROVIDER.getSession();
            QualityGridDAOImpl qualityGridDAO = QualityGridDAOImpl.getInstance();
            QualityGridBO bo = QualityGridTransform.dto2Bo( pGrid );
            qualityGridDAO.save( session, bo );
        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, QualityGridFacade.class.getName() + ".updateGrid" );
        }
        finally
        {
            FacadeHelper.closeSession( session, QualityGridFacade.class.getName() + ".updateGrid" );
        }
    }

    /**
     * Obtention d'une grille
     * 
     * @param pQualityGrid grille qualit�
     * @return grille correspondante
     * @throws JrafEnterpriseException si erreur
     */
    public static QualityGridConfDTO get( QualityGridDTO pQualityGrid )
        throws JrafEnterpriseException
    {
        QualityGridConfDTO result = new QualityGridConfDTO();
        ISession session = null;

        try
        {
            // r�cup�ration d'une session
            session = PERSISTENTPROVIDER.getSession();

            QualityGridDAOImpl qualityGridDAO = QualityGridDAOImpl.getInstance();
            // Obtention de la grille
            QualityGridBO grid = (QualityGridBO) qualityGridDAO.load( session, new Long( pQualityGrid.getId() ) );
            QualityGridTransform.bo2Dto( grid, result );
        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, QualityGridFacade.class.getName() + ".get" );
        }
        finally
        {
            FacadeHelper.closeSession( session, QualityGridFacade.class.getName() + ".get" );
        }

        return result;
    }

    /**
     * Obtention d'une r�gle qualit�
     * 
     * @param pQualityRule r�gle qualit�
     * @param deepTransformation un bool�en indiquant si on veut aussi transformer les formules ou pas.
     * @return r�gle qualit�
     * @throws JrafEnterpriseException si erreur
     */
    public static QualityRuleDTO getQualityRule( QualityRuleDTO pQualityRule, boolean deepTransformation )
        throws JrafEnterpriseException
    {
        QualityRuleDTO result = null;
        ISession session = null;

        try
        {
            // r�cup�ration d'une session
            session = PERSISTENTPROVIDER.getSession();
            QualityRuleDAOImpl factorRuleDAO = QualityRuleDAOImpl.getInstance();
            // Obtention de la r�gle qualit�
            QualityRuleBO qualityRule = (QualityRuleBO) factorRuleDAO.get( session, new Long( pQualityRule.getId() ) );
            result = QualityRuleTransform.bo2Dto( qualityRule, deepTransformation );
        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, QualityGridFacade.class.getName() + ".get" );
        }
        finally
        {
            FacadeHelper.closeSession( session, QualityGridFacade.class.getName() + ".get" );
        }
        return result;
    }

    /**
     * Obtention d'une r�gle qualit� et des mesures utilis�es
     * 
     * @param pQualityRule r�gle qualit�
     * @param deepTransformation un bool�en indiquant si on veut aussi transformer les formules ou pas.
     * @return un tableau � deux entr�es contenant la r�gle qualit� et ses mesures extraites
     * @throws JrafEnterpriseException si erreur
     */
    public static Object[] getQualityRuleAndUsedTres( QualityRuleDTO pQualityRule, boolean deepTransformation )
        throws JrafEnterpriseException
    {
        Object[] result = new Object[2];
        ISession session = null;

        try
        {
            // r�cup�ration d'une session
            session = PERSISTENTPROVIDER.getSession();
            QualityRuleDAOImpl factorRuleDAO = QualityRuleDAOImpl.getInstance();
            // Obtention de la r�gle qualit�
            QualityRuleBO qualityRule = (QualityRuleBO) factorRuleDAO.get( session, new Long( pQualityRule.getId() ) );
            result[0] = QualityRuleTransform.bo2Dto( qualityRule, deepTransformation );
            result[1] = qualityRule.accept( new RuleMetricsTransform( new FormulaMeasureExtractor() ), null );
        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, QualityGridFacade.class.getName() + ".get" );
        }
        finally
        {
            FacadeHelper.closeSession( session, QualityGridFacade.class.getName() + ".get" );
        }
        return result;
    }

    /**
     * Destruction de grilles qualit�
     * 
     * @param pGrids grilles
     * @return grilles ne pouvant pas �tre supprim�es
     * @throws JrafEnterpriseException si erreur
     */
    public static Collection deleteGrids( Collection pGrids )
        throws JrafEnterpriseException
    {
        ISession session = null;
        Collection result = new ArrayList();
        try
        {
            // r�cup�ration d'une session
            session = PERSISTENTPROVIDER.getSession();
            // Parcours des grilles � d�truire
            Iterator gridsIt = pGrids.iterator();
            ArrayList gridsId = new ArrayList();
            AuditGridDAOImpl auditGridDAO = AuditGridDAOImpl.getInstance();
            ProjectDAOImpl projectDAO = ProjectDAOImpl.getInstance();
            while ( gridsIt.hasNext() )
            {
                QualityGridDTO gridDTO = (QualityGridDTO) gridsIt.next();
                Long gridId = new Long( gridDTO.getId() );
                // On v�rifie que la grille n'est pas utilis�e
                // au niveau d''un projet ou au niveau d'un audit r�alis�
                if ( projectDAO.isGridUsed( session, gridId ) || auditGridDAO.isGridUsed( session, gridId ) )
                {
                    result.add( gridDTO );
                }
                else
                {
                    gridsId.add( gridId );
                }
            }
            // Destruction des grilles qui ne sont plus r�f�renc�es
            if ( gridsId.size() > 0 )
            {
                QualityGridDAOImpl qualityGridDAO = QualityGridDAOImpl.getInstance();
                qualityGridDAO.removeGrids( session, gridsId );
            }
        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, QualityGridFacade.class.getName() + ".get" );
        }
        finally
        {
            FacadeHelper.closeSession( session, QualityGridFacade.class.getName() + ".get" );
        }
        return result;
    }

    /**
     * Obtention des m�triques d'une grille Cette m�thode est utilsi�e pour la construction du menu Top
     * 
     * @param pQualityGrid grille qualit�
     * @return map donnant pour chaque type de composant les m�triques utilis�es par la grille qualit�, une map vide est
     *         renvoy�e si la grille n'existe pas
     * @throws JrafEnterpriseException si erreur
     */
    public static Map getGridMetrics( QualityGridDTO pQualityGrid )
        throws JrafEnterpriseException
    {
        HashMap result = new HashMap();
        ISession session = null;

        try
        {
            // r�cup�ration d'une session
            session = PERSISTENTPROVIDER.getSession();

            QualityGridDAOImpl qualityGridDAO = QualityGridDAOImpl.getInstance();
            // Obtention de la grille
            QualityGridBO grid = (QualityGridBO) qualityGridDAO.load( session, new Long( pQualityGrid.getId() ) );
            if ( grid != null )
            {
                // Conversion de la map
                GridMetricsTransform.bo2dto( grid, result, new FormulaMeasureExtractor() );
            }
        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, QualityGridFacade.class.getName() + ".get" );
        }
        finally
        {
            FacadeHelper.closeSession( session, QualityGridFacade.class.getName() + ".get" );
        }

        return result;
    }

    /**
     * update une grille en base
     * 
     * @param pGrid la grille
     * @param pErrors erreur rencontr�es
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public static void updateGrid( QualityGridConfDTO pGrid, StringBuffer pErrors )
        throws JrafEnterpriseException
    {
        ISession session = null;
        try
        {
            // r�cup�ration d'une session
            session = PERSISTENTPROVIDER.getSession();
            QualityGridDAOImpl qualityGridDAO = QualityGridDAOImpl.getInstance();
            /* On modifie la grille � partir du DTO */
            // On update les facteurs
            SortedSet updatedFactors = new TreeSet();
            Collection factors = pGrid.getFactors();
            for ( Iterator it = factors.iterator(); it.hasNext(); )
            {
                FactorRuleDTO factorDTO = (FactorRuleDTO) it.next();
                updatedFactors.add( updateFactor( session, factorDTO ) );
            }
            // On charge la grille
            QualityGridBO bo = (QualityGridBO) qualityGridDAO.get( session, new Long( pGrid.getId() ) );
            // Transformation
            bo.setFactors( updatedFactors );
            // V�rification de la grille
            QualityGridChecker gridChecker = new QualityGridChecker();
            gridChecker.checkGrid( bo, pErrors );
        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, QualityGridFacade.class.getName() + ".updateGrid" );
        }
        finally
        {
            if ( pErrors.length() > 0 && session != null )
            {
                // On ne met pas � jour
                session.rollbackTransaction();
            }
            else
            {
                FacadeHelper.closeSession( session, QualityGridFacade.class.getName() + ".updateGrid" );
            }
        }
    }

    /**
     * update d'un facteur
     * 
     * @param session la session
     * @param factorDTO le facteur
     * @return le facteur mis � jour
     * @throws JrafEnterpriseException en cas d'�chec
     * @throws JrafDaoException erreur dao
     */
    private static FactorRuleBO updateFactor( ISession session, FactorRuleDTO factorDTO )
        throws JrafEnterpriseException, JrafDaoException
    {
        FactorRuleBO bo = null;
        QualityRuleDAOImpl qualityRuleDAO = QualityRuleDAOImpl.getInstance();
        // On update les crit�res
        SortedMap updatedCriteria = new TreeMap();
        Collection criteria = factorDTO.getCriteria().keySet();
        for ( Iterator it = criteria.iterator(); it.hasNext(); )
        {
            CriteriumRuleDTO criteriaDTO = (CriteriumRuleDTO) it.next();
            updatedCriteria.put( updateCriterium( session, criteriaDTO ),
                                 (Float) factorDTO.getCriteria().get( criteriaDTO ) );
        }
        // On charge le facteur
        bo = (FactorRuleBO) qualityRuleDAO.get( session, new Long( factorDTO.getId() ) );
        /* On modifie le facteur */
        bo.setCriteria( updatedCriteria );
        return bo;
    }

    /**
     * update d'un crit�re
     * 
     * @param session la session
     * @param criteriaDTO le crit�re
     * @return le crit�re mis � jour
     * @throws JrafEnterpriseException en cas d'�chec
     * @throws JrafDaoException erreur dao
     */
    private static CriteriumRuleBO updateCriterium( ISession session, CriteriumRuleDTO criteriaDTO )
        throws JrafEnterpriseException, JrafDaoException
    {
        CriteriumRuleBO bo = null;
        QualityRuleDAOImpl qualityRuleDAO = QualityRuleDAOImpl.getInstance();
        // On charge le crit�re
        bo = (CriteriumRuleBO) qualityRuleDAO.get( session, new Long( criteriaDTO.getId() ) );
        // On update les pratiques
        SortedMap updatedPractices = new TreeMap();
        Collection practices = criteriaDTO.getPractices().keySet();
        for ( Iterator it = practices.iterator(); it.hasNext(); )
        {
            PracticeRuleDTO practiceDTO = (PracticeRuleDTO) it.next();
            updatedPractices.put( updatePractice( session, practiceDTO ),
                                  (Float) criteriaDTO.getPractices().get( practiceDTO ) );
        }
        /* On modifie le crit�re � partir du DTO */
        bo.setPractices( updatedPractices );
        return bo;
    }

    /**
     * update d'une pratique
     * 
     * @param practiceDTO la pratique
     * @param session la session
     * @return la pratique mise � jour
     * @throws JrafEnterpriseException en cas d'�chec
     * @throws JrafDaoException erreur dao
     */
    private static PracticeRuleBO updatePractice( ISession session, PracticeRuleDTO practiceDTO )
        throws JrafEnterpriseException, JrafDaoException
    {
        // Initialisation
        PracticeRuleBO bo = null;
        QualityRuleDAOImpl qualityRuleDAO = QualityRuleDAOImpl.getInstance();
        // On charge la pratique
        bo = (PracticeRuleBO) qualityRuleDAO.get( session, new Long( practiceDTO.getId() ) );
        // Transformation
        QualityRuleTransform.dto2Bo( bo, practiceDTO );
        return bo;
    }

}
