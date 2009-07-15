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
package org.squale.squalecommon.datatransfertobject.config;

import java.util.ArrayList;
import java.util.Collection;

/**
 * T�che Squalix Les param�tres de la t�che squalix sont plac�s dans ce DTO : le taskDTO est en fait un merge entre les
 * classes TaskRefBO et TaskBO
 */
public class TaskDTO
{

    /**
     * Identifiant (au sens technique) de l'objet
     */
    private long mId;

    /** Le nom de la t�che */
    private String mName;

    /** La classe correspondante � la t�che */
    private String mClassName;

    /** Configuration possible de la t�che */
    private boolean mConfigurable;

    /** Configuration standard ou non */
    private boolean mStandard;

    /** indique si la t�che est obligatoire */
    private boolean mMandatory;

    /** Param�tres de la t�che sous la forme de TaskParameterDTO */
    private Collection mParameters = new ArrayList();

    /**
     * M�thode d'acc�s pour mId
     * 
     * @return la l'identifiant (au sens technique) de l'objet
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
     * M�thode d'acc�s � mName
     * 
     * @return le nom de la t�che
     */
    public String getName()
    {
        return mName;
    }

    /**
     * Change la valeur de mName
     * 
     * @param pName le nouveau nom de la t�che
     */
    public void setName( String pName )
    {
        mName = pName;
    }

    /**
     * M�thode d'acc�s � mClassName
     * 
     * @return le nom de la classe associ�e � la t�che
     */
    public String getClassName()
    {
        return mClassName;
    }

    /**
     * Change la valeur de mClassName
     * 
     * @param pClassName la nouvelle classe associ�e
     */
    public void setClassName( String pClassName )
    {
        mClassName = pClassName;
    }

    /**
     * M�thode d'acc�s � mConfigurable
     * 
     * @return true si la t�che est configurable
     */
    public boolean isConfigurable()
    {
        return mConfigurable;
    }

    /**
     * Change la valeur de mConfigurable
     * 
     * @param pConfigurable la nouvelle valeur de mConfigurable
     */
    public void setConfigurable( boolean pConfigurable )
    {
        mConfigurable = pConfigurable;
    }

    /**
     * Ajout d'un param�tre
     * 
     * @param pParameter param�tre
     */
    public void addParameter( TaskParameterDTO pParameter )
    {
        mParameters.add( pParameter );
    }

    /**
     * @return param�tres
     */
    public Collection getParameters()
    {
        return mParameters;
    }

    /**
     * @return true si la configuration est standard
     */
    public boolean isStandard()
    {
        return mStandard;
    }

    /**
     * @param pStandard true si la configuration est standard
     */
    public void setStandard( boolean pStandard )
    {
        mStandard = pStandard;
    }

    /**
     * @return true si la t�che est obligatoire
     */
    public boolean isMandatory()
    {
        return mMandatory;
    }

    /**
     * @param pMandatory true si la t�che est obligatoire
     */
    public void setMandatory( boolean pMandatory )
    {
        mMandatory = pMandatory;
    }

}
