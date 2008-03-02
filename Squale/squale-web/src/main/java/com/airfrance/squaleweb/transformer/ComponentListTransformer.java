package com.airfrance.squaleweb.transformer;

import java.util.ArrayList;
import java.util.ListIterator;

import com.airfrance.squalecommon.datatransfertobject.component.ComponentDTO;
import com.airfrance.squaleweb.applicationlayer.formbean.results.ComponentForm;
import com.airfrance.squaleweb.applicationlayer.formbean.results.ComponentListForm;
import com.airfrance.welcom.struts.bean.WActionForm;
import com.airfrance.welcom.struts.transformer.WTransformerException;
import com.airfrance.welcom.struts.transformer.WTransformerFactory;

/**
 * @author M400842
 */
public class ComponentListTransformer
    extends AbstractListTransformer
{

    /**
     * @param pObject le tableau de ComponentDTO � transformer en formulaires.
     * @throws WTransformerException si un pb appara�t.
     * @return le formulaire associ�
     */
    public WActionForm objToForm( Object[] pObject )
        throws WTransformerException
    {
        ComponentListForm form = new ComponentListForm();
        objToForm( pObject, form );
        return form;
    }

    /**
     * @param pObject le tableau de ComponentDTO � transformer en formulaires.
     * @param pForm le formulaire � remplir.
     * @throws WTransformerException si un pb appara�t.
     */
    public void objToForm( Object[] pObject, WActionForm pForm )
        throws WTransformerException
    {
        ArrayList listDTO = (ArrayList) pObject[0];
        ArrayList result = new ArrayList();
        ListIterator it = listDTO.listIterator();
        while ( it.hasNext() )
        {
            result.add( WTransformerFactory.objToForm( ComponentTransformer.class, (ComponentDTO) it.next() ) );
        }
        ComponentListForm form = (ComponentListForm) pForm;
        form.setList( result );
    }

    /**
     * @param pForm le formulaire � lire.
     * @param pObject le tableau deComponentDTO qui r�cup�re les donn�es du formulaire.
     * @throws WTransformerException si un pb appara�t.
     */
    public void formToObj( WActionForm pForm, Object[] pObject )
        throws WTransformerException
    {
        ArrayList listObject = (ArrayList) pObject[0];
        if ( null != ( (ComponentListForm) pForm ).getList() )
        {
            ListIterator it = ( (ComponentListForm) pForm ).getList().listIterator();
            while ( it.hasNext() )
            {
                listObject.add( WTransformerFactory.formToObj( ComponentTransformer.class, (ComponentForm) it.next() )[0] );
            }
        }
    }
}
