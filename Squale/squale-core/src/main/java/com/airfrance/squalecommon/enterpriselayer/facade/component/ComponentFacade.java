package com.airfrance.squalecommon.enterpriselayer.facade.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.jraf.commons.exception.JrafEnterpriseException;
import com.airfrance.jraf.commons.exception.JrafPersistenceException;
import com.airfrance.jraf.helper.PersistenceHelper;
import com.airfrance.jraf.provider.persistence.hibernate.facade.FacadeHelper;
import com.airfrance.jraf.spi.enterpriselayer.IFacade;
import com.airfrance.jraf.spi.persistence.IPersistenceProvider;
import com.airfrance.jraf.spi.persistence.ISession;
import com.airfrance.squalecommon.daolayer.component.AbstractComponentDAOImpl;
import com.airfrance.squalecommon.daolayer.component.AuditDAOImpl;
import com.airfrance.squalecommon.daolayer.component.ProjectDAOImpl;
import com.airfrance.squalecommon.daolayer.result.MarkDAOImpl;
import com.airfrance.squalecommon.daolayer.rule.QualityRuleDAOImpl;
import com.airfrance.squalecommon.datatransfertobject.component.AuditDTO;
import com.airfrance.squalecommon.datatransfertobject.component.ComponentDTO;
import com.airfrance.squalecommon.datatransfertobject.transform.component.AuditTransform;
import com.airfrance.squalecommon.datatransfertobject.transform.component.ComponentTransform;
import com.airfrance.squalecommon.datatransfertobject.transform.rule.RuleMetricsTransform;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AbstractComplexComponentBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AbstractComponentBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ComponentType;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.MarkBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.QualityRuleBO;
import com.airfrance.squalecommon.enterpriselayer.facade.FacadeMessages;
import com.airfrance.squalecommon.enterpriselayer.facade.rule.FormulaMeasureExtractor;
import com.airfrance.squalecommon.util.mapping.Mapping;
import com.airfrance.squalecommon.util.messages.CommonMessages;

/**
 */
public class ComponentFacade implements IFacade {

    /**
     * provider de persistence
     */
    private static final IPersistenceProvider PERSISTENTPROVIDER = PersistenceHelper.getPersistenceProvider();

    /** log */
    private static Log LOG = LogFactory.getLog(ComponentFacade.class);

    /**
     * permet de r�cup�rer l'objet ComponentDTO par un ID
     * @param pComponent composant avec ID renseign�
     * @return ComponentDTO
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB10028
     */
    public static ComponentDTO get(ComponentDTO pComponent) throws JrafEnterpriseException {

        // Initialisation du retour
        ComponentDTO componentDTO = null;

        // Initialisation des variables temporaires
        AbstractComponentBO componentBO = null;
        Long componentID = new Long(pComponent.getID());

        ISession session = null;
        try {
            session = PERSISTENTPROVIDER.getSession();

            AbstractComponentDAOImpl abstractComponentDAO = AbstractComponentDAOImpl.getInstance();

            componentBO = (AbstractComponentBO) abstractComponentDAO.get(session, componentID);

            if (componentBO != null) {
                componentDTO = ComponentTransform.bo2Dto(componentBO);
            } else {
                LOG.error(FacadeMessages.getString("facade.exception.componentfacade.get.componentnull"));
            }

        } catch (JrafDaoException e) {
            FacadeHelper.convertException(e, ComponentFacade.class.getName() + ".get");
        } finally {
            FacadeHelper.closeSession(session, ComponentFacade.class.getName() + ".get");
        }

        return componentDTO;
    }

