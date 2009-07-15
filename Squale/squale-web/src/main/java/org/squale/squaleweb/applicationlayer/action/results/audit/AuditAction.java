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
package org.squale.squaleweb.applicationlayer.action.results.audit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.servlet.ServletUtilities;

import org.squale.jraf.commons.exception.JrafEnterpriseException;
import org.squale.jraf.helper.AccessDelegateHelper;
import org.squale.jraf.spi.accessdelegate.IApplicationComponent;
import org.squale.squalecommon.datatransfertobject.component.AuditDTO;
import org.squale.squalecommon.datatransfertobject.component.ComponentDTO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import org.squale.squaleweb.applicationlayer.action.ActionUtils;
import org.squale.squaleweb.applicationlayer.action.accessRights.ReaderAction;
import org.squale.squaleweb.applicationlayer.formbean.component.AuditForm;
import org.squale.squaleweb.applicationlayer.formbean.component.AuditListForm;
import org.squale.squaleweb.applicationlayer.formbean.component.SplitAuditsListForm;
import org.squale.squaleweb.applicationlayer.tracker.TrackerStructure;
import org.squale.squaleweb.comparator.AuditComparator;
import org.squale.squaleweb.resources.WebMessages;
import org.squale.squaleweb.transformer.ApplicationTransformer;
import org.squale.squaleweb.transformer.AuditListTransformer;
import org.squale.squaleweb.transformer.AuditTransformer;
import org.squale.squaleweb.transformer.ServeurListTransformer;
import org.squale.squaleweb.util.SqualeWebActionUtils;
import org.squale.squaleweb.util.graph.AuditTimeMaker;
import org.squale.squaleweb.util.graph.AuditsSizeMaker;
import org.squale.squaleweb.util.graph.GraphMaker;
import org.squale.welcom.struts.bean.WActionForm;
import org.squale.welcom.struts.transformer.WTransformerException;
import org.squale.welcom.struts.transformer.WTransformerFactory;

/**
 * Permet de manipuler le choix des audits.
 * 
 * @version 1.0
 * @author
 */
