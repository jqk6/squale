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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMapping;

import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import org.squale.squaleweb.transformer.component.parameters.JCompilingConfTransformer;
import org.squale.squaleweb.util.SqualeWebActionUtils;

/**
 * 
 */
public class JCompilingForm
    extends AbstractParameterForm
{

    /** Nom du form */
    public static final String NAME = "JCompilingForm";

    /** Indique si le projet doit �tre compil� avec le plugin Eclipse */
    private boolean mEclipseCompilation = true;

    /**
     * Version de java
     */
    private String mDialect = "";

    /**
     * Chemin vers le bundle eclipse utilis� par le projet Ce chemin fait parti des bundles propos�s dans la liste
     * d�roulante
     */
    private String mBundlePathDefault = "";

    /**
     * Chemin vers le bundle eclipse utilis� par le projet
     */
    private String mBundlePath = "";

    /** Indique si la compilation a besoin du bundle eclipse */
    private boolean mNeedBundle;

    /**
     * Contient la liste des r�gles de compilation ordonn�es
     */
    private List mCompilationRules = new ArrayList( 0 );

    /**
     * Nom des r�pertoires � exclure de la compilation
     */
    private String[] mExcludeDirectories = new String[0];

    /**
     * Variables eclipse
     */
    private List mEclipseVars = new ArrayList( 0 );

    /**
     * Librairies eclipse
     */
    private List mEclipseLibs = new ArrayList( 0 );

    /**
     * Advanced options for eclipse launcher
     */
    private String mAdvancedOptions = "";

    /**
     * @return Nom des r�pertoires � exclure de la compilation
     */
    public String[] getExcludeDirectories()
    {
        return mExcludeDirectories;
    }

    /**
     * @param pExcludeDirectories Nom des r�pertoires � exclure de la compilation
     */
    public void setExcludeDirectories( String[] pExcludeDirectories )
    {
        mExcludeDirectories = pExcludeDirectories;
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
     * @return la liste ordonn�e des r�gles de compilation
     */
    public List getCompilationRules()
    {
        return mCompilationRules;
    }

    /**
     * @param pCompilationRules la liste ordonn�e des r�gles de compilation
     */
    public void setCompilationRules( List pCompilationRules )
    {
        mCompilationRules = pCompilationRules;
    }

    /**
     * @return true si la compilation peut �tre faite avec Ant
     */
    public boolean getAntRulesAvailable()
    {
        return mCompilationRules.size() == 0
            || ( (JavaCompilationForm) mCompilationRules.get( 0 ) ).getKindOfTask().compareTo( ParametersConstants.ANT ) == 0;
    }

    /**
     * @return true si la compilation peut �tre faite avec WSAD
     */
    public boolean getWsadRulesAvailable()
    {
        return mCompilationRules.size() == 0
            || ( (JavaCompilationForm) mCompilationRules.get( 0 ) ).getKindOfTask().compareTo( ParametersConstants.WSAD ) == 0;
    }

    /**
     * @return true si la compilation peut �tre faite avec RSA
     */
    public boolean getRsaRulesAvailable()
    {
        return mCompilationRules.size() == 0
            || ( (JavaCompilationForm) mCompilationRules.get( 0 ) ).getKindOfTask().compareTo( ParametersConstants.RSA ) == 0;
    }

    /**
     * @see org.squale.squaleweb.applicationlayer.formbean.component.parameters.AbstractParameterForm#getTransformer()
     *      {@inheritDoc}
     */
    public Class getTransformer()
    {
        return JCompilingConfTransformer.class;
    }

    /**
     * @see org.squale.squaleweb.applicationlayer.formbean.component.parameters.AbstractParameterForm#getNameInSession()
     *      {@inheritDoc}
     */
    public String getNameInSession()
    {
        return "jCompilingForm";
    }

    /**
     * @see org.squale.squaleweb.applicationlayer.formbean.component.parameters.AbstractParameterForm#getParametersConstants()
     *      {@inheritDoc}
     */
    public String[] getParametersConstants()
    {
        return new String[] { ParametersConstants.EXCLUDED_DIRS, ParametersConstants.DIALECT, ParametersConstants.ANT,
            ParametersConstants.WSAD, ParametersConstants.RSA, ParametersConstants.BUNDLE_PATH,
            ParametersConstants.ECLIPSE };
    }

    /**
     * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping,
     *      javax.servlet.http.HttpServletRequest) {@inheritDoc}
     */
    public void reset( ActionMapping mapping, HttpServletRequest request )
    {
        setDialect( "" );
        setExcludeDirectories( new String[0] );
        setBundlePath( "" );
        setNeedBundle( false );
        setEclipseCompilation( true );
    }

    /**
     * @see org.squale.squaleweb.applicationlayer.formbean.component.parameters.AbstractParameterForm#getTaskName()
     *      {@inheritDoc}
     */
    public String getTaskName()
    {
        return "JCompilingTask";
    }

    /**
     * @see org.squale.squaleweb.applicationlayer.formbean.component.parameters.AbstractParameterForm#validateConf(org.apache.struts.action.ActionMapping,
     *      javax.servlet.http.HttpServletRequest) {@inheritDoc}
     */
    protected void validateConf( ActionMapping pMapping, HttpServletRequest pRequest )
    {
        String kindOfTask = (String) pRequest.getParameter( "kindOfTask" );
        // On ne valide le formulaire que lorsque l'utilisateur valide la configuration enti�re
        // On teste sur le param�tre "kindOfTask" car il indique si on ajoute une r�gle de compilation
        // si il est vide, on valide le formulaire.
        if ( kindOfTask.length() == 0 )
        {
            // Le dialect
            if ( getDialect().length() == 0 )
            {
                addError( "dialect", new ActionError( "error.field.required" ) );
            }
            // Il doit y avoir au moins un chemin ant ou wsad de renseign�
            if ( getCompilationRules().size() == 0 )
            {
                addError( "compilationRules", new ActionError( "error.field.required" ) );
            }
        }
        // On nettoie les exclusions de compilation
        setExcludeDirectories( SqualeWebActionUtils.cleanValues( getExcludeDirectories() ) );
    }

    /**
     * @return le chemin vers le bundle eclispe
     */
    public String getBundlePath()
    {
        // Retourne le bundle entr� par l'utilisateur
        return ( getNeedBundle() ) ? mBundlePath : "";
    }

    /**
     * @return le chemin vers le bundle eclispe
     */
    public String getBundlePathPrior()
    {
        // Retourne la cha�ne si on a besoin d'un bundle particulier sinon le bundle s�lectionn� dans la liste
        return ( getBundlePath().length() > 0 ) ? mBundlePath : mBundlePathDefault;
    }

    /**
     * @param pBundlePath le chemin vers le bundle eclispe
     */
    public void setBundlePath( String pBundlePath )
    {
        mBundlePath = pBundlePath.trim();
    }

    /**
     * @return true si la compilation a besoin du bundle eclipse
     */
    public boolean getNeedBundle()
    {
        return mNeedBundle;
    }

    /**
     * @param pNeedBundle true si la compilation a besoin du bundle eclipse
     */
    public void setNeedBundle( boolean pNeedBundle )
    {
        mNeedBundle = pNeedBundle;
    }

    /**
     * @return le bundle par d�faut
     */
    public String getBundlePathDefault()
    {
        return mBundlePathDefault;
    }

    /**
     * @param pBundlePathDefault le bundle par d�faut
     */
    public void setBundlePathDefault( String pBundlePathDefault )
    {
        mBundlePathDefault = pBundlePathDefault;
    }

    /**
     * @return les librairies utilisateur eclipse
     */
    public List getEclipseLibs()
    {
        return mEclipseLibs;
    }

    /**
     * @return les variables eclipse
     */
    public List getEclipseVars()
    {
        return mEclipseVars;
    }

    /**
     * @param pLibs les variables eclipse
     */
    public void setEclipseLibs( List pLibs )
    {
        mEclipseLibs = pLibs;
    }

    /**
     * @param pVars les variables eclipse
     */
    public void setEclipseVars( List pVars )
    {
        mEclipseVars = pVars;
    }

    /**
     * Ajoute une variable
     * 
     * @param name le nom de la variable
     * @param lib la librairie associ�e � la variable
     */
    public void addEclipseVar( String name, String lib )
    {
        mEclipseVars.add( new EclipseVarForm( name, lib ) );

    }

    /**
     * @return le type de compilation
     */
    public boolean getEclipseCompilation()
    {
        return mEclipseCompilation;
    }

    /**
     * @param pEclipseCompilation le type de compilation
     */
    public void setEclipseCompilation( boolean pEclipseCompilation )
    {
        mEclipseCompilation = pEclipseCompilation;
    }

    /**
     * @return eclipse advanced options
     */
    public String getAdvancedOptions()
    {
        return mAdvancedOptions;
    }

    /**
     * @param pAdvancedOptions eclipse advanced options
     */
    public void setAdvancedOptions( String pAdvancedOptions )
    {
        mAdvancedOptions = pAdvancedOptions;
    }

    /**
     * This method return the kind of compilation already saved in base for the project
     * 
     * @return the kind of compilation already saved
     */
    public String getKindOfCompil()
    {
        String kind = ParametersConstants.NOT_DEFINED;
        if ( mCompilationRules.size() > 0 )
        {
            kind = ( (JavaCompilationForm) mCompilationRules.get( 0 ) ).getKindOfTask();
        }
        return kind;
    }

}
