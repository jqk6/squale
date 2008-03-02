/*
 * Cr�� le 26 sept. 05, par M400832.
 */
package com.airfrance.squalix.tools.compiling.utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Classe "utilitaire" requise pour les tests de compilation.
 * 
 * @author M400832
 * @version 1.0
 */
public class FileManager
{

    /**
     * Constante ".".
     */
    private static final String DOT = ".";

    /**
     * Saut de ligne;
     */
    private static final String NEW_LINE = "\n";

    /**
     * Cette m�thode retourne une liste de tous les fichiers se terminant par une extension donn�e, au sein d'une
     * arborescence donn�e.
     * 
     * @param pDir arborescence � parcourir.
     * @param pExt extension � "matcher".
     * @return la liste des fichiers se terminant par l'extension demand�e dans l'arborescence demand�e.
     */
    public static ArrayList checkFileNumber( final String pDir, final String pExt )
    {
        /* liste contenant les fichiers correspondant � l'extension cherch�e */
        ArrayList list = new ArrayList();

        /* On v�rifie que le r�pertoire existe */
        File dir = new File( pDir );
        if ( dir.isDirectory() )
        {

            /* On r�cup�re la liste des fichiers / dossiers */
            File foundFiles[] = dir.listFiles();

            /* variable utilis�e pour stocker l'extension du fichier */
            String fileExt;
            int i = 0;

            /* tant qu'il y a ds fichiers / dossiers */
            while ( i < foundFiles.length )
            {
                /* dossier */
                if ( foundFiles[i].isDirectory() )
                {
                    /* r�cursion : on ajoute la liste r�cup�r�e */
                    list.addAll( FileManager.checkFileNumber( foundFiles[i].getAbsolutePath(), pExt ) );

                    /* fichier */
                }
                else
                {
                    /* nom du fichier */
                    fileExt = foundFiles[i].getName();
                    /* si le fichier contient au moins un "." */
                    if ( fileExt.lastIndexOf( DOT ) > 0 )
                    {
                        /*
                         * on r�cup�re la cha�ne entre le dernier "." et la fin du nom du fichier
                         */
                        fileExt = fileExt.substring( fileExt.lastIndexOf( DOT ), fileExt.length() );
                    }

                    /* si c'est un .i */
                    if ( pExt.equals( fileExt ) )
                    {
                        /* on ajoute son chemin � la liste */
                        list.add( foundFiles[i].getAbsolutePath() );
                    }
                }
                /* on progresse dans la liste des fichiers */
                i++;
            }
        }
        return list;
    }

    /**
     * Cette m�thode supprime les fichiers dont les chemins sont stock�s dans la liste pass�e en param�tre.
     * 
     * @param pList liste de chemins de fichiers � supprimer.
     */
    public static void removeFiles( final ArrayList pList )
    {
        File iFile;
        Iterator it = pList.iterator();

        /* tant qu'il y a des fichiers dans la liste */
        while ( null != it && it.hasNext() )
        {
            /*
             * on construit un nouveau descripteur et on supprime le fichier.
             */
            iFile = new File( (String) ( it.next() ) );
            iFile.delete();
        }

        iFile = null;
    }

    /**
     * Cette m�thode copie le fichier d'un emplacement � un autre.
     * 
     * @param pSource r�pertoire source.
     * @param pTarget r�pertoire de destination.
     * @throws Exception IOException.
     */
    public static void copyFile( final File pSource, final File pTarget )
        throws Exception
    {

        StringBuffer sbw = new StringBuffer();

        /* cr�ation d'un buffer de lecture du fichier */
        FileReader fr = new FileReader( pSource );
        BufferedReader br = new BufferedReader( fr );

        String myLine = "";
        while ( null != ( myLine = br.readLine() ) )
        {
            sbw.append( myLine );
            sbw.append( NEW_LINE );
        }

        /* filewriter : on �crit le buffer dans le fichier */
        FileWriter fw = new FileWriter( pTarget );
        BufferedWriter bw = new BufferedWriter( fw );
        bw.write( sbw.toString() );

        /* on ferme les flux */
        bw.close();
        br.close();

        /* m�nage */
        bw = null;
        br = null;
        fw = null;
        fr = null;
        sbw = null;
    }
}
