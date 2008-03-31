package com.airfrance.squaleweb.applicationlayer.action.component.parameters;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.airfrance.jraf.helper.AccessDelegateHelper;
import com.airfrance.jraf.spi.accessdelegate.IApplicationComponent;
import com.airfrance.squaleweb.applicationlayer.action.component.CreateProjectAction;
import com.airfrance.squaleweb.applicationlayer.formbean.component.parameters.AbstractParameterForm;
import com.airfrance.squaleweb.applicationlayer.formbean.creation.CreateProjectForm;
import com.airfrance.welcom.struts.bean.WActionForm;
import com.airfrance.welcom.struts.transformer.WTransformerFactory;

/**
 * Action de param�trage des param�tres de la t�che Java/Jsp PMD Une m�me action permet le param�trage de la t�che PMD
 * dans un contexte java seul ou avec le contexte jsp
 */
public class CreatePmdParametersAction
    extends CreateParametersAction
{

    /**
     * @see com.airfrance.squaleweb.applicationlayer.action.component.parameters.CreateParametersAction#getTransformerParameters(com.airfrance.squaleweb.applicationlayer.formbean.creation.CreateProjectForm,
     *      javax.servlet.http.HttpServletRequest) {@inheritDoc}
     */
    public Object[] getTransformerParameters( CreateProjectForm pProject, HttpServletRequest pRequest )
        throws Exception
    {
        // Obtention des configurations disponibles
        IApplicationComponent acPmdAdmin = AccessDelegateHelper.getInstance( "PmdAdmin" );
        // on r�cup�re la liste des configurations disponibles
        Collection configurationsDTO = (Collection) acPmdAdmin.execute( "getAllConfigurations" );
        Object[] params = { pProject.getParameters(), pProject.getConfigurableTask( "PmdTask" ), configurationsDTO };

        return params;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.airfrance.squaleweb.applicationlayer.action.component.parameters.CreateParametersAction#fill(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward fill( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                               HttpServletResponse pResponse )
    {
        super.fill( pMapping, pForm, pRequest, pResponse );
        // Only if it's a new project
        if ( ( (AbstractParameterForm) pForm ).isNewConf() )
        {
            // Save project in order to have a default value for checkstyle
            ActionMessages errors = new ActionMessages();
            CreateProjectForm project = (CreateProjectForm) pRequest.getSession().getAttribute( "createProjectForm" );
            try
            {
                WTransformerFactory.formToObj( ( (AbstractParameterForm) pForm ).getTransformer(), (WActionForm) pForm,
                                               getTransformerParameters( project, pRequest ) );
            }
            catch ( Exception e )
            {
                // Save exceptions
                handleException( e, errors, pRequest );
            }
            if ( !errors.isEmpty() )
            {
                saveMessages( pRequest, errors );
            }
            // Call business layer
            new CreateProjectAction().saveProject( pMapping, project, pRequest, pResponse );
        }
        return null;
    }

}
