package com.airfrance.squaleweb.applicationlayer.action.results.project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.airfrance.jraf.commons.exception.JrafEnterpriseException;
import com.airfrance.jraf.helper.AccessDelegateHelper;
import com.airfrance.jraf.spi.accessdelegate.IApplicationComponent;
import com.airfrance.squalecommon.datatransfertobject.component.AuditDTO;
import com.airfrance.squalecommon.datatransfertobject.component.ComponentDTO;
import com.airfrance.squalecommon.datatransfertobject.result.ResultsDTO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ComponentType;
import com.airfrance.squalecommon.util.SqualeCommonConstants;
import com.airfrance.squaleweb.applicationlayer.action.ActionUtils;
import com.airfrance.squaleweb.applicationlayer.action.accessRights.ReaderAction;
import com.airfrance.squaleweb.applicationlayer.formbean.results.ComponentForm;
import com.airfrance.squaleweb.applicationlayer.formbean.results.ComponentListForm;
import com.airfrance.squaleweb.applicationlayer.formbean.results.ResultListForm;
import com.airfrance.squaleweb.applicationlayer.tracker.TrackerStructure;
import com.airfrance.squaleweb.resources.WebMessages;
import com.airfrance.squaleweb.transformer.ComponentListTransformer;
import com.airfrance.squaleweb.transformer.ComponentTransformer;
import com.airfrance.squaleweb.transformer.ProjectTransformer;
import com.airfrance.squaleweb.transformer.ResultListTransformer;
import com.airfrance.squaleweb.util.SqualeWebConstants;
import com.airfrance.welcom.struts.bean.WActionForm;
import com.airfrance.welcom.struts.transformer.WTransformerFactory;

/**
 * Action charg�e de g�rer la r�cup�ration des r�sultats des composants d'un projet.
 * 
 * @version 1.0
 * @author
 */
public class ProjectComponentsAction extends ReaderAction {

    /**
     * Nombre d'�l�ments � r�cup�rer pour le traceur
     */
    private static final int NUMBER_COMPONENT_TRACER = 4;

    /** Nombre maximum de composants remont�s */
    private static final int MAX_COMPONENTS = 1000;

    /** 
     * Nom de l'attibut en session pour indiquer � l'utilisateur que tous ces composants
     * ne sont pas affich�s.
     */
    public static final String TOO_MUCH_COMPONENTS_MSG = "tooMuchComponentsMsg";

