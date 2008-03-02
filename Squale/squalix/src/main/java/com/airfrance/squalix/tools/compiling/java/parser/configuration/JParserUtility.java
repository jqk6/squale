/*
 * Cr�� le 27 juil. 05, par M400832.
 */
package com.airfrance.squalix.tools.compiling.java.parser.configuration;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.airfrance.squalix.configurationmanager.ConfigUtility;
import com.airfrance.squalix.tools.compiling.CompilingMessages;

/**
 * Cette classe utilise les m�thodes de la classe <code>
 * com.airfrance.squalix.configurationmanager.ConfigUtility</code>.
 * <br/> La seule modification apport�e se situe dans la m�thode <code>
 * getRootNode(String, String)</code>. La m�thode
 * <code>
 * db.parse()</code> ne prend plus un <code>InputStream</code> issu d'un <code>RessourceBundle</code> en
 * param�tre, mais une <code>String</code> qui est un chemin absolu.
 * 
 * @see com.airfrance.squalix.configurationmanager.ConfigUtility
 * @author m400832
 * @version 1.0
 */
public class JParserUtility
{

    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactory.getLog( JParserUtility.class );

    /**
     * Retourne l'�l�ment racine du document XML de configuration.
     * 
     * @param pFile Fichier de configuration
     * @param pName Nom de la racine.
     * @return Le noeud racine.
     * @throws Exception si un probl�me appara�t.
     */
    public static Node getRootNode( final String pFile, final String pName )
        throws Exception
    {
        LOGGER.debug( CompilingMessages.getString( "logs.configuration.getRootNode" ) );
        /* cr�ation du Document */
        DocumentBuilderFactory dbc = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbc.newDocumentBuilder();
        File f = new File( pFile );
        Document doc = db.parse( f );

        /* R�cup�ration de la liste des noeuds */
        NodeList nl = doc.getChildNodes();

        /* instanciation des noeuds */
        Node node = null;
        Node root = null;

        /*
         * tant qu'il y aura des noeuds ds la liste, et que l'on a pas trouv� le noeud racine d�sir�
         */
        for ( int i = 0; i < nl.getLength() && null == root; i++ )
        {
            /* r�cup�ration du i-�me noeud */
            node = nl.item( i );
            /* si l'on trouve le noeud recherch� */
            if ( node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equalsIgnoreCase( pName ) )
            {
                /* alors on le stocke */
                root = node;
            }
        }
        return root;
    }

    /**
     * Retourne un �l�ment directement enfant avec le nom d�sir�, ou null si celui-ci n'existe pas.
     * 
     * @param pParent Le noeud parent.
     * @param pName Le nom de l'�l�ment recherch�, insensible � la casse.
     * @return l'�l�ment trouv� ou null si non trouv�.
     * @see ConfigUtility#getNodeByTagName(Node, String)
     */
    public static Node getNodeByTagName( Node pParent, String pName )
    {
        return ConfigUtility.getNodeByTagName( pParent, pName );
    }
}
