//Source file: D:\\cc_views\\squale_v0_0_act\\squale\\src\\squalix\\src\\com\\airfrance\\squalix\\tools\\clearcase\\ClearCaseConfiguration.java

package com.airfrance.squalix.tools.clearcase.configuration;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.airfrance.jraf.helper.PersistenceHelper;
import com.airfrance.jraf.spi.persistence.IPersistenceProvider;
import com.airfrance.jraf.spi.persistence.ISession;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ListParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.MapParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.StringParameterBO;
import com.airfrance.squalix.configurationmanager.ConfigUtility;
import com.airfrance.squalix.core.exception.ConfigurationException;
import com.airfrance.squalix.tools.clearcase.utility.ClearCaseStringCleaner;

/**
 * Cette classe g�re la configuration pour les t�ches de montage et d�montage des vues ClearCase snapshot.
 * 
 * @author m400832 (by rose)
 * @version 2.1
 */
public class ClearCaseConfiguration
{

    /**
     * le r�pertoire correspondant au chemin de la vue + nom de la branche Permet de r�cup�rer la taille du file system
     * que cr�e la tache
     */
    private String mWriteDirectory;

    /**
     * Provider de persistence
     */
    private IPersistenceProvider mPersistenceProvider;

    /**
     * Session hibernate
     */
    private ISession mSession;

    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactory.getLog( ClearCaseConfiguration.class );

    /**
     * Constante S�parateur UNIX.
     */
    public static final String UNIX_SEPARATOR = "/";

    /**
     * Constante Espace.
     */
    public static final String SPACE = " ";

    /**
     * Constante Underscore.
     */
    public static final String UNDERSCORE = "_";

    /**
     * Commande permettant de monter une vue de travail (branche).<br />
     * Concerne les audits de suivi.
     */
    private String mMountWorkViewCommand;

    /**
     * Commande permettant de monter une vue de consultation (label).<br />
     * Concerne les audits de jalon.
     */
    private String mMountConsultationViewCommand;

    /**
     * Commande de d�montage / suppression de la vue ClearCase.
     */
    private String mUmountViewCommand;

    /**
     * Commande permettant de v�rifier si la vue existe d�j� ou non.
     */
    private String mVerifyViewExistenceCommand;

    /**
     * Commande permettant de supprimer le r�pertoire r�cursivement dans le cas o� le r�pertoire existe mais pas la vue
     */
    private String mRemoveDirectoryCommand;

    /**
     * Commande permettant de supprimer la vue quand le .view existe encore mais plus le r�pertoire
     */
    private String mAuxUnmountViewCommand;

    /**
     * Tableau contenant les vobs (principale et secondaires).
     */
    private String[] mVobList;

    /**
     * Option <code>-vob</code>.
     */
    private String mVobOption;

    /**
     * HashMap utilis�e pour la r�flexion.
     */
    private HashMap mMap = null;

    /**
     * Constructeur par d�faut. <br />
     * Appelle les m�thodes <code>getConfigurationFromXML(String)</code> et
     * <code>process(ProjectBO, AuditBo, String)</code>.
     * 
     * @throws Exception lance une exception si la configuration a �chou�.
     * @param pProject projet.
     * @param pAudit audit.
     */
    public ClearCaseConfiguration( ProjectBO pProject, AuditBO pAudit )
        throws Exception
    {
        /* cr�ation de la session */
        mPersistenceProvider = PersistenceHelper.getPersistenceProvider();
        mSession = mPersistenceProvider.getSession();
        mSession.beginTransaction();

        /* on construit la hashmap de r�flexion */
        createReflectionMap();

        /* on r�cup�re la config depuis le fichier XML */
        getConfigurationFromXML( ClearCaseMessages.getString( "configuration.file" ) );

        MapParameterBO clearcaseMapBO = (MapParameterBO) pProject.getParameter( ParametersConstants.CLEARCASE );
        if ( null == clearcaseMapBO )
        {
            String message = ClearCaseMessages.getString( "clearcase.exception.no_configuration" );
            throw new ConfigurationException( message );
        }
        String branchName;
        /* audit de jalon */
        if ( pAudit.getType().equals( AuditBO.MILESTONE ) )
        {
            branchName = pAudit.getName();

            /* audit de suivi */
        }
        else
        {
            branchName =
                ( (StringParameterBO) clearcaseMapBO.getParameters().get( ParametersConstants.BRANCH ) ).getValue();
        }

        // Recupere l'application du projet
        String appli =
            ( (StringParameterBO) clearcaseMapBO.getParameters().get( ParametersConstants.APPLI ) ).getValue();

        List vobs =
            ( (ListParameterBO) clearcaseMapBO.getParameters().get( ParametersConstants.VOBS ) ).getParameters();
        /* on lance le remplacement des motifs par leur valeur */
        process( pProject, pAudit, branchName, appli, vobs );

        /* fermeture de la session */
        mSession.commitTransactionWithoutClose();
        mSession.closeSession();
    }

