package com.airfrance.squalecommon.enterpriselayer.facade.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.jraf.commons.exception.JrafEnterpriseException;
import com.airfrance.jraf.helper.PersistenceHelper;
import com.airfrance.jraf.provider.persistence.hibernate.facade.FacadeHelper;
import com.airfrance.jraf.spi.enterpriselayer.IFacade;
import com.airfrance.jraf.spi.persistence.IPersistenceProvider;
import com.airfrance.jraf.spi.persistence.ISession;
import com.airfrance.squalecommon.daolayer.component.ApplicationDAOImpl;
import com.airfrance.squalecommon.daolayer.component.ProjectDAOImpl;
import com.airfrance.squalecommon.daolayer.component.ProjectParameterDAOImpl;
import com.airfrance.squalecommon.daolayer.config.ProjectProfileDAOImpl;
import com.airfrance.squalecommon.daolayer.config.SourceManagementDAOImpl;
import com.airfrance.squalecommon.daolayer.rule.QualityGridDAOImpl;
import com.airfrance.squalecommon.datatransfertobject.component.ApplicationConfDTO;
import com.airfrance.squalecommon.datatransfertobject.component.ProjectConfDTO;
import com.airfrance.squalecommon.datatransfertobject.transform.component.ComponentTransform;
import com.airfrance.squalecommon.datatransfertobject.transform.component.ProjectConfTransform;
import com.airfrance.squalecommon.datatransfertobject.transform.component.parameters.MapParameterTransform;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ListParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.MapParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.StringParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.config.ProjectProfileBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.config.SourceManagementBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.QualityGridBO;
import com.airfrance.squalecommon.enterpriselayer.facade.FacadeMessages;

/**
 */
public class ProjectFacade implements IFacade {

    /**
     * provider de persistence
     */
    private static final IPersistenceProvider PERSISTENTPROVIDER = PersistenceHelper.getPersistenceProvider();

    /** log */
    private static Log LOG = LogFactory.getLog(ProjectFacade.class);

    /**
     * Permet de modifier le projet donn� � partir d'un objet DTO
     * @param pProjectConf le projet � modifier
     * @param pApplicationConf application associ�e
     * @param pSession session JRAF
     * @return pProjectConfDTO si l'update s'est correctement deroul�, sinon <code>null</code>
     * @throws JrafEnterpriseException exception JRAF
     */
    public static ProjectConfDTO update(ProjectConfDTO pProjectConf, ApplicationConfDTO pApplicationConf, ISession pSession) throws JrafEnterpriseException {
        // Initialisation du BO associ� et identifiant de l'application
        ProjectBO projectBO = null;
        ApplicationBO applicationBO = null; // ProjectBO charg�
        ProjectBO newProject = null; // ProjectBO apres modification
        List qualityRules = new ArrayList(); // Liste des qualityRules a calculer
        Long projectID = new Long(pProjectConf.getId());
        Long applicationID = new Long(pApplicationConf.getId());
        // indique si l'on doit sauvegarder le projet ou pas
        boolean toUpdate = false;
        // Retour de la m�thode
        ProjectConfDTO result = null;

        try {
            if (pSession == null) {
                pSession = PERSISTENTPROVIDER.getSession();
            }

            // Initialisation des DAO
            ProjectDAOImpl projectDAO = ProjectDAOImpl.getInstance();
            ApplicationDAOImpl applicationDAO = ApplicationDAOImpl.getInstance();

            // Chargement du projet associ�
            projectBO = (ProjectBO) projectDAO.get(pSession, projectID);
            // Si l'objet n'existe pas en base, il faut le cr�er
            if (projectBO == null) {
                projectBO = new ProjectBO();
                toUpdate = true;
            } else {
                // Si il existe d�j� on regarde si il faut l'updater
                toUpdate = projectChanged(pProjectConf, projectBO);
            }
            // Si le projet a chang�, on effectue la transformation et on le sauvegarde :
            if (toUpdate) {
                // Transformation de DTO a BO si le projet n'existe pas en base
                ProjectConfTransform.dto2Bo(pProjectConf, projectBO);

                // Chargement de la grille qualit� associ�e
                String gridName = pProjectConf.getQualityGrid().getName();
                QualityGridBO gridBO = (QualityGridBO) QualityGridDAOImpl.getInstance().findWhereName(pSession, gridName);
                projectBO.setQualityGrid(gridBO);

                // Chargement du profil associ�
                String profileName = pProjectConf.getProfile().getName();
                ProjectProfileBO profileBO = (ProjectProfileBO) ProjectProfileDAOImpl.getInstance().findWhereName(pSession, profileName);
                projectBO.setProfile(profileBO);

                // Chargement du source manager associ�
                String managerName = pProjectConf.getSourceManager().getName();
                SourceManagementBO managerBO = (SourceManagementBO) SourceManagementDAOImpl.getInstance().findWhereName(pSession, managerName);
                projectBO.setSourceManager(managerBO);

                // On charge les param�tres
                ProjectParameterDAOImpl paramDAO = ProjectParameterDAOImpl.getInstance();
                MapParameterBO parameters = MapParameterTransform.dto2Bo(pProjectConf.getParameters());
                Long paramId = new Long(projectBO.getParameters().getId());
                paramDAO.removeAndCreateNew(pSession, paramId, parameters);
                projectBO.setParameters(parameters);

                // Chargement de l'application associ�e
                applicationBO = (ApplicationBO) applicationDAO.get(pSession, applicationID);

                // Si l'application est renseign�e et que le projet a �t� modifi�, on modifie 
                // la relation et on sauvegarde
                if (applicationBO != null) {
                    if (projectBO.getParent() == null || projectBO.getParent().getId() == -1) {
                        // Un projet ne doit jamais changer d'application
                        projectBO.setParent(applicationBO);
                    }
                    newProject = projectDAO.save(pSession, projectBO);
                    // On sauvegarde l'application associ�e pour updater les informations
                    // sur les derni�res modifications
                    applicationBO.setLastUpdate(pApplicationConf.getLastUpdate());
                    applicationBO.setLastUser(pApplicationConf.getLastUser());
                    applicationDAO.save(pSession, applicationBO);
                }

                // initialisation du retour
                if (newProject != null) {
                    result = ProjectConfTransform.bo2Dto(projectBO);
                }
            }

        } catch (JrafDaoException e) {
            FacadeHelper.convertException(e, ProjectFacade.class.getName() + ".update");
        } finally {
            FacadeHelper.closeSession(pSession, ProjectFacade.class.getName() + ".update");
        }

        return result;

    }

