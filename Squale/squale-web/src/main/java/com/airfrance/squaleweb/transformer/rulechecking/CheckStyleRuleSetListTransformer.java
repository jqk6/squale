package com.airfrance.squaleweb.transformer.rulechecking;

import com.airfrance.squaleweb.applicationlayer.formbean.rulechecking.CheckstyleRuleSetListForm;
import com.airfrance.welcom.struts.bean.WActionForm;
import com.airfrance.welcom.struts.transformer.WTransformerException;

/**
 * Transformation d'une liste de jeux de r�gles checkstyle
 */
public class CheckStyleRuleSetListTransformer extends AbstractRuleSetListTransformer {

    /**
     * Constructeur
     */
    public CheckStyleRuleSetListTransformer() {
        super(CheckstyleRuleSetTransformer.class);
    }

    /**
     * @param pObject le tableau de CheckstyleDTO � transformer en formulaires.
     * @throws WTransformerException si un pb appara�t.
     * @return le formulaire associ�
     */
    public WActionForm objToForm(Object[] pObject) throws WTransformerException {
        CheckstyleRuleSetListForm form = new CheckstyleRuleSetListForm();
        objToForm(pObject, form);
        return form;
    }

}
