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
package org.squale.squalecommon.enterpriselayer.businessobject.config;

import java.util.HashSet;
import java.util.Set;

import org.squale.squalecommon.enterpriselayer.businessobject.config.web.AbstractDisplayConfBO;
import org.squale.squalecommon.enterpriselayer.businessobject.rule.QualityGridBO;

/**
 * Profile d'un projet
 * 
 * @hibernate.subclass lazy="true" discriminator-value="Profile"
 */
public class ProjectProfileBO
    extends AbstractTasksUserBO
{

    /** les configurations d'affichage */
    private Set mProfileDisplayConfs = new HashSet();

    /** les grilles associ�es */
    private Set mGrids = new HashSet();

    /** true si l'export IDE est possible avec ce profil */
    private boolean mExportIDE = true;
    
    /** le langage du profil du projet */
    private String mLanguage = null;
    
    /**
     * R�cup�re les configurations d'affichage li�es � ce profil
     * 
     * @return les configurations
     * @hibernate.set table="Profiles_DisplayConfs" lazy="true" cascade="all" inverse="false" sort="unsorted"
     * @hibernate.key column="ProfileId"
     * @hibernate.one-to-many class="org.squale.squalecommon.enterpriselayer.businessobject.config.Profile_DisplayConfBO"
     */
    public Set getProfileDisplayConfs()
    {
        return mProfileDisplayConfs;
    }

    /**
     * R�cup�re les grilles li�es � ce profil
     * 
     * @return les grilles
     * @hibernate.set table="Profiles_Grids" lazy="true" sort="unsorted" cascade="none"
     * @hibernate.key column="ProfileId"
     * @hibernate.many-to-many class="org.squale.squalecommon.enterpriselayer.businessobject.rule.QualityGridBO"
     *                         column="GridId" outer-join="auto"
     */
    public Set getGrids()
    {
        return mGrids;
    }

    /**
     * @hibernate.property name="exportIDE" column="export_IDE" type="boolean" unique="false" update="true"
     *                     insert="true"
     * @return true si le profil permet l'export IDE
     */
    public boolean getExportIDE()
    {
        return mExportIDE;
    }

    /**
     * @param pExport true si le profil permet l'export IDE
     */
    public void setExportIDE( boolean pExport )
    {
        mExportIDE = pExport;
    }

    /**
     * @hibernate.property name="language" column="language" type="string" unique="false" update="true"
     *                     insert="true" length="255"
     * @return le langage du profil du projet
     */
    public String getLanguage()
    {
        return mLanguage;
    }

    /**
     * @param pLanguage le language du profil du projet
     */
    public void setLanguage( String pLanguage )
    {
        mLanguage = pLanguage;
    }  
    
    /**
     * @param pProfileDisplayConfs les configurations d'affichage li�es � ce profil
     */
    public void setProfileDisplayConfs( Set pProfileDisplayConfs )
    {
        mProfileDisplayConfs = pProfileDisplayConfs;
    }

    /**
     * @param pDisplayConf la configuration � ajouter
     */
    public void addProfileDisplayConf( AbstractDisplayConfBO pDisplayConf )
    {
        Profile_DisplayConfBO confLink = new Profile_DisplayConfBO();
        confLink.setDisplayConf( pDisplayConf );
        confLink.setProfile( this );
        mProfileDisplayConfs.add( confLink );
    }

    /**
     * @param pGrids les grilles disponibles pour ce profil
     */
    public void setGrids( Set pGrids )
    {
        mGrids = pGrids;
    }

    /**
     * @param pGrid la grille � ajouter
     */
    public void addGrid( QualityGridBO pGrid )
    {
        mGrids.add( pGrid );
    }
}
