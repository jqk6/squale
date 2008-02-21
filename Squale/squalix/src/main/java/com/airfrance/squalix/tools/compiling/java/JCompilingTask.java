//Source file: D:\\cc_views\\squale_v0_0_act\\squale\\src\\squalix\\src\\com\\airfrance\\squalix\\tools\\compiling\\java\\JavaCompilingTask.java

package com.airfrance.squalix.tools.compiling.java;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Project;

import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ListParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.MapParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.StringParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.ErrorBO;
import com.airfrance.squalix.core.AbstractTask;
import com.airfrance.squalix.core.TaskData;
import com.airfrance.squalix.core.TaskException;
import com.airfrance.squalix.tools.compiling.CompilingMessages;
import com.airfrance.squalix.tools.compiling.java.adapter.JComponent;
import com.airfrance.squalix.tools.compiling.java.beans.JRSAProject;
import com.airfrance.squalix.tools.compiling.java.beans.JWSADProject;
import com.airfrance.squalix.tools.compiling.java.beans.JXMLProject;
import com.airfrance.squalix.tools.compiling.java.compiler.impl.EclipseCompilerImpl;
import com.airfrance.squalix.tools.compiling.java.compiler.impl.JWSADCompilerImpl;
import com.airfrance.squalix.tools.compiling.java.compiler.impl.JXMLCompilerImpl;
import com.airfrance.squalix.tools.compiling.java.configuration.JCompilingConfiguration;
import com.airfrance.squalix.tools.compiling.java.parser.impl.JRSAParserImpl;
import com.airfrance.squalix.tools.compiling.java.parser.impl.JWSADParserImpl;
import com.airfrance.squalix.tools.compiling.java.parser.wsad.JWSADDotProjectParser;
import com.airfrance.squalix.util.file.FileUtility;

/**
 * T�che de compilation JAVA.<br />
 * Compile � partir d'environnements WSAD 5.x et/ou de fichiers XML ANT.
 * H�rite de la classe m�re <code>CompilingTask</code>.
 * @author m400832
 * @version 1.0
 */
public class JCompilingTask extends AbstractTask implements BuildListener {

    /**
     * le message associ� � une erreur de compil
     * utilis� pour mettre les messages sous une bonne forme pour le web
     */
    private String mCompilErrorMessage;

    /**
     * Instance de configuration.
     */
    private JCompilingConfiguration mConfiguration = null;

    /**
     * S�parateur entre r�pertoires dans la HashMap de param�tres du 
     * <code>ProjectBO</code>.
     */
    private static final String SEPDIRS = ";";

    /**
     * Liste de JComponent.
     */
    private ArrayList mComponents = null;

    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactory.getLog(JCompilingTask.class);

    /**
     * Constructeur par d�faut.
     */
    public JCompilingTask() {
        mName = "JCompilingTask";
        mCompilErrorMessage = "";
    }

    /**
     * M�thode de lancement de la t�che.
     * @throws TaskException en cas d'�chec
     */
    public void execute() throws TaskException {
        try {
            mConfiguration = new JCompilingConfiguration();
            mComponents = new ArrayList();
            LOGGER.trace(CompilingMessages.getString("java.logs.task.initialized") + getProject().getName());
            /* cr�ation des composants */
            buildComponents();
            /* traitement des composants, compilation */
            process();
            /* modification des param�tres de sortie */
            modifyParameters();
            /* affichage des param�tres en mode debug */
            printParameters();
            // positionne les donn�es sur la taille du file System
            affectFileSystemSize(mData.getData(TaskData.CLASSES_DIRS), true);

        } catch (Exception e) {
            throw new TaskException(e);
        }
    }

    /**
     * Cette m�thode influe sur le <code>ProjectBO</code> en modifiant la 
     * <code>HashMap</code> de param�tres.
     * @throws Exception Exception lors de la modification.
     */
    private void modifyParameters() throws Exception {
        modifyClasspathInHashMap();
        modifyClassesDirInHashMap();
    }

