package com.airfrance.squaleweb.applicationlayer.action.config;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.airfrance.jraf.helper.AccessDelegateHelper;
import com.airfrance.jraf.spi.accessdelegate.IApplicationComponent;
import com.airfrance.squaleweb.applicationlayer.action.accessRights.AdminAction;
import com.airfrance.squaleweb.applicationlayer.formbean.UploadFileForm;

/**
 * Importation d'une configuratiopn Squalix
 */
public class ConfigCreationAction extends AdminAction {

    /**
     * Importation d'un fichier de configuration Squalix
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward execute(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) {
        // On va cr�er la configuration en traitant les erreurs lors du paring ou de l'enregistrement en base.
        ActionErrors errors = new ActionErrors();
        ActionForward forward;
        try {
            UploadFileForm form = (UploadFileForm) pForm;
            IApplicationComponent ac = AccessDelegateHelper.getInstance("SqualixConfig");
            StringBuffer acErrors = new StringBuffer();
            InputStream is = form.getInputStream();
            // On importe la configuration d�finie dans le fichier
            ac.execute("importConfig", new Object[] { is, acErrors });
            if (acErrors.length() > 0) {
                // Affichage des messages d'erreur
                ActionMessage error = new ActionMessage("config_import.errors", acErrors.toString());
                errors.add(ActionMessages.GLOBAL_MESSAGE, error);
                forward = pMapping.findForward("fail");
            } else {
                is = form.getInputStream();
                // On cr�e la configuration
                ac.execute("createConfig", new Object[] { is, acErrors });
                if (acErrors.length() > 0) {
                    // Affichage des messages d'erreur
                    ActionMessage error = new ActionMessage("config_insert_data.errors", acErrors.toString());
                    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
                    forward = pMapping.findForward("fail");
                } else {
                    forward = pMapping.findForward("success");
                }
            }
        } catch (Exception e) {
            // Traitement des exceptions
            handleException(e, errors, pRequest);
            forward = pMapping.findForward("total_failure");
        }
        if (!errors.isEmpty()) {
            saveMessages(pRequest, errors);
        }
        //On est pass� par un menu donc on r�initialise le traceur
        resetTracker(pRequest);
        return forward;
    }
}
