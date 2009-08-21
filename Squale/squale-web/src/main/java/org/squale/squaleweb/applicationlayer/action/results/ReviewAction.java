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
package org.squale.squaleweb.applicationlayer.action.results;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.squale.squalecommon.datatransfertobject.component.ComponentDTO;
import org.squale.squalecommon.datatransfertobject.result.QualityResultDTO;
import org.squale.squalecommon.datatransfertobject.rule.PracticeRuleDTO;
import org.squale.squalecommon.datatransfertobject.rule.QualityRuleDTO;
import org.squale.squaleweb.applicationlayer.action.accessRights.ReaderAction;
import org.squale.squaleweb.applicationlayer.formbean.results.ComponentForm;
import org.squale.squaleweb.applicationlayer.formbean.results.ParamReviewCommentsForm;
import org.squale.squaleweb.applicationlayer.formbean.results.ParamReviewForm;
import org.squale.squaleweb.applicationlayer.tracker.TrackerStructure;
import org.squale.squaleweb.resources.WebMessages;
import org.squale.squaleweb.transformer.ComponentTransformer;
import org.squale.squaleweb.transformer.ParamReviewCommentsTransform;
import org.squale.squaleweb.transformer.ParamReviewTransformer;
import org.squale.squaleweb.util.SqualeWebConstants;
import org.squale.squaleweb.util.graph.GraphMaker;
import org.squale.squaleweb.util.graph.HistoMaker;
import org.squale.welcom.struts.bean.WActionForm;
import org.squale.welcom.struts.transformer.WTransformerException;
import org.squale.welcom.struts.transformer.WTransformerFactory;

/**
 * Affichage de l'historique Cette action permet l'affichage de l'historique de m�triques ou de r�gles qualit� (facteur,
 * crit�re ou pratique)
 */