    /**
     * Remplace le tag <code>{APP_NAME}</code> par la valeur fournie en param�tre. Cette m�thode est appel�e par la
     * t�che <code>
     * ClearCaseTask</code>, une fois que cette derni�re a r�cup�r� le nom ClearCase de l'application.
     * 
     * @param pApplicationName valeur de remplacement du tag.
     */
    public void processApplicationName( String pApplicationName )
    {
        replace( ClearCaseMessages.getString( "tag.application.name" ), pApplicationName );
    }

    /**
     * Appelle les m�thodes qui remplacent les patterns par leurs valeurs.
     * 
     * @param pProject projet.
     * @param pAudit audit.
     * @param pBranchName nom de la branche.
     * @param pAppli Application (au sens AF Clearcase i.e. vob d'adm)
     * @param pVobList la liste des vobs
     * @see #processVob(String)
     * @see #processBranchSuffix(String, String)
     * @see #processBranchName(AuditBO, String)
     * @see #printCommands()
     */
    private void process( ProjectBO pProject, AuditBO pAudit, String pBranchName, String pAppli, List pVobList )
    {
        processVob( pVobList );
        processBranchSuffix( pProject.getParent().getName(), pProject.getName() );
        processBranchName( pAudit, pBranchName );
        processApplicationName( pAppli );
    }

    /**
     * Casse la liste des vobs suivant le s�parateur fourni dans le fichier <code>common_messages.properties</code>.
     * <br />
     * Remplace le tag <code>{VOB_NAME}</code> par la prmi�re cha�ne trouv�e.<br />
     * Remplace le tag <code>{VOB_LIST}</code>.
     * 
     * @param pVobList liste des vobs.
     */
    private void processVob( List pVobList )
    {
        /* s'il y a plusieurs noms de VOB */
        mVobList = new String[pVobList.size()];
        for ( int i = 0; i < pVobList.size(); i++ )
        {
            mVobList[i] = ( (StringParameterBO) pVobList.get( i ) ).getValue();
        }
        /* on remplace le tag {VOB_NAME} par la premi�re cha�ne trouv�e. */
        replace( ClearCaseMessages.getString( "tag.vob.name" ), mVobList[0] );

        /* on cr�e la cha�ne de remplacement du tag {VOB_LIST}. */
        StringBuffer buf = new StringBuffer();
        int i = 0;

        /* on it�re que les �l�ments de mVobList */
        while ( i < mVobList.length )
        {
            buf.append( mVobOption + " " );
            String vobElement = mVobList[i++].trim();
            buf.append( vobElement );
            buf.append( ClearCaseConfiguration.SPACE );
        }

        /* on remplace le tag {VOB_LIST} */
        replace( ClearCaseMessages.getString( "tag.vob.list" ), buf.toString().trim() );

        buf = null;
    }

    /**
     * Remplace le tag <code>{BRANCH_NAME}</code> par la valeur fournie en param�tre.
     * 
     * @param pBranchName valeur de remplacement du tag.
     * @param pAudit audit.
     * @see #lowerBranchName(String)
     */
    private void processBranchName( AuditBO pAudit, String pBranchName )
    {
        replace( ClearCaseMessages.getString( "tag.branch.name" ), pBranchName );
        mWriteDirectory = pBranchName;
        /* si c'est un audit de jalon */
        if ( pAudit.getType().equals( AuditBO.MILESTONE ) )
        {

            /*
             * on met toute la commande en minuscules (n'a dans l'effet d'impact que sur le nom du jalon, le reste �tant
             * d�j� en minuscules)
             */
            mVerifyViewExistenceCommand = mVerifyViewExistenceCommand.toLowerCase();

            /*
             * on agit de la m�me mani�re pour les commandes de montage et de d�montage d'une vue de jalon
             */
            mMountConsultationViewCommand = lowerBranchName( mMountConsultationViewCommand );
            mUmountViewCommand = lowerBranchName( mUmountViewCommand );
        }
    }

