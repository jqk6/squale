package com.airfrance.squalix.util.file;

import java.io.File;
import java.io.FileFilter;
import java.util.List;

/**
 * Filtre de recherche bas� sur une extension et des r�pertoires exclus
 */
public class ExtensionsIncludedFileFilter implements FileFilter {
    /** Extensions autoris�es */
    private String[] mExtensions;

    /** Fichiers autoris�s sous la forme de cha�ne */
    private List mIncludedDirs;

    /** Le r�pertoire de recherche */
    private File mBaseDir;

    /**
     * @param pExtensions extension
     * @param pIncludedDirs r�pertoires exclus
     */
    public ExtensionsIncludedFileFilter(String[] pExtensions, List pIncludedDirs) {
        mExtensions = new String[pExtensions.length];
        for (int i = 0; i < pExtensions.length; i++) {
            mExtensions[i] = pExtensions[i].toLowerCase();
        }
        mIncludedDirs = pIncludedDirs;
    }

    /** 
     * {@inheritDoc}
     * @see java.io.FileFilter#accept(java.io.File)
     */
    public boolean accept(File pFile) {
        boolean result = true;
        // Si il s'agit d'un fichier, on teste son extension
        if (pFile.isFile()) {
            result = checkExtension(pFile);
        }
        if (result) {
            String regexp = mBaseDir.getAbsolutePath().replace('/', File.separatorChar).replace('\\', File.separatorChar);
            regexp = regexp.replaceAll("\\\\", "\\\\\\\\");
            regexp += "[\\\\/]*";
            String filePath = pFile.getAbsolutePath().replaceFirst(regexp, "");
            // On v�rifie que le nom de chemin n'est pas exclu
            result = mIncludedDirs.contains(filePath);
        }
        return result;
    }

    /**
     * Test de l'extension d'un fichier
     * @param pFile fichier � tester
     * @return true si le fichier satisfait aux crit�res d'extension
     */
    protected boolean checkExtension(File pFile) {
        boolean result = false;
        String fileName = pFile.getName();
        int index = fileName.lastIndexOf('.');
        if (index != -1) {
            String extension = fileName.substring(index).toLowerCase();
            // Parcours de chaque extension
            for (int i = 0;(i < mExtensions.length) && (!result); i++) {
                result = extension.equals(mExtensions[i]);
            }
        }
        return result;
    }

    /**
     * @param pDir le r�pertoire de recherche
     */
    public void setBaseDir(File pDir) {
        mBaseDir = pDir;
    }

}
