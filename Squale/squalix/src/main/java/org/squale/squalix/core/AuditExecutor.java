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
//Source file: D:\\cc_views\\squale_v0_0_act\\squale\\src\\squalix\\src\\org\\squale\\squalix\\core\\AuditExecutor.java

package org.squale.squalix.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.squale.squalecommon.enterpriselayer.businessobject.component.ApplicationBO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import org.squale.squalecommon.enterpriselayer.businessobject.config.TaskRefBO;
import org.squale.squalix.messages.Messages;

/**
 * Ex�cute un audit entier sur un projet. <br />
 * Il ex�cute les t�ches associ�e � une application en les ordonnan�ant. Cet ordonnancement des t�ches est d�termin� par
 * rapport au profil du projet. La configuration doit d�finir un sc�nario d'ex�cution pour le profil du projet. Voir la
 * configuration de Squalix pour plus de renseignements. <br />
 * <br />
 * Toutes les t�ches que lancent l'ex�cuteur d'analyse doivent h�riter de la classe abstraite <code>Task</code>.
 * <br />
 * <br />
 * Deux groupes de t�ches sont lanc�s par l'ex�cuteur d'analyse :
 * <ul>
 * <li>le premier concerne les analyses par des outils divers,</il>
 * <li>le second n�cessite que toutes les t�ches du premier soient termin�es (consolidation de r�sultats, g�n�ration de
 * graphiques...).</li>
 * </ul>
 * 
 * @see org.squale.squalix.configurationmanager.Configuration
 * @author m400842
 * @version 1.0
 */
