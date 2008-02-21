//Source file: D:\\cc_views\\squale_v0_0_act\\squale\\src\\squalix\\src\\com\\airfrance\\squalix\\tools\\mccabe\\McCabeUtility.java

package com.airfrance.squalix.util.file;

import java.util.Iterator;
import java.util.List;

import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.StringParameterBO;

/**
 * Classe contenant les m�thodes outils utilis�es par  les diff�rents outils g�n�rant des rapports.<br>
 * Fournit des m�thodes permettant de manipuler les noms des m�thodes, classes et packages.
 */
public class ToolsReportsUtility {

    /**
     * V�rifie si le nom de la classe correspond aux patterns s�par�s par des point-virgules.
     * @param pClassname le nom de la classe.
     * @param pPattern les patterns sous forme de liste de StringParameterBO.
     * @return le r�sultat de la correspondance.
     */
    public static boolean isMatchingClass(final String pClassname, final List pPattern) {
        boolean result = false;
        if (null != pClassname && null != pPattern) {
            // On parcours la liste des patterns
            Iterator it = pPattern.iterator();
            StringParameterBO pattern;
            while (!result && it.hasNext()) {
                pattern = (StringParameterBO) it.next();
                // On v�rifie pour chaque pattern que celui-ci ne correspond pas � la classe
                result = pClassname.matches(pattern.getValue());
            }
        }
        return result;
    }

    /**
     * Nettoie le nom du composant si celui-ci a �t� mal remont� du rapport.<br>
     * Retire les guillemets et la virgule �ventuelle.
     * @param pName le nom a nettoyer.
     * @return le nom propre.
     */
    public static String clearReportName(String pName) {
        if (pName.matches("\".*\",")) {
            pName = pName.substring(1, pName.length() - 2);
        }
        return pName;
    }

    /**
     * @param pComponentName le nom du composant JSP
     * @return le nom du composant sans le "_jsp" que McCabe place en fin du nom
     */
    public static String clearJspName(String pComponentName) {
        String result = pComponentName;
        int id = pComponentName.lastIndexOf("_jsp");
        if(id > 0) {
            result = pComponentName.substring(0, id);
        }
        return result;
    }
}
