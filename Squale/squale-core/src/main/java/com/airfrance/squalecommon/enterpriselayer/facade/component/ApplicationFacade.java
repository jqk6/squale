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
package com.airfrance.squalecommon.enterpriselayer.facade.component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.jraf.commons.exception.JrafEnterpriseException;
import com.airfrance.jraf.helper.PersistenceHelper;
import com.airfrance.jraf.provider.persistence.hibernate.facade.FacadeHelper;
import com.airfrance.jraf.spi.enterpriselayer.IFacade;
import com.airfrance.jraf.spi.persistence.IPersistenceProvider;
import com.airfrance.jraf.spi.persistence.ISession;
import com.airfrance.squalecommon.daolayer.component.ApplicationDAOImpl;
import com.airfrance.squalecommon.daolayer.component.AuditDAOImpl;
import com.airfrance.squalecommon.daolayer.component.ProjectDAOImpl;
import com.airfrance.squalecommon.daolayer.profile.ProfileDAOImpl;
import com.airfrance.squalecommon.daolayer.profile.UserDAOImpl;
import com.airfrance.squalecommon.daolayer.result.SqualeReferenceDAOImpl;
import com.airfrance.squalecommon.datatransfertobject.component.ApplicationConfDTO;
import com.airfrance.squalecommon.datatransfertobject.component.AuditDTO;
import com.airfrance.squalecommon.datatransfertobject.component.ComponentDTO;
import com.airfrance.squalecommon.datatransfertobject.component.UserDTO;
import com.airfrance.squalecommon.datatransfertobject.transform.component.ApplicationConfTransform;
import com.airfrance.squalecommon.datatransfertobject.transform.component.ComponentTransform;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.access.UserAccessBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.profile.ProfileBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.profile.UserBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.SqualeReferenceBO;
import com.airfrance.squalecommon.enterpriselayer.facade.FacadeMessages;
import com.airfrance.squalecommon.util.messages.CommonMessages;

/**
 * Fa�ade responsable de la gestion d'une application :<br /> - suppression, <br /> - tests d'existence, <br /> -
 * r�cup�ration, <br /> - insertion, mise � jour, ...<br />
 * d'une application.
 * 
 * @author ABOZ
 */
