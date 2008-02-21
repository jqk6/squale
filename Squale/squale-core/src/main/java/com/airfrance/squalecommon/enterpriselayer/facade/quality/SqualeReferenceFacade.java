package com.airfrance.squalecommon.enterpriselayer.facade.quality;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.jraf.commons.exception.JrafEnterpriseException;
import com.airfrance.jraf.commons.exception.JrafPersistenceException;
import com.airfrance.jraf.helper.PersistenceHelper;
import com.airfrance.jraf.provider.persistence.hibernate.facade.FacadeHelper;
import com.airfrance.jraf.spi.enterpriselayer.IFacade;
import com.airfrance.jraf.spi.persistence.IPersistenceProvider;
import com.airfrance.jraf.spi.persistence.ISession;
import com.airfrance.squalecommon.daolayer.component.ApplicationDAOImpl;
import com.airfrance.squalecommon.daolayer.component.AuditDAOImpl;
import com.airfrance.squalecommon.daolayer.component.ProjectDAOImpl;
import com.airfrance.squalecommon.daolayer.result.MeasureDAOImpl;
import com.airfrance.squalecommon.daolayer.result.QualityResultDAOImpl;
import com.airfrance.squalecommon.daolayer.result.SqualeReferenceDAOImpl;
import com.airfrance.squalecommon.datatransfertobject.component.AuditDTO;
import com.airfrance.squalecommon.datatransfertobject.result.SqualeReferenceDTO;
import com.airfrance.squalecommon.datatransfertobject.transform.result.SqualeReferenceTransform;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.FactorResultBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.SqualeReferenceBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.mccabe.McCabeQAProjectMetricsBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.rsm.RSMProjectMetricsBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.FactorRuleBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.QualityGridBO;

/**
 * 
 */
public class SqualeReferenceFacade implements IFacade {

    /**
     * provider de persistance
     */
    private static final IPersistenceProvider PERSISTENCEPROVIDER = PersistenceHelper.getPersistenceProvider();

    /**
     * Permet d'ins�rer un audit et ses r�sultats dans le r�f�rentiel
     * @dev-squale entr�e des r�sultats de projets dans le r�f�rentiel
     * @param pAudit AuditDTO contenant l'ID de l'audit
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB502B9
     */
    public static void insertAudit(AuditDTO pAudit) throws JrafEnterpriseException {
        insertAudit(pAudit, null);
    }

