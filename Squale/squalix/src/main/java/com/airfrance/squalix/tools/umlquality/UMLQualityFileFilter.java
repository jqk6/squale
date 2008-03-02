package com.airfrance.squalix.tools.umlquality;

import java.io.File;
import java.io.FileFilter;

/**
 * Filtre de nom des rapports(fichiers resultat) g�nerer par l'outil umlquality.
 * 
 * @author m400842 (by rose)
 * @version 1.0
 */
public class UMLQualityFileFilter
    implements FileFilter
{

    /** Extensions permises */
    private String[] mExtensions;

    /**
     * Constructeur
     * 
     * @param pExtensions extensions admises
     */
    UMLQualityFileFilter( String[] pExtensions )
    {

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
            // On v�rifie si l'extension est accept�e
            for ( int i = 0; i < mExtensions.length && !result; i++ )
            {
                if ( filename.endsWith( mExtensions[i] ) )
                {
                    result = true;
                }
            }
        }
        return result;
    }

}
