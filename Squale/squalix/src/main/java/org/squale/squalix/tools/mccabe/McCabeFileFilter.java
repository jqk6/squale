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
package org.squale.squalix.tools.mccabe;

import java.io.File;
import java.io.FileFilter;

/**
 * Filtre de nom utilis� pour filtrer les fichiers � analyser.
 * 
 * @author m400842 (by rose)
 * @version 1.0
 */
class McCabeFileFilter
    implements FileFilter
{
    /** R�pertoire racine */
    private String mRoot;

    /** Extensions permises */
    private String[] mExtensions;

    /**
     * Constructeur
     * 
     * @param pRoot r�pertoire racine
     * @param pExtensions extensions admises
     */
    McCabeFileFilter( String pRoot, String[] pExtensions )
    {
        mRoot = pRoot;
        mExtensions = pExtensions;
    }

    /**
     * @param pFile le nom du fichier
     * @return vrai si le fichier doit �tre list�, faux sinon.
     * @roseuid 42D3CF0C0296
     */
    public boolean accept( File pFile )
    {
        boolean result = false;
        if ( pFile.isFile() )
        {
            // Si c'est un fichier
            // On r�cup�re le nom du fichier
            String filename = pFile.getName();
            // On r�cup�re la liste des extensions
            String fileExtension = null;
            // On r�cup�re l'extension
            int extIndex = filename.lastIndexOf( "." );
            if ( extIndex != -1 )
            {
                fileExtension = filename.substring( extIndex + 1 );
            }
            else
            {
                fileExtension = "";
            }
            // On v�rifie si l'extension est accept�e
            for ( int i = 0; i < mExtensions.length && !result; i++ )
            {
                if ( mExtensions[i].equalsIgnoreCase( fileExtension ) )
                {
                    result = true;
                }
            }
        }
        else if ( pFile.isDirectory() )
        {
            result = true;
        }
        return result;
    }
}