//Source file: D:\\cc_views\\squale_v0_0_act\\squale\\src\\squalix\\src\\com\\airfrance\\squalix\\util\\csv\\CSVConfiguration.java

package com.airfrance.squalix.util.csv;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Sp�cifie pour chaque type de fichier susceptible d'�tre pars� :
 * <ul>
 * <li>le nom du template</li>
 * <li>la classe concr�te enti�rement sp�cifi�e du CSVBean correspondant</li>
 * <li>et pour chaque colonne du fichier csv, le nom de l'attribut de la classe 
 * concr�te lui correspondant, ainsi que son 
 * type objet enti�rement sp�cifi� (et pas simple)</li>
 * </ul>
 * @author m400842
 * @version 1.0
 */
public class CSVConfiguration {
    
    /**
     * Nombre de lignes d'en-t�te � ne pas prendre en compte
     */
    private int mHeaderSize;
    
    /**
     * Nombre de lignes de pied de page � ne pas prendre en compte
     */
    private int mFooterSize;
    
    /**
     * Nom du mod�le de mapping utilis�
     */
    private String mTemplateName;
    
    /**
     * Nom de la classe des objets � mapper
     */
    private String mCSVBean;
    
    /**
     * Contient les couples "num�ro de colonne" => "methode"
     */
    private HashMap mMethods;
    
    /**
     * Instancie une nouvelle configuration avec le nom du mod�le
     * 
     * @param pTemplateName le nom du mod�le
     * @roseuid 42B2BE430148
     */
    public CSVConfiguration(final String pTemplateName) {
        mTemplateName = pTemplateName;
    }
    
    /**
     * Access method for the mTemplateName property.
     * 
     * @return   the current value of the mTemplateName property
     * @roseuid 42CE6C6E0081
     */
    public String getTemplateName() {
        return mTemplateName;
    }
    
    /**
     * Access method for the mCSVBean property.
     * 
     * @return   the current value of the mCSVBean property
     * @roseuid 42CE6C6E0082
     */
    public String getCSVBean() {
        return mCSVBean;
    }
    
    /**
     * Access method for the mMethods property.
     * 
     * @return   the current value of the mMethods property
     * @roseuid 42CE6C6E0083
     */
    public HashMap getMethods() {
        return mMethods;
    }
    
    /**
     * Access method for the mHeaderSize property.
     * 
     * @return   the current value of the mHeaderSize property
     * @roseuid 42CE6C6E0090
     */
    public int getHeaderSize() {
        return mHeaderSize;
    }
    
    /**
     * Retourne le couple de mapping correspondant � la colonne pass�e en param�tre.
     * 
     * @param pColumn Le num�ro de la colonne dans le fichier CSV
     * @return Le setter correspondant � la colonne.
     * @roseuid 42CE745D023E
     */
    public Method getMappingData(int pColumn) {
        Integer column = new Integer(pColumn);
        Method method = null;
        if (mMethods.containsKey(column)) {
            method = (Method) mMethods.get(column);
        }
        return method;
    }
    
    /**
     * Sets the value of the mHeaderSize property.
     * 
     * @param pHeaderSize the new value of the mHeaderSize property
     * @roseuid 42CE749B0146
     */
    public void setHeaderSize(int pHeaderSize) {
        mHeaderSize = pHeaderSize;
    }
    
    /**
     * Access method for the mFooterSize property.
     * 
     * @return   the current value of the mFooterSize property
     * @roseuid 42CE749B0156
     */
    public int getFooterSize() {
        return mFooterSize;
    }
    
    /**
     * Sets the value of the mFooterSize property.
     * 
     * @param pFooterSize the new value of the mFooterSize property
     * @roseuid 42CE749B0164
     */
    public void setFooterSize(int pFooterSize) {
        mFooterSize = pFooterSize;
    }
    
    /**
     * Sets the value of the mCSVBean property.
     * 
     * @param pCSVBean the new value of the mCSVBean property
     * @roseuid 42DFB34601C5
     */
    public void setCSVBean(String pCSVBean) {
        mCSVBean = pCSVBean;
    }
    
    /**
     * Sets the value of the mMethods property.
     * 
     * @param pMethods the new value of the mMethods property
     * @roseuid 42DFB34601D4
     */
    public void setMethods(HashMap pMethods) {
        mMethods = pMethods;
    }
}
