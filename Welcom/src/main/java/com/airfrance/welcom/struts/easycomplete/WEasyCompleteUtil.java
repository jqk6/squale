/*
 * Cr�� le 16 juin 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.struts.easycomplete;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author M327836 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class WEasyCompleteUtil
{
    /**
     * Filtre une chaine de caract�re pour �chapper tous les carract�re de commande d'expressions r�guli�res
     * 
     * @param ch la chaine � filtrer
     * @return Chaine �chapp�
     */
    public static String filter( String ch )
    {
        final Pattern regexp = Pattern.compile( "(\\p{Punct})" );
        final Matcher m = regexp.matcher( ch );
        final StringBuffer buf = new StringBuffer();
        int start = 0;
        int end = 0;

        while ( m.find() )
        {
            end = m.start();
            buf.append( ch.substring( start, end ) );
            buf.append( "\\" );
            buf.append( m.group() );
            start = m.end();
        }

        buf.append( ch.substring( start ) );
        ch = buf.toString();

        return ch;
    }
}