    /**
     * Remplace le tag <code>{BRANCH_SUFFIX}</code>.
     * 
     * @param pApplicationName nom de l'application.
     * @param pProjectName nom du projet.
     * @see ClearCaseStringCleaner#getCleanedStringFrom(String)
     * @see #replace(String, String)
     */
    private void processBranchSuffix( String pApplicationName, String pProjectName )
    {
        /*
         * on concat�ne le nom de l'application, un underscore, et le nom du projet
         */
        StringBuffer buf = new StringBuffer( ClearCaseStringCleaner.getCleanedStringFrom( pApplicationName ) );
        buf.append( ClearCaseConfiguration.UNDERSCORE );
        buf.append( ClearCaseStringCleaner.getCleanedStringFrom( pProjectName ) );

        /* on applique la modif aux commandes */
        replace( ClearCaseMessages.getString( "tag.branch.suffix" ), buf.toString() );
        buf = null;
    }

    /**
     * Cette m�thode est appel�e dans le cas d'un audit de jalon pour mettre en minuscules une partie pr�cise (le nom de
     * la branche) de la commande.<br />
     * Typiquement, elle modifie la cha�ne de la fa�on suivante : <br />
     * 
     * <pre>
     * /usr/atria/bin/Perl -S /DINB/outils/gcl/script/mkview.pl -application 
     * {APP_NAME} -vob /vobs/tonus_intranet -consultation TONUS_INTRANET_V1_2_ACT 
     * -vws /app/SQUALE/clearcase/cc_storage/views/
     * TONUS_INTRANET_V1_2_ACT_mon_application_mon_projet_squale.vws 
     * -login mon_application_mon_projet_squale -snap -dir 
     * /app/SQUALE/&lt;b&gt;TONUS_INTRANET_V1_2_ACT&lt;/b&gt;_mon_application_mon_projet_squale
     * </pre>
     * 
     * <br />
     * en <br />
     * 
     * <pre>
     * /usr/atria/bin/Perl -S /DINB/outils/gcl/script/mkview.pl -application 
     * {APP_NAME} -vob /vobs/tonus_intranet -consultation TONUS_INTRANET_V1_2_ACT 
     * -vws /app/SQUALE/clearcase/cc_storage/views/
     * TONUS_INTRANET_V1_2_ACT_mon_application_mon_projet_squale.vws 
     * -login mon_application_mon_projet_squale -snap -dir 
     * /app/SQUALE/&lt;b&gt;tonus_intranet_v1_2_act&lt;/b&gt;_mon_application_mon_projet_squale
     * </pre>
     * 
     * @param pCommand commande � modifier.
     * @return la commande modifi�e.
     */
    private String lowerBranchName( String pCommand )
    {
        StringBuffer buf = new StringBuffer( pCommand );

        /*
         * on cherche la position du caract�re situ� juste apr�s le dernier s�parateur UNIX ("/") de la cha�ne.
         */
        int pos = buf.lastIndexOf( ClearCaseConfiguration.UNIX_SEPARATOR ) + 1;

        /* longueur de la chaine. */
        int length = buf.length();

        /* on met en minuscules la chaine comprise entre "pos" et "length". */
        buf.replace( pos, length, buf.substring( pos, length ).toLowerCase() );

        return buf.toString();
    }

    /**
     * Cette m�thode remplace un motif par sa valeur pour toutes les commandes UNIX disponibles dans le fichier xml de
     * configuration.
     * 
     * @param pPattern motif � remplacer. Typiquement <code>{MOTIF}</code>.
     * @param pValue valeur de remplacement.
     */
    private void replace( final String pPattern, final String pValue )
    {
        mUmountViewCommand = mUmountViewCommand.replaceAll( pPattern, pValue );
        mMountConsultationViewCommand = mMountConsultationViewCommand.replaceAll( pPattern, pValue );
        mMountWorkViewCommand = mMountWorkViewCommand.replaceAll( pPattern, pValue );
        mVerifyViewExistenceCommand = mVerifyViewExistenceCommand.replaceAll( pPattern, pValue );
        mAuxUnmountViewCommand = mAuxUnmountViewCommand.replaceAll( pPattern, pValue );
        mRemoveDirectoryCommand = mRemoveDirectoryCommand.replaceAll( pPattern, pValue );
    }

