package com.airfrance.squalecommon.enterpriselayer.applicationcomponent.display;

import com.airfrance.jraf.commons.exception.JrafEnterpriseException;
import com.airfrance.jraf.provider.accessdelegate.DefaultExecuteComponent;
import com.airfrance.squalecommon.datatransfertobject.stats.SetOfStatsDTO;
import com.airfrance.squalecommon.enterpriselayer.facade.stats.StatsFacade;

/**
 */
public class StatsComponentApplicationAccess
    extends DefaultExecuteComponent
{

    /**
     * R�cup�ration des donn�es statistiques niveau administrateur
     * 
     * @param pDaysForTerminatedAudit le nombre de jours max pour lesquels il doit y avoir au moins un audit r�ussi pour
     *            que l'application soit active (sert pour les statistiques par application)
     * @param pDaysForAllAudits le nombre de jours d�fini pour compter les audits
     * @return l'objet regroupant les stats (sert pour les statistiques par application)
     * @throws JrafEnterpriseException en cas d'�chec de r�cup�ration des donn�es
     */
    public SetOfStatsDTO getStats( Integer pDaysForTerminatedAudit, Integer pDaysForAllAudits )
        throws JrafEnterpriseException
    {
        return StatsFacade.getStats( pDaysForTerminatedAudit.intValue(), pDaysForAllAudits.intValue() );
    }
}
