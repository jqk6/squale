package com.airfrance.squalecommon.daolayer.message;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.jraf.provider.persistence.hibernate.AbstractDAOImpl;
import com.airfrance.jraf.spi.persistence.ISession;
import com.airfrance.squalecommon.daolayer.DAOUtils;
import com.airfrance.squalecommon.enterpriselayer.businessobject.message.MessageBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.message.NewsBO;

/**
 * Couche DAO pour les news
 */
public class NewsDAOImpl
    extends AbstractDAOImpl
{
    /**
     * Instance singleton
     */
    private static NewsDAOImpl instance = null;

    /** initialisation du singleton */
    static
    {
        instance = new NewsDAOImpl();
    }

    /**
     * Constructeur prive
     * 
     * @throws JrafDaoException
     */
    private NewsDAOImpl()
    {
        initialize( NewsBO.class );
    }

    /**
     * Retourne un singleton du DAO
     * 
     * @return singleton du DAO
     */
    public static NewsDAOImpl getInstance()
    {
        return instance;
    }

    /**
     * @param pSession la session hibernate
     * @param pKind le type de news qu'on veut r�cup�rer
     * @return les news correspondantes
     * @throws JrafDaoException en cas d'�chec
     */
    public Collection findWhereKind( ISession pSession, String pKind )
        throws JrafDaoException
    {
        String whereClause = "where ";
        // Clause qu'on rajoute pour trier les d�peches de la plus r�cente � la plus ancienne
        String orderClause = " order by " + getAlias() + ".beginningDate desc";
        Collection coll = null;
        // Dans ces cas on les r�cup�re toutes
        if ( pKind == null || ( !pKind.equals( "old" ) && !pKind.equals( "current" ) ) )
        {
            coll = findWhere( pSession, orderClause );
        }
        else
        {
            // s�lection en fonction de crit�res sur les dates
            Date now = Calendar.getInstance().getTime();
            String todayDate = DAOUtils.makeQueryDate( now );
            // On r�cup�re celles qui ne sont plus d'actualit�
            if ( pKind.equals( "old" ) )
            {
                whereClause += getAlias() + ".endDate < " + todayDate;
            }
            else
            {
                // on r�cup�re celles qui sont actuellement visibles sur le portail
                if ( pKind.equals( "current" ) )
                {
                    whereClause +=
                        getAlias() + ".beginningDate < " + todayDate + "and " + getAlias() + ".endDate > " + todayDate;
                }
            }
            // et on les trie pour afficher la plus r�cente en premier
            whereClause += orderClause;
            coll = findWhere( pSession, whereClause );
        }
        return coll;
    }

    /**
     * supprime une news
     * 
     * @param pSession la session hibernate
     * @param pNewsBO la news que l'on veut supprimer
     * @param pMessBO le message de la news qu'on veut supprimer
     * @throws JrafDaoException en cas d'�chec
     */
    public void removeNews( ISession pSession, NewsBO pNewsBO, MessageBO pMessBO )
        throws JrafDaoException
    {
        MessageDAOImpl messDao = MessageDAOImpl.getInstance();
        messDao.removeMessage( pSession, pMessBO );
        long id = pNewsBO.getId();
        String whereClause = "where " + getAlias() + ".id=" + id;
        removeWhere( pSession, whereClause );
    }

    /**
     * ajoute une news
     * 
     * @param pSession la session hibernate
     * @param pNewsBO la news � ajouter
     * @param pMessBO le message de la news � ajouter
     * @throws JrafDaoException en cas d'�chec
     */
    public void addNews( ISession pSession, NewsBO pNewsBO, MessageBO pMessBO )
        throws JrafDaoException
    {
        MessageDAOImpl messDao = MessageDAOImpl.getInstance();
        messDao.create( pSession, pMessBO );
        create( pSession, pNewsBO );
    }

    /**
     * modifie une news
     * 
     * @param pSession la session hibernate
     * @param pNewsBO la news � ajouter
     * @param pMessBO le message de la news � ajouter
     * @throws JrafDaoException en cas d'�chec
     */
    public void modifyNews( ISession pSession, NewsBO pNewsBO, MessageBO pMessBO )
        throws JrafDaoException
    {
        MessageDAOImpl messDao = MessageDAOImpl.getInstance();
        // on fait un remove puis un create au lieu d'un save
        // car sinon avec l'id composite on ne prend pas en compte
        // les modifications mais on ajoute un nouveau message
        messDao.save( pSession, pMessBO );
        // pas de probl�me dans le cas d'une news car il y a un id
        save( pSession, pNewsBO );
    }

    /**
     * @param pSession la session
     * @param pKey la cl�
     * @return la collection des news contenant un telle cl�
     * @throws JrafDaoException en cas d'�chec
     */
    public Collection findWhereNewsKeyIs( ISession pSession, String pKey )
        throws JrafDaoException
    {
        String whereClause = "where key like '" + pKey + "'";
        return findWhere( pSession, whereClause );
    }
}