    /**
     * 
     */
    private void modifyClassesDirInHashMap() {
        List classesDirs = new ArrayList();
        Iterator it = mComponents.iterator();
        /* si l'it�rateur poss�de des �l�ments */
        if (null != it && it.hasNext()) {
            /* initialisation des variables utilis�es par la boucle */
            JComponent jComp = null;

            /* tant que l'it�rateur poss�de des �l�ments */
            while (it.hasNext()) {
                /* on cr�e un JComponent */
                jComp = (JComponent) it.next();
                /* si le JComponent est de type JWSADParserImpl 
                 * i.e. projet WSAD ou RSA avec compilation avec le plugin Eclipse */
                if (jComp.getComponent() instanceof EclipseCompilerImpl) {
                    // On ajoute la liste des r�pertoires
                    classesDirs.addAll(((EclipseCompilerImpl)jComp.getComponent()).getEclipseCompiler().getOutputDirs());
                    mData.putData(TaskData.CLASSES_DIRS, classesDirs);
                }
            }
        }
    }

    /**
     * Positionne la variable donnant le chemin du r�pertoire 
     * racine contenant les .class du projet
     * @param classesDir le chemin du r�pertoire contenant les .class
     * Param�tre fourni par les m�thodes create***Components
     */
    private void modifyClassesDirInHashMap(String classesDir) {
        List classesDirs = new ArrayList();
        classesDirs.add(classesDir);
        mData.putData(TaskData.CLASSES_DIRS, classesDirs);
    }

    /**
     * Cette m�thode influe sur le <code>ProjectBO</code> en modifiant la 
     * <code>HashMap</code> de param�tres, pour le param�tre <code>
     * project.parameter.classpath</code>.
     * @throws Exception Exception lors de la modification.
     */
    private void modifyClasspathInHashMap() throws Exception {
        //TODO : revoir cette m�thode pour �viter de faire des instanceof!
        Iterator it = mComponents.iterator();

        /* si l'it�rateur poss�de des �l�ments */
        if (null != it && it.hasNext()) {
            /* initialisation des variables utilis�es par la boucle */
            JComponent jComp = null;
            StringBuffer buf = new StringBuffer();

            /* tant que l'it�rateur poss�de des �l�ments */
            while (it.hasNext()) {
                /* on cr�e un JComponent */
                jComp = (JComponent) it.next();

                /* si le JComponent est de type JWSADParserImpl 
                 * i.e. projet WSAD */
                if (jComp.getComponent() instanceof JWSADParserImpl) {
                    /* alors on modifie le classpath pour McCabe */
                    buf.append(modifyClasspathInHashMapWSAD(((JWSADParserImpl) (jComp.getComponent())).getProjectList()));
                    /* on ajoute le s�parateur de r�pertoires, i.e. ";" */
                    buf.append(SEPDIRS);

                    /* si le composant est de type JXMLCompilerImpl
                     * i.e. projet ANT */
                } else if ((jComp.getComponent()) instanceof JXMLCompilerImpl) {
                    /* alors on modifie le classpath pour McCabe */
                    buf.append((((JXMLCompilerImpl) (jComp.getComponent())).getXMLProject()));
                    /* on ajoute le s�parateur de r�pertoires, i.e. ";" */
                    buf.append(SEPDIRS);
                } else if (jComp.getComponent() instanceof JRSAParserImpl) {
                    /* alors on modifie le classpath pour McCabe */
                    buf.append(modifyClasspathInHashMapWSAD(((JRSAParserImpl) (jComp.getComponent())).getProjectList()));
                    /* on ajoute le s�parateur de r�pertoires, i.e. ";" */
                    buf.append(SEPDIRS);
                } else if (jComp.getComponent() instanceof EclipseCompilerImpl) {
                    /* alors on modifie le classpath pour McCabe */
                    buf.append(((EclipseCompilerImpl) (jComp.getComponent())).getEclipseCompiler().getClasspath());
                    /* on ajoute le s�parateur de r�pertoires, i.e. ";" */
                    buf.append(SEPDIRS);
                }
            }
            jComp = null;

            /* on r�cup�re la cl� de la map de param�tres du projet 
             * dont la valeur associ�e sera le classpath */
            String key = TaskData.CLASSPATH;

            String value = (String) mData.getData(key);
            if (value == null) {
                /* sinon on cr�e une cha�ne vide */
                value = "";
            }

            /* on met au propre le classpath */
            value = generateProperClasspath(value, buf.toString());

            buf = null;

            /* on modifie la hashmap de param�tres du projet en 
             * cons�quence. */
            getData().putData(key, value);
        }
        it = null;
    }

