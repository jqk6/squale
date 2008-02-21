//Source file: D:\\cc_views\\squale_v0_0_act\\squale\\src\\squalix\\src\\com\\airfrance\\squalix\\tools\\mccabe\\McCabeConfiguration.java

package com.airfrance.squalix.tools.mccabe;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.crimson.tree.AttributeNode;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.StringParameterBO;
import com.airfrance.squalix.configurationmanager.ConfigUtility;
import com.airfrance.squalix.core.TaskData;
import com.airfrance.squalix.util.file.FileUtility;

/**
 * Configuration de l'API McCabe.
 * @author m400842
 * @version 1.0
 */
public class McCabeConfiguration {

    /**
     * Valeur limite du random de g�n�ration du dossier d'analyse du projet
     */
    private static final int RANDOM_RANGE = 1000;
    
    /**
     * Le nombre max de fichier pcf � conserver
     */
    public static final int MAX_PCF_SAVED = 10;

    /**
     * D�finit la commande pour parser les fichiers (cli ne doit pas �tre sp�cifi� dans ici)
     */
    private String mParserCommand[] = null;

    /**
     * D�finit la commande pour g�n�rer les rapports (cli ne doit pas �tre sp�cifi� dans ici)
     */
    private String mMetricsCommand[] = null;

    /**
     * Espace de travail autoris� (voire r�serv�) � McCabe : il permet d'accueillir 
     * tous les fichiers g�n�r�s par McCabe
     */
    private File mWorkspace = null;

    /**
     * Le fichier permettant de logger les traces de McCabe
     */
    private File mLogger = null;

    /**
     * Le fichier permettant de logger les erreurs de McCabe
     */
    private File mErrorLogger = null;

    /**
     * Dossier contenant les fichiers rapports ainsi que le filedb.pc les 
     * sp�cifiant.<br>
     * Les fichiers pr�-cit�s seront copi�s dans le workspace McCabe afin d'�tre 
     * utilis�s.
     */
    private File mReportsPath = null;

    /**
     * Liste des rapports � g�n�rer.
     */
    private List mReports = null;

    /**
     * Liste des parseurs utilis�s pour chaque extension. L'extension est utilis�e 
     * comme cl�.
     */
    private String mParser = null;

    /**
     * Emplacement utilis� pour l'analyse courante
     */
    private File mSubWorkspace = null;

    /**
     * Extensions des fichiers � analyser
     */
    private String mExtensions[] = null;

    /**
     * Extensions des ent�tes � include dans le filtre 'myheader.dat' 
     * (C++ uniquement)
     */
    private String mEntetes[] = null;

    /**
     * Projet � analyser
     */
    private ProjectBO mProject = null;

    /**
     * Param�tres d'analyse des fichiers
     */
    private String mParseParameters[] = null;

    /**
     * Commande utilis�e pour lancer une action en ligne de commande.<br>
     * Par ex : <code>cli</code>
     */
    private String mCliCommand = null;

    /** Les expressions r�guli�res des messages renvoy�s par McCabe en erreur qu'il faut mettre en warning */
    private List mErrorToWarningMsgs = new ArrayList(0);

    /** Les expressions r�guli�res des messages renvoy�s par McCabe qu'il faut ignorer */
    private List mIgnoringMsgs = new ArrayList(0);

    /** Les expressions r�guli�res des messages auquel il faut ajouter le nom du fichier */
    private List mAddFileNameMsgs = new ArrayList(0);

    /** Liste des messages � remplacer*/
    private Map mReplacingMsgs = new HashMap(0);

    /**
     * Logger
     */
    private static final Log LOGGER = LogFactory.getLog(McCabeConfiguration.class);

