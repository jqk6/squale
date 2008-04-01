//Source file: D:\\cc_views\\squale_v0_0_act\\squale\\src\\squalix\\src\\com\\airfrance\\squalix\\tools\\compiling\\java\\JavaCompilingConfiguration.java

package com.airfrance.squalix.tools.compiling.java.configuration;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.airfrance.squalix.configurationmanager.ConfigUtility;
import com.airfrance.squalix.tools.compiling.CompilingMessages;

/**
 * Classe de configuration pour la compilation des projets JAVA.<br />
 * La version 2.0 introduit le recours � la r�flexion.
 * 
 * @author m400832
 * @version 2.0
 */
public class JCompilingConfiguration
{

    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactory.getLog( JCompilingConfiguration.class );

    /**
     * S�parateur UNIX.
     */
    public static final String UNIX_SEPARATOR = "/";

    /**
     * S�parateur Windows.
     */
    public static final String WINDOWS_SEPARATOR = "\\";

    /**
     * Version par d�faut du JDK � utiliser.
     */
    private String mJDKDefaultVersion = "";

    /**
     * R�pertoire cible pour stocker les classes compil�es.
     */
    private String mDestDir = "";

    /**
     * S�parateur du classpath.
     */
    private String mClasspathSeparator = "";

    /**
     * M�moire requise pour la compilation.
     */
    private String mRequiredMemory = "";

    /**
     * R�pertoire cible pour copier ou extraire le bundle eclipse.
     */
    private String mEclipseBundleDir = "";

    /**
     * R�pertoire cible pour extraire les libraries export�es dans le cas de la compilation RCP.
     */
    private File mExportedLibsDir = new File( "" );

    /**
     * Lib to add to the javac bootclasspath option depend on dialect. 1.3 -> pathtoJreLib1_3 1.4 -> pathToJreLib1_4 1.5 ->
     * pathToJreLib1_5
     */
    private HashMap mBootclasspaths = new HashMap();

    /**
     * HashMap utilis�e opur la r�flexion.
     */
    private HashMap mMap;

    /**
     * Constructeur.
     * 
     * @throws Exception exception lors de la configuration.
     * @see #createReflectionMap()
     * @see #getConfigurationFromXML(String)
     */
    public JCompilingConfiguration()
        throws Exception
    {
        createReflectionMap();
        getConfigurationFromXML( CompilingMessages.getString( "configuration.file" ) );
    }

    /**
     * Cette m�thode lance le parsing du fichier de configuration XML.
     * 
     * @param pFile chemin du fichier de configuration � parser.
     * @throws Exception exception en cas d'erreur de parsing.
     */
    private void getConfigurationFromXML( final String pFile )
        throws Exception
    {
        LOGGER.trace( CompilingMessages.getString( "logs.task.entering_method" ) );
        boolean isNull = false;

        /* on r�cup�re le noeud racine */
        Node root = ConfigUtility.getRootNode( pFile, CompilingMessages.getString( "configuration.root" ) );

        /* s'il n'est pas nul */
        if ( null != root )
        {
            /* on r�cup�re le noeud relatif la compilation JAVA */
            Node myNode = ConfigUtility.getNodeByTagName( root, CompilingMessages.getString( "configuration.java" ) );

            /* s'il n'est pas nul */
            if ( null != myNode )
            {
                /*
                 * on r�cup�re le noeud relatif � la configuration g�n�rale pour la compilation JAVA
                 */
                myNode =
                    ConfigUtility.getNodeByTagName( myNode, CompilingMessages.getString( "configuration.java.general" ) );

                /* r�cup�ration des s�parateurs */
                getSeparatorsFromXML( myNode );

                /* r�cup�ration des classpath des APIs Java */
                getBootclasspathsFromXML( myNode );

                /*
                 * cr�ation d'une liste des autres noeuds contenant les infos de config g�n�rale
                 */
                ArrayList nodes = new ArrayList();
                /* version par d�faut du JDK */
                nodes.add( CompilingMessages.getString( "configuration.java.general.default_jdk_version" ) );
                /* r�pertoire de stockage des classes compil�es */
                nodes.add( CompilingMessages.getString( "configuration.java.general.dest_dir" ) );
                /* m�moire requise pour la compilation */
                nodes.add( CompilingMessages.getString( "configuration.java.general.required_memory" ) );
                /* r�pertoire contenant le bundle eclipse de l'utilisateur */
                nodes.add( CompilingMessages.getString( "configuration.java.general.eclipse_bundle_path" ) );
                /* r�pertoire contenant les librairies export�es dans le cas de la compilation RCP */
                nodes.add( CompilingMessages.getString( "configuration.java.general.exported_libs_path" ) );

                /* r�cup�ration des autres noeuds de la config g�n�rale */
                getGeneralFromXML( myNode, nodes );
            }
            else
            {
                /* noeud rencontr� nul / n'existe pas */
                isNull = true;
            }
            myNode = null;

        }
        else
        {
            /* noeud rencontr� nul / n'existe pas */
            isNull = true;
        }

        /* si le noeud rencontr� est nul / n'existe pas */
        if ( isNull )
        {
            /* on lance l'exception en rapport */
            throw new Exception( CompilingMessages.getString( "exception.xml.node_not_found" ) );
        }

        root = null;
    }