    /**
     * Permet de r�cup�rer tous les composants fils d'un composant donn� dans la limite de 1000
     * @param pParent composant parent, si <b>null</b>, liste de tous les 
     * applications
     * @param pType cl� du type de composant, sinon <code>null</code> pour tous les composants
     * @param pAudit l'audit correspondant aux composants fils que l'on souhaite, si <code>null</code> les enfants de sont retourn�s pour tout les audits
     * @param pFilter le filtre sur les noms des enfants
     * @return Collection de ComponentDTO
     * @throws JrafEnterpriseException exception JRAF
     */
    public static Collection getChildren(ComponentDTO pParent, String pType, AuditDTO pAudit, String pFilter) throws JrafEnterpriseException {
        LOG.debug(CommonMessages.getString("method.entry"));
        final int limit = 1000;
        // Initialisation du retour
        Collection componentDTOs = new ArrayList(0);

        // Initialisation des variables temporaires
        AbstractComponentBO parentBO = null; // Objet metier parent
        Collection childrenBO = null; // Collection des objets metiers fils
        Long parentID = new Long(pParent.getID()); // identifiant du composant parent
        // l'instance de AbstractComponentDAO
        AbstractComponentDAOImpl abstractComponentDAO = AbstractComponentDAOImpl.getInstance();

        ISession session = null;
        try {
            session = PERSISTENTPROVIDER.getSession();

            // Recupere les objets metiers par la DAO
            // On fait un tri suivant le param�tre pType qui indique quels types de composants on veut
            // et on ne r�cup�re que les 1000 premiers pour �viter les "out of memory"

            parentBO = (AbstractComponentBO) abstractComponentDAO.get(session, parentID);
            // verification que le composant parent possede des fils
            if (parentBO != null && parentBO instanceof AbstractComplexComponentBO) {
                Long auditId = null;
                if (null != pAudit) {
                    auditId = new Long(pAudit.getID());
                }
                childrenBO = abstractComponentDAO.findChildrenWhere(session, parentID, auditId, pType, pFilter);
            }
            // Manipulation de la collection pour la transformation en DTO
            for (Iterator it = childrenBO.iterator(); it.hasNext();) {
                AbstractComponentBO componentTemp = (AbstractComponentBO) it.next();
                componentDTOs.add(ComponentTransform.bo2Dto(componentTemp));
            }

        } catch (JrafDaoException e) {
            LOG.error(ComponentFacade.class.getName() + ".getChildren", e);
        } finally {
            FacadeHelper.closeSession(session, ComponentFacade.class.getName() + ".getChildren");
        }

        LOG.debug(CommonMessages.getString("method.exit"));
        return componentDTOs;
    }

    /**
    * Permet de compter tous les composants fils d'un composant donn� dans la limite de 1000
    * @param pComponent composant parent, si <b>null</b>, liste de toutes les applications
    * @param pType cl� du type de composant, sinon <code>null</code> pour tous les composants
    * @param pAudit l'audit correspondant aux composants fils que l'on souhaite, si <code>null</code> 
    * les enfants de sont retourn�s pour tout les audits
    * @param pFilter le filtre sur les noms des enfants
    * @return le nombre d'enfants
    * @throws JrafEnterpriseException exception JRAF
    */
    public static Integer countChildren(ComponentDTO pComponent, String pType, AuditDTO pAudit, String pFilter) throws JrafEnterpriseException {
        LOG.debug("Enter in countChildren method");
        // Initialisation du retour
        Integer nbChildren = new Integer(0);

        // Initialisation des variables temporaires
        AbstractComponentBO parentBO = null; // Objet metier parent
        Long parentID = new Long(pComponent.getID()); // identifiant du composant parent
        // l'instance de AbstractComponentDAO
        AbstractComponentDAOImpl abstractComponentDAO = AbstractComponentDAOImpl.getInstance();

        ISession session = null;
        try {
            session = PERSISTENTPROVIDER.getSession();

            // Recupere les objets metiers par la DAO
            // On fait un tri suivant le param�tre pType qui indique quels types de composants on veut

            parentBO = (AbstractComponentBO) abstractComponentDAO.get(session, parentID);
            // verification que le composant parent possede des fils
            if (parentBO != null && parentBO instanceof AbstractComplexComponentBO) {
                Long auditId = null;
                if (null != pAudit) {
                    auditId = new Long(pAudit.getID());
                }
                nbChildren = abstractComponentDAO.countChildrenWhere(session, parentID, auditId, pType, pFilter);
            }

        } catch (JrafDaoException e) {
            LOG.error(ComponentFacade.class.getName() + ".getChildren", e);
        } finally {
            FacadeHelper.closeSession(session, ComponentFacade.class.getName() + ".getChildren");
        }

        LOG.debug(CommonMessages.getString("method.exit"));
        return nbChildren;
    }

