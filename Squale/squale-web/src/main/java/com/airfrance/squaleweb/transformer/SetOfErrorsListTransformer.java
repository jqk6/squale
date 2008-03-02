package com.airfrance.squaleweb.transformer;

import java.util.ArrayList;

import com.airfrance.squaleweb.applicationlayer.formbean.component.SetOfErrorsListForm;
import com.airfrance.welcom.struts.bean.WActionForm;
import com.airfrance.welcom.struts.transformer.WITransformer;
import com.airfrance.welcom.struts.transformer.WTransformerException;

/**
 */
public class SetOfErrorsListTransformer
    implements WITransformer
{

    /**
     * @param pObject l'objet � transformer
     * @throws WTransformerException si un pb apparait.
     * @return le formulaire.
     */
    public WActionForm objToForm( Object[] pObject )
        throws WTransformerException
    {
        SetOfErrorsListForm form = new SetOfErrorsListForm();
        objToForm( pObject, form );
        return form;
    }

    /**
     * @param pObject l'objet � transformer
     * @param pForm le formulaire � remplir.
     * @throws WTransformerException si un pb apparait.
     */
    public void objToForm( Object[] pObject, WActionForm pForm )
        throws WTransformerException
    {
        SetOfErrorsListForm form = (SetOfErrorsListForm) pForm;
        form.setSetOfErrors( (ArrayList) pObject[0] );
    }

    // il n'est pas n�c�ssaire de surcharger les autres m�thodes
    // mais on lance une exception (code d�fensif) pour signaler
    // qu'il ne faut pas les utiliser

    /**
     * @param pForm le formulaire � remplir.
     * @throws WTransformerException car on ne peut pas l'appeler
     * @return plus rien car ne doit pas etre appel�e
     */
    public Object[] formToObj( WActionForm pForm )
        throws WTransformerException
    {
        throw new WTransformerException();
    }

    /**
     * @param pObject l'objet � transformer
     * @param pForm le formulaire � remplir.
     * @throws WTransformerException car on ne peut pas l'appeler
     */
    public void formToObj( WActionForm pForm, Object[] pObject )
        throws WTransformerException
    {
        throw new WTransformerException();
    }
}