    /**
     * Cette m�thode provoque la r�cup�ration des chemins, des commandes UNIX et des patterns n�cessaires � la t�che
     * ClearCase.<br />
     * <b>ATTENTION :</b> les donn�es de la balise <code>commands</code> doivent absolument �tre r�cup�r�es en
     * premier.<br />
     * En effet, la m�thode <code>processFromXML(Node pNode)</code> sur le noeud <code>patterns</code> fait appel �
     * la m�thode <code>replace(String pPattern, String pValue)</code> qui lance une <code>NullPointerException</code>
     * si les variables contenant les commandes UNIX n'ont pas �t� initialis�es.
     * 
     * @param pFile chemin du fichier � charger.
     * @throws Exception en cas d'erreur de parsing du fichier XML de configuration.
     * @see #processFromXML(Node, String, String, String)
     * @see ConfigUtility
     */
    private void getConfigurationFromXML( final String pFile )
        throws Exception
    {
        /* on r�cup�re le noeud racine */
        Node root = ConfigUtility.getRootNode( pFile, ClearCaseMessages.getString( "configuration.root" ) );

        if ( null != root )
        {

            /* on r�cup�re le noeud concernant les commandes */
            Node myNode =
                ConfigUtility.getNodeByTagName( root, ClearCaseMessages.getString( "configuration.commands" ) );
            /*
             * on traite les noeuds command en appliquant la m�thode "mapKeyValue"
             */
            processFromXML( myNode, ClearCaseMessages.getString( "configuration.commands.command" ),
                            ClearCaseMessages.getString( "configuration.commands.command.key" ), "mapKeyValue" );

            /* on r�cup�re le noeud concernant les patterns */
            myNode = ConfigUtility.getNodeByTagName( root, ClearCaseMessages.getString( "configuration.patterns" ) );
            /* on traite les noeuds pattern en appliquant la m�thode "replace" */
            processFromXML( myNode, ClearCaseMessages.getString( "configuration.patterns.pattern" ),
                            ClearCaseMessages.getString( "configuration.patterns.pattern.key" ), "replace" );

            /* on r�cup�re le noeud concernant les options */
            myNode = ConfigUtility.getNodeByTagName( root, ClearCaseMessages.getString( "configuration.options" ) );
            /*
             * on traite les noeuds option en appliquant la m�thode "mapKeyValue"
             */
            processFromXML( myNode, ClearCaseMessages.getString( "configuration.options.option" ),
                            ClearCaseMessages.getString( "configuration.options.option.key" ), "mapKeyValue" );

            myNode = null;
        }

        root = null;
    }

