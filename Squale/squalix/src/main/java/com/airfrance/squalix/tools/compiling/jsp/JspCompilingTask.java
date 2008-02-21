package com.airfrance.squalix.tools.compiling.jsp;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Project;

import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ListParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.StringParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.ErrorBO;
import com.airfrance.squalix.core.AbstractTask;
import com.airfrance.squalix.core.TaskData;
import com.airfrance.squalix.core.TaskException;
import com.airfrance.squalix.core.exception.ConfigurationException;
import com.airfrance.squalix.tools.compiling.CompilingMessages;
import com.airfrance.squalix.tools.compiling.java.beans.JWSADProject;
import com.airfrance.squalix.tools.compiling.java.compiler.wsad.JWSADAntCompiler;
import com.airfrance.squalix.tools.compiling.jsp.bean.J2eeWSADProject;
import com.airfrance.squalix.tools.compiling.jsp.configuration.JspCompilingConfiguration;
import com.airfrance.squalix.tools.compiling.jsp.wsad.AbstractTomcatCompiler;
import com.airfrance.squalix.tools.compiling.jsp.wsad.TomcatCompilerFactory;
import com.airfrance.squalix.util.buildpath.BuildProjectPath;
import com.airfrance.squalix.util.file.FileUtility;

/**
 * T�che de compilation des JSPs.
 * La t�che de compilation Java doit avoir �t� effectu�e au pr�alable
 * afin d'avoir le classpath correctement form� et accessible via le param�tre
 * temporaire <code>CLASSPATH</code>
 */
public class JspCompilingTask extends AbstractTask implements BuildListener {

    /**
     * Logger
     */
    private static final Log LOGGER = LogFactory.getLog(JspCompilingTask.class);

    /** Le message des erreurs de compilation */
    private String compileErrorMessage;

    /** pour savoir si on compile les .java */
    private boolean isJavaComplation;

    /**
     * Constructeur par d�faut.
     */
    public JspCompilingTask() {
        mName = "JspCompilingTask";
        compileErrorMessage = "";
        isJavaComplation = false;
    }

    /** 
     * {@inheritDoc}
     * @see com.airfrance.squalix.core.AbstractTask#execute()
     */
    public void execute() throws TaskException {
        // On r�cup�re le classpath dans les param�tres temporaires
        String classpath = (String) mData.getData(TaskData.CLASSPATH);
        if (null == classpath) {
            // On lance une exception de configuration car cette variable
            // doit exister
            String message = CompilingMessages.getString("jsp.exception.classpath_not_found");
            throw new TaskException(message);
        }
        // On cr�e le projet J2eeWSADProject associ�
        J2eeWSADProject j2eeProject = new J2eeWSADProject();
        j2eeProject.setName(mProject.getName());
        j2eeProject.setClasspath(classpath);
        j2eeProject.setListener(this);
        String viewPath = (String) mData.getData(TaskData.VIEW_PATH);
        if (null == viewPath) {
            // On lance une exception de configuration car cette variable
            // doit exister
            String message = CompilingMessages.getString("jsp.exception.view_path_not_found");
            throw new TaskException(message);
        }
        if (!viewPath.endsWith("/")) {
            viewPath += "/";
        }
        if (isConfigurationSetting()) {
            try {
                String servletVersion = setJ2eeWSADProject(j2eeProject, viewPath);
                // On lance le compilateur
                AbstractTomcatCompiler compiler = new TomcatCompilerFactory().createCompiler(j2eeProject, servletVersion);
                compiler.compileJsp();
            } catch (Exception be) {
                throw new TaskException(be);
            }
            // on modifie les param�tres temporaires pour ajouter le r�pertoire
            // des .java des JSPs qui viennent d'�tre compil�es
            mData.putData(TaskData.JSP_TO_JAVA_DIR, j2eeProject.getJspDestPath());
            compileJava(viewPath, j2eeProject);
        }
    }

