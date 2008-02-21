/*
 * Cr�� le 20 juil. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.squalecommon.daolayer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author M400843
 *
 */
public class DAOUtils {
    /**
     * le pattern de date utilis�
     */
    private final static String JAVADATEPATTERN = "dd/MM/yyyy HH:mm:ss";
    /**
     * le pattern de requete utlis�
     */
    private final static String QUERYDATEPATTERN = "DD/MM/YYYY HH24:mi:ss";
    /**
     * nom de la m�thode sql de conversion de date
     */
    private final static String TODATE = "to_date";
    
    /**
     * 
     * @param pDate la date � convertir en chaine
     * @return la chaine de caract�re permettant d'utiliser la date dans une requete
     */
    public static String makeQueryDate(final Date pDate){
        String queryDate = TODATE + "('";
        SimpleDateFormat sdf = new SimpleDateFormat(JAVADATEPATTERN);
        queryDate += sdf.format(pDate);
        queryDate += "', '" + QUERYDATEPATTERN + "')";
//        "to_date('26/07/2005 10:18:40', 'DD/MM/YYYY hh:mi:ss')"
        return queryDate;
    }
}
