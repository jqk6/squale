package com.airfrance.squalecommon.enterpriselayer.facade.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.jraf.commons.exception.JrafEnterpriseException;
import com.airfrance.jraf.helper.PersistenceHelper;
import com.airfrance.jraf.provider.persistence.hibernate.facade.FacadeHelper;
import com.airfrance.jraf.spi.persistence.IPersistenceProvider;
import com.airfrance.jraf.spi.persistence.ISession;
import com.airfrance.squalecommon.daolayer.component.ProjectDAOImpl;
import com.airfrance.squalecommon.datatransfertobject.transform.config.TaskTransform;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.config.TaskRefBO;

/**
 */
public class TaskFacade
{

    /**
     * provider de persistence
     */
    private static final IPersistenceProvider PERSISTENTPROVIDER = PersistenceHelper.getPersistenceProvider();

    /**
     * r�cup�re toutes les taches existantes
     * 
     * @param pProjectId l'id du projet
     * @return la collection de taches
     * @throws JrafEnterpriseException en cas d'�chec
     */
    public static Collection getAllTasks( Long pProjectId )
        throws JrafEnterpriseException
    {

        ISession session = null;
        Collection collBo = new ArrayList();
        Collection collDto = new ArrayList( 0 );
        try
        {
            session = PERSISTENTPROVIDER.getSession();
            // On r�cup�re le projet
            ProjectBO project = (ProjectBO) ProjectDAOImpl.getInstance().get( session, pProjectId );
            /*
             * On va ajouter les t�ches dans leur ordre d'�x�cution c-a-d : 1 - les t�ches d'analyses du source manager
             * 2 - les t�ches d'analyses du profil 3 - les t�ches terminales du profil 4 - les t�ches terminales du
             * source manager
             */
            collBo.addAll( project.getSourceManager().getAnalysisTasks() );
            collBo.addAll( project.getProfile().getAnalysisTasks() );
            collBo.addAll( project.getProfile().getTerminationTasks() );
            collBo.addAll( project.getSourceManager().getTerminationTasks() );
            // on caste chaque �l�ment de la collection de taskBO en TaskDTO
            Iterator it = collBo.iterator();
            while ( it.hasNext() )
            {
                TaskRefBO taskRef = (TaskRefBO) it.next();
                collDto.add( TaskTransform.bo2dto( taskRef ) );
            }
        }
        catch ( JrafDaoException e )
        {
            FacadeHelper.convertException( e, TaskFacade.class.getName() + ".getAllTasks" );
        }
        finally
        {
            FacadeHelper.closeSession( session, TaskFacade.class.getName() + ".getAllTasks" );
        }
        return collDto;
    }

}