    /**
     * Dans un premier temps, cette m�thode r�cup�re l'attribut et la valeur d'un noeud donn�. Puis elle appelle la
     * m�thode dont le nom est pass� en param�tre par r�flexion. <br />
     * Elle ne fonctionne que si la m�thode dont le nom est pass� en param�tre prend elle-m�me pour param�tres 2
     * <code>java.lang.String</code>.
     * 
     * @param pNode le noeud XML � parser.
     * @param pRootAnchor le nom de la balise racine � trouver
     * @param pChildAnchor le nom de la / des balise(s) fille(s) � trouver.
     * @param pMethodName le nom de la m�thode � appeler par r�flexion. ATTENTION : on ne peut passer en param�tre que
     *            des m�thodes prenant elles-m�me 2 <code>java.lang.String</code> en param�tres.
     * @throws Exception g�n�re une exception si le nom du noeud ne correspond pas � une des valeurs d�finies dans le
     *             fichier de configuration <code>
     * com.airfrance.squalix.tools.clearcase.clearcase.properties</code>.
     * @see ConfigUtility
     */
    private void processFromXML( final Node pNode, final String pRootAnchor, final String pChildAnchor,
                                 final String pMethodName )
        throws Exception
    {
        /* on r�cup�re le 1er noeud contenant une commande */
        Node myNode = ConfigUtility.getNodeByTagName( pNode, pRootAnchor );

        /*
         * instanciation des variables qui vont servir dans la boucle qui va suivre
         */
        String nodeName = null;
        String pattern = null;
        String attrName = null;
        String nodeValue = null;
        NamedNodeMap attrMap = null;

        /* tant que le noeud n'est pas nul */
        while ( null != myNode )
        {
            /* noeud de type ELEMENT */
            if ( Node.ELEMENT_NODE == myNode.getNodeType() )
            {
                /* on r�cup�re le nom du noeud. */
                nodeName = myNode.getFirstChild().getNodeName();

                /* on r�cup�re tous les attributs du noeud. */
                attrMap = ( myNode.getAttributes() );

                /* on r�cup�re le nom de l'attribut qui nous int�resse. */
                attrName = (String) ( attrMap.getNamedItem( pChildAnchor ) ).getNodeValue().trim();

                if ( null != attrName && !"".equals( attrName ) )
                {
                    // On recupere la valeur de la balise
                    // le texte peut etre parsem� de commentaires
                    // utilsi�s dans le migConfig
                    // d'o� la n�cessit� de parcourir tous les fils de type
                    // texte
                    nodeValue = null;
                    NodeList nodes = myNode.getChildNodes();
                    for ( int i = 0; ( i < nodes.getLength() ) && ( nodeValue == null ); i++ )
                    {
                        Node currentNode = nodes.item( i );
                        if ( currentNode.getNodeType() == Node.TEXT_NODE )
                        {
                            /* valeur du noeud en question. */
                            String value = currentNode.getNodeValue().trim();
                            if ( value.length() > 0 )
                            {
                                nodeValue = value;
                            }
                        }
                    }
                    // Affichage d'un warning lorsque qu'aucune valeur n'a �t� trouv�e
                    if ( nodeValue == null )
                    {
                        nodeValue = "";
                        LOGGER.warn( ClearCaseMessages.getString( "logs.cfg.empty" ) + attrName );
                    }
                    /*
                     * on cr�e la tableau de type de param�tres ici, un tableau de 2 String
                     */
                    Class[] param = { String.class, String.class };

                    /* on cr�e le tableau des 2 param�tres en rapport */
                    Object[] args = { attrName, nodeValue };

                    /* on invoque la m�thode fournie en param�tre */
                    ( (Method) ( this.getClass().getDeclaredMethod( pMethodName, param ) ) ).invoke( this, args );
                }
                else
                {
                    /* on lance une exception */
                    throw new Exception( ClearCaseMessages.getString( "exception.xml.null_or_void_attribute" ) );
                }
            }
            /* on it�re */
            myNode = myNode.getNextSibling();
        }

        /* m�nage */
        myNode = null;
        nodeName = null;
        nodeValue = null;
        pattern = null;
        attrName = null;
        attrMap = null;
    }

    /**
     * Cette m�thode attribue � des variables de classes des valeurs par r�flexion.<br />
     * La m�thode est utilis�e, mais uniquement par r�flexion.
     * 
     * @param pKey nom de commande.
     * @param pValue valeur de la commande.
     * @throws Exception exception si le nom de la commande pass� en param�tre n'est pas reconnu.
     * @see #createReflectionMap()
     */
    protected void mapKeyValue( final String pKey, final String pValue )
        throws Exception
    {
        /*
         * on invoque le setter correspondant � la cl� pKey, en lui passant la valeur pValue
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
        /*
         * tableau contenant la classe du param�tre � passer � chaque setter. ici, java.lang.String.
         */
        Class[] param = { String.class };

        mMap = new HashMap();

        /* commande v�rifiant l'existence d'une vue */
        mMap.put( ClearCaseMessages.getString( "configuration.commands.command.key.verify_view_existence" ),
                  this.getClass().getDeclaredMethod( "setVerifyViewExistenceCommand", param ) );

        /* commande montant une vue de travail -> audit de suivi */
        mMap.put( ClearCaseMessages.getString( "configuration.commands.command.key.mount_work_view" ),
                  this.getClass().getDeclaredMethod( "setMountWorkViewCommand", param ) );

        /* commande montant une vue de consultation -> audit de jalon */
        mMap.put( ClearCaseMessages.getString( "configuration.commands.command.key.mount_consultation_view" ),
                  this.getClass().getDeclaredMethod( "setMountConsultationViewCommand", param ) );

        /* commande de d�montage d'une vue */
        mMap.put( ClearCaseMessages.getString( "configuration.commands.command.key.umount_view" ),
                  this.getClass().getDeclaredMethod( "setUmountViewCommand", param ) );

        /* commande de d�montage d'une vue dans le cas o� le r�pertoire n'existe plus */
        mMap.put( ClearCaseMessages.getString( "configuration.commands.command.key.aux_umount_view" ),
                  this.getClass().getDeclaredMethod( "setAuxUmountViewCommand", param ) );

        /* commande de suppression r�cursive du r�pertoire quand le .view n'existe plus */
        mMap.put( ClearCaseMessages.getString( "configuration.commands.command.key.remove_directory" ),
                  this.getClass().getDeclaredMethod( "setRemoveDirectoryCommand", param ) );

        /* option -vob */
        mMap.put( ClearCaseMessages.getString( "configuration.options.option.key.vob" ),
                  this.getClass().getDeclaredMethod( "setVobOption", param ) );
    }

