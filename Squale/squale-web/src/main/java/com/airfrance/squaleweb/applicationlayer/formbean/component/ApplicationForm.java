package com.airfrance.squaleweb.applicationlayer.formbean.component;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.airfrance.squaleweb.applicationlayer.formbean.ActionIdFormSelectable;

/**
 * Contient les donn�es indispensables relatives � une application
 * 
 * @author M400842
 */
public class ApplicationForm
    extends ActionIdFormSelectable
{

    /** l'�ventuelle justification associ�e au composant */
    private String justification;

    /** un bool�en permettant de savoir si le composant est � exclure du plan d'aciton */
    private boolean excludedFromActionPlan;

    /** indique si le composant a des r�sultats */
    private boolean mHasResults;

    /** Date de la derni�re modification */
    private Date mLastUpdate;

    /** L'utilisateur ayant fait la derni�re modification */
    private String mLastUser;

    /**
     * @return true si le composant est exclu du plan d'action
     */
    public boolean getExcludedFromActionPlan()
    {
        return excludedFromActionPlan;
    }

    /**
     * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping,
     *      javax.servlet.http.HttpServletRequest A impl�menter sinon on ne peut pas d�cocher la checkBox
     * @param mapping le mapping
     * @param request la requ�te
     */
    public void reset( ActionMapping mapping, HttpServletRequest request )
    {
        super.reset( mapping, request );
        // Reinitialisation du checkbox
        excludedFromActionPlan = false;
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
     * Redefinition of the hashCode method
     * {@inheritDoc} 
     * @return return the hash number of the object
     */
    public int hashCode(){
        return super.hashCode();
    }

    
    
    /**
     * @see java.lang.Object#equals(java.lang.Object)
     * @param obj l'objet � comparer
     * @return true si obj=this, false sinon
     */
    public boolean equals( Object obj )
    {
        boolean result = false;
        if ( obj instanceof ApplicationForm )
        {
            ApplicationForm compare = (ApplicationForm) obj;
            if ( null != this.getApplicationName() )
            {
                result = this.getApplicationName().equals( compare.getApplicationName() );
            }
        }
        return result;
    }
    
    

    /**
     * @return true si le composant a des r�sultats
     */
    public boolean getHasResults()
    {
        return mHasResults;
    }

    /**
     * @param pHasResults indique si le composant a des r�sultats
     */
    public void setHasResults( boolean pHasResults )
    {
        mHasResults = pHasResults;
    }

    /**
     * @return la date de la derni�re modification
     */
    public Date getLastUpdate()
    {
        return mLastUpdate;
    }

    /**
     * @param pDate la date de la derni�re modification
     */
    public void setLastUpdate( Date pDate )
    {
        mLastUpdate = pDate;
    }

    /**
     * @return l'utilisateur ayant fait la derni�re modification
     */
    public String getLastUser()
    {
        return mLastUser;
    }

    /**
     * @param pMatricule l'utilisateur ayant fait la derni�re modification
     */
    public void setLastUser( String pMatricule )
    {
        mLastUser = pMatricule;
    }

}
