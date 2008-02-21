package com.airfrance.squalecommon.enterpriselayer.applicationcomponent.administration;


import java.io.InputStream;
import java.util.Collection;

import com.airfrance.jraf.commons.exception.JrafEnterpriseException;
import com.airfrance.jraf.provider.accessdelegate.DefaultExecuteComponent;
import com.airfrance.squalecommon.datatransfertobject.rulechecking.CheckstyleDTO;
import com.airfrance.squalecommon.enterpriselayer.facade.checkstyle.CheckstyleFacade;


/**
 * <p>Title : RulesCheckingAdminComponentAccess.java</p>
 * <p>Description : Application component de l'outil checkstyle.  </p>
 * <p>Copyright : Copyright (c) 2006</p>
 * <p>Company : HENIX</p>
 */
public class CheckstyleAdminComponentAccess extends DefaultExecuteComponent{

    /**
     * permet de recup�rer toutes les versions existantes du fichier de configuration checkstyle
     * @return List la liste des versions
     * @throws JrafEnterpriseException exception JRAF
     */
    public Collection getAllConfigurations() throws JrafEnterpriseException{
        return CheckstyleFacade.getAllVersions();
    }

    /**
     * Destruction des jeu de r�gles inutilis�s
     * @param pRuleSets rulesets devant �tre d�truits
     * @return rulesets utilis�s et ne pouvant donc pas �tre d�truits
     * @throws JrafEnterpriseException si erreur
     */
    public Collection deleteRuleSets(Collection pRuleSets) throws JrafEnterpriseException {
        return CheckstyleFacade.deleteRuleSets(pRuleSets);
    }
    /**
     * Parse le fichier de configuration en fonction du nom du fichier
     * @param pStream flux fu fichier
     * @param pErrors les erreurs g�n�r�s pendant le parsing. 
     * @return un CheckstyleDTO Complet
     * @throws JrafEnterpriseException exception JRAF
     */
    public CheckstyleDTO importConfiguration(InputStream pStream,StringBuffer pErrors) throws JrafEnterpriseException{
        
        CheckstyleDTO dto=null;
        dto=CheckstyleFacade.importCheckstyleConfFile(pStream,pErrors);          
        return dto;
    }
    
}
