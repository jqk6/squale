package com.airfrance.squaleweb.transformer.rulechecking;

import com.airfrance.squaleweb.applicationlayer.formbean.rulechecking.CppTestRuleSetListForm;
import com.airfrance.welcom.struts.bean.WActionForm;
import com.airfrance.welcom.struts.transformer.WTransformerException;

/**
 * Transformation d'une liste de tuleset CppTest
 */
public class CppTestRuleSetListTransformer extends AbstractRuleSetListTransformer {

    /**
     * Constructeur
     */
    public CppTestRuleSetListTransformer() {
        super(CppTestRuleSetTransformer.class);
    }

    /**
     * @param pObject le tableau de CheckstyleDTO � transformer en formulaires.
     * @throws WTransformerException si un pb appara�t.
     * @return le formulaire associ�
     */
    public WActionForm objToForm(Object[] pObject) throws WTransformerException {
        CppTestRuleSetListForm form = new CppTestRuleSetListForm();
        objToForm(pObject, form);
        return form;
    }

}
