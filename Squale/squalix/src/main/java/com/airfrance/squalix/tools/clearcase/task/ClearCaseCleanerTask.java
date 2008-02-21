//Source file: D:\\cc_views\\squale_v0_0_act\\squale\\src\\squalix\\src\\com\\airfrance\\squalix\\tools\\clearcase\\ClearCaseCleanerTask.java

package com.airfrance.squalix.tools.clearcase.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.squalix.core.AbstractTask;
import com.airfrance.squalix.core.TaskException;
import com.airfrance.squalix.tools.clearcase.configuration.ClearCaseConfiguration;
import com.airfrance.squalix.tools.clearcase.configuration.ClearCaseMessages;
import com.airfrance.squalix.util.process.ProcessErrorHandler;
import com.airfrance.squalix.util.process.ProcessManager;

/**
 * Nettoie l'environnement de travail apr�s avoir l'analyse compl�te d'un 
 * projet.<br>
 * D�monte la vue ClearCase notamment.<br>
 * Elle est la derni�re t�che ex�cut�e sur un projet avant le calcul des 
 * r�sultats qualit�.
 * @author m400832
 * @version 2.1
 */
public class ClearCaseCleanerTask extends AbstractTask implements ProcessErrorHandler {

    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactory.getLog(ClearCaseCleanerTask.class);

    /**
     * Instance de configuration.
     */
    private ClearCaseConfiguration mConfiguration;

    /**
     * Constructeur.
     * @roseuid 42B97E86009E
     */
    public ClearCaseCleanerTask() {
        mName = "ClearCaseCleanerTask";
    }
    
    // Cette tache n'a pas d'influence dans le calcul de la taille max du file system

    /**
     * M�thode de lancement de la t�che de d�montage.
     * @throws TaskException en cas d'�chec
     *  @roseuid 42AE95AB03C8
     */
    public void execute() throws TaskException {
        /* r�cup�re la configuration clearcase */
        try {
            mConfiguration = new ClearCaseConfiguration(mProject, mAudit);
            /* d�montage */
            umount();
        } catch (Exception e) {
            throw new TaskException(e);
        }
    }

    /**
     * M�thode cr��e pour �viter une pieuvre.
     * @see #cleanView()
     * @throws Exception exception.
     */
    private void umount() throws Exception {
        /* suppression de la vue */
        if (cleanView()) {
            LOGGER.info(ClearCaseMessages.getString("logs.view.umounted"));

            /* si la vue n'a pu �tre d�mont�e */
        } else {
            /* exception lanc�e */
            throw new Exception(ClearCaseMessages.getString("exception.view.umount_failed"));
        }
    }

    /**
     * Supprime / d�monte la vue ClearCase
     * @throws Exception si la commande UNIX produit des erreurs.
     * @return <code>true</code> si la vue est correctement d�mont�e, <code>
     * false</code> sinon.
     */
    protected boolean cleanView() throws Exception {
        /* instanciation du process et ex�cution */
        ProcessManager mgr = new ProcessManager(mConfiguration.getUmountViewCommand(), null, null);

        /* si le processus se termine correctement */
        return (mgr.startProcess(this) == 0);
    }
    /** (non-Javadoc)
     * @see com.airfrance.squalix.util.process.ProcessErrorHandler#processError(java.lang.String)
     */
    public void processError(String pErrorMessage) {
        initError(pErrorMessage);
    }
}