    /**
     * Cette m�thode r�cup�re les valeurs de fichier de configuration relatives � la configuration g�n�rale de la t�che,
     * et les attribue par r�flexion.
     * 
     * @param pNode noeud racine.
     * @param pChildNodeNames noeud fils � parcourir.
     * @throws Exception exception lors du traitement.
     */
    private void getGeneralFromXML( final Node pNode, final ArrayList pChildNodeNames )
        throws Exception
    {
        LOGGER.trace( CompilingMessages.getString( "logs.task.entering_method" ) );

        /*
         * instanciation des variables pour la boucle qui va suivre.
         */
        Node myNode;
        String pChildNodeName;

        /* it�rateur sur les noms de noeuds */
        Iterator it = pChildNodeNames.iterator();

        /* si l'it�rateur n'est pas nul */
        if ( null != it )
        {
            /* tant qu'il a des �l�ments */
            while ( it.hasNext() )
            {
                /* nom du noeud fils courant */
                pChildNodeName = (String) it.next();

                /* noeud enfant */
                myNode = ConfigUtility.getNodeByTagName( pNode, pChildNodeName );

                /* si le noeud contient une valeur */
                if ( null != myNode && Node.ELEMENT_NODE == myNode.getNodeType() )
                {
                    mapKeyValue( pChildNodeName, myNode.getFirstChild().getNodeValue().trim() );

                    /* sinon */
                }
                else
                {
                    /* on lance l'exception en rapport */
                    throw new Exception( CompilingMessages.getString( "exception.xml.node_not_found" ) );
                }
            }
        }
        myNode = null;
    }

