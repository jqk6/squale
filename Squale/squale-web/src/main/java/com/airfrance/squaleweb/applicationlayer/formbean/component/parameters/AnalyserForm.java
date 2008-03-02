package com.airfrance.squaleweb.applicationlayer.formbean.component.parameters;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMapping;

import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import com.airfrance.squaleweb.transformer.component.parameters.AnalyserTransformer;

/**
 * Bean pour la configuration de la r�cup�ration des sources via l'arborescence de fichiers
 */
public class AnalyserForm
    extends AbstractParameterForm
{

    /**
     * La chemin racine du projet Ce chemin peut-�tre le chemin absolu d'un fichier compress� ou d'un r�pertoire.
     */
    private String mPath;

    /**
     * Constructeur par d�faut
     */
    public AnalyserForm()
    {
        mPath = "";
    }

    /**
     * @return le chemin racine
     */
    public String getPath()
    {
        return mPath;
    }

    /**
     * @param pPath le chemin racine
     */
    public void setPath( String pPath )
    {
        mPath = pPath;
    }

    /**
     * @return le transformer � utiliser
     * @see com.airfrance.squaleweb.applicationlayer.formbean.component.parameters.AbstractParameterForm#getTransformer()
     */
    public Class getTransformer()
    {
        return AnalyserTransformer.class;
    }

    /**
     * @return le nom en session
     * @see com.airfrance.squaleweb.applicationlayer.formbean.component.parameters.AbstractParameterForm#getNameInSession()
     */
    public String getNameInSession()
    {
        return "analyserForm";
    }

    /**
     * @see com.airfrance.squaleweb.applicationlayer.formbean.component.parameters.AbstractParameterForm#getParametersConstants()
     *      {@inheritDoc}
     */
    public String[] getParametersConstants()
    {
        return new String[] { ParametersConstants.ANALYSER };
    }

    /**
     * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping,
     *      javax.servlet.http.HttpServletRequest) {@inheritDoc}
     */
    public void reset( ActionMapping mapping, HttpServletRequest request )
    {
        setPath( "" );
    }

    /**
     * @see com.airfrance.squaleweb.applicationlayer.formbean.component.parameters.AbstractParameterForm#validateConf(org.apache.struts.action.ActionMapping,
     *      javax.servlet.http.HttpServletRequest) {@inheritDoc}
     */
    protected void validateConf( ActionMapping pMapping, HttpServletRequest pRequest )
    {
        setPath( getPath().trim() );
        if ( getPath().length() == 0 )
        {
            addError( "path", new ActionError( "error.field.required" ) );
        }
    }

    /**
     * @see com.airfrance.squaleweb.applicationlayer.formbean.component.parameters.AbstractParameterForm#getTaskName()
     *      {@inheritDoc}
     */
    public String getTaskName()
    {
        return "SourceCodeAnalyserTask";
    }

}
