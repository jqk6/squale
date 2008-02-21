package com.airfrance.squalix.tools.jdepend;

import java.io.File;
import java.io.FileFilter;

/**
 * Filtre de nom utilis� pour filtrer les fichiers � analyser.
 */
public class JDependFilter implements FileFilter {

    
    /**
     * @param pFile le nom du fichier
     * @return vrai si le fichier est un r�pertoire, pour construire les packages
     * seul les r�pertoires nous int�ressent
     * @roseuid 42D3CF0C0296
     */
    public boolean accept(File pFile) {
        return pFile.isDirectory();
    }
}