    /**
     * R�cup�re les classpaths vers les APIs Java
     * 
     * @param pNode le noeud
     * @throws Exception si erreur
     */
    private void getBootclasspathsFromXML( final Node pNode )
        throws Exception
    {

        /* noeud racine contenant les classpaths */
        Node myNode =
            ConfigUtility.getNodeByTagName( pNode,
                                            CompilingMessages.getString( "configuration.java.general.bootclasspaths" ) );

        boolean throwException = false;

        // not null and element type
        if ( null != myNode && Node.ELEMENT_NODE == myNode.getNodeType() )
        {
            // We get the first child node
            myNode =
                ConfigUtility.getNodeByTagName(
                                                myNode,
                                                CompilingMessages.getString( "configuration.java.general.bootclasspaths.bootclasspath" ) );

            // If node exists
            if ( null != myNode )
            {
                NamedNodeMap attrMap = null;
                String javaVersion = null;

                /* tant qu'il y a des noeuds */
                while ( null != myNode )
                {
                    if ( Node.ELEMENT_NODE == myNode.getNodeType() )
                    {
                        /* on r�cup�re les attributs du noeud */
                        attrMap = myNode.getAttributes();

                        /* attribut "version" */
                        javaVersion =
                            ( attrMap.getNamedItem( CompilingMessages.getString( "configuration.java.general.bootclasspaths.bootclasspath.version" ) ) ).getNodeValue().trim();

                        // We will get all children nodes
                        getBootClasspathLibsFromXML( myNode, javaVersion );
                    }
                    /* on it�re */
                    myNode = myNode.getNextSibling();
                }

                attrMap = null;
                javaVersion = null;

                /* erreur rencontr�e --> exception � lancer */
            }
            else
            {
                throwException = true;
            }
            /* erreur rencontr�e --> exception � lancer */
        }
        else
        {
            throwException = true;
        }

        myNode = null;

        /* erreur rencontr�e */
        if ( throwException )
        {
            /* exception lanc�e */
            throw new Exception( CompilingMessages.getString( "exception.xml.node_not_found" ) );
        }
    }

    /**
     * Add bootclasspath lib to the bootclasspath map with javaVersion key
     * 
     * @param pNode root node containing all lib tag definitions
     * @param javaVersion java dialect
     * @throws Exception if error
     */
    private void getBootClasspathLibsFromXML( Node pNode, String javaVersion )
        throws Exception
    {

        boolean throwException = false;
        Node node = pNode;

        // not null and element type
        if ( null != node && Node.ELEMENT_NODE == node.getNodeType() )
        {
            // We get the first child node
            node =
                ConfigUtility.getNodeByTagName(
                                                node,
                                                CompilingMessages.getString( "configuration.java.general.bootclasspaths.bootclasspath.lib" ) );

            // If node exists
            if ( null != node )
            {

                NamedNodeMap attrMap = null;
                String attrPath = null;

                /* While there are nodes */
                while ( null != node )
                {
                    if ( Node.ELEMENT_NODE == node.getNodeType() )
                    {
                        /* on r�cup�re les attributs du noeud */
                        attrMap = node.getAttributes();

                        /* "path" attribute */
                        attrPath =
                            ( attrMap.getNamedItem( CompilingMessages.getString( "configuration.java.general.bootclasspaths.bootclasspath.lib.path" ) ) ).getNodeValue().trim();

                        // We invoke add method
                        ( (Method) ( mMap.get( CompilingMessages.getString( "configuration.java.general.bootclasspaths.bootclasspath" ) ) ) ).invoke(
                                                                                                                                                      this,
                                                                                                                                                      new String[] {
                                                                                                                                                          javaVersion,
                                                                                                                                                          attrPath } );
                    }
                    /* on it�re */
                    node = node.getNextSibling();
                }

                attrMap = null;
                attrPath = null;

                /* have error --> launch exception */
            }
            else
            {
                throwException = true;
            }
            /* have erreor --> launch exception */
        }
        else
        {
            throwException = true;
        }

        node = null;

        /* have error */
        if ( throwException )
        {
            /* launched exception */
            throw new Exception( CompilingMessages.getString( "exception.xml.node_not_found" ) );
        }
    }