    /**
     * 1. V�rifie que les sources vers les JSPs existent et sont bien des r�pertoires racines de pages JSPs
     * (ie. a des .jsp � la racine ou � la racine de ses r�pertoires)
     * 2. Construit la liste des jsps � compiler sous forme de map. 
     * La map est de la taille de la liste donn�e dans les param�tres du projet et chaque entr�e correspond
     * contient la liste des fichiers JSPs que le r�pertoire de m�me index contient.
     * Ex : ParametersConstants.JSP = {path1, path2} alors le r�sultat sera {{*.jsp dans path1}, {*.jsp dans path2}}
     * PRE-CONDITION : le param�tre du projet <code>ParametersConstants.JSP</code> n'est pas nul.
     * 
     * @param viewPath le chemin de la vue
     * @param absoluteJspPaths la liste des chemins absolus vers les r�pertoires sources JSP
     * @return la liste de liste des jsps � compiler de chaque r�pertoire source JSP
     */
    private Object[][] verifyAndBuildJspSrc(String viewPath, List absoluteJspPaths) {
        ListParameterBO excludedDirs = (ListParameterBO) mProject.getParameter(ParametersConstants.JSP_EXCLUDED_DIRS);
        Object[][] result = new Object[absoluteJspPaths.size()][J2eeWSADProject.NB_ID];
        File curJspDir;
        File[] jspsList;
        List path = new ArrayList(1);
        for (int i = 0; i < absoluteJspPaths.size(); i++) {
            curJspDir = new File((String) absoluteJspPaths.get(i));
            if (curJspDir.exists()) {
                // On regarde si le r�pertoire contient des .jsp ou des r�pertoires contenant des .jsps
                if (!isJspRoot(curJspDir)) {
                    // Il ne s"agit pas d'un r�pertoire racine de JSP
                    String message = CompilingMessages.getString("jsp.exception.no_jsp_root", curJspDir.getAbsolutePath().replace('\\', '/').replaceFirst(viewPath, ""));
                    // On affiche un warning sans lancer d'exception car la t�che va quand m�me �tre ex�cut�e
                    initError(message);
                    LOGGER.warn(message);
                }
                // On ajoute les fichiers de ce r�pertoire � traiter sans prendre en compte les r�pertoires
                // exclus
                path.clear();
                path.add(curJspDir.getAbsolutePath().replace('\\', '/'));
                result[i][J2eeWSADProject.DIR_ID] = curJspDir.getAbsolutePath();
                result[i][J2eeWSADProject.JSP_LIST_ID] = FileUtility.getIncludedFiles(viewPath, path, null, null, excludedDirs, new String[] { ".jsp" });
            } else {
                String message = CompilingMessages.getString("jsp.exception.src_not_found", curJspDir.getAbsolutePath().replace('\\', '/').replaceFirst(viewPath, ""));
                // On affiche un warning sans lancer d'exception car la t�che peut quand m�me �tre ex�cut�e
                initError(message);
                LOGGER.warn(message);
                // On ajoute une liste vide dans la liste des sources pour respecter l'ordre des indexes
                result[i][J2eeWSADProject.DIR_ID] = curJspDir.getAbsolutePath();
                result[i][J2eeWSADProject.JSP_LIST_ID] = new ArrayList(0);
            }
        }
        return result;
    }

    /**
     * @param curJspDir le r�pertoire courant des Jsps � v�rifier
     * @return true si le r�pertoire contient des .jsp � la racine ou
     * � la racine de ses r�pertoires.
     */
    private boolean isJspRoot(File curJspDir) {
        boolean result = false;
        if (curJspDir.isDirectory()) {
            // On liste les fichiers et r�pertoires de premier niveau
            File[] files = curJspDir.listFiles();
            for (int i = 0; i < files.length && !result; i++) {
                result = files[i].isFile() && files[i].getName().endsWith(".jsp");
            }
        } else if (curJspDir.getName().endsWith(".jsp")) {
            result = true;
        }
        return result;
    }

    /**
     * @return true si la t�che a �t� correctement configur�e
     */
    private boolean isConfigurationSetting() {
        // On r�cup�re la version servlet utilis�e
        StringParameterBO webapp = (StringParameterBO) mProject.getParameter(ParametersConstants.WEB_APP);
        if (webapp == null) {
            // erreur de configuration
            String message = CompilingMessages.getString("jsp.exception.webb_app_not_set");
            // On affiche un warning sans lancer d'exception, la t�che ne sera pas ex�cut�e.
            initError(message);
            LOGGER.warn(message);
            // Les param�tres sont mal configur�s, on annule la t�che
            mStatus = CANCELLED;
        }
        // On r�cup�re la version j2ee utilis�e
        StringParameterBO j2eeScr = (StringParameterBO) mProject.getParameter(ParametersConstants.J2EE_VERSION);
        if (j2eeScr == null) {
            // erreur de configuration
            String message = CompilingMessages.getString("jsp.exception.servlet_version_not_set");
            // On affiche un warning sans lancer d'exception, la t�che ne sera pas ex�cut�e.
            initError(message);
            LOGGER.warn(message);
            // Les param�tres sont mal configur�s, on annule la t�che
            mStatus = CANCELLED;
        }
        // On r�cup�re la liste des chemins vers les jsps du projet
        ListParameterBO jspsList = (ListParameterBO) mProject.getParameter(ParametersConstants.JSP);
        if (null == jspsList) {
            // erreur de configuration
            String message = CompilingMessages.getString("jsp.exception.sources_not_found");
            // On affiche un warning sans lancer d'exception, la t�che ne sera pas ex�cut�e.
            initError(message);
            LOGGER.warn(message);
            // Les param�tres sont mal configur�s, on annule la t�che
            mStatus = CANCELLED;
        }
        return mStatus != CANCELLED;
    }

