//Source file: D:\\cc_views\\squale_v0_0_act\\squale\\src\\squalix\\src\\com\\airfrance\\squalix\\tools\\mccabe\\McCabePersistor.java

package com.airfrance.squalix.tools.mccabe;

import com.airfrance.jraf.spi.persistence.ISession;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squalix.core.TaskData;

/**
 * Objet charg� de faire persister les composants identifi�s par McCabe ainsi que les r�sultats.
 */
public class McCabePersistor
{
    /**
     * Session Persistance
     */
    protected ISession mSession;

    /** les param�tres temporaires */
    protected TaskData mDatas;

    /** le nom de la tache r�el, pour diff�rencier java et cpp */
    protected String mTaskName;

    /**
     * Audit durant lequel l'analyse est effectu�e
     */
    protected AuditBO mAudit;

    /**
     * Configuration
     */
    protected McCabeConfiguration mConfiguration;

    /**
     * Projet sur lequel est r�alis�e l'analyse.
     */
    protected ProjectBO mProject;

    /**
     * Constructeur.
     * @param pSession la session de persistance utilis�e par la t�che.
     * @param pDatas la liste des param�tres temporaires du projet
     * @param pTaskName le nom de la tache (pour diff�rencier java et cpp)
     * @param pAudit audit encadrant l'ex�cution.
     * @param pConfiguration configuration du framework.
     */
    public McCabePersistor(final ISession pSession, final TaskData pDatas, final String pTaskName,
                           final AuditBO pAudit, final McCabeConfiguration pConfiguration) {
        mSession = pSession;
        mDatas = pDatas;
        mTaskName = pTaskName;
        mAudit = pAudit;
        mConfiguration = pConfiguration;
        mProject = pConfiguration.getProject();
    }
    
}
