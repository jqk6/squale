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
package com.airfrance.squalecommon.datatransfertobject.transform.stats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.airfrance.squalecommon.datatransfertobject.stats.ApplicationStatsDTO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.access.UserAccessBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AuditDateComparator;

/**
 * Transformation d'une application sous forme BO en dto repr�sentatif des statistiques niveau application
 */
public class ApplicationStatsTransformer
{

    /**
     * Transformation en DTO des statistiques d'une application
     * 
     * @param pApplicationBO l'application
     * @param pDaysForTerminatedAudit le nombre de jours max pour lesquels il doit y avoir au moins un audit r�ussi pour
     *            que l'application soit active
     * @param pDaysForAllAudits le nombre de jours d�fini pour compter les audits
     * @return les statistiques sous forme DTO
     */
    public static ApplicationStatsDTO bo2Dto( ApplicationBO pApplicationBO, int pDaysForTerminatedAudit,
                                              int pDaysForAllAudits )
    {
        ApplicationStatsDTO stat = new ApplicationStatsDTO();
        // On tri les audits selon leur date d'ex�cution
        List sortedAudits = new ArrayList( pApplicationBO.getAudits() );
        Collections.sort( sortedAudits, new AuditDateComparator( true ) );
        AuditBO firstTerminatedAudit = getFirstTerminatedAudit( sortedAudits );
        Collections.reverse( sortedAudits );
        AuditBO lastExecutedAudit = getLastExecutedAudit( sortedAudits );
        // On calcule la date limite pour d�finir l'activation d'une application
        Calendar minDate = Calendar.getInstance();
        minDate.add( Calendar.DAY_OF_MONTH, -pDaysForTerminatedAudit );
        stat.setApplicationName( pApplicationBO.getName() );
        stat.setActivatedApplication( hasTerminatedAuditSince( sortedAudits, pDaysForTerminatedAudit ) );
        if ( null != firstTerminatedAudit )
        {
            stat.setFirstTerminatedAuditDate( firstTerminatedAudit.getDate() );
        }
        if ( pApplicationBO.getUserAccesses().size() > 0 )
        {
            stat.setLastAccess( ( (UserAccessBO) pApplicationBO.getUserAccesses().get( 0 ) ).getDate() );
        }
        if ( null != lastExecutedAudit )
        {
            stat.setLastAuditDuration( lastExecutedAudit.getDuration() );
            stat.setLastAuditIsTerminated( lastExecutedAudit.getStatus() == AuditBO.TERMINATED );
        }
        AuditBO lastFailedAudit = getLastAudit( sortedAudits, new int[] { AuditBO.FAILED, AuditBO.PARTIAL } );
        if ( null != lastFailedAudit )
        {
            stat.setLastFailedAuditDate( lastFailedAudit.getDate() );
        }
        AuditBO lastTerminatedAudit = getLastAudit( sortedAudits, new int[] { AuditBO.TERMINATED } );
        if ( null != lastTerminatedAudit )
        {
            stat.setLastTerminatedAuditDate( lastTerminatedAudit.getDate() );
        }
        stat.setNbAudits( getNbAuditsSince( sortedAudits, pDaysForAllAudits ) );
        stat.setNbPartialOrFaliedAudits( getNbPartialOrFailedAudits( sortedAudits ) );
        stat.setNbTerminatedAudits( getNbTerminatedAudits( sortedAudits ) );
        if ( null != pApplicationBO.getServeurBO() )
        {
            // Peut �tre nul dans le cas des applications non valid�es
            stat.setServerName( pApplicationBO.getServeurBO().getName() );
        }
        stat.setValidatedApplication( pApplicationBO.getStatus() == ApplicationBO.VALIDATED );
        stat.setPurgeFrequency( pApplicationBO.getResultsStorageOptions() );
        return stat;
    }

    /**
     * @param pAudits la liste tri�e des audits (ordre inverse)
     * @param pDaysForTerminatedAudit le nombre de jours max pour lesquels il doit y avoir au moins un audit r�ussi pour
     *            que l'application soit active
     * @return true si l'application a un audit r�ussi dans le <code>pDaysForTerminatedAudit</code> derniers jours
     */
    private static boolean hasTerminatedAuditSince( List pAudits, int pDaysForTerminatedAudit )
    {
        boolean has = false;
        // On calcule la date limite pour compter les audits
        Calendar minDate = Calendar.getInstance();
        minDate.add( Calendar.DAY_OF_MONTH, -pDaysForTerminatedAudit );
        boolean dateInTime = true;
        AuditBO currentAudit = null;
        for ( int i = 0; i < pAudits.size() && !has && dateInTime; i++ )
        {
            currentAudit = (AuditBO) pAudits.get( i );
            // On teste si on est encore dans la p�riode de temps souhaiter
            if ( null != currentAudit.getDate() && currentAudit.getDate().compareTo( minDate.getTime() ) < 0 )
            {
                dateInTime = false;
            }
            has = ( dateInTime && currentAudit.getStatus() == AuditBO.TERMINATED );
        }
        return has;
    }