    /**
     * Modifie les param�tres du projet wsad
     * PRECONDITION : Les param�tres du projet ont �t� v�rifi�s et ne sont pas nuls.
     * @param j2eeProject le projet wsad
     * @param viewPath le viewpath
     * @return les sp�cifications servlet
     * @throws TaskException si erreur
     */
    private String setJ2eeWSADProject(J2eeWSADProject j2eeProject, String viewPath) throws TaskException {
        // Version des sources java
        j2eeProject.setJavaVersion(((StringParameterBO) mProject.getParameter(ParametersConstants.DIALECT)).getValue());
        // On r�cup�re la version j2ee utilis�e
        StringParameterBO j2eeScr = (StringParameterBO) mProject.getParameter(ParametersConstants.J2EE_VERSION);
        String j2eeVersion = j2eeScr.getValue();
        // On r�cup�re la liste des chemins vers les jsps du projet
        ListParameterBO jspsList = (ListParameterBO) mProject.getParameter(ParametersConstants.JSP);

        // On construit le chemin vers les jsps en ne r�cup�rant que les fichiers � compiler
        // (on prend en compte les r�pertoires exclus de la compilation)
        List jsps = BuildProjectPath.buildProjectPath(viewPath, jspsList.getParameters());
        j2eeProject.setJspPaths(verifyAndBuildJspSrc(viewPath, jsps));

        // On r�cup�re le r�pertoire racine de l'application dans les param�tres du projet
        String webAppPath = ((StringParameterBO) mProject.getParameter(ParametersConstants.WEB_APP)).getValue();
        File webApp = FileUtility.getAbsoluteFile(viewPath, new File(webAppPath));
        if (null == webApp) {
            // On lance une erreur de configuration
            LOGGER.warn("Web application directory not found for project " + mProject.getName());
            throw new TaskException("Web application directory not found : " + webAppPath);
        }
        j2eeProject.setPath(webApp.getAbsolutePath());
        j2eeProject.setJspDestPath(J2eeWSADProject.JAVA_DEST);
        // On affecte le r�pertoire de destination
        File destDir = new File(viewPath + j2eeProject.getJspDestPath());
        if (!destDir.exists()) {
            // On le cr�e
            destDir.mkdirs();
        }
        j2eeProject.setJspDestPath(destDir.getAbsolutePath());
        // On r�cup�re le r�pertoire contenant les jars n�cessaires � la compilation
        // des jsps pour l'ajouter au classpath
        JspCompilingConfiguration conf = new JspCompilingConfiguration();
        try {
            conf.parse(new FileInputStream("config/jspcompiling-config.xml"));
            // La version des servlets est de type 2.4, le nom du r�pertoire contenant les jars
            // et de la forme 2_4 donc on remplace le '.' par '_'
            File dir = new File(conf.getJarDirectory() + "/" + j2eeVersion.replace('.', '_'));
            if (!dir.exists() && !dir.isDirectory()) {
                String message = CompilingMessages.getString("jsp.exception.jar_dir_not_found", new Object[] { conf.getJarDirectory()});
                throw new TaskException(new ConfigurationException(message));
            }
            j2eeProject.addJarDirToClasspath(dir);
            // On copie notre log4j.properties dans le WEB-INF/classes pour avoir une bonne configuration
            // des logs
            File log4j = new File("config/log4j.properties");
            if (log4j.exists()) {
                FileUtility.copyInto(log4j, new File(j2eeProject.getPath(), "WEB-INF/classes"));
            }
            LOGGER.debug("Classpath pour la compilation des JSPs : " + j2eeProject.getClasspath());
        } catch (Exception be) {
            throw new TaskException(be);
        }
        return j2eeVersion;
    }

