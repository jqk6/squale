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
 * Cr�� le 12 juil. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.squalecommon.enterpriselayer.facade;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.airfrance.squalecommon.enterpriselayer.facade.component.ApplicationFacadeTest;
import com.airfrance.squalecommon.enterpriselayer.facade.component.AuditFacadeTest;
import com.airfrance.squalecommon.enterpriselayer.facade.component.ComponentFacadeTest;
import com.airfrance.squalecommon.enterpriselayer.facade.component.ProjectFacadeTest;
import com.airfrance.squalecommon.enterpriselayer.facade.component.UserFacadeTest;
import com.airfrance.squalecommon.enterpriselayer.facade.quality.ErrorFacadeTest;
import com.airfrance.squalecommon.enterpriselayer.facade.quality.MarkFacadeTest;
import com.airfrance.squalecommon.enterpriselayer.facade.quality.PracticeRuleFacadeTest;
import com.airfrance.squalecommon.enterpriselayer.facade.quality.QualityResultFacadeTest;
import com.airfrance.squalecommon.enterpriselayer.facade.quality.SqualeReferenceFacadeTest;

/**
 * @author M400841 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class FacadesTests
{

    /**
     * Suite de tests JUnit pour les facades
     * 
     * @return la suite de tests
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite( "Test for com.airfrance.squalecommon.enterpriselayer.facade.component" );
        // $JUnit-BEGIN$
        suite.addTest( new TestSuite( AuditFacadeTest.class ) );
        suite.addTest( new TestSuite( ComponentFacadeTest.class ) );
        suite.addTest( new TestSuite( ApplicationFacadeTest.class ) );
        suite.addTest( new TestSuite( ProjectFacadeTest.class ) );
        suite.addTest( new TestSuite( UserFacadeTest.class ) );
        suite.addTest( new TestSuite( ErrorFacadeTest.class ) );
        suite.addTest( new TestSuite( MarkFacadeTest.class ) );
        suite.addTest( new TestSuite( PracticeRuleFacadeTest.class ) );
        suite.addTest( new TestSuite( QualityResultFacadeTest.class ) );
        suite.addTest( new TestSuite( SqualeReferenceFacadeTest.class ) );

        // $JUnit-END$
        return suite;
    }
}