    /**
     * Cette m�thode influe sur le <code>ProjectBO</code> en modifiant la 
     * <code>HashMap</code> de param�tres, pour les projets WSAD.
     * @param pList liste de projets WSAD.
     * @throws Exception exception lors du traitement.
     * @return String valeur � ajouter � la cl�.
     */
    private String modifyClasspathInHashMapWSAD(List pList) throws Exception {
        String value = null;

        /* si la liste de projets fournie n'est pas vide */
        if (null != pList) {
            Iterator it = pList.iterator();

            /* si l'it�rateur poss�de des �l�ments */
            if (null != it && it.hasNext()) {

                /* on instancie les variables utilis�es dans la boucle */
                JWSADProject project = null;
                StringBuffer cp = new StringBuffer();

                /* tant que l'it�rateur a des �l�ments */
                while (it.hasNext()) {
                    /* on cr�e le bean JWSADProject */
                    project = (JWSADProject) it.next();
                    /* on ajoute le classpath du bean au buffer 
                     * NB : pas besoin d'ajouter de SEPDIRS car le classpath 
                     * retourn� se termine toujours par un ";" */
                    cp.append(project.getClasspath());
                    /* on ajoute au buffer le chemin du r�pertoire contenant 
                     * les classes compil�es */
                    cp.append(project.getDestPath());
                    /* on ajoute un s�parateur de r�pertoires, i.e. ";" */
                    cp.append(SEPDIRS);
                }

                /* on affecte la valeur retourn�e*/
                value = cp.toString();

                /* on fait le m�nage */
                cp = null;
                project = null;
            }
            it = null;
        }
        return value;
    }

    /**
     * Cette m�thode influe sur le <code>ProjectBO</code> en modifiant la 
     * <code>HashMap</code> de param�tres, pour les projets WSAD.
     * @param pProject JXMLProject.
     * @throws Exception exception lors du traitement.
     * @return valeur � ajouter � la cl�.
     */
    private String modifyClasspathInHashMapXML(JXMLProject pProject) throws Exception {
        String value = null;
        /* si le projet n'est pas nulle */
        if (null != pProject && null != pProject.getPath()) {

            /* on r�cup�re la portion du chemin du projet XML contenu entre le 
             * d�but de ladite cha�ne et le dernier "/" rencontr�.
             * i.e. on r�cup�re "/app/SQUALE" si le chemin est 
             * "/app/SQUALE/build.xml".
             */
            String path = pProject.getPath();
            value = path.substring(0, path.lastIndexOf(JCompilingConfiguration.UNIX_SEPARATOR));
        }
        return value;
    }

    /**
     * M�thode qui lance l'ex�cution des parseurs et compilateurs.
     * @throws Exception exception lors de l'ex�cution.
     */
    private void process() throws Exception {
        Iterator it = mComponents.iterator();

        /* si l'it�rateur a des �l�ments */
        if (it != null && it.hasNext()) {

            /* tant qu'il en poss�de */
            while (it.hasNext()) {
                /* on active le composant */
                JComponent comp = (JComponent) (it.next());
                comp.getComponent().execute();
                // On r�cup�re les �ventuelles erreurs de traitement
                for (Iterator itErr = comp.getComponent().getErrors().iterator(); itErr.hasNext();) {
                    ErrorBO err = (ErrorBO) itErr.next();
                    initError(err.getInitialMessage(), err.getLevel());
                }
            }
        }
        it = null;
    }

