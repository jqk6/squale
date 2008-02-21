package com.airfrance.squaleweb.applicationlayer.formbean.reference;

import java.util.ArrayList;
import java.util.List;

import com.airfrance.squaleweb.applicationlayer.formbean.RootForm;

/**
 * Formulaire contenant la liste des formulaires du r�f�rentiel
 */
public class ReferenceListForm extends RootForm {

    /**
     * Liste des r�f�rences, liste de ReferenceForm
     */
    private List mList = new ArrayList(0);

    /**
     * @return la liste des r�f�rences
     */
    public List getList() {
        return mList;
    }
    
    /**
     * 
     * @param pList la liste des r�f�rence
     */
    public void setList(List pList) {
        mList = pList;
    }
}
