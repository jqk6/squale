package com.airfrance.squalix.util.file;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.tools.ant.taskdefs.optional.jsp.JspNameMangler;

import com.airfrance.squalix.tools.compiling.jsp.configuration.JspCompilingConfiguration;

/**
 * Utilitaire pour la manipulation des fichiers JSP
 */
public class JspFileUtility {
    
    /** Extension des pages JSP */
    public static final String JSP_EXTENSION = ".jsp";

    /**
     * Dans le cas des compilation des jsps, un nom de package ou classe interdit en java
     * mais autoris� pour les JSPs est renomm�.<br/>
     * Par exemple <b>package</p> sera renomm� en <b>package%</b>
     * lors de la compilation des .jsp en .java.<br/>
     * Ensuite les noms de contenant des caract�res sp�ciaux sont renomm�s.<br/>
     * Par exemple notre <b>package%</b> sera renomm� en <b>package_0025</b>
     * @param pJspPaths les r�pertoires contenant les jsps
     * @param pFullName le nom absolue d'une classe jsp compil�e
     * @throws IOException si erreur
     * @return le nom absolu du fichier correpondant � la JSP
     */
    public static String getAbsoluteJspFileName(List pJspPaths, String pFullName) throws IOException {
        String fileName = null;
        boolean search = true;
        // On va examiner tout les noms de package ainsi que le nom
        // de la classe
        String[] decomposed = pFullName.split("\\.");
        String covertedDirName = "";
        // Le premier package correspons au package par d�faut des JPSs donn� lors de la persistance
        // du composant (ie. {jsp}) donc on l'ignore.
        // Le deuxi�me package indique l'index du r�pertoire source dans la liste donn�e en param�tre
        // ex : jsp -> index 0; jsp1 -> index1 
        // On commence donc au package 3 pour la recherche du fichier
        for (int i = 2; i < decomposed.length-1; i++) {
            covertedDirName += decomposed[i] + "/";
        }
        covertedDirName += decomposed[decomposed.length-1] + JSP_EXTENSION;
        String firstPackage = pFullName.substring(0, pFullName.indexOf("."));
        File directory = new File((String)pJspPaths.get(getJspDirectoryId(pJspPaths, decomposed[1])));
        Set files = new HashSet();
        files = FileUtility.createRecursiveListOfFiles(directory, new ConvertFileNameFilter(directory, covertedDirName), files);
        if (files.size() > 0) {
            fileName = (String)files.iterator().next();
        }
        return fileName;
    }

    /**
     * @param pJspPaths la liste des noms des chemins vers les jsps
     * @param pJspDir le nom du r�pertoire des jsps � trouver
     * @return l'index du r�pertoire correspondant
     */
    public static int getJspDirectoryId(List pJspPaths, String pJspDir) {
        // On r�cup�re l'index du r�pertoire pJspDir
        int index = 0;
        try {
            // On tente de r�cup�rer l'index du package
            index  = Integer.parseInt(pJspDir.replaceFirst(JspCompilingConfiguration.FIRST_PACKAGE, ""));
        } catch (NumberFormatException nbfe) {
            // Si erreur c'est qu'il n'y a pas de chiffre � la fin du nom --> index 0
            index = 0;
        }
        return index;
    }

    /**
    * Converti un nom de classe ou package avec l'algorithme
    * utilis� par JspC. 
    * Code copi� de JspNameMangler.java de ant
    *
    * @param pName le nom � convertir
    * @return le nom converti
    */
    public static String convertWithJspNameMangler(String pName) {
        // since we don't mangle extensions like the servlet does,
        // we need to check for keywords as class names
        for (int i = 0; i < JspNameMangler.keywords.length && !pName.endsWith("%"); ++i) {
            if (pName.equals(JspNameMangler.keywords[i])) {
                pName += "%";
            }
        }

        // Fix for invalid characters. If you think of more add to the list.
        StringBuffer modifiedClassName = new StringBuffer(pName.length());
        // first char is more restrictive than the rest
        char firstChar = pName.charAt(0);
        if (Character.isJavaIdentifierStart(firstChar)) {
            modifiedClassName.append(firstChar);
        } else {
            modifiedClassName.append(mangleChar(firstChar));
        }
        // this is the rest
        for (int i = 1; i < pName.length(); i++) {
            char subChar = pName.charAt(i);
            if (Character.isJavaIdentifierPart(subChar)) {
                modifiedClassName.append(subChar);
            } else {
                modifiedClassName.append(mangleChar(subChar));
            }
        }
        return modifiedClassName.toString();
    }