public class ApplicationFacade
    implements IFacade
{

    /**
     * provider de persistence
     */
    private static final IPersistenceProvider PERSISTENTPROVIDER = PersistenceHelper.getPersistenceProvider();

    /** log */
    private static Log LOG = LogFactory.getLog( ApplicationFacade.class );

    /**
     * D�finit l'heure � laquelle est programm� le premier audit
     */
    public static final int HOUR_OF_AUDIT = 24;

    /**
     * D�finit les minutes � laquelle est programm� le premier audit
     */
    public static final int MINUTE_OF_AUDIT = 00;

    /**
     * Permet de r�cup�rer un objet ApplicationConfDTO � partir d'un objet presentant au moins l'ID de l'application.
     * 
     * @use by ApplicationAdministratorComponent
     * @param pApplicationConf ApplicationConfDTO contenant l'ID de l'application concern�e
     * @return ApplicationConfDTO
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB101B9
     */
    public static ApplicationConfDTO getApplicationConf( ApplicationConfDTO pApplicationConf )
        throws JrafEnterpriseException
    {

        // Initialisation
        ApplicationConfDTO applicationConfDTO = null; // retour de la methode
        Collection userBOs = null; // collection de UserBOs appartenant a l'application

        // Initialisation des variables temporaires
        ApplicationBO applicationBO = null;
        Long applicationID = new Long( pApplicationConf.getId() );

        ISession session = null;

        try
        {

            session = PERSISTENTPROVIDER.getSession();

            ApplicationDAOImpl applicationDAO = ApplicationDAOImpl.getInstance();

            applicationBO = (ApplicationBO) applicationDAO.get( session, applicationID );

            UserDAOImpl userDAO = UserDAOImpl.getInstance();

            if ( applicationBO != null )
            {

                userBOs = userDAO.findWhereApplication( session, applicationBO );
                applicationConfDTO = ApplicationConfTransform.bo2Dto( applicationBO, userBOs );

            }

        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, ApplicationFacade.class.getName() + ".getApplicationConf" );
        }
        finally
        {
            FacadeHelper.closeSession( session, ApplicationFacade.class.getName() + ".getApplicationConf" );
        }

        return applicationConfDTO;
    }

    /**
     * Permet de v�rifier l'existence d'une application � partir de son nom
     * 
     * @use by ApplicationAdministratorComponent
     * @param pApplication ComponentDTO renseignant le nom d'une application
     * @return <code>true</code> si l'application existe, sinon <code>false</code>
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB101C3
     */
    public static boolean exists( ComponentDTO pApplication )
        throws JrafEnterpriseException
    {

        // Nombre d'applications portant le meme nom
        int numberApplication = 0;
        boolean existsApplication = false;

        ISession session = null;

        try
        {

            session = PERSISTENTPROVIDER.getSession();

            ApplicationDAOImpl applicationDAO = ApplicationDAOImpl.getInstance();

            numberApplication = applicationDAO.countWhereName( session, pApplication.getName() );

        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, ApplicationFacade.class.getName() + ".exists" );
        }
        finally
        {
            FacadeHelper.closeSession( session, ApplicationFacade.class.getName() + ".exists" );
        }

        if ( numberApplication > 0 )
        {
            existsApplication = true;
        }
        else
        {
            existsApplication = false;
        }

        return existsApplication;
    }

    /**
     * - Transforme les DTO en objets m�tiers via des transformeurs.
     * 
     * @dev-squale Demande � la partie DAO de sauvegarder / mettre � jour les objets m�tiers : <br /> - Un ApplicationBO<br /> -
     *             Des ProjectBO<br /> - Des UserBO<br /> - 0 ou 1 AuditBO<br /> - Des RightsBO<br />
     * @use by ApplicationAdministratorComponent
     * @param pApplicationConf configuration de l'application courante
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB101C5
     */
    public static void update( ApplicationConfDTO pApplicationConf )
        throws JrafEnterpriseException
    {
        update( pApplicationConf, null );
    }

    /**
     * Transforme les DTO en objets m�tiers via des transformeurs. Demande � la partie DAO de sauvegarder / mettre �
     * jour les objets m�tiers :<br /> - Un ApplicationBO<br /> - Des ProjectBO<br /> - Des UserBO<br /> - 0 ou 1
     * AuditBO<br /> - Des RightsBO<br />
     * 
     * @param pApplicationConf configuration de l'application courante
     * @param pSession session JRAF
     * @throws JrafEnterpriseException exception JRAF
     */
    public static void update( ApplicationConfDTO pApplicationConf, ISession pSession )
        throws JrafEnterpriseException
    {
        // Initialisation
        ApplicationBO applicationBO = null; // retour du load pour l'application
        UserBO userBO = null; // retour du load pour l'utilisateur
        Long applicationID = new Long( pApplicationConf.getId() );
        // identifiant de l'application
        ProfileBO profileBO = null; // profil des utilisateurs a ajouter

        try
        {
            // Cr�ation d'une session locale si besoin
            if ( pSession == null )
            {
                // CHECKSTYLE:OFF
                pSession = PERSISTENTPROVIDER.getSession();
                // CHECKSTYLE:ON
            }
            // Initialisation des DAO
            ApplicationDAOImpl applicationDAO = ApplicationDAOImpl.getInstance();
            UserDAOImpl userDAO = UserDAOImpl.getInstance();
            ProfileDAOImpl profileDAO = ProfileDAOImpl.getInstance();

            // On r�cup�re l'application en base
            applicationBO = (ApplicationBO) applicationDAO.get( pSession, applicationID );

            // Si celle-ci existe (code d�fensif)
            if ( applicationBO != null )
            {
                // Variables temporaires de sauvegarde des donn�es en base avant transformation
                int previousFrequency = applicationBO.getAuditFrequency();
                String user = applicationBO.getLastUser();
                Date date = applicationBO.getLastUpdate();
                // On va sauvegarder la transformation seulement si il y a eu des modifications
                boolean toUpdate = applicationToUpdate( pApplicationConf, applicationBO );
                // Transformation du DTO en BO
                ApplicationConfTransform.dto2Bo( pApplicationConf, applicationBO );
                // On garde les anciennes valeurs qui seront updat�es seulement si on sauvegarde
                // l'application
                applicationBO.setLastUpdate( date );
                applicationBO.setLastUser( user );

                // Modification du type d'audit si besoin
                // Seulement dans le cas d'une application dont la fr�quence implique
                // le changement du type d'Audit (de jalon ou de suivi)
                if ( previousFrequency != applicationBO.getAuditFrequency() )
                {
                    updateAudit( pSession, applicationBO, previousFrequency );
                }
                // On modifie les droits sur l'application
                toUpdate |= updateApplicationRights( pSession, pApplicationConf, applicationBO );
                // On sauvegarde l'application si la conf ou les droits ont chang�
                if ( toUpdate )
                {
                    // On modifie l'application, il faut mettre � jour les informations
                    // sur les derni�res modifications
                    applicationBO.setLastUpdate( pApplicationConf.getLastUpdate() );
                    applicationBO.setLastUser( pApplicationConf.getLastUser() );
                    applicationDAO.save( pSession, applicationBO );
                }
            }
        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, ApplicationFacade.class.getName() + ".update" );
        }
        finally
        {
            FacadeHelper.closeSession( pSession, ApplicationFacade.class.getName() + ".update" );
        }
    }

    /**
     * @param pDTO l'application � comparer
     * @param pBO l'application en base
     * @return true si l'objet en base � chang�
     */
    private static boolean applicationToUpdate( ApplicationConfDTO pDTO, ApplicationBO pBO )
    {
        boolean toUpdate = !pDTO.getName().equals( pBO.getName() );
        if ( null != pBO.getServeurBO() )
        {
            toUpdate |= pDTO.getServeurDTO().getServeurId() != pBO.getServeurBO().getServeurId();
        }
        toUpdate |= pDTO.getPublic() != pBO.getPublic();
        toUpdate |= pDTO.getAuditFrequency() != pBO.getAuditFrequency();
        toUpdate |= pDTO.getResultsStorageOptions() != pBO.getResultsStorageOptions();
        toUpdate |= pDTO.getInProduction() != pBO.getInProduction();
        toUpdate |= pDTO.getExternalDev() != pBO.getExternalDev();
        return toUpdate;
    }

    /**
     * @param pSession la session hibernate
     * @param pApplicationConf l'application modifi�e
     * @param pAppliBO l'application � modifier
     * @return true si les modifications doivent �tre sauvegard�es
     * @throws JrafDaoException si erreur
     */
    private static boolean updateApplicationRights( ISession pSession, ApplicationConfDTO pApplicationConf,
                                                    ApplicationBO pAppliBO )
        throws JrafDaoException
    {
        // Initialisation
        UserBO userBO = null; // retour du load pour l'utilisateur
        ProfileBO profileBO = null; // profil des utilisateurs a ajouter
        // Les DAO
        UserDAOImpl userDAO = UserDAOImpl.getInstance();
        ProfileDAOImpl profileDAO = ProfileDAOImpl.getInstance();
        // Retour de la m�thode
        boolean usersChanged = false;

        // On it�re sur les nouveaux profils
        Iterator usersIterator = pApplicationConf.getUsers().keySet().iterator();
        while ( usersIterator.hasNext() )
        {
            String userName = (String) usersIterator.next();
            // r�cup�ration du userBO associ� au matricule
            userBO = (UserBO) userDAO.loadWithMatricule( pSession, userName );
            // Initialisation du profil de lecteur par defaut
            profileBO = profileDAO.loadByKey( pSession, (String) pApplicationConf.getUsers().get( userName ) );
            // Si l'utilisateur n'existe pas, on le cr�e en base
            // Sinon on change la map du userBO et on sauvegarde en base
            if ( userBO == null )
            {
                // Initialisation du matricule et du profil par defaut
                userBO = new UserBO();
                userBO.setDefaultProfile( profileBO );
                userBO.setMatricule( userName );
                // Attribution sur les droits
                Map applicationsRights = new HashMap();
                applicationsRights.put( pAppliBO, profileBO );
                userBO.setRights( applicationsRights );
                // On cr�e l'objet en base
                userDAO.create( pSession, userBO );
                usersChanged |= true;
            }
            else
            {
                // Remplacement ou affectation d'un nouveau profil sur
                // l'application concern�e
                ProfileBO curProfil = (ProfileBO) userBO.getRights().get( pAppliBO );
                if ( curProfil == null || !curProfil.equals( profileBO ) )
                {
                    userBO.getRights().put( pAppliBO, profileBO );
                    userDAO.save( pSession, userBO );
                    usersChanged = true;
                }
            }
        }
        // Purge des utilisateurs qui ne doivent plus �tre d�finis sur
        // l'application
        usersChanged |= purgeUsers( pSession, pAppliBO, pApplicationConf.getUsers().keySet() );
        return usersChanged;
    }

    /**
     * Purge des utilisateurs
     * 
     * @param pSession session
     * @param pApplicationBO application
     * @param pUserDefined utilisateurs d�finis
     * @return true si des droits ont �t� supprim�s
     * @throws JrafDaoException si erreur
     */
    static private boolean purgeUsers( ISession pSession, ApplicationBO pApplicationBO, Collection pUserDefined )
        throws JrafDaoException
    {
        boolean deleted = false;
        UserDAOImpl userDAO = UserDAOImpl.getInstance();
        Collection userBOs = userDAO.findWhereApplication( pSession, pApplicationBO );
        Iterator existingUsers = userBOs.iterator();
        while ( existingUsers.hasNext() )
        {
            UserBO userBO = (UserBO) existingUsers.next();
            if ( !pUserDefined.contains( userBO.getMatricule() ) )
            {
                userBO.getRights().remove( pApplicationBO );
                userDAO.save( pSession, userBO );
                deleted = true;
            }
        }
        return deleted;
    }

    /**
     * Mise � jour des audits sur une application Lorsqu'une application est modifi�e, les audits qui y sont associ�s
     * peuvent passer d'un audit de type normal � un audit de type jalon. Dans ce cas, il faut supprimer l'audit normal
     * s'il en existe un. Dans le cas inverse, il faut cr�er un audit normal et le rattacher � l'application.
     * 
     * @param pSession session
     * @param pApplicationBO application
     * @param pPreviousFrequency l'ancienne fr�quence
     * @throws JrafEnterpriseException si erreur
     * @throws JrafDaoException si erreur
     */
    private static void updateAudit( ISession pSession, ApplicationBO pApplicationBO, int pPreviousFrequency )
        throws JrafEnterpriseException, JrafDaoException
    {
        AuditDAOImpl auditDAO = AuditDAOImpl.getInstance();
        // Modification du type d'audit
        if ( pPreviousFrequency == -1 && pApplicationBO.getStatus() == ApplicationBO.VALIDATED )
        {
            // Cr�ation d'un audit de suivi seulement si l'application est valid�e
            AuditBO newAudit = createNewAudit();
            auditDAO.create( pSession, newAudit );
            pApplicationBO.addAudit( newAudit );
        }
        else
        {
            // Suppression ou modification des audits de suivi
            ComponentDTO applicationDTO = new ComponentDTO();
            applicationDTO.setID( pApplicationBO.getId() );
            List audits = AuditFacade.getAudits( applicationDTO, null, null, AuditBO.NORMAL, AuditBO.NOT_ATTEMPTED );
            Iterator iterator = audits.iterator();
            while ( iterator.hasNext() )
            {
                AuditDTO auditDTO = (AuditDTO) iterator.next();
                if ( pApplicationBO.getAuditFrequency() == -1 )
                {
                    // Le type des audits a chang�, on supprime
                    AuditFacade.delete( auditDTO, pSession );
                }
                else
                { // La fr�quence a chang� sans changer le type des audits
                    // On ajoute la diff�rence entre les fr�quences
                    Date auditDate = auditDTO.getDate();
                    Calendar newDate = GregorianCalendar.getInstance();
                    newDate.setTime( auditDate );
                    newDate.add( Calendar.DATE, pApplicationBO.getAuditFrequency() - pPreviousFrequency );
                    auditDTO.setDate( newDate.getTime() );
                    // On update l'audit
                    AuditFacade.modifyAudit( auditDTO );
                }
            }
        }
    }

    /**
     * Permet de cr�er en base une application (le premier audit sera cr�� � la validation) et v�rifier si une
     * application portant le m�me nom n'existe pas d�j�
     * 
     * @dev-squale Ajouter l'utilisateur cr�ant au applicationConfDTO
     * @use by ApplicationAdministratorComponent
     * @param pApplicationConf application existante
     * @param pUserCreating utilisateur qui cr�e l'application
     * @return ApplicationConfDTO avec le nom et l'ID de l'application en cours de cr�ation
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB101D9
     */
    public static ApplicationConfDTO insert( ApplicationConfDTO pApplicationConf, UserDTO pUserCreating )
        throws JrafEnterpriseException
    {
        return insert( pApplicationConf, pUserCreating, null );
    }

    /**
     * Permet de cr�er en base une application (le premier audit sera cr�� � la validation) et v�rifier si une
     * application portant le m�me nom n'existe pas d�j�
     * 
     * @dev-squale Ajouter l'utilisateur cr�ant au applicationConfDTO
     * @use by ApplicationAdministratorComponent
     * @param pApplicationConf application existant
     * @param pSession session JRAF
     * @param pUserCreating utilisateur gestionnaire
     * @return ApplicationConfDTO avec le nom et l'ID de l'application en cours de cr�ation
     * @except une exception si application d�j� existante ou nom d'application invalide
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB101E1
     */
    public static ApplicationConfDTO insert( ApplicationConfDTO pApplicationConf, UserDTO pUserCreating,
                                             ISession pSession )
        throws JrafEnterpriseException
    {

        // Initialisation
        Long userID = new Long( pUserCreating.getID() );
        // identifiant de l'utilisateur
        UserBO userBO = null; // retour de UserDAO
        ProfileBO profileBO = null; // retour de ProfileDAO

        // Initialisation des variables temporaires
        ApplicationBO applicationBO = null;
        ApplicationConfDTO newApplicationConf = pApplicationConf;

        try
        {

            if ( pSession == null )
            {
                // CHECKSTYLE:OFF
                pSession = PERSISTENTPROVIDER.getSession();
                // CHECKSTYLE:ON
            }

            ApplicationDAOImpl applicationDAO = ApplicationDAOImpl.getInstance();
            UserDAOImpl userDAO = UserDAOImpl.getInstance();
            ProfileDAOImpl profileDAO = ProfileDAOImpl.getInstance();

            // Transformation du DTO en BO + creation de l'objet en base
            applicationBO = ApplicationConfTransform.dto2Bo( newApplicationConf );
            applicationBO = applicationDAO.create( pSession, applicationBO );

            userBO = (UserBO) userDAO.get( pSession, userID );

            /*
             * Chargement du UserBO et ProfileBO correspondant au gestionnaire d'application si une application du meme
             * nom n'existe pas
             */
            if ( applicationBO != null && userBO != null )
            {

                profileBO = (ProfileBO) profileDAO.loadByKey( pSession, ProfileBO.MANAGER_PROFILE_NAME );

                // Ajout d'une application dans la Map rights et update du UserBO charg�
                userBO.getRights().put( applicationBO, profileBO );
                userDAO.save( pSession, userBO );

                Collection userBOs = new ArrayList();
                userBOs.add( userBO );

                newApplicationConf = ApplicationConfTransform.bo2Dto( applicationBO, userBOs );
            }
            else
            {
                newApplicationConf = null;
            }

        }
        catch ( JrafDaoException e )
        {
            newApplicationConf = null;
            if ( applicationBO != null )
            {
                LOG.info( FacadeMessages.getString( "facade.exception.applicationfacade.insert.existence" ), e );
            }
        }
        finally
        {
            FacadeHelper.closeSession( pSession, ApplicationFacade.class.getName() + ".insert" );
        }
        return newApplicationConf;
    }

    /**
     * permet de r�cup�rer l'objet ApplicationDTO par un ID
     * 
     * @use by ResultsComponent
     * @param pApplication ApplicationDTO renseignant l'id de l'application concern�e
     * @return ApplicationDTO
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB101EC
     */
    public static ComponentDTO getApplication( ComponentDTO pApplication )
        throws JrafEnterpriseException
    {

        // Initialisation du retour
        ComponentDTO applicationDTO = null;

        // Initialisation des variables temporaires
        ApplicationBO applicationBO = null;
        Long applicationID = new Long( pApplication.getID() );

        ISession session = null;

        try
        {

            session = PERSISTENTPROVIDER.getSession();

            ApplicationDAOImpl applicationDAO = ApplicationDAOImpl.getInstance();

            applicationBO = (ApplicationBO) applicationDAO.get( session, applicationID );

            if ( applicationBO != null )
            {
                applicationDTO = ComponentTransform.bo2Dto( applicationBO );
            }
            else
            {
                LOG.error( FacadeMessages.getString( "facade.exception.applicationfacade.getApplication.applicationnull" ) );
            }

        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, ApplicationFacade.class.getName() + ".getApplication" );
        }
        finally
        {
            FacadeHelper.closeSession( session, ApplicationFacade.class.getName() + ".getApplication" );
        }

        return applicationDTO;
    }

    /**
     * Permet d'effacer une liste d'applications
     * 
     * @param pApplicationDTOs liste des ApplicationDTOs renseignant l'identifiant qui sont � supprimer
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB101F4
     */
    public static void deleteApplicationConfList( Collection pApplicationDTOs )
        throws JrafEnterpriseException
    {
        deleteApplicationConfList( pApplicationDTOs, null );
    }

    /**
     * Permet d'effacer une liste d'applications
     * 
     * @param pApplicationDTOs liste de ApplicationDTOs renseignant l'identifiant qui sont � supprimer
     * @param pSession session JRAF
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB101F6
     */
    public static void deleteApplicationConfList( Collection pApplicationDTOs, ISession pSession )
        throws JrafEnterpriseException
    {

        try
        {
            if ( pSession == null )
            {
                // si aucune session, on en r�cup�re une
                // CHECKSTYLE:OFF
                pSession = PERSISTENTPROVIDER.getSession();
                // CHECKSTYLE:ON
            }
            // R�cup�ration d'une instance de ApplicationDAOImpl
            ApplicationDAOImpl applicationDAO = ApplicationDAOImpl.getInstance();

            Iterator applicationIterator = pApplicationDTOs.iterator();
            ApplicationBO applicationTemp = null;

            // Suppression de la liste de ProjectConfBO
            while ( applicationIterator.hasNext() )
            {
                Long applicationId = new Long( ( (ApplicationConfDTO) applicationIterator.next() ).getId() );
                // r�cup�ration de l'application p/r � l'id
                applicationTemp = (ApplicationBO) applicationDAO.get( pSession, applicationId );
                // suppression de l'application
                applicationDAO.remove( pSession, applicationTemp );
            }

        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, ApplicationFacade.class.getName() + ".deleteApplicationConfList" );
        }
        finally
        {
            FacadeHelper.closeSession( pSession, ApplicationFacade.class.getName() + ".deleteApplicationConfList" );
        }
    }

    /**
     * Permet de valider une liste de ApplicationConf
     * 
     * @use by ValidationComponent
     * @param pApplicationIDs liste des ids des applications � valider
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB10209
     */
    public static void validateApplicationConfList( Collection pApplicationIDs )
        throws JrafEnterpriseException
    {
        validateApplicationConfList( pApplicationIDs, null );
    }

    /**
     * Permet de valider une liste de ApplicationConf
     * 
     * @use by ValidationComponent
     * @param pApplicationConfDTOs liste des des applications � valider
     * @param pSession session JRAF
     * @return l'ensemble des applications qui n'ont pas pu �tre valid�es
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB10212
     */
    public static List validateApplicationConfList( Collection pApplicationConfDTOs, ISession pSession )
        throws JrafEnterpriseException
    {

        // Initialisation
        AuditBO newAudit = null; // Nouvel audit a ajouter
        List notValidated = new ArrayList();

        try
        {
            if ( pSession == null )
            {
                // CHECKSTYLE:OFF
                pSession = PERSISTENTPROVIDER.getSession();
                // CHECKSTYLE:ON
            }

            // Initialisation des DAO
            ApplicationDAOImpl applicationDAO = ApplicationDAOImpl.getInstance();
            AuditDAOImpl auditDAO = AuditDAOImpl.getInstance();

            Iterator iterator = pApplicationConfDTOs.iterator();
            Long applicationIDTemp = null;
            ApplicationBO applicationToValidate = null;

            while ( iterator.hasNext() )
            {

                applicationIDTemp = new Long( ( (ApplicationConfDTO) iterator.next() ).getId() );

                // Recuperation de applicationBO
                applicationToValidate = (ApplicationBO) applicationDAO.get( pSession, applicationIDTemp );

                // On v�rifie qu'il n'y a pas d'application du m�me nom en base
                // (possible lorsque les temps de r�ponse sont trop longs lorsqu'on cr�e une application)
                int existantApplication = applicationDAO.countWhereName( pSession, applicationToValidate );
                if ( existantApplication > 1 )
                {
                    // On ajoute son nom � l'ensemble des applications qui n'ont pas pu �tre
                    // valid�e car une autre du m�me nom existe en base
                    notValidated.add( applicationToValidate.getName() );
                }
                else
                {

                    // Creation d'un nouvel audit pour le lendemain minuit si un audit de suivi est souhait�
                    // et si aucun audit de suivi n'a d�j� �t� programm� (cas d'un audit de test d'une application non
                    // valid�e)
                    List lList =
                        auditDAO.findWhereComponent( pSession, applicationIDTemp.longValue(), null, null,
                                                     AuditBO.NORMAL, AuditBO.NOT_ATTEMPTED );
                    if ( applicationToValidate.getAuditFrequency() > 0 && lList.isEmpty() )
                    {
                        newAudit = createNewAudit();
                        auditDAO.create( pSession, newAudit );
                    }

                    // Changement du status en validated et Ajout de l'audit nouvellement cr�� � l'application
                    applicationToValidate.setStatus( ApplicationBO.VALIDATED );
                    if ( newAudit != null )
                    {
                        applicationToValidate.addAudit( newAudit );
                    }

                    // Sauvegarde de l'objet
                    applicationDAO.save( pSession, applicationToValidate );
                }
            }

        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, ApplicationFacade.class.getName() + ".validateApplicationConfList" );
        }
        finally
        {
            FacadeHelper.closeSession( pSession, ApplicationFacade.class.getName() + ".validateApplicationConfList" );
        }
        return notValidated;
    }

    /**
     * Permet de retourner une collection de ApplicationConfDTO � valider par l'administrateur du portail
     * 
     * @return Collection de ApplicationConfDTO applications � valider
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB10215
     */
    public static Collection getApplicationConfList()
        throws JrafEnterpriseException
    {

        // Initialisation des variables
        Collection applicationBOs = null;
        Collection applicationConfDTOs = new ArrayList();
        Collection userBOs = null; // collection de UserBOs appartenant a l'application

        ISession session = null;

        try
        {
            // r�cup�ration d'une session
            session = PERSISTENTPROVIDER.getSession();

            ApplicationDAOImpl applicationDAO = ApplicationDAOImpl.getInstance();

            // Recuperation des applications non valid�es
            applicationBOs = applicationDAO.findWhereStatus( session, ApplicationBO.IN_CREATION );

            Iterator iterator = applicationBOs.iterator();
            ApplicationBO applicationTemp = null;

            // Transformation des BOs en DTOs
            while ( iterator.hasNext() )
            {
                // Pour chaque application
                applicationTemp = (ApplicationBO) iterator.next();

                if ( applicationTemp != null )
                {
                    // r�cup�ration de la liste d'utilisateur de cette application
                    userBOs = UserDAOImpl.getInstance().findWhereApplication( session, applicationTemp );
                    // Cr�ation d'un ApplicationDTO � partir du ApplicationBO et de la liste de UserBO
                    applicationConfDTOs.add( ApplicationConfTransform.bo2Dto( applicationTemp, userBOs ) );
                }
            }

        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, ApplicationFacade.class.getName() + ".getApplicationConfList" );
        }
        finally
        {
            FacadeHelper.closeSession( session, ApplicationFacade.class.getName() + ".getApplicationConfList" );
        }

        return applicationConfDTOs;
    }

    /**
     * Permet de supprimer une application et toutes ses relations (audits et r�sultats)
     * 
     * @dev-squale s'il n'y a pas de jalon existant, ins�rer le dernier audit dans le r�f�rentiel.
     * @use by PurgeComponent.purgeApplication()
     * @param pApplicationConf ApplicationConfDTO renseignant l'ID de l'application
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB1021E
     */
    public static void deleteAll( ApplicationConfDTO pApplicationConf )
        throws JrafEnterpriseException
    {
        deleteAll( pApplicationConf, null );
    }

    /**
     * Permet de v�rifier si une application poss�de un milestone.
     * 
     * @use by PurgeComponent.purgeApplication()
     * @param pApplicationConf ApplicationConfDTO renseignant l'ID de l'application
     * @return <code>true</code> si l'application poss�de un milestone sinon <code>false</code>
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB10220
     */
    public static boolean existsMilestone( ApplicationConfDTO pApplicationConf )
        throws JrafEnterpriseException
    {
        return existsMilestone( pApplicationConf, null );
    }

    /**
     * Permet de supprimer une application et toutes ses relations (audits et r�sultats)
     * 
     * @dev-squale s'il n'y a pas de jalon existant, ins�rer le dernier audit dans le r�f�rentiel.
     * @use by PurgeComponent.purgeApplication()
     * @param pApplicationConf ApplicationConfDTO renseignant l'ID de l'application
     * @param pSession session Hibernate
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB10227
     */
    public static void deleteAll( ApplicationConfDTO pApplicationConf, ISession pSession )
        throws JrafEnterpriseException
    {

        // Initialisation
        Long applicationID = new Long( pApplicationConf.getId() ); // identifiant application
        ApplicationBO applicationBO = null; // application a supprimer

        try
        {

            if ( pSession == null )
            {
                // CHECKSTYLE:OFF
                pSession = PERSISTENTPROVIDER.getSession();
                // CHECKSTYLE:ON
            }

            ApplicationDAOImpl applicationDAO = ApplicationDAOImpl.getInstance();

            
            // on acc�de au projectBO pour purger les projets li�s � cette application
            ProjectDAOImpl projectDAO = ProjectDAOImpl.getInstance();
            
            Iterator it = projectDAO.findAllProjects( pSession, applicationID ).iterator();
            
            while ( it.hasNext() )
            {
                ProjectBO pBO = (ProjectBO) it.next();
                projectDAO.setStatusDelete( pSession, pBO );
            }
            
            // Chargement de l'application associ�e � l'identifiant application
            // Suppression du ApplicationBO associ� et toutes ses relations
            applicationBO = (ApplicationBO) applicationDAO.get( pSession, applicationID );
            applicationDAO.remove( pSession, applicationBO );

        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, ApplicationFacade.class.getName() + ".deleteAll" );
        }
        finally
        {
            FacadeHelper.closeSession( pSession, ApplicationFacade.class.getName() + ".deleteAll" );
        }

    }

    /**
     * Permet de v�rifier si une application poss�de un milestone.
     * 
     * @use by PurgeComponent.purgeApplication()
     * @param pApplicationConf ApplicationConfDTO renseignant l'ID d'une application
     * @param pSession session Hibernate
     * @return <code>true</code> si l'application poss�de un milestone sinon <code>false</code>
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42CBFFB10230
     */
    public static boolean existsMilestone( ApplicationConfDTO pApplicationConf, ISession pSession )
        throws JrafEnterpriseException
    {

        // Initialisation du retour et objets temporaires
        boolean existsMilestone = true; // retour
        ApplicationBO applicationBO = null; // objet metier utilise
        int nbMilestones = -1; // nombre de milestone retourne par la DAO

        try
        {

            if ( pSession == null )
            {
                // si aucune session n'est pass�e
                // CHECKSTYLE:OFF
                pSession = PERSISTENTPROVIDER.getSession();
                // CHECKSTYLE:ON
            }
            // transformation du ApplicationConfDTO en ApplicationBO
            applicationBO = ApplicationConfTransform.dto2Bo( pApplicationConf );

            AuditDAOImpl auditDAO = AuditDAOImpl.getInstance();

            // R�cup�ration du nombre de milestone pour l'application
            nbMilestones = auditDAO.countWhereType( pSession, applicationBO, AuditBO.MILESTONE, AuditBO.TERMINATED );

        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, ApplicationFacade.class.getName() + ".existsMilestone" );
        }
        finally
        {
            FacadeHelper.closeSession( pSession, ApplicationFacade.class.getName() + ".existsMilestone" );
        }

        // si des milestone ont �t� trouv�, on retourne true, on retourne false sinon
        if ( nbMilestones > 0 )
        {
            existsMilestone = true;
        }
        else
        {
            existsMilestone = false;
        }

        return existsMilestone;

    }

    /**
     * Permet d'ajouter un prochain audit � une application donn� apr�s validation de l'application par un
     * administrateur
     * 
     * @return AuditBO un nouvel audit cr�� pour le jour meme a minuit
     * @throws JrafEnterpriseException exception JRAF
     */
    private static AuditBO createNewAudit()
        throws JrafEnterpriseException
    {

        // Initialisation
        AuditBO auditBO = new AuditBO(); // audit � ajouter

        // Date du premier audit initialis� au jour meme � 0:00
        Calendar myDate = GregorianCalendar.getInstance();
        myDate.add( GregorianCalendar.HOUR_OF_DAY, HOUR_OF_AUDIT - myDate.get( GregorianCalendar.HOUR_OF_DAY ) );
        myDate.add( GregorianCalendar.MINUTE, MINUTE_OF_AUDIT - myDate.get( GregorianCalendar.MINUTE ) );
        auditBO.setDate( myDate.getTime() );

        // Ajout du commentaire de premier audit
        auditBO.setComments( CommonMessages.getString( "bo.audit.comments.firstaudit" ) );

        auditBO.setStatus( AuditBO.NOT_ATTEMPTED );

        // name = date en string
        auditBO.setName( myDate.getTime().toString() );

        // type dans le fichier de configuration
        auditBO.setType( AuditBO.NORMAL );

        return auditBO;
    }

    /**
     * Constructeur vide
     * 
     * @roseuid 42CBFFB10233
     */
    private ApplicationFacade()
    {
    }

    /**
     * @param auditId l'id de l'audit (on veut r�cup�rer l'application correspondante)
     * @param pSession la session
     * @return l'application sous la forme d'un componentDTO
     * @throws JrafDaoException en cas d'�checs
     */
    public static ComponentDTO loadByAuditId( Long auditId, ISession pSession )
        throws JrafDaoException
    {
        ApplicationDAOImpl dao = ApplicationDAOImpl.getInstance();
        return ComponentTransform.bo2Dto( dao.loadByAuditId( pSession, auditId ) );
    }

    /**
     * retourne la liste des applications d�finies
     * 
     * @param pSession la session
     * @return la liste des application
     * @throws JrafDaoException en cas d'�checs
     */
    public static Collection listAll( ISession pSession )
        throws JrafDaoException
    {
        ApplicationDAOImpl dao = ApplicationDAOImpl.getInstance();
        Collection collDao = dao.findNotDeleted( pSession );
        Collection collDto = new ArrayList( 0 );
        Iterator it = collDao.iterator();
        while ( it.hasNext() )
        {
            collDto.add( ComponentTransform.bo2Dto( (ApplicationBO) it.next() ) );
        }
        return collDto;
    }

    /**
     * @param pSession la session
     * @param pApplicationId application accessed
     * @param pMatricule le matricule de l'utilisateur
     * @param maxSize le nombre max d'acc�s � sauvegarder
     * @throws JrafDaoException si erreur
     */
    public static void addUserAccess( ISession pSession, Long pApplicationId, String pMatricule, Integer maxSize )
        throws JrafDaoException
    {
        // Initialisation du DAO
        ApplicationDAOImpl appliDAO = ApplicationDAOImpl.getInstance();
        ApplicationBO appliBO = null;
        // On r�cup�re l'application
        appliBO = (ApplicationBO) appliDAO.get( pSession, pApplicationId );
        if ( null != appliBO && appliBO.getStatus() == ApplicationBO.VALIDATED )
        { // code d�fensif
            // On ajoute l'acc�s utilisateur
            UserAccessBO userAccess = new UserAccessBO( pMatricule, Calendar.getInstance().getTime() );
            appliBO.addUserAccess( userAccess, maxSize.intValue() );
            // On sauvegarde l'application
            appliDAO.save( pSession, appliBO );
        }
    }

    /**
     * @param pApplicationId l'id de l'application
     * @param pLastUser le nom du dernier utilisateur ayant modifi� l'application
     * @param pLastUpdate la date de derni�re modification
     * @throws JrafEnterpriseException si erreur
     */
    public static void updateLastModifParams( Long pApplicationId, String pLastUser, Date pLastUpdate )
        throws JrafEnterpriseException
    {
        // Initialisation du DAO
        ApplicationDAOImpl appliDAO = ApplicationDAOImpl.getInstance();
        // La session
        ISession session = null;
        try
        {
            session = PERSISTENTPROVIDER.getSession();
            // On r�cup�rer l'application
            ApplicationBO appliBO = (ApplicationBO) appliDAO.get( session, pApplicationId );
            // On modifie les param�tres concernant les derni�res modifications
            appliBO.setLastUpdate( pLastUpdate );
            appliBO.setLastUser( pLastUser );
            // On sauvegarde en base
            appliDAO.save( session, appliBO );
        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, ApplicationFacade.class.getName() + ".updateLastModifParams" );
        }
        finally
        {
            FacadeHelper.closeSession( session, ApplicationFacade.class.getName() + ".updateLastModifParams" );
        }
    }

    /**
     * Hide an application for not admin users without delete it physically: - remove users - if it's public, becomes
     * private - disactive audits - delete current not attempted audits - set public status in repository
     * 
     * @param pApplicationConf application to hide
     * @param pSession hibernate session
     * @throws JrafEnterpriseException if error
     */
    public static void hideApplication( ApplicationConfDTO pApplicationConf, ISession pSession )
        throws JrafEnterpriseException
    {

        // Initialisation
        ApplicationBO appliBO = null; // application to delete
        Long appliId = new Long( pApplicationConf.getId() ); // application id
        try
        {
            if ( pSession == null )
            {
                // CHECKSTYLE:OFF Assign value to null parameter pSession
                pSession = PERSISTENTPROVIDER.getSession();
                // CHECKSTYLE:ON
            }
            ApplicationDAOImpl applicationDAO = ApplicationDAOImpl.getInstance();
            // Load application from database
            appliBO = (ApplicationBO) applicationDAO.get( pSession, appliId );
            if ( appliBO != null )
            {
                // remove users
                purgeUsers( pSession, appliBO, new ArrayList( 0 ) );
                // set public status to private
                appliBO.setPublic( false );
                // Disactive audits
                appliBO.setAuditFrequency( -1 );
                // save modification
                applicationDAO.save( pSession, appliBO );
                // delete not attempted audits
                AuditDAOImpl auditDAO = AuditDAOImpl.getInstance();
                List auditsToDelete =
                    auditDAO.findWhereComponent( pSession, appliBO.getId(), null, null, AuditBO.ALL_TYPES,
                                                 AuditBO.NOT_ATTEMPTED );
                for ( Iterator it = auditsToDelete.iterator(); it.hasNext(); )
                {
                    auditDAO.remove( pSession, (AuditBO) it.next() );
                }
                // If public audit exists in repository, set to private
                SqualeReferenceDAOImpl refDAO = SqualeReferenceDAOImpl.getInstance();
                Collection refs = refDAO.findReferencesByAppliName( pSession, appliBO.getName() );
                for ( Iterator it = refs.iterator(); it.hasNext(); )
                {
                    SqualeReferenceBO ref = (SqualeReferenceBO) it.next();
                    if ( ref.getPublic() )
                    {
                        ref.setPublic( false );
                        refDAO.save( pSession, ref );
                    }
                }
            }
            else
            {
                LOG.error( FacadeMessages.getString( "facade.exception.applicationfacade.hide.applinull" ) );
            }
        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, ApplicationFacade.class.getName() + ".hideApplication" );
        }
        finally
        {
            FacadeHelper.closeSession( pSession, ApplicationFacade.class.getName() + ".hideApplication" );
        }
    }

}
