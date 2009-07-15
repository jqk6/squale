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
package org.squale.squaleweb.applicationlayer.formbean.component;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import org.squale.squaleweb.applicationlayer.formbean.ActionIdFormSelectable;

/**
 * Contient les donn�es indispensables relatives � un projet
 * 
 * @author M400842
 */
public class ProjectForm
    extends ActionIdFormSelectable
{

    /** Le nombre de niveaux des erreurs */
    public static final int NB_ERRORS_LEVELS = 3;

    /** Index pour acc�der au nombre d'erreurs fatales */
    public static final int ERROR_ID = 0;

    /** Index pour acc�der au nombre de warning */
    public static final int WARNING_ID = 1;

    /** Index pour acc�der au nombre d'information */
    public static final int INFO_ID = 2;

    /** Les erreurs */
    private int[] mErrorsRepartition = new int[NB_ERRORS_LEVELS];

    /** Les t�ches en erreur fatale */
    private List mFailedTasks = new ArrayList( 0 );

    /** l'�ventuelle justification associ�e au composant */
    private String justification;

    /** un bool�en permettant de savoir si le composant est � exclure du plan d'action */
    private boolean excludedFromActionPlan;
    
    /** le langage du projet */
    private String mLanguage;
    
    /**
     * @return true si le composant est exclu du plan d'action
     */
    public boolean getExcludedFromActionPlan()
    {
        return excludedFromActionPlan;
    }

    /**
     * @return la justification du composant
     */
    public String getJustification()
    {
        return justification;
    }

    /**
     * @param pExcluded le bool�en indiquant si il faut exclure le composant ou pas
     */
    public void setExcludedFromActionPlan( boolean pExcluded )
    {
        excludedFromActionPlan = pExcluded;
    }

    /**
     * @param pJustification la nouvelle valeur de la justification
     */
    public void setJustification( String pJustification )
    {
        justification = pJustification;
    }

    /**
     * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping,
     *      javax.servlet.http.HttpServletRequest {@inheritDoc} A impl�menter sinon on ne peut pas d�cocher la checkBox
     */
    public void reset( ActionMapping mapping, HttpServletRequest request )
    {
        super.reset( mapping, request );
        // Reinitialisation du checkbox
        excludedFromActionPlan = false;
    }

    /**
     * @return l'id de l'application parent
     */
    public long getParentId()
    {
        long result = -1;
        if ( !getApplicationId().equals( "" ) )
        {
            result = new Long( getApplicationId() ).longValue();
        }
        return result;
    }

    /**
     * @return le nombre d'erreurs li�s � ce projet
     */
    public int getNbOfErrors()
    {
        return mErrorsRepartition[0] + mErrorsRepartition[1] + mErrorsRepartition[2];
    }

    /**
     * @return les erreurs li�s � ce projet
     */
    public int[] getErrorsRepartition()
    {
        return mErrorsRepartition;
    }

    /**
     * @param pErrors la nouvelle r�partition des erreurs
     */
    public void setErrors( int[] pErrors )
    {
        mErrorsRepartition = pErrors;
    }

    /**
     * un bool�en juste pour indiquer si le projet a un audit termin�, n'est pas stock� en base sert juste pour
     * l'affichage
     */
    private boolean hasTerminatedAudit;

    /**
     * @return le bool�en indiquant si le projet a un audit termin�
     */
    public boolean getHasTerminatedAudit()
    {
        return hasTerminatedAudit;
    }

    /**
     * @param pHasTerminatedAudit le bool�en indiquant si le projet a un audit termin�
     */
    public void setHasTerminatedAudit( boolean pHasTerminatedAudit )
    {
        hasTerminatedAudit = pHasTerminatedAudit;
    }

    /**
     * @return les t�ches en �chec
     */
    public List getFailedTasks()
    {
        return mFailedTasks;
    }

    /**
     * @param pTasks les t�ches en �chec
     */
    public void setFailedTasks( List pTasks )
    {
        mFailedTasks = pTasks;
    }
    
    /**
     * @return le language du projet
     */
    public String getLanguage()
    {
        return mLanguage;
    }

    /**
     * @param pLanguage le langage du projet
     */
    public void setLanguage( String pLanguage )
    {
        mLanguage = pLanguage;
    }

}