    /**
     * Parse le fichier de configuration afin d'en extraire la configuration au format 
     * objet.
     * @param pProject projet � analyser.
     * @param pFile nom du fichier de configuration.
     * @param pDatas la liste des param�tres temporaires du projet
     * @return la configuration demand�e
     * @throws Exception si un probl�me de parsing apparait.
     * @roseuid 42B97169031D
     */
    public static McCabeConfiguration build(final ProjectBO pProject, final String pFile, TaskData pDatas) throws Exception {
        McCabeConfiguration config = new McCabeConfiguration();
        config.mProject = pProject;
        // Recuperation de la configuration
        Node root = ConfigUtility.getRootNode(pFile, McCabeMessages.getString("configuration.root"));
        if (null != root) {
            // R�cup�ration du noeud contenant la configuration g�n�rale
            Node generalNode = ConfigUtility.getNodeByTagName(root, McCabeMessages.getString("configuration.general"));
            setGeneral(generalNode, config);
            // R�cup�ration du noeud contenant la configuration des profils
            Node profilesNode = ConfigUtility.getNodeByTagName(root, McCabeMessages.getString("configuration.profiles"));
            Node profileNode = profilesNode.getFirstChild();
            boolean found = false;
            // Recherche du profil associ� au projet
            String valueProject = pProject.getProfile().getName();
            // R�cup�ration du dialecte associ� au langage
            StringParameterBO dialect = (StringParameterBO) pProject.getParameters().getParameters().get(ParametersConstants.DIALECT);
            if (null != dialect) {
                valueProject += dialect.getValue();
            }
            while (null != profileNode && !found) {
                String valueConfig = ConfigUtility.getAttributeValueByName(profileNode, McCabeMessages.getString("configuration.profile.name"));
                if (profileNode.getNodeType() == Node.ELEMENT_NODE && valueConfig.equals(valueProject)) {
                    found = true;
                    // On parse la configuration associ�e au profil
                    setProfile(profileNode, config);
                } else {
                    profileNode = profileNode.getNextSibling();
                }
            }
            if (!found) {
                throw new McCabeException(McCabeMessages.getString("exception.no_profile") + valueProject);
            }
        }
        // On met en place les valeurs des param�tres du projet
        setParameters(config, pDatas);
        // Et enfin on cr�e un espace propre au projet, totalement al�atoire,
        // avec une composante d�pendant du temps, pour s'assurer que deux audits ne seront
        // pas g�n�r�s dans le m�me dossier.
        config.mSubWorkspace = new File(config.mWorkspace.getAbsolutePath() + File.separator + System.currentTimeMillis() + (int) Math.random() * RANDOM_RANGE);
        // On cr�e le dossier destin� � accueillir les fichiers McCabe                    
        if (!config.mSubWorkspace.mkdirs()) {
            throw new McCabeException(McCabeMessages.getString("exception.no_subworkspace") + config.mSubWorkspace.getAbsolutePath());
        }
        LOGGER.debug(McCabeMessages.getString("logs.debug.subworkspace_created") + config.mSubWorkspace.getAbsolutePath());
        return config;
    }

    /**
     * Access method for the mParserCommand property.
     * 
     * @return   the current value of the mParserCommand property
     * @roseuid 42D3A4EB030F
     */
    public String[] getParserCommand() {
        return mParserCommand;
    }

    /**
     * Access method for the mMetricsCommand property.
     * 
     * @return   the current value of the mMetricsCommand property
     * @roseuid 42D3A4EB031F
     */
    public String[] getMetricsCommand() {
        return mMetricsCommand;
    }

    /**
     * Access method for the mWorkspace property.
     * 
     * @return   the current value of the mWorkspace property
     * @roseuid 42D3A4EB033E
     */
    public File getWorkspace() {
        return mWorkspace;
    }

    /**
     * Access method for the mReportsPath property.
     * 
     * @return   the current value of the mReportsPath property
     * @roseuid 42D3A4EB034E
     */
    public File getReportsPath() {
        return mReportsPath;
    }

    /**
     * Access method for the mReports property.
     * 
     * @return   the current value of the mReports property
     * @roseuid 42D3A4EB036D
     */
    public List getReports() {
        return mReports;
    }

    /**
     * Access method for the mParsers property.
     * 
     * @return   the current value of the mParsers property
     * @roseuid 42D3A4EB037D
     */
    public String getParser() {
        return mParser;
    }

