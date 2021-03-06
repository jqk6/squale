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

import java.util.ArrayList;
import java.util.List;

import org.squale.squalecommon.datatransfertobject.component.parameters.ListParameterDTO;
import org.squale.squalecommon.datatransfertobject.component.parameters.MapParameterDTO;
import org.squale.squalecommon.datatransfertobject.component.parameters.StringParameterDTO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import org.squale.squaleweb.applicationlayer.formbean.component.parameters.JavaMockCompilingForm;
import org.squale.welcom.struts.bean.WActionForm;
import org.squale.welcom.struts.transformer.WITransformer;
import org.squale.welcom.struts.transformer.WTransformerException;

/**
 * Transformeur de param�tres pour la compilation java dans le cas de projet d�j� compil�
 */
public class JavaMockCompilingTransformer
    implements WITransformer
{

    /**
     * @see org.squale.welcom.struts.transformer.WITransformer#objToForm(java.lang.Object[]) {@inheritDoc}
     */
    public WActionForm objToForm( Object[] pObject )
        throws WTransformerException
    {
        JavaMockCompilingForm ckjmForm = new JavaMockCompilingForm();
        objToForm( pObject, ckjmForm );
        return ckjmForm;
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
        JavaMockCompilingForm taskForm = (JavaMockCompilingForm) pForm;
        MapParameterDTO projectParams = (MapParameterDTO) pObject[0];
        MapParameterDTO taskParam = new MapParameterDTO();
        ListParameterDTO sources = new ListParameterDTO();
        // Insertion des param�tres dans la map
        // sources compil�es:
        fillCompiledSourcesList( sources, taskForm.getCompiledSources() );
        taskParam.getParameters().put( ParametersConstants.COMPILED_SOURCES_DIRS, sources );
        // classpath
        StringParameterDTO classpath = new StringParameterDTO();
        classpath.setValue( taskForm.getClasspath() );
        taskParam.getParameters().put( ParametersConstants.CLASSPATH, classpath );
        // dialect
        StringParameterDTO dialect = new StringParameterDTO();
        dialect.setValue( taskForm.getDialect() );
        projectParams.getParameters().put( ParametersConstants.DIALECT, dialect );
        projectParams.getParameters().put( ParametersConstants.COMPILED, taskParam );
    }

    /**
     * Remplissage d'une liste de sources
     * 
     * @param pSourcesList liste � remplir
     * @param pSourcesTab source � y ins�rer
     */
    protected void fillCompiledSourcesList( ListParameterDTO pSourcesList, String[] pSourcesTab )
    {
        ArrayList sourcesList = new ArrayList();
        for ( int i = 0; i < pSourcesTab.length; i++ )
        {
            StringParameterDTO strParamSource = new StringParameterDTO();
            strParamSource.setValue( pSourcesTab[i] );
            sourcesList.add( strParamSource );
        }
        pSourcesList.setParameters( sourcesList );
    }

    /**
     * @see org.squale.welcom.struts.transformer.WITransformer#objToForm(java.lang.Object[],
     *      org.squale.welcom.struts.bean.WActionForm) {@inheritDoc}
     */
    public void objToForm( Object[] pObject, WActionForm pForm )
        throws WTransformerException
    {
        // on positionne les param�tres du formulaire
        JavaMockCompilingForm taskForm = (JavaMockCompilingForm) pForm;
        MapParameterDTO projectParams = (MapParameterDTO) pObject[0];
        MapParameterDTO taskParam = (MapParameterDTO) projectParams.getParameters().get( ParametersConstants.COMPILED );
        if ( taskParam != null )
        {
            // Liste des sources compil�es
            ListParameterDTO sourcesDTO =
                (ListParameterDTO) taskParam.getParameters().get( ParametersConstants.COMPILED_SOURCES_DIRS );
            if ( sourcesDTO != null )
            {
                taskForm.setCompiledSources( convertList( sourcesDTO ) );
            }
            // Le classpath
            StringParameterDTO classpathDTO =
                (StringParameterDTO) taskParam.getParameters().get( ParametersConstants.CLASSPATH );
            if ( classpathDTO != null )
            {
                taskForm.setClasspath( classpathDTO.getValue() );
            }
            // le dialect
            StringParameterDTO dialectDTO =
                (StringParameterDTO) projectParams.getParameters().get( ParametersConstants.DIALECT );
            if ( dialectDTO != null )
            {
                taskForm.setDialect( dialectDTO.getValue() );
            }
        }
    }

    /**
     * Conversion d'une liste de sources compil�es
     * 
     * @param pSourcesList liste de sources compil�es
     * @return liste des sources compil�es
     */
    protected String[] convertList( ListParameterDTO pSourcesList )
    {
        List sourcesParam = pSourcesList.getParameters();
        int size = sourcesParam.size();
        String[] sources = new String[size];
        for ( int i = 0; i < size; i++ )
        {
            StringParameterDTO source = (StringParameterDTO) sourcesParam.get( i );
            sources[i] = source.getValue().trim();

        }
        return sources;
    }

}