    /**
     * Permet d'ins�rer un audit et ses r�sultats dans le r�f�rentiel
     * @dev-squale entr�e des r�sultats de projets dans le r�f�rentiel
     * @param pAudit AuditDTO dont les champs sont � jour
     * @param pSession session Hibernate
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB502C3
     */
    public static void insertAudit(AuditDTO pAudit, ISession pSession) throws JrafEnterpriseException {

        // Initialisation 
        ISession session = pSession; // session Hibernate

        Long auditId = new Long(pAudit.getID()); // identifiant de l'audit
        Collection projectBOs = null; // collection de projets relatifs a un audit

        try {

            // Initialisation des Daos
            ProjectDAOImpl projectDao = ProjectDAOImpl.getInstance();
            QualityResultDAOImpl qualityResultDao = QualityResultDAOImpl.getInstance();
            SqualeReferenceDAOImpl squaleReferenceDAO = SqualeReferenceDAOImpl.getInstance();
            AuditDAOImpl auditDAOImpl = AuditDAOImpl.getInstance();
            AuditBO auditBO = (AuditBO) auditDAOImpl.get(pSession, auditId);
            if (null == session) {
                // r�cup�ration d'une session si celle-ci n'est pas d�j� initialis�e
                session = PERSISTENCEPROVIDER.getSession();
            }

            // r�cup�ration des projets li�s � l'audit 
            projectBOs = (Collection) projectDao.findWhere(session, auditId);

            Iterator projectIterator = projectBOs.iterator();
            ProjectBO currentProject = null;
            SqualeReferenceBO currentSqualeReference = null;
            Long projectId = null;
            // Pour chaque projet :
            while (projectIterator.hasNext()) {

                // Recuperation du projet courant
                currentProject = (ProjectBO) projectIterator.next();
                projectId = new Long(currentProject.getId());

                // Recupere l'ancienne reference
                currentSqualeReference = SqualeReferenceDAOImpl.getInstance().loadByName(session, currentProject.getParent().getName(), currentProject.getName());
                boolean validated = false;
                // si elle existe
                if (currentSqualeReference != null) {
                    // recupere sa validation 
                    validated = currentSqualeReference.getHidden();
                    // et supprime la reference du referenciel
                    SqualeReferenceDAOImpl.getInstance().remove(session, currentSqualeReference);
                }

                // Initialisation du nouveau SqualeReferenceBO
                currentSqualeReference = new SqualeReferenceBO();
                // en gardant la reference precedente
                currentSqualeReference.setHidden(validated);
                // Grille qualit� correspondant � cet audit
                QualityGridBO grid = auditBO.getAuditGrid(currentProject).getGrid();
                // R�cup�ration de la grille qualit�
                currentSqualeReference.setQualityGrid(grid);
                // Recuperation des resultats
                // Pour tous les facteurs qualit�s du projet
                Iterator it = grid.getFactors().iterator();
                while (it.hasNext()) {
                    FactorRuleBO qRule = (FactorRuleBO) it.next();
                    // recupere la note
                    FactorResultBO factorResult = (FactorResultBO) QualityResultDAOImpl.getInstance().load(session, projectId, auditId, new Long(qRule.getId()));
                    if (factorResult != null) {
                        // si la r�gle � �t� trouv�e, on affecte sa note � la r�f�rence
                        currentSqualeReference.getFactors().put(qRule, new Float(factorResult.getMeanMark()));
                    }
                }

                // set des diff�rent param�tres de la r�f�rence
                currentSqualeReference.setDate(pAudit.getDate());
                currentSqualeReference.setLanguage(currentProject.getProfile().getName());
                currentSqualeReference.setApplicationName(currentProject.getParent().getName());
                currentSqualeReference.setProjectName(currentProject.getName());
                currentSqualeReference.setVersion(pAudit.getName());
                currentSqualeReference.setAuditType(pAudit.getType());
                // Obtention du caract�re public de l'application de rattachement
                // L'application est cens�e exister et donc le code d�fensif est ici inutile
                // Le caract�re public d'un audit est li� au caract�re public de l'application
                // au moment o� cet audit est cr�� dans la base
                boolean publicApplication = ApplicationDAOImpl.getInstance().loadByName(session, currentProject.getParent().getName()).getPublic();
                currentSqualeReference.setPublic(publicApplication);

                // Volum�trie
                RSMProjectMetricsBO projectVol = (RSMProjectMetricsBO) MeasureDAOImpl.getInstance().load(session, projectId, auditId, RSMProjectMetricsBO.class);
                if (projectVol != null) {
                    currentSqualeReference.setCodeLineNumber(projectVol.getSloc().intValue());
                }
                McCabeQAProjectMetricsBO presult = (McCabeQAProjectMetricsBO) MeasureDAOImpl.getInstance().load(session, projectId, auditId, McCabeQAProjectMetricsBO.class);
                if (presult != null) {
                    currentSqualeReference.setClassNumber(presult.getNumberOfClasses().intValue());
                    currentSqualeReference.setMethodNumber(presult.getNumberOfMethods().intValue());
                }

                // Creation en base
                squaleReferenceDAO.create(session, currentSqualeReference);
            }

        } catch (JrafDaoException e) {
            FacadeHelper.convertException(e, SqualeReferenceFacade.class.getName() + ".insertAudit");
        } finally {
            FacadeHelper.closeSession(session, SqualeReferenceFacade.class.getName() + ".insertAudit");
        }
    }