    /**
     * Algorithme de conversion de JspC.
     * Code copi� de JspNameMangler.java de ant.
     *
     * @param ch char to mangle
     * @return mangled string; 5 digit hex value
     */
    private static final String mangleChar(char ch) {
        // La conversion du caract�re sp�cial sera de 6 caract�res
        final int nbChar = 6;
        if (ch == File.separatorChar) {
            ch = '/';
        }
        // Il converti en hexad�cimal pr�c�d� de "_"
        // et d'autant de z�ro que n�cessaire pour compl�ter les 6
        // caract�res
        String s = Integer.toHexString(ch);
        int nzeros = (nbChar - 1) - s.length();
        char[] result = new char[nbChar];
        result[0] = '_';
        for (int i = 1; i <= nzeros; ++i) {
            result[i] = '0';
        }
        int resultIndex = 0;
        for (int i = nzeros + 1; i < nbChar; ++i) {
            result[i] = s.charAt(resultIndex++);
        }
        return new String(result);
    }

    /**
     * Filtre sur les r�pertoires des JSPs pour ne trouver que le
     * fichier correspondant au nom converti
     */
    static class ConvertFileNameFilter implements FileFilter {
        
        /** Chemin racine � ne pas convertir */
        private File mRoot;

        /** chemin de la jsp �ventuellement converti */
        private String mPath;

        /**
         * Constructeur
         * @param pRoot le chemin racine de la jsp
         * @param pPath le nom du fichier �ventuellement converti par JspC
         */
        public ConvertFileNameFilter(File pRoot, String pPath) {
            mRoot = pRoot;
            mPath = pPath;
        }

        /** 
         * {@inheritDoc}
         * @see java.io.FileFilter#accept(java.io.File)
         */
        public boolean accept(File pathname) {
            boolean result = false;
            if(pathname.isDirectory()) {
                result = true;
            } else if (pathname.isFile() && pathname.getName().endsWith(JSP_EXTENSION)) {
                // si il s'agit d'une jsp, on v�rifie qu'elle ne correspond pas
                // � la jsp convertie
                String convertedPath = convertPath(pathname);
                if (convertedPath.endsWith(mPath)) {
                    result = true;
                }
            }
            return result;
        }

        /**
         * @param pPathname le nom du fichier � convertir
         * @return le nom converti
         */
        private String convertPath(File pPathname) {
            String result = "";
            try {
                // On remplace les s�parateur de fichier pour avoir une coh�rence dans les comparaisons
                String pathname = pPathname.getCanonicalPath().replaceAll("\\\\", "/");
                String rootPathname = mRoot.getCanonicalPath().replaceAll("\\\\", "/");
                // On r�cup�re le chemin relatif par rapport au r�pertoire racine de la jsp
                String relativePath = pathname.replaceFirst(rootPathname, "");
                // On supprime le "/" au d�but et l'extension ".jsp" car il ne faut pas les convertir
                relativePath = relativePath.substring(1, relativePath.length()-JSP_EXTENSION.length());
                // On parcours l'ensemble du chemin pour convertir tous les r�pertoires
                String[] paths = relativePath.split("/");
                for(int i=0; i<paths.length-1; i++) {
                    result += convertWithJspNameMangler(paths[i]) + "/";
                }
                // On convertit le nom de la jsp et on lui rajoute son extension supprim�e plus haut
                result += convertWithJspNameMangler(paths[paths.length-1]) + JSP_EXTENSION;
            } catch (IOException e) {
                // Si une erreur survient, le fichier n'est pas correcte
                result = "";
            }
            return result;
        }

    }
}
