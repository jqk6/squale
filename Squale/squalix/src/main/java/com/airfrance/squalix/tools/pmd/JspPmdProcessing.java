package com.airfrance.squalix.tools.pmd;

import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ListParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.MapParameterBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;

import net.sourceforge.pmd.SourceType;

/**
 * Traitement PMD sur du code java
 */
public class JspPmdProcessing
    extends AbstractPmdProcessing
{
    /** Extensions autoris�es */
    private String[] mExtensions = { ".jsp" };

    /**
     * {@inheritDoc}
     * 
     * @see com.airfrance.squalix.tools.pmd.AbstractPmdProcessing#getLanguage()
     */
    protected String getLanguage()
    {
        return "jsp";
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.airfrance.squalix.tools.pmd.AbstractPmdProcessing#getSourceType()
     */
    protected SourceType getSourceType()
    {
        return SourceType.JSP;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.airfrance.squalix.tools.pmd.AbstractPmdProcessing#getExtensions()
     */
    protected String[] getExtensions()
    {
        return mExtensions;
    }

    /**
     * Obtention des sources
     * 
     * @param pProjectParams param�tres du projet
     * @return sources sous la forme de ListParameterBO(StringParameterBO)
     */
    protected ListParameterBO getSourcesDirs( MapParameterBO pProjectParams )
    {
        return (ListParameterBO) pProjectParams.getParameters().get( ParametersConstants.JSP );
    }

    /**
     * Obtention des r�pertoires exclus de la compilation
     * 
     * @param pProjectParams param�tres du projet
     * @return r�pertories exclus sous la forme de ListParameterBO(StringParameterBO)
     */
    protected ListParameterBO getExcludedDirs( MapParameterBO pProjectParams )
    {
        return (ListParameterBO) pProjectParams.getParameters().get( ParametersConstants.JSP_EXCLUDED_DIRS );
    }

}