    /**
     * Cette m�thode lance la cr�ation des composants.
     * @throws Exception exception lors de la cr�ation.
     */
    private void buildComponents() throws Exception {
        // Le projet contient soit une d�finition de projet WSAD
        // soit une d�finition ANT, soit une d�finition RSA
        ListParameterBO wsadList = (ListParameterBO) mProject.getParameters().getParameters().get(ParametersConstants.WSAD);
        if (wsadList != null) {
            createWSADOrRSAComponents(mProject.getParameters(), wsadList.getParameters().iterator(), false);
        } else {
            ListParameterBO antList = (ListParameterBO) mProject.getParameters().getParameters().get(ParametersConstants.ANT);
            if (antList != null) {
                for (int i = 0; i < antList.getParameters().size(); i++) {
                    createXMLComponent((MapParameterBO) antList.getParameters().get(i));
                }
            } else {
                ListParameterBO rsaParams = (ListParameterBO) mProject.getParameters().getParameters().get(ParametersConstants.RSA);
                if (null != rsaParams) {
                    createWSADOrRSAComponents(mProject.getParameters(), rsaParams.getParameters().iterator(), true);
                } else {
                    /*  en cas d'exception */
                    throw new Exception(CompilingMessages.getString("java.exception.task.unsupported_component"));
                }
            }
        }
    }

    /**
     * Cette m�thode cr�e les composants n�cessaires � la compilation des 
     * projects de type WSAD.
     * @param pMap MapParameterBO contenant les 
     * informations n�cessaires � la cr�ation des composants.
     * @param pProjectsIt l'it�rateur sur les projets
     * @param pIsRSA si il s'agit d'une liste de projet type RSA
     * @throws Exception exception.
     */
    private void createWSADOrRSAComponents(MapParameterBO pMap, Iterator pProjectsIt, boolean pIsRSA) throws Exception {
        /* on initialise les variables utilis�es par la boucle */
        ListParameterBO exludedDirs = (ListParameterBO) pMap.getParameters().get(ParametersConstants.EXCLUDED_DIRS);
        // On r�cup�re le bundle eclipse
        String bundleDir = getEclipseBundleFile((StringParameterBO) pMap.getParameters().get(ParametersConstants.BUNDLE_PATH));
        // La liste des JWSADprojects
        List JWSADProjectList = new ArrayList(0);
        JWSADProject myProject = null;
        List projectParamsList = null;
        for (; pProjectsIt.hasNext();) {
            myProject = new JWSADProject();
            // Ajout du listener
            myProject.setListener(this);
            // On change le fichier du bundle
            myProject.setBundleDir(bundleDir);
            /* on affecte les attributs */
            String dirName = "";
            if (!pIsRSA) {
                StringParameterBO stringBO = (StringParameterBO) pProjectsIt.next();
                dirName = stringBO.getValue();
                // On r�cup�re le manifest (dans le cas par exemple des plugins RCP)
                MapParameterBO projectsParams = (MapParameterBO) pMap.getParameters().get(ParametersConstants.WSAD_PROJECT_PARAM);
                if (null != projectsParams) {
                    MapParameterBO projectParamsMap = (MapParameterBO) projectsParams.getParameters().get(dirName);
                    if (null != projectParamsMap) {
                        StringParameterBO manifest = (StringParameterBO) projectParamsMap.getParameters().get(ParametersConstants.MANIFEST_PATH);
                        myProject.setManifestPath(manifest.getValue());
                    }
                }
                // On modifie les param�tres communs aux projets RSA ou WSAD
                setProjectParams(pMap, myProject, dirName);
                /* on ajoute le projet � la liste des projets */
                JWSADProjectList.add(myProject);
            } else {
                projectParamsList = ((ListParameterBO) pProjectsIt.next()).getParameters();
                dirName = ((StringParameterBO) projectParamsList.get(ParametersConstants.WORKSPACE_ID)).getValue();
                setProjectParams(pMap, myProject, dirName);
                // Si le projet est un projet RSA7, on parse avec le parser RSA
                // car il y a des petites diff�rences au niveau de la construction du .classpath
                // On ajoute les informations n�cessaires au projet pour le parsing RSA
                JRSAProject rsaProject = new JRSAProject(myProject);
                if (projectParamsList.size() > ParametersConstants.EAR_NAME_ID) {
                    String earName = ((StringParameterBO) projectParamsList.get(ParametersConstants.EAR_NAME_ID)).getValue();
                    rsaProject.setEARProjectName(earName);
                }
                if (projectParamsList.size() > ParametersConstants.MANIFEST_PATH_ID) {
                    String manifestPath = ((StringParameterBO) projectParamsList.get(ParametersConstants.MANIFEST_PATH_ID)).getValue();
                    rsaProject.setManifestPath(manifestPath);
                }
                JWSADProjectList.add(rsaProject);
            }
            if (!myProject.isWSAD()) {
                /* ce n'est pas un projet WSAD */
                throw new Exception(CompilingMessages.getString("java.exception.task.malformed_wsad_project_in_hashmap") + myProject.getPath());
            }
        }
        addEclipseOrWSADCompilerToComponents(pMap, JWSADProjectList, pIsRSA);

    }

