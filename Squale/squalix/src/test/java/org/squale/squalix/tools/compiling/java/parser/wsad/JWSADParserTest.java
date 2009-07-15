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
package org.squale.squalix.tools.compiling.java.parser.wsad;

import java.util.LinkedList;

import org.squale.squalecommon.SqualeTestCase;
import org.squale.squalix.tools.compiling.java.beans.JWSADProject;

/**
 * Test du parser de fichier de configuration WSAD
 */
public class JWSADParserTest
    extends SqualeTestCase
{

    /**
     * Test de parsing du fichier .classpath Ce tests suppose qu'il est ex�cut� dans le r�pertoire du projet WSAD
     * courant
     */
    public void testExecuteWSAD()
    {
        // Le fait de creer un projet vide permet son traitement avec junit
        // car il cherchera � travailler sur le r�pertoire courant
        JWSADProject project = new JWSADProject();
        project.setPath( "./" );
        assertEquals( "Le classpath devrait �tre vide", project.getClasspath(), "" );
        assertEquals( "Le chemin des sources devrait �tre vide", project.getSrcPath(), "" );
        assertEquals( "Le classpath des librairies export�es devrait �tre vide", project.getExportedLib(), "" );
        LinkedList list = new LinkedList();
        list.add( project );
        try
        {
            JWSADParser parser = new JWSADParser( list );
            parser.execute();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "Unexpected exception" );
        }
        assertTrue( "Le classpath n'a pas �t� pars�", project.getClasspath().length() > 0 );
        // Le projet n'exporte aucune librairie
        assertTrue( "Le classpath des librairies export�es devrait �tre vide", project.getExportedLib().length() == 0 );
        assertTrue( "Le fichier .classpath a mal �t� pars�", project.getSrcPath().length() > 0 );
        assertFalse( "Le projet n'est pas compil�", project.isCompiled() );
        assertTrue( "Le projet est un projet WSAD", project.isWSAD() );
    }

    /**
     * Test de parsing en l'absence du fichier .classpath
     */
    public void testExecuteNonWSAD()
    {
        // Le fait de creer un projet vide permet son traitement avec junit
        // car il cherchera � travailler sur le r�pertoire courant
        JWSADProject project = new JWSADProject();
        project.setPath( "../" );
        assertEquals( "Le classpath ne devrait �tre vide", project.getClasspath(), "" );
        assertEquals( "Le chemin des sources devrait �tre nul", project.getSrcPath(), "" );
        LinkedList list = new LinkedList();
        list.add( project );
        try
        {
            JWSADParser parser = new JWSADParser( list );
            parser.execute();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "Unexpected exception" );
        }
        assertEquals( "Le classpath ne devrait �tre vide", project.getClasspath(), "" );
        assertEquals( "Le chemin des sources devrait �tre nul", project.getSrcPath(), "" );
        assertTrue( "Le projet est consid�r� compil�", project.isCompiled() );
        assertFalse( "Le projet est un projet WSAD", project.isWSAD() );
    }

    /**
     * Teste l'ajout des librairies export�es dans les classpath
     */
    public void testWSADProjects()
    {
        final String root = "./data/ClasspathForJavaCompilingTest/WSADProjects/";
        LinkedList list = new LinkedList();
        // Projet D
        final String DRoot = root + "ProjectD/";
        JWSADProject projectD = new JWSADProject();
        projectD.setName( "ProjectD" );
        projectD.setPath( DRoot );
        list.add( projectD );
        // Projet C
        final String CRoot = root + "ProjectC/";
        JWSADProject projectC = new JWSADProject();
        projectC.setName( "ProjectC" );
        projectC.setPath( CRoot );
        list.add( projectC );
        // Projet B
        final String BRoot = root + "ProjectB/";
        JWSADProject projectB = new JWSADProject();
        projectB.setName( "ProjectB" );
        projectB.setPath( BRoot );
        list.add( projectB );
        // Projet A
        final String ARoot = root + "ProjectA/";
        JWSADProject projectA = new JWSADProject();
        projectA.setName( "ProjectA" );
        projectA.setPath( ARoot );
        list.add( projectA );
        try
        {
            JWSADParser parser = new JWSADParser( list );
            parser.execute();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "Unexpected exception" );
        }
        // V�rification projet D
        assertTrue( "Le projet D est un projet WSAD", projectD.isWSAD() );
        assertEquals( "Le chemin des sources de D", DRoot + "src/;", projectD.getSrcPath() );
        assertTrue( "Le classpath de D a exportedDLib.jar", projectD.getClasspath().matches( ".*exportedDLib.*" ) );
        assertTrue( "Le classpath de D a DLib.jar", projectD.getClasspath().matches( ".*/DLib.*" ) );
        assertTrue( "Les librairies export�es de D ont exportedDLib.jar",
                    projectD.getExportedLib().matches( ".*exportedDLib.*" ) );
        // V�rification projet C
        assertTrue( "Le projet C est un projet WSAD", projectC.isWSAD() );
        assertEquals( "Le chemin des sources de C", CRoot + "src/;", projectC.getSrcPath() );
        assertTrue( "Le classpath de C a exportedCLib.jar", projectC.getClasspath().matches( ".*/exportedCLib.*" ) );
        assertTrue( "Le classpath de C a la librairie export�e de D",
                    projectC.getClasspath().matches( ".*exportedDLib.*" ) );
        assertFalse( "Le classpath de C n'a pas DLib.jar", projectC.getClasspath().matches( ".*/DLib.*" ) );
        assertTrue( "Les librairies export�es de C ont exportedCLib.jar",
                    projectC.getExportedLib().matches( ".*exportedCLib.*" ) );
        // V�rification projet B
        assertTrue( "Le projet B est un projet WSAD", projectB.isWSAD() );
        assertEquals( "Le chemin des sources de B", BRoot + "src/;", projectB.getSrcPath() );
        assertTrue( "Le classpath de B a exportedBLib.jar", projectB.getClasspath().matches( ".*exportedBLib.*" ) );
        assertTrue( "Le classpath de B a la librairie export�e de C",
                    projectB.getClasspath().matches( ".*exportedCLib.*" ) );
        assertTrue( "Le classpath de B a la librairie export�e de D",
                    projectB.getClasspath().matches( ".*exportedDLib.*" ) );
        assertTrue( "Les librairies export�es de B ont exportedBLib.jar",
                    projectB.getExportedLib().matches( ".*exportedBLib.*" ) );
        // V�rification projet A
        assertTrue( "Le projet a est un projet WSAD", projectA.isWSAD() );
        assertTrue( "Le classpath de A a exportedALib.jar", projectA.getClasspath().matches( ".*exportedALib.*" ) );
        assertTrue( "Le classpath de A a la librairie export�e de B",
                    projectA.getClasspath().matches( ".*exportedBLib.*" ) );
        assertTrue( "Le classpath de A a la librairie export�e de C",
                    projectA.getClasspath().matches( ".*exportedCLib.*" ) );
        assertTrue( "Le classpath de A a la librairie export�e de D",
                    projectA.getClasspath().matches( ".*exportedDLib.*" ) );
        assertTrue( "Les librairies export�es de A ont exportedALib.jar",
                    projectA.getExportedLib().matches( ".*exportedALib.*" ) );
    }
}
