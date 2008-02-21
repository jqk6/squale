package com.airfrance.squaleweb.applicationlayer.formbean.mails;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMapping;

import com.airfrance.squaleweb.applicationlayer.formbean.RootForm;
import com.airfrance.squaleweb.applicationlayer.formbean.component.ApplicationListForm;

/**
 * 
 */
public class MailForm extends RootForm {
    
    /** Constante pour indiquer qu'on s�lectionne toutes les applications */
    public static final String ALL_APPLICATIONS = "Toutes les applications";

    /** la liste des appli concern�e pour le choix*/
    private ApplicationListForm mApplicationFormsList = new ApplicationListForm();

    /** l'application qui a �t� choisie */
    private String mAppliName = "";

    /** l'objet du mail */
    private String mObject = "";

    /** le contenu du mail */
    private String mContent = "";

    /**
     * @return le contenu du mail
     */
    public String getContent() {
        return mContent;
    }

    /**
     * @return l'objet du mail
     */
    public String getObject() {
        return mObject;
    }

    /**
     * @param pContent le nouveau contenu du mail
     */
    public void setContent(String pContent) {
        mContent = pContent;
    }

    /**
     * @param pObject le nouvel objet du mail
     */
    public void setObject(String pObject) {
        mObject = pObject;
    }

    /**
     * @return la liste des applications
     */
    public ApplicationListForm getApplicationFormsList() {
        return mApplicationFormsList;
    }

    /**
     * @param collection la nouvelle collection d'appli
     */
    public void setApplicationFormsList(ApplicationListForm collection) {
        mApplicationFormsList = collection;
    }

    /**
     * @return l'application s�lectionn�e
     */
    public String getAppliName() {
        return mAppliName;
    }

    /**
     * @param pAppliName la nouvelle appli s�lectionn�e
     */
    public void setAppliName(String pAppliName) {
        mAppliName = pAppliName;
    }

    /**
     * @see com.airfrance.welcom.struts.bean.WActionForm#wValidate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     * {@inheritDoc}
     */
    public void wValidate(ActionMapping mapping, HttpServletRequest request) {
        super.wValidate(mapping, request);
        // on v�rifie que si on arrive depuis le menu, on ne doit pas v�rifier que 
        // les champs sont bien remplis car il y a une �tape 
        if (request.getParameter("fromMenu") == null) {
            // L'objet du mail
            setObject(getObject().trim());
            if (getObject().length() == 0) {
                addError("object", new ActionError("error.field.required"));
            }
            // Le message
            setContent(getContent().trim());
            if (getContent().length() == 0) {
                addError("content", new ActionError("error.field.required"));
            }
        }

    }

    /** 
     * {@inheritDoc}
     * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        setContent("");
        setObject("");
        setAppliName("");
    }

}