    /**
     * Cette m�thode r�cup�re le valeur des cl�s n�cessaires pour parser le fichier de classpath.
     * 
     * @param pNode noeud XML � parser.
     * @throws Exception exception en cas d'erreur lors du parsing.
     * @see #mapKeyValue(String, String)
     */
    private void getSeparatorsFromXML( final Node pNode )
        throws Exception
    {
        LOGGER.trace( CompilingMessages.getString( "logs.task.entering_method" ) );

        /* noeud racine contenant les s�parateurs */
        Node myNode =
            ConfigUtility.getNodeByTagName( pNode,
                                            CompilingMessages.getString( "configuration.java.general.separators" ) );

        boolean throwException = false;

        /* noeud non nul et de type ELEMENT */
        if ( null != myNode && Node.ELEMENT_NODE == myNode.getNodeType() )
        {
            /* on r�cup�re le 1er noeud fils */
            myNode =
                ConfigUtility.getNodeByTagName(
                                                myNode,
                                                CompilingMessages.getString( "configuration.java.general.separators.separator" ) );

            /* si ce noeud existe */
            if ( null != myNode )
            {
                NamedNodeMap attrMap = null;
                String attrValue = null, attrName = null;

                /* tant qu'il y a des noeuds */
                while ( null != myNode )
                {
                    if ( Node.ELEMENT_NODE == myNode.getNodeType() )
                    {
                        /* on r�cup�re les attributs du noeud */
                        attrMap = myNode.getAttributes();

                        /* attribut "cl�" */
                        attrName =
                            ( attrMap.getNamedItem( CompilingMessages.getString( "configuration.java.general.separators.separator.name" ) ) ).getNodeValue().trim();

                        /* attribut "valeur" */
                        attrValue =
                            ( attrMap.getNamedItem( CompilingMessages.getString( "configuration.java.general.separators.separator.value" ) ) ).getNodeValue().trim();

                        /* on mappe les cl�s / valeurs -> r�flexion. */
                        mapKeyValue( attrName, attrValue );
                    }
                    /* on it�re */
                    myNode = myNode.getNextSibling();
                }

                attrMap = null;
                attrName = null;
                attrValue = null;

                /* erreur rencontr�e --> exception � lancer */
            }
            else
            {
                throwException = true;
            }
            /* erreur rencontr�e --> exception � lancer */
        }
        else
        {
            throwException = true;
        }

        myNode = null;

        /* erreur rencontr�e */
        if ( throwException )
        {
            /* exception lanc�e */
            throw new Exception( CompilingMessages.getString( "exception.xml.node_not_found" ) );
        }
    }

    /**
     * Cette m�thode assure la r�flexion.
     * 
     * @param pKey cl� dans la hashMap.
     * @param pValue valeur.
     * @throws Exception exception de r�flexion.
     */
    private void mapKeyValue( final String pKey, final String pValue )
        throws Exception
    {

        /*
         * on invoque le setter correspondant � la cl� attrName, en lui passant la valeur attrValue
         */
        Object[] obj = { pValue };
        ( (Method) ( mMap.get( pKey ) ) ).invoke( this, obj );
    }

    /**
     * Cette m�thode cr�e une map contenant des cl�s associ�es � des m�thodes de type setter. <br />
     * En proc�dant ainsi, on pourra facilement affecter une valeur � une variable par r�flexion.
     * 
     * @throws Exception exception de r�flexion.
     * @see #mapKeyValue(String, String)
     */
    private void createReflectionMap()
        throws Exception
    {
        LOGGER.trace( CompilingMessages.getString( "logs.task.entering_method" ) );

        /*
         * tableau contenant la classe du param�tre � passer � chaque setter. ici, java.lang.String.
         */
        Class[] param = { String.class };

        mMap = new HashMap();

        /* Version par d�faut du JDK � utiliser. */
        mMap.put( CompilingMessages.getString( "configuration.java.general.default_jdk_version" ),
                  this.getClass().getMethod( "setJDKDefaultVersion", param ) );

        /* R�pertoire cible pour stocker les classes compil�es. */
        mMap.put( CompilingMessages.getString( "configuration.java.general.dest_dir" ),
                  this.getClass().getMethod( "setDestDir", param ) );

        /* M�moire requise pour la compilation. */
        mMap.put( CompilingMessages.getString( "configuration.java.general.required_memory" ),
                  this.getClass().getMethod( "setRequiredMemory", param ) );

        /* S�parateur du classpath. */
        mMap.put( CompilingMessages.getString( "configuration.java.general.separators.separator.name.classpath" ),
                  this.getClass().getMethod( "setClasspathSeparator", param ) );

        /* R�pertoire cible du bundle eclipse. */
        mMap.put( CompilingMessages.getString( "configuration.java.general.eclipse_bundle_path" ),
                  this.getClass().getMethod( "setEclipseBundleDir", param ) );

        /* R�pertoire cible des librairies export�es qu'il faut d�zipp�es pour la compilation RCP. */
        mMap.put( CompilingMessages.getString( "configuration.java.general.exported_libs_path" ),
                  this.getClass().getMethod( "setExportedLibsDir", param ) );

        /* Les chemins vers les classpath des APIs java */
        mMap.put( CompilingMessages.getString( "configuration.java.general.bootclasspaths.bootclasspath" ),
                  this.getClass().getMethod( "addBootclasspath", new Class[] { String.class, String.class } ) );
    }

