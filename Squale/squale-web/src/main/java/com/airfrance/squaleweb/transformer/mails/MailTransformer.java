package com.airfrance.squaleweb.transformer.mails;

import java.util.ArrayList;

import com.airfrance.squaleweb.applicationlayer.formbean.component.ApplicationListForm;
import com.airfrance.squaleweb.applicationlayer.formbean.mails.MailForm;
import com.airfrance.squaleweb.transformer.ApplicationListTransformer;
import com.airfrance.welcom.struts.bean.WActionForm;
import com.airfrance.welcom.struts.transformer.WITransformer;
import com.airfrance.welcom.struts.transformer.WTransformerException;
import com.airfrance.welcom.struts.transformer.WTransformerFactory;

/**
 * Transformeur de mail
 */
public class MailTransformer implements WITransformer {

    /** 
     * {@inheritDoc}
     * @see com.airfrance.welcom.struts.transformer.WITransformer#objToForm(java.lang.Object[])
     */
    public WActionForm objToForm(Object[] pObject) throws WTransformerException {
        MailForm form = new MailForm();
        objToForm(pObject, form);
        return form;
    }

    /** 
     * {@inheritDoc}
     * @see com.airfrance.welcom.struts.transformer.WITransformer#objToForm(java.lang.Object[], com.airfrance.welcom.struts.bean.WActionForm)
     */
    public void objToForm(Object[] pObject, WActionForm pForm) throws WTransformerException {
        ArrayList listDTO = (ArrayList) pObject[0];
        MailForm mailForm = (MailForm) pForm;
        mailForm.setApplicationFormsList((ApplicationListForm) WTransformerFactory.objToForm(ApplicationListTransformer.class, listDTO));
    }

    /** 
     * {@inheritDoc}
     * @see com.airfrance.welcom.struts.transformer.WITransformer#formToObj(com.airfrance.welcom.struts.bean.WActionForm)
     */
    public Object[] formToObj(WActionForm pForm) throws WTransformerException {
        throw new WTransformerException("not implemented");
    }

    /** 
     * {@inheritDoc}
     * @see com.airfrance.welcom.struts.transformer.WITransformer#formToObj(com.airfrance.welcom.struts.bean.WActionForm, java.lang.Object[])
     */
    public void formToObj(WActionForm arg0, Object[] arg1) throws WTransformerException {
        throw new WTransformerException("not implemented");
    }

}