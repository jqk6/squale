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
//Source file: D:\\CC_VIEWS\\SQUALE_V0_0_ACT\\SQUALE\\SRC\\squaleCommon\\src\\org\\squale\\squalecommon\\enterpriselayer\\applicationcomponent\\ComponentApplicationComponentAccess.java

package org.squale.squalecommon.enterpriselayer.applicationcomponent.display;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.squale.jraf.commons.exception.JrafEnterpriseException;
import org.squale.jraf.provider.accessdelegate.DefaultExecuteComponent;
import org.squale.squalecommon.datatransfertobject.component.AuditDTO;
import org.squale.squalecommon.datatransfertobject.component.ComponentDTO;
import org.squale.squalecommon.datatransfertobject.component.ProjectConfDTO;
import org.squale.squalecommon.datatransfertobject.result.ResultsDTO;
import org.squale.squalecommon.datatransfertobject.tag.TagDTO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import org.squale.squalecommon.enterpriselayer.facade.component.AuditFacade;
import org.squale.squalecommon.enterpriselayer.facade.component.ComponentFacade;
import org.squale.squalecommon.enterpriselayer.facade.component.ProjectFacade;
import org.squale.squalecommon.enterpriselayer.facade.quality.MeasureFacade;
import org.squale.squalecommon.enterpriselayer.facade.tag.TagFacade;

/**
 * @author m400841 (by Rose) Classe permettant de r�cup�rer les composants (fils, parent) et les audits (dernier ou
 *         particulier)
 */