    /**
     * R�cup�re les composants exclus
     * @param pAudit l'audit courant
     * @return Collection de ComponentDTO
     * @throws JrafEnterpriseException exception JRAF
     */
    public static Collection getExcluded(AuditDTO pAudit) throws JrafEnterpriseException {
        // Initialisation du retour
        Collection componentDTOs = new ArrayList(0);

        ISession session = null;
        try {
            session = PERSISTENTPROVIDER.getSession();

            // recupere l'instance de AbstractComponentDAO
            AbstractComponentDAOImpl abstractComponentDAO = AbstractComponentDAOImpl.getInstance();

            // on r�cup�re tous les composants exclus 
            Collection parentBOs = (Collection) abstractComponentDAO.getExcludedFromPlan(session);
            Iterator it = parentBOs.iterator();
            while (it.hasNext()) {
                AbstractComponentBO componentTemp = (AbstractComponentBO) it.next();
                // Seulement si le composant est un �l�ment de l'audit courant
                if (componentTemp.containsAuditById(pAudit.getID())) {
                    // on transforme le BO en DTO et on l'ajoute � la collection de retour
                    componentDTOs.add(ComponentTransform.bo2Dto(componentTemp));
                }
            }
        } catch (JrafDaoException e) {
            LOG.error(ComponentFacade.class.getName() + ".getExcluded", e);
        } finally {
            FacadeHelper.closeSession(session, ComponentFacade.class.getName() + ".getExcluded");
        }

        LOG.debug(CommonMessages.getString("method.exit"));
        return componentDTOs;
    }

    /**
     * Permet de recuperer toutes les cl�s de TREs fils d'un TRE donn�
     * @param pKeyTRE cl� d'un type de resultat
     * @return Collection de cl�s de TREs fils du TR donn�
     * @throws JrafEnterpriseException exception JRAF
     */
    public static Collection getTREChildren(Long pKeyTRE) throws JrafEnterpriseException {
        LOG.debug(CommonMessages.getString("method.entry"));

        // Initialisation 
        Collection treChildren = null; // retour de la facade
        Collection treChildrenClasses = null; // collection des classes de TREs enfants
        QualityRuleBO qualityRule = null; // retour de la DAO

        // session Hibernate
        ISession session = null;

        try {

            session = PERSISTENTPROVIDER.getSession();

            QualityRuleDAOImpl qualityRuleDao = QualityRuleDAOImpl.getInstance();

            qualityRule = (QualityRuleBO) qualityRuleDao.load(session, pKeyTRE);

            treChildren = (Collection) qualityRule.accept(new RuleMetricsTransform(new FormulaMeasureExtractor()), null);

        } catch (JrafPersistenceException e) {
            LOG.error(ComponentFacade.class.getName() + ".getTREChildren", e);
        } catch (JrafDaoException e) {
            LOG.error(ComponentFacade.class.getName() + ".getTREChildren", e);
        } finally {
            FacadeHelper.closeSession(session, ComponentFacade.class.getName() + ".getTREChildren");
        }

        LOG.debug(CommonMessages.getString("method.exit"));
        return treChildren;

    }

    /**
     * Constructeur par d�faut
     * @roseuid 42CBFFB1003D
     */
    private ComponentFacade() {
    }

    /**
     * Permet de recuperer une liste de composants qui possede une valeur donn�e pour une
     * note donn�e
     * @param pAudit AuditDTO associ�
     * @param pProject ComponentDTO relatif a une application
     * @param pTreKey cl� de tre valide
     * @param pValue Integer representant l'index de r�partition
     * @param pMax  Nombre maximum de composants retourn�
     * @return Collection de ComponentDTO
     * @throws JrafEnterpriseException exception Jraf
     */
    public static Collection get(AuditDTO pAudit, ComponentDTO pProject, Long pTreKey, Integer pValue, Integer pMax) throws JrafEnterpriseException {
        // Initialisation
        ISession session = null; // session Hibernate

        Collection components = null; // retour de la methode
        Collection marks = null; // retour de markDao

        Long auditId = new Long(pAudit.getID()); // identifiant de l'audit, parametre de MarkDao
        Long projectId = new Long(pProject.getID()); // identifiant de l'application
        // classe de l'objet metier, parametre de MarkDao

        try {

            session = PERSISTENTPROVIDER.getSession();

            // recupere les marks ayant la note pValue en se limitant � pMax elements
            MarkDAOImpl markDAO = MarkDAOImpl.getInstance();
            marks = markDAO.findWhere(session, auditId, projectId, pTreKey, pValue, pMax);

            if (marks != null) {
                // et recupere le composant correspondant
                Iterator markIterator = marks.iterator();
                ComponentDTO currentComponent = null;
                if (components == null) {
                    components = new ArrayList();
                }
                while (markIterator.hasNext()) {
                    MarkBO mark = (MarkBO) markIterator.next();
                    AbstractComponentBO component = mark.getComponent();
                    currentComponent = ComponentTransform.bo2Dto(component);
                    components.add(currentComponent);
                }

            } else {
                LOG.error(FacadeMessages.getString("facade.exception.componentfacade.get.marknull"));
            }

        } catch (JrafDaoException e) {
            LOG.error(ComponentFacade.class.getName() + ".get", e);
        } finally {
            FacadeHelper.closeSession(session, ComponentFacade.class.getName() + ".get");
        }

        //LOG.debug(CommonMessages.getString("method.exit"));
        return components;

    }