    /**
     * Access method for the msubWorkspace property.
     * 
     * @return   the current value of the msubWorkspace property
     * @roseuid 42D3C20A0247
     */
    public File getSubWorkspace() {
        return mSubWorkspace;
    }

    /**
     * Retourne la liste des extensions des fichiers � traiter
     * @return la liste des extensions
     * @roseuid 42D3C7AE0178
     */
    public String[] getExtensions() {
        return mExtensions;
    }

    /** Retourne la liste des extensions des ent�tes � inclure dans le filtre.
     * @return la liste des extensions des ent�tes.
     */
    public String[] getEntetes() {
        return mEntetes;
    }

    /**
     * Retourne les param�tres d'analyse des fichiers.
     * @return une chaine contenant tous les param�tres d'audit.
     * @roseuid 42D3E686021F
     */
    public String[] getParseParameters() {
        return mParseParameters;
    }

    /**
     * Retourne la commande � utiliser pour lancer une op�ration en ligne de commande.
     * @return la commande � ex�cuter.
     * @roseuid 42D4C865009A
     */
    public String getCliCommand() {
        return mCliCommand;
    }

    /**
     * Met la configuration g�n�rale en place.
     * @param pNode le noeud XML � parser
     * @param pConfiguration l'instance de configuration � mettre en place.
     * @throws Exception si la configuration g�n�rale n'est pas correcte.
     * @roseuid 42D520A303C2
     */
    private static void setGeneral(final Node pNode, final McCabeConfiguration pConfiguration) throws Exception {
        // le fichier de log
        pConfiguration.mLogger = FileUtility.getLogFile(ConfigUtility.getNodeByTagName(pNode, McCabeMessages.getString("configuration.general.logger")).getFirstChild().getNodeValue().trim());
        // le fichier de log des erreurs
        String errorLogName = McCabeMessages.getString("configuration.general.error.logger");
        pConfiguration.mErrorLogger = FileUtility.getLogFile(ConfigUtility.getNodeByTagName(pNode, errorLogName).getFirstChild().getNodeValue().trim());
        // Workspace
        pConfiguration.mWorkspace = new File(ConfigUtility.getNodeByTagName(pNode, McCabeMessages.getString("configuration.general.workspace")).getFirstChild().getNodeValue().trim());
        // On effectue le nettoyage dans le cas o� la t�che s'est arr�t�e brusquement:
        deleteOldSubWorkspace(pConfiguration.mWorkspace);
        // Chemin du fichier contenant les rapports
        pConfiguration.mReportsPath = new File(ConfigUtility.getNodeByTagName(pNode, McCabeMessages.getString("configuration.general.reportspath")).getFirstChild().getNodeValue().trim());
        // Commandes
        Node commandsNode = ConfigUtility.getNodeByTagName(pNode, McCabeMessages.getString("configuration.general.commands"));
        // Commande cli
        pConfiguration.mCliCommand = ConfigUtility.getNodeByTagName(commandsNode, McCabeMessages.getString("configuration.general.commands.clicommand")).getFirstChild().getNodeValue().trim();
        // Commandes parser
        StringTokenizer token =
            new StringTokenizer(ConfigUtility.getNodeByTagName(commandsNode, McCabeMessages.getString("configuration.general.commands.parsercommand")).getFirstChild().getNodeValue().trim(), " ");
        String params[] = new String[token.countTokens()];
        for (int i = 0; i < params.length; i++) {
            params[i] = token.nextToken();
        }
        pConfiguration.mParserCommand = params;
        // Commandes metrics
        token = new StringTokenizer(ConfigUtility.getNodeByTagName(commandsNode, McCabeMessages.getString("configuration.general.commands.metricscommand")).getFirstChild().getNodeValue().trim(), " ");
        params = new String[token.countTokens()];
        for (int i = 0; i < params.length; i++) {
            params[i] = token.nextToken();
        }
        pConfiguration.mMetricsCommand = params;
        // Liste des rapports
        pConfiguration.mReports = getStrListFromNode(pNode, "configuration.general.reports");
        /* Les filtre */
        Node filterNode = ConfigUtility.getNodeByTagName(pNode, McCabeMessages.getString("configuration.general.filter"));
        // Liste des messages d'erreur � changer en warning
        pConfiguration.mErrorToWarningMsgs = getStrListFromNode(filterNode, "configuration.general.filter.errorToWarningMsgs");
        // Liste des messages � ignorer
        pConfiguration.mIgnoringMsgs = getStrListFromNode(filterNode, "configuration.general.filter.ignoringMsgs");
        // Liste des messages auxquels il faut ajouter le nom du fichier
        pConfiguration.mAddFileNameMsgs = getStrListFromNode(filterNode, "configuration.general.filter.addFleNameMsgs");
        // Liste des messages � remplacer
        Node replacingMsgsNode = ConfigUtility.getNodeByTagName(filterNode, McCabeMessages.getString("configuration.general.filter.replacingMsgs"));
        Iterator it = ConfigUtility.filterList(replacingMsgsNode.getChildNodes(), Node.ELEMENT_NODE).iterator();
        NamedNodeMap replacingMsgNodeMap = null;
        String keyMsg = McCabeMessages.getString("configuration.general.filter.replacingMsg.ketAttr");
        String ValueMsg = McCabeMessages.getString("configuration.general.filter.replacingMsg.ValueAttr");
        while (it.hasNext()) {
            replacingMsgNodeMap = ((Node)it.next()).getAttributes();
            pConfiguration.mReplacingMsgs.put(((AttributeNode)replacingMsgNodeMap.getNamedItem(keyMsg)).getValue(), ((AttributeNode)replacingMsgNodeMap.getNamedItem(ValueMsg)).getValue());
        }
    }

