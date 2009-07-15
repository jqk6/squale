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

/**
 * Repr�sente le param�trage de la fr�quence max des audits en fonction des acc�s aux applications
 * 
 * @hibernate.class table="AuditFrequency" lazy="true"
 */
public class AuditFrequencyBO
    implements Comparable
{

    /** L'identifiant (au sens technique) de l'objet */
    private long mId = -1;

    /** Dur�e depuis le dernier acc�s (en nombre de jours) */
    private int mDays;

    /** Fr�quence max d'audit (en nombre de jours) */
    private int mFrequency;

    /**
     * M�thode d'acc�s � mId
     * 
     * @return l'identifiant de l'objet
     * @hibernate.id generator-class="native" type="long" column="AuditFrequencyId" unsaved-value="-1" length="19"
     * @hibernate.generator-param name="sequence" value="auditFrequency_sequence"
     */
    public long getId()
    {
        return mId;
    }

    /**
     * @return la dur�e le depuis dernier acc�s
     * @hibernate.property name="days" column="Nb_days" type="int" length="9" not-null="true" update="true"
     *                     insert="true"
     */
    public int getDays()
    {
        return mDays;
    }

    /**
     * @return la fr�quence max d'audit
     * @hibernate.property name="frequency" column="Frequency" type="int" length="9" not-null="true" update="true"
     *                     insert="true"
     */
    public int getFrequency()
    {
        return mFrequency;
    }

    /**
     * Change la valeur de mId
     * 
     * @param pId la nouvelle valeur de l'identifiant de l'objet
     */
    public void setId( long pId )
    {
        mId = pId;
    }

    /**
     * @param pDays la dur�e le depuis dernier acc�s
     */
    public void setDays( int pDays )
    {
        mDays = pDays;
    }

    /**
     * @param pFrequency la fr�quence max d'audit
     */
    public void setFrequency( int pFrequency )
    {
        mFrequency = pFrequency;
    }

    /**
     * On compare selon le nombre de jours
     * 
     * @param object {@inheritDoc}
     * @return {@inheritDoc}
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo( Object object )
    {
        return new Integer( getDays() ).compareTo( new Integer( ( (AuditFrequencyBO) object ).getDays() ) );
    }
}
