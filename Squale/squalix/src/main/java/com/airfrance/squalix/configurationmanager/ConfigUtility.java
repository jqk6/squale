//Source file: D:\\cc_views\\squale_v0_0_act\\squale\\src\\squalix\\src\\com\\airfrance\\squalix\\configurationmanager\\ConfigUtility.java

package com.airfrance.squalix.configurationmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.airfrance.squalix.messages.Messages;

/**
 * Cette classe fournit des m�thodes "utilitaires" utilisables par les t�ches pour manipuler des fichiers XML de
 * configuration de mani�re standard.
 * 
 * @author m400842 (by rose)
 * @version 1.0
 */
public class ConfigUtility
{

    /**
     * Logger
     */
    private static final Log LOGGER = LogFactory.getLog( ConfigUtility.class );

    /**
     * Retourne l'�l�ment racine du document XML de configuration.
     * 
     * @param pFile Fichier de configuration
     * @param pName Nom de la racine.
     * @return Le noeud racine.
     * @throws Exception si un probl�me appara�t.
     * @roseuid 42C925800210
     */
    public static Node getRootNode( final String pFile, final String pName )
        throws Exception
    {
        LOGGER.debug( Messages.getString( "log.configuration.getRootNode" ) );
        DocumentBuilderFactory dbc = DocumentBuilderFactory.newInstance();
        Node root = null;
        DocumentBuilder db = dbc.newDocumentBuilder();
        // On va utiliser de pr�f�rence un InputStream issu du classpath,
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream( pFile );
        if ( null == is )
        {
            // mais si celui-ci n'exista pas, alors on le r�cup�re du fichier pass� en param�tre
            is = new FileInputStream( pFile );
        }
        Document doc = db.parse( is );
        NodeList nl = doc.getChildNodes();
        // A partir de la liste des noeuds enfants du document, on va rechercher
        // celui qui poss�de le nom attendu
        Node node = null;
        for ( int i = 0; i < nl.getLength() && null == root; i++ )
        {
            node = nl.item( i );
            if ( node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equalsIgnoreCase( pName ) )
            {
                // Le noeud doit �tre un �l�ment, et son nom doit correspondre � celui recherch�
                root = node;
            }
        }
        return root;
    }

    /**
     * Filtre une liste de noeuds DOM, en ne gardant que les noeuds du type pass� en param�tre. Le type est celui
     * d�finit par la classe <code>Node</code>.
     * 
     * @param pList liste de noeud � filtrer.
     * @param pType type des �l�ments � garder.
     * @return la liste filtr�e.
     * @roseuid 42C92580021F
     */
    public static Collection filterList( NodeList pList, short pType )
    {
        LOGGER.debug( Messages.getString( "log.configuration.filterList" ) );
        Collection coll = new ArrayList();
        Node node = null;
        for ( int i = 0; i < pList.getLength(); i++ )
        {
            node = pList.item( i );
            if ( node.getNodeType() == pType )
            {
                coll.add( node );
            }
        }
        return coll;
    }

    /**
     * V�rifie que le fichier sp�cifi� existe et est lisible.
     * 
     * @param pFile le fichier � v�rifier.
     * @return la validit� du fichier.
     * @roseuid 42C9258001E1
     */
    public static boolean checkFile( String pFile )
    {
        LOGGER.debug( Messages.getString( "log.configuration.checkFile" ) + pFile );
        File f = new File( pFile );
        return ( f.exists() && f.isFile() && f.canRead() );
    }

    /**
     * Retourne un �l�ment directement enfant avec le nom d�sir�, ou null si celui-ci n'existe pas.<br />
     * Si plusieurs �l�ments enfants portent le m�me nom, seul le premier est renvoy�.
     * 
     * @param pParent Le noeud parent.
     * @param pName Le nom de l'�l�ment recherch�, insensible � la casse.
     * @return l'�l�ment trouv� ou null si non trouv�.
     * @roseuid 42C92580024E
     */
    public static Node getNodeByTagName( Node pParent, String pName )
    {
        LOGGER.debug( Messages.getString( "log.configuration.getNodeByTagName" ) );
        Collection coll = filterList( pParent.getChildNodes(), Node.ELEMENT_NODE );
        Iterator it = coll.iterator();
        Node node = null;
        Node result;
        while ( it.hasNext() && null == node )
        {
            result = (Node) it.next();
            if ( result.getNodeName().equalsIgnoreCase( pName ) )
            {
                node = result;
            }
        }
        return node;
    }

    /**
     * Retourne la valeur de l'attribut pName de l'�l�ment pParent, ou null s'il n'est pas trouv� ou si le parent n'est
     * pas un �l�ment valide.
     * 
     * @param pParent El�ment parent de l'attribut.
     * @param pName Nom de l'attribut, insensible � la casse.
     * @return la valeur de l'attribut
     * @roseuid 42C92580028D
     */
    public static String getAttributeValueByName( Node pParent, String pName )
    {
        LOGGER.debug( Messages.getString( "log.configuration.getAttributeValueByName" ) );
        String value = null;
        if ( pParent.getNodeType() == Node.ELEMENT_NODE )
        {
            Node node = pParent.getAttributes().getNamedItem( pName );
            if ( null != node )
            {
                value = node.getNodeValue();
            }
        }
        return value;
    }
}
