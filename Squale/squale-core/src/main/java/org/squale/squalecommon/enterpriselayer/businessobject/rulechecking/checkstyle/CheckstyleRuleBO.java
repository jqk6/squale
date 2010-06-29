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
package org.squale.squalecommon.enterpriselayer.businessobject.rulechecking.checkstyle;

import java.util.HashSet;
import java.util.Set;

import org.squale.squalecommon.enterpriselayer.businessobject.rulechecking.RuleBO;

/**
 * R�gle checkstyle
 * 
 * @hibernate.subclass discriminator-value="Checkstyle"
 */
public class CheckstyleRuleBO
    extends RuleBO
{

    /**
     * Liste des modules
     */
    protected Set mModules = new HashSet();

    /**
     * Access method for the mModules property.
     * 
     * @return the current value of the mModules property
     * @hibernate.set lazy="true" cascade="all" sort="unsorted" inverse="true"
     * @hibernate.key column="RuleId" on-delete="cascade"
     * @hibernate.one-to-many class="org.squale.squalecommon.enterpriselayer.businessobject.rulechecking.checkstyle.CheckstyleModuleBO"
     */

    public Set getModules()
    {
        return mModules;
    }

    /**
     * Sets the value of the mModules property.
     * 
     * @param pModules the new value of the mModules property
     */

    public void setModules( Set pModules )
    {
        mModules = pModules;
    }

    /**
     * rajoute le module � la liste des modules de la r�gle
     * 
     * @param pModule module checkstyle
     * @throws MissingSeverityException si l'attribut severity est absent du module
     * @throws MismatchSeverityException si l'attribut severity du module est incoh�rent avec la s�v�rit� de la r�gle
     */
    public void addModule( CheckstyleModuleBO pModule )
        throws MissingSeverityException, MismatchSeverityException
    {

        if ( pModule.getSeverity() != null )
        {

            if ( ( mSeverity == null ) )
            {// premi�re module � �tre regroup�e sous cette regle interne

                setSeverity( pModule.getSeverity() );
                pModule.setRule( this );

                mModules.add( pModule );

            }
            else
            {
                // on ne peut regrouper que des modules qui ont la m�me s�verit� sous un m�me code
                if ( mSeverity.equals( pModule.getSeverity() ) )
                {

                    setSeverity( pModule.getSeverity() );
                    pModule.setRule( this );
                    mModules.add( pModule );
                }
                else
                {
                    throw new MismatchSeverityException();
                }
            }

        }
        else
        {
            throw new MissingSeverityException();
        }
    }

    /**
     * Exception de s�v�rit� manquante Lev�e lorsque l'attribut severity est absent
     */
    public class MissingSeverityException
        extends Exception
    {
    }

    /**
     * Exception d'incoh�rence de s�v�rit� Lev�e lorsque la s�v�rit� d'un module checkstyle est incompatible avec celle
     * de la r�gle checkstyle
     */
    public class MismatchSeverityException
        extends Exception
    {
    }
}
