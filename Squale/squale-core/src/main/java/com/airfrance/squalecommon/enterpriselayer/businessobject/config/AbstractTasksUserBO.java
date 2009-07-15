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
package com.airfrance.squalecommon.enterpriselayer.businessobject.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Lanceur de t�ches Poss�de un nom unique. Poss�de des t�ches d'analyses d�finies dans un ordre pr�cis Poss�de des
 * t�ches dites finales d�finies dans un ordre pr�cis
 * 
 * @hibernate.class table="Tasks_User" lazy="true" discriminator-value="AbstractTasksUser"
 * @hibernate.discriminator column="subclass"
 */
public abstract class AbstractTasksUserBO
{

    /**
     * Identifiant (au sens technique) de l'objet
     */
    protected long mId;

    /** Le nom associ� */
    protected String mName;

    /** La liste des t�ches d'analyses */
    protected List mAnalysisTasks;

    /** La liste des t�ches finales */
    protected List mTerminationTasks;

    /**
     * Constructeur par d�faut
     */
    public AbstractTasksUserBO()
    {
        mId = -1;
        mName = "";
        mAnalysisTasks = new ArrayList();
        mTerminationTasks = new ArrayList();
    }

    /**
     * M�thode d'acc�s pour mId
     * 
     * @return la l'identifiant (au sens technique) de l'objet
     * @hibernate.id generator-class="native" type="long" column="AbstractTasksUserId" unsaved-value="-1" length="19"
     * @hibernate.generator-param name="sequence" value="tasksUser_sequence"
     */
    public long getId()
    {
        return mId;
    }

    /**
     * Change la valeur de mId
     * 
     * @param pId le nouvel identifiant
     */
    public void setId( long pId )
    {
        mId = pId;
    }

    /**
     * M�thode d'acc�s � mAnalysisTasks
     * 
     * @return la liste des t�ches d'analyses
     * @hibernate.list table="Analysis_Task" cascade="all" lazy="false"
     * @hibernate.key column="TasksUserId"
     * @hibernate.index column="AnalysisTaskIndex" type="int" length="10"
     * @hibernate.many-to-many class="com.airfrance.squalecommon.enterpriselayer.businessobject.config.TaskRefBO"
     *                         column="TaskRefId" outer-join="auto"
     */
    public List getAnalysisTasks()
    {
        return mAnalysisTasks;
    }

    /**
     * M�thode d'acc�s � mName
     * 
     * @return le nom du profile
     * @hibernate.property name="name" column="Name" type="string" length="255" not-null="true" unique="true"
     *                     update="true" insert="true"
     */
    public String getName()
    {
        return mName;
    }

    /**
     * M�thode d'acc�s � mTerminationTasks
     * 
     * @return la liste des t�ches finales
     * @hibernate.list table="Termination_Task" cascade="all" lazy="false"
     * @hibernate.key column="TasksUserId"
     * @hibernate.index column="TerminationTaskIndex" type="int" length="10"
     * @hibernate.many-to-many class="com.airfrance.squalecommon.enterpriselayer.businessobject.config.TaskRefBO"
     *                         column="TaskRefId" outer-join="auto"
     */
    public List getTerminationTasks()
    {
        return mTerminationTasks;
    }

    /**
     * Change la valeur de mAnalysisTasks
     * 
     * @param pAnalysisTasks la nouvelle liste des t�ches d'analyses
     */
    public void setAnalysisTasks( List pAnalysisTasks )
    {
        mAnalysisTasks = pAnalysisTasks;
    }

    /**
     * Change la valeur de mName
     * 
     * @param pName le nouveau nom du profile
     */
    public void setName( String pName )
    {
        mName = pName;
    }

    /**
     * Change la valeur de mTerminationTasks
     * 
     * @param pTerminationTasks la nouvelle liste de t�ches finales
     */
    public void setTerminationTasks( List pTerminationTasks )
    {
        mTerminationTasks = pTerminationTasks;
    }

    /**
     * Ajoute une t�che d'analyse � la fin de la liste des t�ches d'analyses
     * 
     * @param pTask la t�che � ajouter
     */
    public void addAnalysisTask( TaskRefBO pTask )
    {
        mAnalysisTasks.add( pTask );
    }

    /**
     * Supprime une t�che de la liste des t�ches finales
     * 
     * @param pTask la t�che � supprimer
     */
    public void removeTerminationTask( TaskRefBO pTask )
    {
        mTerminationTasks.remove( pTask );
    }

    /**
     * Supprime une t�che d'analyse de la liste des t�ches d'analyses
     * 
     * @param pTask la t�che � supprimer
     */
    public void removeAnalysisTask( TaskRefBO pTask )
    {
        mAnalysisTasks.remove( pTask );
    }

    /**
     * Ajoute une t�che � ex�cuter en dernier � la fin de la liste des t�ches finales
     * 
     * @param pTask la t�che � ajouter
     */
    public void addTerminationTask( TaskRefBO pTask )
    {
        mTerminationTasks.add( pTask );
    }

    /**
     * Retourne la liste de toutes les t�ches pouvant �tre configur�es
     * 
     * @return les t�ches configurables
     */
    public Collection getConfigurableTasks()
    {
        Collection tasks = new ArrayList();
        tasks.addAll( getConfigurableTasks( mAnalysisTasks ) );
        tasks.addAll( getConfigurableTasks( mTerminationTasks ) );
        return tasks;
    }

    /**
     * Renvoit les t�ches de la liste qui sont configurables
     * 
     * @param pTasks une liste de TaskBO
     * @return la collection des t�ches configurables
     */
    private Collection getConfigurableTasks( List pTasks )
    {
        Collection tasks = new ArrayList();
        TaskRefBO task;
        for ( int i = 0; i < pTasks.size(); i++ )
        {
            task = (TaskRefBO) pTasks.get( i );
            if ( task.getTask().isConfigurable() )
            {
                tasks.add( task.getTask() );
            }
        }
        return tasks;
    }
}