    /**
     * Getter.
     * 
     * @return Version par d�faut du JDK � utiliser.
     */
    public String getJDKDefaultVersion()
    {
        return mJDKDefaultVersion;
    }

    /**
     * Setter.
     * 
     * @param pJDKDefaultVersion Version par d�faut du JDK � utiliser.
     */
    public void setJDKDefaultVersion( String pJDKDefaultVersion )
    {
        mJDKDefaultVersion = pJDKDefaultVersion;
    }

    /**
     * Getter.
     * 
     * @return le s�parateur du classpath.
     */
    public String getClasspathSeparator()
    {
        return mClasspathSeparator;
    }

    /**
     * Setter.
     * 
     * @param pClasspathSeparator le s�parateur du classpath.
     */
    public void setClasspathSeparator( String pClasspathSeparator )
    {
        mClasspathSeparator = pClasspathSeparator;
    }

    /**
     * Getter.
     * 
     * @return le r�pertoire cible de stockage des classes compil�es.
     */
    public String getDestDir()
    {
        return mDestDir;
    }

    /**
     * Setter.
     * 
     * @param pDestDir le r�pertoire cible de stockage des classes compil�es.
     */
    public void setDestDir( String pDestDir )
    {
        mDestDir = pDestDir;
    }

    /**
     * Getter.
     * 
     * @return la m�moire requise pour la compilation
     */
    public String getRequiredMemory()
    {
        return mRequiredMemory;
    }

    /**
     * Setter.
     * 
     * @param pRequiredMemory la m�moire requise pour la compilation
     */
    public void setRequiredMemory( String pRequiredMemory )
    {
        mRequiredMemory = pRequiredMemory;
    }

    /**
     * @return le r�pertoire cible pour copier ou extraire le bundle eclipse.
     */
    public String getEclipseBundleDir()
    {
        return mEclipseBundleDir;
    }

    /**
     * @param pEclipseBundleDir le r�pertoire cible pour copier ou extraire le bundle eclipse.
     */
    public void setEclipseBundleDir( String pEclipseBundleDir )
    {
        mEclipseBundleDir = pEclipseBundleDir;
    }

    /**
     * @return le r�pertoire des libraries export�es
     */
    public File getExportedLibsDir()
    {
        return mExportedLibsDir;
    }

    /**
     * @param pExportedLibsDir le r�pertoire des libraries export�es
     */
    public void setExportedLibsDir( String pExportedLibsDir )
    {
        mExportedLibsDir = new File( pExportedLibsDir );
    }

    /**
     * @param pDialect la version java sous la forme 1.4, 1.3,...
     * @return les chemins vers les libs jre de l'api java de la version java pDialect
     */
    public List getBootclasspath( String pDialect )
    {
        return (List) mBootclasspaths.get( pDialect );
    }

    /**
     * Ajoute une entr�e pour l'option -booclasspath de javac
     * 
     * @param pDialect le dialect java qui sert de cl�
     * @param pPath le chemin vers une librairie de l'API sun de version pDialect
     */
    public void addBootclasspath( String pDialect, String pPath )
    {
        ArrayList libs = (ArrayList) mBootclasspaths.get( pDialect );
        if ( libs == null )
        {
            libs = new ArrayList();
        }
        libs.add( pPath );
        mBootclasspaths.put( pDialect, libs );
    }

}