    /**
     * @param pMap les param�tres de la t�che
     * @param pJWSADProjectList la liste des projets WSAD
     * @param pIsRSA si les projets sont de type RSA
     * @throws Exception si erreur lors de la cr�ation
     */
    private void addEclipseOrWSADCompilerToComponents(MapParameterBO pMap, List pJWSADProjectList, boolean pIsRSA) throws Exception {
        MapParameterBO eclipseMap = (MapParameterBO) pMap.getParameters().get(ParametersConstants.ECLIPSE);
        StringParameterBO isEclipse = null;
        MapParameterBO eclipseVars = null;
        MapParameterBO eclipseLibs = null;
        if (null != eclipseMap) {
            isEclipse = (StringParameterBO) eclipseMap.getParameters().get(ParametersConstants.ECLIPSE_COMPILATION);
            eclipseVars = (MapParameterBO) eclipseMap.getParameters().get(ParametersConstants.ECLIPSE_VARS);
            eclipseLibs = (MapParameterBO) eclipseMap.getParameters().get(ParametersConstants.ECLIPSE_LIBS);

        }
        if (null != isEclipse && isEclipse.getValue().equals("false")) {
            // On va parser les .classpath puis compiler avec javac
            /* on cr�e les composants : pour compiler les projets WSAD, il faut d'abord 
             * parser les fichiers ".classpath" puis compiler. D'o� le recours � 
             * deux composants */
            JComponent j1 = new JComponent(new JWSADParserImpl(pJWSADProjectList));
            if (pIsRSA) {
                j1 = new JComponent(new JRSAParserImpl(pJWSADProjectList));
            }
            JWSADCompilerImpl jcawi = new JWSADCompilerImpl(pJWSADProjectList);
            JComponent j2 = new JComponent(jcawi);
            addToComponents(j1);
            addToComponents(j2);
        } else {
            // On compile avec le plugin eclipse
            // On r�cup�re les variables et librairies d�finies par l'utilisateur
            EclipseCompilerImpl eclipseCompiler = new EclipseCompilerImpl(pJWSADProjectList, (String) mData.getData(TaskData.VIEW_PATH), eclipseVars, eclipseLibs);
            JComponent component = new JComponent(eclipseCompiler);
            addToComponents(component);
        }
    }

    /**
     * @param bundlePath le param�tre d�finissant le chemin vers le bundle eclipse
     * @return le chemin d�finitif et v�rifi� vers la distribution d'eclipse pour la compilation RCP
     */
    private String getEclipseBundleFile(StringParameterBO bundlePath) {
        // On r�cup�re la version de distribution d'eclipse � utiliser
        // Si il s'agit d'un r�pertoire, on le d�zippe sinon on utilise le chemin tel quel
        String bundleDir = "";
        if (null != bundlePath) {
            try {
                File bundleFile = FileUtility.getAbsoluteFile((String) mData.getData(TaskData.VIEW_PATH), new File(bundlePath.getValue()));
                if (bundleFile.isDirectory()) {
                    bundleDir = bundleFile.getAbsolutePath();
                } else {
                    // On extrait l'archive
                    FileUtility.copyOrExtractInto(bundleFile, new File(mConfiguration.getEclipseBundleDir()));
                    // on affecte le chemin donn� dans la configuration
                    bundleDir = mConfiguration.getEclipseBundleDir();
                }
            } catch (Exception e) {
                // On va juste lancer un warning en affichant l'erreur car la compilation peut-�tre 
                // r�ussir sans le bundle
                String exceptionMsg = (e.getMessage() == null) ? e.getMessage() : e.toString();
                String message = CompilingMessages.getString("java.warning.eclipse_bundle_not_correct", new String[] { bundlePath.getValue(), exceptionMsg });
                LOGGER.warn(message);
                initError(message);
            }
        }
        return bundleDir;
    }