    /**
     * Permet de recuperer une liste de composants qui possede une valeur donn�e pour une
     * note donn�e
     * @param pAudit AuditDTO associ�
     * @param pProject ComponentDTO relatif a une application
     * @param pTreKey cl� de tre valide
     * @param pMinValue Integer representant le min de l'interval
     * @param pMaxValue Integer representant le max de l'interval
     * @param pMax  Nombre maximum de composants retourn�
     * @return Collection de ComponentDTO
     * @throws JrafEnterpriseException exception Jraf
     */
    public static Collection getComponentsInInterval(AuditDTO pAudit, ComponentDTO pProject, Long pTreKey, Double pMinValue, Double pMaxValue, Integer pMax) throws JrafEnterpriseException {
        // Initialisation
        ISession session = null; // session Hibernate

        Collection components = null; // retour de la methode
        Collection marks = null; // retour de markDao

        Long auditId = new Long(pAudit.getID()); // identifiant de l'audit, parametre de MarkDao
        Long projectId = new Long(pProject.getID()); // identifiant de l'application
        // classe de l'objet metier, parametre de MarkDao

        try {

            session = PERSISTENTPROVIDER.getSession();

            // recupere les marks ayant la note pValue en se limitant � pMax elements
            MarkDAOImpl markDAO = MarkDAOImpl.getInstance();
            marks = markDAO.findWhereInterval(session, auditId, projectId, pTreKey, pMinValue, pMaxValue, pMax);

            if (marks != null) {
                // et recupere le composant correspondant
                Iterator markIterator = marks.iterator();
                ComponentDTO currentComponent = null;
                if (components == null) {
                    components = new ArrayList();
                }
                while (markIterator.hasNext()) {
                    MarkBO mark = (MarkBO) markIterator.next();
                    AbstractComponentBO component = mark.getComponent();
                    currentComponent = ComponentTransform.bo2Dto(component);
                    components.add(currentComponent);
                }

            } else {
                LOG.error(FacadeMessages.getString("facade.exception.componentfacade.get.marknull"));
            }

        } catch (JrafDaoException e) {
            LOG.error(ComponentFacade.class.getName() + ".get", e);
        } finally {
            FacadeHelper.closeSession(session, ComponentFacade.class.getName() + ".get");
        }

        //LOG.debug(CommonMessages.getString("method.exit"));
        return components;

    }

    /**
     * Permet de recuperer tous les composants parents qui ne sont ni des 
     * projets, ni des applications
     * @param pComponent ComponentDTO dont on souhaite connaitre les parents
     * @param pNbParents Integer
     * @return List de ComponentDTO si le composant possede des parents, sinon une liste vide
     * @throws JrafEnterpriseException eeception Jraf
     */
    public static List getParentsComponent(ComponentDTO pComponent, Integer pNbParents) throws JrafEnterpriseException {

        List parentList = null;
        int nbParents = -1;

        // Initialisation des variables temporaires
        AbstractComponentBO componentBO = null;
        Long componentID = new Long(pComponent.getID());
        if (pNbParents != null) {
            nbParents = pNbParents.intValue();
        }

        ISession session = null;
        try {
            session = PERSISTENTPROVIDER.getSession();

            AbstractComponentDAOImpl abstractComponentDAO = AbstractComponentDAOImpl.getInstance();

            componentBO = (AbstractComponentBO) abstractComponentDAO.get(session, componentID);

            parentList = getParentsList(componentBO, nbParents);

        } catch (JrafDaoException e) {
            FacadeHelper.convertException(e, ComponentFacade.class.getName() + ".get");
        } finally {
            FacadeHelper.closeSession(session, ComponentFacade.class.getName() + ".get");
        }

        return parentList;

    }