    /**
     * Permet de r�cup�rer r�f�rences
     * @use by ResultsComponent dans la comparaison d'applications
     * @param pNbLignes nombre de lignes
     * @param pIndexDepart index de d�part
     * @param pIsAdmin booleen permettant de savoir si on doit r�cup�rer les projets masqu�s
     * @param pUserId l'id de l'utilisateur
     * @return Collection de SqualeReferenceDTO
     * @throws JrafEnterpriseException exception JRAF
     */
    public static List getProjectResults(Integer pNbLignes, Integer pIndexDepart, boolean pIsAdmin, Long pUserId) throws JrafEnterpriseException {

        // Initialisation
        List referenceDTOs = null; // retour de la m�thode
        List referenceBOs = null; // retour de la DAO
        ISession session = null; // session Hibernate

        try {

            SqualeReferenceDAOImpl squaleDAO = SqualeReferenceDAOImpl.getInstance();
            // r�cup�ration d'un session
            session = PERSISTENCEPROVIDER.getSession();
            
            // R�cup�ration des r�f�rences souhait�es
            referenceBOs = (List) squaleDAO.findWhereScrollable(session, pNbLignes, pIndexDepart, pIsAdmin, pUserId);

            Iterator referenceIterator = referenceBOs.iterator();
            SqualeReferenceDTO currentReferenceDTO = null;

            if (referenceBOs != null) {
                referenceDTOs = new ArrayList();

                while (referenceIterator.hasNext()) {
                    // pour chaque r�f�rence : 
                    // on la transforme en SqualeReferenceDTO
                    currentReferenceDTO = SqualeReferenceTransform.bo2Dto((SqualeReferenceBO) referenceIterator.next());
                    // et on l'ajoute � la collection de retour
                    referenceDTOs.add(currentReferenceDTO);
                }
            }

        } catch (JrafDaoException e) {
            FacadeHelper.convertException(e, SqualeReferenceFacade.class.getName() + ".getProjectResults");
        } finally {
            FacadeHelper.closeSession(session, SqualeReferenceFacade.class.getName() + ".getProjectResults");
        }

        return referenceDTOs;
    }

    /**
     * Permet de supprimer une collection d'audits d'applications du r�f�rentiel
     * @param pAuditsToRemove collection de SqualeReferenceDTO � supprimer
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB502E0
     */
    public static void deleteAuditList(Collection pAuditsToRemove) throws JrafEnterpriseException {
        deleteAuditList(pAuditsToRemove, null);
    }

    /**
     * Permet de supprimer une collection d'audits d'applications du r�f�rentiel
     * @param pAuditsToRemove collection de SqualeReferenceDTO � supprimer
     * @param pSession session JRAF
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB502E2
     */
    public static void deleteAuditList(Collection pAuditsToRemove, ISession pSession) throws JrafEnterpriseException {

        // Initialisation
        Iterator referenceIterator = pAuditsToRemove.iterator();
        SqualeReferenceBO squaleReference = null;
        Long currentReferenceId = null;

        try {
            if (pSession == null) {
                // si aucune session n'a �t� fournie � la fa�ade, on en r�cup�re une
                pSession = PERSISTENCEPROVIDER.getSession();
            }

            SqualeReferenceDAOImpl referenceDao = SqualeReferenceDAOImpl.getInstance();
            // Pour chaque audit d'application dans le r�f�rentiel
            while (referenceIterator.hasNext()) {
                // r�cup�ration de l'id de la r�f�rence en cours
                currentReferenceId = new Long(((SqualeReferenceDTO) referenceIterator.next()).getId());

                // Chargement de l'objet en base
                squaleReference = (SqualeReferenceBO) referenceDao.load(pSession, currentReferenceId);
                // Suppression de l'objet en base
                referenceDao.remove(pSession, squaleReference);
            }

        } catch (JrafDaoException e) {
            FacadeHelper.convertException(e, SqualeReferenceFacade.class.getName() + ".deleteAuditList");
        } finally {
            FacadeHelper.closeSession(pSession, SqualeReferenceFacade.class.getName() + ".deleteAuditList");
        }
    }

    /**
     * Permet de valider une collection de r�sultats d'une application dans le referentiel
     * @param pReferences collection de SqualeReferenceDTO � valider
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB502EB
     */
    public static void validateAuditList(Collection pReferences) throws JrafEnterpriseException {
        updateReferentiel(pReferences, null);
    }

