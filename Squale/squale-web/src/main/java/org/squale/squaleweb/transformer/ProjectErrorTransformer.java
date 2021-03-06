/**
 * Copyright (C) 2008-2010, Squale Project - http://www.squale.org
 *
 * This file is part of Squale.
 *
 * Squale is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * Squale is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Squale.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.squale.squaleweb.transformer;

import java.util.List;

import org.squale.squaleweb.applicationlayer.formbean.component.ProjectForm;
import org.squale.welcom.struts.bean.WActionForm;
import org.squale.welcom.struts.transformer.WITransformer;
import org.squale.welcom.struts.transformer.WTransformerException;

/**
 */
public class ProjectErrorTransformer
    extends ProjectTransformer
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
        return (ProjectForm) super.objToForm( pObject );
    }

    /**
     * @param pObject l'objet � transformer
     * @param pForm le formulaire � remplir.
     * @throws WTransformerException si un pb apparait.
     */
    public void objToForm( Object[] pObject, WActionForm pForm )
        throws WTransformerException
    {
        ProjectForm form = (ProjectForm) pForm;
        super.objToForm( new Object[] { pObject[0] }, form );
        if ( null != pObject[1] )
        {
            // On modifie la r�partition des erreurs
            form.getErrorsRepartition()[ProjectForm.ERROR_ID] =
                ( ( (Integer[]) pObject[1] )[ProjectForm.ERROR_ID].intValue() );
            form.getErrorsRepartition()[ProjectForm.WARNING_ID] =
                ( ( (Integer[]) pObject[1] )[ProjectForm.WARNING_ID].intValue() );
            form.getErrorsRepartition()[ProjectForm.INFO_ID] =
                ( ( (Integer[]) pObject[1] )[ProjectForm.INFO_ID].intValue() );
        }
        // On modifie les t�ches en �chec
        form.setFailedTasks( (List) pObject[2] );
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
