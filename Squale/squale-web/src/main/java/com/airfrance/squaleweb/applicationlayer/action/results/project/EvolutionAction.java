package com.airfrance.squaleweb.applicationlayer.action.results.project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.airfrance.jraf.helper.AccessDelegateHelper;
import com.airfrance.jraf.spi.accessdelegate.IApplicationComponent;
import com.airfrance.squalecommon.datatransfertobject.component.AuditDTO;
import com.airfrance.squalecommon.datatransfertobject.component.ComponentDTO;
import com.airfrance.squaleweb.applicationlayer.action.ActionUtils;
import com.airfrance.squaleweb.applicationlayer.action.accessRights.BaseDispatchAction;
import com.airfrance.squaleweb.applicationlayer.action.accessRights.ReaderAction;
import com.airfrance.squaleweb.applicationlayer.formbean.LogonBean;
import com.airfrance.squaleweb.applicationlayer.formbean.results.EvolutionForm;
import com.airfrance.squaleweb.transformer.EvolutionTransformer;
import com.airfrance.welcom.outils.pdf.PDFDataJasperReports;
import com.airfrance.welcom.outils.pdf.PDFEngine;
import com.airfrance.welcom.outils.pdf.PDFFactory;
import com.airfrance.welcom.struts.bean.WActionForm;
import com.airfrance.welcom.struts.transformer.WTransformerFactory;
import com.airfrance.welcom.struts.util.WConstants;

/**
 * Action pour la ncomparaison d�taill�e de deux audits
 */
public class EvolutionAction
    extends ReaderAction
{

    /** le nombre max de r�sultats � r�cup�rer */
    public static final int MAX_RESULTS = 300;

    /**
     * Nom de l'attibut en session pour indiquer � l'utilisateur que les r�sultats sont limit�s
     */
    public static final String DISPLAY_RESULTS_MSG = "displayLimitMsg";

    /**
     * Filtre les pratiques et les composants
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward list( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                               HttpServletResponse pResponse )
    {

        ActionForward forward = null;
        ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
        try
        {
            forward = pMapping.findForward( "success" );
            // On r�cup�re les deux audits
            List auditDTOList = ActionUtils.getCurrentAuditsAsDTO( pRequest );
            // On r�cup�re le projet
            ComponentDTO project = (ComponentDTO) pRequest.getSession().getAttribute( BaseDispatchAction.PROJECT_DTO );
            // On v�rifie qu'on en a bien deux et qu'ils ne sont pas nuls et que le projet est d�fini
            // sinon il s'agit d'une erreur
            if ( auditDTOList != null && auditDTOList.size() == 2 && auditDTOList.get( 0 ) != null
                && auditDTOList.get( 1 ) != null && project != null )
            {
                // On transforme le formulaire en un tableau de string
                Object[] filter = new Object[EvolutionForm.NB_FILTER];
                WTransformerFactory.formToObj( EvolutionTransformer.class, (WActionForm) pForm, filter );
                // On r�cup�re l'applicationComponent
                IApplicationComponent ac = AccessDelegateHelper.getInstance( "Results" );
                AuditDTO firstAudit = (AuditDTO) auditDTOList.get( 0 );
                AuditDTO secondAudit = (AuditDTO) auditDTOList.get( 1 );
                Object[] paramsTab = { firstAudit, secondAudit, project, filter, new Integer( MAX_RESULTS ) };
                // On appelle la m�thode avec le filtre associ�
                Collection results = (Collection) ac.execute( "getChangedComponentResults", paramsTab );
                // Si le nombre de r�sultats correspond au maximum d'�l�ment affichables (cf. MAX_RESULTS)
                // on affiche un message dans la page
                pRequest.getSession().removeAttribute( DISPLAY_RESULTS_MSG ); // gestion de la session
                if ( results.size() >= MAX_RESULTS )
                {
                    pRequest.getSession().setAttribute( DISPLAY_RESULTS_MSG, Boolean.TRUE );
                }
                // On transforme les listes en formulaire qui d�pend des filtres utilis�s et du tri
                Object[] args = new Object[] { results, pRequest.getLocale() };
                WTransformerFactory.objToForm( EvolutionTransformer.class, (WActionForm) pForm, args );
            }
        }
        catch ( Exception e )
        {
            handleException( e, errors, pRequest );
        }
        return forward;
    }

    /**
     * Export PDF pour la comparaison des audits
     * 
     * @param pMapping le actionMapping
     * @param pForm le form
     * @param pRequest la request
     * @param pResponse la response
     * @return l'actionForward
     * @throws ServletException exception pouvant etre levee
     */
    public ActionForward exportPDF( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                    HttpServletResponse pResponse )
        throws ServletException
    {
        try
        {
            // Les donn�es seront une liste � 1 �l�ment contenant le formulaire courant
            Collection data = new ArrayList();
            data.add( pForm );

            // Les param�tres
            HashMap parameters = new HashMap();
            // Le nom de l'utilisateur
            LogonBean logon = (LogonBean) pRequest.getSession().getAttribute( WConstants.USER_KEY );
            parameters.put( "userName", logon.getMatricule() );

            PDFDataJasperReports pdfData =
                new PDFDataJasperReports( pRequest.getLocale(), getResources( pRequest ), data,
                                          "/com/airfrance/squaleweb/resources/jasperreport/Comparison.jasper", false,
                                          parameters );
            PDFFactory.generatePDFToHTTPResponse( pdfData, pResponse, "", PDFEngine.JASPERREPORTS );
        }
        catch ( Exception e )
        {
            throw new ServletException( e );
        }
        return null;
    }
}