    /**
     * Parents d'un composant donn�
     * @param pComponent AbstractComponentBO sur lequel on recupere les parents
     * @param pNbParents nb de parents que l'on souhaite
     * @return List de ComponentDTO parents en ordre hi�rarchique et ne comprenant
     * pas le projet ni l'application 
     */
    private static List getParentsList(AbstractComponentBO pComponent, int pNbParents) {
        List parentList = new ArrayList();

        // Si aucun composant n'est pass�, on ne peu pas chercher ses parents
        if (pComponent != null) {
            // r�cup�ration du premier parent
            AbstractComponentBO parent = pComponent.getParent();

            // tant que l'on trouve un parent est que l'on a pas trouv� le bon nombre composant parents, 
            while (parent != null && pNbParents != 0) {
                if (pNbParents >= 0) {
                    pNbParents--;
                }

                // si l'on est pas arriv� au niveau projet (� forciori application)
                if (parent.getType().compareTo(ComponentType.APPLICATION) != 0 && parent.getType().compareTo(ComponentType.PROJECT) != 0) {
                    // on l'insere en premiere position dans la liste
                    parentList.add(0, ComponentTransform.bo2Dto(parent));
                }
                // r�cup�ration du parent suivant
                parent = parent.getParent();
            }
        } else {
            LOG.error(FacadeMessages.getString("facade.exception.componentfacade.get.componentnull"));
        }

        return parentList;

    }

    /**
     * Permet de r�cup�rer tous les enfants d'un projet de n'importe quel niveau 
     * @param pProject composant parent renseignant l'identifiant
     * @param pType cl� du type de composant, sinon <code>null</code> pour tous les composants
     * @param pAudit audit du composant associ�, si <code>null</code>, on utilise le dernier
     * audit
     * @return Collection de ComponentDTO
     * @throws JrafEnterpriseException exception JRAF
     */
    public static Collection getProjectChildren(ComponentDTO pProject, String pType, AuditDTO pAudit) throws JrafEnterpriseException {

        // Initialisation
        Collection children = null; // retour de la methode
        Collection childrenBo = null; // retour de la dao
        ISession session = null; // session Hibernate

        Long projectId = new Long(pProject.getID()); // identifiant du projet
        Long auditId = new Long(pAudit.getID()); // identifiant de l'audit

        ProjectBO projectBO = null; // objet metier du projet
        AuditBO auditBO = null; // objet metier de l'audit

        AbstractComponentDAOImpl componentDao = AbstractComponentDAOImpl.getInstance();
        AuditDAOImpl auditDao = AuditDAOImpl.getInstance();

        try {
            // r�cup�ration d'une session
            session = PERSISTENTPROVIDER.getSession();

            projectBO = (ProjectBO) componentDao.get(session, projectId);
            auditBO = (AuditBO) auditDao.get(session, auditId);

            childrenBo = componentDao.findProjectChildren(session, projectBO, auditBO, Mapping.getComponentClass(pType));

            // initialisation de la collection d'enfants
            children = new ArrayList();

            // pour chaque enfant
            Iterator boIterator = childrenBo.iterator();
            ComponentDTO component = null;
            while (boIterator.hasNext()) {

                // transformation de l'AbstractComponentBO en ComponentDTO 
                component = ComponentTransform.bo2Dto((AbstractComponentBO) boIterator.next());
                // ajout du l'enfant � la collection de retour
                children.add(component);
            }

        } catch (JrafDaoException e) {
            FacadeHelper.convertException(e, ComponentFacade.class.getName() + ".getProjectChildren");
        } finally {
            FacadeHelper.closeSession(session, ComponentFacade.class.getName() + ".getProjectChildren");
        }
        return children;

    }

