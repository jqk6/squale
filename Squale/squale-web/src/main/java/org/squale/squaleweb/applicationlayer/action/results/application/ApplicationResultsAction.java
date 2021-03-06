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
package org.squale.squaleweb.applicationlayer.action.results.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.servlet.ServletUtilities;

import org.squale.jraf.commons.exception.JrafEnterpriseException;
import org.squale.jraf.helper.AccessDelegateHelper;
import org.squale.jraf.spi.accessdelegate.IApplicationComponent;
import org.squale.squalecommon.datatransfertobject.component.AuditDTO;
import org.squale.squalecommon.datatransfertobject.component.ComponentDTO;
import org.squale.squalecommon.datatransfertobject.tag.TagDTO;
import org.squale.squaleweb.applicationlayer.action.ActionUtils;
import org.squale.squaleweb.applicationlayer.action.accessRights.BaseDispatchAction;
import org.squale.squaleweb.applicationlayer.action.accessRights.ReaderAction;
import org.squale.squaleweb.applicationlayer.formbean.LogonBean;
import org.squale.squaleweb.applicationlayer.formbean.component.ApplicationForm;
import org.squale.squaleweb.applicationlayer.formbean.component.ProjectForm;
import org.squale.squaleweb.applicationlayer.formbean.component.ProjectListForm;
import org.squale.squaleweb.applicationlayer.formbean.component.SplitAuditsListForm;
import org.squale.squaleweb.applicationlayer.formbean.results.ProjectSummaryForm;
import org.squale.squaleweb.applicationlayer.formbean.results.ResultListForm;
import org.squale.squaleweb.transformer.ApplicationTransformer;
import org.squale.squaleweb.transformer.FactorsResultListTransformer;
import org.squale.squaleweb.util.SqualeWebConstants;
import org.squale.squaleweb.util.graph.GraphMaker;
import org.squale.squaleweb.util.graph.KiviatMaker;
import org.squale.squaleweb.util.graph.PieChartMaker;
import org.squale.welcom.outils.pdf.PDFDataJasperReports;
import org.squale.welcom.outils.pdf.PDFEngine;
import org.squale.welcom.outils.pdf.PDFFactory;
import org.squale.welcom.struts.transformer.WTransformerException;
import org.squale.welcom.struts.transformer.WTransformerFactory;
import org.squale.welcom.struts.util.WConstants;
import org.squale.welcom.taglib.table.TableUtil;

/**
 * Affichage des r�sultats de niveau application Les r�sultats disponibles au niveau de l'application sont affich�s par
 * cette action, il s'agit des graphes de kiviat et de piechart ainsi que les facteurs
 */
