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
package org.squale.squalecommon.util.mail;

/**
 * Exception d'envoi de mail Cette classe permet de remonter les erreurs lors de l'envoi de mail
 */
public class MailException
    extends Exception
{

    /**
     * Constructeur
     * 
     * @param e exception
     */
    public MailException( Exception e )
    {
        super( e );
    }

    /**
     * Constructor
     * 
     * @param message Error message
     */
    public MailException( String message )
    {
        super( message );
    }

}
