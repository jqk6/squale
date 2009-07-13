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
 * Cr�� le 8 oct. 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package org.squale.welcom.outils.rsh.Impl;

import java.io.IOException;

import org.apache.commons.io.CopyUtils;
import org.apache.commons.io.IOUtils;
import org.squale.welcom.outils.rsh.RshClient;


/**
 * @author M327837 Simule du rsh
 */
public class CmdRshClient
    extends RshClient
{
    /** programme rsh */
    private final static String RSH_FILE = "/bin/rsh";

    /** Valeur par defult de retour */
    private final static int DEFAULT_EXIT_VAL = 35;

    /** Commande */
    private String rshCmd = null;

    /**
     * @param serveur : Serveur poru le rsh
     * @param loginDistant : login distant
     * @param loginLocal : login local
     */
    public CmdRshClient( final String serveur, final String loginDistant, final String loginLocal )
    {
        super( serveur, loginDistant, loginLocal );
        rshCmd = RSH_FILE + " -l " + loginDistant + " " + serveur + " ";
    }

    /**
     * Retourne le resultat d'un commande Unix en rsh Attention : le buffer est limit� a 1024.
     * 
     * @param cmd : Commande unix
     * @param buff : Ecrit dans l'entree standard le contenu
     * @throws IOException : Retourne le buffer rcp ou bien une erreur d'execution
     * @return resultat unix
     */
    public int executecmd( final String cmd, final byte buff[] )
        throws IOException
    {
        lastReturnStream = null;
        lastErrorStream = null;

        Process rsh = null;
        int exitVal = DEFAULT_EXIT_VAL;

        // Stocke ce que l'on a envoyer
        final String cmdsend = rshCmd + cmd;
        addMessage( cmdsend + "\n" );

        try
        {
            rsh = Runtime.getRuntime().exec( cmdsend );

            // Si on a quelquechose dans le buffer
            if ( buff != null )
            {
                CopyUtils.copy( buff, rsh.getOutputStream() );
                addMessage( buff );
                rsh.getOutputStream().close();
            }

            exitVal = rsh.waitFor();

            if ( rsh.getInputStream() != null )
            {
                lastReturnStream = IOUtils.toString( rsh.getInputStream() );
                addMessage( lastReturnStream );
            }

            if ( rsh.getErrorStream() != null )
            {
                lastErrorStream = IOUtils.toString( rsh.getErrorStream() );

                if ( lastErrorStream.length() > 0 )
                {
                    addMessage( lastErrorStream );

                    return 1;
                }
            }
        }
        catch ( final IOException ioe )
        {
            addMessage( ioe.getMessage() );
            throw ioe;
        }
        catch ( final InterruptedException e )
        {
            addMessage( e.getMessage() );
            throw new IOException( e.getMessage() );
        }

        if ( exitVal == 0 )
        {
            addMessage( ">OK\n" );
        }

        return exitVal;
    }
}