public class ApplicationResultsAction
    extends ReaderAction
{

    /**
     * Le nom d�signant l'attribut en session pour indiquer si on veut tous les facteurs ou non pour le kiviat.
     */
    private static final String ALL_FACTORS = "allFactors";

    /**
     * Synth�se d'une application Si l'application comporte un seul projet, la synth�se du projet est alors affich�e.
     * Dans le cas contraire, trois onglets sont pr�sent�s : un qui donne la r�partition, l'autre donnant les facteurs
     * par projet et le dernier donnant le kiviat.
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward summary( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                  HttpServletResponse pResponse )
    {

        // Cette action est appel�e dans plusieurs contextes possibles
        // Depuis une action struts avec l'id de l'application ou depuis
        // une page de l'application sans id de l'application (on a alors affaire � l'application courante)
        ActionForward forward;
        try
        {
            // Add an user access for this application
            addUserAccess( pRequest, ActionUtils.getCurrentApplication( pRequest ).getId() );
            forward = checkApplication( pMapping, pRequest );
            // Si forward est renseign�, l'application contient un seul projet
            // On redirige donc vers le projet ad�quat
            if ( null == forward )
            {
                // R�cup�ration des informations concernant les facteurs
                // R�cup�ration du param�tre : tous les facteurs ou seuls les facteurs ayant une note ?
                ResultListForm resultListForm = (ResultListForm) pForm;
                boolean pAllFactors = resultListForm.isAllFactors();
                // On met cette valeur en session
                pRequest.getSession().setAttribute( ALL_FACTORS, new Boolean( pAllFactors ) );

                ApplicationForm application = ActionUtils.getCurrentApplication( pRequest );
                // On remet � jour les form en session permettant d'acc�der aux diff�rents types d'audits
                SplitAuditsListForm auditsForm =
                    (SplitAuditsListForm) pRequest.getSession().getAttribute( "splitAuditsListForm" );
                if ( auditsForm != null )
                {
                    auditsForm.copyValues( application );
                    auditsForm.resetAudits( ActionUtils.getCurrentAuditsAsDTO( pRequest ) );
                    pRequest.getSession().setAttribute( "splitAuditsListForm", auditsForm );
                }
                List auditsDTO = ActionUtils.getCurrentAuditsAsDTO( pRequest );
                if ( auditsDTO != null )
                {
                    ( (ResultListForm) pForm ).resetAudits( auditsDTO );
                }
                // On efface ce form contenant les erreurs de l'application, car si on passe par ici
                // Ce form ne peut que contenir les informations pour une autre application
                pRequest.getSession().removeAttribute( "applicationErrorForm" );

                // Pr�paration de l'appel � la couche m�tier
                IApplicationComponent ac = AccessDelegateHelper.getInstance( "Results" );
                List applications =
                    Arrays.asList( WTransformerFactory.formToObj( ApplicationTransformer.class, application ) );
                Object[] paramIn = { applications, auditsDTO };
                // Les r�sultats sont retourn�s dans l'ordre impos� par la grille
                // on maintient cet ordre pour l'affichage
                List results = (List) ac.execute( "getApplicationResults", paramIn );
                pRequest.getSession().removeAttribute( SqualeWebConstants.RESULTS_KEY );
                if ( null != results && results.size() > 0 )
                {
                    // Transformation des r�sultats avant leur affichage par la couche
                    // welcom
                    WTransformerFactory.objToForm( FactorsResultListTransformer.class, (ResultListForm) pForm,
                                                   new Object[] { applications, results } );
                }
                else
                {
                    // si aucun resultat => liste vide
                    WTransformerFactory.objToForm( FactorsResultListTransformer.class, (ResultListForm) pForm,
                                                   new Object[] { applications, new ArrayList() } );
                }
                // R�cup�ration des donn�es permettant la g�n�ration du Kiviat et du PieChart de l'application
                if ( null != auditsDTO && null != auditsDTO.get( 0 ) )
                {
                    // Pr�paration de l'appel � la couche m�tier
                    ac = AccessDelegateHelper.getInstance( "Graph" );
                    Long pCurrentAuditId = new Long( ( (AuditDTO) auditsDTO.get( 0 ) ).getID() );
                    Object[] paramAuditIdKiviat = { pCurrentAuditId, String.valueOf( pAllFactors ) };

                    // Recherche des donn�es Kiviat
                    KiviatMaker maker = new KiviatMaker();
                    Map projectsValues = (Map) ac.execute( "getApplicationKiviatGraph", paramAuditIdKiviat );
                    Set keysSet = projectsValues.keySet();
                    Iterator it = keysSet.iterator();
                    while ( it.hasNext() )
                    {
                        String key = (String) it.next();
                        maker.addValues( key, (SortedMap) projectsValues.get( key ), pRequest );
                    }
                    JFreeChart chartKiviat = maker.getChart();
                    ChartRenderingInfo infoKiviat = new ChartRenderingInfo( new StandardEntityCollection() );
                    // Initialisation du kiviat
                    String fileNameKiviat =
                        ServletUtilities.saveChartAsPNG( chartKiviat, KiviatMaker.DEFAULT_WIDTH,
                                                         KiviatMaker.DEFAULT_HEIGHT, infoKiviat, pRequest.getSession() );
                    GraphMaker applicationKiviatChart = new GraphMaker( pRequest, fileNameKiviat, infoKiviat );

                    // Pour l'export en PDF
                    pRequest.getSession().removeAttribute( "kiviatChart" );
                    pRequest.getSession().setAttribute(
                                                        "kiviatChart",
                                                        chartKiviat.createBufferedImage( KiviatMaker.DEFAULT_WIDTH,
                                                                                         KiviatMaker.DEFAULT_HEIGHT ) );

                    // Recherche des donn�es PieChart
                    Object[] paramAuditIdPieChart = { pCurrentAuditId };
                    JFreeChart pieChart;
                    Object[] maps = (Object[]) ac.execute( "getApplicationPieChartGraph", paramAuditIdPieChart );
                    String pPreviousAuditId = null;
                    // on peut ne pas avoir d'audit pr�c�dent
                    if ( auditsDTO.size() > 1 )
                    {
                        pPreviousAuditId = "" + ( (AuditDTO) auditsDTO.get( 1 ) ).getID();
                    }
                    PieChartMaker pieMaker = new PieChartMaker( null, pCurrentAuditId.toString(), pPreviousAuditId );
                    pieMaker.setValues( (Map) maps[0] );
                    pieChart = pieMaker.getChart( (Map) maps[1], pRequest );

                    ChartRenderingInfo infoPieChart = new ChartRenderingInfo( new StandardEntityCollection() );
                    // Initialisation du pieChart
                    String fileName =
                        ServletUtilities.saveChartAsPNG( pieChart, PieChartMaker.DEFAULT_WIDTH,
                                                         PieChartMaker.DEFAULT_HEIGHT, infoPieChart,
                                                         pRequest.getSession() );
                    GraphMaker applicationPieChart = new GraphMaker( pRequest, fileName, infoPieChart );

                    // Met � jour les 2 champs du form avec les 2 graphs calcul�s
                    ( (ResultListForm) pForm ).setKiviat( applicationKiviatChart );
                    ( (ResultListForm) pForm ).setPieChart( applicationPieChart );

                    // Met � jour le form en session
                    pRequest.getSession().setAttribute( "resultListForm", pForm );

                    // Pour l'export en PDF
                    pRequest.getSession().removeAttribute( "pieChart" );
                    pRequest.getSession().setAttribute(
                                                        "pieChart",
                                                        pieChart.createBufferedImage( PieChartMaker.DEFAULT_WIDTH,
                                                                                      PieChartMaker.DEFAULT_HEIGHT ) );
                }
                // Affichage des informations sur la page jsp
                forward = pMapping.findForward( "summary" );
            }
        }
        catch ( Exception e )
        {
            ActionErrors errors = new ActionErrors();
            // Traitement factoris� des erreurs
            handleException( e, errors, pRequest );
            // Sauvegarde des erreurs
            saveMessages( pRequest, errors );
            // Routage vers la page d'erreur
            forward = pMapping.findForward( "total_failure" );
        }
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );
        // Indique que l'on vient d'une vue synth�se et pas d'une vue composant
        changeWay( pRequest, "false" );
        return forward;
    }

    /**
     * Selectionne � nouveau l'application courante � visualiser.
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward select( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                 HttpServletResponse pResponse )
    {

        ActionForward forward = null;
        ActionErrors errors = new ActionErrors();

        try
        {
            // Add an user access for this application
            addUserAccess( pRequest, ActionUtils.getCurrentApplication( pRequest ).getId() );
            // On supprime l'attribut en session
            pRequest.getSession().removeAttribute( ALL_FACTORS );
            if ( null == forward )
            {
                forward = pMapping.findForward( "summaryAction" );
            }
        }
        catch ( Exception e )
        {
            // Traitement factoris� des exceptions
            handleException( e, errors, pRequest );
        }
        if ( !errors.isEmpty() )
        {
            // Sauvegarde des messages
            saveMessages( pRequest, errors );
            // Routage vers la page d'erreur
            forward = pMapping.findForward( "total_failure" );
        }
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );
        return forward;
    }

    /**
     * V�rification de l'application Si l'application est invalide ou s'il ne comporte qu'un seul projet, un forward est
     * renvoy�, sinon les projets sont extraits pour la mise � jour du menu de gauche.
     * 
     * @param pMapping le mapping.
     * @param pRequest la requ�te HTTP.
     * @return l'action � r�aliser.
     * @throws Exception exception.
     */
    private ActionForward checkApplication( ActionMapping pMapping, HttpServletRequest pRequest )
        throws Exception
    {
        ActionForward forward = null;
        ApplicationForm application = ActionUtils.getCurrentApplication( pRequest );
        if ( null == application )
        {
            // l'id n'est pas valide
            ActionError error = new ActionError( "error.invalid_application" );
            ActionErrors errors = new ActionErrors();
            errors.add( "application", error );
            saveErrors( pRequest, errors );
            forward = pMapping.findForward( "failure" );
        }
        else
        {
            // On r�cup�re la liste des projets de l'application
            List projects =
                ( (ProjectListForm) pRequest.getSession().getAttribute( BaseDispatchAction.PROJECTS_KEY ) ).getList();
            if ( null != projects && projects.size() == 1 )
            {
                // On r�cup�re l'id du projet et on renvoie vers
                // l'affichage du projet
                Long projectId = new Long( ( (ProjectForm) projects.get( 0 ) ).getId() );
                pRequest.setAttribute( "projectId", projectId.toString() );
                // On ajoute aussi l'id du projet dans les param�tres pour le traceur
                // TODO FAB : regarder cette ligne d�comment�e (lien avec le traceur...)
                // pRequest.getParameterMap().put("projectId", new String[]{projectId.toString()});
                forward = pMapping.findForward( "project" );
            }
        }
        return forward;
    }

    /**
     * Action exportPDF pour StyleReport ou iText
     * 
     * @param mapping le actionMapping
     * @param form le form
     * @param request la request
     * @param response la response
     * @return l'actionForward
     * @throws ServletException exception pouvant etre levee
     */
    public ActionForward exportPDF( ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                    HttpServletResponse response )
        throws ServletException
    {
        Collection results = TableUtil.getSortedTable( request.getSession(), "resultListForm", "list" );
        try
        {
            HashMap parameters = new HashMap();
            PDFDataJasperReports data =
                new PDFDataJasperReports( request.getLocale(), getResources( request ), results,
                                          "/org/squale/squaleweb/resources/jasperreport/ApplicationResults.jasper",
                                          false, parameters );
            // Pour r�cup�rer les param�tres root
            parameters.put( "applicationName", ( (ResultListForm) form ).getApplicationName() );
            // La date ou le nom des audits
            parameters.put( "auditDate", ( (ResultListForm) form ).getAuditName() );
            parameters.put( "previousAuditDate", ( (ResultListForm) form ).getPreviousAuditName() );
            // L'image du pieChart
            parameters.put( "pieChart", (java.awt.Image) request.getSession().getAttribute( "pieChart" ) );
            // R�cup�ration des donn�es sur la volum�trie des projets de l'application
            // Pr�paration de l'appel � la couche m�tier
            IApplicationComponent ac = AccessDelegateHelper.getInstance( "Graph" );
            Long pCurrentAuditId = new Long( Long.parseLong( ( (ResultListForm) form ).getCurrentAuditId() ) );
            Object[] paramAuditId = { pCurrentAuditId };
            // Recherche des donn�es PieChart
            JFreeChart pieChart;
            Object[] maps = (Object[]) ac.execute( "getApplicationPieChartGraph", paramAuditId );
            // On r�cup�re la volum�trie par grille
            final int indexVol = 2;
            PieChartMaker pieMaker = new PieChartMaker( null, null, null );
            pieMaker.setValues( (Map) maps[indexVol] );
            pieChart = pieMaker.getChart( new HashMap(), request );
            // L'image du pieChart pour la volumetrie par grille
            parameters.put( "gridPieChart", pieChart.createBufferedImage( PieChartMaker.DEFAULT_WIDTH,
                                                                          PieChartMaker.DEFAULT_HEIGHT ) );
            // L'image du kiviat
            parameters.put( "kiviatChart", (java.awt.Image) request.getSession().getAttribute( "kiviatChart" ) ); // Le
            // nom
            // de
            // l'utilisateur
            LogonBean logon = (LogonBean) request.getSession().getAttribute( WConstants.USER_KEY );
            parameters.put( "userName", logon.getMatricule() );
            PDFFactory.generatePDFToHTTPResponse( data, response, "", PDFEngine.JASPERREPORTS );
        }
        catch ( Exception e )
        {
            throw new ServletException( e );
        }
        return null;
    }

    /**
     * Action addTag adds a tag to the current application
     * 
     * @param pMapping the actionMapping
     * @param pForm the form
     * @param pRequest the request
     * @param pResponse the response
     * @return the actionForward
     * @throws ServletException exception that can be thrown
     */
    public ActionForward addTag( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                 HttpServletResponse pResponse )
        throws ServletException
    {
        ActionForward forward = null;
        ActionErrors errors = new ActionErrors();
        IApplicationComponent ac;

        try
        {
            String tagToAdd = ( (ResultListForm) pForm ).getTagSupp();
            TagDTO tagDTO = null;
            if ( tagToAdd != null )
            {
                ac = AccessDelegateHelper.getInstance( "TagAdmin" );
                Object[] paramIn = { new String[] { tagToAdd } };
                Collection<TagDTO> tags = ( (Collection<TagDTO>) ac.execute( "getTagsByName", paramIn ) );
                if ( tags != null && tags.size() == 1 )
                {
                    for ( TagDTO tagDTOtemp : tags )
                    {
                        tagDTO = tagDTOtemp;
                    }
                }

                if ( tagDTO == null )
                {
                    pRequest.setAttribute( "unexistingTag", tagToAdd );
                }
                else
                {
                    // the given tag exists in the DB: let's add it to the project
                    ComponentDTO application =
                        (ComponentDTO) pRequest.getSession().getAttribute( BaseDispatchAction.APPLI_DTO );

                    // On rajoute le tag sur le formulaire
                    Collection<TagDTO> applicationTags = application.getTags();
                    boolean possedeTag = application.possessTag( tagDTO );
                    if ( !possedeTag )
                    {
                        applicationTags.add( tagDTO );
                        application.setTags( applicationTags );
                        ( (ResultListForm) pForm ).setTags( applicationTags );
                    }

                    // On a pu le rajouter sur le formulaire, on va le rajouter en base
                    if ( !possedeTag )
                    {
                        ac = AccessDelegateHelper.getInstance( "ApplicationAdmin" );
                        paramIn = new Object[] { application.getID(), tagDTO };
                        ac.execute( "addTag", paramIn );
                    }
                    
                }
                forward = pMapping.findForward( "summaryAction" );
            }
        }
        catch ( Exception e )
        {
            // Traitement factoris� des exceptions
            handleException( e, errors, pRequest );
        }
        if ( !errors.isEmpty() )
        {
            // Sauvegarde des messages
            saveMessages( pRequest, errors );
            // Routage vers la page d'erreur
            forward = pMapping.findForward( "total_failure" );
        }
        return forward;
    }

    /**
     * Action delTag removes a tag from the current application
     * 
     * @param pMapping the actionMapping
     * @param pForm the form
     * @param pRequest the request
     * @param pResponse the response
     * @return the actionForward
     * @throws ServletException exception that can be thrown
     */
    public ActionForward removeTag( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                    HttpServletResponse pResponse )
        throws ServletException
    {
        ActionForward forward = null;
        ActionErrors errors = new ActionErrors();
        IApplicationComponent ac;

        try
        {
            String tagTodelete = ( (ResultListForm) pForm ).getTagDel();
            long idApplication = Long.parseLong( ( (ResultListForm) pForm ).getApplicationId() );
            TagDTO tagDTOToDelete = null;
            if ( tagTodelete != null )
            {
                ac = AccessDelegateHelper.getInstance( "TagAdmin" );
                Object[] paramIn = { new String[] { tagTodelete } };
                Collection<TagDTO> tags = ( (Collection<TagDTO>) ac.execute( "getTagsByName", paramIn ) );
                if ( tags != null && tags.size() == 1 )
                {
                    for ( TagDTO tagDTOtemp : tags )
                    {
                        tagDTOToDelete = tagDTOtemp;
                    }
                }
                ComponentDTO application = getApplication( pRequest, idApplication );

                // On retire le tag du formulaire
                Collection<TagDTO> applicationTags = application.getTags();
                boolean possedeTag = application.possessTag( tagDTOToDelete );
                if ( possedeTag )
                {
                    Collection<TagDTO> newApplicationTags = new ArrayList<TagDTO>();
                    for ( TagDTO tagApplication : applicationTags )
                    {
                        if ( !tagApplication.getName().equals( tagDTOToDelete.getName() ) )
                        {
                            newApplicationTags.add( tagApplication );
                        }
                    }
                    application.setTags( newApplicationTags );
                    ( (ResultListForm) pForm ).setTags( newApplicationTags );
                    ( (ResultListForm) pForm ).setTagDel( "" );

                    // On a pu le retirer du formulaire, on va le retirer de la base

                    ac = AccessDelegateHelper.getInstance( "ApplicationAdmin" );
                    paramIn = new Object[] { application.getID(), tagDTOToDelete };
                    ac.execute( "removeTag", paramIn );
                }

            }
            forward = pMapping.findForward( "summaryAction" );
        }
        catch ( Exception e )
        {
            // Traitement factoris� des exceptions
            handleException( e, errors, pRequest );
        }
        if ( !errors.isEmpty() )
        {
            // Sauvegarde des messages
            saveMessages( pRequest, errors );
            // Routage vers la page d'erreur
            forward = pMapping.findForward( "total_failure" );
        }
        return forward;
    }

    /**
     * M�thode that will retrieve an application from the context or from the data base
     * 
     * @param pRequest the HTTP Request
     * @param pIdApplication idApplication in case it has to be retrieved from the database
     * @return the retrieved application
     * @throws JrafEnterpriseException if an exception occurs
     */
    private ComponentDTO getApplication( HttpServletRequest pRequest, long pIdApplication )
        throws JrafEnterpriseException
    {
        // The application is retrieved from the context
        ComponentDTO application = (ComponentDTO) pRequest.getSession().getAttribute( BaseDispatchAction.APPLI_DTO );

        // if it is not there, it will be retrieved from the database
        if ( application == null )
        {
            ComponentDTO componentTemp = new ComponentDTO();
            componentTemp.setID( pIdApplication );
            IApplicationComponent ac2 = AccessDelegateHelper.getInstance( "Component" );
            application = (ComponentDTO) ac2.execute( "get", new Object[] { componentTemp } );
        }
        return application;
    }
}
