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
 * Created on 4 ao�t 06
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.airfrance.squalix.core;

import java.util.ArrayList;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.squalecommon.SqualeTestCase;
import com.airfrance.squalecommon.daolayer.component.ApplicationDAOImpl;
import com.airfrance.squalecommon.daolayer.component.AuditDAOImpl;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AuditBO;

/**
 * Test pour la purge
 */
// UNIT_KO : Test impossible car le delete ne delete pas en cascade dans hibersonic.
public class PurgeTest
    extends SqualeTestCase
{

    /** application deleted */
    private ApplicationBO appli2;

    /**
     * Set-up
     * 
     * @throws Exception en cas de probl�mes
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp()
        throws Exception
    {
        super.setUp();
        getSession().beginTransaction();
        // On cr�e la premi�re application qui aura un audit
        // supprim� et un en attente
        ApplicationBO appli = getComponentFactory().createApplicationWithSite( getSession(), "qvi" );
        appli.setName( "onepurge" );
        AuditBO audit = new AuditBO();
        audit.setName( "audit1" );
        audit.setType( AuditBO.MILESTONE );
        AuditDAOImpl.getInstance().create( getSession(), audit );
        appli.addAudit( audit );
        audit = new AuditBO();
        audit.setName( "audit2" );
        audit.setType( AuditBO.MILESTONE );
        audit.setStatus( AuditBO.DELETED );
        AuditDAOImpl.getInstance().create( getSession(), audit );
        appli.addAudit( audit );
        ApplicationDAOImpl.getInstance().save( getSession(), appli );

        // On cr�e la deuxi�me application avec le statut 'supprim�'
        appli2 = getComponentFactory().createApplication( getSession() );
        appli2.setServeurBO( appli.getServeurBO() );
        appli2.setName( "deletedappli" );
        appli2.setStatus( ApplicationBO.DELETED );
        audit = new AuditBO();
        audit.setName( "audit1" );
        audit.setType( AuditBO.MILESTONE );
        AuditDAOImpl.getInstance().create( getSession(), audit );
        appli2.addAudit( audit );
        ApplicationDAOImpl.getInstance().save( getSession(), appli2 );

        getSession().commitTransactionWithoutClose();

    }

    /**
     * Test de la purge -> impossible en fait :-)
     * 
     * @throws JrafDaoException en cas de pb JRAF
     * @throws InterruptedException en cas de pb JRAF
     */
    public void testPurge()
        throws JrafDaoException, InterruptedException
    {
        // delete ne delete pas en cascade dans hibersonic !
        // donc le test de purge n'est pas possible avec la FK de component_audits
        // Purge purge=new Purge("qvi");
        // purge.start();

        // donc test juste que le nombre d'audit est correct
        // 1 audit supprim� + 1 audit d'appli supprim�
        assertEquals( 2, AuditDAOImpl.getInstance().findDeleted( getSession(), appli2.getServeurBO().getServeurId(),
                                                                 new ArrayList() ).size() );
        getSession().rollbackTransaction();
    }
}
