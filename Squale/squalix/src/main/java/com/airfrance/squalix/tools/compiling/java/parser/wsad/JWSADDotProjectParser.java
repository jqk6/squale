package com.airfrance.squalix.tools.compiling.java.parser.wsad;

import java.io.File;

import org.w3c.dom.Node;

import com.airfrance.squalix.configurationmanager.ConfigUtility;

/**
 */
public class JWSADDotProjectParser
{

    /** le fichier .project du projet */
    private File dotProject;

    /** une constante servant en cas d'�chec */
    private static final String UNKNOWN = "Fichier .project introuvable";

    /**
     * Constructeur
     * 
     * @param dotProjectPath le chemin du .project du projet courant
     */
    public JWSADDotProjectParser( String dotProjectPath )
    {
        // on ajoute �ventuellement un "/" ) la fin
        // si il n'est pas d�j� pr�sent
        if ( !dotProjectPath.endsWith( "/" ) )
        {
            dotProject = new File( dotProjectPath + "/.project" );
        }
        else
        {
            dotProject = new File( dotProjectPath + ".project" );
        }
    }

    /**
     * parse le fichier pour r�cup�rer le nom du projet
     * 
     * @return le nom du projet
     */
    public String retrieveName()
    {
        String result = UNKNOWN;
        if ( dotProject.exists() )
        {
            // on parse le fichier
            /* on r�cup�re le noeud racine */
            Node root;
            try
            {
                root = ConfigUtility.getRootNode( dotProject.getPath(), "projectDescription" );
                Node name = ConfigUtility.getNodeByTagName( root, "name" );
                // Il ne doit y avoir qu'un seul enfant � ce noeud
                if ( Node.TEXT_NODE == name.getFirstChild().getNodeType() )
                {
                    result = name.getFirstChild().getNodeValue();
                }
            }
            catch ( Exception e )
            {
                e.printStackTrace();
            }

        }
        return result;
    }

}