    /**
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward root(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) {

        ActionForward forward = null;
        ActionErrors errors = new ActionErrors();
        try {
            // R�cup�ration de l'audit courant
            AuditDTO auditDTO = null;
            List auditDTOList = ActionUtils.getCurrentAuditsAsDTO(pRequest);
            if (null != auditDTOList) {
                auditDTO = (AuditDTO) auditDTOList.get(0);
            }
            // Si on passe par l� c'est qu'on est pass� par le menu et non par un composant directement
            // on met donc � jour la variable qui sert � savoir de quelle vue on vient
            changeWay(pRequest, "true");
            //Il faut r�initialiser le traceur historique quand on passe par l�
            resetTracker(pRequest);

            // R�cup�ration des packages
            IApplicationComponent ac = AccessDelegateHelper.getInstance("Component");
            ComponentDTO projectDTO = (ComponentDTO) WTransformerFactory.formToObj(ProjectTransformer.class, ActionUtils.getCurrentProject(pRequest))[0];
            String which = pRequest.getParameter("which");
            Object[] paramIn;
            Collection dtos = new ArrayList(0);
            if (which.equals(SqualeCommonConstants.EXCLUDED_FROM_PLAN_COMPONENTS)) {
                paramIn = new Object[] { auditDTO, projectDTO };
                dtos = (Collection) ac.execute("getExcluded", paramIn);
                pRequest.setAttribute("excluded", "true");
            } else {
                paramIn = new Object[] { projectDTO, ComponentType.PACKAGE, auditDTO, ((ComponentForm) pForm).getFilter()};
                dtos = getChildren(pRequest, ac, paramIn);
            }
            Object obj[] = { dtos };
            ComponentListForm packagesList = (ComponentListForm) WTransformerFactory.objToForm(ComponentListTransformer.class, obj);
            // s'il n'y a qu'un seul package,
            if (packagesList.getList().size() == 1 && !which.equals(SqualeCommonConstants.EXCLUDED_FROM_PLAN_COMPONENTS)) {
                // stock l'ID du composant a recuperer
                pRequest.setAttribute("component", Long.toString(((ComponentForm) packagesList.getList().get(0)).getId()));
                // On enl�ve le filtre sur les enfants
                 ((ComponentForm) pForm).setFilter("");
                forward = component(pMapping, pForm, pRequest, pResponse, which);
            } else {
                // R�cup�ration des fichiers
                ComponentListForm filesList = new ComponentListForm();
                Object results[] = { packagesList, filesList };
                pRequest.getSession().setAttribute(SqualeWebConstants.CHILDREN_KEY, results);

                forward = pMapping.findForward("component_root");

            }
        } catch (Exception e) {
            handleException(e, errors, pRequest);
        }
        // les erreurs sont affich�es sur la page speciale
        if (!errors.isEmpty()) {
            saveMessages(pRequest, errors);
            forward = pMapping.findForward("total_failure");
        }

        return forward;
    }

    /**
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward component(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) {
        // appel de la m�thode priv�e qui fait le travail avec en param�tre le status null car on n'en tient pas compte
        return component(pMapping, pForm, pRequest, pResponse, null);
    }

    /**
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @param pWhich le status du composant par rapport au plan d'action
     * @return l'action � r�aliser.
     */
    private ActionForward component(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse, String pWhich) {

        ActionForward forward = null;
        ActionErrors errors = new ActionErrors();

        try {
            // Obtention de l'id du composant dans la requ�te
            String param = (String) pRequest.getParameter("component");

            if (null == param) {
                param = (String) pRequest.getAttribute("component");
            }
            if (null != param) {
                ComponentDTO dto = new ComponentDTO();
                Long value = new Long(param);
                dto.setID(value.longValue());
                dto.setNumberOfChildren(1);
                // Obtention des informations sp�cifiques � ce composant
                AuditDTO auditDTO = null;
                List auditDTOList = ActionUtils.getCurrentAuditsAsDTO(pRequest);
                if (null != auditDTOList) {
                    auditDTO = (AuditDTO) auditDTOList.get(0);
                }
                forward = getComponentFeatures(pMapping, pForm, pRequest, pResponse, dto, auditDTO);
            } else {
                // Pas de composant identifi�, on affiche les informations
                // de plus haut niveau
                forward = root(pMapping, pForm, pRequest, pResponse);
            }
        } catch (Exception e) {
            // Traitement factoris� des exceptions
            handleException(e, errors, pRequest);
        }
        if (!errors.isEmpty()) {
            // Sauvegarde des message et transfert vers la page d'erreur
            saveMessages(pRequest, errors);
            forward = pMapping.findForward("total_failure");
        }

        return forward;
    }

