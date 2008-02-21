//Source file: D:\\cc_views\\squale_v0_0_act\\squale\\src\\squalix\\src\\com\\airfrance\\squalix\\util\\csv\\CSVParser.java

package com.airfrance.squalix.util.csv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

/**
 * Permet de parser un fichier CSV (comma separated values) et de le mapper avec un Bean selon 
 * un configuration.<br>
 * Les beans � mapper doivent impl�menter l'interface <code>CSVBean</code> et poss�der les 
 * setters publics ad�quats.<br><br>
 * Les d�pendances avec le logger JRAF et la classe 
 * com.airfrance.squalix.configurationmanager.ConfigUtility doivent �tre r�solues.
 * <br>
 * Le fichier de mapping (ici csv-mapping.xml) :<br>
 * <code>
 * &nbsp;&nbsp;&lt;templates><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;template name="method"><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;csvbean>test.MonBean&lt;/csvbean><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;header size="2" /><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;footer size="3" /><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;field name="name" type="java.lang.String" column="0">&lt;/field><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;field name="misc" type="java.lang.String" column="1">&lt;/field><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;field name="val1" type="java.lang.Integer" column="2">&lt;/field><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;field name="val2" type="java.lang.Double" column="3">&lt;/field><br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;/template><br>
 * &nbsp;&nbsp;&lt;/templates><br>
 * </code>
 * 
 * Pour chaque template, sp�cifier :<br>
 * <ul>
 * <li>son nom avec l'attribut name</li>
 * <li>le nom de la classe correspondante avec l'�lement csvbean</li>
 * <li>le nombre de lignes de l'en-t�te</li>
 * <li>le nombre de lignes du pied de page</li>
 * <li>chaque attribut avec pour chacun :
 * <ul>
 * <li>son nom (attribut name) (si la valeur de cet attribut est monNom, le setter recherch� est alors setMonNom)</li>
 * <li>son type objet (pas de type simple) qui accepte un constructeur avec une <code>String</code> en param�tre</li>
 * <li>le num�ro de la colonne correspondante dans le fichier CSV (� partir de 0)</li>
 * </ul>
 * </li>
 * </ul> 
 * <br><br>
 * Les classes mapp�es doivent poss�der les setters associ�s aux
 * attributs mapp�s sur le CSV.<br>
 * Exemple : si la classe poss�de un attribut nomm� <code>name</code>, elle devra 
 * impl�menter une m�thode publique nomm�e <code>setName</code>.<br>
 * D'autre part les param�tres des setters ne peuvent �tre des types simples. Ils 
 * doivent �tre des objets qui poss�dent un constructeur prenant un seul param�tre 
 * <code>String</code>. 
 * 
 * @author m400842
 * @version 1.0
 */
public class CSVParser {

    /**
     * Configuration du framework
     */
    private CSVConfiguration mConfiguration;

    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactoryImpl.getLog(CSVParser.class);

    /**
     * Buffer de lecture du fichier CSV
     */
    private BufferedReader mBuffreader = null;

    /**
     * Chaine contenant l'expression r�guli�re permettant de r�cup�rer les champs du fichier CSV
     * s�par�ment.
     */
    private String mREGEXPCSV = "\"([^\"]|\"\")*\"(,|$)|[^,]+(?=,|$)";

    /**
     * Instance du r�cup�rateur de configuration
     */
    private CSVConfigurationGetter mConfigGetter = null;

    /**
     * Cr�e une nouvelle instance de CSVParser avec la configuration donn�e.
     * 
     * @param pConfigFile Chemin du fichier de configuration
     * @throws CSVException si un probl�me appara�t
     * @roseuid 429431A903D2
     */
    public CSVParser(final String pConfigFile) {
        mConfigGetter = new CSVConfigurationGetter(pConfigFile);
    }

    /**
     * Cr�e une nouvelle instance de CSVParser
     * 
     * @throws CSVException si un probl�me appara�t
     * @roseuid 429431C201AF
     */
    public CSVParser() throws CSVException {
        mConfigGetter = new CSVConfigurationGetter();
    }

    /**
     * Retourne les valeurs ordonn�es de la ligne sous la forme d'un vecteur
     * 
     * @return les �l�ments de la ligne
     * @roseuid 4294384A01A5
     */
    private ArrayList readNextLine() {
        ArrayList results = null;
        String line = null;
        Matcher m;
        Pattern p;
        try {
            // On cr�e le pattern de partage des valeurs
            p = Pattern.compile(mREGEXPCSV);
            // On r�cup�re la nouvelle ligne
            line = mBuffreader.readLine();

            if (line != null) {
                results = new ArrayList();
                // On ajoute dans le tableau chacune des valeurs issues
                // du parsing de la ligne
                m = p.matcher(line);
                while (m.find()) {
                    results.add(m.group());
                }
            }
        } catch (Exception e) {
            // Si un probl�me de lecture apparait
            LOGGER.error(e);
        }

        return results;
    }