    /**
     * Modifie les param�tres communs d'un projet wsad ou rsa
     * @param pParams les param�tres
     * @param pProject le projet
     * @param pDirName le chemin du workspace
     */
    private void setProjectParams(MapParameterBO pParams, JWSADProject pProject, String pDirName) {
        // Ajout du listener
        pProject.setListener(this);
        /* on affecte les attributs */
        pProject.setName((new JWSADDotProjectParser(((String) mData.getData(TaskData.VIEW_PATH)) + pDirName)).retrieveName());
        pProject.setJavaVersion(((StringParameterBO) pParams.getParameters().get(ParametersConstants.DIALECT)).getValue());
        String viewPath = (String) mData.getData(TaskData.VIEW_PATH) + "/";
        String destDir = viewPath.replaceAll("//", "/");
        pProject.setPath((destDir + pDirName + "/").replaceAll("//", "/"));

        destDir += mConfiguration.getDestDir();

        pProject.setDestPath(destDir);
        this.modifyClassesDirInHashMap(destDir);
        // Prise en compte des r�pertoires exclus 
        addExcludedDirs(pProject, (ListParameterBO) pParams.getParameters().get(ParametersConstants.EXCLUDED_DIRS), viewPath);
        // On ajoute le param�tre relatif � la RAM d�di�e qui 
        // sera utilis�e lors de la compilation du projet
        pProject.setRequiredMemory(mConfiguration.getRequiredMemory());
        // Le classpath de l'API java a utiliser
        pProject.setBootClasspath(mConfiguration.getBootclasspath(pProject.getJavaVersion()));
    }

    /**
     * @param pProject le projet
     * @param pExcludedDirs les r�pertoires � exclure
     * @param pViewPath le chemin de la vue
     */
    private void addExcludedDirs(JWSADProject pProject, ListParameterBO pExcludedDirs, String pViewPath) {
        if (null != pExcludedDirs) {
            /* on ajoute au projet la liste des r�pertoires 
             * � exclure */
            for (Iterator it = pExcludedDirs.getParameters().iterator(); it.hasNext();) {
                // On v�rifie que le r�pertoire appartient bien au projet
                String currentEx = (pViewPath + ((StringParameterBO) it.next()).getValue()).replaceAll("//", "/");
                if (currentEx.matches(pProject.getPath() + ".*")) {
                    pProject.addToExcludeDirs(currentEx);
                }
            }
        }

    }

    /**
     * Cette m�thode cr�e un composant XML.
     * @param pAntMap le MapParameterBO contenant les 
     * informations n�cessaires � la cr�ation du composant.
     * @throws Exception exception.
     */
    private void createXMLComponent(MapParameterBO pAntMap) throws Exception {

        /* on cr�e un bean project XML */
        JXMLProject project = new JXMLProject();
        StringParameterBO buildFile = (StringParameterBO) pAntMap.getParameters().get(ParametersConstants.ANT_BUILD_FILE);
        StringParameterBO antTarget = (StringParameterBO) pAntMap.getParameters().get(ParametersConstants.ANT_TARGET);

        if (buildFile == null) {
            /* on lance une exception en rapport */
            throw new Exception(CompilingMessages.getString("java.exception.task.malformed_xml_project_in_hashmap"));
        }
        project.setPath(makePath(buildFile.getValue()));
        if (antTarget != null) {
            String targ = antTarget.getValue();
            project.setTarget(targ);
        }

        // Ajout du listener
        project.setListener(this);
        /* on cr�e le composant */
        JXMLCompilerImpl jxci = new JXMLCompilerImpl(project);
        JComponent jc = new JComponent(jxci);

        /* et on l'ajoute � la liste */
        addToComponents(jc);
    }

    /**
     * M�thode d'ajout d'un <code>JComponent</code> � la liste 
     * <code>mComponents</code>.
     * @param pComponent <code>JComponent</code> � ajouter.
     */
    private void addToComponents(JComponent pComponent) {
        mComponents.add(pComponent);
    }