    /**
     * Retourne le liste des projets dont le nom commence par <code>mProjectName</code>, dont
     * l'application commence par <code>mAppliName</code> et qui appartient � la liste
     * <code>pUserAppli</code> associ� � son dernier audits (peut-�tre nul)
     * 
     * @param pUserAppli la liste des applications de l'utilisateur courant
     * @param mAppliName le d�but du nom de l'application associ�e au projet
     * @param mProjectName le d�but du nom du projet � chercher
     * @throws JrafEnterpriseException si erreurs
     * 
     * @return les projets trouv�s avec leur dernier audit si il existe
     */
    public static Map getProjectsWithLastAudit(Collection pUserAppli, String mAppliName, String mProjectName) throws JrafEnterpriseException {
        ProjectDAOImpl dao = ProjectDAOImpl.getInstance(); // dao pour les composants
        AuditDAOImpl auditDao = AuditDAOImpl.getInstance();
        ISession session = null; // session Hibernate
        Collection projects = null; //retour du dao
        Map projectsDTO = new HashMap();
        try {
            // r�cup�ration d'une session
            session = PERSISTENTPROVIDER.getSession();
            // On r�cup�re les ids des applications de l'utilisateur
            long[] ids = new long[pUserAppli.size()];
            int index = 0;
            for (Iterator it = pUserAppli.iterator(); it.hasNext(); index++) {
                ComponentDTO appli = (ComponentDTO) it.next();
                ids[index] = appli.getID();
            }
            projects = dao.findProjects(session, ids, mAppliName, mProjectName);
            // On transforme la liste des projectBO en projectDTO
            List audits;
            AuditDTO auditDTO;
            for (Iterator it = projects.iterator(); it.hasNext();) {
                ProjectBO projectBO = (ProjectBO) it.next();
                ComponentDTO projectDTO = (ComponentDTO) ComponentTransform.bo2Dto(projectBO);
                // On r�cup�re son dernier audit
                audits = auditDao.findExecutedWhereComponent(session, projectDTO.getID(), new Integer(1), new Integer(0), AuditBO.ALL_TYPES);
                auditDTO = null;
                if (audits.size() > 0) {
                    auditDTO = AuditTransform.bo2Dto((AuditBO) audits.get(0), projectDTO.getIDParent());
                }
                // On transforme l'audit et on l'associe au projet
                projectsDTO.put(projectDTO, auditDTO);
            }
        } catch (JrafDaoException e) {
            FacadeHelper.convertException(e, ComponentFacade.class.getName() + ".getProjects");
        } finally {
            FacadeHelper.closeSession(session, ComponentFacade.class.getName() + ".getProjects");
        }
        return projectsDTO;
    }

    /**
     * @param pComponent le composant � sauvegarder
     * @throws JrafEnterpriseException si erreurs
     */
    public static void updateComponent(ComponentDTO pComponent) throws JrafEnterpriseException {
        ISession session = null;
        try {
            session = PERSISTENTPROVIDER.getSession();
            AbstractComponentDAOImpl abstractComponentDAO = AbstractComponentDAOImpl.getInstance();
            // Il y a seulement 2 champs modifiables sur le composant, on le recharge donc et apr�s on les met
            // � jour. On ne proc�de pas avec un transformer car le ComponentDTO et le AbstractComponentBO n'ont pas la meme structure,
            // le DTO ne r�cup�rant pas tous les champs inutilis�s pour l'affichage.
            // Seul Squalix utilise ces champs du BO. 
            session.beginTransaction();
            AbstractComponentBO componentToUpdate = (AbstractComponentBO) abstractComponentDAO.get(session, new Long(pComponent.getID()));
            session.commitTransactionWithoutClose();
            componentToUpdate.setExcludedFromActionPlan(pComponent.getExcludedFromActionPlan());
            componentToUpdate.setJustification(pComponent.getJustification());
            session.beginTransaction();
            abstractComponentDAO.save(session, componentToUpdate);
            session.commitTransactionWithoutClose();
        } catch (JrafDaoException e) {
            FacadeHelper.convertException(e, ComponentFacade.class.getName() + ".updateComponent");
        } finally {
            FacadeHelper.closeSession(session, ComponentFacade.class.getName() + ".updateComponent");
        }
    }
}
