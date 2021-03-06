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
/*
 * Cr�� le 8 juil. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package org.squale.squalecommon.daolayer.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.squale.jraf.commons.exception.JrafDaoException;
import org.squale.jraf.provider.persistence.hibernate.AbstractDAOImpl;
import org.squale.jraf.spi.persistence.ISession;
import org.squale.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.ProjectParameterBO;
import org.squale.squalecommon.enterpriselayer.businessobject.config.ProjectProfileBO;
import org.squale.squalecommon.enterpriselayer.businessobject.config.SourceManagementBO;
import org.squale.squalecommon.enterpriselayer.businessobject.rule.QualityGridBO;

/**
 * @author M400843
 */
public class ProjectDAOImpl
    extends AbstractDAOImpl
{
    /**
     * Instance singleton
     */
    private static ProjectDAOImpl instance = null;

    /** log */
    private static Log LOG;

    /** initialisation du singleton */
    static
    {
        instance = new ProjectDAOImpl();
    }

    /**
     * Constructeur prive
     * 
     * @throws JrafDaoException
     */
    private ProjectDAOImpl()
    {
        initialize( ProjectBO.class );
        if ( null == LOG )
        {
            LOG = LogFactory.getLog( ProjectDAOImpl.class );
        }
    }

    /**
     * Retourne un singleton du DAO
     * 
     * @return singleton du DAO
     */
    public static ProjectDAOImpl getInstance()
    {
        return instance;
    }

    /**
     * Retourne les projets en relation avec l'audit
     * 
     * @param pSession une session hibernate
     * @param pAuditId l'id de l'audit
     * @return une collection de projets
     * @throws JrafDaoException si une erreur � lieu
     */
    public Collection findWhere( ISession pSession, Long pAuditId )
        throws JrafDaoException
    {
        String whereClause = "where ";
        whereClause += pAuditId + " in elements(" + getAlias() + ".audits)";

        Collection ret = findWhere( pSession, whereClause );
        return ret;
    }

    /**
     * Cr�ation ou sauvegarde de l'objet en fonction du param�trage d�fini dans le fichier de mapping V�rifie l'unicit�
     * du nom du projet pour l'application
     * 
     * @param pSession la session
     * @param pProject le projet � persister ou � mettre � jour
     * @return le projet cr�� si tout s'est bien pass�
     * @throws JrafDaoException si une erreur � lieu ou si le nom du projet existe d�j� pour l'application correspondant
     */
    public ProjectBO save( ISession pSession, ProjectBO pProject )
        throws JrafDaoException
    {
        Long idProject = new Long( pProject.getId() );
        ProjectBO newProject = null;

        // si le projet n'existe pas, il se peut qu'un autre projet existe deja avec le meme nom
        if ( ( null != load( pSession, idProject ) ) || ( 0 == countWhereParentName( pSession, pProject ) ) )
        {
            super.save( pSession, pProject );
            newProject = pProject;
        }
        return newProject;
    }

    /**
     * Retourne les projets tagg�s par un tag donn�
     * 
     * @param pSession une session hibernate
     * @param ptagIds tableau d'ids des Tags demand�s
     * @return une collection de projets
     * @throws JrafDaoException si une erreur � lieu
     */
    public Collection findtagged( ISession pSession, Long[] ptagIds )
        throws JrafDaoException
    {
        String whereClause = "where ";
        if ( ptagIds.length > 1 )
        {
            whereClause += ptagIds[0] + " in elements(" + getAlias() + ".tags)";
            for ( int i = 1; i < ptagIds.length; i++ )
            {
                whereClause += " and " + ptagIds[i] + " in elements(" + getAlias() + ".tags)";
            }
        }
        else
        {
            whereClause += ptagIds[0] + " in elements(" + getAlias() + ".tags)";
        }

        Collection ret = findWhere( pSession, whereClause );
        return ret;
    }

    /**
     * Cr�ation de l'objet m�tier persistent, <b>Attention : </b> la relation avec le projet doit �tre � jour
     * 
     * @param pSession la session
     * @param pProject le projet � persister
     * @return le projet cr�� si tout s'est bien pass�
     * @throws JrafDaoException si une erreur � lieu ou si le nom du projet existe d�j� pour l'application
     *             correspondante
     */
    public ProjectBO create( ISession pSession, ProjectBO pProject )
        throws JrafDaoException
    {
        int existantProject = countWhereParentName( pSession, pProject );
        ProjectBO newProject = null;

        if ( 0 == existantProject )
        {
            super.create( pSession, pProject );
            newProject = pProject;
        }
        return newProject;
    }

    /**
     * R�cup�re la liste des projets de cette application
     * 
     * @param pSession la session
     * @param pApplicationId l'id de l'application dont on veut les projets
     * @return la liste des projets fils
     * @throws JrafDaoException en cas d'�chec
     */
    public Collection findAllProjects( ISession pSession, Long pApplicationId )
        throws JrafDaoException
    {
        String whereClause = "where ";
        whereClause += getAlias() + ".parent=" + pApplicationId.longValue();

        Collection col = findWhere( pSession, whereClause );
        return col;
    }

    /**
     * Retourne le nombre de projet du nom de pProject et de meme application que pProject
     * 
     * @param pSession la session
     * @param pProject le projet
     * @return le nombre de projets trouv� (th�oriquement 0 ou 1)
     * @throws JrafDaoException si une erreur � lieu
     */
    private int countWhereParentName( ISession pSession, ProjectBO pProject )
        throws JrafDaoException
    {
        String whereClause = "where ";
        whereClause += getAlias() + ".parent.id = " + pProject.getParent().getId();
        whereClause += " AND ";
        whereClause += getAlias() + ".name = '" + pProject.getName() + "'";
        // Il peut en exister d'autre de m�me nom mais avec un status "� supprimer"
        whereClause += " AND ";
        whereClause += getAlias() + ".status != " + ProjectBO.DELETED;

        int ret = countWhere( pSession, whereClause ).intValue();
        return ret;
    }

    /**
     * @see org.squale.jraf.spi.persistence.IPersistenceDAO#remove(org.squale.jraf.spi.persistence.ISession,
     *      java.lang.Object)
     */
    public void remove( ISession pSession, Object pObj )
        throws JrafDaoException
    {
        super.remove( pSession, pObj );
    }

    /**
     * @see org.squale.jraf.spi.persistence.IPersistenceDAO#remove(org.squale.jraf.spi.persistence.ISession,
     *      java.lang.Object)
     */
    public void setStatusDelete( ISession pSession, ProjectBO pProject )
        throws JrafDaoException
    {
        // Supprime "logiquement" le projet lui-m�me
        pProject.setStatus( ProjectBO.DELETED );
        super.save( pSession, pProject );
        // il faut faire (en batch) la suppresion physique !
    }

    /**
     * Mise � jour des grilles qualit�
     * 
     * @param pSession session
     * @param pQualityGrid grille qualit�
     * @throws JrafDaoException si erreur
     */
    public void updateQualityGrid( ISession pSession, QualityGridBO pQualityGrid )
        throws JrafDaoException
    {
        String whereClause = "where ";
        whereClause += getAlias() + ".qualityGrid.id != " + pQualityGrid.getId();
        whereClause += " AND ";
        whereClause += getAlias() + ".qualityGrid.name = '" + pQualityGrid.getName() + "'";
        Iterator projectsIt = findWhere( pSession, whereClause ).iterator();
        // Parcours de la collection
        while ( projectsIt.hasNext() )
        {
            // Mise � jour des grilles qualit�
            ProjectBO project = (ProjectBO) projectsIt.next();
            project.setQualityGrid( pQualityGrid );
            save( pSession, project );
        }
    }

    /**
     * Retourne la liste des projets utilisant cette grille qualit� Sert notamment pour la notification aux
     * gestionnaires des applications lors d'un chargement de nouvelle grille
     * 
     * @param pSession session
     * @param pQualityGridName grille qualit�
     * @return l'ensemble des projets utilisant cette grille
     * @throws JrafDaoException si erreur
     */
    public Collection findWhereQualityGrid( ISession pSession, String pQualityGridName )
        throws JrafDaoException
    {
        String whereClause = "where ";
        whereClause += getAlias() + ".qualityGrid.name = '" + pQualityGridName + "'";
        return findWhere( pSession, whereClause );
    }

    /**
     * Test d'utilisation de la grille qualit�
     * 
     * @param pSession session
     * @param pQualityGridId id de grille qualit�
     * @return true si la grille est utilis�e par au moins un projet
     * @throws JrafDaoException si erreur
     */
    public boolean isGridUsed( ISession pSession, Long pQualityGridId )
        throws JrafDaoException
    {
        String whereClause = "where ";
        whereClause += getAlias() + ".qualityGrid.id = " + pQualityGridId;
        Collection projects = findWhere( pSession, whereClause );
        return projects.size() > 0;
    }

    /**
     * Retourne les projets dont le source manager est psourceManagerId
     * 
     * @param pSession session
     * @param pSourceManagerId l'id du source manager
     * @throws JrafDaoException si erreur
     * @return une liste de ProjectBO
     */
    public Collection findWhereSourceManager( ISession pSession, Long pSourceManagerId )
        throws JrafDaoException
    {
        SourceManagementBO managerBO = null;
        String whereClause = "where ";
        whereClause += getAlias() + ".sourceManager = " + pSourceManagerId;
        Collection projects = findWhere( pSession, whereClause );
        return projects;
    }

    /**
     * Retourne les projets dont le profil est pProfileId
     * 
     * @param pSession session
     * @param pProfileId l'id du profil
     * @throws JrafDaoException si erreur
     * @return une liste de ProjectBO
     */
    public Collection findWhereProfile( ISession pSession, Long pProfileId )
        throws JrafDaoException
    {
        ProjectProfileBO profileBO = null;
        String whereClause = "where ";
        whereClause += getAlias() + ".profile = " + pProfileId;
        Collection projects = findWhere( pSession, whereClause );
        return projects;
    }

    /**
     * Returns the list of projects with the name beginning with <code>pProjectName</code>, with their application's
     * name beginning with <code>pAppliName</code>, posessing the tags wanted in <code>pTagNames</code> and included in
     * the list <code>pUserAppli</code> associated with their last audit (may be null)
     * 
     * @param pSession the session
     * @param pAppliIds the ids of the current users's applications
     * @param pAppliName the beginning of the name of the associated application
     * @param pProjectName the beginning of the name of the project
     * @param pTagIds The ids from the tags wanted on the project
     * @throws JrafDaoException if an error occurs
     * @return the list of retrieved projects
     */
    public Collection findProjects( ISession pSession, long[] pAppliIds, String pAppliName, String pProjectName,
                                    long[] pTagIds )
        throws JrafDaoException
    {
        Collection projects = null;
        int nbAppli = pAppliIds.length;
        // On v�rifie qu'il y a des applications pour �viter
        // une requete inutile
        if ( nbAppli == 0 )
        {
            projects = new ArrayList();
        }
        else
        {
            // On construit la clause where afin que les comparaisons
            // de string ne prennent pas compte de la casse.
            String whereClause = "where ";
            whereClause += "lower(" + getAlias() + ".name) like '" + pProjectName.toLowerCase() + "%'";
            whereClause += " and ";
            whereClause += "lower(" + getAlias() + ".parent.name) like '" + pAppliName.toLowerCase() + "%'";
            whereClause += " and ";
            whereClause += getAlias() + ".parent.id in (";
            // On prend le premier �l�ment sans mettre de virgule,
            // comme �a on est s�r que la clause sera correctement form�e.
            whereClause += pAppliIds[0];
            for ( int i = 1; i < nbAppli; i++ )
            {
                whereClause += ", " + pAppliIds[i];
            }
            whereClause += ")";
            if ( pTagIds != null )
            {
                whereClause += " and ";
                if ( pTagIds.length > 1 )
                {
                    whereClause += pTagIds[0] + " in elements(" + getAlias() + ".tags)";
                    for ( int i = 1; i < pTagIds.length; i++ )
                    {
                        whereClause += " and " + pTagIds[i] + " in elements(" + getAlias() + ".tags)";
                    }
                }
                else
                {
                    whereClause += pTagIds[0] + " in elements(" + getAlias() + ".tags)";
                }
            }
            projects = findWhere( pSession, whereClause );
        }
        return projects;
    }

    /**
     * @param pSession la session hibernate
     * @param pSiteId l'id du site
     * @param pProfileName le nom du profil
     * @return le nombre de projets pour ce site et ce profil
     * @throws JrafDaoException en cas d'�chec
     */
    public int countBySiteAndProfil( ISession pSession, long pSiteId, String pProfileName )
        throws JrafDaoException
    {
        int result = 0;
        // On construit la clause where afin que les comparaisons
        // de string ne prennent pas compte de la casse.
        String whereClause =
            "where " + getAlias() + ".parent.serveurBO.serveurId='" + pSiteId + "' AND " + getAlias()
                + ".profile.name='" + pProfileName + "'";
        result = countWhere( pSession, whereClause ).intValue();
        return result;
    }

    /**
     * Retourne tous les projets ayant le statut <code>pStatus</code> et rattach� � une application <code>pSite</code>
     * 
     * @param pSession la session
     * @param pStatus le status du projet
     * @param pSite le site de l'application
     * @return les projets concern�s
     * @throws JrafDaoException si erreur DAO
     */
    public Collection findWhereStatusAndSite( ISession pSession, int pStatus, long pSite )
        throws JrafDaoException
    {
        String whereClause = "where ";
        // le statut
        whereClause += getAlias() + ".status=" + pStatus;
        // le site de l'application
        whereClause += " and (";
        whereClause += getAlias() + ".parent.serveurBO.serveurId='" + pSite + "'";
        whereClause += " or ";
        whereClause += getAlias() + ".parent.serveurBO is null)";
        return findWhere( pSession, whereClause );

    }

    /**
     * @param pSession la session
     * @param pProjectId l'id du projet
     * @param pKey la cl� du param�tre � rechercher
     * @return le param�tre du projet de premier niveau de cl� <code>pKey</code>
     * @throws JrafDaoException si erreur
     */
    public ProjectParameterBO getParameterWhere( ISession pSession, Long pProjectId, String pKey )
        throws JrafDaoException
    {
        ProjectParameterBO result = null;
        StringBuffer query = new StringBuffer( "select " );
        query.append( "param from ProjectBO as " );
        query.append( getAlias() );
        query.append( " join " );
        query.append( getAlias() );
        query.append( ".parameters.parameters as param " );
        query.append( " where " );
        query.append( getAlias() );
        query.append( ".id=" );
        query.append( pProjectId );
        query.append( " and index(param) = '" );
        query.append( pKey );
        query.append( "'" );
        List results = find( pSession, query.toString() );
        LOG.warn( query.toString() );
        if ( results.size() == 1 )
        { // Il ne peut y avoir qu'un param�tre trouv�
            result = (ProjectParameterBO) results.get( 0 );
        }
        return result;
    }

    /**
     * This method retrieves each module of the application (represented by its technical id given in argument). This
     * method returns a list of array and each array represents one module
     * 
     * @param session The hibernate session
     * @param appTechnicalId The technical id of the parent application
     * @return A list of array [module technical id (Long), module name (String)]
     * @throws JrafDaoException Exception occurs during the search in the database
     */
    public List<Object[]> getChildren( ISession session, Long appTechnicalId )
        throws JrafDaoException
    {
        List<Object[]> listToReturnList = null;
        StringBuffer request = new StringBuffer( "select mod.id, mod.name from ProjectBO mod where mod.parent.id = " );
        request.append( appTechnicalId );
        listToReturnList = find( session, request.toString() );
        return listToReturnList;
    }

    /**
     * This method retrieves the list of modules linked to an audit
     * 
     * @param session The hibernate session
     * @param audit The audit
     * @return The list of module linked to audit given in argument
     * @throws JrafDaoException Exception occurs during the serach
     */
    public List<ProjectBO> getModuleslinkedToAudit( ISession session, AuditBO audit )
        throws JrafDaoException
    {
        List<ProjectBO> moduleList = new ArrayList<ProjectBO>();
        StringBuilder whereClause = new StringBuilder( "where " );
        whereClause.append( getAlias() );
        whereClause.append( ".audits.id = " );
        whereClause.append( audit.getId() );
        moduleList = findWhere( session, whereClause.toString() );
        return moduleList;
    }

}
