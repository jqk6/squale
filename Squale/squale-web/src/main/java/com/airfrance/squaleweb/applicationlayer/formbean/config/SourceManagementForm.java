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
package com.airfrance.squaleweb.applicationlayer.formbean.config;

import com.airfrance.squalecommon.datatransfertobject.config.SourceManagementDTO;
import com.airfrance.squaleweb.applicationlayer.formbean.RootForm;

/**
 * Contient les donn�es des r�cup�rateurs de sources
 */
public class SourceManagementForm
    extends RootForm
{

    /** Id du r�cup�rateur de sources en String pour le r�cup�rer dans l'option */
    private String mId;

    /** Nom du r�cup�rateur de source */
    private String mName;

    /**
     * Constructeur par d�faut
     */
    public SourceManagementForm()
    {
        super();
    }

    /**
     * Constructeur
     * 
     * @param pSourceManagement le DTO
     */
    public SourceManagementForm( SourceManagementDTO pSourceManagement )
    {
        mId = Long.toString( pSourceManagement.getId() );
        mName = pSourceManagement.getName();
    }

    /**
     * M�thode d'acc�s � mId
     * 
     * @return l'identifiant du r�cup�rateur de source
     */
    public String getId()
    {
        return mId;
    }

    /**
     * M�thode d'acc�s � mName
     * 
     * @return le nom du r�cup�rateur de source
     */
    public String getName()
    {
        return mName;
    }

    /**
     * Change la valeur de mId
     * 
     * @param pId le nouvel identifiant
     */
    public void setId( String pId )
    {
        mId = pId;
    }

    /**
     * Change la valeur de mId
     * 
     * @param pId le nouvel identifiant
     */
    public void setId( long pId )
    {
        mId = Long.toString( pId );
    }

    /**
     * Change la valeur de mName
     * 
     * @param pName le nouveau nom
     */
    public void setName( String pName )
    {
        mName = pName;
    }

}
