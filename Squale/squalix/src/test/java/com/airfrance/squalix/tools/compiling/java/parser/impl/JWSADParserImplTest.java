package com.airfrance.squalix.tools.compiling.java.parser.impl;

import java.util.LinkedList;

import com.airfrance.squalecommon.SqualeTestCase;
import com.airfrance.squalix.tools.compiling.java.beans.JWSADProject;

/**
 *
 */
public class JWSADParserImplTest
    extends SqualeTestCase
{

    /**
     * Test de l'impl�mentation d'un parser de projet WSAD
     */
    public void testExecute()
    {
        // Le fait de creer un projet vide permet son traitement avec junit
        // car il cherchera � travailler sur le r�pertoire courant
        JWSADProject project = new JWSADProject();
        project.setPath( "./" );
        assertEquals( "Le classpath ne devrait �tre vide", project.getClasspath(), "" );
        assertEquals( "Le chemin des sources devrait �tre nul", project.getSrcPath(), "" );
        LinkedList list = new LinkedList();
        list.add( project );
        try
        {
            JWSADParserImpl parser = new JWSADParserImpl( list );
            parser.execute();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "Unexpected exception" );
        }
        assertTrue( "Le classpath n'a pas �t� pars�", project.getClasspath().length() > 0 );
        assertTrue( "Le fichier .classpath a mal �t� pars�", project.getSrcPath().length() > 0 );
        assertFalse( "Le projet n'est pas compil�", project.isCompiled() );
        assertTrue( "Le projet est un projet WSAD", project.isWSAD() );
    }

}
