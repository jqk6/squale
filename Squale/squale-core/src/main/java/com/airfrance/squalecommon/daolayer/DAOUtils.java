/*
 * Cr�� le 20 juil. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.squalecommon.daolayer;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.squalecommon.util.database.DatabaseTypeFactory;

/**
 * @author M400843
 */
public class DAOUtils
{
    /**
     * le pattern de date utilis�
     */
    private final static String JAVADATEPATTERN = "dd/MM/yyyy HH:mm:ss";

    /**
     * @param pDate la date � convertir en chaine
     * @return la chaine de caract�re permettant d'utiliser la date dans une requete
     */
    public static String makeQueryDate( final Date pDate )
    throws JrafDaoException
    {
        SimpleDateFormat sdf = new SimpleDateFormat( JAVADATEPATTERN );
        String dateString = sdf.format( pDate );
        String queryDate = DatabaseTypeFactory.getInstance().toDate(dateString);
        return queryDate;
    }
}
