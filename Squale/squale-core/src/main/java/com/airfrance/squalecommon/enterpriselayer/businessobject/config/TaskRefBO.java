package com.airfrance.squalecommon.enterpriselayer.businessobject.config;

import java.util.ArrayList;
import java.util.Collection;

/**
 * R�f�rence de t�che *
 * 
 * @hibernate.class table="TaskRef" lazy="true"
 */
public class TaskRefBO
{
    /**
     * Identifiant (au sens technique) de l'objet
     */
    protected long mId = -1;

    /**
     * T�che
     */
    protected TaskBO mTask;

    /**
     * Param�tres de la t�che
     */
    private Collection mParameters = new ArrayList();

    /**
     * M�thode d'acc�s pour mId
     * 
     * @return la l'identifiant (au sens technique) de l'objet
     * @hibernate.id generator-class="native" type="long" column="TaskRefId" unsaved-value="-1" length="19"
     * @hibernate.generator-param name="sequence" value="taskRef_sequence"
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
     * R�cup�re la collection des param�tres
     * 
     * @return les param�tres
     * @hibernate.bag table="TaskParameter" lazy="true" cascade="all"
     * @hibernate.collection-key column="TaskRefId"
     * @hibernate.collection-one-to-many class="com.airfrance.squalecommon.enterpriselayer.businessobject.config.TaskParameterBO"
     */
    public Collection getParameters()
    {
        return mParameters;
    }

    /**
     * @param pParameters param�tres
     */
    public void setParameters( Collection pParameters )
    {
        mParameters = pParameters;
    }

    /**
     * Ajout d'un Parameter
     * 
     * @param pName nom
     * @param pValue valeur
     */
    public void addParameter( String pName, String pValue )
    {
        TaskParameterBO arg = new TaskParameterBO();
        arg.setName( pName );
        arg.setValue( pValue );
        getParameters().add( arg );
    }

    /**
     * @return t�che
     * @hibernate.many-to-one column="TaskId" cascade="all"
     *                        class="com.airfrance.squalecommon.enterpriselayer.businessobject.config.TaskBO"
     */
    public TaskBO getTask()
    {
        return mTask;
    }

    /**
     * @param pTask t�che
     */
    public void setTask( TaskBO pTask )
    {
        mTask = pTask;
    }
}
