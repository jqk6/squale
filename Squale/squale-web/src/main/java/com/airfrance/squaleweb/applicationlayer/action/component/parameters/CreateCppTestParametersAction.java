package com.airfrance.squaleweb.applicationlayer.action.component.parameters;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.airfrance.jraf.helper.AccessDelegateHelper;
import com.airfrance.jraf.spi.accessdelegate.IApplicationComponent;
import com.airfrance.squaleweb.applicationlayer.formbean.creation.CreateProjectForm;

/**
 * Configuration des param�tres de la t�che CppTest
 */
public class CreateCppTestParametersAction
    extends CreateParametersAction
{

    /**
     * @see com.airfrance.squaleweb.applicationlayer.action.component.parameters.CreateParametersAction#getTransformerParameters(com.airfrance.squaleweb.applicationlayer.formbean.creation.CreateProjectForm,
     *      javax.servlet.http.HttpServletRequest) {@inheritDoc}
     */
    public Object[] getTransformerParameters( CreateProjectForm pProject, HttpServletRequest pRequest )
        throws Exception
    {
        IApplicationComponent acCheckstyleAdmin = AccessDelegateHelper.getInstance( "CppTestAdmin" );
        // On r�cup�re la liste des configurations existantes
        Collection configsDTO = (Collection) acCheckstyleAdmin.execute( "getAllConfigurations" );
        // On ajoute les param�tres de CPPTEST
        Object[] params = { pProject.getParameters(), configsDTO };
        return params;
    }

}
