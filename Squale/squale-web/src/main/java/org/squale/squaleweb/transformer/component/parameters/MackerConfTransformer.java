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
package org.squale.squaleweb.transformer.component.parameters;

import org.squale.squalecommon.datatransfertobject.component.parameters.MapParameterDTO;
import org.squale.squalecommon.datatransfertobject.component.parameters.StringParameterDTO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import org.squale.squaleweb.applicationlayer.formbean.component.parameters.MackerForm;
import org.squale.welcom.struts.bean.WActionForm;
import org.squale.welcom.struts.transformer.WITransformer;
import org.squale.welcom.struts.transformer.WTransformerException;

/**
 * Transformation des param�tres Macker
 */
public class MackerConfTransformer
    implements WITransformer
{

    /**
     * @see org.squale.welcom.struts.transformer.WITransformer#objToForm(java.lang.Object[]) {@inheritDoc}
     */
    public WActionForm objToForm( Object[] pObject )
        throws WTransformerException
    {
        MackerForm mackerForm = new MackerForm();
        objToForm( pObject, mackerForm );
        return mackerForm;
    }

    /**
     * @see org.squale.welcom.struts.transformer.WITransformer#objToForm(java.lang.Object[],
     *      org.squale.welcom.struts.bean.WActionForm) {@inheritDoc}
     */
    public void objToForm( Object[] pObject, WActionForm pForm )
        throws WTransformerException
    {
        MapParameterDTO params = (MapParameterDTO) pObject[0];
        MackerForm mackerForm = (MackerForm) pForm;
        // On remplit le form
        // Fichier de configuration
        MapParameterDTO mackerParams = (MapParameterDTO) params.getParameters().get( ParametersConstants.MACKER );
        if ( null != mackerParams )
        {
            StringParameterDTO configFileParam =
                (StringParameterDTO) mackerParams.getParameters().get( ParametersConstants.MACKER_CONFIGURATION );
            String configFilePath = configFileParam.getValue();
            mackerForm.setConfigFile( configFilePath );
        }
    }

    /**
     * @see org.squale.welcom.struts.transformer.WITransformer#formToObj(org.squale.welcom.struts.bean.WActionForm)
     *      {@inheritDoc}
     */
    public Object[] formToObj( WActionForm pForm )
        throws WTransformerException
    {
        Object[] obj = { new MapParameterDTO() };
        formToObj( pForm, obj );
        return obj;
    }

    /**
     * @see org.squale.welcom.struts.transformer.WITransformer#formToObj(org.squale.welcom.struts.bean.WActionForm,
     *      java.lang.Object[]) {@inheritDoc}
     */
    public void formToObj( WActionForm pForm, Object[] pObject )
        throws WTransformerException
    {
        MapParameterDTO params = (MapParameterDTO) pObject[0];
        MackerForm mackerForm = (MackerForm) pForm;
        // Insertion des param�tres dans la map
        // Configuration macker
        MapParameterDTO mackerParams = new MapParameterDTO();
        StringParameterDTO configFileParam = new StringParameterDTO();
        configFileParam.setValue( mackerForm.getConfigFile() );
        mackerParams.getParameters().put( ParametersConstants.MACKER_CONFIGURATION, configFileParam );
        params.getParameters().put( ParametersConstants.MACKER, mackerParams );
    }

}
