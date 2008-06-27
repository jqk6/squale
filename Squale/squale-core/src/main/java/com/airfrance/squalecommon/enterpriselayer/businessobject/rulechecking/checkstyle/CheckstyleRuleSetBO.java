package com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking.checkstyle;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking.RuleSetBO;

/**
 * Jeu de r�gles Checkstyle
 * 
 * @hibernate.subclass discriminator-value="Checkstyle"
 */

public class CheckstyleRuleSetBO
    extends RuleSetBO
{
    /**
     * Valeur sous forme de chaine de bytes
     */
    private byte[] mValue;

    /**
     * Access method for the mFileName property.
     * 
     * @return the current value of the FileName property
     * @hibernate.property name="Value" column="FileContent"
     *                     type="com.airfrance.jraf.provider.persistence.hibernate.BinaryBlobType" not-null="false"
     *                     unique="false" update="true" insert="true"
     */
    public byte[] getValue()
    {
        return mValue;
    }

    /**
     * @param pValue sous forme de byte[]
     */
    public void setValue( byte[] pValue )
    {
        mValue = pValue;
    }

    /**
     * Verifie s'il existe des ambiguit�s au niveau des modules checkstyle
     * 
     * @return le nom du module concern� si les ambigut� ne sont pas lev�es, sinon null.
     */
    public Collection isAmbiguityModules()
    {
        Collection modulesName = new Vector();
        CheckstyleRuleBO rule = null;
        Collection CheckstyleRules = mRules.values();
        Collection checkstyleModules = new Vector();

        Iterator itRules = CheckstyleRules.iterator();
        // pacours de chaque r�gle pour extraire leur momdules
        while ( itRules.hasNext() )
        {
            rule = (CheckstyleRuleBO) itRules.next();
            // ajout des modules dans la liste
            checkstyleModules.addAll( rule.getModules() );
        }
        String key = null;
        itRules = checkstyleModules.iterator();
        CheckstyleModuleBO checkstyleModule = null;
        Map moduleHashMap = new HashMap();
        boolean status = false;
        // parcours de l'ensemble des modules pour verifier leur unicit�
        while ( itRules.hasNext() )
        {
            checkstyleModule = (CheckstyleModuleBO) itRules.next();

            key = checkstyleModule.getName();
            status = moduleHashMap.containsKey( key );
            if ( !status )
            {
                moduleHashMap.put( key, checkstyleModule );
            }
            else
            {
                CheckstyleModuleBO temp = (CheckstyleModuleBO) moduleHashMap.get( key );
                if ( !verifiedMessage( checkstyleModule.getMessage(), temp.getMessage() ) )
                {
                    modulesName.add( checkstyleModule.getName() );
                }
            }
        }

        return modulesName;
    }

    /**
     * verifie Si deux modules ont chacune un message et que les messages sont diff�rents
     * 
     * @param pMessageUn le premier message
     * @param pMessageDeux le deuxi�me message
     * @return vrai si les deux messages sont �gaux
     */

    private boolean verifiedMessage( String pMessageUn, String pMessageDeux )
    {
        boolean result = false;

        if ( null != pMessageUn && null != pMessageDeux )
        {
            result = !pMessageUn.equals( pMessageDeux );
        }

        return result;

    }
}
