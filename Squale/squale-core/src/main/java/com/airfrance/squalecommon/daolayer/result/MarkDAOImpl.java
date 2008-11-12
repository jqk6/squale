package com.airfrance.squalecommon.daolayer.result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.jraf.provider.persistence.hibernate.AbstractDAOImpl;
import com.airfrance.jraf.spi.persistence.ISession;
import com.airfrance.squalecommon.daolayer.DAOMessages;
import com.airfrance.squalecommon.datatransfertobject.result.PracticeEvolutionDTO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AbstractComponentBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.MarkBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.PracticeResultBO;
import com.airfrance.squalecommon.util.database.DatabaseTypeFactory;

/**
 * @author M400843
 */
public class MarkDAOImpl
    extends AbstractDAOImpl
{
    /**
     * Instance singleton
     */
    private static MarkDAOImpl instance = null;

    /** log */
    private static Log LOG;

    /** initialisation du singleton */
    static
    {
        instance = new MarkDAOImpl();
    }

    /**
     * Constructeur prive
     * 
     * @throws JrafDaoException
     */
    private MarkDAOImpl()
    {
        initialize( MarkBO.class );
        LOG = LogFactory.getLog( MarkDAOImpl.class );
    }

    /**
     * Retourne un singleton du DAO
     * 
     * @return singleton du DAO
     */
    public static MarkDAOImpl getInstance()
    {
        return instance;
    }

    /**
     * Supprime toutes les notes appartenant � l'application
     * 
     * @param pSession la session
     * @param pProject le projet
     * @throws JrafDaoException si une erreur � lieu
     */
    public void removeWhereProject( ISession pSession, ProjectBO pProject )
        throws JrafDaoException
    {
        String whereClause = "where ";
        whereClause += getAlias() + ".practice.project.id = " + pProject.getId();
        // il reste un probl�me sur la m�thode remove where,
        // il faut donc faire un find where puis un remove
        // sur chaque �l�ment de la collection
        List temp = findWhere( pSession, whereClause );
        for ( int i = 0; i < temp.size(); i++ )
        {
            remove( pSession, temp.get( i ) );
        }
    }

    /**
     * Supprime toutes les notes li�es au composant
     * 
     * @param pSession la session
     * @param pComponent le composant
     * @throws JrafDaoException si une erreur � lieu
     */
    public void removeWhereComponent( ISession pSession, AbstractComponentBO pComponent )
        throws JrafDaoException
    {
        String whereClause = "where ";
        whereClause += getAlias() + ".component.id = " + pComponent.getId();
        removeWhere( pSession, whereClause );
    }

    /**
     * Permet de r�cup�rer les notes en fonction d'une liste de noms de TREs
     * 
     * @param pSession session Hibernate
     * @param pComponentID identifiant du composant
     * @param pAuditID identifiant de l'audit
     * @param pRuleIds identificateurs des r�gles qualit�
     * @return liste des valeurs ordonn�s par raopport a la liste des TREs
     * @throws JrafDaoException exception DAO
     */
    public List findWhere( ISession pSession, Long pComponentID, Long pAuditID, List pRuleIds )
        throws JrafDaoException
    {
        List marks = new ArrayList();
        Iterator it = pRuleIds.iterator();
        while ( it.hasNext() )
        {
            Long ruleId = (Long) it.next();
            marks.add( load( pSession, pComponentID, pAuditID, ruleId ) );
        }
        return marks;
    }

    /**
     * Permet de r�cup�rer une note en fonction d'un audit
     * 
     * @param pSession session Hibernate
     * @param pComponentID identifiant du composant
     * @param pAuditID identifiant de l'audit
     * @param pRuleId id du TRE
     * @return la note associ�e au type de r�sultat, au composant et � l'audit
     * @throws JrafDaoException exception DAO
     */
    public MarkBO load( ISession pSession, Long pComponentID, Long pAuditID, Long pRuleId )
        throws JrafDaoException
    {
        String whereClause = "where ";
        whereClause += getAlias() + ".practice.rule.id = " + pRuleId;
        whereClause += " and ";
        whereClause += getAlias() + ".component.id = '" + pComponentID + "'";
        whereClause += " and ";
        whereClause += getAlias() + ".practice.audit.id = '" + pAuditID + "'";

        MarkBO mark = null;
        Collection col = findWhere( pSession, whereClause );
        if ( col.size() >= 1 )
        {
            mark = (MarkBO) col.iterator().next();
            if ( col.size() > 1 )
            {
                String tab[] = { pAuditID.toString(), pComponentID.toString(), pRuleId.toString() };
                LOG.warn( DAOMessages.getString( "mark.many.audit_component_tre", tab ) );
            }
        }

        return mark;
    }

    /**
     * Retrouve les notes qui ont pour type pTreClass, pour id d'audit pauditId et pour valeur pValue
     * 
     * @param pSession session Hibernate
     * @param pAuditId identifiant de l'audit
     * @param pProjectId id du projet
     * @param pTreId classe du TRE
     * @param pValue valeur de la note
     * @param pMax Nombre maximum de composants retourn�
     * @return les notes associ�es
     * @throws JrafDaoException exception DAO
     */
    public Collection findWhere( ISession pSession, Long pAuditId, Long pProjectId, Long pTreId, Integer pValue,
                                 Integer pMax )
        throws JrafDaoException
    {
        String whereClause = "where ";
        whereClause += getAlias() + ".practice.rule.id = " + pTreId;
        whereClause += " and ";
        whereClause += getAlias() + ".practice.audit.id = '" + pAuditId + "'";
        whereClause += " and ";
        whereClause += getAlias() + ".practice.project.id = " + pProjectId;
        whereClause += " and ";
        whereClause += whereValueClause( pValue );
        Collection ret = (Collection) findWhereScrollable( pSession, whereClause, pMax.intValue(), 0, false );
        return ret;
    }

    /**
     * Retrouve les notes qui ont pour type pTreClass, pour id d'audit pauditId et pour valeur pValue
     * 
     * @param pSession session Hibernate
     * @param pAuditId identifiant de l'audit
     * @param pProjectId id du projet
     * @param pTreId classe du TRE
     * @param pValueMin valeur min de l'intervalle
     * @param pValueMax valeur max de l'intervalle
     * @param pMax Nombre maximum de composants retourn�
     * @return les notes associ�es
     * @throws JrafDaoException exception DAO
     */
    public Collection findWhereInterval( ISession pSession, Long pAuditId, Long pProjectId, Long pTreId,
                                         Double pValueMin, Double pValueMax, Integer pMax )
        throws JrafDaoException
    {
        String whereClause = "where ";
        whereClause += getAlias() + ".practice.rule.id = " + pTreId;
        whereClause += " and ";
        whereClause += getAlias() + ".practice.audit.id = '" + pAuditId + "'";
        whereClause += " and ";
        whereClause += getAlias() + ".practice.project.id = " + pProjectId;
        whereClause += " and ";
        whereClause += whereValueClause( pValueMin.doubleValue(), pValueMax.doubleValue() );
        Collection ret = (Collection) findWhereScrollable( pSession, whereClause, pMax.intValue(), 0, false );
        return ret;
    }

    /**
     * @param pValue la valeur de l'index
     * @return le crit�re de recher sur la note pour la clause where
     */
    private String whereValueClause( Integer pValue )
    {
        // Si il n'y a pas de statut associ� � l'index, le composant est non not� (-->value=-1)
        String result = getAlias() + ".value = " + -1;
        if ( pValue.intValue() == PracticeResultBO.REFUSED_MIN )
        {
            result = getAlias() + ".value >= " + PracticeResultBO.REFUSED_MIN;
            result += " and " + getAlias() + ".value < " + PracticeResultBO.REFUSED_MAX;
        }
        else if ( pValue.intValue() == PracticeResultBO.NEARLY_ACCEPTED_MIN )
        {
            result = getAlias() + ".value >= " + PracticeResultBO.NEARLY_ACCEPTED_MIN;
            result += " and " + getAlias() + ".value < " + PracticeResultBO.NEARLY_ACCEPTED_MAX;
        }
        else if ( pValue.intValue() == PracticeResultBO.ACCEPTED_MIN )
        {
            result = getAlias() + ".value >= " + PracticeResultBO.ACCEPTED_MIN;
            result += " and " + getAlias() + ".value < " + PracticeResultBO.ACCEPTED_MAX;
        }
        else if ( pValue.intValue() == PracticeResultBO.EXCELLENT )
        {
            result = getAlias() + ".value = " + PracticeResultBO.EXCELLENT;
        }
        return result;
    }

    /**
     * @param pValueMin la valeur min de l'intervalle
     * @param pValueMax la valeur max de l'intervalle
     * @return le crit�re de recherche pour l'intervalle pour la clause where
     */
    private String whereValueClause( double pValueMin, double pValueMax )
    {
        // Si il n'y a pas de statut associ� � l'index, le composant est non not� (-->value=-1)
        String result = getAlias() + ".value = " + -1;
        if ( pValueMin >= 0 )
        {
            result = getAlias() + ".value >= " + pValueMin;
            // On ne prend la borne sup�rieure de l'intervalle que si c'est 3
            if ( pValueMax != PracticeResultBO.EXCELLENT )
            {
                result += " AND " + getAlias() + ".value < " + pValueMax;
            }
            else
            {
                result += " AND " + getAlias() + ".value <= " + pValueMax;
            }

        }
        return result;
    }

    /**
     * Obtention de toutes les notes sur un composant et un audit
     * 
     * @param pSession session
     * @param pComponentID composant
     * @param pAuditID audit
     * @return notes associ�es
     * @throws JrafDaoException si erreur
     */
    public Collection findWhere( ISession pSession, Long pComponentID, Long pAuditID )
        throws JrafDaoException
    {
        String whereClause = "where ";
        whereClause += getAlias() + ".component.id = '" + pComponentID + "'";
        whereClause += " and ";
        whereClause += getAlias() + ".practice.audit.id = '" + pAuditID + "'";

        MarkBO mark = null;
        Collection col = findWhere( pSession, whereClause );
        if ( col.size() >= 1 )
        {
            mark = (MarkBO) col.iterator().next();
            if ( col.size() > 1 )
            {
                String tab[] = { pAuditID.toString(), pComponentID.toString(), "" };
                LOG.warn( DAOMessages.getString( "mark.many.audit_component_tre", tab ) );
            }
        }

        return col;
    }

    /**
     * @param pSession session
     * @param pAuditId l'id de l'audit
     * @param pPreviousId l'id de l'audit pr�c�dent
     * @return le nombre de corrections correspondant au nombre de composants qui �taient � z�ro et qui ne le sont plus.
     * @throws JrafDaoException si erreur
     */
    public int findCorrectionsWithProgessions( ISession pSession, Long pAuditId, Long pPreviousId )
        throws JrafDaoException
    {
        String requete = "select count(m1.id) from MarkBO as m1, MarkBO as m2";
        String whereClause = " where ";
        whereClause += "(m1.practice.audit.id = '" + pPreviousId + "' and m1.value=0)";
        whereClause += " and ";
        whereClause += "(m2.practice.audit.id = '" + pAuditId + "' and m2.value>0)";
        whereClause += " and ";
        whereClause += "m1.component.id=m2.component.id";
        whereClause += " and ";
        whereClause += "m1.practice.rule.id=m2.practice.rule.id";
        LOG.debug( "requete : " + requete + whereClause );
        List result = find( pSession, requete + whereClause );
        return ( (Integer) result.get( 0 ) ).intValue();
    }

    /**
     * @param pSession la session
     * @param pAuditId l'id de l'audit
     * @param pPreviousId l'id de l'audit pr�c�dent
     * @return le nombre de corrections correspondant au nombre de composants qui avaient une note � 0 et qui n'existent
     *         plus.
     * @throws JrafDaoException si erreur
     */
    public int findCorrectionsWithSuppressions( ISession pSession, Long pAuditId, Long pPreviousId )
        throws JrafDaoException
    {
        String requete = "select count(m1.component.id) from MarkBO as m1";
        requete += " where ";
        requete += "(m1.practice.audit.id=" + pPreviousId + " and m1.value=0)";
        requete += " and ";
        requete += "(m1.component.id not in ";
        requete += "(select m2.component.id from MarkBO as m2";
        requete += " where ";
        requete += " m1.practice.rule.id=m2.practice.rule.id ";
        requete += " and ";
        requete += "m2.practice.audit.id=" + pAuditId + "))";
        LOG.debug( "requete : " + requete );
        List result = find( pSession, requete );
        return ( (Integer) result.get( 0 ) ).intValue();
    }

    /**
     * Donne la requ�te qui donne la liste des notes sur les nouveaux composants. NB : Cette m�thode sert aussi �
     * retouver les composants supprim�s (on inverse les ids des audits)
     * 
     * @param pSession la session
     * @param pAuditId l'audit courant
     * @param pPreviousId l'audit de comparaison
     * @param pProjectId l'id du projet
     * @return la requ�te
     * @throws JrafDaoException si erreur
     */
    private String getDeletedOrNewComponentsWhereClause( ISession pSession, Long pAuditId, Long pPreviousId,
                                                         Long pProjectId )
        throws JrafDaoException
    {
        String whereClause = "where ";
        whereClause += getAlias() + ".practice.project.id = '" + pProjectId + "'";
        whereClause += " and ";
        whereClause += getAlias() + ".practice.audit.id = '" + pPreviousId + "'";
        whereClause +=
            " and " + getAlias() + ".component.id not in (" + getComponentIdsWhere( pSession, pProjectId, pAuditId )
                + ")";
        return whereClause;
    }

    /**
     * @param pSession la session
     * @param pProjectId l'id du projet
     * @param pAuditId l'id de l'audit
     * @return la requ�te r�cup�rant les ids des composants du projet d'id <code>pProjectId</code> not�s lors de
     *         l'audit d'id <code>pAuditId</code>
     * @throws JrafDaoException si erreur
     */
    private String getComponentIdsWhere( ISession pSession, Long pProjectId, Long pAuditId )
        throws JrafDaoException
    {
        String select = "select m.component.id from MarkBO as m where ";
        select += "m.practice.project.id = '" + pProjectId + "'";
        select += " and ";
        select += "m.practice.audit.id = '" + pAuditId + "'";
        return select;
    }

    /**
     * Donne la liste des notes sur les nouveaux composants. NB : Cette m�thode sert aussi � retouver les composants
     * supprim�s (on inverse les ids des audits)
     * 
     * @param pSession la session
     * @param pAuditId l'audit courant
     * @param pPreviousId l'audit de comparaison
     * @param pProjectId l'id du projet
     * @param pFilter le filtre
     * @param pLimit le nombre de r�sultats � remonter
     * @return la liste des nouveaux composants entre <code>pAuditId</code> et <code>pPreviousId</code>
     * @throws JrafDaoException si erreur
     */
    public Collection findDeletedComponents( ISession pSession, Long pAuditId, Long pPreviousId, Long pProjectId,
                                             Object[] pFilter, int pLimit )
        throws JrafDaoException
    {
        String whereClause = getDeletedOrNewComponentsWhereClause( pSession, pAuditId, pPreviousId, pProjectId );
        if ( null != pFilter[PracticeEvolutionDTO.ONLY_PRACTICES_ID] )
        {
            String[] practices = (String[]) pFilter[PracticeEvolutionDTO.ONLY_PRACTICES_ID];
            // On r�cup�re que les notes des pratiques qui sont dans pPractices
            int nbPractices = practices.length;
            if ( nbPractices > 0 )
            { // s�curit� pour le code car aucun int�r�t si il n'y a pas de pratique.
                whereClause += " and " + getAlias() + ".practice.rule.name in ";
                whereClause += getPracticesInClause( practices );
            }
        }
        if ( null != pFilter[PracticeEvolutionDTO.THRESHOLD_ID] )
        {
            String[] thresholdFilter = (String[]) pFilter[PracticeEvolutionDTO.THRESHOLD_ID];
            // On convertit le seuil en int
            final float maxMark = 3;
            float limit;
            try
            {
                limit = Float.parseFloat( thresholdFilter[1] );
            }
            catch ( NumberFormatException nfe )
            {
                // l'utilisateur a donn� une mauvais seuil
                // on met la seuil max
                limit = maxMark;
            }
            whereClause += " and ";
            whereClause += getAlias() + ".value " + thresholdFilter[0] + " " + limit;
        }
        // On ordonne par type et nom de composant
        whereClause += " order by  " + getAlias() + ".component.class, " + getAlias() + ".component.name asc";
        LOG.debug( whereClause );
        return (Collection) findWhereScrollable( pSession, whereClause, pLimit, 0, false );
    }

    /**
     * @param pPractices les pratiques
     * @return les pratiques s�par�es par des virgules pour une clause in
     */
    private String getPracticesInClause( String[] pPractices )
    {
        String inClause = "(";
        int limit = pPractices.length - 1;
        for ( int i = 0; i < limit; i++ )
        {
            inClause += "'" + pPractices[i] + "', ";
        }
        inClause += "'" + pPractices[limit] + "')";
        return inClause;
    }

    /**
     * @param pSession la session
     * @param pAuditId l'id de l'audit de r�f�rence
     * @param pPreviousId l'id de l'audit de comparaison
     * @param pProjectId l'id du projet
     * @param pFilter le filtre
     * @param pLimit le nombre de r�sultats � remonter
     * @return une liste de tableau � deux �l�ments de MarkBO repr�sentant toutes les notes qui sont diff�rentes entre
     *         les deux audits pour un m�me composant une m�me r�gle.
     * @throws JrafDaoException si erreur
     */
    public Collection findChangedComponentWhere( ISession pSession, Long pAuditId, Long pPreviousId, Long pProjectId,
                                                 Object[] pFilter, int pLimit )
        throws JrafDaoException
    {
        String query = getEvolutionQuery( pAuditId, pPreviousId, pProjectId );
        query += " and ";
        query += "m1.value != m2.value";
        // On filtre
        if ( null != pFilter[PracticeEvolutionDTO.ONLY_UP_OR_DOWN_ID] )
        {
            if ( ( (String) pFilter[PracticeEvolutionDTO.ONLY_UP_OR_DOWN_ID] ).equals( PracticeEvolutionDTO.ONLY_UP ) )
            {
                query += " and ";
                // On r�cup�re les composants dont la note s'est am�lior�e ou si le composant
                // n'est plus not�
                query +=
                    "(m1.value > m2.value or (m1.value = " + MarkBO.NOT_NOTED_VALUE + " and m2.value != "
                        + MarkBO.NOT_NOTED_VALUE + "))";

            }
            else
            {
                query += " and ";
                // On r�cup�re les composants dont la note s'est d�grad�e (si le composant
                // n'est plus not� il s'agit d'une am�lioration)
                query +=
                    "((m1.value < m2.value or m2.value = " + MarkBO.NOT_NOTED_VALUE + ") and m1.value != "
                        + MarkBO.NOT_NOTED_VALUE + ")";

            }
        }
        if ( null != pFilter[PracticeEvolutionDTO.ONLY_PRACTICES_ID] )
        {
            String[] practices = (String[]) pFilter[PracticeEvolutionDTO.ONLY_PRACTICES_ID];
            // On r�cup�re que les notes des pratiques qui sont dans pPractices
            int nbPractices = practices.length;
            if ( nbPractices > 0 )
            { // s�curit� pour le code car aucun int�r�t si il n'y a pas de pratique.
                query += " and m1.practice.rule.name in ";
                query += getPracticesInClause( practices );
            }
        }
        
        // On ordonne par type et nom de composant; on limite le nombre de r�sultat
        // On r�cup�re la bonne string en fonction de la base de donn�e utilis�e
        query += DatabaseTypeFactory.getInstance().resNumberLimit(pLimit);
        
        
        LOG.debug( query );
        return find( pSession, query );
    }

    /**
     * @param pAuditId l'id de l'audit de r�f�rence
     * @param pPreviousId l'id de l'audit de comparaison
     * @param pProjectId l'id du projet
     * @return le d�but de la requ�te pour trouver les �volutions
     */
    private String getEvolutionQuery( Long pAuditId, Long pPreviousId, Long pProjectId )
    {
        String query = "select m1.component, m1.practice, m1.value, m2.value from MarkBO as m1, MarkBO as m2 ";
        String whereClause = "where ";
        whereClause += "m1.practice.project.id = '" + pProjectId + "'";
        whereClause += " and ";
        whereClause += "m1.practice.audit.id = '" + pAuditId + "'";
        whereClause += " and ";
        whereClause += "m1.component.id = m2.component.id";
        whereClause += " and ";
        whereClause += "m1.practice.rule.id = m2.practice.rule.id";
        whereClause += " and ";
        whereClause += "m2.practice.audit.id = '" + pPreviousId + "'";
        return query + whereClause;
    }

    /**
     * Retrouve les <code>pMax</code> plus mauvaises notes (inf�rieur � <code>pMark</code>+1)qui ont pour id
     * d'audit <code>pAuditId</code> pour la pratique d'id <code>pPracticeId</code> et dont le composant n'est pas
     * exclu du plan d'action.
     * 
     * @param pSession session Hibernate
     * @param pPracticeId l'id de la pratique
     * @param pMark la note de la pratique
     * @param pMax Nombre maximum de composants retourn�
     * @return les notes associ�es
     * @throws JrafDaoException exception DAO
     */
    public Collection findWorstWhere( ISession pSession, Long pPracticeId, float pMark, Integer pMax )
        throws JrafDaoException
    {
        String whereClause = getWorstWhereClause( pPracticeId, pMark );
        // On ordonne par note
        whereClause += " order by  " + getAlias() + ".value asc";
        Collection ret = (Collection) findWhereScrollable( pSession, whereClause, pMax.intValue(), 0, false );
        return ret;
    }

    /**
     * Compte les plus mauvaises notes (inf�rieur � <code>pMark</code>+1)qui ont pour id d'audit
     * <code>pAuditId</code> pour la pratique d'id <code>pPracticeId</code> et dont le composant n'est pas exclu du
     * plan d'action.
     * 
     * @param pSession session Hibernate
     * @param pPracticeId l'id de la pratique
     * @param pMark la note de la pratique
     * @return le nombre des plus mauvais composants pour cette pratique
     * @throws JrafDaoException exception DAO
     */
    public int countWorstWhere( ISession pSession, Long pPracticeId, float pMark )
        throws JrafDaoException
    {
        String whereClause = getWorstWhereClause( pPracticeId, pMark );
        return countWhere( pSession, whereClause ).intValue();
    }

    /**
     * @param pPracticeId l'id de la pratique
     * @param pMark la note de la pratique
     * @return la clause where pour r�cup�rer les plus mauvais composants
     */
    private String getWorstWhereClause( Long pPracticeId, float pMark )
    {
        String whereClause = "where ";
        whereClause += getAlias() + ".practice.id=" + pPracticeId;
        // On ne prend pas en compte les composants non not�s
        whereClause += " and ";
        whereClause += getAlias() + ".value!=" + MarkBO.NOT_NOTED_VALUE;
        // ni les composants exclus (0=false, 1=true)
        whereClause += " and ";
        whereClause += getAlias() + ".component.excludedFromActionPlan = 0";
        // Condition sur la note
        whereClause += " and " + getAlias() + ".value < " + pMark + 1;
        return whereClause;
    }
}
