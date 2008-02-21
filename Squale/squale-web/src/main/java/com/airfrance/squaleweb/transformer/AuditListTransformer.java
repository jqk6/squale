package com.airfrance.squaleweb.transformer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.ListIterator;

import com.airfrance.squalecommon.datatransfertobject.component.AuditDTO;
import com.airfrance.squaleweb.applicationlayer.formbean.component.AuditForm;
import com.airfrance.squaleweb.applicationlayer.formbean.component.AuditListForm;
import com.airfrance.squaleweb.comparator.AuditComparator;
import com.airfrance.welcom.struts.bean.WActionForm;
import com.airfrance.welcom.struts.transformer.WTransformerException;
import com.airfrance.welcom.struts.transformer.WTransformerFactory;

/**
 * Transformer des listes d'audits.
 * 
 * @author M400842
 */
public class AuditListTransformer extends AbstractListTransformer {

    /**
     * @param pObject le tableau de AuditDTO � transformer en formulaires.
     * @throws WTransformerException si un pb appara�t.
     * @return le formulaire associ�
     */
    public WActionForm objToForm(Object[] pObject) throws WTransformerException {
        AuditListForm form = new AuditListForm();
        objToForm(pObject, form);
        return form;
    }

    /**
     * @param pObject le tableau de AuditDTO � transformer en formulaires.
     * @param pForm le formulaire � remplir.
     * @throws WTransformerException si un pb appara�t.
     */
    public void objToForm(Object[] pObject, WActionForm pForm) throws WTransformerException {
        ArrayList listAuditsDTO = (ArrayList) pObject[0];
        AuditListForm auditListForm = (AuditListForm) pForm;
        ArrayList listAuditForm = new ArrayList();
        if(null != listAuditsDTO){
            Iterator it = listAuditsDTO.iterator();
            AuditDTO dto = null;
            while(it.hasNext()){
                dto = (AuditDTO)it.next();
                listAuditForm.add(WTransformerFactory.objToForm(AuditTransformer.class,dto));
            }
        }
        auditListForm.setAudits(listAuditForm);
    }

    /**
     * @param pForm le formulaire � lire.
     * @param pObject le tableau de AuditDTO qui r�cup�re les donn�es du formulaire.
     * @throws WTransformerException si un pb appara�t.
     */
    public void formToObj(WActionForm pForm, Object[] pObject) throws WTransformerException {
        ArrayList listObject = (ArrayList) pObject[0];
        ListIterator it = ((AuditListForm) pForm).getAudits().listIterator();
        while (it.hasNext()) {
            listObject.add(WTransformerFactory.formToObj(AuditTransformer.class, (AuditForm) it.next())[0]);
        }
        AuditComparator ac = new AuditComparator();
        Collections.sort(listObject, ac);
    }
}