public class ReviewAction
    extends ReaderAction
{

    /**
     * R�cup�re la liste des TREs correspondant au composant s�lectionn�
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward review( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                 HttpServletResponse pResponse )
    {
        ActionForward forward = null;
        ActionErrors errors = new ActionErrors();
        try
        {
            forward = pMapping.findForward( "review" );
            String tre;
            String ruleId;
            boolean isManualMark=false;
            // D�termination du type de param�tre
            boolean isMetric =
                ( pRequest.getParameter( "kind" ) == null ) || ( pRequest.getParameter( "kind" ).equals( "metric" ) );
            if ( isMetric )
            {
                tre = pRequest.getParameter( "which" );
                ruleId = "";
            }
            else
            {
                // R�cup�ration du v�ritable nom de la r�gle
                IApplicationComponent ac = AccessDelegateHelper.getInstance( "QualityGrid" );
                QualityRuleDTO dto = new QualityRuleDTO();
                ruleId = pRequest.getParameter( "which" );
                dto.setId( Integer.valueOf( ruleId ).longValue() );
                dto = (QualityRuleDTO) ac.execute( "getQualityRule", new Object[] { dto, new Boolean( false ) } );
                tre = dto.getName();
                if ( dto instanceof PracticeRuleDTO )
                {
                    //If the practice is a manual practice 
                    if(((PracticeRuleDTO)dto).getFormulaType()==null)
                    {
                        isManualMark=true;
                        forward = pMapping.findForward( "reviewManualMark" );
                    }
                }
            }
            // si un composant est pr�cis�, on le prend sinon on prend le projet
            String componentId = pRequest.getParameter( "component" );
            if ( componentId == null )
            {
                componentId = pRequest.getParameter( "projectId" );
            }
            WTransformerFactory.objToForm( ParamReviewTransformer.class, (WActionForm) pForm, new Object[] { tre,
                ruleId, componentId,isManualMark } );
            IApplicationComponent ac = AccessDelegateHelper.getInstance( "Component" );
            ComponentDTO dto = new ComponentDTO();
            dto.setID( new Long( componentId ).longValue() );
            Object[] paramIn = { dto };
            dto = (ComponentDTO) ac.execute( "get", paramIn );
            Object obj[] = { dto };
            ComponentForm cForm = (ComponentForm) WTransformerFactory.objToForm( ComponentTransformer.class, obj );
            ( (ParamReviewForm) pForm ).setComponentName( cForm.getName() );
            ( (ParamReviewForm) pForm ).setComponentType( cForm.getType() );
            // Met � jour les champs graph et comments du form
            setParamsInForm( pForm, pRequest );

        }
        catch ( Exception e )
        {
            // Factorisation du traitement des erreurs
            handleException( e, errors, pRequest );
        }
        if ( !errors.isEmpty() )
        {
            // Sauvegarde des message et transfert vers la page d'erreur
            saveErrors( pRequest, errors );
            forward = pMapping.findForward( "failure" );
        }
        // Mise en place du traceur historique
        String displayName = WebMessages.getString( pRequest.getLocale(), "tracker.mark.history" );
        // Dans le cas ou on arrive depuis la vue composant
        // il ne faut pas faire un updateTracker car ce n'est pas le TrackerHist
        // qui est affich� mais le Tracker des composants.
        // Par cons�quent, il faut cr��r un nouveau form dont le seul champ � remplir est le nom,
        // car ce sera le dernier �l�ment et ne sera donc pas cliquable.
        if ( pRequest.getSession().getAttribute( SqualeWebConstants.TRACKER_BOOL ).equals( "true" ) )
        {
            // nouveau form dont on ne fait que remplir le nom
            ComponentForm form = new ComponentForm();
            form.setName( displayName );
            // remet � jour le Tracker des composants
            updateTrackerComponent( form.getId(), form.getName(), pRequest );
        }
        else
        {
            updateHistTracker( displayName, "", TrackerStructure.UNDEFINED, pRequest, false ); // Le lien est vide car
                                                                                                // c'est action
                                                                                                // terminale
        }
        return forward;
    }

    /**
     * R�cup�re la liste des TREs correspondant au composant s�lectionn�
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward changeDays( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                     HttpServletResponse pResponse )
    {
        ActionErrors errors = new ActionErrors();
        ParamReviewForm currentForm = (ParamReviewForm)pForm;
        ActionForward forward =null;
        // If we work on manual practice
        if (currentForm.isManualMark())
        {
            forward = pMapping.findForward( "reviewManualMark" );
        }
        else
        {
            forward = pMapping.findForward( "review" );
        }
        // Met � jour les champs graph et comments du form
        try
        {
            setParamsInForm( pForm, pRequest );
        }
        catch ( Exception e )
        {
            // Factorisation du traitement des erreurs
            handleException( e, errors, pRequest );
            forward = pMapping.findForward( "total_failure" );
        }
        // traceur non n�cessaire ici
        return forward;
    }

    /**
     * 
     * @param pForm le formulaire
     * @param pRequest la requ�te
     * @return les param�tres
     */
    private Object[] getParams( ActionForm pForm, HttpServletRequest pRequest )
    {
        ActionErrors errors = new ActionErrors();
        Object[] paramIn = null;
        ParamReviewForm currentForm = (ParamReviewForm)pForm;
        int index = 0;
        int nbDays = currentForm.getNbDays();
        String tre = currentForm.getTre();
        String ruleId =currentForm.getRuleId();
        String componentId = currentForm.getComponentId();
        
        ComponentDTO comp = new ComponentDTO();
        comp.setID( Long.decode( componentId ).longValue() );
        Date date = null;
        // Conversion en une date
        if ( nbDays> 0 )
        {
            GregorianCalendar gc = new GregorianCalendar();
            gc.add( Calendar.DATE, -nbDays );
            date = gc.getTime();
        }
        String treLabel = WebMessages.getString( pRequest, tre );
        if ( ruleId.length() > 0 )
        {
            paramIn = new Object[] { comp, tre, treLabel, date, Long.decode( ruleId ) };
        }
        else
        {
            paramIn = new Object[] { comp, tre, treLabel, date, null };
        }
        return paramIn;
    }

    /**
     * Construit un graph et le recopie dans le outputStream si le formulaire a �t� valid�
     * 
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pComp le ComponentDTO
     * @param pResult le r�sultat de l'appel � la couche m�tier "Graph"
     * @return l'action � r�aliser.
     * @throws JrafEnterpriseException en cas de probl�me de r�cup�ration des donn�s
     * @throws IOException en cas de probl�mes de cr�ation du graph
     */
    private GraphMaker getGraph( ActionForm pForm, HttpServletRequest pRequest, ComponentDTO pComp, Object[] pResult )
        throws JrafEnterpriseException, IOException
    {
        // Initialisation
        GraphMaker histoChart = null;
        // Obtention de la couche m�tier
        IApplicationComponent ac = AccessDelegateHelper.getInstance( "Component" );
        // R�cup�ration du componentDTO
        Object[] paramIn2 = new Object[] { pComp };
        ComponentDTO componentDTO = (ComponentDTO) ac.execute( "get", paramIn2 );
        // S'applique uniquement dans le cas d'une m�thode
        // on enl�ve tout ce qui concerne les param�tres de la m�thode
        // pour faciliter l'affichage
        String componentName = componentDTO.getName();
        StringTokenizer st = new StringTokenizer( componentName, "(" );
        if ( st.hasMoreTokens() )
        {
            componentDTO.setName( st.nextToken() );
        }

        // On n'affiche le graphe que si on a des r�sultats
        if(((ParamReviewForm)pForm).isManualMark())
        {
            histoChart = manualMarkGraph( pRequest, pResult );
        }
        else
        {
            histoChart = componentGraph( pRequest, pResult );
        }
        
        // Met � jour l'attribut graph dans le form
        ( (ParamReviewForm) pForm ).setReviewGraph( histoChart );
        pRequest.getSession().removeAttribute( SqualeWebConstants.VALIDATED );
        return histoChart;
    }

    /**
     * Create the GraphMaker
     * 
     * @param pRequest The http request
     * @param result The list of results to display
     * @return an object GraphMaker
     * @throws IOException exception happen
     */
    private GraphMaker componentGraph(HttpServletRequest pRequest,Object[] result) throws IOException
    {
        GraphMaker histoChart = null;
        if ( ( (Map) result[1] ).size() > 0 )
        {
            HistoMaker histoMaker = new HistoMaker();
            //Add the curve for the list of result
            histoMaker.addCurve( (String) result[0], (Map) result[1] );
            JFreeChart chartKiviat = histoMaker.getChart();
            ChartRenderingInfo infoHisto = new ChartRenderingInfo( new StandardEntityCollection() );
            // Sauvegarde du graphe historique au format png dans un espace temporaire
            String fileNameHisto =
                ServletUtilities.saveChartAsPNG( chartKiviat, HistoMaker.DEFAULT_WIDTH, HistoMaker.DEFAULT_HEIGHT,
                                                 infoHisto, pRequest.getSession() );
            histoChart = new GraphMaker( pRequest, fileNameHisto, infoHisto );
        }
        return histoChart;
    }
    
    /**
     * Create the GraphMaker for a manual practice
     * 
     * @param pRequest The http request
     * @param result The list of results to display
     * @return an object GraphMaker
     * @throws IOException exception happen
     */
    private GraphMaker manualMarkGraph(HttpServletRequest pRequest,Object[] result) throws IOException
    {
        GraphMaker histoChart = null;
        if ( ( (Map) result[1] ).size() > 0 || ((Map) result[3]).size()>0)
        {
            HistoMaker histoMaker = new HistoMaker();
            //Add the curve for the historic of mark
            histoMaker.addCurve( (String) result[0], (Map) result[1] );
            //Add the curve for the validity period
            histoMaker.addCurve(WebMessages.getString( pRequest, "reviewManualMark.validity" ), (Map) result[3] );
            JFreeChart chartKiviat = histoMaker.getChart(true);
            ChartRenderingInfo infoHisto = new ChartRenderingInfo( new StandardEntityCollection() );
            // save the graph as png in a temporary space
            String fileNameHisto =
                ServletUtilities.saveChartAsPNG( chartKiviat, HistoMaker.DEFAULT_WIDTH, HistoMaker.DEFAULT_HEIGHT,
                                                 infoHisto, pRequest.getSession() );
            histoChart = new GraphMaker( pRequest, fileNameHisto, infoHisto );
        }
        return histoChart;
    }
    
    /***
     * Cette m�thode sert d'interm�diaire pour lancer la construction
     * du graph (getGraph) et la r�cup�ration des commentaires
     * de la note manuelle, ces m�thodes partageant les m�mes donn�es
     * 
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @throws JrafEnterpriseException en cas de probl�me de r�cup�ration des donn�s
     * @throws IOException IOException en cas de probl�mes de cr�ation du graph
     */
    private void setParamsInForm( ActionForm pForm, HttpServletRequest pRequest ) throws JrafEnterpriseException, IOException
    {
        //obtenir les params n�cessaires pour lancer getGraph and setComments
        Object[] paramIn = getParams( pForm, pRequest );
        
        // Obtention de la couche m�tier
        IApplicationComponent ac2 = AccessDelegateHelper.getInstance( "Graph" );

        // Appel de la couche m�tier
        Object[] result = (Object[]) ac2.execute( "getHistoricGraph", paramIn );
        
        // R�cup�ration du componentDTO pour getGraph 
        ComponentDTO comp = (ComponentDTO) paramIn[0];
        
        //lancement de getGraph
        getGraph( pForm, pRequest, comp, result );
        
        //lancement de setCommentsInForm � partir de la liste de QualityResultDTO 
        //si la liste a bien �t� envoy�e
        ArrayList<QualityResultDTO> listeManualMark = new ArrayList<QualityResultDTO>(); 
        if ( result.length > 4 ) 
        {
            listeManualMark = (ArrayList<QualityResultDTO>) result[4];
            setCommentsInForm( listeManualMark, pForm );  
        }
    }
    
    /***
     * M�thode ajoutant l'historique des commentaires des notes manuelles
     * au formulaire
     * 
     * @param pManualMarkList la liste des commentaires
     */
    private void setCommentsInForm( ArrayList<QualityResultDTO> pManualMarkList, ActionForm pForm )
    {
        ArrayList<ParamReviewCommentsForm> commentsFormList = new ArrayList<ParamReviewCommentsForm>();
        //on pr�pare les formbean commentaire
        ParamReviewCommentsForm commentsForm;
        //on compose l'historique � afficher
        for ( QualityResultDTO dto : pManualMarkList )
        {
            commentsForm = new ParamReviewCommentsForm();
            // Transform the bo into form
            try
            {
                WTransformerFactory.objToForm( ParamReviewCommentsTransform.class, commentsForm, new Object[] { dto } );
            }
            catch ( WTransformerException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            commentsFormList.add( commentsForm );
        }
        //on ajoute la liste au formbean courant
        ( (ParamReviewForm) pForm).setCommentsList( commentsFormList ); 
    }
}
