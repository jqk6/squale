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
package org.squale.squalix.tools.jdepend;

import org.squale.squalecommon.util.messages.BaseMessages;

/**
 * Messages pour la t�che ckjm
 */
public class JDependMessages
    extends BaseMessages
{
    /** Instance */
    static private JDependMessages mInstance = new JDependMessages();

    /**
     * Constructeur par d�faut. Priv� pour �viter l'instanciation.
     */
    private JDependMessages()
    {
        super( "org.squale.squalix.tools.jdepend.jdepend" );
    }

    /**
     * Retourne la cha�ne de caract�re identifi�e par la cl�.
     * 
     * @param pKey nom de la cl�.
     * @return la cha�ne associ�e.
     */
    public static String getString( String pKey )
    {
        return mInstance.getBundleString( pKey );
    }
}