public class AuditAction
    extends ReaderAction
{

    /** Cl� pour r�cup�rer le nom du serveur */
    public static final String SERVER_NAME_KEY = "serverName";

    /** Cl� pour r�cup�rer la liste des serveurs */
    public static final String SERVER_LIST_KEY = "serverList";

    /**
     * Obtention de la m�thode componentAccess � utiliser Le nom de la m�thode � appeler d�pend du type d'audit
     * visualis�
     * 
     * @param pKind type d'audit
     * @return m�thode � appeler
     */
    private String getMethodForKind( String pKind )
    {
        String result = null;
        // Pas d'utilisation de map car trop peu de donn�es
        // La m�thode utilis�e d�pend du type d'audit
        if ( pKind.equals( "terminated" ) )
        {
            result = "getTerminatedAudits"; // Tous les audits
        }
        else if ( pKind.equals( "periodic" ) )
        {
            result = "getLastPeriodicAudits"; // Audits p�riodiques
        }
        else if ( pKind.equals( "milestone" ) )
        {
            result = "getLastMilestones"; // Audits de jalon
        }
        else if ( pKind.equals( "failed" ) )
        {
            result = "getFailedAudits"; // Audits en �chec
        }
        else if ( pKind.equals( "partial" ) )
        {
            result = "getPartialAudits"; // Audits partiels
        }
        return result;
    }

    /**
     * Permet la r�cup�ration de tous les audits de l'application
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

        ActionErrors errors = new ActionErrors();
        ActionForward forward = pMapping.findForward( "total_failure" );
        String kind = pRequest.getParameter( "kind" );
        try
        {
            // On efface ces forms contenant car si on passe par ici
            // Ces forms ne peuvent que contenir les informations pour une autre application
            pRequest.getSession().removeAttribute( "applicationErrorForm" );
            pRequest.getSession().removeAttribute( "resultListForm" );
            List audits = getAudits( pRequest, getMethodForKind( kind ) );
            WTransformerFactory.objToForm( AuditListTransformer.class, (SplitAuditsListForm) pForm, audits );
            forward = pMapping.findForward( "list" );
            // Place l'attribut dans la forme pour etre recuper� dans la jsp
            ( (SplitAuditsListForm) pForm ).setKind( kind );
        }
        catch ( Exception e )
        {
            handleException( e, errors, pRequest );
        }
        if ( !errors.isEmpty() )
        {
            saveMessages( pRequest, errors );
        }
        // On est pass� par un menu donc on r�initialise le traceur
        // et on rajoute la page courante
        String displayName = WebMessages.getString( pRequest.getLocale(), "tracker.audits.list." + kind );
        updateHistTracker( displayName, "audits.do?action=list&kind=" + kind, TrackerStructure.UNDEFINED, pRequest,
                           true );
        return ( forward );
    }

    /**
     * Permet la s�lection d'audits � afficher.
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
        ActionErrors errors = new ActionErrors();
        ActionForward forward = new ActionForward();
        try
        {
            List auditsSelected = getAuditsSelected( pForm, pRequest );
            // V�rification de la s�lection
            if ( auditsSelected.size() < 1 || auditsSelected.size() > 2 )
            {
                ActionError error = new ActionError( "error.invalid_audits_selection" );
                errors.add( "invalid.selection", error );
            }
            else
            {
                Long applicationId = new Long( ( (AuditForm) auditsSelected.get( 0 ) ).getApplicationId() );
                pRequest.setAttribute( "currentAuditId", "" + ( (AuditForm) auditsSelected.get( 0 ) ).getId() );
                if ( auditsSelected.size() == 1 )
                {
                    // S'il n'y en a qu'un de s�lectionn�, on choisit aussi le pr�c�dent de la liste
                    // s'il existe
                    AuditForm audit = getPreviousAudit( (AuditForm) auditsSelected.get( 0 ) );
                    if ( null != audit )
                    {
                        auditsSelected.add( audit );
                    }
                }
                // On trie les audits dans l'ordre inverse chronologique
                AuditComparator ac = new AuditComparator();
                Collections.sort( auditsSelected, ac );
                Collections.reverse( auditsSelected );

                // On remplit le formulaire qui sera mis en session
                AuditListForm listForm = new AuditListForm();
                listForm.setAudits( auditsSelected );
                // on a r�ussi � r�cup�rer l'audit pr�c�dent et il n'est pas nul
                if ( auditsSelected.size() == 2 )
                {
                    pRequest.setAttribute( "previousAuditId", "" + ( (AuditForm) auditsSelected.get( 1 ) ).getId() );
                }

                pRequest.removeAttribute( "id" );
                pRequest.setAttribute( "applicationId", applicationId.toString() );
                // on d�finit l'audit en cours pour v�rification (audit le plus r�cent)
                pRequest.setAttribute( "audit", auditsSelected.get( 0 ) );
                // Forward diff�rent selon le type de l'audit
                if ( pRequest.getParameter( "kind" ) != null && pRequest.getParameter( "kind" ).equals( "failed" ) )
                {
                    String oldAuditId = pRequest.getParameter( "oldAudit" );
                    String oldPreviousAuditId = pRequest.getParameter( "oldPreviousAudit" );
                    if ( null != oldAuditId )
                    {
                        // On le rajoute dans le requ�te pour pouvoir r�cup�rer l'action du bouton retour
                        pRequest.setAttribute( "oldAuditId", oldAuditId );
                        pRequest.setAttribute( "oldPreviousAuditId", oldPreviousAuditId );
                    }
                    else
                    {
                        // On met une valeur par d�faut
                        pRequest.setAttribute( "oldAuditId", "none" );
                        pRequest.setAttribute( "oldPreviousAuditId", "none" );
                    }
                    forward = pMapping.findForward( "applicationErrors" );
                }
                else
                {
                    forward = pMapping.findForward( "application" );
                }
                // In all cases, add user access for the application
                addUserAccess( pRequest, applicationId.longValue() );
            }
        }
        catch ( Exception e )
        {
            // Traitement factoris� des exceptions
            handleException( e, errors, pRequest );
        }
        if ( !errors.isEmpty() )
        {
            // Enregistrement du message et retour sur la page pour afficher l'erreur
            saveErrors( pRequest, errors );
            forward = pMapping.findForward( "failure" );
        }
        // pas de tracker ici
        return forward;
    }

    /**
     * Donne la liste des audits s�lectionn�s par l'utilisateur. La s�lection peut venir d'une liste de checkbox ou par
     * un lien donnant l'id de l'audit
     * 
     * @param pForm le formulaire
     * @param pRequest la requ�te
     * @return les audits s�lectionn�s
     * @throws WTransformerException si erreur lors de la transformation
     * @throws JrafEnterpriseException si erreur Jraf
     * @throws NumberFormatException si erreur de parsing
     */
    private List getAuditsSelected( ActionForm pForm, HttpServletRequest pRequest )
        throws WTransformerException, JrafEnterpriseException, NumberFormatException
    {
        SplitAuditsListForm form = (SplitAuditsListForm) pForm;
        // On remet � jour les form en session permettant d'acc�der aux diff�rents types d'audits
        List auditsSession = ActionUtils.getCurrentAuditsAsDTO( pRequest );
        form.resetAudits( auditsSession );
        pRequest.getSession().setAttribute( "splitAuditsListForm", form );

        Object param = pRequest.getParameter( "currentAuditId" );
        ArrayList auditsSelected = new ArrayList();
        // Dans le cas o� l'audit est donn� en param�tre, on va le chercher directement en base
        // Sinon on recherche les audits dans la liste en session car il s'agit d'une s�lection
        // par checkbox
        if ( null != param )
        {
            // Recheche en base
            IApplicationComponent ac = AccessDelegateHelper.getInstance( "Component" );
            Object[] auditIdParam = new Object[] { new Long( Long.parseLong( (String) param ) ) };
            AuditDTO auditDTO = (AuditDTO) ac.execute( "getById", auditIdParam );
            if ( null != auditDTO )
            {
                // On recherche l'application associ�e
                ac = AccessDelegateHelper.getInstance( "ApplicationAdmin" );
                ComponentDTO appli = (ComponentDTO) ac.execute( "loadByAuditId", auditIdParam );
                auditDTO.setApplicationId( appli.getID() );
                auditDTO.setApplicationName( appli.getName() );
                // On le transforme
                WActionForm auditForm = WTransformerFactory.objToForm( AuditTransformer.class, auditDTO );
                // et on l'ajoute � la liste des audits s�lectionn�s
                auditsSelected.add( auditForm );
            }
        }
        else
        {
            // Recherche par liste
            ArrayList audits = new ArrayList( form.getAudits() );
            audits.addAll( form.getPublicAudits() );
            auditsSelected.addAll( new AuditUtils().getSelection( audits ) );
        }
        return auditsSelected;
    }

    /**
     * Retourne l'audit pr�c�dent celui avec l'id param�tre dans la liste fournie. <br />
     * La pr�c�dence est calcul�e � partir de la date.
     * 
     * @param pAudit l'audit de r�f�rence
     * @return l'AuditForm suivant, ou null s'il n'en existe pas.
     * @throws WTransformerException si un probleme de transformation apparait.
     * @throws JrafEnterpriseException si un probleme hibernate apparait.
     */
    private AuditForm getPreviousAudit( final AuditForm pAudit )
        throws WTransformerException, JrafEnterpriseException
    {
        AuditForm auditTemp = null;
        AuditForm audit = null;
        AuditDTO auditDTO = (AuditDTO) WTransformerFactory.formToObj( AuditTransformer.class, pAudit )[0];
        IApplicationComponent ac = AccessDelegateHelper.getInstance( "Results" );
        Object[] paramIn = { auditDTO };
        auditDTO = (AuditDTO) ac.execute( "getPreviousAudit", paramIn );
        if ( auditDTO != null )
        {
            audit = (AuditForm) WTransformerFactory.objToForm( AuditTransformer.class, new Object[] { auditDTO } );
        }

        return audit;
    }

    /**
     * Execute la r�cup�ration des audits avec la m�thode de l'application component donn�e.
     * 
     * @param pRequest la requete http.
     * @param pMethod la methode � ex�cuter.
     * @return une liste tri�e d'audits.
     * @throws JrafEnterpriseException si un pb d'appel � l'AC survient.
     * @throws WTransformerException si un pb de transformation apparait.
     */
    private List getAudits( final HttpServletRequest pRequest, final String pMethod )
        throws JrafEnterpriseException, WTransformerException
    {
        IApplicationComponent ac = AccessDelegateHelper.getInstance( "Component" );
        ComponentDTO application =
            (ComponentDTO) WTransformerFactory.formToObj( ApplicationTransformer.class,
                                                          ActionUtils.getCurrentApplication( pRequest ) )[0];
        // on a juste besoin de l'application, la m�thode a d�j� �t� d�finie
        Object[] paramIn = new Object[] { application, null, null };
        Collection audits = (Collection) ac.execute( pMethod, paramIn );
        ArrayList results = new ArrayList();
        results.addAll( audits );
        return results;
    }

    /**
     * Permet de r�cup�rer les audits programm�s, ainsi que ceux interrompus/en cours
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward displayNotAttemptedAndRunning( ActionMapping pMapping, ActionForm pForm,
                                                        HttpServletRequest pRequest, HttpServletResponse pResponse )
    {

        ActionErrors errors = new ActionErrors();
        ActionForward forward = new ActionForward();

        try
        {
            // On veut les audits de suivis qui sont programm�s
            AuditListForm notAttemptedListForm = initAuditsList( AuditBO.NORMAL, new Integer( AuditBO.NOT_ATTEMPTED ) );
            // On veut tous les audits qui sont termin�s,
            AuditListForm shutDownListForm = initAuditsList( AuditBO.ALL_TYPES, new Integer( AuditBO.RUNNING ) );
            // Met � jour les 2 champs
            ( (SplitAuditsListForm) pForm ).setNotAttemptedAudits( notAttemptedListForm.getAudits() );
            ( (SplitAuditsListForm) pForm ).setShutDownAudits( shutDownListForm.getAudits() );
            forward = pMapping.findForward( "display" );
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
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );
        return forward;
    }

    /**
     * Permet de r�cup�rer les audits partiels ou en �chec
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward displayFailedOrPartial( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                                 HttpServletResponse pResponse )
    {

        ActionErrors errors = new ActionErrors();
        ActionForward forward = new ActionForward();

        try
        {
            // On veut les audits en �chec
            AuditListForm failedListForm = initAuditsList( AuditBO.ALL_TYPES, new Integer( AuditBO.FAILED ) );
            // On veut tous les audits partiels
            AuditListForm partialListForm = initAuditsList( AuditBO.ALL_TYPES, new Integer( AuditBO.PARTIAL ) );
            List partialAndFailed = failedListForm.getAudits();
            partialAndFailed.addAll( partialListForm.getAudits() );
            // Met � jour le champ
            ( (SplitAuditsListForm) pForm ).setFailedOrPartialAudits( partialAndFailed );
            forward = pMapping.findForward( "display" );
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
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );
        return forward;
    }

    /**
     * Permet de r�cup�rer les audits partiels ou en �chec
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward changeStatus( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                       HttpServletResponse pResponse )
    {
        ActionErrors errors = new ActionErrors();
        ActionForward forward = pMapping.findForward( "displayFailed" );
        ActionMessages messages = new ActionMessages();

        try
        {
            List audits = ( (SplitAuditsListForm) pForm ).getFailedOrPartialAudits();
            ArrayList changedAudits = new ArrayList();
            for ( int i = 0; i < audits.size(); i++ )
            {
                AuditForm auditForm = (AuditForm) audits.get( i );
                // Si un audit est s�lectionn�, c'est qu'on veut passer son status de failed � partiel
                // ou r�ciproquement
                if ( auditForm.isSelected() )
                {
                    if ( auditForm.getStatus() == AuditBO.PARTIAL )
                    {
                        auditForm.setStatus( AuditBO.FAILED );
                    }
                    else
                    { // sinon c'est forc�ment dans l'autre sens
                        auditForm.setStatus( AuditBO.PARTIAL );
                    }
                    // on enl�ve la s�lection
                    auditForm.setSelected( false );
                    // On transforme en dto
                    // On l'ajoute � la liste des audits � mettre � jour
                    changedAudits.add( WTransformerFactory.formToObj( AuditTransformer.class, (WActionForm) auditForm )[0] );
                }
            }
            // Sauvegarde les modifications en base
            // Le nombre d'audits � modifier
            Integer nbChanged = new Integer( changedAudits.size() );
            if ( nbChanged.intValue() > 0 )
            {
                IApplicationComponent ac = AccessDelegateHelper.getInstance( "Component" );
                // On update les audits
                Object[] paramIn = new Object[] { changedAudits };
                nbChanged = (Integer) ac.execute( "updateAuditsDateOrStatus", paramIn );
            }
            // On affiche le nombre d'audits modifi�s
            ActionMessage msg = new ActionMessage( "message.nbChangedAudits", nbChanged );
            messages.add( "informations", msg );
            saveMessages( pRequest, messages );
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
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );
        return forward;
    }

    /**
     * Permet de r�cup�rer les audits termin�s
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward displayTerminated( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                            HttpServletResponse pResponse )
    {

        ActionErrors errors = new ActionErrors();
        ActionForward forward = new ActionForward();

        try
        {
            // R�cup�re le dernier audit r�ussi de chaque application
            IApplicationComponent ac = AccessDelegateHelper.getInstance( "Component" );
            Collection audits = (Collection) ac.execute( "getLastTerminatedAudits" );
            AuditListForm listForm = (AuditListForm) WTransformerFactory.objToForm( AuditListTransformer.class, audits );
            List auditsList = listForm.getAudits();
            // Met � jour le champ
            ( (SplitAuditsListForm) pForm ).setTerminatedAudits( auditsList );
            // On r�cup�re le nom du serveur s�lectionn�
            String serverName = (String) pRequest.getParameter( SERVER_NAME_KEY );
            // Construction des graphes de statistiques par serveur
            GraphMaker timeGraph = null;
            GraphMaker sizeGraph = null;
            if ( null != serverName && serverName.length() > 0 )
            {
                GraphMaker[] graphics = buildStatGraphics( pRequest, auditsList, serverName );
                // Ajoute les makers au form pour pouvoir l'afficher (appel dans la jsp)
                // sur le temps
                timeGraph = graphics[0];
                // sur la taille
                sizeGraph = graphics[1];
            }
            else
            {
                serverName = "";
            }
            // Affectation des graphes (null si il ne doivent pas appra�tre dans la jsp)
            ( (SplitAuditsListForm) pForm ).setTimeMaker( timeGraph );
            ( (SplitAuditsListForm) pForm ).setSizeMaker( sizeGraph );
            // chargement de la liste des serveurs
            IApplicationComponent acServer = AccessDelegateHelper.getInstance( "Serveur" );
            Collection serversDTO = (Collection) acServer.execute( "listeServeurs" );
            // On place la liste des serveurs dans la session (et non dans la requ�te pour les tris welcom)
            pRequest.getSession().setAttribute(
                                                SERVER_LIST_KEY,
                                                WTransformerFactory.objToForm( ServeurListTransformer.class, serversDTO ) );
            // Ainsi que le nom s�lectionn�
            pRequest.setAttribute( SERVER_NAME_KEY, serverName );
            forward = pMapping.findForward( "list_terminated" );
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
        // On est pass� par un menu donc on r�initialise le traceur
        resetTracker( pRequest );
        return forward;
    }

    /**
     * Consrtuit les grpahes repr�sentant les statistiques par serveur sur le temps et la taille des audits
     * 
     * @param pRequest la requ�te
     * @param auditsList la liste des audits
     * @param serverName le nom du serveur
     * @return un tableau de GraphMaker � deux �l�ment : [TimeMaker, SizeMaker]
     * @throws IOException si erreur � la transformation des graphes en image
     */
    private GraphMaker[] buildStatGraphics( HttpServletRequest pRequest, List auditsList, String serverName )
        throws IOException
    {
        // Initialisation
        GraphMaker timeMaker = null;
        List durationList = new ArrayList( 0 );
        List fsSizeList = new ArrayList( 0 );
        GraphMaker sizeMaker = null;
        // ajoute les valeurs d�j� r�cup�r�es aux maps
        for ( int i = 0; i < auditsList.size(); i++ )
        {
            AuditForm form = (AuditForm) auditsList.get( i );
            if ( serverName.equals( form.getServerName() ) )
            {
                String auditDate =
                    SqualeWebActionUtils.getFormattedDate( pRequest.getLocale(), form.getDate(),
                                                           "date.format.simple.short" );
                durationList.add( new Object[] { auditDate, form.getDuration(), form.getApplicationName() } );
                fsSizeList.add( new Object[] { auditDate, form.getMaxFileSystemSize(), form.getApplicationName() } );
            }
        }
        // construit le maker et lui passe les valeurs afin qu'il construise
        // les diff�rentes courbes si il y a des donn�es
        if ( durationList.size() > 0 )
        {
            AuditTimeMaker statsTimeMaker = new AuditTimeMaker( pRequest );
            statsTimeMaker.setValues( durationList );
            AuditsSizeMaker statsSizeMaker = new AuditsSizeMaker( pRequest );
            statsSizeMaker.addCurve( fsSizeList );
            JFreeChart timeChart = statsTimeMaker.getChart();
            JFreeChart sizeChart = statsSizeMaker.getChart();
            ChartRenderingInfo infoStats = new ChartRenderingInfo( new StandardEntityCollection() );
            // Initialisation des graphs
            String fileNameTime =
                ServletUtilities.saveChartAsPNG( timeChart, statsTimeMaker.getDefaultWidth(),
                                                 statsTimeMaker.getDefaultHeight(), infoStats, pRequest.getSession() );
            timeMaker = new GraphMaker( pRequest, fileNameTime, infoStats );
            infoStats = new ChartRenderingInfo( new StandardEntityCollection() );
            String fileNameSize =
                ServletUtilities.saveChartAsPNG( sizeChart, statsSizeMaker.getDefaultWidth(),
                                                 statsSizeMaker.getDefaultHeight(), infoStats, pRequest.getSession() );
            sizeMaker = new GraphMaker( pRequest, fileNameSize, infoStats );
        }
        return new GraphMaker[] { timeMaker, sizeMaker };
    }

    /**
     * Factorisation de code Permet de r�cup�rer une liste d'audits en fonction du type d'audits et du status de
     * l'audit, en r�cup�rant �galement l'application associ�.
     * 
     * @param pAuditType le type de l'audit
     * @param pAuditStatus le status de l'audit
     * @throws JrafEnterpriseException en cas d'�chec
     * @throws WTransformerException en cas d'�chec de la transformation des DTO en form correspondants
     * @return la liste des auditsForm
     */
    private AuditListForm initAuditsList( String pAuditType, Integer pAuditStatus )
        throws JrafEnterpriseException, WTransformerException
    {
        IApplicationComponent ac = AccessDelegateHelper.getInstance( "Component" );
        Object[] paramIn = new Object[] { pAuditType, pAuditStatus };
        Collection audits = (Collection) ac.execute( "getAudits", paramIn );
        AuditListForm ListForm = (AuditListForm) WTransformerFactory.objToForm( AuditListTransformer.class, audits );
        // R�cup�re les diff�rents auditsForm de la liste, r�cup�re l'application associ�e
        // et l'affecte au form
        for ( int i = 0; i < ListForm.getAudits().size(); i++ )
        {
            AuditForm auditForm = (AuditForm) ListForm.getAudits().get( i );
            ComponentDTO appliDTO = new ComponentDTO();
            appliDTO.setID( new Long( auditForm.getApplicationId() ).longValue() );
            appliDTO = (ComponentDTO) ac.execute( "get", new Object[] { appliDTO } );
            auditForm.setApplicationName( appliDTO.getName() );
        }
        return ListForm;
    }

    /**
     * Permet de changer la date d'un audit de suivi.
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward changeDate( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                     HttpServletResponse pResponse )
    {
        ActionMessages messages = new ActionMessages();
        ActionErrors errors = new ActionErrors();
        ActionForward forward = pMapping.findForward( "display" );
        try
        {
            // On r�cup�re les audits modifi�s
            AuditListForm form = (AuditListForm) pForm;
            ArrayList changedAudits = new ArrayList();
            // On parcours tous les audits pour ne mettre � jour que ceux qui
            // ont �t� modifi�s
            for ( int i = 0; i < form.getNotAttemptedAudits().size(); i++ )
            {
                AuditForm auditForm = (AuditForm) form.getNotAttemptedAudits().get( i );
                if ( auditForm.isEdited() )
                {
                    // On transforme en dto
                    // On l'ajoute � la liste des audits � mettre � jour
                    changedAudits.add( WTransformerFactory.formToObj( AuditTransformer.class, (WActionForm) auditForm )[0] );
                    // Les champs d'�dition sont mis � false
                    auditForm.setChanged( false );
                    auditForm.setEdited( false );
                }
            }
            // Le nombre d'audits � modifier
            Integer nbChanged = new Integer( changedAudits.size() );
            if ( nbChanged.intValue() > 0 )
            {
                IApplicationComponent ac = AccessDelegateHelper.getInstance( "Component" );
                // On update les audits
                Object[] paramIn = new Object[] { changedAudits };
                nbChanged = (Integer) ac.execute( "updateAuditsDateOrStatus", paramIn );
            }
            // On affiche le nombre d'audits modifi�s
            ActionMessage msg = new ActionMessage( "message.nbChangedAudits", nbChanged );
            messages.add( "informations", msg );
            saveMessages( pRequest, messages );
        }
        catch ( Exception e )
        {
            // Exception
            handleException( e, errors, pRequest );
            // Enregistrement du message
            saveErrors( pRequest, errors );
        }
        return forward;
    }

}
