/*
 * Cr�� le 6 sept. 05, par m400832.
 */
package com.airfrance.squalix.tools.clearcase.utility;

import com.airfrance.squalix.tools.clearcase.configuration.ClearCaseConfiguration;


/**
 * @author m400832
 * @version 1.0
 */
public class ClearCaseStringCleaner {

    /**
     * Cette m�thode retourne une cha�ne en minuscules, sans caract�res sp�ciaux ; 
     * i.e. uniquement des lettres, des chiffres et des "<code>_</code>" 
     * (qui est utilis� comme caract�re de remplacement).<br />
     * Typiquement :<br /><pre>
     * a0�~&�������1245cacahu�es ' hihi#{((ldkqsmiopnncwx,ncxcwx)\\|-||
     * </pre> deviendra : <br /><pre>
     * a0__________1245cacahu_es___hihi____ldkqsmiopnncwx_ncxcwx______
     * </pre>
     * @param pStringToBeCleaned cha�ne � nettoyer.
     * @return la cha�ne nettoy�e.
     */
    public static String getCleanedStringFrom(String pStringToBeCleaned) {
        StringBuffer tmp = new StringBuffer();
        char car;
        
        int i=0;
        /* tant que l'on est pas arriv� au bout de la cha�ne */
        while(i < pStringToBeCleaned.length()){
            
            /* on r�cup�re le caract�re � traiter */
            car = pStringToBeCleaned.charAt(i);
            
            /* s'il s'agit d'un caract�re sp�cial et/ou accentu� */
            if(Character.isJavaIdentifierPart(car) && 
                Character.getNumericValue(car) >= 0) {
                    
                tmp.append(car);
            /* sinon */
            } else { 
                tmp.append(ClearCaseConfiguration.UNDERSCORE);
            }
            i++;
        }
        
        /* on retourne la cha�ne nettoy�e */
        return tmp.toString().toLowerCase();
    }
}
