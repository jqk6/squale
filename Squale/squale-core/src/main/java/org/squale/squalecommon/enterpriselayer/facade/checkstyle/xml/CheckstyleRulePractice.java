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
 * Cr�� le 2 nov. 06
 *
 * Lors du parcours du fichier de configuration Checkstyle via le Digester,
 * m�morisation sur la balise "metadata" de la pratique associ�e � la r�gle
 */
package org.squale.squalecommon.enterpriselayer.facade.checkstyle.xml;

import org.xml.sax.Attributes;

import org.squale.squalecommon.util.xml.FactoryAdapter;

/**
 * M�morisation de la cat�gorie lors de l'instanciation
 * 
 * @param pAttributes attributs code et valeur
 * @return null
 * @throws Exception erreur de param�trage
 */
public class CheckstyleRulePractice
    extends FactoryAdapter
{
    /** cat�gorie de la r�gle */
    private String practice;

    /** Constructeur */
    public CheckstyleRulePractice()
    {
    }

    /**
     * M�morisation de la cat�gorie lors de l'instanciation
     * 
     * @param pAttributes code et valeur de la balise metadata
     * @return null
     * @throws Exception balise metadata mal param�tr�e
     */
    public Object createObject( Attributes pAttributes )
        throws Exception
    {
        String name = pAttributes.getValue( "name" );
        String value = pAttributes.getValue( "value" );

        // Test si le pattern metadata est en rapport avec le nom de la r�gle
        if ( name.trim().equals( XmlCheckstyleParsingMessages.getString( "checkstyle.rule.practice" ) ) )
        {
            practice = value;
        }
        else
        {
            throw new Exception( XmlCheckstyleParsingMessages.getString( "checkstyle.pattern.reserved",
                                                                         new Object[] { "\n<module>\n\t"
                                                                             + "<module>\n\t\t" + "<metadata>\n" } ) );
        }
        return null;
    }

    /**
     * @return category
     */
    public String getPractice()
    {
        return practice;
    }

}
