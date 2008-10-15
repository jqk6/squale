package com.airfrance.squalix.util.parser;

import java.util.List;

import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ClassBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.MethodBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;

/**
 * Parse les noms de type Cobol et les remplace par les objets correspondants.
 */

public class CobolParser
{

    /**
     * Nom donn� par McCabe au module principal d'un programme.
     */
    private final String MAINLINE_TOKEN = "mainline";

    /**
     * S�parateur de programme et module.
     */
    private final String PRG_MODULE_SEPARATOR = "`";

    /**
     * S�parateur de programme et de son module principal.
     */
    private final String PRG_MAIN_MODULE_SEPARATOR = "_";

    /**
     * S�parateur de module et de paragraphe.
     */
    private final String MODULE_PARAGRAPH_SEPARATOR = ":";

    /**
     * Nom du module cr�� dans le cas d'un programme sans module.
     */
    private final String MAINPROGRAM_MODULE = "mainprogram";

    /** Projet sur lequel est r�alis� l'analyse */
    private ProjectBO mProject;

    /**
     * Retourne le nom du projet sur lequel est r�alis� l'analyse.
     * 
     * @return le nom du projet
     */
    public ProjectBO getProject()
    {
        return mProject;
    }

    /**
     * Constructeur.
     * 
     * @param pProject le projet sur lequel est r�alis� l'analyse
     */
    public CobolParser( final ProjectBO pProject )
    {
        mProject = pProject;
    }

    /**
     * Retourne un objet m�tier module portant le nom indiqu�.
     * 
     * @param pModuleName le nom du module
     * @param pFilename le nom du fichier d�finissant le module
     * @param pPrgName le nom du programme d�finissant le module
     * @return l'objet m�tier module
     */
    public MethodBO getModule( final String pModuleName, final String pFilename, final ClassBO pPrgName )
    {
        MethodBO lMethodBO = new MethodBO( pModuleName );
        lMethodBO.setLongFileName( pFilename );
        lMethodBO.setParent( pPrgName );
        lMethodBO.setProject( mProject );
        return lMethodBO;
    }

    /**
     * Retourne un objet m�tier programme portant le nom indiqu�.
     * 
     * @param pPrgName le nom du programme
     * @param pFilename le nom du fichier d�finissant le programme
     * @return l'objet m�tier programme
     */
    public ClassBO getProgram( final String pPrgName, final String pFilename )
    {
        ClassBO lClassBO = new ClassBO( pPrgName );
        lClassBO.setParent( mProject );
        lClassBO.setFileName( pFilename );
        return lClassBO;
    }

    /**
     * Retourne les noms du programme et du module Cobol � partir du nom complet du module tel qu'indiqu� dans le
     * rapport McCabe. <br/> Le nom du programme et du module doivent �tre s�par�s par un simple guillemet.
     * 
     * @param pFullModuleName le nom complet du module
     * @param pPrgAndModuleNames les noms du programme et du module Cobol retourn�s par cette m�thode si ceux-ci sont
     *            correctement s�par�s par un simple guillement. Cette liste doit �tre initiallement vide, et elle est
     *            retourn�e vide si le nom du module n'est pas bien form�.
     */
    public void getPrgAndModuleNamesForModule( final String pFullModuleName, final List<String> pPrgAndModuleNames )
    {
        // initialisation de la liste
        pPrgAndModuleNames.clear();

        // cas d'un module non principal
        int lIndexOfSeparator = pFullModuleName.indexOf( PRG_MODULE_SEPARATOR );
        if ( lIndexOfSeparator == -1 )
        {
            // cas du module principal du programme
            if ( pFullModuleName.contains( PRG_MAIN_MODULE_SEPARATOR + MAINLINE_TOKEN ) )
            {
                lIndexOfSeparator = pFullModuleName.indexOf( PRG_MAIN_MODULE_SEPARATOR );
            }
        }
        // d�composition du nom complet du module
        // cas d'un programme et d'un module
        if ( lIndexOfSeparator != -1 )
        {
            // ajout du nom du programme et du module dans la liste
            pPrgAndModuleNames.add( pFullModuleName.substring( 0, lIndexOfSeparator ) );
            pPrgAndModuleNames.add( pFullModuleName.substring( lIndexOfSeparator + 1 ) );
        }
        // cas d'un programme sans nom de module
        else
        {
            pPrgAndModuleNames.add( pFullModuleName );
            pPrgAndModuleNames.add( MAINPROGRAM_MODULE );
        }
    }

}
