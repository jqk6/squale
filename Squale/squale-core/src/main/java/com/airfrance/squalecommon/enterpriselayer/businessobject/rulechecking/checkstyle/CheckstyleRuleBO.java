package com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking.checkstyle;

import java.util.HashSet;
import java.util.Set;

import com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking.RuleBO;

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
     * @hibernate.set lazy="true" cascade="all" sort="unsorted"
     * @hibernate.key column="RuleId"
     * @hibernate.one-to-many class="com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking.checkstyle.CheckstyleModuleBO"
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