    /**
     * Compile les .java des JSPs
     * @param viewPath le chemin de la vue
     * @param j2eeProject le projet J2EE
     * @throws TaskException si erreur de compilation
     */
    private void compileJava(String viewPath, J2eeWSADProject j2eeProject) throws TaskException {
        try {
            JWSADProject jwsadProject = new JWSADProject();
            // l'�couteur est celui de la compilation Java
            jwsadProject.setListener(j2eeProject.getListener());
            jwsadProject.setRequiredMemory("1024m");
            // On indique le r�pertoire qui va recevoir les .class
            File dest = new File(viewPath + J2eeWSADProject.CLASSES_DEST);
            // On le cr�e
            dest.mkdirs();
            jwsadProject.setDestPath(dest.getAbsolutePath());
            // On r�cup�re les r�pertoires des .class (hors jsp) du projet pour l'ajouter au classpath
            List classesDirs = (List) mData.getData(TaskData.CLASSES_DIRS);
            for(int i=0; i<classesDirs.size(); i++) {
                jwsadProject.setClasspath(j2eeProject.getClasspath() + ";" + (String)classesDirs.get(i));
            }
            jwsadProject.setSrcPath(j2eeProject.getJspDestPath());
            jwsadProject.setJavaVersion(((StringParameterBO) mProject.getParameter(ParametersConstants.DIALECT)).getValue());
            JWSADAntCompiler compiler = new JWSADAntCompiler(jwsadProject);
            isJavaComplation = true;
            compiler.doCompilation();
            mData.putData(TaskData.JSP_CLASSES_DIR, jwsadProject.getDestPath());
            StringBuffer classpath = new StringBuffer((String) mData.getData(TaskData.CLASSPATH));
            // On ajoute le r�pertoire des .class jsp au classpath pour mccabe
            if (classpath.length() > 0 && !classpath.toString().endsWith(";")) {
                classpath.append(";");
            }
            classpath.append(jwsadProject.getDestPath());
            mData.putData(TaskData.CLASSPATH, classpath.toString());
            affectFileSystemSize(mData.getData(TaskData.JSP_CLASSES_DIR), true);
        } catch (Exception e) {
            throw new TaskException(e);
        }
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
        // TODO : Trouver comment configurer Tomcat
        // en attendant on filtre les logs log4j
        // On adapte les logs en fonction de la gravit� du log initial
        String message = pEvent.getMessage();
        if (message != null && !message.startsWith("log4j")) {
            Throwable exception = pEvent.getException();
            // Utilisation d'un switch car on ne peut pas
            // se passer du mapping entre en entier et une m�thode
            // � appeler : on pourrait m�moriser dans une map
            // la m�thode et l'entier mais cel� est un peu lourd
            switch (pEvent.getPriority()) {
                case Project.MSG_ERR :
                    LOGGER.error(message, exception);
                    manageCompilError(message, ErrorBO.CRITICITY_FATAL);
                    break;

                case Project.MSG_WARN :
                    LOGGER.warn(message, exception);
                    manageCompilError(message, ErrorBO.CRITICITY_WARNING);
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
                    manageCompilError(message, ErrorBO.CRITICITY_WARNING);
                    break;
            }
        }
    }

    /**
     * g�re la cr�ation des erreurs de compilation
     * @param pMessage le message d'erreur
     * @param pLevel le niveau d'erreur
     */
    private void manageCompilError(String pMessage, String pLevel) {
        if (isJavaComplation) { // TODO : sortir le code de JavaCompilingTask pour le factoriser avec celui-ci
            // Filtrage Java
            if (pMessage.indexOf("see the compiler error output") == -1) {
                // rajoute "\n" pour formater l'affichage de la jsp
                compileErrorMessage += pMessage + "\n";
            }
            // on ne stocke que les erreurs de compil,pas les infos
            if (pMessage.indexOf("^") != -1) {
                // on enl�ve le chemin de la vue qui n'est pas une information n�cessaire
                compileErrorMessage = compileErrorMessage.replaceAll((String) mData.getData(TaskData.VIEW_PATH), "");
                // d�termine le niveau de criticit�
                if (compileErrorMessage.toLowerCase().indexOf("warning") == -1) {
                    initError(compileErrorMessage, ErrorBO.CRITICITY_FATAL);
                } else {
                    initError(compileErrorMessage, ErrorBO.CRITICITY_WARNING);
                }
                // reset
                compileErrorMessage = "";
            } else { // pb de classpath sur les chargements de jar
                if (pMessage.indexOf(".jar") != -1) {
                    //  on enl�ve le chemin de la vue qui n'est pas une information n�cessaire
                    compileErrorMessage = compileErrorMessage.replaceAll((String) mData.getData(TaskData.VIEW_PATH), "");
                    // envoie du message niveau warning
                    initError(compileErrorMessage, ErrorBO.CRITICITY_WARNING);
                    // reset
                    compileErrorMessage = "";
                }
            }
        } else { // Filtrage JSP
            // on enl�ve le chemin de la vue qui n'est pas une information n�cessaire
            compileErrorMessage = pMessage.replaceAll((String) mData.getData(TaskData.VIEW_PATH), "");
            initError(compileErrorMessage, pLevel);
            compileErrorMessage = "";
        }
    }
}
