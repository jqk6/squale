package com.airfrance.squalix.util.file;

import java.io.File;
import java.io.FileFilter;

/**
 * Filtre de nom utilis� pour filtrer les fichiers selon leur extension.
 */
public class ExtensionFileFilter
    implements FileFilter
{

    /** L'extension � filter */
    private String mExtension = "";

    /**
     * Constructeur
     * 
     * @param pExtension l'extension � utiliser
     */
    public ExtensionFileFilter( String pExtension )
    {
        mExtension = pExtension;
    }

    /**
     * @return l'extension
     */
    public String getExtension()
    {
        return mExtension;
    }

    /**
     * Modifie l'extension
     * 
     * @param pExtension l'extension
     */
    public void setExtension( String pExtension )
    {
        mExtension = pExtension;
    }

    /**
     * @param pFile le nom du fichier
     * @return vrai si le fichier doit �tre list�, faux sinon.
     */
    public boolean accept( File pFile )
    {
        boolean result = false;
        if ( pFile.isFile() )
        {
            // Si c'est un fichier
            // On r�cup�re le nom du fichier
            String fileName = pFile.getName();
            // On v�rifie qu'il s'agit bien d'un fichier jar
            result = fileName.endsWith( mExtension );
        }
        else if ( pFile.isDirectory() )
        {
            result = true;
        }
        return result;
    }

}
