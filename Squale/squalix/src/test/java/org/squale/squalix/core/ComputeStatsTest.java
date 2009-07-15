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
package org.squale.squalix.core;

import java.util.Collection;

import org.squale.jraf.commons.exception.JrafDaoException;
import org.squale.squalecommon.SqualeTestCase;
import org.squale.squalecommon.daolayer.stats.SiteAndProfilStatsDICTDAOImpl;
import org.squale.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import org.squale.squalecommon.enterpriselayer.businessobject.config.ProjectProfileBO;
import org.squale.squalecommon.enterpriselayer.businessobject.rule.QualityGridBO;
import org.squale.squalix.stats.ComputeStats;

/**
 */
// UNIT_KO : Un requ�te hibernate utilise la comparaison oracle des dates.
// Il faudrait mettre la m�thode priv�e 'calculateGlobalROI' en protected pour pouvoir
// cr�er une classe stub la surchargeant permettant ainsi de tester la m�thode 'computeDICTStats'
// sans prendre en compte le calcul du ROI.
public class ComputeStatsTest
    extends SqualeTestCase
{

    /** le nom de la grille */
    private final static String GRID_NAME = "java";

    /**
     * M�thode cr�ant l'environnement
     * 
     * @throws Exception en cas d'�checs
     */
    protected void setUp()
        throws Exception
    {
        super.setUp();
        getSession().beginTransaction();
        ApplicationBO appli1 = getComponentFactory().createApplicationWithSite( getSession(), "qvi" );
        QualityGridBO grid1 = getComponentFactory().createGrid( getSession() );
        grid1.setName( GRID_NAME );
        ProjectProfileBO profile = getComponentFactory().createProjectProfileWithName( getSession(), grid1.getName() );
        ProjectBO project = getComponentFactory().createProject( getSession(), appli1, grid1 );
        project.setProfile( profile );
        AuditBO audit1 = getComponentFactory().createAuditResult( appli1 );
        audit1.setStatus( AuditBO.TERMINATED );
        AuditBO audit2 = getComponentFactory().createAuditResult( appli1 );
        audit2.setStatus( AuditBO.FAILED );
        AuditBO audit3 = getComponentFactory().createAuditResult( appli1 );
        audit3.setStatus( AuditBO.NOT_ATTEMPTED );
        getSession().commitTransactionWithoutClose();
    }

    /**
     * Teste le bon calcul des stats et la bonne r�cup�ration avec l'environnement d�fini
     * 
     * @throws JrafDaoException en cas d'�checs
     */
    public void testComputeStats()
        throws JrafDaoException
    {
        ComputeStats computer = new ComputeStats();
        getSession().beginTransaction();
        computer.computeDICTStats();
        getSession().commitTransactionWithoutClose();
        getSession().beginTransaction();
        Collection siteAndProfilsColl = SiteAndProfilStatsDICTDAOImpl.getInstance().findAll( getSession() );
        // 1 seul profil et un seul site
        assertEquals( 1, siteAndProfilsColl.size() );

    }
}
