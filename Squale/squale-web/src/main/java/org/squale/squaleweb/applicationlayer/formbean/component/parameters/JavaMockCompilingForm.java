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
import org.squale.squaleweb.transformer.component.parameters.JavaMockCompilingTransformer;
import org.squale.squaleweb.util.SqualeWebActionUtils;

/**
 * Bean pour la t�che de compilation dans les cas o� le projet est d�j� compil�
 */
public class JavaMockCompilingForm
    extends AbstractParameterForm
{

    /** Chemin vers les sources compil�es */
    private String[] mCompiledSources;

    /** Chemin vers le classpath du projet */
    private String mClasspath;

    /**
     * Version de java
     */
    private String mDialect;

    /**
     * Constructeur par d�faut
     */
    public JavaMockCompilingForm()
    {
        mClasspath = "";
        mDialect = "";
        mCompiledSources = new String[0];
    }

    /**
     * @return le chemin vers le classpath du projet
     */
    public String getClasspath()
    {
        return mClasspath;
    }

    /**
     * Modifie le chemin vers le classpath du projet
     * 
     * @param pClasspath le chemin vers le classpath du projet
     */
    public void setClasspath( String pClasspath )
    {
        mClasspath = pClasspath;
    }

    /**
     * @return la version de java
     */
    public String getDialect()
    {
        return mDialect;
    }

    /**
     * @param pDialect la version de java
     */
    public void setDialect( String pDialect )
    {
        mDialect = pDialect;
    }

    /**
     * @return les chemins vers les sources compil�es
     */
    public String[] getCompiledSources()
    {
        return mCompiledSources;
    }

    /**
     * @param pSrcs les chemins vers les sources compil�es
     */
    public void setCompiledSources( String[] pSrcs )
    {
        mCompiledSources = pSrcs;
    }

    /**
     * @see org.squale.squaleweb.applicationlayer.formbean.component.parameters.AbstractParameterForm#getTransformer()
     *      {@inheritDoc}
     */
    public Class getTransformer()
    {
        return JavaMockCompilingTransformer.class;
    }

    /**
     * @see org.squale.squaleweb.applicationlayer.formbean.component.parameters.AbstractParameterForm#getNameInSession()
     *      {@inheritDoc}
     */
    public String getNameInSession()
    {
        return "javaMockCompilingForm";
    }

    /**
     * @see org.squale.squaleweb.applicationlayer.formbean.component.parameters.AbstractParameterForm#getParametersConstants()
     *      {@inheritDoc}
     */
    public String[] getParametersConstants()
    {
        return new String[] { ParametersConstants.COMPILED, ParametersConstants.DIALECT };
    }

    /**
     * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping,
     *      javax.servlet.http.HttpServletRequest) {@inheritDoc}
     */
    public void reset( ActionMapping mapping, HttpServletRequest request )
    {
        setClasspath( "" );
        setCompiledSources( new String[0] );
    }

    /**
     * @see org.squale.squaleweb.applicationlayer.formbean.component.parameters.AbstractParameterForm#getTaskName()
     *      {@inheritDoc}
     */
    public String getTaskName()
    {
        return "JavaMockCompilingTask";
    }

    /**
     * @see org.squale.squaleweb.applicationlayer.formbean.component.parameters.AbstractParameterForm#validateConf(org.apache.struts.action.ActionMapping,
     *      javax.servlet.http.HttpServletRequest) {@inheritDoc}
     */
    protected void validateConf( ActionMapping pMapping, HttpServletRequest pRequest )
    {
        // Les sources compil�es
        setCompiledSources( SqualeWebActionUtils.cleanValues( getCompiledSources() ) );
        if ( getCompiledSources().length == 0 )
        {
            addError( "compiledSources", new ActionError( "error.field.required" ) );
        }
        setClasspath( getClasspath().trim() );
        // Le classpath est un champ obligatoire tout comme le chemin vers les sources compil�es
        if ( getClasspath().length() == 0 )
        {
            addError( "classpath", new ActionError( "error.field.required" ) );
        }
        // Le dialect est aussi obligatoire
        if ( getDialect().length() == 0 )
        {
            addError( "dialect", new ActionError( "error.field.required" ) );
        }
    }

}
