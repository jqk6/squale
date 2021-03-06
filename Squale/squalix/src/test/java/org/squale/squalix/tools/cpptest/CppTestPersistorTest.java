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
package org.squale.squalix.tools.cpptest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.squale.squalecommon.SqualeTestCase;
import org.squale.squalecommon.daolayer.result.MeasureDAOImpl;
import org.squale.squalecommon.daolayer.rulechecking.CppTestRuleSetDAOImpl;
import org.squale.squalecommon.datatransfertobject.rulechecking.CppTestRuleSetDTO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import org.squale.squalecommon.enterpriselayer.businessobject.result.rulechecking.CppTestTransgressionBO;
import org.squale.squalecommon.enterpriselayer.businessobject.result.rulechecking.RuleCheckingTransgressionItemBO;
import org.squale.squalecommon.enterpriselayer.businessobject.rulechecking.RuleBO;
import org.squale.squalecommon.enterpriselayer.businessobject.rulechecking.cpptest.CppTestRuleSetBO;

/**
 * Test de CppTestPersistor
 */
public class CppTestPersistorTest
    extends SqualeTestCase
{

    /**
     * Test de stockage des r�sultats
     */
    public void testStoreResults()
    {
        try
        {
            CppTestPersistor persistor = new CppTestPersistor();
            getSession().beginTransaction();
            // On cr�e un projet avec un audit
            ApplicationBO appli = getComponentFactory().createApplication( getSession() );
            ProjectBO project = getComponentFactory().createProject( getSession(), appli, null );
            AuditBO audit = getComponentFactory().createAudit( getSession(), project );
            // On simule les r�sultats renvoy�s par Cpptest sous la forme
            // de deux transgression
            Map results = new HashMap();
            ArrayList list1 = new ArrayList();
            RuleCheckingTransgressionItemBO item1_1 = new RuleCheckingTransgressionItemBO();
            item1_1.setComponentFile( "file1_1" );
            item1_1.setLine( 2 );
            item1_1.setMessage( "detail1_1" );
            list1.add( item1_1 );
            results.put( "rulecode1", list1 );
            ArrayList list2 = new ArrayList();
            RuleCheckingTransgressionItemBO item2_1 = new RuleCheckingTransgressionItemBO();
            item2_1.setComponentFile( "file2_1" );
            item2_1.setLine( 2 );
            item2_1.setMessage( "detail2_1" );
            list2.add( item2_1 );
            RuleCheckingTransgressionItemBO item2_2 = new RuleCheckingTransgressionItemBO();
            item2_2.setComponentFile( "file2_2" );
            item2_2.setLine( 1 );
            item2_2.setMessage( "detail2_1_2" );
            list2.add( item2_2 );
            results.put( "rulecode2", list2 );
            CppTestRuleSetBO ruleset = new CppTestRuleSetBO();
            RuleBO rule = new RuleBO();
            rule.setCategory( "cat" );
            rule.setRuleSet( ruleset );
            rule.setCode( "rulecode1" );
            ruleset.addRule( rule );
            rule = new RuleBO();
            rule.setCategory( "cat" );
            rule.setRuleSet( ruleset );
            rule.setCode( "rulecode2" );
            ruleset.addRule( rule );
            CppTestRuleSetDAOImpl.getInstance().create( getSession(), ruleset );
            getSession().commitTransactionWithoutClose();
            CppTestRuleSetDTO rsdto = new CppTestRuleSetDTO();
            rsdto.setId( ruleset.getId() );
            getSession().beginTransaction();
            persistor.storeResults( getSession(), project, audit, results, rsdto );
            getSession().commitTransactionWithoutClose();
            // V�rification des r�sultats obtenus
            MeasureDAOImpl dao = MeasureDAOImpl.getInstance();
            assertEquals( 1, dao.count( getSession() ).intValue() );
            CppTestTransgressionBO trans =
                (CppTestTransgressionBO) MeasureDAOImpl.getInstance().findAll( getSession() ).get( 0 );
            final int detailCount = 3;
            assertEquals( detailCount, trans.getDetails().size() );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }
}
