//Source file: D:\\CC_VIEWS\\SQUALE_V0_0_ACT\\SQUALE\\SRC\\squaleCommon\\src\\com\\airfrance\\squalecommon\\enterpriselayer\\facade\\AuditFacade.java

package com.airfrance.squalecommon.enterpriselayer.facade.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.jraf.commons.exception.JrafEnterpriseException;
import com.airfrance.jraf.helper.PersistenceHelper;
import com.airfrance.jraf.provider.persistence.hibernate.facade.FacadeHelper;
import com.airfrance.jraf.spi.enterpriselayer.IFacade;
import com.airfrance.jraf.spi.persistence.IPersistenceProvider;
import com.airfrance.jraf.spi.persistence.ISession;
import com.airfrance.squalecommon.daolayer.component.AuditDAOImpl;
import com.airfrance.squalecommon.daolayer.component.ApplicationDAOImpl;
import com.airfrance.squalecommon.datatransfertobject.component.AuditDTO;
import com.airfrance.squalecommon.datatransfertobject.component.ComponentDTO;
import com.airfrance.squalecommon.datatransfertobject.transform.component.AuditTransform;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;
import com.airfrance.squalecommon.enterpriselayer.facade.FacadeMessages;

/**
 */
public class AuditFacade
    implements IFacade
{

    /**
     * Entier renseignant que l'on souhaite tous les audits
     */
    public static final int ALL_AUDIT = 0;

    /**
     * Entier renseignant que l'on souhaite uniquement les audits de suivi
     */
    public static final int PERIODIC_ONLY = 1;

    /**
     * Entier renseignant qu'on souhaite uniquement les audits de jalon
     */
    public static final int MILESTONE_ONLY = 2;

    /** log */
    private static Log LOG = LogFactory.getLog( AuditFacade.class );

    /**
     * provider de persistence
     */
    private static final IPersistenceProvider PERSISTENTPROVIDER = PersistenceHelper.getPersistenceProvider();

    /**
     * Permet de supprimer un audit donn� et tous ses r�sultats associ�s
     * 
     * @use by PurgeComponent.purgeApplication()
     * @param pAudit AuditDTO renseignant l'ID de l'audit
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB0024D
     */
    public static void delete( AuditDTO pAudit )
        throws JrafEnterpriseException
    {
        delete( pAudit, null );
    }

    /**
     * Charge l'audit
     * 
     * @param pAudit l'audit � r�cup�rer (on ne fourni que l'id)
     * @return l'audit
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public static AuditDTO get( AuditDTO pAudit )
        throws JrafEnterpriseException
    {
        return getById( new Long( pAudit.getID() ) );
    }

    /**
     * Permet de supprimer un audit donn� et tous ses r�sultats associ�s
     * 
     * @use by PurgeComponent.purgeApplication()
     * @param pAudit AuditDTO renseignant l'ID de l'audit
     * @param pSession session JRAF
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB0024F
     */
    public static void delete( AuditDTO pAudit, ISession pSession )
        throws JrafEnterpriseException
    {

        // Initialisation
        AuditBO auditBO = null; // objet metier a supprimer
        Long auditID = new Long( pAudit.getID() ); // identifiant de l'audit
        try
        {
            if ( pSession == null )
            {
                // CHECKSTYLE:OFF
                pSession = PERSISTENTPROVIDER.getSession();
                // CHECKSTYLE:ON
            }
            AuditDAOImpl auditDAO = AuditDAOImpl.getInstance();
            // Chargement de l'objet AuditBO + suppression
            auditBO = (AuditBO) auditDAO.load( pSession, auditID );
            if ( auditBO != null )
            {
                auditDAO.remove( pSession, auditBO );
            }
            else
            {
                LOG.error( FacadeMessages.getString( "facade.exception.auditfacade.delete.auditnull" ) );
            }
        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, AuditFacade.class.getName() + ".delete" );
        }
        finally
        {
            FacadeHelper.closeSession( pSession, AuditFacade.class.getName() + ".delete" );
        }
    }

    /**
     * Renvoie le dernier audit DE SUIVI de l'application en cours
     * 
     * @use by ResultsComponent
     * @param pApplication ComponentDTO de l'application concern�e
     * @param pAuditType type d'Audit sous forme de cha�nes de caract�res
     * @return AuditDTO dernier audit effectu�
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB00258
     */
    public static AuditDTO getLastAudit( ComponentDTO pApplication, String pAuditType )
        throws JrafEnterpriseException
    {
        return getLastAudit( pApplication, pAuditType, null );
    }

    /**
     * Renvoie l'audit pr�cedent pour la m�me application
     * 
     * @param pAudit audit courant
     * @param pSession session hibernat
     * @return l'audit pr�cedent s'il existe
     * @throws JrafEnterpriseException si pb hibernate
     */
    public static AuditDTO getPreviousAudit( AuditDTO pAudit, ISession pSession )
        throws JrafEnterpriseException
    {
        AuditDTO auditDTO = null;
        AuditDAOImpl auditDAO = AuditDAOImpl.getInstance();
        ISession session = pSession;
        try
        {
            // cr�ation d'une session Hibernate
            if ( session == null )
            {
                session = PERSISTENTPROVIDER.getSession();
            }
            // On r�cup�re l'audit en base
            Long auditId = new Long( pAudit.getID() );
            AuditBO curAudit = (AuditBO) auditDAO.load( session, auditId );
            // et l'application associ�e
            ApplicationBO appli = ApplicationDAOImpl.getInstance().loadByAuditId( session, auditId );
            if ( curAudit != null && appli != null )
            {
                AuditBO audit =
                    auditDAO.findPreviousAudit( session, curAudit.getId(), curAudit.getHistoricalDate(), appli.getId(),
                                                null );
                if ( audit != null )
                {
                    auditDTO = AuditTransform.bo2Dto( audit, pAudit.getApplicationId() );
                }
            }

        }
        catch ( JrafDaoException e )
        {
            LOG.error( AuditFacade.class.getName() + ".getLastAudit", e );
        }
        finally
        {
            FacadeHelper.closeSession( session, AuditFacade.class.getName() + ".getLastAudit" );
        }
        return auditDTO;
    }

    /**
     * Renvoie le dernier audit DE SUIVI de l'application en cours
     * 
     * @use by ResultsComponent
     * @param pApplication ComponentDTO de l'application concern�e
     * @param pAuditType type d'Audit sous forme de cha�nes de caract�res
     * @param pSession session jraf
     * @return AuditDTO dernier audit effectu�
     * @throws JrafEnterpriseException exception JRAF
     */
    public static AuditDTO getLastAudit( ComponentDTO pApplication, String pAuditType, ISession pSession )
        throws JrafEnterpriseException
    {

        // Initialisation
        AuditDTO auditDTO = null; // retour de la facade
        AuditBO auditBO = null; // retour de AuditDAO

        ISession session = pSession;
        try
        {
            // cr�ation d'une session Hibernate
            if ( session == null )
            {
                session = PERSISTENTPROVIDER.getSession();
            }

            AuditDAOImpl auditDAO = AuditDAOImpl.getInstance();

            auditBO =
                auditDAO.getLastAuditByApplication( session, pApplication.getID(), pAuditType, AuditBO.TERMINATED );

            if ( auditBO != null )
            {
                auditDTO = AuditTransform.bo2Dto( auditBO, pApplication.getID() );
            }
            else
            {
                LOG.error( FacadeMessages.getString( "facade.exception.auditfacade.getlastaudit.auditnull" ) );
            }

        }
        catch ( JrafDaoException e )
        {
            LOG.error( AuditFacade.class.getName() + ".getLastAudit", e );
        }
        finally
        {
            FacadeHelper.closeSession( session, AuditFacade.class.getName() + ".getLastAudit" );
        }
        return auditDTO;
    }

    /**
     * Permet de r�cup�rer une liste d'audits en �chec pour une application donn�e
     * 
     * @use by ResultsComponent
     * @param pComponent ApplicationDTO renseignant l'ID de l'application
     * @param pNbLignes nombre de lignes
     * @param pIndexDepart index de depart
     * @return Collection de AuditDTO
     * @throws JrafEnterpriseException exception JRAF
     */
    public static List getFailedAudits( ComponentDTO pComponent, Integer pNbLignes, Integer pIndexDepart )
        throws JrafEnterpriseException
    {
        return getAudits( pComponent, pNbLignes, pIndexDepart, null, AuditBO.FAILED );
    }

    /**
     * Permet de r�cup�rer une liste d'audits termin�s pour une application donn�e
     * 
     * @use by ResultsComponent
     * @param pComponent ApplicationDTO renseignant l'ID de l'application
     * @param pNbLignes nombre de lignes
     * @param pIndexDepart index de depart
     * @return Collection de AuditDTO
     * @throws JrafEnterpriseException exception JRAF
     */
    public static List getTerminatedAudits( ComponentDTO pComponent, Integer pNbLignes, Integer pIndexDepart )
        throws JrafEnterpriseException
    {
        return getAudits( pComponent, pNbLignes, pIndexDepart, null, AuditBO.TERMINATED );
    }

    /**
     * Permet de r�cup�rer une liste d'audits pour une application donn�e
     * 
     * @use by ResultsComponent
     * @param pComponent ApplicationDTO renseignant l'ID de l'application
     * @param pNbLignes nombre de lignes
     * @param pIndexDepart index de depart
     * @param pType type de l'audit
     * @param pStatus statut de l'audit
     * @return Collection de AuditDTO
     * @throws JrafEnterpriseException exception JRAF
     */
    public static List getAudits( ComponentDTO pComponent, Integer pNbLignes, Integer pIndexDepart, String pType,
                                  int pStatus )
        throws JrafEnterpriseException
    {
        // Initialisation
        List auditBOs = null; // retour de la DAO
        List auditDTOs = new ArrayList(); // retour de la facade

        ISession session = null;
        try
        {
            // cr�ation d'une session Hibernate
            session = PERSISTENTPROVIDER.getSession();

            AuditDAOImpl auditDAO = AuditDAOImpl.getInstance();
            // Obtention des audits dans la base
            auditBOs =
                auditDAO.findWhereComponent( session, pComponent.getID(), pNbLignes, pIndexDepart, pType, pStatus );

            // Parcours de chaque audit et transformation de l'audit
            Iterator iterator = auditBOs.iterator();
            AuditBO auditBO = null;
            while ( iterator.hasNext() )
            {
                auditBO = (AuditBO) iterator.next();
                // Recuperation du BO dans la collection + transformation en DTO
                // ajout du AuditDTO a la collection de DTOs
                AuditDTO auditDTO = AuditTransform.bo2Dto( auditBO, pComponent.getID() );
                auditDTOs.add( auditDTO );
            }
        }
        catch ( JrafDaoException e )
        {
            auditDTOs = null;
            LOG.error( AuditFacade.class.getName() + ".getLastAudits", e );
        }
        finally
        {
            FacadeHelper.closeSession( session, AuditFacade.class.getName() + ".getLastAudits" );
        }
        return auditDTOs;
    }

    /**
     * Permet de r�cup�rer laudits termin�s pour une application donn�e
     * 
     * @use by ResultsComponent
     * @param pComponent ApplicationDTO renseignant l'ID du projet
     * @param pNbLignes nombre de lignes
     * @param pIndexDepart index de depart
     * @param pType type de l'audit
     * @param pStatus le status de l'audit
     * @return Collection de AuditDTO
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB00275
     */
    public static List getLastAudits( ComponentDTO pComponent, Integer pNbLignes, Integer pIndexDepart, String pType,
                                      int pStatus )
        throws JrafEnterpriseException
    {
        return getAudits( pComponent, pNbLignes, pIndexDepart, pType, pStatus );
    }

    /**
     * The list of audits for the applications of pApplications which have been done after pDate (if this parameter is
     * not null) and whose status aren't in pExcludedStatus.
     * 
     * @param pApplications The applications
     * @param pDate The date
     * @param pExcludedStatus The list of status which should be excluded
     * @return The list of audits
     * @throws JrafEnterpriseException If an error occur
     */
    public static Collection getAllAuditsAfterDate( Collection pApplications, Date pDate, Integer[] pExcludedStatus )
        throws JrafEnterpriseException
    {

        // Initialization
        List auditBOs = null;
        List auditDTOs = new ArrayList();
        List appliIds = new ArrayList();
        ISession session = null;

        // Creation of the list of applications id's
        Iterator it = pApplications.iterator();
        while ( it.hasNext() )
        {
            ComponentDTO currentComponent = (ComponentDTO) it.next();
            appliIds.add( new Long( currentComponent.getID() ) );
        }

        try
        {
            // The hibernate session
            session = PERSISTENTPROVIDER.getSession();
            AuditDAOImpl auditDAO = AuditDAOImpl.getInstance();
            auditBOs = auditDAO.findAfterDateWhereComponents( session, appliIds, pDate, pExcludedStatus, null );
            auditDTOs = bo2DtoWithApplication( session, auditBOs );
        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, AuditFacade.class.getName() + ".getAllAuditsAfterDate" );
        }

        finally
        {
            FacadeHelper.closeSession( session, AuditFacade.class.getName() + ".getAllAuditsAfterDate" );
        }
        return auditDTOs;
    }

    /**
     * @param pApplication l' application renseignant l'id
     * @param pDate la date limite de r�cup�ration des audits
     * @param pWithFailedAudits indique si les audits en �chec doivent �galement �tre collect�s
     * @return les audits correspondants aux crit�res de recherche
     * @throws JrafEnterpriseException exception JRAF
     */
    private static Collection getAllAuditsAfterDate( ComponentDTO pApplication, Date pDate, boolean pWithFailedAudits )
        throws JrafEnterpriseException
    {
        // Initialisation
        List auditBOs = null; // retour de la DAO
        List auditDTOs = new ArrayList(); // retour de la facade

        ISession session = null;
        try
        {
            // cr�ation d'une session Hibernate
            session = PERSISTENTPROVIDER.getSession();

            AuditDAOImpl auditDAO = AuditDAOImpl.getInstance();
            // Obtention des audits dans la base
            auditBOs = auditDAO.findAfterDateWhereComponent( session, pApplication.getID(), pDate, pWithFailedAudits );

            // Parcours de chaque audit et transformation de l'audit
            Iterator iterator = auditBOs.iterator();
            AuditBO auditBO = null;
            while ( iterator.hasNext() )
            {
                auditBO = (AuditBO) iterator.next();
                // Recuperation du BO dans la collection + transformation en DTO
                // ajout du AuditDTO a la collection de DTOs
                AuditDTO auditDTO = AuditTransform.bo2Dto( auditBO, pApplication.getID() );
                auditDTO.setApplicationName( pApplication.getName() );
                auditDTOs.add( auditDTO );
            }
        }
        catch ( JrafDaoException e )
        {
            auditDTOs = null;
            LOG.error( AuditFacade.class.getName() + ".getAllAuditsAfterDate", e );
        }
        finally
        {
            FacadeHelper.closeSession( session, AuditFacade.class.getName() + ".getAllAuditsAfterDate" );
        }
        return auditDTOs;
    }

    /**
     * @param pApplications les applications
     * @param pDate la date
     * @param pExcludedStatus les statuts � exclure
     * @param pExcludedApplications les ids des applications � ne pas prendre en compte
     * @param pNbAuditsPerAppli le nombre max d'audits par application
     * @return les audits pour les applications de pApplications dont l'ex�cution s'est effectu� apr�s pDate (si ce
     *         param�tre n'est pas nul) et dont le statut n'est pas dans <code>pExcludedStatus</code>
     * @throws JrafEnterpriseException si erreur
     */
    public static Collection getAuditsForPortlet( Collection pApplications, Date pDate, Integer[] pExcludedStatus,
                                                  String[] pExcludedApplications, Integer pNbAuditsPerAppli )
        throws JrafEnterpriseException
    {
        // Initialisation
        List auditBOs = null; // retour de la DAO
        List auditDTOs = new ArrayList(); // retour de la facade
        // DAO
        AuditDAOImpl auditDAO = AuditDAOImpl.getInstance();
        ApplicationDAOImpl appliDAO = ApplicationDAOImpl.getInstance();

        ISession session = null;
        try
        {
            // cr�ation d'une session Hibernate
            session = PERSISTENTPROVIDER.getSession();

            List excludedApplisList = new ArrayList( 0 );
            if ( null != pExcludedApplications )
            {
                excludedApplisList = Arrays.asList( pExcludedApplications );
            }
            List appliIds = new ArrayList();
            // On construit la liste des ids des applications en prenant en compte les applications exclues
            for ( Iterator it = pApplications.iterator(); it.hasNext(); )
            {
                ComponentDTO currentComponent = (ComponentDTO) it.next();
                if ( !excludedApplisList.contains( "" + currentComponent.getID() ) )
                {
                    appliIds.add( new Long( currentComponent.getID() ) );
                }
            }
            auditBOs =
                auditDAO.findAfterDateWhereComponents( session, appliIds, pDate, pExcludedStatus, pNbAuditsPerAppli );
            if ( 0 == auditBOs.size() )
            {
                // On r�cup�re le dernier audit effectu�
                auditBOs =
                    auditDAO.findAfterDateWhereComponents( session, appliIds, null, pExcludedStatus, new Integer( 1 ) );
            }
            // On transforme les audits afin que l'application associ�e soit aussi renseign�e
            auditDTOs = bo2DtoWithApplication( session, auditBOs );
        }
        catch ( JrafDaoException e )
        {
            auditDTOs = null;
            LOG.error( AuditFacade.class.getName() + ".getAuditsForPortlet", e );
        }
        finally
        {
            FacadeHelper.closeSession( session, AuditFacade.class.getName() + ".getAuditsForPortlet" );
        }
        return auditDTOs;
    }

    /**
     * permet d'ajouter un audit � une application donn�e (cas d'un milestone)
     * 
     * @use by ApplicationAdministratorComponent
     * @param pAudit AuditDTO
     * @return AuditDTO avec l'identifiant renseign�
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB00289
     */
    public static AuditDTO insertAudit( AuditDTO pAudit )
        throws JrafEnterpriseException
    {
        return insertAudit( pAudit, null );
    }

    /**
     * permet d'ajouter un audit � une application donn�e (cas d'un milestone)
     * 
     * @use by ApplicationAdministratorComponent
     * @param pAudit AuditDTO
     * @param pSession session JRAF
     * @return AuditDTO avec l'identifiant renseign�
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB0028C
     */
    public static AuditDTO insertAudit( AuditDTO pAudit, ISession pSession )
        throws JrafEnterpriseException
    {

        // Initialisation
        AuditDTO auditDTO = null; // retour de la facade
        AuditBO auditBO = null; // objet metier audit a ajouter
        ApplicationBO applicationBO = null; // projet relatif a l'audit
        Long applicationID = new Long( pAudit.getApplicationId() );

        // identifiant du projet relatif a un audit
        try
        {
            // creation d'une session Hibernate
            if ( pSession == null )
            {
                // CHECKSTYLE:OFF
                pSession = PERSISTENTPROVIDER.getSession();
                // CHECKSTYLE:ON
            }

            // Initialisation des DAO
            AuditDAOImpl auditDAO = AuditDAOImpl.getInstance();
            ApplicationDAOImpl applicationDAO = ApplicationDAOImpl.getInstance();

            // Chargement du projet relatif
            applicationBO = (ApplicationBO) applicationDAO.get( pSession, applicationID );

            if ( applicationBO != null )
            {
                // Verification qu'un premier audit de jalon n'a pas ete pos�
                int nbAudits = auditDAO.countWhereType( pSession, applicationBO, pAudit.getType(), pAudit.getStatus() );

                if ( nbAudits == 0 )
                {
                    // transformation de l'audit
                    auditBO = AuditTransform.dto2Bo( pAudit );
                    // Creation de l'audit sans aucune relation
                    auditDAO.create( pSession, auditBO );
                    // ajout de l'audit et sauvegarde du projet
                    applicationBO.addAudit( auditBO );
                    applicationDAO.save( pSession, applicationBO );
                    auditDTO = AuditTransform.bo2Dto( auditBO, applicationID.longValue() );
                }

            }
            else
            {
                LOG.error( FacadeMessages.getString( "facade.exception.auditfacade.insertaudit.applicationnull" ) );
            }

        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, AuditFacade.class.getName() + ".insertNextAudit" );
        }
        finally
        {
            FacadeHelper.closeSession( pSession, AuditFacade.class.getName() + ".insertNextAudit" );
        }

        return auditDTO;

    }

    /**
     * Insertion d'un audit.
     * 
     * @param pAuditDTO l'audit � ins�rer
     * @param pSession la session hibernate
     * @return le DTO de l'audit ins�r� (null en cas d'erreur d'insertion)
     * @throws JrafEnterpriseException si erreur
     */
    private static AuditDTO insertNormalAudit( AuditDTO pAuditDTO, ISession pSession )
        throws JrafEnterpriseException
    {
        Long applicationID = new Long( pAuditDTO.getApplicationId() );
        AuditDTO lAuditDTO = null;
        try
        {
            AuditDAOImpl lAuditDAO = AuditDAOImpl.getInstance();
            ApplicationDAOImpl lApplicationDAO = ApplicationDAOImpl.getInstance();

            ApplicationBO lApplicationBO = (ApplicationBO) lApplicationDAO.get( pSession, applicationID );
            if ( lApplicationBO != null )
            {
                AuditBO lAuditBO = AuditTransform.dto2Bo( pAuditDTO );
                lAuditDAO.create( pSession, lAuditBO );
                lApplicationBO.addAudit( lAuditBO );
                lApplicationDAO.save( pSession, lApplicationBO );
                lAuditDTO = AuditTransform.bo2Dto( lAuditBO );
            }
        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, AuditFacade.class.getName() + ".insertNormalAudit" );
        }
        return lAuditDTO;
    }

    /**
     * Constructeur par defaut
     * 
     * @roseuid 42CBFFB00296
     */
    private AuditFacade()
    {
    }

    /**
     * Permet de reprogramm� un audit sur une application au lendemain. si il s'agit d'un audit de suivi, l'audit d�j�
     * programm� est supprim�
     * 
     * @param pAudit AuditDTO d�finissant l'id de l'audit
     * @return l'audit reprogramm�
     * @throws JrafEnterpriseException si erreur
     */
    public static AuditDTO restartAudit( AuditDTO pAudit )
        throws JrafEnterpriseException
    {
        AuditDTO auditDTO = null;
        ISession session = null;
        try
        {
            // cr�ation d'une session Hibernate
            session = PERSISTENTPROVIDER.getSession();
            AuditDAOImpl auditDAO = AuditDAOImpl.getInstance();
            // On r�cup�re l'audit dont l'id est renseign� par pAudit
            AuditBO failedAudit = (AuditBO) auditDAO.load( session, new Long( pAudit.getID() ) );
            // On transforme le bo en dto
            auditDTO = AuditTransform.bo2Dto( failedAudit, pAudit.getApplicationId() );
            // On change la date pour le lancer le lendemain � 0:00
            GregorianCalendar cal = new GregorianCalendar();
            cal.add( GregorianCalendar.HOUR_OF_DAY, ApplicationFacade.HOUR_OF_AUDIT
                - cal.get( GregorianCalendar.HOUR_OF_DAY ) );
            cal.add( GregorianCalendar.MINUTE, ApplicationFacade.MINUTE_OF_AUDIT - cal.get( GregorianCalendar.MINUTE ) );
            auditDTO.setDate( cal.getTime() );
            // Son status sera en attente d'ex�cution
            auditDTO.setStatus( AuditBO.NOT_ATTEMPTED );
            // On ins�re l'audit avec un traitement diff�rent selon le type de l'audit
            if ( auditDTO.getType().equals( AuditBO.MILESTONE ) )
            {
                auditDTO = insertAudit( auditDTO );
            }
            else
            { // Il s'agit d'un audit de suivi
                auditDTO = restartNormalAudit( auditDTO );
            }
        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, AuditFacade.class.getName() + ".restartAudit" );
        }
        finally
        {
            FacadeHelper.closeSession( session, AuditFacade.class.getName() + ".restartAudit" );
        }
        return auditDTO;
    }

    /**
     * Ins�re un audit de suivi et supprime celui cr�e normalement
     * 
     * @param pAudit l'audit de suivi � ins�rer
     * @return l'audit ins�r� ou null si l'insertion a �chou�
     * @throws JrafEnterpriseException si erreur
     */
    private static AuditDTO restartNormalAudit( AuditDTO pAudit )
        throws JrafEnterpriseException
    {
        // Initialisation
        AuditDTO auditDTO = null; // retour de la facade
        AuditBO auditBO = null; // objet metier audit a ajouter
        ApplicationBO applicationBO = null; // projet relatif a l'audit
        ISession session = null; // session hibernate
        // identifiant du projet relatif a un audit
        Long applicationID = new Long( pAudit.getApplicationId() );
        try
        {
            session = PERSISTENTPROVIDER.getSession();
            // Initialisation des DAO
            AuditDAOImpl auditDAO = AuditDAOImpl.getInstance();
            ApplicationDAOImpl applicationDAO = ApplicationDAOImpl.getInstance();
            // Chargement du projet relatif
            applicationBO = (ApplicationBO) applicationDAO.get( session, applicationID );
            if ( applicationBO != null )
            {
                // R�cup�ration du prochain audit lanc� normalement
                AuditBO nextAudit =
                    auditDAO.getLastAuditByApplication( session, pAudit.getApplicationId(), pAudit.getType(),
                                                        pAudit.getStatus() );

                if ( null != nextAudit )
                {
                    // On supprime le prochain audit
                    auditDAO.remove( session, nextAudit );
                }
                // On ins�re le nouveau
                // transformation de l'audit
                auditBO = AuditTransform.dto2Bo( pAudit );
                // Creation de l'audit sans aucune relation
                auditDAO.create( session, auditBO );
                // ajout de l'audit et sauvegarde du projet
                applicationBO.addAudit( auditBO );
                applicationDAO.save( session, applicationBO );
                auditDTO = AuditTransform.bo2Dto( auditBO, applicationID.longValue() );

            }
            else
            {
                LOG.error( FacadeMessages.getString( "facade.exception.auditfacade.insertaudit.applicationnull" ) );
            }

        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, AuditFacade.class.getName() + ".restartNormalAudit" );
        }
        finally
        {
            FacadeHelper.closeSession( session, AuditFacade.class.getName() + ".restartNormalAudit" );
        }

        return auditDTO;

    }

    /**
     * R�cup�re l'audit de jalon programm� pour l'application d'id <code>pApplicationId</code> si il existe
     * 
     * @param pApplicationId l'id de l'application
     * @return l'audit trouv� ou null sinon
     * @throws JrafEnterpriseException si erreur
     */
    public static AuditDTO getMilestoneAudit( Long pApplicationId )
        throws JrafEnterpriseException
    {
        AuditDTO auditDTO = null;
        ISession session = null; // session hibernate
        try
        {
            session = PERSISTENTPROVIDER.getSession();
            // Initialisation du DAO
            AuditDAOImpl auditDAO = AuditDAOImpl.getInstance();
            AuditBO auditBO = auditDAO.findMilestoneAudit( session, pApplicationId.longValue() );
            if ( null != auditBO )
            {
                auditDTO = AuditTransform.bo2Dto( auditBO, pApplicationId.longValue() );
            }
        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, AuditFacade.class.getName() + ".getMilestoneAudit" );
        }
        finally
        {
            FacadeHelper.closeSession( session, AuditFacade.class.getName() + ".getMilestoneAudit" );
        }
        return auditDTO;
    }

    /**
     * Liste des audits de suivi programm� pour une application.
     * 
     * @param pApplicationId l'id de l'application
     * @param pAuditDTO l'audit de suivi � ins�rer
     * @param pSession la session hibernate
     * @return le DTO de l'audit ins�r� (null en cas d'erreur d'insertion)
     * @throws JrafEnterpriseException si erreur
     */
    public static AuditDTO modifyNextNormalAudit( long pApplicationId, AuditDTO pAuditDTO, ISession pSession )
        throws JrafEnterpriseException
    {
        AuditDTO lAuditDTO = null;
        List lList = new ArrayList();
        try
        {
            AuditDAOImpl lAuditDAO = AuditDAOImpl.getInstance();

            // suppression des audits de suivi programm�s
            lList =
                lAuditDAO.findWhereComponent( pSession, pApplicationId, null, null, AuditBO.NORMAL,
                                              AuditBO.NOT_ATTEMPTED );
            Iterator lAuditIt = lList.iterator();
            while ( lAuditIt.hasNext() )
            {
                AuditBO lAuditBO = (AuditBO) lAuditIt.next();
                lAuditDAO.removeWhereId( pSession, lAuditBO.getId() );
            }
            // ajout de l'audit de suivi
            lAuditDTO = insertNormalAudit( pAuditDTO, pSession );
        }
        catch ( Exception e )
        {
        }
        return lAuditDTO;
    }

    /**
     * Supprime l'audit de jalon d'id <code>removeAudit</code>
     * 
     * @param pAuditId l'id de l'audit
     * @return Integer : 0 pour la r�ussite de la methode sinon 1
     * @throws JrafEnterpriseException si erreur
     */
    public static Integer removeAudit( Long pAuditId )
        throws JrafEnterpriseException
    {
        Integer status = new Integer( 0 );
        ISession session = null; // session hibernate
        try
        {
            session = PERSISTENTPROVIDER.getSession();
            // Initialisation du DAO
            AuditDAOImpl auditDAO = AuditDAOImpl.getInstance();
            auditDAO.removeWhereId( session, pAuditId.longValue() );
        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, AuditFacade.class.getName() + ".removeAudit" );
            status = new Integer( 1 );
        }
        finally
        {
            FacadeHelper.closeSession( session, AuditFacade.class.getName() + ".removeAudit" );
        }
        return status;
    }

    /**
     * Modifie l'audit de jalon d'id <code>removeAudit</code>
     * 
     * @param pAudit l'audit � mettre � jour
     * @return l'audit modifi�
     * @throws JrafEnterpriseException si erreur
     */
    public static AuditDTO modifyAudit( AuditDTO pAudit )
        throws JrafEnterpriseException
    {
        AuditDTO auditDTO = null;
        ISession session = null; // session hibernate
        try
        {
            session = PERSISTENTPROVIDER.getSession();
            // Initialisation du DAO
            AuditDAOImpl auditDAO = AuditDAOImpl.getInstance();
            AuditBO auditBO = AuditTransform.dto2Bo( pAudit );
            auditDAO.save( session, auditBO );
            auditDTO = pAudit;
        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, AuditFacade.class.getName() + ".modifyAudit" );
        }
        finally
        {
            FacadeHelper.closeSession( session, AuditFacade.class.getName() + ".modifyAudit" );
        }
        return auditDTO;
    }

    /**
     * @param pType le type d'audit
     * @param pStatus le status de l'audit
     * @return les audits dont le statut est <code>pStatus</code> de type <code>pType</code>
     * @throws JrafEnterpriseException si erreur
     */
    public static Collection getAudits( String pType, int pStatus )
        throws JrafEnterpriseException
    {
        // Initialisation
        List auditBOs = null; // retour de la DAO
        List auditDTOs = new ArrayList(); // retour de la facade

        ISession session = null;
        try
        {
            // cr�ation d'une session Hibernate
            session = PERSISTENTPROVIDER.getSession();

            AuditDAOImpl auditDAO = AuditDAOImpl.getInstance();
            // Obtention des audits dans la base
            auditBOs = auditDAO.findWhereStatusAndType( session, pType, pStatus );
            // On transforme les audits afin que l'application associ�e soit aussi renseign�e
            auditDTOs = bo2DtoWithApplication( session, auditBOs );
        }
        catch ( JrafDaoException e )
        {
            auditDTOs = null;
            LOG.error( AuditFacade.class.getName() + ".getAudits", e );
        }
        finally
        {
            FacadeHelper.closeSession( session, AuditFacade.class.getName() + ".getAudits" );
        }
        return auditDTOs;
    }

    /**
     * Permet de transformer un ensemble d'audits sous forme BO en DTO en r�cup�rant au pr�alable l'application
     * associ�e.
     * 
     * @param pSession la session
     * @param pAuditBOs les audits sous forme BO
     * @return les audits sous forme DTO avec l'application rensign�e
     * @throws JrafDaoException si erreur
     */
    private static List bo2DtoWithApplication( ISession pSession, Collection pAuditBOs )
        throws JrafDaoException
    {
        List auditDTOs = new ArrayList(); // retour de la facade
        // Parcours de chaque audit et transformation de l'audit
        Iterator iterator = pAuditBOs.iterator();
        AuditBO auditBO = null;
        while ( iterator.hasNext() )
        {
            auditBO = (AuditBO) iterator.next();
            // Recuperation du BO dans la collection + transformation en DTO
            // On r�cup�re l'application associ�e
            ApplicationBO appliBO =
                ApplicationDAOImpl.getInstance().loadByAuditId( pSession, new Long( auditBO.getId() ) );
            // Code d�fensif, ne devrait normalement pas se produire
            if ( appliBO != null )
            {
                AuditDTO auditDTO = AuditTransform.bo2Dto( auditBO, appliBO.getId() );
                auditDTO.setApplicationName( appliBO.getName() );
                // ajout du AuditDTO a la collection de DTOs
                auditDTOs.add( auditDTO );
            }
        }
        return auditDTOs;
    }

    /**
     * Met � jour la date et/ou le status d'un audit
     * 
     * @param pAudits les audits dont la date ou le status est � mettre � jour
     * @return le nombre d'audits mis � jour
     * @throws JrafEnterpriseException exception Jraf
     */
    public static Integer updateAuditsDateOrStatus( Collection pAudits )
        throws JrafEnterpriseException
    {
        // Initialisation
        ISession session = null;
        int nbChanged = 0;
        try
        {
            // cr�ation d'une session Hibernate
            session = PERSISTENTPROVIDER.getSession();
            AuditDAOImpl auditDAO = AuditDAOImpl.getInstance();
            // Parcours de chaque audit et transformation de l'audit
            Iterator iterator = pAudits.iterator();
            AuditDTO auditDTO = null;
            while ( iterator.hasNext() )
            {
                auditDTO = (AuditDTO) iterator.next();
                // On r�cup�re l'audit en base
                AuditBO auditBO = (AuditBO) auditDAO.load( session, auditDTO.getID() );
                // update du bo
                auditBO.setDate( auditDTO.getDate() );
                auditBO.setStatus( auditDTO.getStatus() );
                auditDAO.save( session, auditBO );
                nbChanged++;
            }
        }
        catch ( JrafDaoException e )
        {
            nbChanged = 0;
            LOG.error( AuditFacade.class.getName() + ".updateAuditsDateOrStatus", e );
        }
        finally
        {
            FacadeHelper.closeSession( session, AuditFacade.class.getName() + ".updateAuditsDateOrStatus" );
        }
        return new Integer( nbChanged );
    }

    /**
     * @param pComponent le composant
     * @param pNbLignes le nombre de lignes remont�es
     * @param pIndexDepart l'index de d�part
     * @return les audits partiels
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public static Collection getPartialAudits( ComponentDTO pComponent, Integer pNbLignes, Integer pIndexDepart )
        throws JrafEnterpriseException
    {
        return getAudits( pComponent, pNbLignes, pIndexDepart, null, AuditBO.PARTIAL );
    }

    /**
     * @param pAuditId l'id de l'audit qu'on veut r�cup�rer
     * @return l'auditDTO
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public static AuditDTO getById( Long pAuditId )
        throws JrafEnterpriseException
    {
        ISession session = null;
        AuditDTO auditDTO = null;
        try
        {
            session = PERSISTENTPROVIDER.getSession();
            AuditDAOImpl auditDAO = AuditDAOImpl.getInstance();
            // Chargement de l'objet AuditBO + transformation
            AuditBO auditBO = (AuditBO) auditDAO.load( session, pAuditId );
            auditDTO = AuditTransform.bo2Dto( auditBO );
        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, AuditFacade.class.getName() + ".getById" );
        }
        finally
        {
            FacadeHelper.closeSession( session, AuditFacade.class.getName() + ".getById" );
        }
        return auditDTO;
    }

    /**
     * @return le dernier audit termin� de chaque application
     * @throws JrafEnterpriseException si erreur
     */
    public static Collection getLastTerminatedAudits()
        throws JrafEnterpriseException
    {
        return getLastAudits( ApplicationBO.class, AuditBO.ALL_TYPES, AuditBO.TERMINATED );
    }

    /**
     * @param pComponentType la classe des composants concern�s
     * @param pType le type des audits
     * @param pStatus le status des audits
     * @return les derniers audits des composants de type <pComponentType</code> de type <code>pType</code> dont le
     *         status est <code>pStatus</code>
     * @throws JrafEnterpriseException si erreur
     */
    private static Collection getLastAudits( Class pComponentType, String pType, int pStatus )
        throws JrafEnterpriseException
    {
        // indice du tableau pour le nom du serveur de l'application
        final int SERVER_NAME_ID = 3;
        // Initialisation
        ISession session = null;
        Collection audits = new ArrayList();
        try
        {
            // cr�ation d'une session Hibernate
            session = PERSISTENTPROVIDER.getSession();
            AuditDAOImpl auditDAO = AuditDAOImpl.getInstance();
            // On r�cup�re les derniers audits selon les param�tres de la m�thode
            Collection lastAudits = auditDAO.findAllLastAudits( session, pComponentType, pType, pStatus );
            // Parcours de chaque audit et transformation de l'audit
            Iterator iterator = lastAudits.iterator();
            Object[] tab;
            while ( iterator.hasNext() )
            {
                // On r�cup�re le tableau � trois �l�ment du type {applicationId, applicationName, auditBO}
                tab = (Object[]) iterator.next();
                AuditDTO auditDTO = AuditTransform.bo2Dto( (AuditBO) tab[2], ( (Long) tab[0] ).longValue() );
                auditDTO.setApplicationName( (String) tab[1] );
                if ( tab.length > SERVER_NAME_ID )
                {
                    auditDTO.setServerName( (String) tab[SERVER_NAME_ID] );
                }
                audits.add( auditDTO );
            }
        }
        catch ( JrafDaoException e )
        {
            LOG.error( AuditFacade.class.getName() + ".getLastAudits", e );
        }
        finally
        {
            FacadeHelper.closeSession( session, AuditFacade.class.getName() + ".getLastAudits" );
        }
        return audits;
    }
}