public class AuditExecutor
    implements Runnable
{

    /**
     * Statut de l'ex�cution de l'audit � la fin de l'ex�cution
     */
    private int mFinalStatus = AuditBO.NOT_ATTEMPTED;

    /**
     * Audit param�trant l'analyse.
     */
    private AuditBO mAudit;

    /**
     * Projet sur lequel est ex�cut�e l'audit.
     */
    private ProjectBO mProject;

    /**
     * Logger
     */
    private static final Log LOGGER = LogFactory.getLog( AuditExecutor.class );

    /**
     * Liste des t�ches instanci�es dans l'audit.
     */
    private List mTasks;

    /**
     * T�ches � r�aliser lorsque toutes les t�ches d'analyse sont termin�es.
     */
    private List mTerminationTasks = null;

    /**
     * Instance du scheduleur de l'audit
     */
    private Scheduler mScheduler = null;

    /**
     * Constructor.<br>
     * Create the audit executor which contains task and termination task to execute for an audit.
     * 
     * @param pAudit The audit to do.
     * @param pProject Project on which we realize the audit.
     * @param pTasks Analyze tasks to realize.
     * @param pTerminationTasks Termination task to realize
     * @param lastProject Is it the last project ?
     * @roseuid 42AE866F01F7
     */
    public AuditExecutor( final List pTasks, final List pTerminationTasks, final AuditBO pAudit,
                          final ProjectBO pProject, boolean lastProject )
    {
        /*
         * The TaskData contains some temporary parameter. Empty at the beginning, it could be modified by each task.
         * Then the following tasks could recover the informations they need and which depends from a previous task.
         */
        TaskData taskData = new TaskData();
        ApplicationBO appli = (ApplicationBO) pProject.getParent();
        // Creation des t�ches d'analyse
        mTasks = new ArrayList();
        AbstractTask task = null;
        for ( int i = 0; i < pTasks.size(); i++ )
        {
            // Creation of each task
            task =
                TaskUtility.createTask( (TaskRefBO) pTasks.get( i ), pProject.getId(), pProject.getParent().getId(),
                                        pAudit.getId(), taskData );
            // The list of task is completed with the new one
            mTasks.add( task );

        }

        // Creation des t�ches de compl�tude sur le m�me sch�ma
        mTerminationTasks = new ArrayList();
        for ( int i = 0; i < pTerminationTasks.size(); i++ )
        {
            task =
                TaskUtility.createTask( (TaskRefBO) pTerminationTasks.get( i ), pProject.getId(),
                                        pProject.getParent().getId(), pAudit.getId(), taskData );

            // Is the task a source code recovering termination task ?
            if ( task instanceof AbstractSourceTerminationTask )
            {
                // if yes then we should determine if task does the optimization of the source code recovering
                AbstractSourceTerminationTask sourceterminationtask = (AbstractSourceTerminationTask) task;
                if ( sourceterminationtask.doOptimization() )
                {
                    // If the task does the optimization of the source code recovering then we don't add this task to
                    // the list and we keep it for after
                    appli.getSourceCodeTerminationTask().add( pTerminationTasks.get( i ) );

                }
                else
                {
                    // If the task doesn't do the optimization of the source code recovering then we add it to list of
                    // termination task
                    mTerminationTasks.add( task );
                }
            }
            else
            {
                // if the task is not a source code recovering termination task then we add it to the list of
                // termination task
                mTerminationTasks.add( task );
            }
        }
        // Is it the last project of this Audit ?
        if ( lastProject )
        {
            // If yes then we add all source code termination task we keep
            Iterator keepTaskIt = appli.getSourceCodeTerminationTask().iterator();
            while ( keepTaskIt.hasNext() )
            {
                // Creation of each task
                task =
                    TaskUtility.createTask( (TaskRefBO) keepTaskIt.next(), pProject.getId(),
                                            pProject.getParent().getId(), pAudit.getId(), taskData );
                // The termination task list is completed with the new one
                mTerminationTasks.add( task );
            }
        }

        mAudit = pAudit;
        mProject = pProject;
    }

    /**
     * @param pTask la t�che observ�e ayant chang� de statut.
     * @return un bool�en indiquant si on doit continuer � faire les taches d'analyse, return false si une tache a
     *         �chou�e
     * @roseuid 42C29E870138
     */
    public boolean update( final AbstractTask pTask )
    {
        // bool�en indiquant si on doit continuer � faire l'ensemble
        // des taches normalement ou si on doit arr�ter si il y a eu un probl�me
        boolean stop = false;
        int status = pTask.getStatus();
        Iterator it = mTasks.iterator();
        if ( AbstractTask.FAILED == status )
        {
            // la tache a �chou�e
            // On stoppe l'audit de toutes les t�ches en cours non encore lanc�es
            // En les mettant en statut CANCELLED
            LOGGER.error( "La tache : " + pTask.getClass().getName() + " a echouee" );
            // On parcours la liste des taches jusqu'� celle pass�e en param�tre
            while ( it.hasNext() && (AbstractTask) it.next() != pTask )
            {
            }
            ;
            // on dit que celle l� a �chou�e
            pTask.setStatus( AbstractTask.FAILED );
            // Si la tache �tait obligatoire, on annule les autres sinon on continue
            // mais dans tous les cas la tache est mise en echec
            stop = pTask.isMandatoryTask();
            if ( stop )
            {
                // et on change le status des suivantes � "annuler"
                while ( it.hasNext() )
                {
                    AbstractTask t = (AbstractTask) it.next();
                    TaskUtility.stopTask( t );
                    LOGGER.info( "La tache : " + t.getClass().getName() + " a ete annulee" );
                }
            }
        }
        return stop;
    }

    /**
     * Renvoie le status de l'audit executor apr�s analyse des status de toutes les taches
     * 
     * @return le status
     */
    private int getStatus()
    {
        // Initialisation � termin� par d�faut
        int result = AuditBO.TERMINATED;
        // On met dans les conditions de sortie de la boucle le staus failed,
        // car une fois que c'est failed c'est d�finitif
        // On n'affecte jamais termin� dans la boucle, car si on passe dans un des tests
        // c'est qu'il ne peut pas etre termin�
        // Termin� < partiel < failed
        for ( int i = 0; result != AuditBO.FAILED && i < mTasks.size(); i++ )
        {
            AbstractTask task = ( (AbstractTask) mTasks.get( i ) );
            if ( task.getStatus() == AbstractTask.FAILED )
            {
                if ( !task.isMandatoryTask() )
                {
                    result = AuditBO.PARTIAL;
                }
                else
                {
                    result = AuditBO.FAILED;
                }
            }
        }
        return result;
    }

    /**
     * Access method for the mAudit property.
     * 
     * @return the current value of the mAudit property
     * @roseuid 42C2A3B000B6
     */
    public AuditBO getAudit()
    {
        return mAudit;
    }

    /**
     * Getter method for the mTerminationTasks property.
     * 
     * @return the current value of the mTerminationTasks property
     */
    public List getTerminationTasks()
    {
        return mTerminationTasks;
    }

    /**
     * Termine l'audit en effectuant les op�rations n�cessaires � sa cl�ture
     * 
     * @roseuid 42CBC4C001F3
     */
    private void terminateAudit()
    {
        try
        {
            LOGGER.info( Messages.getString( "log.audit_executor.terminated" ) + mProject.getParent().getName()
                + Messages.getString( "separator.project" ) + mProject.getName() );
            // Execution des t�ches de compl�tude
            LOGGER.info( "**** Execution des taches terminales ****" );
            for ( int i = 0; i < mTerminationTasks.size(); i++ )
            {
                AbstractTask task = (AbstractTask) mTerminationTasks.get( i );
                task.run();
                // Attention: bien mettre les taches de terminaison
                // qui ne sont pas critiques comme �tant facultatives pour ne pas avoir
                // un audit en �chec si ces taches l� �chouent
                update( task );
            }
            // Les status termin�s ,partiels ou en �chec ne peuvent etre affect�s qu'ici
            // une fois que toutes les taches ont �t� r�alis�es
            mFinalStatus = getStatus();
        }
        catch ( Exception e )
        {
            LOGGER.error( e, e );
        }
    }

    /**
     * Execute l'ensemble des taches de l'audit
     * 
     * @roseuid 42CCD3550279
     */
    public void run()
    {
        boolean stop = false;
        // On lance les taches syst�mes pr�alables
        AbstractTask task = null;
        if ( mFinalStatus != AuditBO.FAILED )
        {
            // Execution des t�ches d'analyse
            for ( int i = 0; i < mTasks.size() && !stop; i++ )
            {
                task = (AbstractTask) mTasks.get( i );
                task.run();
                // On signale que la tache a �t� faite
                // et on met � jour son status
                stop = update( task );
            }
        }
        // met � jour la taille du file system
        mAudit.setMaxFileSystemSize( getMax( mTasks ) );
        // Puis on lance les t�ches qui cloturent l'analyse du projet
        terminateAudit();
    }

    /**
     * @param pTasks la liste des taches de l'audit
     * @return la taille max du file system durant l'audit
     */
    private Long getMax( List pTasks )
    {
        long finalMax = 0;
        for ( int i = 0; i < mTasks.size(); i++ )
        {
            long max = ( (AbstractTask) mTasks.get( i ) ).getMaxFileSystemSize();
            for ( int j = 0; j < mTasks.size(); j++ )
            {
                max += ( (AbstractTask) mTasks.get( i ) ).getPersistentFileSystemSize();
            }
            if ( max > finalMax )
            {
                finalMax = max;
            }
        }
        // Pour un affichage en m�gaOctet
        final int Mega = 1000000;
        return new Long( finalMax / Mega );
    }

    /**
     * Sets the value of the mScheduler property.
     * 
     * @param pScheduler the new value of the mScheduler property
     * @roseuid 42CE36C70276
     */
    public void setScheduler( Scheduler pScheduler )
    {
        mScheduler = pScheduler;
    }

    /**
     * Projet sur lequel est ex�cut� l'analyse.
     * 
     * @return le projet.
     * @roseuid 42DFC07A01C5
     */
    public ProjectBO getProject()
    {
        return mProject;
    }

    /**
     * @return le status � la fin de l'ex�cution
     */
    public int getFinalStatus()
    {
        return mFinalStatus;
    }

    /**
     * @param pFinalStatus la nouvelle valeur du status � la fin de l'ex�cution
     */
    public void setFinalStatus( int pFinalStatus )
    {
        mFinalStatus = pFinalStatus;
    }

}