    /**
     * Getter.
     * 
     * @return la commande UNIX permettant de monter une vue de consultation sous ClearCase.
     */
    public String getMountConsultationViewCommand()
    {
        return mMountConsultationViewCommand;
    }

    /**
     * Getter.
     * 
     * @return la commande UNIX permettant de monter une vue de travail sous ClearCase.
     */
    public String getMountWorkViewCommand()
    {
        return mMountWorkViewCommand;
    }

    /**
     * Getter.
     * 
     * @return la commande UNIX permettant de d�monter une vue sous ClearCase.
     */
    public String getUmountViewCommand()
    {
        return mUmountViewCommand;
    }

    /**
     * Getter.
     * 
     * @return la commande UNIX permettant de v�rifier l'existence d'une vue sous ClearCase.
     */
    public String getVerifyViewExistenceCommand()
    {
        return mVerifyViewExistenceCommand;
    }

    /**
     * Getter.
     * 
     * @return la valeur de l'option.
     */
    public String getVobOption()
    {
        return mVobOption;
    }

    /**
     * Setter.
     * 
     * @param pVobOption la valeur de l'option.
     */
    public void setVobOption( String pVobOption )
    {
        mVobOption = pVobOption;
    }

    /**
     * Setter.
     * 
     * @param pMountConsultationViewCommand la commande UNIX permettant de monter une vue de consultation sous
     *            ClearCase.
     */
    public void setMountConsultationViewCommand( String pMountConsultationViewCommand )
    {
        mMountConsultationViewCommand = pMountConsultationViewCommand;
    }

    /**
     * Setter.
     * 
     * @param pMountWorkViewCommand la commande UNIX permettant de monter une vue de travail sous ClearCase.
     */
    public void setMountWorkViewCommand( String pMountWorkViewCommand )
    {
        mMountWorkViewCommand = pMountWorkViewCommand;
    }

    /**
     * Setter.
     * 
     * @param pUmountViewCommand la commande UNIX permettant de d�monter une vue sous ClearCase.
     */
    public void setUmountViewCommand( String pUmountViewCommand )
    {
        mUmountViewCommand = pUmountViewCommand;
    }

    /**
     * Setter.
     * 
     * @param pVerifyViewExistenceCommand la commande UNIX permettant de v�rifier l'existence d'une vue sous ClearCase.
     */
    public void setVerifyViewExistenceCommand( String pVerifyViewExistenceCommand )
    {
        mVerifyViewExistenceCommand = pVerifyViewExistenceCommand;
    }

    /**
     * @return le r�pertoire ou clearcase travaille
     */
    public String getWriteDirectory()
    {
        return mWriteDirectory;
    }

    /**
     * @return la commande pour supprimer le r�pertoire
     */
    public String getRemoveDirectoryCommand()
    {
        return mRemoveDirectoryCommand;

    }

    /**
     * @return la commande pour supprimer la vue dans le cas o� le r�pertoire n'existe plus
     */
    public String getAuxUmountViewCommand()
    {
        return mAuxUnmountViewCommand;

    }

    /**
     * @param pRemoveDirectoryCommand la commande pour supprimer le r�pertoire
     */
    public void setRemoveDirectoryCommand( String pRemoveDirectoryCommand )
    {
        mRemoveDirectoryCommand = pRemoveDirectoryCommand;

    }

    /**
     * @param pAuxUmountViewCommand la commande pour supprimer la vue si le r�pertoire n'existe plus
     */
    public void setAuxUmountViewCommand( String pAuxUmountViewCommand )
    {
        mAuxUnmountViewCommand = pAuxUmountViewCommand;

    }

}
