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
package org.squale.squaleweb.applicationlayer.formbean.component.parameters;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMapping;

import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import org.squale.squaleweb.transformer.component.parameters.MackerConfTransformer;

/**
 * Formulaire pour la configuration de la t�che Macker
 */
public class MackerForm
    extends AbstractParameterForm
{

    /** Chemin relatif ou absolu vers le fichier de configuration Macker */
    private String mConfigFile;

    /**
     * Constructeur par d�faut
     */
    public MackerForm()
    {
        mConfigFile = "";
    }

    /**
     * @return le chemin du fichier de configuration
     */
    public String getConfigFile()
    {
        return mConfigFile;
    }

    /**
     * Modifie le chemin du fichier de configuration
     * 
     * @param pConfigFile le nouveau chemin du fichier de configuration
     */
    public void setConfigFile( String pConfigFile )
    {
        mConfigFile = pConfigFile;
    }

    /**
     * @see org.squale.squaleweb.applicationlayer.formbean.component.parameters.AbstractParameterForm#getTransformer()
     *      {@inheritDoc}
     */
    public Class getTransformer()
    {
        return MackerConfTransformer.class;
    }

    /**
     * @see org.squale.squaleweb.applicationlayer.formbean.component.parameters.AbstractParameterForm#getNameInSession()
     *      {@inheritDoc}
     */
    public String getNameInSession()
    {
        return "mackerForm";
    }

    /**
     * @see org.squale.squaleweb.applicationlayer.formbean.component.parameters.AbstractParameterForm#getParametersConstants()
     *      {@inheritDoc}
     */
    public String[] getParametersConstants()
    {
        return new String[] { ParametersConstants.MACKER };
    }

    /**
     * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping,
     *      javax.servlet.http.HttpServletRequest) {@inheritDoc}
     */
    public void reset( ActionMapping mapping, HttpServletRequest request )
    {
        setConfigFile( "" );
    }

    /**
     * @see org.squale.squaleweb.applicationlayer.formbean.component.parameters.AbstractParameterForm#getTaskName()
     *      {@inheritDoc}
     */
    public String getTaskName()
    {
        return "MackerTask";
    }

    /**
     * @see org.squale.squaleweb.applicationlayer.formbean.component.parameters.AbstractParameterForm#validateConf(org.apache.struts.action.ActionMapping,
     *      javax.servlet.http.HttpServletRequest) {@inheritDoc}
     */
    protected void validateConf( ActionMapping pMapping, HttpServletRequest pRequest )
    {
        // V�rification du fichier de configuration
        setConfigFile( getConfigFile().trim() );
        if ( getConfigFile().length() == 0 )
        {
            addError( "configFile", new ActionError( "error.field.required" ) );
        }
    }
}
