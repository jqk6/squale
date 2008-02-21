//Source file: D:\\cc_views\\squale_v0_0_act\\squale\\src\\squalix\\src\\com\\airfrance\\squalix\\core\\TaskUtility.java

package com.airfrance.squalix.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.squalecommon.enterpriselayer.businessobject.config.TaskRefBO;
import com.airfrance.squalix.messages.Messages;

/**
 * Cette classe fournit un certin nombre de m�thodes "utilitaires" utilisables par les
 * toutes les t�ches et le moteur.
 * 
 * @author m400842 (by rose)
 * @version 1.0
 */
public class TaskUtility {

    /**
     * Logger
     */
    private static final Log LOGGER = LogFactory.getLog(TaskUtility.class);

    /**
     * Retourne une instance de la classe de la classe dont le nom
     * est pass� en param�tre.
     * 
     * @param pClassName le nom enti�rement sp�cifi� de la classe.
     * @return l'instance d'une t�che.
     * @throws Exception si un probl�me d'instanciation appara�t.
     * @roseuid 42CAA2A1013B
     */
    private static AbstractTask instanciate(String pClassName) throws Exception {
        AbstractTask task = null;
        try {
            Class theClass = Class.forName(pClassName);
            task = (AbstractTask) theClass.newInstance();
        } catch (Exception e) {
            LOGGER.error(e, e);
            LOGGER.error(Messages.getString("exception.task_not_found") + pClassName);
            task = null;
        }
        return task;
    }

    /**
     * Cr�e un arbre de t�ches � partir des descripteurs d'ex�cution.<br />
     * Pour chaque t�che, une instance est cr��e, et ses enfants lui sont attribu�s.<br />
     * Cette action est fait de mani�re r�cursive sur toutes les t�ches sous la racine du
     * descripteur d'ex�cution. 
     * 
     * @param pTaskRef le nom de la classe de la tache
     * @param pProjectId l'id du projet analys�. Peut-�tre null.
     * @param pAuditId l'id de l'audit pour lequel est ex�cut� la t�che. Peut-�tre null.
     * @param pApplicationId l'id de l'application contenant ce projet
     * @param pData les param�tres temporaires
     * @return la t�che racine.
     * @roseuid 42CD40730176
     */
    public static AbstractTask createTask(TaskRefBO pTaskRef, long pProjectId, long pApplicationId, long pAuditId, TaskData pData) {
        AbstractTask task = null;
        try {
            // Cr�e une nouvelle instance de t�che
            task = instanciate(pTaskRef.getTask().getClassName());
            if (null != task) {
                // On attribue � la t�che les valeurs de ses attributs
                // pattern IOC
                task.setProjectId(new Long(pProjectId));
                task.setAuditId(new Long(pAuditId));
                task.setApplicationId(new Long(pApplicationId));
                task.setStatus(AbstractTask.NOT_ATTEMPTED);
                task.setMandatoryTask(pTaskRef.getTask().isMandatory());
                task.setData(pData);
                task.setTaskParameters(pTaskRef.getParameters());
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return task;
    }

    /**
     * Annule la tache qui a �chou� et celles qui suivaient .<br />
     * Le statut qui leur est attribu� est CANCELLED.
     * 
     * @param pTask t�che � annuler
     * @roseuid 42D22EF8027A
     */
    public static void stopTask(AbstractTask pTask) {
        if (pTask.getStatus() != AbstractTask.RUNNING) {
            pTask.setStatus(AbstractTask.CANCELLED);
        }
    }

}
