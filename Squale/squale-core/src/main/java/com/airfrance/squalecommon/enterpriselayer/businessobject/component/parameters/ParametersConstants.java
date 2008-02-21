package com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters;

/**
 * Les param�tres des projets
 */
public class ParametersConstants {

    // *********************** Projet JAVA *********************************

    // constantes d�finissant le dialect
    /** version 1.3 */
    public final static String JAVA1_3 = "1.3";

    /** version 1.4 */
    public final static String JAVA1_4 = "1.4";

    /** version 1.5 */
    public final static String JAVA1_5 = "1.5";
    
    // Les param�tres obligatoires:

    /** Le dialecte du language */
    public final static String DIALECT = "dialect";
    
    // Un projet java doit avoir le param�tre ant OU le param�tre WSAD

    // Cas d'un projet ant
    /** Projet ANT */
    public final static String ANT = "ant";
    
    /** Localisation ant du build.xml */
    public final static String ANT_BUILD_FILE = "buildfile";

    /** Nom de la target ant */
    public final static String ANT_TARGET = "target";
    
    // Cas d'un projet wsad
    /** Pour r�cup�rer les projets WSAD*/
    public final static String WSAD = "wsad";
    /** Pour r�cup�rer les param�tres des projets wsad */
    public final static String WSAD_PROJECT_PARAM = "wsadProjectsParams";
    /** Indique le chemin vers le manifest */
    public final static String MANIFEST_PATH = "manifestPath";
    
    // Cas d'un projet rsa
    /** Pour r�cup�rer les projets RSA */
    public final static String RSA = "rsa";
    /** L'index indiquant le chemin vers le workspace RSA */
    public final static int WORKSPACE_ID = 0;
    /** L'index indiquant le nom du projet ear si il y en a un */
    public final static int EAR_NAME_ID = 1;
    /** L'index indiquant le chemin vers le manifest */
    public final static int MANIFEST_PATH_ID = 2;

    
    
    
    // Les param�tres facultatifs:

    /** Le chemin vers le bundle eclipse */
    public final static String BUNDLE_PATH = "bundlePath"; 
    
    /** Les param�tres eclipse */
    public final static String ECLIPSE = "eclipseParameters";

    /** Les variables eclipse */
    public final static String ECLIPSE_VARS = "eclipseVars"; 

    /** Les librairies utilisateur eclipse */
    public final static String ECLIPSE_LIBS = "eclipseLibs"; 

    /** Les r�pertoire exclus de la compilation */
    public final static String EXCLUDED_DIRS = "excludedDirs"; 
    
    /** Les r�pertoire exclus de la compilation des JSP */
    public final static String JSP_EXCLUDED_DIRS = "jspExcludedDirs"; 

    /** Les patterns � exclure */
    public final static String EXCLUDED_PATTERNS = "excludedPatterns"; 

    /** Les patterns � inclure */
    public final static String INCLUDED_PATTERNS = "includedPatterns"; 
    
    // Pour l'administrateur uniquement
    /** Le type de compilation */
    public final static String ECLIPSE_COMPILATION = "eclipseCompilation";

    // ************** Tache Clearcase ***************************************

    /** Pour r�cup�rer les infos ClearCase */
    public final static String CLEARCASE = "clearcase";
    
    // Les param�tres obligatoires:
    
    /** Pour r�cup�rer la liste des projets */
    public final static String APPLI = "appli";
    
    /** La branche ClearCase */
    public final static String BRANCH = "branch";

    /** Les vobs ClearCase*/
    public final static String VOBS = "vobs";

    // *************** Projet C++ ******************************************

    /** Les informations C++ */
    public final static String CPP = "cpp";
    
    // constantes d�finissant le dialect pour McCabe
    /** SUNWS_5X */
    public final static String SUNWS_5X = "Sunws_5x";
    /** SUNWS_5X */
    public final static String FORTE = "Forte";
    
    /** Script de compilation C++ */
    public final static String CPP_SCRIPTFILE = "scriptFile";
    
    // ********************* T�che CppTest **********************************
    /** Informations Cpptest */
    public final static String CPPTEST="cpptest";
    /** Liste des fichiers projet CppTest */
    public final static String CPPTEST_SCRIPT = "script";
    /** Nom du set de r�gles CppTest */
    public final static String CPPTEST_RULESET_NAME = "rulesetname";
    
    // ********************* T�che checkstyle *****************************
    /** Nom du set de r�gles Checkstyle */
    public final static String CHECKSTYLE_RULESET_NAME = "rulesetname";
    
    // ********************* T�che PMD ************************************
    /** Informations PMD */
    public final static String PMD="pmd";
    /** Nom du jeu de r�gles PMD pour java */
    public final static String PMD_JAVA_RULESET_NAME = "javarulesetname";
    /** Nom du jeu de r�gles PMD pour jsp */
    public final static String PMD_JSP_RULESET_NAME = "jsprulesetname";

    // ********************* T�che McCabe **********************************
    
    // Les param�tres obligatoires:

    /** Le chemin des sources */
    public final static String SOURCES = "sources";
    
    /** Le chemin des pages JSP dans le cas d'un profil J2EE */
    public final static String JSP = "jsp"; 
    /** Chemin vers le r�pertoire d'application web */
    public final static String WEB_APP = "webApp"; 
    /** Version du j2ee */
    public final static String J2EE_VERSION = "j2ee_version"; 
    // constantes d�finissant les versions
    /** version 2.2 */
    public final static String J2EE1_2 = "1.2";
    /** version 2.3 */
    public final static String J2EE1_3 = "1.3";
    /** version 2.4 */
    public final static String J2EE1_4 = "1.4";
    /** version 2.5 */
    public final static String J2EE1_5 = "1.5";
    /** tableau r�capitulatif des versions disponibles */
    public final static String[] J2EE_VERSIONS = new String[]{J2EE1_2, J2EE1_3, J2EE1_4, J2EE1_5};

    //********************* T�che Macker **********************************
    /** Information Macker */
    public final static String MACKER = "macker";
    
    /** L'emplacement du fichier de configuration */
    public final static String MACKER_CONFIGURATION = "configurationFile";
    
    //********************* T�che UMLQuality **********************************
    
    /** Informations UMLQuality */
    public final static String UMLQUALITY = "umlquality";
    
    /** L'emplacement du fichier xmi � analyser */
    public final static String UMLQUALITY_SOURCE_XMI = "xmi";
    
    // Les param�tres facultatifs:
    
     /** Les classes � exclure */
     public final static String MODEL_EXCLUDED_CLASSES = "modelExcludedClasses";
    
    //********************* T�che d'analyse du code source **********************************
    /** Informations analyser */
    public final static String ANALYSER = "analyser";
    /** Chemin vers l'arborescence des fichiers � analyser */
    public final static String PATH = "path";

    //********************* T�che de compilation dans le cas de projets d�j� compil�s *******
    /** Informations sur la compilation des projets d�j� compil�s */
    public final static String COMPILED = "compiled";
    /** Emplacement des sources compil�es */
    public final static String COMPILED_SOURCES_DIRS = "compiledSourcesDirs";
    /** Le classpath dans le cas d'un projet java */
    public final static String CLASSPATH = "classpath";

    
    
    
}