    /**
     * @param pAudits la liste tri�e des audits (ordre inverse)
     * @return le dernier audit ex�cut�
     */
    private static AuditBO getLastExecutedAudit( List pAudits )
    {
        AuditBO lastAudit = null;
        // On parcours les audits jusqu'� un trouv� le dernier ex�cut�
        // (i.e. le premier qui n'est pas en attente dans la liste)
        AuditBO currentAudit = null;
        for ( int i = 0; i < pAudits.size() && null == lastAudit; i++ )
        {
            currentAudit = (AuditBO) pAudits.get( i );
            if ( currentAudit.getStatus() != AuditBO.NOT_ATTEMPTED && currentAudit.getStatus() != AuditBO.RUNNING )
            {
                lastAudit = currentAudit;
            }
        }
        return lastAudit;
    }

    /**
     * @param pAudits les audits
     * @return ne nombre d'audits partiel ou en �chec
     */
    private static int getNbPartialOrFailedAudits( List pAudits )
    {
        int nbAudits = 0;
        AuditBO currentAudit = null;
        for ( int i = 0; i < pAudits.size(); i++ )
        {
            currentAudit = (AuditBO) pAudits.get( i );
            if ( currentAudit.getStatus() == AuditBO.FAILED || currentAudit.getStatus() == AuditBO.PARTIAL )
            {
                // On veut le nombre d'audits en �chec ou patiel
                nbAudits++;
            }
        }
        return nbAudits;
    }

    /**
     * @param pAudits les audits
     * @return ne nombre d'audits r�ussis
     */
    private static int getNbTerminatedAudits( List pAudits )
    {
        int nbAudits = 0;
        AuditBO currentAudit = null;
        for ( int i = 0; i < pAudits.size(); i++ )
        {
            currentAudit = (AuditBO) pAudits.get( i );
            if ( currentAudit.getStatus() == AuditBO.TERMINATED )
            {
                // On veut le nombre d'audits en �chec ou patiel
                nbAudits++;
            }
        }
        return nbAudits;
    }

    /**
     * Compte les audits dont le statut est "en echec", "partiel" ou "r�ussi" r�alis�s durant les
     * <code>pDaysForAllAudits</code> derniers jours
     * 
     * @param pAudits les audits tri�s par date d'ex�cution (ordre inverse)
     * @param pDaysForAllAudits le nombre de jours d�fini pour compter les audits
     * @return le nombre d'audits r�alis�s durant les <code>pDaysForAllAudits</code> derniers jours
     */
    private static int getNbAuditsSince( List pAudits, int pDaysForAllAudits )
    {
        int nbAudits = 0;
        // On calcule la date limite pour compter les audits
        Calendar minDate = Calendar.getInstance();
        minDate.add( Calendar.DAY_OF_MONTH, -pDaysForAllAudits );
        boolean dateInTime = true;
        AuditBO currentAudit = null;
        for ( int i = 0; i < pAudits.size() && dateInTime; i++ )
        {
            currentAudit = (AuditBO) pAudits.get( i );
            // On teste si on est encore dans la p�riode de temps souhaiter
            if ( null != currentAudit.getDate() && currentAudit.getDate().compareTo( minDate.getTime() ) < 0 )
            {
                dateInTime = false;
            }
            // On ne prend pas les status "en cours", "supprim�" et "en attente"
            if ( dateInTime && currentAudit.getStatus() != AuditBO.NOT_ATTEMPTED
                && currentAudit.getStatus() != AuditBO.DELETED && currentAudit.getStatus() != AuditBO.RUNNING )
            {
                // On veut le nombre d'audits ex�cut�s donc on prend tous les audits
                // sauf ceux en attente
                nbAudits++;
            }
        }
        return nbAudits;
    }

    /**
     * @param pAudits les audits tri�s par date d'ex�cution (ordre inverse)
     * @param pStatus les statuts � rechercher
     * @return le dernier audit r�ussi
     */
    private static AuditBO getLastAudit( List pAudits, int[] pStatus )
    {
        // Initialisation
        AuditBO lastTerminatedAudit = null;
        // On tri le tableau des status pour pouvoir utiliser la recherche
        Arrays.sort( pStatus );
        // On parcours les audits jusqu'� un trouver un r�ussi
        AuditBO currentAudit = null;
        int curStatus = 0;
        for ( int i = 0; i < pAudits.size() && null == lastTerminatedAudit; i++ )
        {
            currentAudit = (AuditBO) pAudits.get( i );
            // On affecte le int pour flusher l'objet
            curStatus = currentAudit.getStatus();
            int id = Arrays.binarySearch( pStatus, curStatus );
            if ( id >= 0 && id < pStatus.length )
            {
                lastTerminatedAudit = currentAudit;
            }
        }
        return lastTerminatedAudit;
    }

    /**
     * @param pAudits les audits tri�s par date d'ex�cution
     * @return le premier audit r�ussi
     */
    private static AuditBO getFirstTerminatedAudit( List pAudits )
    {
        AuditBO firstTerminatedAudit = null;
        // On parcours les audits jusqu'� un trouv� un r�ussi
        AuditBO currentAudit = null;
        for ( int i = 0; i < pAudits.size() && null == firstTerminatedAudit; i++ )
        {
            currentAudit = (AuditBO) pAudits.get( i );
            if ( currentAudit.getStatus() == AuditBO.TERMINATED )
            {
                firstTerminatedAudit = currentAudit;
            }
        }
        return firstTerminatedAudit;
    }
}