    /**
     * Cette m�thode reforme une chemin complet
     * @param pName : * WSAD = nom du r�pertoire contenant le projet /
     * *XML : chemin vers le fichier XML.
     * @return le chemin compl�t�.
     * @throws Exception exception lors du traitement.
     */
    private String makePath(String pName) throws Exception {
        /* on r�cup�re le view_path depuis la hashmap */
        String sTmp = (String) getData().getData(TaskData.VIEW_PATH);

        /* on cr�e le buffer */
        StringBuffer value = new StringBuffer(sTmp);

        /* si il manque le "/" */
        if (!sTmp.endsWith("/") && !pName.startsWith("/")) {
            /* alors on l'ajoute */
            value.append(JCompilingConfiguration.UNIX_SEPARATOR);
        }

        /* on ajoute le pName */
        value.append(pName);

        /* si le source_path ne se termine pas par un "/" */
        if (!pName.endsWith("/")) {
            /* alors on l'ajoute */
            value.append(JCompilingConfiguration.UNIX_SEPARATOR);
        }

        return value.toString();
    }

    /**
     * Cette m�thode est utilis�e par la m�thode 
     * <code>modifyClasspathInHashMap()</code> afin d'�viter les 
     * doublons dans la cha�ne de caract�res contenant le classpath.
     * @param pString1 1�re cha�ne � tester.
     * @param pString2 2nde cha�ne � tester.
     * @return le r�sultat du test.
     * @throws Exception exception lors du traitement.
     * @see #modifyClasspathInHashMap()
     */
    private String generateProperClasspath(String pString1, String pString2) throws Exception {
        /* CODE HISTORIQUE : � modifier en utilisant un HashSet */

        StringBuffer result = new StringBuffer();

        /* si l'une et / ou l'autre des cha�nes fournies est nulle*/
        if (null == pString1) {
            /* alors on l'affecte vide */
            pString1 = "";
        }
        if (null == pString2) {
            /* alors on l'affecte vide */
            pString2 = "";
        }

        /* on cr�e une 1�re liste en cassant la 1�re cha�ne selon 
         * le s�parateur de classpath */
        ArrayList list1 = new ArrayList((List) Arrays.asList((String[]) (pString1.split(mConfiguration.getClasspathSeparator()))));

        /* on cr�e une 2nde liste en cassant la 2nde cha�ne selon 
        * le s�parateur de classpath */
        ArrayList list2 = new ArrayList((List) Arrays.asList((String[]) (pString2.split(mConfiguration.getClasspathSeparator()))));

        /* si aucune des listes n'est vide */
        if (null != list1 && null != list2) {
            int i = 0;

            /* tant que l'on a pas atteint l'extr�mit� de la 2nde liste */
            while (i < list2.size()) {
                /* si la premi�re liste ne contient pas l'�l�ment courant 
                 * de la seconde */
                if (!list1.contains(list2.get(i))) {
                    /* alors cet �l�ment est ajout� � la 1�re liste */
                    list1.add(list2.get(i));
                }
                i++;
            }

            i = 0;

            /* tant que l'on a pas atteint l'extr�mit� de la 1�re liste */
            while (i < list1.size()) {
                /* si l'�l�ment courant n'est pas vide */
                if (!"".equals(list1.get(i))) {
                    /* alors il est ajout� au buffer */
                    result.append(list1.get(i));
                    /* on ajoute le s�parateur de classpath */
                    result.append(mConfiguration.getClasspathSeparator());
                }
                i++;
            }
        }

        /* on fait le m�nage */
        list1 = null;
        list2 = null;

        /* si le buffer se r�sume � un s�parateur de classpath */
        if (mConfiguration.getClasspathSeparator().equals(result.toString())) {
            /* alors on instance un nouveau buffer vierge */
            result = new StringBuffer();
        }

        /* on retourne le buffer */
        return result.toString();
    }

    /**
     * Cette m�thode affiche des param�tres en mode DEBUG uniquement.
     */
    private void printParameters() {
        LOGGER.debug("Project: " + mProject.getName());
        LOGGER.debug("ClassPath:" + (String) (getData().getData(TaskData.CLASSPATH)));
    }

    /** (non-Javadoc)
     * @see org.apache.tools.ant.BuildListener#buildStarted(org.apache.tools.ant.BuildEvent)
     */
    public void buildStarted(BuildEvent pEvent) {
        log(pEvent);
    }

