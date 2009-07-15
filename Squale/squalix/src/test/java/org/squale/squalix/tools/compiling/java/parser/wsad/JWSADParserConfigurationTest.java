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
 * Cr�� le 3 ao�t 05, par M400832.
 */
package org.squale.squalix.tools.compiling.java.parser.wsad;

import org.squale.squalecommon.SqualeTestCase;

/**
 * Cette classe teste la validit� de la configuration de la t�che de compilation JAVA.
 */
public class JWSADParserConfigurationTest
    extends SqualeTestCase
{

    /**
     * Instance de configuration pour un parser WSAD.
     */
    private JWSADParserConfiguration mConf = null;

    /** test sur la cl� d'ancrage du classpath */
    public void testClasspathAnchor()
    {
        assertEquals( mConf.getClasspathAnchor(), "classpath" );
    }

    /** test sur la cl� d'entr�e du classpath */
    public void testClasspathentry()
    {
        assertEquals( mConf.getClasspathentry(), "classpathentry" );
    }

    /** test sur la cl� d�crivant le type */
    public void testKind()
    {
        assertEquals( mConf.getKind(), "kind" );
    }

    /** test sur la cl� path */
    public void testPath()
    {
        assertEquals( mConf.getPath(), "path" );
    }

    /** test sur la cl� exported */
    public void testExported()
    {
        assertEquals( mConf.getExported(), "exported" );
    }

    /** test sur la cl� lib */
    public void testLib()
    {
        assertEquals( mConf.getLib(), "lib" );
    }

    /** test sur la cl� src */
    public void testSrc()
    {
        assertEquals( mConf.getSrc(), "src" );
    }

    /** test sur la cl� var */
    public void testVar()
    {
        assertEquals( mConf.getVar(), "var" );
    }

    /**
     * test sur la cl� d�crivant le nom du fichier � partir duquel on r�cup�re les informations
     */
    public void testFilename()
    {
        assertEquals( mConf.getFilename(), ".classpath" );
    }

    /**
     * Tear-down.
     * 
     * @throws Exception en cas de probl�mes
     */
    protected void tearDown()
        throws Exception
    {
        super.tearDown();
    }

    /**
     * {@inheritDoc}
     * 
     * @throws Exception {@inheritDoc}
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp()
        throws Exception
    {
        super.setUp();
        mConf = new JWSADParserConfiguration();
    }

}
