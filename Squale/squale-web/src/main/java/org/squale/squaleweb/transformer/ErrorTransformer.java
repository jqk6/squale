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

import java.util.Locale;

import org.squale.squalecommon.datatransfertobject.result.ErrorDTO;
import org.squale.squaleweb.applicationlayer.formbean.component.ErrorForm;
import org.squale.squaleweb.resources.WebMessages;
import org.squale.welcom.struts.bean.WActionForm;
import org.squale.welcom.struts.transformer.WITransformer;
import org.squale.welcom.struts.transformer.WTransformerException;

/**
 * Transformer ErrorDTO <-> ErrorForm
 * 
 * @author M400842
 */
public class ErrorTransformer
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
        ErrorForm form = new ErrorForm();
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
        ErrorDTO dto = (ErrorDTO) pObject[0];
        ErrorForm form = (ErrorForm) pForm;
        form.setLevel( dto.getLevel() );
        // Code d�fensif dans le cas o� le message serait nul.
        if ( null == dto.getMessageKey() )
        {
            dto.setMessageKey( "No message available for this error" );
        }
        form.setMessage( dto.getMessageKey().replaceAll( "<", "&lt;" ).replaceAll( "\n", "<br/>" ) );
        form.setErrorProjectId( dto.getProjectId() );
        form.setAuditId( dto.getAuditId() );
        if (pObject.length == 2){
            form.setTaskName( WebMessages.getString( (Locale) pObject[1], dto.getTaskName() + ".name" ) );
        } else {
            form.setTaskName( WebMessages.getString( dto.getTaskName() + ".name" ) );
        }
    }

    /**
     * @param pForm le formulaire � lire.
     * @throws WTransformerException si un pb apparait.
     * @return le tableaux des objets.
     */
    public Object[] formToObj( WActionForm pForm )
        throws WTransformerException
    {
        Object[] obj = { new ErrorDTO() };
        formToObj( pForm, obj );
        return obj;
    }

    /**
     * @param pObject l'objet � remplir
     * @param pForm le formulaire � lire.
     * @throws WTransformerException si un pb apparait.
     */
    public void formToObj( WActionForm pForm, Object[] pObject )
        throws WTransformerException
    {
        ErrorForm form = (ErrorForm) pForm;
        ErrorDTO dto = (ErrorDTO) pObject[0];
        dto.setAuditId( form.getAuditId() );
        dto.setProjectId( form.getErrorProjectId() );
        dto.setTaskName( form.getTaskName() );
    }
}