    /** (non-Javadoc)
     * @see org.apache.tools.ant.BuildListener#buildFinished(org.apache.tools.ant.BuildEvent)
     */
    public void buildFinished(BuildEvent pEvent) {
        log(pEvent);
    }

    /** (non-Javadoc)
     * @see org.apache.tools.ant.BuildListener#targetStarted(org.apache.tools.ant.BuildEvent)
     */
    public void targetStarted(BuildEvent pEvent) {
        log(pEvent);
    }

    /** (non-Javadoc)
     * @see org.apache.tools.ant.BuildListener#targetFinished(org.apache.tools.ant.BuildEvent)
     */
    public void targetFinished(BuildEvent pEvent) {
        log(pEvent);
    }

    /** (non-Javadoc)
     * @see org.apache.tools.ant.BuildListener#taskStarted(org.apache.tools.ant.BuildEvent)
     */
    public void taskStarted(BuildEvent pEvent) {
        log(pEvent);
    }

    /** (non-Javadoc)
     * @see org.apache.tools.ant.BuildListener#taskFinished(org.apache.tools.ant.BuildEvent)
     */
    public void taskFinished(BuildEvent pEvent) {
        log(pEvent);
    }

    /** (non-Javadoc)
     * @see org.apache.tools.ant.BuildListener#messageLogged(org.apache.tools.ant.BuildEvent)
     */
    public void messageLogged(BuildEvent pEvent) {
        log(pEvent);
    }

    /**
     * Log JRAF
     * L'�v�nement ANT est redirig� vers le log JRAF
     * @param pEvent �v�nement ANT
     */
    private void log(BuildEvent pEvent) {
        // On adapte les logs en fonction de la gravit� du log initial
        String message = pEvent.getMessage();
        Throwable exception = pEvent.getException();
        // Utilisation d'un switch car on ne peut pas
        // se passer du mapping entre en entier et une m�thode
        // � appeler : on pourrait m�moriser dans une map
        // la m�thode et l'entier mais cel� est un peu lourd
        switch (pEvent.getPriority()) {
            case Project.MSG_ERR :
                LOGGER.error(message, exception);
                break;

            case Project.MSG_WARN :
                LOGGER.warn(message, exception);
                manageCompilError(message);
                break;

            case Project.MSG_INFO :
                LOGGER.info(message, exception);
                break;

            case Project.MSG_VERBOSE :
                LOGGER.trace(message, exception);
                break;

            case Project.MSG_DEBUG :
                LOGGER.debug(message, exception);
                break;

            default :
                // Par d�faut on log sur le flux de warning
                LOGGER.warn(message, exception);
                // par d�faut
                manageCompilError(message);
                break;
        }
    }

    /**
     * g�re la cr�ation des erreurs de compilation
     * @param pMessage le message d'erreur
     */
    private void manageCompilError(String pMessage) {
        // petit filtrage
        if (pMessage.indexOf("see the compiler error output") == -1) {
            // rajoute "\n" pour formater l'affichage de la jsp
            mCompilErrorMessage += pMessage + "\n";
        }
        // on ne stocke que les erreurs de compil,pas les infos
        if (pMessage.indexOf("^") != -1) {
            // on enl�ve le chemin de la vue qui n'est pas une information n�cessaire
            mCompilErrorMessage = mCompilErrorMessage.replaceAll((String) mData.getData(TaskData.VIEW_PATH), "");
            // d�termine le niveau de criticit�
            if (mCompilErrorMessage.toLowerCase().indexOf("warning") == -1) {
                initError(mCompilErrorMessage, ErrorBO.CRITICITY_FATAL);
            } else {
                initError(mCompilErrorMessage, ErrorBO.CRITICITY_WARNING);
            }
            // reset
            mCompilErrorMessage = "";
        } else { // pb de classpath sur les chargements de jar
            if (pMessage.indexOf(".jar") != -1) {
                //  on enl�ve le chemin de la vue qui n'est pas une information n�cessaire
                mCompilErrorMessage = mCompilErrorMessage.replaceAll((String) mData.getData(TaskData.VIEW_PATH), "");
                // envoie du message niveau warning
                initError(mCompilErrorMessage, ErrorBO.CRITICITY_WARNING);
                // reset
                mCompilErrorMessage = "";
            }
        }
    }
}