    /**
     * Permet de valider une collection de r�sultats d'une application dans le referentiel
     * @param pReferences collection de SqualeReferenceDTO � valider
     * @param pSession session Hibernate
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB502ED
     */
    public static void updateReferentiel(Collection pReferences, ISession pSession) throws JrafEnterpriseException {

        // Initialisation
        Iterator referenceIterator = pReferences.iterator();
        Long currentReferenceId = null;
        SqualeReferenceBO referenceBO = null;

        try {

            // Initialisation de la session
            if (pSession == null) {
                pSession = PERSISTENCEPROVIDER.getSession();
            }
            // R�cup�ration d'une instance de SqualeReferenceDAOImpl
            SqualeReferenceDAOImpl referenceDao = SqualeReferenceDAOImpl.getInstance();
            // Pour chaque r�f�rence
            while (referenceIterator.hasNext()) {
                // Chargement de l'objet et modification du champs "validated"
                SqualeReferenceDTO currentReference = (SqualeReferenceDTO) referenceIterator.next();
                referenceBO = (SqualeReferenceBO) referenceDao.loadByName(pSession, currentReference.getApplicationName(), currentReference.getProjectName());
                // Si l'application et le projet n'�tait pas d�j� r�f�renc�, on ne fait rien
                // Ce cas ne doit pas arriver, sauf lors de cas particuliers comme des migrations ou des passages en prod par exemple
                if (referenceBO != null) {
                    referenceBO.setHidden(!currentReference.getHidden());
                    // sauvegarde de l'objet modifi�
                    referenceDao.save(pSession, referenceBO);
                }
            }

        } catch (JrafDaoException e) {
            FacadeHelper.convertException(e, SqualeReferenceFacade.class.getName() + ".updateReferentiel");
        } finally {
            FacadeHelper.closeSession(pSession, SqualeReferenceFacade.class.getName() + ".updateReferentiel");
        }

    }

    /**
     * @param pAppliName le nom du projet
     * @param pSession la session
     * @return l'objet r�f�rence correspondant si il existe, null sinon
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public static SqualeReferenceDTO getReferencedAudit(String pAppliName, ISession pSession) throws JrafEnterpriseException {
        SqualeReferenceBO referenceBO = null;
        SqualeReferenceDTO result = null;
        try {

            // Initialisation de la session
            if (pSession == null) {
                pSession = PERSISTENCEPROVIDER.getSession();
            }
            // R�cup�ration d'une instance de SqualeReferenceDAOImpl
            SqualeReferenceDAOImpl referenceDao = SqualeReferenceDAOImpl.getInstance();

            referenceBO = referenceDao.findReferenceByAppliName(pSession, pAppliName);
        } catch (JrafDaoException e) {
            FacadeHelper.convertException(e, SqualeReferenceFacade.class.getName() + ".validateAuditList");
        } finally {
            FacadeHelper.closeSession(pSession, SqualeReferenceFacade.class.getName() + ".validateAuditList");
        }
        if (referenceBO != null) {
            result = SqualeReferenceTransform.bo2Dto(referenceBO);
        }
        return result;
    }

    /**
     * @return la collection des noms d'application stock�es dans le r�f�rentiel
     * @throws JrafEnterpriseException en cas d'�chec
     * @throws JrafPersistenceException en cas d'�chec
     */
    public static Collection listReferentiel() throws JrafEnterpriseException, JrafPersistenceException {
        Collection result = new ArrayList(0);
        ISession mSession =  PERSISTENCEPROVIDER.getSession();
        try {
            // R�cup�ration d'une instance de SqualeReferenceDAOImpl
            SqualeReferenceDAOImpl referenceDao = SqualeReferenceDAOImpl.getInstance();
            result =  referenceDao.findAllDistinctAppliName(mSession);
        } catch (JrafDaoException e) {
            FacadeHelper.convertException(e, SqualeReferenceFacade.class.getName() + ".listReferentiel");
        } finally {
            FacadeHelper.closeSession(mSession, SqualeReferenceFacade.class.getName() + ".listReferentiel");
        }
        return result;
    }
}
