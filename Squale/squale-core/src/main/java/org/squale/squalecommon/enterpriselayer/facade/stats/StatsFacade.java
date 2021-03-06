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
package org.squale.squalecommon.enterpriselayer.facade.stats;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.squale.jraf.commons.exception.JrafDaoException;
import org.squale.jraf.commons.exception.JrafEnterpriseException;
import org.squale.jraf.helper.PersistenceHelper;
import org.squale.jraf.provider.persistence.hibernate.facade.FacadeHelper;
import org.squale.jraf.spi.persistence.IPersistenceProvider;
import org.squale.jraf.spi.persistence.ISession;
import org.squale.squalecommon.daolayer.component.ApplicationDAOImpl;
import org.squale.squalecommon.daolayer.profile.UserDAOImpl;
import org.squale.squalecommon.daolayer.stats.SiteAndProfilStatsDICTDAOImpl;
import org.squale.squalecommon.daolayer.stats.SiteStatsDICTDAOImpl;
import org.squale.squalecommon.datatransfertobject.stats.ApplicationStatsDTO;
import org.squale.squalecommon.datatransfertobject.stats.SetOfStatsDTO;
import org.squale.squalecommon.datatransfertobject.transform.stats.ApplicationStatsTransformer;
import org.squale.squalecommon.datatransfertobject.transform.stats.StatsTransform;
import org.squale.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;

/**
 */
public class StatsFacade
{

    /**
     * provider de persistence
     */
    private static final IPersistenceProvider PERSISTENTPROVIDER = PersistenceHelper.getPersistenceProvider();

    /**
     * @param pDaysForTerminatedAudit le nombre de jours max pour lesquels il doit y avoir au moins un audit r�ussi pour
     *            que l'application soit active (sert pour les statistiques par application)
     * @param pDaysForAllAudits le nombre de jours d�fini pour compter les audits (sert pour les staistiques par
     *            application)
     * @return l'objet regroupant les donn�es statistiques ainsi que les statistiques par application
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public static SetOfStatsDTO getStats( int pDaysForTerminatedAudit, int pDaysForAllAudits )
        throws JrafEnterpriseException
    {
        SiteAndProfilStatsDICTDAOImpl siteAndProfilDAO = SiteAndProfilStatsDICTDAOImpl.getInstance();
        SiteStatsDICTDAOImpl siteDAO = SiteStatsDICTDAOImpl.getInstance();
        ISession session = null;
        SetOfStatsDTO result = null;
        try
        {
            session = PERSISTENTPROVIDER.getSession();
            StatsTransform transform = new StatsTransform();
            Collection siteAndProfilStats = siteAndProfilDAO.findAll( session );
            Collection siteStats = siteDAO.findAll( session );
            result = transform.bo2dto( siteAndProfilStats, siteStats );
            // On r�cup�re les statistiques par application
            List applisStats = getApplicationsStats( pDaysForTerminatedAudit, pDaysForAllAudits );
            // On ajoute au set des stats
            result.setListOfApplicationsStatsDTO( applisStats );
        }
        catch ( JrafDaoException jde )
        {
            FacadeHelper.convertException( jde, StatsFacade.class.getName() + ".getStats" );
        }
        finally
        {
            FacadeHelper.closeSession( session, StatsFacade.class.getName() + ".getStats" );
        }
        return result;
    }

    /**
     * R�cup�ration des statistiques par applications
     * 
     * @param pDaysForTerminatedAudit le nombre de jours max pour lesquels il doit y avoir au moins un audit r�ussi pour
     *            que l'application soit active
     * @param pDaysForAllAudits le nombre de jours d�fini pour compter les audits
     * @return la liste des staistiques de toutes les applications non supprim�e
     * @throws JrafEnterpriseException si erreur
     */
    private static List getApplicationsStats( int pDaysForTerminatedAudit, int pDaysForAllAudits )
        throws JrafEnterpriseException
    {

        // Initialisation
        ISession session = null;
        ApplicationDAOImpl appliDAO = ApplicationDAOImpl.getInstance();
        UserDAOImpl userDAO = UserDAOImpl.getInstance();
        List results = new ArrayList();
        try
        {
            session = PERSISTENTPROVIDER.getSession();
            // On r�cup�re toutes les applications non supprim�es tri�es par ordre alphab�tique
            List allApplis = appliDAO.findNotDeleted( session );
            // On effectue la transformation en statistiques
            for ( int i = 0; i < allApplis.size(); i++ )
            {
                ApplicationBO appliBO = (ApplicationBO) allApplis.get( i );
                ApplicationStatsDTO statsDTO =
                    ApplicationStatsTransformer.bo2Dto( appliBO, pDaysForTerminatedAudit, pDaysForAllAudits );
                // Test if it's an archived application (= 0 user)
                if ( userDAO.countWhereApplication( session, appliBO.getId() ) == 0 )
                {
                    statsDTO.setArchived( true );
                }
                results.add( statsDTO );
            }

        }
        catch ( JrafDaoException jde )
        {
            FacadeHelper.convertException( jde, StatsFacade.class.getName() + ".getApplicationsStats" );
        }
        finally
        {
            FacadeHelper.closeSession( session, StatsFacade.class.getName() + ".getApplicationsStats" );
        }

        return results;
    }
}