    /**
     * Supprime l'ancien subWorkspace dans le cas o� la t�che s'est arr�t�e brusquement et que la suppression
     * du r�pertoire n'a pas pu se faire.
     * @param pWorkspace le workspace mccabe
     */
    private static void deleteOldSubWorkspace(File pWorkspace) {
        // On supprime l'ancien subWorkspace identifi� par un nom de r�pertoire
        // constitu� que de chiffres
        if(!pWorkspace.exists()) {
            pWorkspace.mkdirs();
        }
        File[] files = pWorkspace.listFiles();
        boolean found = false;
        for(int i=0; i<files.length && !found; i++) {
            if(files[i].isDirectory() && files[i].getName().matches("[0-9]+")) {
                found = true;
                FileUtility.deleteRecursively(files[i]);
            }
        }
    }

    /**
     * Retourne une liste de cha�nes de caract�res repr�senant les noeuds contenus dans le noeud de cl� 
     * <code>pChildKey</code>
     * @param pParentNode le neoud parent
     * @param pChildKey la cl� du fichier de properties du noeud enfant
     * @return la liste des noeuds contenus dans le noeud de cl� pChildKey
     */
    private static List getStrListFromNode(Node pParentNode, String pChildKey) {
        Node rootNode = ConfigUtility.getNodeByTagName(pParentNode, McCabeMessages.getString(pChildKey));
        Iterator it = ConfigUtility.filterList(rootNode.getChildNodes(), Node.ELEMENT_NODE).iterator();
        ArrayList results = new ArrayList();
        String name = null;
        while (it.hasNext()) {
            name = ((Node) it.next()).getFirstChild().getNodeValue().trim();
            results.add(name);
        }
        return results;
    }

