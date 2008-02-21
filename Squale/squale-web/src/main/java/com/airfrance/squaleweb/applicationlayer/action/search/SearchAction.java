package com.airfrance.squaleweb.applicationlayer.action.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.airfrance.jraf.helper.AccessDelegateHelper;
import com.airfrance.jraf.spi.accessdelegate.IApplicationComponent;
import com.airfrance.squaleweb.applicationlayer.action.accessRights.DefaultAction;
import com.airfrance.squaleweb.transformer.SearchProjectTransformer;
import com.airfrance.welcom.struts.bean.WActionForm;
import com.airfrance.welcom.struts.transformer.WTransformerFactory;

/**
 * Recherche un projet.
 */
public class SearchAction extends DefaultAction {

    /**
     * Recherche un projet
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward searchProject(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) {

        ActionMessages errors = new ActionMessages();
        ActionForward forward = null;

        try {
            String firstCallParam = pRequest.getParameter("firstCall");
            // On ne fait le traitement si on a pas cliqu� sur le bouton "rechercher".
            // Le param�tre "firstCall" l'indique
            if (null == firstCallParam) {
                // On r�cup�re les donn�es du formulaire de recherche d'un projet
                Object[] data = WTransformerFactory.formToObj(SearchProjectTransformer.class, (WActionForm) pForm);
                String appli = (String) data[0];
                String project = (String) data[1];
                // R�cup�ration de la liste des applications
                List applications = getUserApplicationListAsDTO(pRequest);
                // Obtention de la couche m�tier
                IApplicationComponent ac = AccessDelegateHelper.getInstance("Component");
                Object[] paramIn = { applications, appli, project };
                // Appel de la couche m�tier pour obtenir les projets correspondants aux crit�res
                Map projectsDto = (Map) ac.execute("getProjectsWithLastAudit", paramIn);
                // Transformation en formulaire
                WTransformerFactory.objToForm(SearchProjectTransformer.class, (WActionForm) pForm, new Object[] { projectsDto, applications });
            } else {
                // On reset le formulaire
                pForm = WTransformerFactory.objToForm(SearchProjectTransformer.class, new Object[]{new HashMap(), new ArrayList()});
                pRequest.getSession().setAttribute("searchProjectForm", pForm);
            }
            forward = pMapping.findForward("list");
        } catch (Exception e) {
            // Traitement factoris� des exceptions et transfert vers la page d'erreur
            handleException(e, errors, pRequest);
            //saveMessages(pRequest, messages);
            forward = pMapping.findForward("total_failure");
        }
        return forward;
    }
}
