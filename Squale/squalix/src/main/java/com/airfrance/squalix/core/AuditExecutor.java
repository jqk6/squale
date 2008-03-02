//Source file: D:\\cc_views\\squale_v0_0_act\\squale\\src\\squalix\\src\\com\\airfrance\\squalix\\core\\AuditExecutor.java

package com.airfrance.squalix.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.config.TaskRefBO;
import com.airfrance.squalix.messages.Messages;

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
 * @see com.airfrance.squalix.configurationmanager.Configuration
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
     * Constructeur.<br>
     * Instancie toutes les t�ches de l'arbre, et ex�cute un audit sur le projet.
     * 
     * @param pAudit Audit correspondant.
     * @param pProject projet sur lequel r�aliser l'audit.
     * @param pTasks les taches d'analyse � r�aliser
     * @param pTerminationTasks les t�ches � r�aliser lorsque toutes les analyses ont �t� termin�es.
     * @throws Exception si l'Audit ne peut-�tre cr��.
     * @roseuid 42AE866F01F7
     */
    public AuditExecutor( final List pTasks, final List pTerminationTasks, final AuditBO pAudit,
                          final ProjectBO pProject )
        throws Exception
    {
        // Le TaskData contenant les diff�rents param�tres temporaires.
        // Vide au d�part, il sera potentiellement modifi� par chaque t�che
        // ce qui permettra aux t�ches suivantes de r�cup�rer les informations
        // dont elles ont besoin et qui d�pendent d'une t�che pr�c�demment ex�cut�e.
        TaskData taskData = new TaskData();
        // Creation des t�ches d'analyse
        mTasks = new ArrayList();
        AbstractTask task = null;
        for ( int i = 0; i < pTasks.size(); i++ )
        {
            // On instancie chaque tache
            task =
                TaskUtility.createTask( (TaskRefBO) pTasks.get( i ), pProject.getId(), pProject.getParent().getId(),
                                        pAudit.getId(), taskData );
            // La liste des t�ches est compl�t�e par la nouvelle
            mTasks.add( task );
        }

        // Creation des t�ches de compl�tude sur le m�me sch�ma
        mTerminationTasks = new ArrayList();
        for ( int i = 0; i < pTerminationTasks.size(); i++ )
        {
            task =
                TaskUtility.createTask( (TaskRefBO) pTerminationTasks.get( i ), pProject.getId(),
                                        pProject.getParent().getId(), pAudit.getId(), taskData );
            // La liste des t�ches de terminaison est compl�t�e par la nouvelle
            mTerminationTasks.add( task );
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
     * Access method for the mTasks property.
     * 
     * @return the current value of the mTasks property
     * @roseuid 42C2A3B000C5
     */
    public List getTasks()
    {
        return mTasks;
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