    /**
     * @param pProjectConf le projet modifi�
     * @param projectBO le projet en base
     * @return true si le projet en base doit �tre updat�
     */
    private static boolean projectChanged(ProjectConfDTO pProjectConf, ProjectBO projectBO) {
        boolean toUpdate = false;
        toUpdate |= !pProjectConf.getName().equals(projectBO.getName());
        // Si on a modifi� la grille, on devra sauvegarder le projet
        toUpdate |= !projectBO.getQualityGrid().getName().equals(pProjectConf.getQualityGrid().getName());
        // Si on a modifi� le profil, on devra sauvegarder le projet
        toUpdate |= !projectBO.getProfile().getName().equals(pProjectConf.getProfile().getName());
        // Si on a modifi� le source manager, on devra sauvegarder le projet
        toUpdate |= !projectBO.getSourceManager().getName().equals(pProjectConf.getSourceManager().getName());
        // On tranforme les param�tres pour la comparaison
        MapParameterBO parameters = MapParameterTransform.dto2Bo(pProjectConf.getParameters());
        toUpdate |= !projectBO.getParameters().equals(parameters);
        return toUpdate;
    }

    /**
     * Permet de supprimer un projet s�lectionn�
     * @param pProjectId l'ID du projet
     * @param pSession session JRAF
     * @return le projet modifi�, null si erreur
     * @throws JrafEnterpriseException exception JRAF
     */
    public static ProjectConfDTO delete(Long pProjectId, ISession pSession) throws JrafEnterpriseException {
        // Initialisation
        ProjectConfDTO result = null;
        try {
            // Initailisation du DAO
            ProjectDAOImpl projectDAO = ProjectDAOImpl.getInstance();
            // On r�cup�re le projet gr�ce � l'id pass� en param�tre
            ProjectBO projectBO = (ProjectBO) projectDAO.get(pSession, pProjectId);
            // On change son statut
            projectBO.setStatus(ProjectBO.DELETED);
            // On update
            projectDAO.save(pSession, projectBO);
            // On transforme
            result = ProjectConfTransform.bo2Dto(projectBO);
        } catch (JrafDaoException e) {
            FacadeHelper.convertException(e, ProjectFacade.class.getName() + ".delete");
        } finally {
            FacadeHelper.closeSession(pSession, ProjectFacade.class.getName() + ".delete");
        }
        return result;
    }

