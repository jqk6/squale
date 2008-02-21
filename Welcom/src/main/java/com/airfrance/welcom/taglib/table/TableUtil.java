/*
 * Cr�� le 3 sept. 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.taglib.table;

import java.util.Collection;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author M327836
 *
 * Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class TableUtil {

    /** logger */
    private static Log log = LogFactory.getLog(TableUtil.class);

    /**
     * Retourne la collection tri�
     * @param session : Session en cour
     * @param name : Nom du bean
     * @return la collection tri�e
     */
    public static Collection getSortedTable(final HttpSession session, final String name) {

        return InternalTableUtil.getSortedTable(session, name, null);
    }

    /**
     * Retourne la collection tri�
     * @param session : Session en cour
     * @param name : Nom du bean
     * @param property : Nom de la property du bean
     * @return la collection tri�e
     */
    public static Collection getSortedTable(final HttpSession session, final String name, final String property) {

        return InternalTableUtil.getSortedTable(session, name, property);
    }

    /**
     * Suuprime le trie surla table sp�cifi�
     * @param session : Session en cour
     * @param name : Nom du bean
     */
    public static void resetSortOfTable(final HttpSession session, final String name) {

        InternalTableUtil.resetSortOfTable(session, name, null);
    }

    /**
     * Retourne la collection tri�
     * @param session : Session en cour
     * @param name : Nom du bean
     * @param property : Nom de la property du bean
     */
    public static void resetSortOfTable(final HttpSession session, final String name, final String property) {

        InternalTableUtil.resetSortOfTable(session, name, property);
    }

}