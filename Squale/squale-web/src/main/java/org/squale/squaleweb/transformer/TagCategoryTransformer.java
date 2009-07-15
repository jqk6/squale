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

import org.squale.squalecommon.datatransfertobject.tag.TagCategoryDTO;
import org.squale.squaleweb.applicationlayer.formbean.tag.TagCategoryForm;
import org.squale.welcom.struts.bean.WActionForm;
import org.squale.welcom.struts.transformer.WITransformer;
import org.squale.welcom.struts.transformer.WTransformerException;

/**
 * Transformateurs DTO <-> Form pour les applications
 * 
 * @author M400842
 */
public class TagCategoryTransformer
    implements WITransformer
{

    /**
     * Object --> Form
     * @param pObject l'objet � transformer
     * @throws WTransformerException si un pb apparait.
     * @return le formulaire.
     */
    public WActionForm objToForm( Object[] pObject )
        throws WTransformerException
    {
        TagCategoryForm form = new TagCategoryForm();
        objToForm( pObject, form );
        return form;
    }

    /**
     * Object --> Form
     * @param pObject l'objet � transformer
     * @param pForm le formulaire � remplir.
     * @throws WTransformerException si un pb apparait.
     */
    public void objToForm( Object[] pObject, WActionForm pForm )
        throws WTransformerException
    {
        TagCategoryDTO dto = (TagCategoryDTO) pObject[0];
        TagCategoryForm form = (TagCategoryForm) pForm;

        form.setId( dto.getID() );
        form.setName( "" + dto.getName() );
        form.setDescription( "" + dto.getDescription() );
    }

    /**
     * Form --> Object
     * @param pForm le formulaire � lire.
     * @throws WTransformerException si un pb apparait.
     * @return le tableaux des objets.
     */
    public Object[] formToObj( WActionForm pForm )
        throws WTransformerException
    {
        Object[] obj = { new TagCategoryDTO() };
        formToObj( pForm, obj );
        return obj;
    }

    /**
     * Form --> Object
     * @param pObject l'objet � remplir
     * @param pForm le formulaire � lire.
     * @throws WTransformerException si un pb apparait.
     */
    public void formToObj( WActionForm pForm, Object[] pObject )
        throws WTransformerException
    {
        TagCategoryDTO dto = (TagCategoryDTO) pObject[0];
        TagCategoryForm form = (TagCategoryForm) pForm;
        dto.setID( form.getId() );
        dto.setName( form.getName() );
        dto.setDescription( form.getDescription() );
    }
}
