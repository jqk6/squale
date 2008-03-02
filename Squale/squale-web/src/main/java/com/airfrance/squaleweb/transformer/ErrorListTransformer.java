package com.airfrance.squaleweb.transformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.airfrance.squalecommon.datatransfertobject.result.ErrorDTO;
import com.airfrance.squaleweb.applicationlayer.formbean.component.ErrorForm;
import com.airfrance.squaleweb.applicationlayer.formbean.component.ErrorListForm;
import com.airfrance.squaleweb.resources.WebMessages;
import com.airfrance.welcom.struts.bean.WActionForm;
import com.airfrance.welcom.struts.transformer.WITransformer;
import com.airfrance.welcom.struts.transformer.WTransformArrayList;
import com.airfrance.welcom.struts.transformer.WTransformerException;
import com.airfrance.welcom.struts.transformer.WTransformerFactory;

/**
 * Transforme des listes d'erreurs Form <-> DTO
 * 
 * @author M400842
 */
public class ErrorListTransformer
    extends AbstractListTransformer
{
    /**
     * @param pObject le tableau de ProjectDTO � transformer en formulaires.
     * @throws WTransformerException si un pb appara�t.
     * @return le formulaire associ�
     */
    public WActionForm objToForm( Object[] pObject )
        throws WTransformerException
    {
        ErrorListForm form = new ErrorListForm();
        objToForm( pObject, form );
        return form;
    }

    /**
     * @param pObject le tableau de ProjectDTO � transformer en formulaires.
     * @param pForm le formulaire � remplir.
     * @throws WTransformerException si un pb appara�t.
     */
    public void objToForm( Object[] pObject, WActionForm pForm )
        throws WTransformerException
    {
        Collection dto = (Collection) pObject[1];
        HashMap map = new HashMap();
        ErrorListForm form = (ErrorListForm) pForm;
        if ( null != dto )
        {
            Iterator it = dto.iterator();
            while ( it.hasNext() )
            {
                ErrorForm error =
                    (ErrorForm) WTransformerFactory.objToForm( ErrorTransformer.class, (ErrorDTO) it.next() );
                // On ins�re dans la map avec le message en cl�
                if ( map.get( error.getMessage() ) != null )
                {
                    error.setNbOcc( error.getNbOcc() + 1 );
                }
                map.put( error.getMessage(), error );
            }
        }
        form.setList( new ArrayList( map.values() ) );
        form.setTaskName( WebMessages.getString( (String) pObject[0] + ".name" ) );
        form.setMaxErrorLevel( (String) pObject[2] );
    }

    /**
     * @param pForm le formulaire � lire.
     * @param pObject le tableau de ProjectDTO qui r�cup�re les donn�es du formulaire.
     * @throws WTransformerException si un pb appara�t.
     */
    public void formToObj( WActionForm pForm, Object[] pObject )
        throws WTransformerException
    {
        ArrayList listObject = (ArrayList) pObject[0];
        ArrayList listForm = (ArrayList) ( (ErrorListForm) pForm ).getList();
        WITransformer auditTransformer = WTransformerFactory.getSingleTransformer( AuditTransformer.class );
        WTransformArrayList.formToObj( listForm, listObject, auditTransformer );
    }
}