    /**
     * Met la configuration du profil en place.
     * @param pNode le noeud XML � parser.
     * @param pConfiguration l'instance de configuration � mettre en place.
     * @roseuid 42D520A303D2
     */
    private static void setProfile(final Node pNode, final McCabeConfiguration pConfiguration) {
        // Nom du parser
        pConfiguration.mParser = ConfigUtility.getNodeByTagName(pNode, McCabeMessages.getString("configuration.profile.parser")).getFirstChild().getNodeValue().trim();
        // Liste des extensions
        Node extensionsNode = ConfigUtility.getNodeByTagName(pNode, McCabeMessages.getString("configuration.profile.extensions"));
        // Les extensions sont s�par�es par des ","
        StringTokenizer token = new StringTokenizer(extensionsNode.getFirstChild().getNodeValue().trim(), ",");
        ArrayList extensions = new ArrayList();
        // On ajoute les extensions � la liste
        while (token.hasMoreTokens()) {
            extensions.add(token.nextToken().trim());
        }
        String[] type = new String[0];
        // On convertit la liste des extensions en tableau de String
        pConfiguration.mExtensions = (String[]) extensions.toArray(type);

        // Liste des extensions des ent�tes
        Node entetesNode = ConfigUtility.getNodeByTagName(pNode, McCabeMessages.getString("configuration.profile.entetes"));
        if (entetesNode != null) {
            // Les en-t�tes sont s�par�s par des ","
            token = new StringTokenizer(entetesNode.getFirstChild().getNodeValue().trim(), ",");
            ArrayList entetes = new ArrayList();
            while (token.hasMoreTokens()) {
                entetes.add(token.nextToken().trim());
            }
            type = new String[0];
            // Conversion de la liste cr�er en tableau de String
            pConfiguration.mEntetes = (String[]) entetes.toArray(type);
        }

        // Liste des param�tres de parsing
        Node parametersNode = ConfigUtility.getNodeByTagName(pNode, McCabeMessages.getString("configuration.profile.parameters"));
        ArrayList parameters = new ArrayList();
        // Un tag par param�tre
        Node parameterNode = parametersNode.getFirstChild();
        // On ajoute tous les param�tres
        while (null != parameterNode) {
            if (parameterNode.getNodeType() == Node.ELEMENT_NODE) {
                parameters.add(parameterNode.getFirstChild().getNodeValue());
            }
            parameterNode = parameterNode.getNextSibling();
        }
        // Conversion en tableau de String
        pConfiguration.mParseParameters = (String[]) parameters.toArray(type);
    }

    /**
     * Met en place le dossier contenant les fichiers g�n�r�s par McCabe pour le projet.
     * @param pFile le dossier
     * @roseuid 42DD182603A4
     */
    public void setSubWorkspace(File pFile) {
        mSubWorkspace = pFile;
    }

    /**
     * Rectourne le projet associ� � la configuration.
     * @return le projet.
     * @roseuid 42DE0375023D
     */
    public ProjectBO getProject() {
        return mProject;
    }

    /**
     * Remplit la configuration avec les param�tres li�s au projet.
     * @param pConfig configuration � remplir.
     * @param pDatas la liste des param�tres du projet.
     */
    private static void setParameters(final McCabeConfiguration pConfig, final TaskData pDatas) {
        // On modifie le classpath
        // Lorsqu'on r�cup�re les param�tres, si mClasspath n'est pas null
        // on remplace la valeur key.substition.classpath par mClasspath
        String classpath = "\"\"";
        if (null != pDatas.getData(TaskData.CLASSPATH)) {
            classpath = (String) pDatas.getData(TaskData.CLASSPATH);
            // On cr�e un classpath destin� � Unix, en s'assurant que chaque entr�e sp�cifi�e
            // existe, sinon elle est supprim�e de la liste
            StringBuffer sb = new StringBuffer();
            StringTokenizer tok = new StringTokenizer(classpath, ";");
            File f = null;
            while (tok.hasMoreTokens()) {
                f = new File(tok.nextToken());
                if (f.exists()) {
                    sb.append(f.getAbsolutePath());
                    // Les entr�es du Classpath doivent �tre s�par�s par des ":" pour Unix
                    sb.append(File.pathSeparator);
                }
            }
            if (sb.length() > 0) {
                // S'il y a des valeurs dans le classpath, on sait
                // qu'un : a �t� ajout� en trop
                sb.deleteCharAt(sb.length() - 1);
            }
            // On s'assure que la valeur du classpath est consid�r�e comme un seul param�tre
            classpath = "\"" + sb.toString() + "\"";
        }
        //
        String classPathPattern = McCabeMessages.getString("key.substition.classpath");
        for (int i = 0; i < pConfig.mParseParameters.length; i++) {
            if (pConfig.mParseParameters[i].equals(classPathPattern)) {
                pConfig.mParseParameters[i] = classpath;
            }
        }
    }

