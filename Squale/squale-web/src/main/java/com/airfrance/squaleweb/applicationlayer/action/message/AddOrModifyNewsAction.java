package com.airfrance.squaleweb.applicationlayer.action.message;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.airfrance.jraf.helper.AccessDelegateHelper;
import com.airfrance.jraf.spi.accessdelegate.IApplicationComponent;
import com.airfrance.squalecommon.datatransfertobject.message.NewsDTO;
import com.airfrance.squaleweb.applicationlayer.action.accessRights.AdminAction;
import com.airfrance.squaleweb.applicationlayer.formbean.messages.NewsForm;
import com.airfrance.squaleweb.transformer.message.NewsTranformer;
import com.airfrance.welcom.struts.transformer.WTransformerFactory;

/**
 * 
 */
public class AddOrModifyNewsAction
    extends AdminAction
{

    /**
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward add( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                              HttpServletResponse pResponse )
    {
        ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
        // par d�faut on est redirig�s sur la page courante
        ActionForward forward = null;
        NewsForm form = (NewsForm) pForm;
        try
        {
            IApplicationComponent ac = AccessDelegateHelper.getInstance( "Messages" );
            NewsDTO dto = (NewsDTO) WTransformerFactory.formToObj( NewsTranformer.class, form )[0];
            Object[] paramIn = new Object[] { dto };
            // si la news existe d�ja, on revient sur la page courante
            // en signalant qu'on a pas pu ajout�
            if ( ( (Boolean) ac.execute( "newsAlreadyExists", paramIn ) ).booleanValue() )
            {
                forward = pMapping.findForward( "uncorrect" );
                ActionMessage error = new ActionMessage( "error.key.alreadyExists" );
                messages.add( ActionMessages.GLOBAL_MESSAGE, error );
                saveMessages( pRequest, messages );
            }
            else
            { // on ajoute effectivement en base
                ac.execute( "addNews", paramIn );
                forward = pMapping.findForward( "success" );
            }
        }
        catch ( Exception e )
        {
            handleException( e, errors, pRequest );
        }
        if ( !errors.isEmpty() )
        {
            // Enregistrement du message et routage vers la page d'erreur
            saveErrors( pRequest, errors );
            forward = pMapping.findForward( "total_failure" );
        }
        return forward;
    }

    /**
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward modify( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                 HttpServletResponse pResponse )
    {
        ActionErrors errors = new ActionErrors();
        // par d�faut on est redirig�s sur la page courante
        ActionForward forward = null;
        NewsForm form = (NewsForm) pForm;
        try
        {
            IApplicationComponent ac = AccessDelegateHelper.getInstance( "Messages" );
            NewsDTO dto = (NewsDTO) WTransformerFactory.formToObj( NewsTranformer.class, form )[0];
            Object[] paramIn = new Object[] { dto };
            ac.execute( "modifyNews", paramIn );
            forward = pMapping.findForward( "success" );
        }
        catch ( Exception e )
        {
            handleException( e, errors, pRequest );
        }
        if ( !errors.isEmpty() )
        {
            // Enregistrement du message et routage vers la page d'erreur
            saveErrors( pRequest, errors );
            forward = pMapping.findForward( "total_failure" );
        }
        return forward;
    }

}
