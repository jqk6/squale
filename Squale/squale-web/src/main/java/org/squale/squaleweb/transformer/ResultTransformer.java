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

import org.squale.squalecommon.datatransfertobject.result.QualityResultDTO;
import org.squale.squaleweb.applicationlayer.formbean.results.ResultForm;
import org.squale.welcom.struts.bean.WActionForm;
import org.squale.welcom.struts.transformer.WITransformer;
import org.squale.welcom.struts.transformer.WTransformerException;

/**
 * Transforme un r�sultat qualit�
 */
public class ResultTransformer
    implements WITransformer
{

    /**
     * @see org.squale.welcom.struts.transformer.WITransformer#objToForm(java.lang.Object[]) {@inheritDoc}
     */
    public WActionForm objToForm( Object[] pObject )
        throws WTransformerException
    {
        ResultForm form = new ResultForm();
        objToForm( pObject, form );
        return form;
    }

    /**
     * @see org.squale.welcom.struts.transformer.WITransformer#objToForm(java.lang.Object[],
     *      org.squale.welcom.struts.bean.WActionForm) {@inheritDoc}
     */
    public void objToForm( Object[] pObject, WActionForm pForm )
        throws WTransformerException
    {
        QualityResultDTO resultDTO = (QualityResultDTO) pObject[0];
        ResultForm resultForm = (ResultForm) pForm;
        resultForm.setName( resultDTO.getRule().getName() );
        resultForm.setId( "" + resultDTO.getRule().getId() );
        resultForm.setCurrentMark( "" + resultDTO.getMeanMark() );
    }

    /**
     * @see org.squale.welcom.struts.transformer.WITransformer#formToObj(org.squale.welcom.struts.bean.WActionForm)
     *      {@inheritDoc}
     */
    public Object[] formToObj( WActionForm form )
        throws WTransformerException
    {
        throw new WTransformerException( "Not Yet Implemented" );
    }

    /**
     * @see org.squale.welcom.struts.transformer.WITransformer#formToObj(org.squale.welcom.struts.bean.WActionForm,
     *      java.lang.Object[]) {@inheritDoc}
     */
    public void formToObj( WActionForm form, Object[] object )
        throws WTransformerException
    {
        throw new WTransformerException( "Not Yet Implemented" );
    }

}