    /**
     * Met en place l'instance du projet.
     * @param pProject le projet analys�.
     * @roseuid 42E5F0CF00D2
     */
    public void setProject(final ProjectBO pProject) {
        mProject = pProject;
    }

    /**
     * @return le fichier de log
     */
    public File getLogger() {
        return mLogger;
    }

    /**
     * @param pFile le fichier de log
     */
    public void setLogger(File pFile) {
        mLogger = pFile;
    }

    /**
     * @return le fichier de log des erreurs
     */
    public File getErrorLogger() {
        return mErrorLogger;
    }

    /**
     * @param pFile le fichier de log des erreurs
     */
    public void setErrorLogger(File pFile) {
        mErrorLogger = pFile;
    }

    /** 
     * Indique si le message d'erreur doit �tre pass� en warning 
     * @param errorMsg le message d'erreur � v�rifier
     * @return true si le message d'erreur est consid�r� comme un warning
     */
    public boolean changeErrorMsgToWarning(String errorMsg) {
        return matchesRegexInTab(errorMsg, mErrorToWarningMsgs);
    }

    /** 
     * Ajoute si le nom du fichier si le message est indiqu� dans la partie
     * "addFileNameMessages" du filtre des messages de sortie
     * @param msg le message � modifier
     * @param fileName le nom du fichier � ajouter
     * @return le message modifi� si besoin
     */
    public String addFileNameToMsg(String msg, String fileName) {
        String result = msg;
        if (matchesRegexInTab(msg, mAddFileNameMsgs)) {
            result += " File: " + fileName;
        }
        return result;
    }

    /** 
     * Indique si le message de sortie doit �tre ignor�
     * @param msg le message de sortie � v�rifier
     * @return true si le message doit �tre ignor�
     */
    public boolean ignoreMsg(String msg) {
        boolean result = false;
        // On ignore les messages nuls ou vide
        if (msg != null && !msg.equals("")) {
            result = matchesRegexInTab(msg, mIgnoringMsgs);
        }
        return result;
    }

    /**
     * substitue <code>msg</code> par le message donn� en configuration
     * si il est pr�sent dans la liste
     * @param msg le messae � remplacer potentiellement
     * @return le message de substitution
     */
    public String getReplacingMsg(String msg) {
        String result = msg;
        boolean found = false;
        // on recherche l'expression r�guli�re correspondant au message
        for (Iterator it = mReplacingMsgs.keySet().iterator(); it.hasNext() && !found;) {
            String curRegex = (String) it.next();
            if (msg.matches(curRegex)) {
                result = (String) mReplacingMsgs.get(curRegex);
                found = true;
            }
        }
        return result;
    }

    /**
     * @param msg le message � v�rifier
     * @param regexs la liste des expressions r�guli�res
     * @return true si <code>msg</code> match une expression r�guli�re contenue dans <code>tab</code>
     */
    private boolean matchesRegexInTab(String msg, List regexs) {
        boolean result = false;
        for (int i = 0; i < regexs.size() && !result; i++) {
            result = msg.matches((String)regexs.get(i));
        }
        return result;
    }

    /**
     * @return la liste Les expressions r�guli�res des messages auquel il faut ajouter le nom du fichier
     */
    public List getAddFileNameMsgs() {
        return mAddFileNameMsgs;
    }

    /**
     * @return la liste des messages d'erreur � mettre en warning
     */
    public List getErrorToWarningMsgs() {
        return mErrorToWarningMsgs;
    }

    /**
     * @return la liste des messages � ignorer
     */
    public List getIgnoringMsgs() {
        return mIgnoringMsgs;
    }

    /**
     * @return la liste des messages � remplacer par un autre
     */
    public Map getReplacingMsgs() {
        return mReplacingMsgs;
    }

}
