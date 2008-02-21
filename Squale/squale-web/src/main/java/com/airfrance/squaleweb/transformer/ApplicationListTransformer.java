package com.airfrance.squaleweb.transformer;

import java.util.ArrayList;
import java.util.ListIterator;

import com.airfrance.squaleweb.applicationlayer.formbean.component.ApplicationForm;
import com.airfrance.squaleweb.applicationlayer.formbean.component.ApplicationListForm;
import com.airfrance.welcom.struts.bean.WActionForm;
import com.airfrance.welcom.struts.transformer.WITransformer;
import com.airfrance.welcom.struts.transformer.WTransformArrayList;
import com.airfrance.welcom.struts.transformer.WTransformerException;
import com.airfrance.welcom.struts.transformer.WTransformerFactory;

/**
 * Transformer des listes d'applications.
 * 
 * @author M400842
 *
 */
public class ApplicationListTransformer extends AbstractListTransformer {

    /**
     * @param pObject le tableau de ProjectDTO � transformer en formulaires.
     * @throws WTransformerException si un pb appara�t.
     * @return le formulaire associ�
     */
    public WActionForm objToForm(Object[] pObject) throws WTransformerException {
        ApplicationListForm form = new ApplicationListForm();
        objToForm(pObject, form);
        return form;
    }

    /**
     * @param pObject le tableau de ProjectDTO � transformer en formulaires.
     * @param pForm le formulaire � remplir.
     * @throws WTransformerException si un pb appara�t.
     */
    public void objToForm(Object[] pObject, WActionForm pForm) throws WTransformerException {
        ArrayList listDTO = (ArrayList) pObject[0];
        ApplicationListForm listForm = (ApplicationListForm) pForm;
        WITransformer transformer = WTransformerFactory.getSingleTransformer(ApplicationTransformer.class);
        ArrayList list = WTransformArrayList.objToForm(listDTO, transformer);
        listForm.setList(list);
    }

    /**
     * @param pForm le formulaire � lire.
     * @param pObject le tableau de ProjectDTO qui r�cup�re les donn�es du formulaire.
     * @throws WTransformerException si un pb appara�t.
     */
    public void formToObj(WActionForm pForm, Object[] pObject) throws WTransformerException {
        ArrayList listObject = (ArrayList) pObject[0];
        if (null != ((ApplicationListForm) pForm).getList()) {
            ListIterator it = ((ApplicationListForm) pForm).getList().listIterator();
            while (it.hasNext()) {
                listObject.add(WTransformerFactory.formToObj(ApplicationTransformer.class, (ApplicationForm) it.next())[0]);
            }
        }
    }

}
