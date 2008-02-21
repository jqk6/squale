package com.airfrance.squalecommon.enterpriselayer.facade.config.xml;

import java.util.Hashtable;

import org.xml.sax.Attributes;

import com.airfrance.squalecommon.enterpriselayer.businessobject.config.TaskBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.config.TaskRefBO;
import com.airfrance.squalecommon.util.xml.FactoryAdapter;


/**
 * Fabrique de r�f�rence de pratique
 *
 */
class TaskRefFactory extends FactoryAdapter {
    
    /** T�ches */
    private Hashtable mTasks;
    
    /**
     * Constructeur
     * @param pTasks t�ches existantes
     */
    public TaskRefFactory(Hashtable pTasks) {
        mTasks = pTasks;
    }
    /** (non-Javadoc)
     * @see org.apache.commons.digester.ObjectCreationFactory#createObject(org.xml.sax.Attributes)
     */
    public Object createObject(Attributes attributes) throws Exception {
        String name = attributes.getValue("name");
        TaskBO task = (TaskBO) mTasks.get(name);
        if (task == null) {
            // D�tection d'objet inexistant
            throw new Exception(XmlConfigMessages.getString("task.unknown", new Object[]{name}));
        }
        TaskRefBO ref = new TaskRefBO();
        ref.setTask(task);
        return ref;
    }

}