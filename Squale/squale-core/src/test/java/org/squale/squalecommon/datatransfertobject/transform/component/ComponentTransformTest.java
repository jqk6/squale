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
package org.squale.squalecommon.datatransfertobject.transform.component;

import org.squale.jraf.spi.persistence.ISession;
import org.squale.squalecommon.SqualeTestCase;
import org.squale.squalecommon.datatransfertobject.component.ComponentDTO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.AbstractComponentBO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.ClassBO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.ComponentType;
import org.squale.squalecommon.enterpriselayer.businessobject.component.PackageBO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import org.squale.squalecommon.enterpriselayer.businessobject.config.ProjectProfileBO;

/**
 * @author M400841 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class ComponentTransformTest
    extends SqualeTestCase
{

    /** Test non pr�vu */
    public void testDto2BoProxy()
    {
        /** TODO non pr�vu car pas d'update de composants en base */
    }

    /**
     * teste la transformation d'un componentBO en componentDTO
     * 
     * @throws Exception en cas d'�chec de la transformation
     */
    public void testBo2Dto()
        throws Exception
    {
        ISession session = getSession();
        session.beginTransaction();
        // cr�ation du profil du projet
        ProjectProfileBO profileBO = getComponentFactory().createProjectProfile( session );
        // Test pour un projet
        ApplicationBO applicationBO = getComponentFactory().createApplication( session );
        // Test pour un projet
        ProjectBO projectBO = getComponentFactory().createProject( session, applicationBO, null );
        // il faut ajouter le profil au projet car la factory ne le fait pas
        projectBO.setProfile( profileBO );
        // Test pour un package
        PackageBO packageBO = getComponentFactory().createPackage( session, projectBO );
        // Test pour une classe
        ClassBO classBO = getComponentFactory().createClass( session, packageBO );
        // Test pour une methode
        AbstractComponentBO methodBO = getComponentFactory().createMethod( session, classBO );
        session.commitTransactionWithoutClose();

        // Initialisation du retour
        ComponentDTO componentDTO = null;

        componentDTO = ComponentTransform.bo2Dto( methodBO );

        assertEquals( componentDTO.getName(), methodBO.getName() );
        assertEquals( componentDTO.getType(), ComponentType.METHOD );
        assertEquals( componentDTO.getID(), methodBO.getId() );

        componentDTO = ComponentTransform.bo2Dto( classBO );
        assertEquals( componentDTO.getName(), classBO.getName() );
        assertEquals( componentDTO.getType(), ComponentType.CLASS );
        assertEquals( componentDTO.getID(), classBO.getId() );

        componentDTO = ComponentTransform.bo2Dto( packageBO );
        assertEquals( componentDTO.getName(), packageBO.getName() );
        assertEquals( componentDTO.getType(), ComponentType.PACKAGE );
        assertEquals( componentDTO.getID(), packageBO.getId() );

        componentDTO = ComponentTransform.bo2Dto( projectBO );
        assertEquals( componentDTO.getName(), projectBO.getName() );
        assertEquals( componentDTO.getType(), ComponentType.PROJECT );
        assertEquals( componentDTO.getID(), projectBO.getId() );

        componentDTO = ComponentTransform.bo2Dto( applicationBO );
        assertEquals( componentDTO.getName(), applicationBO.getName() );
        assertEquals( componentDTO.getType(), ComponentType.APPLICATION );
        assertEquals( componentDTO.getID(), applicationBO.getId() );

    }

}
