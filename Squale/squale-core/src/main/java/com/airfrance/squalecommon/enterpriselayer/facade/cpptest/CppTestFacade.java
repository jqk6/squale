package com.airfrance.squalecommon.enterpriselayer.facade.cpptest;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.jraf.commons.exception.JrafEnterpriseException;
import com.airfrance.jraf.helper.PersistenceHelper;
import com.airfrance.jraf.provider.persistence.hibernate.facade.FacadeHelper;
import com.airfrance.jraf.spi.persistence.IPersistenceProvider;
import com.airfrance.jraf.spi.persistence.ISession;
import com.airfrance.squalecommon.daolayer.result.rulechecking.RuleCheckingTransgressionDAOImpl;
import com.airfrance.squalecommon.daolayer.rulechecking.CppTestRuleSetDAOImpl;
import com.airfrance.squalecommon.datatransfertobject.rulechecking.CppTestRuleSetDTO;
import com.airfrance.squalecommon.datatransfertobject.transform.rulechecking.CppTestRuleSetTransform;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking.cpptest.CppTestRuleSetBO;

/**
 * Facade pour CppTest
 */
public class CppTestFacade {
    /**
     * Provider de persistence
     */
    private static final IPersistenceProvider PERSISTENTPROVIDER = PersistenceHelper.getPersistenceProvider();

    /** Log */
    private static Log LOG = LogFactory.getLog(CppTestFacade.class);

    /**
     * Parsing du fichier de configuration
     * @param pStream flux � lire
     * @param pErrors erreurs rencontr�es
     * @return ruleset cr�� ou nul si la contrainte d'unicit� n'est pas
     * respect�e
     * @throws JrafEnterpriseException si erreur
     */
    public static CppTestRuleSetDTO importCppTestConfig(InputStream pStream, StringBuffer pErrors) throws JrafEnterpriseException {
        ISession session = null;
        CppTestRuleSetDTO dto = null;
        try {
            session = PERSISTENTPROVIDER.getSession();
            // Importation du fichier
            CppTestConfigParser parser = new CppTestConfigParser();
            CppTestRuleSetBO ruleset = parser.parseFile(pStream, pErrors);
            // Si le parsing ne provoque pas d'erreur, on tente la sauvegarde dans la base
            if (pErrors.length() == 0) {
                CppTestRuleSetDAOImpl dao = CppTestRuleSetDAOImpl.getInstance();
                ruleset = dao.createCppTestRuleSet(session, ruleset);
                // ruleset vaut nul si la contrainte d'unicit� n'est pas respect�e
                // dans ce cas on renvoie null
                if (ruleset != null) {
                    dto = CppTestRuleSetTransform.bo2Dto(ruleset);
                }
            }
        } catch (JrafDaoException e) {
            // Log de l'erreur
            LOG.error(e.getMessage(), e);
            FacadeHelper.convertException(e, CppTestFacade.class.getName() + ".get");
        } finally {
            // Fermeture de la session
            FacadeHelper.closeSession(session, CppTestFacade.class.getName() + ".get");
        }
        return dto;
    }

    /**
     * Obtention des configurations CppTest
     * @return collection de configurations sous la forme cd CppTestRuleSetDTO
     * @throws JrafEnterpriseException si erreur
     */
    public static Collection getCppTestConfigurations() throws JrafEnterpriseException {
        ISession session = null;
        Collection result = new ArrayList();
        try {
            session = PERSISTENTPROVIDER.getSession();
            // Obtention des RuleSet
            CppTestRuleSetDAOImpl dao = CppTestRuleSetDAOImpl.getInstance();
            Collection rulesets = dao.findAll(session);
            Iterator boIt = rulesets.iterator();
            while (boIt.hasNext()) {
                CppTestRuleSetBO ruleset = (CppTestRuleSetBO) boIt.next();
                result.add(CppTestRuleSetTransform.bo2Dto(ruleset));
            }
        } catch (JrafDaoException e) {
            LOG.error(e.getMessage(), e);
            FacadeHelper.convertException(e, CppTestFacade.class.getName() + ".get");
        } finally {
            // Fermeture de la session
            FacadeHelper.closeSession(session, CppTestFacade.class.getName() + ".get");
        }
        return result;
    }

    /**
     * Obtention d'une configuration CppTest
     * @param pName nom
     * @return ruleset correspondant ou null si non trouv�
     * @throws JrafEnterpriseException si erreur
     */
    public static CppTestRuleSetDTO getCppTestConfiguration(String pName) throws JrafEnterpriseException {
        ISession session = null;
        CppTestRuleSetDTO result = null;
        try {
            session = PERSISTENTPROVIDER.getSession();
            // Obtention des RuleSet
            CppTestRuleSetDAOImpl dao = CppTestRuleSetDAOImpl.getInstance();
            CppTestRuleSetBO ruleset = dao.findRuleSet(session, pName);
            if (ruleset!=null) {
                result = CppTestRuleSetTransform.bo2Dto(ruleset);
            }
        } catch (JrafDaoException e) {
            LOG.error(e.getMessage(), e);
            FacadeHelper.convertException(e, CppTestFacade.class.getName() + ".get");
        } finally {
            // Fermeture de la session
            FacadeHelper.closeSession(session, CppTestFacade.class.getName() + ".get");
        }
        return result;
    }
    /**
      * Destruction de rulesets obsol�tes
      * @param pRuleSets ruleSets devant �tre d�truits
      * @return rulesets obsol�tes ne pouvant pas �tre supprim�s
      * @throws JrafEnterpriseException si erreur
      */
    public static Collection deleteRuleSets(Collection pRuleSets) throws JrafEnterpriseException {
        ISession session = null;
        Collection result = new ArrayList();
        try {
            // r�cup�ration d'une session
            session = PERSISTENTPROVIDER.getSession();
            CppTestRuleSetDAOImpl checkstyleRuleSetDAO = CppTestRuleSetDAOImpl.getInstance();
            Iterator ruleSetsIt = pRuleSets.iterator();
            // Parcours des rulesets � d�truire
            ArrayList rulesetsId = new ArrayList();
            RuleCheckingTransgressionDAOImpl ruleCheckingTransgressionDAO = RuleCheckingTransgressionDAOImpl.getInstance();
            while (ruleSetsIt.hasNext()) {
                CppTestRuleSetDTO checkstyleDTO = (CppTestRuleSetDTO) ruleSetsIt.next();
                Long ruleSetId = new Long(checkstyleDTO.getId());
                // On v�rifie que le jeu de r�gles n'est pas utilis�
                // au niveau des mesures r�alis�es, pour les projets param�tr�s mais non encore audit�s, on ne le g�re pas
                if (ruleCheckingTransgressionDAO.isRuleSetUsed(session, ruleSetId)) {
                    result.add(checkstyleDTO);
                } else {
                    // Ajout dans les rulesets � d�truire
                    rulesetsId.add(ruleSetId);
                }
            }
            // Destruction des rulesets qui ne sont plus r�f�renc�s
            if (rulesetsId.size() > 0) {
                checkstyleRuleSetDAO.removeCppTestRuleSets(session, rulesetsId);
            }
        } catch (JrafDaoException e) {
            FacadeHelper.convertException(e, CppTestFacade.class.getName() + ".get");
        } finally {
            FacadeHelper.closeSession(session, CppTestFacade.class.getName() + ".get");
        }
        return result;
    }
}