public class ComponentApplicationComponentAccess
    extends DefaultExecuteComponent
{

    /**
     * Constructeur vide
     * 
     * @roseuid 42CBFBF70095
     */
    public ComponentApplicationComponentAccess()
    {
    }

    /**
     * Permet de recuperer un ComponentDTO avec un identifiant renseign�
     * 
     * @param pComponentDTO renseignant l'identifiant
     * @return ComponentDTO avec toutes les donn�es
     * @throws JrafEnterpriseException exception JRAF
     */
    public ComponentDTO get( ComponentDTO pComponentDTO )
        throws JrafEnterpriseException
    {
        return ComponentFacade.get( pComponentDTO );
    }

    /**
     * @param pAuditId l'id de l'audit
     * @return l'audit d'id <code>pAuditId</code>
     * @throws JrafEnterpriseException si erreur
     */
    public AuditDTO getById( Long pAuditId )
        throws JrafEnterpriseException
    {
        return AuditFacade.getById( pAuditId );
    }

    /**
     * R�cup�re les valeurs permettant d'avoir l'historique relatif a des cles de tre et un composant
     * 
     * @param pComponentDTO un composant
     * @param pTreLabel label de tre
     * @param pTreKey cles de tre concerne par l'historique
     * @param pAuditDate date a partir de laquelle on souhaite recuperer tous les audits
     * @param pRuleId id de la r�gle qualit� ou null s'il s'agit d'une mesure
     * @return La map correspondant aux points
     * @throws JrafEnterpriseException exception Jraf
     */
    public Map getHistoricMap( ComponentDTO pComponentDTO, String pTreKey, String pTreLabel, Date pAuditDate,
                               Long pRuleId )
        throws JrafEnterpriseException
    {
        return MeasureFacade.getHistoricResults( pComponentDTO, pTreKey, pTreLabel, pAuditDate, pRuleId );
    }

    /**
     * Permet de r�cup�rer tous les enfants d'un composant donn� avec la possibilit� de sp�cifier la cl� du type de
     * composant souhait�
     * 
     * @param pComponent composant parent renseignant l'identifiant
     * @param pType cl� du type de composant, sinon <code>null</code> pour tous les composants
     * @param pAudit audit du composant associ�, si <code>null</code>, on utilise le dernier
     * @param pFilter le filtre sur les noms des enfants
     * @return Collection de ComponentDTO
     * @throws JrafEnterpriseException exception JRAF
     */
    public Collection getChildren( ComponentDTO pComponent, String pType, AuditDTO pAudit, String pFilter )
        throws JrafEnterpriseException
    {

        // Initialisation de la facade
        Collection collection = new ArrayList( 0 );

        if ( pAudit != null )
        {
            collection = ComponentFacade.getChildren( pComponent, pType, pAudit, pFilter );
        }
        else
        {
            AuditDTO lastAudit = AuditFacade.getLastAudit( pComponent, null );
            collection = ComponentFacade.getChildren( pComponent, pType, lastAudit, pFilter );
        }
        return collection;
    }

    /**
     * @param pComponent le composant
     * @return les enfants du composant
     * @throws JrafEnterpriseException si erreur
     */
    public Collection getChildren( ComponentDTO pComponent )
        throws JrafEnterpriseException
    {
        return ComponentFacade.getChildren( pComponent, null, null, null );
    }

    /**
     * Permet de compter tous les enfants d'un composant donn� avec la possibilit� de sp�cifier la cl� du type de
     * composant souhait�
     * 
     * @param pComponent composant parent renseignant l'identifiant
     * @param pType cl� du type de composant, sinon <code>null</code> pour tous les composants
     * @param pAudit audit du composant associ�, si <code>null</code>, on utilise le dernier
     * @param pFilter le filtre sur les noms des enfants
     * @return le nombre d'enfants
     * @throws JrafEnterpriseException exception JRAF
     */
    public Integer countChildren( ComponentDTO pComponent, String pType, AuditDTO pAudit, String pFilter )
        throws JrafEnterpriseException
    {

        // Initialisation de la facade
        Integer nbChildren = new Integer( 0 );

        if ( pAudit != null )
        {
            nbChildren = ComponentFacade.countChildren( pComponent, pType, pAudit, pFilter );
        }
        else
        {
            AuditDTO lastAudit = AuditFacade.getLastAudit( pComponent, null );
            nbChildren = ComponentFacade.countChildren( pComponent, pType, lastAudit, pFilter );
        }
        return nbChildren;
    }

    /**
     * R�cup�re les composants exclus
     * 
     * @param pAudit l'audit
     * @param pProject le projet auquel doivent appartenir les composants
     * @return les composants exclus du plan d'action
     * @throws JrafEnterpriseException exception JRAF
     */
    public Collection getExcluded( AuditDTO pAudit, ComponentDTO pProject )
        throws JrafEnterpriseException
    {
        Collection collection = new ArrayList( 0 );
        // Si il y a un audit, on le prend
        if ( pAudit != null )
        {
            collection = ComponentFacade.getExcluded( pAudit );
            // Sinon on r�cup�re le dernier
        }
        else
        {
            AuditDTO lastAudit = AuditFacade.getLastAudit( pProject, null );
            collection = ComponentFacade.getExcluded( lastAudit );
        }
        return collection;
    }

    /**
     * Permet de r�cup�rer tous les enfants d'un sous-projet de n'importe quel niveau
     * 
     * @param pProject composant parent renseignant l'identifiant
     * @param pType cl� du type de composant
     * @param pAudit audit du composant associ�, si <code>null</code>, on utilise le dernier audit
     * @return Collection de ComponentDTO
     * @throws JrafEnterpriseException exception JRAF
     */
    public Collection getProjectChildren( ComponentDTO pProject, String pType, AuditDTO pAudit )
        throws JrafEnterpriseException
    {

        Collection children = null;

        if ( pAudit != null )
        {
            children = ComponentFacade.getProjectChildren( pProject, pType, pAudit );
        }
        else
        {
            AuditDTO lastAudit = AuditFacade.getLastAudit( pProject, null );
            children = ComponentFacade.getProjectChildren( pProject, pType, lastAudit );

        }

        return children;

    }

    /**
     * Permet de r�cup�rer les n derniers milestones d'un projet donn�
     * 
     * @param pApplication ApplicationDTO renseignant l'ID du projet
     * @param pNbLignes nombre de lignes
     * @param pIndexDepart index de depart, si 0 utilis�, les derniers audits
     * @return Collection d'AuditDTO
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42DB6BE100E5
     */
    public Collection getLastMilestones( ComponentDTO pApplication, Integer pNbLignes, Integer pIndexDepart )
        throws JrafEnterpriseException
    {
        Collection collection =
            AuditFacade.getLastAudits( pApplication, pNbLignes, pIndexDepart, AuditBO.MILESTONE, AuditBO.TERMINATED );
        return collection;
    }

    /**
     * Permet de r�cup�rer les n derniers audits en �chec
     * 
     * @param pApplication ApplicationDTO renseignant l'ID du projet
     * @param pNbLignes nombre de lignes
     * @param pIndexDepart index de depart, si 0 utilis�, les derniers audits
     * @return Collection d'AuditDTO
     * @throws JrafEnterpriseException exception JRAF
     */
    public Collection getFailedAudits( ComponentDTO pApplication, Integer pNbLignes, Integer pIndexDepart )
        throws JrafEnterpriseException
    {
        Collection collection = AuditFacade.getFailedAudits( pApplication, pNbLignes, pIndexDepart );
        return collection;
    }

    /**
     * Retourne le dernier audit de suivi programm� d'une application. (<code>null</code> si aucun audit de suivi
     * n'est programm� pour l'application)
     * 
     * @param pApplicationDTO l'application
     * @return le dernier audit de suivi programm�
     * @throws JrafEnterpriseException si erreur
     */
    public AuditDTO getLastNotAttemptedAudit( ComponentDTO pApplicationDTO )
        throws JrafEnterpriseException
    {
        AuditDTO lAuditDTO = null;

        List lAuditsList =
            AuditFacade.getLastAudits( pApplicationDTO, new Integer( 1 ), new Integer( 0 ), AuditBO.NORMAL,
                                       AuditBO.NOT_ATTEMPTED );
        if ( lAuditsList.size() != 0 )
        {
            lAuditDTO = (AuditDTO) lAuditsList.get( 0 );
        }
        return lAuditDTO;
    }

    /**
     * Permet de r�cup�rer les n derniers audits termin�s
     * 
     * @param pApplication ApplicationDTO renseignant l'ID du projet
     * @param pNbLignes nombre de lignes
     * @param pIndexDepart index de depart, si 0 utilis�, les derniers audits
     * @return Collection d'AuditDTO
     * @throws JrafEnterpriseException exception JRAF
     */
    public Collection getTerminatedAudits( ComponentDTO pApplication, Integer pNbLignes, Integer pIndexDepart )
        throws JrafEnterpriseException
    {
        Collection collection = AuditFacade.getTerminatedAudits( pApplication, pNbLignes, pIndexDepart );
        return collection;
    }

    /**
     * Permet de r�cup�rer les n derniers audits de suivi d'un projet donn�
     * 
     * @param pApplication ApplicationDTO renseignant l'ID du projet
     * @param pNbLignes nombre de lignes
     * @param pIndexDepart index de depart, si 0 utilis�, les derniers audits
     * @return Collection d'AuditDTO
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42DB6BE10121
     */
    public Collection getLastPeriodicAudits( ComponentDTO pApplication, Integer pNbLignes, Integer pIndexDepart )
        throws JrafEnterpriseException
    {
        Collection collection =
            AuditFacade.getLastAudits( pApplication, pNbLignes, pIndexDepart, AuditBO.NORMAL, AuditBO.TERMINATED );
        return collection;
    }

    /**
     * Permet de r�cup�rer les n derniers audits (tous confondus) d'un projet donn�
     * 
     * @param pApplication ApplicationDTO renseignant l'ID du projet
     * @param pNbLignes nombre de lignes
     * @param pIndexDepart index de depart, si 0 utilis�, les derniers audits
     * @param pStatus le status de l'audit
     * @return Collection d'AuditDTO
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42DB6BE10153
     */
    public Collection getLastAllAudits( ComponentDTO pApplication, Integer pNbLignes, Integer pIndexDepart,
                                        Integer pStatus )
        throws JrafEnterpriseException
    {
        Collection collection =
            AuditFacade.getLastAudits( pApplication, pNbLignes, pIndexDepart, null, pStatus.intValue() );
        return collection;
    }

    /**
     * Permet de r�cup�rer les n derniers audits partiels d'un projet donn�
     * 
     * @param pApplication ApplicationDTO renseignant l'ID du projet
     * @param pNbLignes nombre de lignes
     * @param pIndexDepart index de depart, si 0 utilis�, les derniers audits
     * @return Collection d'AuditDTO
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42DB6BE10153
     */
    public Collection getPartialAudits( ComponentDTO pApplication, Integer pNbLignes, Integer pIndexDepart )
        throws JrafEnterpriseException
    {
        Collection collection = AuditFacade.getPartialAudits( pApplication, pNbLignes, pIndexDepart );
        return collection;
    }

    /**
     * This method return the list of AuditDTO done (or to do) after the date (pDate) given in argument and which status
     * is not in pExcludedStatus list
     * 
     * @param pApplications The applications for which we search audits
     * @param pDate The limit date
     * @param pExcludedStatus The list of audit status excluded from the search
     * @return The list of auditDTO corresponding to the criteria given in arguments
     * @throws JrafEnterpriseException Error happen during the search
     */
    public Collection getAllAuditsAfterDate( Collection pApplications, Date pDate, Integer[] pExcludedStatus )
        throws JrafEnterpriseException
    {
        return AuditFacade.getAllAuditsAfterDate( pApplications, pDate, pExcludedStatus );
    }

    /**
     * @param pApplications les applications
     * @param pDate la date
     * @param pExcludedStatus les statuts � exclure
     * @param pExcludedApplications les ids des applications � ne pas prendre en compte
     * @param pNbAuditsPerAppli le nombre max d'audits par application
     * @return les audits pour les applications de pApplications dont l'ex�cution s'est effectu� apr�s pDate (si ce
     *         param�tre n'est pas nul)
     * @throws JrafEnterpriseException si erreur
     */
    public Collection getAuditsForPortlet( Collection pApplications, Date pDate, Integer[] pExcludedStatus,
                                           String[] pExcludedApplications, Integer pNbAuditsPerAppli )
        throws JrafEnterpriseException
    {
        return AuditFacade.getAuditsForPortlet( pApplications, pDate, pExcludedStatus, pExcludedApplications,
                                                pNbAuditsPerAppli );
    }

    /**
     * Permet de r�cup�rer les n derniers audits (tous confondus) d'un projet donn�
     * 
     * @param pAudit l'auditDto permettant d'obtenir
     * @return un AuditDTO
     * @throws JrafEnterpriseException exception JRAF
     * @roseuid 42DB6BE10153
     */
    public Collection getLastTwoAuditsByAuditId( AuditDTO pAudit )
        throws JrafEnterpriseException
    {
        Collection collection = new ArrayList( 0 );
        collection.add( AuditFacade.get( pAudit ) );
        collection.add( AuditFacade.getPreviousAudit( pAudit, null ) );
        return collection;
    }

    /**
     * Permet de retourner la liste des parents relatif a un composant
     * 
     * @param pComponent ComponentDTO renseignant l'identifiant
     * @param pNbParents nombre de parents qu'on souhaite retourner, si <code>null</code>, tous les parents sont
     *            retourn�s
     * @return List de ComponentDTO parents du composant
     * @throws JrafEnterpriseException exception Jraf
     */
    public List getParentsComponent( ComponentDTO pComponent, Integer pNbParents )
        throws JrafEnterpriseException
    {

        List parentList = null;

        if ( null != pComponent )
        {
            if ( pComponent.getID() >= 0 )
            {

                parentList = ComponentFacade.getParentsComponent( pComponent, pNbParents );

            }
        }

        return parentList;

    }

    /**
     * M�thode pour mettre � jour un composant apr�s que l'on ait modifi� sa justification ou son status par rapport au
     * plan d'action
     * 
     * @param pComponent le composant
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public void updateComponent( ComponentDTO pComponent )
        throws JrafEnterpriseException
    {
        ComponentFacade.updateComponent( pComponent );
    }

    /**
     * Returns the list of projects with the name beginning with <code>pProjectName</code>, with their application's name
     * beginning with <code>pAppliName</code>, posessing the tags wanted in <code>pTagNames</code> and included in the
     * list <code>pUserAppli</code> associated with their last audit (may be null)
     * 
     * @param pUserAppli the list of application of the current user
     * @param pAppliName the beginning of the name of the associated application
     * @param pProjectName the beginning of the name of the project
     * @param pTagNames The tags wanted on the project
     * @throws JrafEnterpriseException if an error occurs
     * @return the found projects with their last audit if it exists
     */
    public Map getProjectsWithLastAudit( Collection pUserAppli, String pAppliName, String pProjectName, String[] pTagNames)
        throws JrafEnterpriseException
    {
        return ComponentFacade.getProjectsWithLastAudit( pUserAppli, pAppliName, pProjectName, pTagNames );
    }

    /**
     * Permet de r�cup�rer des valeurs pour une liste de types de r�sultat, un type de composant et une liste d'audits
     * donn�s
     * 
     * @param pTags tableau de TagDTO renseignant le ou les tags selon lequel on doit chercher
     * @return applications Les applications poss�dant le tag demand�
     * @throws JrafEnterpriseException Exception JRAF
     * @roseuid 42CBFBFD030B
     */
    public List<ComponentDTO> getTaggedApplications( TagDTO[] pTags )
        throws JrafEnterpriseException
    {

        // Initialisation
        List<ComponentDTO> applications = null; // retour de l'AC

        if ( pTags != null && pTags.length > 0 )
        {
            // Envoi un tag aux fa�ades et r�cup�re des r�sultats
            applications = ComponentFacade.getTaggedApplications( pTags );
        }

        return applications;
    }

    /**
     * Permet de r�cup�rer des valeurs pour une liste de types de r�sultat, un type de composant et une liste d'audits
     * donn�s
     * 
     * @param pTags tableau de TagDTO renseignant le ou les tags selon lequel on doit chercher
     * @return projects Les projets poss�dant le tag demand�
     * @throws JrafEnterpriseException Exception JRAF
     * @roseuid 42CBFBFD030B
     */
    public List<ComponentDTO> getTaggedProjects( TagDTO[] pTags )
        throws JrafEnterpriseException
    {

        // Initialisation
        List<ComponentDTO> projects = null; // retour de l'AC

        if ( pTags != null && pTags.length > 0 )
        {
            // Envoi un tag aux fa�ades et r�cup�re des r�sultats
            projects = ComponentFacade.getTaggedProjects( pTags );
        }

        return projects;
    }
    
    /**
     * @param pType le type d'audit
     * @param pStatus le status de l'audit
     * @return les audits dont le statut est <code>pStatus</code> de type <code>pType</code>
     * @throws JrafEnterpriseException exception Jraf
     */
    public Collection getAudits( String pType, Integer pStatus )
        throws JrafEnterpriseException
    {
        return AuditFacade.getAudits( pType, pStatus.intValue() );
    }

    /**
     * @return le dernier audit termin� de chaque application
     * @throws JrafEnterpriseException si erreur
     */
    public Collection getLastTerminatedAudits()
        throws JrafEnterpriseException
    {
        return AuditFacade.getLastTerminatedAudits();
    }

    /**
     * @param pAudits les audits dont la date est � mettre � jour
     * @return le nombre d'audits mis � jour
     * @throws JrafEnterpriseException exception Jraf
     */
    public Integer updateAuditsDateOrStatus( Collection pAudits )
        throws JrafEnterpriseException
    {
        return AuditFacade.updateAuditsDateOrStatus( pAudits );
    }

    /**
     * Renvoie la liste des projets attach�s � la grille qualit� avec le nom en param�tre
     * 
     * @param pQualityGridName le nom de la grille qualit�
     * @return la liste
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public Collection findWhereQualityGrid( String pQualityGridName )
        throws JrafEnterpriseException
    {
        return ProjectFacade.findWhereQualityGrid( pQualityGridName );
    }

    /**
     * @param pProjectId l'id du projet
     * @return true si l'export IDE est possible pour ce projet
     * @throws JrafEnterpriseException si erreur
     */
    public Boolean canBeExportedToIDE( Long pProjectId )
        throws JrafEnterpriseException
    {
        return ProjectFacade.canBeExportedToIDE( pProjectId );
    }

    /**
     * @param pProjectId l'id du projet
     * @return le workspace du projet si il existe, null sinon
     * @throws JrafEnterpriseException si erreur
     */
    public String getProjectWorkspace( Long pProjectId )
        throws JrafEnterpriseException
    {
        return ProjectFacade.getProjectWorkspace( pProjectId );
    }
}
