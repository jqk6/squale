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
package com.airfrance.squalecommon.enterpriselayer.applicationcomponent.display;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.jraf.commons.exception.JrafEnterpriseException;
import com.airfrance.squalecommon.SqualeTestCase;
import com.airfrance.squalecommon.daolayer.component.AbstractComponentDAOImpl;
import com.airfrance.squalecommon.daolayer.component.AuditDAOImpl;
import com.airfrance.squalecommon.daolayer.component.ProjectDAOImpl;
import com.airfrance.squalecommon.daolayer.rule.QualityGridDAOImpl;
import com.airfrance.squalecommon.datatransfertobject.component.ComponentDTO;
import com.airfrance.squalecommon.datatransfertobject.rule.QualityGridDTO;
import com.airfrance.squalecommon.datatransfertobject.transform.component.ComponentTransform;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ClassBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.MethodBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.PackageBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.config.ProjectProfileBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.QualityGridBO;
import com.airfrance.squalecommon.enterpriselayer.facade.rule.AuditComputing;
import com.airfrance.squalecommon.enterpriselayer.facade.rule.QualityGridImport;

/**
 * @author M400841 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class ResultsApplicationComponentAccessTest
    extends SqualeTestCase
{

    /**
     * @throws JrafEnterpriseException si erreur
     * @throws JrafDaoException si erreur
     */
    public void testGetApplicationResults()
        throws JrafEnterpriseException, JrafDaoException
    {
        getSession().beginTransaction();
        ResultsApplicationComponentAccess resultAccess = new ResultsApplicationComponentAccess();
        // Chargement de la grille
        InputStream stream = getClass().getClassLoader().getResourceAsStream( "data/grid/grid_compute.xml" );
        StringBuffer errors = new StringBuffer();
        Collection grids;
        grids = QualityGridImport.createGrid( stream, errors );
        QualityGridBO grid =
            (QualityGridBO) QualityGridDAOImpl.getInstance().load(
                                                                   getSession(),
                                                                   new Long(
                                                                             ( (QualityGridDTO) grids.iterator().next() ).getId() ) );
        // Cr�ation de l'application
        ApplicationBO application = getComponentFactory().createApplication( getSession() );
        // Cr�ation du projet
        ProjectBO project = getComponentFactory().createProject( getSession(), application, grid );
        // cr�ation du profil du projet
        ProjectProfileBO profileBO = getComponentFactory().createProjectProfile( getSession() );
        project.setProfile( profileBO );
        ProjectDAOImpl.getInstance().save( getSession(), project );
        // Cr�ation de l'audit
        AuditBO audit = new AuditBO();
        audit.setStatus( AuditBO.TERMINATED );
        audit.setName( "audit1" );
        AuditDAOImpl.getInstance().create( getSession(), audit );
        application.addAudit( audit );
        AbstractComponentDAOImpl.getInstance().save( getSession(), application );
        project.addAudit( audit );
        AbstractComponentDAOImpl.getInstance().save( getSession(), project );
        // Cr�ation du package
        PackageBO pkg = getComponentFactory().createPackage( getSession(), project );
        pkg.addAudit( audit );
        AbstractComponentDAOImpl.getInstance().save( getSession(), pkg );
        // Cr�ation de la classe
        ClassBO cls = getComponentFactory().createClass( getSession(), pkg );
        cls.addAudit( audit );
        AbstractComponentDAOImpl.getInstance().save( getSession(), cls );
        // Cr�ation de la m�thode
        MethodBO method = getComponentFactory().createMethod( getSession(), cls );
        method.addAudit( audit );
        AbstractComponentDAOImpl.getInstance().save( getSession(), cls );
        getComponentFactory().createMeasures( getSession(), audit, project, cls, method );
        // Calcul de l'audit
        AuditComputing.computeAuditResult( getSession(), project, audit );
        getSession().commitTransactionWithoutClose();
        ComponentDTO appliDTO = ComponentTransform.bo2Dto( application );
        List components = new ArrayList();
        components.add( appliDTO );
        List appliResults = resultAccess.getApplicationResults( components, null );
        // On doit avoir les r�sultats pour sloc et comments de rsm
        assertEquals( 1, appliResults.size() );
    }

}
