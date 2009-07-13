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
 * Cr�� le 13 oct. 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package org.squale.welcom.outils.rsh;

import org.squale.welcom.outils.rsh.Impl.CmdRshClient;
import org.squale.welcom.outils.rsh.Impl.JavaRshClient;

/**
 * @author M327837 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class RshClientFactory
{
    /**
     * Contructeur
     */
    private RshClientFactory()
    {
        super();
    }

    /**
     * Retourne un rsh client en fonction de la plateforme.
     * 
     * @param serveur : Serveur
     * @param loginDistant : Logon Distant
     * @param loginLocal : Login Local
     * @return RshClient en fonction de l'os ... soit full java ou rsh
     */
    public static RshClient getRshClient( final String serveur, final String loginDistant, final String loginLocal )
    {
        if ( System.getProperty( "os.name" ).equals( "SunOS" ) )
        {
            return new CmdRshClient( serveur, loginDistant, loginLocal );
        }
        else
        {
            return new JavaRshClient( serveur, loginDistant, loginLocal );
        }
    }
}