    /**
     * Permet de supprimer un projet s�lectionn�
     * @param pProjectId l'ID du projet
     * @param pSession session JRAF
     * @return le projet modifi�, null si erreur
     * @throws JrafEnterpriseException exception JRAF
     */
    public static ProjectConfDTO disactiveOrReactiveProject(Long pProjectId, ISession pSession) throws JrafEnterpriseException {
        // Initialisation
        ProjectConfDTO result = null;
        try {
            // Initailisation du DAO
            ProjectDAOImpl projectDAO = ProjectDAOImpl.getInstance();
            // On r�cup�re le projet gr�ce � l'id pass� en param�tre
            ProjectBO projectBO = (ProjectBO) projectDAO.get(pSession, pProjectId);
            // On change son statut
            if (projectBO.getStatus() == ProjectBO.ACTIVATED) {
                // On d�sactive
                projectBO.setStatus(ProjectBO.DISACTIVATED);
            } else {
                // On active
                projectBO.setStatus(ProjectBO.ACTIVATED);
            }
            // On update
            projectDAO.save(pSession, projectBO);
            // On transforme
            result = ProjectConfTransform.bo2Dto(projectBO);
        } catch (JrafDaoException e) {
            FacadeHelper.convertException(e, ProjectFacade.class.getName() + ".delete");
        } finally {
            FacadeHelper.closeSession(pSession, ProjectFacade.class.getName() + ".delete");
        }
        return result;
    }

    /**
     * permet de r�cup�rer l'objet ProjectDTO par un ID
     * @param pProjectConf ProjectDTO renseignant l'id du projet concern�
     * @return ProjectDTO
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB103E1
     */
    public static ProjectConfDTO get(ProjectConfDTO pProjectConf) throws JrafEnterpriseException {

        // Initialisation du BO associ� et de l'ID
        ProjectBO projectBO = null; // projet DTO
        List resultsCalculated = new ArrayList(); // liste des resultats calcul�s
        Long projectID = null; // identifiant du projet

        ISession session = null;

        try {

            projectID = new Long(pProjectConf.getId());

            session = PERSISTENTPROVIDER.getSession();

            ProjectDAOImpl projectDAO = ProjectDAOImpl.getInstance();

            // Chargement du BO associ�
            projectBO = (ProjectBO) projectDAO.get(session, projectID);
            // Transformation du BO en DTO
            if (null != projectBO) {
                pProjectConf = ProjectConfTransform.bo2Dto(projectBO);
            }
        } catch (JrafDaoException e) {
            FacadeHelper.convertException(e, ProjectFacade.class.getName() + ".get");
        } finally {
            FacadeHelper.closeSession(session, ProjectFacade.class.getName() + ".get");
        }

        return pProjectConf;

    }

    /**
     * Permet de cr�er le projet relatif a une application
     * @param pProjectConf le projet � modifier
     * @param pApplicationConf application auquel il est rattach�
     * @param pSession session JRAF
     * @throws JrafEnterpriseException exception JRAF
     */
    public static void insert(ProjectConfDTO pProjectConf, ApplicationConfDTO pApplicationConf, ISession pSession) throws JrafEnterpriseException {

        // Initialisation 
        ProjectBO projectBO = null; // retour de la ProjectDAO
        ApplicationBO ApplicationBO = null; // retour de la ApplicationDAO
        Long projectID = new Long(pProjectConf.getId());
        // identifiant du projet
        Long ApplicationID = new Long(pApplicationConf.getId());

        try {
            if (pSession == null) {
                pSession = PERSISTENTPROVIDER.getSession();
            }

            // Initialisation des DAOs
            ProjectDAOImpl projectDAO = ProjectDAOImpl.getInstance();
            ApplicationDAOImpl ApplicationDAO = ApplicationDAOImpl.getInstance();

            // chargement de ApplicationBO et ProjectBO
            ApplicationBO = (ApplicationBO) ApplicationDAO.get(pSession, ApplicationID);
            projectBO = (ProjectBO) projectDAO.get(pSession, projectID);

            // transformation du DTO au BO
            ProjectConfTransform.dto2Bo(pProjectConf, projectBO);

            // association du projet � l'application
            projectBO.setParent(ApplicationBO);

            // creation du projet en base
            projectBO = projectDAO.create(pSession, projectBO);

        } catch (JrafDaoException e) {
            if (projectBO == null) {
                LOG.error(FacadeMessages.getString("facade.exception.projectfacade.existence.insert"), e);
            } else {
                LOG.error(FacadeMessages.getString("facade.exception.projectfacade.dao.insert"), e);
            }
        } finally {
            FacadeHelper.closeSession(pSession, ProjectFacade.class.getName() + ".insert");
        }

    }

