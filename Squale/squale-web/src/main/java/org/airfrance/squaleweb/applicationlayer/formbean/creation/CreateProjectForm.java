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
package com.airfrance.squaleweb.applicationlayer.formbean.creation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionMapping;

import com.airfrance.squalecommon.datatransfertobject.component.parameters.MapParameterDTO;
import com.airfrance.squalecommon.datatransfertobject.config.TaskDTO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squaleweb.applicationlayer.formbean.ActionIdFormSelectable;

/**
 * Form bean for a Struts application.
 * 
 * @version 1.0
 * @author
 */
public class CreateProjectForm
    extends ActionIdFormSelectable
{

    /**
     * Les param�tres du projet
     */
    private MapParameterDTO mParameters;

    /** Grille qualit� */
    private String mQualityGrid;

    /**
     * Profil de l'application (Java, CPP, ...)
     */
    private String mProfile;

    /**
     * Type de r�cup�ration des sources de l'application
     */
    private String mSourceManagement;

    /**
     * Liste des t�ches configurables et standards
     */
    private List mStandardTasks;

    /**
     * Liste des t�ches configurables et dont la configuration est avanc�e
     */
    private List mAdvancedTasks;

    /**
     * Liste des noms des beans associ�s aux param�tres du projet.
     */
    private Set mTaskForms;

    /**
     * Le statut du projet
     */
    private int mStatus;

    /**
     * Constructeur
     */
    public CreateProjectForm()
    {
        setProjectId( "-1" );
        mParameters = new MapParameterDTO();
        mQualityGrid = "";
        mProfile = "";
        mSourceManagement = "";
        mStandardTasks = new ArrayList();
        mAdvancedTasks = new ArrayList();
        mTaskForms = new TreeSet();
        mStatus = ProjectBO.ACTIVATED;
    }

    /**
     * Reset du formulaire
     */
    public void reset()
    {
        setProjectId( "-1" );
        setProjectName( "" );
        mParameters = new MapParameterDTO();
        mQualityGrid = "";
        mProfile = "";
        mSourceManagement = "";
        mStandardTasks = new ArrayList();
        mAdvancedTasks = new ArrayList();
        mTaskForms = new TreeSet();
        mStatus = ProjectBO.ACTIVATED;
    }

    /**
     * Access method for the mProfile property.
     * 
     * @return the current value of the mProfile property
     */
    public String getProfile()
    {
        return mProfile;
    }

    /**
     * Sets the value of the mProfile property.
     * 
     * @param pProfile the new value of the mProfile property
     */
    public void setProfile( String pProfile )
    {
        mProfile = pProfile;
    }

    /**
     * @return grille qualit�
     */
    public String getQualityGrid()
    {
        return mQualityGrid;
    }

    /**
     * @param pString grille qualit�
     */
    public void setQualityGrid( String pString )
    {
        mQualityGrid = pString;
    }

    /**
     * @return le r�cup�rateur de sources
     */
    public String getSourceManagement()
    {
        return mSourceManagement;
    }

    /**
     * @param pSourceManagement le r�cup�rateur de sources
     */
    public void setSourceManagement( String pSourceManagement )
    {
        mSourceManagement = pSourceManagement;
    }

    /**
     * @return la liste des t�ches de configuration standard
     */
    public List getStandardTasks()
    {
        return mStandardTasks;
    }

    /**
     * @param pTasks la liste des t�ches de configuration standard
     */
    public void setStandardTasks( List pTasks )
    {
        mStandardTasks = pTasks;
    }

    /**
     * @return la liste des t�ches de configuration avanc�e
     */
    public List getAdvancedTasks()
    {
        return mAdvancedTasks;
    }

    /**
     * @param pTasks la liste des t�ches de configuration avanc�e
     */
    public void setAdvancedTasks( List pTasks )
    {
        mAdvancedTasks = pTasks;
    }

    /**
     * Dispatche les t�ches dans les bonnes listes
     * 
     * @param pTasks les t�ches configurables
     */
    public void setTasks( Collection pTasks )
    {
        // On reset les listes
        mStandardTasks.clear();
        mAdvancedTasks.clear();
        TaskDTO task = null;
        for ( Iterator it = pTasks.iterator(); it.hasNext(); )
        {
            task = (TaskDTO) it.next();
            //task.setAbstractGenericInherited();
            if ( task.isStandard() )
            {
                mStandardTasks.add( task );
            }
            else
            {
                mAdvancedTasks.add( task );
            }
        }
    }

    /**
     * @return les param�tres
     */
    public MapParameterDTO getParameters()
    {
        return mParameters;
    }

    /**
     * @param pParameters les nouveaux param�tres
     */
    public void setParameters( MapParameterDTO pParameters )
    {
        mParameters = pParameters;
    }

    /**
     * @return les nom des beans des t�ches configur�es
     */
    public Set getTaskForms()
    {
        return mTaskForms;
    }

    /**
     * @param pTaskForms la liste des nom des beans des t�ches configur�es
     */
    public void setTaskForms( Set pTaskForms )
    {
        mTaskForms = pTaskForms;
    }

    /**
     * Obtention d'une t�che par son nom
     * 
     * @param pTaskName nom de la t�che
     * @return t�che correspondante
     */
    public TaskDTO getConfigurableTask( String pTaskName )
    {
        ArrayList configurableTasks = new ArrayList( mStandardTasks );
        configurableTasks.addAll( mAdvancedTasks );
        TaskDTO result = null;
        for ( Iterator it = configurableTasks.iterator(); it.hasNext() && ( result == null ); )
        {
            TaskDTO task = (TaskDTO) it.next();
            if ( task.getName().equals( pTaskName ) )
            {
                result = task;
            }
        }
        return result;
    }

    /**
     * @return le statut
     */
    public int getStatus()
    {
        return mStatus;
    }

    /**
     * @param pStatus le statut
     */
    public void setStatus( int pStatus )
    {
        mStatus = pStatus;
    }

    /**
     * M�thode de validation pour v�rifier que le nom mis � l'application ne contient pas de caract�rs sp�ciaux. La
     * liste des caract�res sp�ciaux est d�fini par le pattern situ� dans la classe m�re
     * 
     * @param pMapping le mapping hibernate
     * @param pRequest la requete
     */
    public void wValidate( ActionMapping pMapping, HttpServletRequest pRequest )
    {
        // Le nom du projet
        if ( getProjectName().length() == 0 )
        {
            addError( "projectName", new ActionError( "error.field.required" ) );
        }
        else if ( !isAValidName( getProjectName() ) )
        {
            addError( "projectName", new ActionError( "error.name.containsInvalidCharacter" ) );
        }
        // Le r�cup�rateur de source
        if ( getSourceManagement().length() == 0 )
        {
            addError( "sourceManagement", new ActionError( "error.field.required" ) );
        }
        // Le profil
        if ( getProfile().length() == 0 )
        {
            addError( "profile", new ActionError( "error.field.required" ) );
        }
        // La grille qualit�
        if ( getQualityGrid().length() == 0 )
        {
            addError( "qualityGrid", new ActionError( "error.field.required" ) );
        }
    }

}