    /**
     * R�cup�re les composants et les enfants.
     * 
     * @param pMapping le mapping
     * @param pForm le form
     * @param pRequest la requ�te
     * @param pResponse la r�ponse
     * @param pDto le composant dont on veut les projets
     * @param pAuditDTO l'audit pour lequel on veut les r�sultats
     * @return le forward
     * @throws Exception si un probl�me appara�t.
     */
    private ActionForward getComponentFeatures(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse, ComponentDTO pDto, final AuditDTO pAuditDTO)
        throws Exception {

        ActionForward forward = null;
        boolean reload = true;
        String which = ((String) pRequest.getParameter("which"));
        boolean excluded = (which != null && which.equals("components.excluded"));
        // R�cup�ration du composant
        while (reload) {
            IApplicationComponent ac = AccessDelegateHelper.getInstance("Component");
            Object[] paramIn = { pDto };
            pDto = (ComponentDTO) ac.execute("get", paramIn);
            // Initialise le form
            WTransformerFactory.objToForm(ComponentTransformer.class, ((WActionForm) pForm), new Object[] { pDto });
            // R�cup�ration des parents et composition du mot complet
            List parentDtoList = getParentList(pDto);

            //R�cup�ration de la liste pour l'onglet "info g�n�rale"
            Object[] infoListTab = new Object[1];
            infoListTab[0] = parentDtoList;
            ComponentListForm infoList = (ComponentListForm) WTransformerFactory.objToForm(ComponentListTransformer.class, infoListTab);
            infoList.copyValues((ComponentForm) pForm);
            pRequest.getSession().setAttribute(SqualeWebConstants.GEN_INFO, infoList);

            // Mise en place du traceur
            int size = parentDtoList.size();
            Object[] list = new Object[1];
            if (size > NUMBER_COMPONENT_TRACER) {
                list[0] = new ArrayList(parentDtoList.subList(size - NUMBER_COMPONENT_TRACER, size));
            } else {
                list[0] = parentDtoList;
            }
            updateTrackerComponent(((ComponentForm) pForm).getId(), ((ComponentForm) pForm).getName(), pRequest);
            // R�cup�ration des r�sultats du composant
            Map results = getComponentResults(((ComponentForm) pForm), pAuditDTO);
            ((ComponentForm) pForm).setResults((ResultListForm) results.get(SqualeWebConstants.RESULTS_KEY));
            ((ComponentForm) pForm).setPractices((ResultListForm) results.get(SqualeWebConstants.PRACTICES_KEY));
            // gestion du traceur, cas du composant � ajouter en fin de synth�se
            if ("true".equals(pRequest.getSession().getAttribute(SqualeWebConstants.TRACKER_RESET))) {
                // Dans ce cas, on doit ajouter le composant � la vue historique et ne pas changer
                // de type de traceur
                // pour enlever ce qui est inutile d'afficher
                StringTokenizer st = new StringTokenizer(((ComponentForm) pForm).getName(), "(");
                String url = "project_component.do?action=component&component=" + new Long(pRequest.getParameter("component")).longValue();
                updateHistTracker(st.nextToken(), url, TrackerStructure.COMPONENT_VIEW, pRequest, false);
            }
            // R�cup�ration des enfants
            reload = false;
            ((ComponentForm) pForm).setChildren(null);
            // On ne fait rien dans le cas d'une m�thode ou d'une page JSP -- suppos�e ne pas contenir de
            // descendants afin de ne pas afficher l'onglet "contenu du composant"
            if (!((ComponentForm) pForm).getType().equals(ComponentType.METHOD) && !((ComponentForm) pForm).getType().equals(ComponentType.JSP)) {
                Object[] paramIn3 = { pDto, null, pAuditDTO, ((ComponentForm) pForm).getFilter()};
                Collection childrenDTO = getChildren(pRequest, ac, paramIn3);
                ComponentListForm childrenList = (ComponentListForm) WTransformerFactory.objToForm(ComponentListTransformer.class, new Object[] { childrenDTO });
                childrenList.copyValues((ComponentForm) pForm);
                ((ComponentForm) pForm).setChildren(childrenList);
                // Dans le cas d'un package avec un seul fils, on descend
                // dans la hi�rarchie pour afficher directement le premier package
                // pertinent
                if (childrenList.getList().size() == 1 && ((ComponentForm) pForm).getResults().getList().size() == 0) {
                    ComponentForm child = (ComponentForm) childrenList.getList().get(0);
                    if (child.getType().equals(ComponentType.PACKAGE)) {
                        pDto.setID(child.getId());
                        reload = true;
                        // reset du form
                         ((ComponentForm) pForm).reset(pMapping, pRequest);
                    }
                }
            }
        }
        forward = pMapping.findForward("component");
        return forward;
    }

    /**
     * @param pRequest la requ�te
     * @param ac l'application component access
     * @param paramIn les param�tres d'appel
     * @return les enfants
     * @throws JrafEnterpriseException si erreur
     */
    private Collection getChildren(HttpServletRequest pRequest, IApplicationComponent ac, Object[] paramIn) throws JrafEnterpriseException {
        // On supprime le message en session si il existe
        pRequest.getSession().removeAttribute(TOO_MUCH_COMPONENTS_MSG);
        Integer nbcomponents = (Integer) ac.execute("countChildren", paramIn);
        if (nbcomponents.intValue() > MAX_COMPONENTS) {
            // On affichera un message d'avertissement que l'on met en session
            // car on veut qu'il s'affiche tous le temps
            String message = (String)WebMessages.getString(pRequest.getLocale(),"only.thousand.components", new String[]{nbcomponents.toString()});
            pRequest.getSession().setAttribute(TOO_MUCH_COMPONENTS_MSG, message);
        }
        return (Collection) ac.execute("getChildren", paramIn);
    }