    /**
     * Renvoie la liste des projets attach�s � la grille qualit� avec le nom en param�tre
     * @param pQualityGridName le nom de la grille qualit�
     * @return la liste
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public static Collection findWhereQualityGrid(String pQualityGridName) throws JrafEnterpriseException {
        Collection result = new ArrayList(0);
        ISession session = null;
        try {
            session = PERSISTENTPROVIDER.getSession();
            Collection resultAux = ProjectDAOImpl.getInstance().findWhereQualityGrid(session, pQualityGridName);
            // Transformation de chaque projectBO en componentDTO
            Iterator it = resultAux.iterator();
            while (it.hasNext()) {
                result.add(ComponentTransform.bo2Dto((ProjectBO) it.next()));
            }
        } catch (JrafDaoException e) {
            FacadeHelper.convertException(e, ProjectFacade.class.getName() + ".findWhereQualityGrid");
        } finally {
            FacadeHelper.closeSession(session, ProjectFacade.class.getName() + ".findWhereQualityGrid");
        }
        return result;
    }

    /**
     * Constructeur vide
     * @roseuid 42CBFFB103E3
     */
    private ProjectFacade() {
    }

    /**
     * @param pProjectId l'id du projet
     * @return le workspace du projet si il existe, null sinon
     * @throws JrafEnterpriseException si erreur
     */
    public static String getProjectWorkspace(Long pProjectId) throws JrafEnterpriseException {
        // Initialisation
        String projectWorkspace = ""; // Si il n'y a pas de workspace, on renvoit le champ vide
        List workspaces = null;
        ISession session = null;
        ProjectDAOImpl projectDao = ProjectDAOImpl.getInstance();
        try {
            session = PERSISTENTPROVIDER.getSession();
            // On r�cup�re la liste des workspaces
            ListParameterBO workspacesParams = (ListParameterBO) projectDao.getParameterWhere(session, pProjectId, ParametersConstants.WSAD);
            if (null != workspacesParams) { // On a trouv� des workspaces
                workspaces = workspacesParams.getParameters();
                // Si il n'y en a qu'un, on prend celui-l�
                if (1 == workspaces.size()) {
                    projectWorkspace = ((StringParameterBO) workspaces.get(0)).getValue();
                } else {
                    // On r�cup�re la liste des chemins vers les sources
                    ListParameterBO sources = (ListParameterBO) projectDao.getParameterWhere(session, pProjectId, ParametersConstants.SOURCES);
                    if (null != sources) { // On a trouv� des sources
                        // on fait un get(0) sans v�rification de taille, car si un param�tre est vide
                        // il fau lever une exception car c'est une erreur en base.
                        String source = ((StringParameterBO) sources.getParameters().get(0)).getValue();
                        // Le workspace du projet sera celui qui contient les sources
                        for (int i = 0; i < workspaces.size() && projectWorkspace.length() == 0; i++) {
                            String curWsp = ((StringParameterBO) workspaces.get(i)).getValue();
                            if (source.startsWith(curWsp)) {
                                projectWorkspace = curWsp;
                            }
                        }
                    }
                }
            }
        } catch (JrafDaoException e) {
            FacadeHelper.convertException(e, ProjectFacade.class.getName() + ".getProjectWorkspace");
        } finally {
            FacadeHelper.closeSession(session, ProjectFacade.class.getName() + ".getProjectWorkspace");
        }
        return projectWorkspace;
    }

    /**
     * @param pProjectId l'id du projet
     * @return true si l'export IDE est possible pour ce projet
     * @throws JrafEnterpriseException si erreur
     */
    public static Boolean canBeExportedToIDE(Long pProjectId) throws JrafEnterpriseException {
        // Initialisation
        Boolean canExport = new Boolean(true);
        ISession session = null;
        ProjectDAOImpl projectDao = ProjectDAOImpl.getInstance();
        try {
            session = PERSISTENTPROVIDER.getSession();
            // On r�cup�re le profil du projet
            ProjectBO project = (ProjectBO) projectDao.load(session, pProjectId);
            if (null != project) { // On a trouv� le projet (code d�fensif)
                canExport = new Boolean(project.getProfile().getExportIDE());
            }
        } catch (JrafDaoException e) {
            FacadeHelper.convertException(e, ProjectFacade.class.getName() + ".canBeExportedToIDE");
        } finally {
            FacadeHelper.closeSession(session, ProjectFacade.class.getName() + ".canBeExportedToIDE");
        }
        return canExport;
    }
}
