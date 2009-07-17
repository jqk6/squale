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
package org.squale.squalecommon.enterpriselayer.facade.config;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.squale.jraf.commons.exception.JrafEnterpriseException;
import org.squale.squalecommon.SqualeTestCase;
import org.squale.squalecommon.daolayer.config.web.AbstractDisplayConfDAOImpl;
import org.squale.squalecommon.daolayer.rule.QualityGridDAOImpl;
import org.squale.squalecommon.datatransfertobject.config.ProjectProfileDTO;
import org.squale.squalecommon.datatransfertobject.config.SqualixConfigurationDTO;
import org.squale.squalecommon.datatransfertobject.rule.QualityGridDTO;
import org.squale.squalecommon.enterpriselayer.businessobject.config.web.BubbleConfBO;
import org.squale.squalecommon.enterpriselayer.businessobject.config.web.VolumetryConfBO;
import org.squale.squalecommon.enterpriselayer.businessobject.rule.QualityGridBO;

/**
 * Test l'importation d'une configuration en base de donn�es
 */
public class ConfigurationImportTest
    extends SqualeTestCase
{

    /**
     * Importation nominale de la configuration
     */
    public void testImportNominal()
    {
        StringBuffer errors = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream( "config/squalix-config.xml" );
        SqualixConfigurationDTO conf;
        try
        {
            conf = ConfigurationImport.importConfig( stream, errors );
            assertEquals( 2, conf.getSourceManagements().size() );
            final int nbProfiles = 2;
            assertEquals( nbProfiles, conf.getProfiles().size() );
            assertEquals( 0, errors.length() );
        }
        catch ( JrafEnterpriseException e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }

    /**
     * Test de la cr�ation sans donn�es en base
     */
    public void testCreateConfig()
    {
        StringBuffer errors = new StringBuffer();
        InputStream stream = getClass().getClassLoader().getResourceAsStream( "data/config/squalix-config_simple.xml" );
        SqualixConfigurationDTO conf;
        try
        {
            // On cr�e la grille grid
            getComponentFactory().createGrid( getSession() );
            conf = ConfigurationImport.createConfig( stream, errors );
            assertNotNull( conf );
            assertTrue( errors.length() == 0 );
            // 1 profil
            assertEquals( 1, conf.getProfiles().size() );
            ProjectProfileDTO profileDTO = (ProjectProfileDTO) conf.getProfiles().iterator().next();
            // Associ� � 1 t�che d'analyse
            assertEquals( 8, profileDTO.getAnalysisTasks().size() );
            // 1 grille associ�e
            assertEquals( 1, profileDTO.getGrids().size() );
            // qui se nomme "grid"
            assertEquals( "grid", profileDTO.getGrids().get( 0 ) );
            // On r�cup�re la grille "grid" qui doit avoir un profil associ�
            Collection grids = QualityGridDAOImpl.getInstance().findWhereProfile( getSession(), profileDTO.getId() );
            assertEquals( 1, grids.size() );
            assertEquals( "grid", ( (QualityGridBO) grids.iterator().next() ).getName() );
            // On a aussi cr�e 3 configurations
            AbstractDisplayConfDAOImpl displayDAO = AbstractDisplayConfDAOImpl.getInstance();
            // 1 bubble
            List bubbleConf = displayDAO.findAllSubclass( getSession(), BubbleConfBO.class );
            assertEquals( 1, bubbleConf.size() );
            // 2 volum�try
            List volConf = displayDAO.findAllSubclass( getSession(), VolumetryConfBO.class );
            assertEquals( 2, volConf.size() );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "unexpected exception" );
        }
    }
}
