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

import java.util.Collection;
import java.util.List;

import com.airfrance.jraf.commons.exception.JrafEnterpriseException;
import com.airfrance.jraf.helper.AccessDelegateHelper;
import com.airfrance.jraf.spi.accessdelegate.IApplicationComponent;
import com.airfrance.squalecommon.SqualeTestCase;
import com.airfrance.squalecommon.datatransfertobject.component.ComponentDTO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ComponentType;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;

/**
 * @author M400841 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class ComponentApplicationComponentAccessTest
    extends SqualeTestCase
{

    /**
     * Test d'acc�s au composant
     */
    public void testComponentApplicationComponentAccess()
    {
        // Teste si la construction de l'application component par AccessDelegateHelper
        IApplicationComponent appComponent;
        try
        {
            appComponent = AccessDelegateHelper.getInstance( "Component" );
            assertNotNull( appComponent );
        }
        catch ( JrafEnterpriseException e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }

    /**
     * @throws JrafEnterpriseException
     */
    public void testGetChildren()
    {
        // Teste si la methode est accessible par AccessDelegateHelper
        // et si l'objet renvoy� n'est pas nul
        IApplicationComponent appComponent;

        // Initialisation de ce qu'on r�cup�re
        Collection children = null;

        try
        {
            // Cr�ation des objets
            getSession().beginTransaction();
            ApplicationBO appli = getComponentFactory().createTestApplication();
            ProjectBO project = (ProjectBO) appli.getChildren().iterator().next();
            getComponentFactory().createAudit( getSession(), project );
            getSession().commitTransactionWithoutClose();

            // Initialisation des parametres sous forme de tableaux d'objets
            ComponentDTO existingProject = new ComponentDTO();
            existingProject.setID( project.getId() );
            Object[] paramIn = { existingProject, ComponentType.PACKAGE, null };

            appComponent = AccessDelegateHelper.getInstance( "Component" );
            children = (Collection) appComponent.execute( "getChildren", paramIn );

            // Test de l'architecture des donn�es renvoy�es
            assertTrue( children instanceof List );
            if ( children != null )
            {

                Object component = children.iterator().next();
                assertTrue( component instanceof ComponentDTO );

                ComponentDTO pkg = (ComponentDTO) component;
                assertTrue( pkg.getID() >= 0 );
                assertNotNull( pkg.getName() );
                assertEquals( ComponentType.PACKAGE, pkg.getType() );
                assertTrue( pkg.getNumberOfChildren() >= 0 );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }

}
