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
package org.squale.squalecommon.datatransfertobject.rulechecking;

import org.squale.squalecommon.datatransfertobject.component.ComponentDTO;

/**
 * DTO pour les items des transgressions
 */
public class RuleCheckingItemDTO
{

    /** D�tail de la transgression */
    private String mMessage;

    /** Composant concern� par la transgression */
    private ComponentDTO mComponent;

    /** Composant en relation avec la transgression */
    private ComponentDTO mComponentInvolved;

    /** S�v�rit� de la r�gle */
    private String mRuleSeverity;

    /** Code de la r�gle */
    private String mRuleCode;

    /** Chemin du fichier consern� */
    private String mComponentFile;

    /** Num�ro de la ligne */
    private int mLine;

    /**
     * @return le d�tail de la transgession
     */
    public String getMessage()
    {
        return mMessage;
    }

    /**
     * @return le composant concern� par la transgression
     */
    public ComponentDTO getComponent()
    {
        return mComponent;
    }

    /**
     * @return le composant en relation avec la transgression
     */
    public ComponentDTO getComponentInvolved()
    {
        return mComponentInvolved;
    }

    /**
     * Modifie mMessage
     * 
     * @param pMessage le d�tail de la transgession
     */
    public void setMessage( String pMessage )
    {
        mMessage = pMessage;
    }

    /**
     * Modifie mComponent
     * 
     * @param pComponent le composant concern� par la transgression
     */
    public void setComponent( ComponentDTO pComponent )
    {
        mComponent = pComponent;
    }

    /**
     * Modifie mComponentInvolved
     * 
     * @param pComponentInvolved le composant en relation avec la transgression
     */
    public void setComponentInvolved( ComponentDTO pComponentInvolved )
    {
        mComponentInvolved = pComponentInvolved;
    }

    /**
     * @return le code de la r�gle
     */
    public String getRuleCode()
    {
        return mRuleCode;
    }

    /**
     * @return la s�v�rit� de la r�gle
     */
    public String getRuleSeverity()
    {
        return mRuleSeverity;
    }

    /**
     * @param pRuleCode la code de la r�gle
     */
    public void setRuleCode( String pRuleCode )
    {
        mRuleCode = pRuleCode;
    }

    /**
     * @param pRuleSeverity la s�v�rit� de la r�gle
     */
    public void setRuleSeverity( String pRuleSeverity )
    {
        mRuleSeverity = pRuleSeverity;
    }

    /**
     * @return le chemin du fichier
     */
    public String getComponentFile()
    {
        return mComponentFile;
    }

    /**
     * @return le num�ro de ligne
     */
    public int getLine()
    {
        return mLine;
    }

    /**
     * @param pComponentFile le chemin du fichier
     */
    public void setComponentFile( String pComponentFile )
    {
        mComponentFile = pComponentFile;
    }

    /**
     * @param pLine le num�ro de ligne
     */
    public void setLine( int pLine )
    {
        mLine = pLine;
    }
}