    /**
     * Charge le fichier, cr�e la collection de CSVBean correspondant, puis 
     * renvoie cette collection
     * 
     * @param pTemplateName le nom du mod�le de parsing � appliquer.
     * @param pFilename le nom du fichier � parser.
     * @return la collection d'objets issus du fichier.
     * @throws CSVException si un probl�me de parsing appara�t.
     * 
     * @roseuid 4294287000A6
     */
    public Collection parse(final String pTemplateName, final String pFilename) throws CSVException {
        Collection beans = new ArrayList();
        CSVBeanInstanciator instanciator = new CSVBeanInstanciator();
        BeanCSVHandler handler = new BeanCSVHandler(beans, instanciator);
        parseLines(pTemplateName, pFilename, handler);
        return beans;
    }

    /**
     * Instanciation de bean par lecture de CSV
     *
     */
    class BeanCSVHandler implements CSVHandler {
        /** beans */
        private Collection mBeans;
        /** instanciateur */
        private CSVBeanInstanciator mInstanciator;
        /**
         * Constructeur
         * @param pBeans beans
         * @param pInstanciator instanciateur
         */
        public BeanCSVHandler(Collection pBeans, CSVBeanInstanciator pInstanciator) {
            mBeans = pBeans;
            mInstanciator = pInstanciator;
        }
        /** 
         * {@inheritDoc}
         * @see com.airfrance.squalix.util.csv.CSVParser.CSVHandler#processLine(java.util.ArrayList)
         */
        public void processLine(List pLine) {
            mBeans.add(fullfillBean(pLine, mInstanciator));
        }
    }
    /**
     * Parsing des lignes du fichier
     * @param pTemplateName template de d�finition
     * @param pFilename fichier � parser
     * @param pHandler handler de parsing
     * @throws CSVException si erreur
     */
    public void parseLines(final String pTemplateName, final String pFilename, CSVHandler pHandler) throws CSVException {
        // r�cup�re la configuration
        mConfiguration = mConfigGetter.getConfiguration(pTemplateName);
        try {
            instanciateBufferedReader(pFilename);
            ArrayList values = readNextLine();
            // On compte le nombre de lignes � lire
            int fileSize = getLineCount(pFilename) - mConfiguration.getFooterSize();
            // analyse chaque ligne du fichier qui a �t� mise sous forme d'une 
            // liste d'objets
            for (int i = mConfiguration.getHeaderSize(); i < fileSize && null != values; i++) {
                pHandler.processLine(values);
                values = readNextLine();
            }
        } catch (Exception e) {
            // Si un probl�me de lecture du fichier appara�t.
            throw new CSVException(e);
        } finally {
            // on essaye de fermer le buffer dans tous les cas
            try {
                if (mBuffreader != null) {
                    mBuffreader.close();
                }
            } catch (IOException e1) {
                LOGGER.error(e1, e1);
            }
        }
    }

    /**
     * Remplit un bean avec les valeurs pass�es en param�tre.
     * 
     * @param pValues la liste des valeurs ordonn�es.
     * @param pInstanciator l'instance d'instanciateur utilis�e.
     * @return un objet rempli.
     */
    private Object fullfillBean(final List pValues, final CSVBeanInstanciator pInstanciator) {
        Object bean = null;
        String value = null;
        try {
            // On instancie un bean par ligne
            bean = pInstanciator.instanciate(mConfiguration.getCSVBean());
            for (int j = 0; j < pValues.size(); j++) {
                // Pour chaque valeur de la ligne, on attribue la valeur au 
                // bean, en r�cup�rant le nom de l'attribut de la configuration.
                Method setter = mConfiguration.getMappingData(j);
                if (null != setter) {
                    value = String.valueOf(pValues.get(j));
                    pInstanciator.setValue(bean, setter, value);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e, e);
            bean = null;
        }
        return bean;
    }

    /**
     * Retourne le nombre de lignes du fichier
     * @param pFileName Le nom du fichier
     * @return Le nombre de ligne du fichier
     * @throws Exception si un probl�me de lecture apparait.
     * @roseuid 42DFB3500158
     */
    private int getLineCount(String pFileName) throws Exception {
        int count = 0;
        FileReader fr = null;
        BufferedReader br = null;
        fr = new FileReader(pFileName);
        br = new BufferedReader(fr);
        while (null != br.readLine()) {
            count++;
        }
        br.close();
        fr.close();
        return count;
    }

    /**
     * Instancie un BufferedReader li� au fichier
     * @param pFileName le nom du fichier
     * @throws Exception si un probl�me de lecture apparait.
     * @roseuid 42DFB3500168
     */
    private void instanciateBufferedReader(String pFileName) throws Exception {
        FileReader fr;
        mBuffreader = null;
        fr = new FileReader(pFileName);
        mBuffreader = new BufferedReader(fr);
        if (mConfiguration.getHeaderSize() > 0) {
            // S'il y a des lignes d'en-t�te dont il ne faut pas tenir compte,
            // elles sont lues pour placer le lecteur de buffer � la premi�re ligne utile.
            for (int i = 0; i < mConfiguration.getHeaderSize(); i++) {
                mBuffreader.readLine();
            }
        }
    }

    /**
     * Traitement des donn�es d'un fichier CSV
     *
     */
    public interface CSVHandler {
        /**
         * Traitement d'une ligne
         * @param pLine ligne � lire, les donn�es sont contenues dans une liste
         */
        public void processLine(List pLine);
    }
}