    /**
     * R�cup�re les r�sultats d'un composant.
     * 
     * @param pComponent le composant pour lequel on d�sire les r�sultats.
     * @param pAudit l'audit des r�sultats.
     * @return une map contenant les r�sultats. 
     * @throws Exception si un probl�me de transformation ou JRAF appara�t.
     */
    private Map getComponentResults(final ComponentForm pComponent, final AuditDTO pAudit) throws Exception {
        IApplicationComponent ac = AccessDelegateHelper.getInstance("Results");
        ComponentDTO dto = (ComponentDTO) (WTransformerFactory.formToObj(ComponentTransformer.class, pComponent)[0]);
        Map results = new TreeMap();
        ResultListForm measures = new ResultListForm();
        ResultListForm practices = new ResultListForm();
        // Mesures
        Object[] paramIn = { pAudit, dto, Boolean.TRUE };
        ResultsDTO resultsDTO = (ResultsDTO) ac.execute("getComponentResults", paramIn);
        LinkedList values = new LinkedList();
        // Ajout des valeurs de mesures
        if (null != resultsDTO) {
            values.addAll((Collection) resultsDTO.getResultMap().get(dto));
            Object obj[] = { resultsDTO.getResultMap().get(null), values };
            measures = (ResultListForm) WTransformerFactory.objToForm(ResultListTransformer.class, obj);
        }
        // Pratiques
        paramIn[2] = Boolean.FALSE;
        ResultsDTO practicesDTO = (ResultsDTO) ac.execute("getComponentResults", paramIn);
        values = new LinkedList();
        // Ajout des valeurs de pratiques
        if (null != practicesDTO) {
            values.addAll((Collection) practicesDTO.getResultMap().get(dto));
            Object obj[] = { practicesDTO.getResultMap().get(null), values };
            practices = (ResultListForm) WTransformerFactory.objToForm(ResultListTransformer.class, obj);
        }
        results.put(SqualeWebConstants.RESULTS_KEY, measures);
        results.put(SqualeWebConstants.PRACTICES_KEY, practices);
        return results;
    }

    /**
     * Permet de r�cup�rer la liste des ComponentrDTO parents 
     * @param pDTO ComponentDTO fils 
     * @return List de ComponentDTO parents tri�s 
     * @throws Exception exception Jraf
     */
    private List getParentList(ComponentDTO pDTO) throws Exception {

        // Initialisation
        List parentDtoList = null;

        Object[] paramIn = { pDTO, null };
        IApplicationComponent ac = AccessDelegateHelper.getInstance("Component");
        parentDtoList = (List) ac.execute("getParentsComponent", paramIn);

        return parentDtoList;
    }

    /**
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward update(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) {
        ActionForward forward = null;
        ActionErrors errors = new ActionErrors();
        try {

            // Obtention de l'id du composant dans la requ�te
            String param = (String) pRequest.getParameter("component");
            ComponentDTO dto = new ComponentDTO();
            Long value = new Long(param);
            dto.setID(value.longValue());
            dto.setNumberOfChildren(1);
            // Obtention des informations sp�cifiques � ce composant
            AuditDTO auditDTO = null;
            List auditDTOList = ActionUtils.getCurrentAuditsAsDTO(pRequest);
            if (null != auditDTOList) {
                auditDTO = (AuditDTO) auditDTOList.get(0);
            }
            IApplicationComponent ac = AccessDelegateHelper.getInstance("Component");
            pRequest.setAttribute("component", value.toString());
            // R�cup�re les informations li�es � la modification
            String justif = ((ComponentForm) pForm).getJustification();
            boolean excluded = ((ComponentForm) pForm).getExcludedFromActionPlan();
            if (excluded) {
                pRequest.setAttribute("which", "components.excluded");
            }
            // Rempli le form avec les autres caract�ristiques m�tiers necessaires pour la transformation
            // pr�alable � l'enregistrement en base
            forward = getComponentFeatures(pMapping, pForm, pRequest, pResponse, dto, auditDTO);
            // remet les bonnes valeurs concernant la justification et l'exclusion dans le form
             ((ComponentForm) pForm).setExcludedFromActionPlan(excluded);
            ((ComponentForm) pForm).setJustification(justif);
            ComponentDTO componentToUpdate = (ComponentDTO) WTransformerFactory.formToObj(ComponentTransformer.class, (ComponentForm) pForm)[0];
            Object[] paramIn = new Object[] { componentToUpdate };
            ac.execute("updateComponent", paramIn);
            forward = pMapping.findForward("component");

        } catch (Exception e) {
            // Traitement factoris� des exceptions
            handleException(e, errors, pRequest);
        }
        if (!errors.isEmpty()) {
            // Sauvegarde des message et transfert vers la page d'erreur
            saveMessages(pRequest, errors);
            forward = pMapping.findForward("total_failure");
        }
        return forward;
    }
}
