package com.airfrance.squalecommon.datatransfertobject.component;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * Composant
 */
public class ComponentDTO implements Serializable {

    /**
     * ID en base du composant
     */
    private long mID = -1;

    /**
     * Nom du composant
     */
    private String mName = null;
    
    /**
     * Le nom complet du composant
     */
    private String mFullName = null;

    /**
     * Le num�ro de ligne de la m�thode dans le fichier (0 par d�faut)
     */
    private String mStartLine = "0";

    /**
     * Type du composant (package, classe, m�thode)
     */
    private String mType = null;

    /**
     * Nom du fichier dans le cas d'une m�thode
     */
    private String mFileName = null;

    /**
     * identifiant du composant parent
     */
    private long mIDParent = -1;

    /**
     * Collection d'identifiants des composants fils
     */
    private int mNumberOfChildren = -1;

    /**
     * Technologie du composant s'il s'agit d'un projet
     */
    private String mTechnology = null;

    /** l'�ventuelle justification associ�e au composant */
    private String justification;

    /** un bool�en permettant de savoir si le composant est � exclure du plan d'action */
    private boolean excludedFromActionPlan;
    
    /** indique si le composant a des r�sultats */
    private boolean mHasResults;

    /**
     * Date de derniere modification par un utilisateur quelconque
     */
    private Date mLastUpdate;
    
    /**
     * Nom du dernier utilisateur ayant modifi� l'application
     */
    private String mLastUser;

    /**
     * @return true si le composant est exclu du plan d'action
     */
    public boolean getExcludedFromActionPlan() {
        return excludedFromActionPlan;
    }

    /**
     * @return la justification du composant
     */
    public String getJustification() {
        return justification;
    }

    /**
     * @param pExcluded le bool�en indiquant si il faut exclure le composant ou pas
     */
    public void setExcludedFromActionPlan(boolean pExcluded) {
        excludedFromActionPlan = pExcluded;
    }

    /**
     * @param pJustification la nouvelle valeur de la justification
     */
    public void setJustification(String pJustification) {
        justification = pJustification;
    }

    /**
     * Constructeur par d�faut
     * @roseuid 42CB915F02F4
     */
    public ComponentDTO() {
        // initialisation des valeurs par d�faut
        excludedFromActionPlan = false;
        justification = "";
    }

    /**
     * Access method for the mName property.
     * 
     * @return   the current value of the mName property
     * @roseuid 42CB915F033A
     */
    public String getName() {
        return mName;
    }

    /**
     * Sets the value of the mName property.
     * 
     * @param pName the new value of the mName property
     * @roseuid 42CB915F0344
     */
    public void setName(String pName) {
        mName = pName;
    }

    /**
     * Access method for the mType property.
     * 
     * @return   the current value of the mType property
     * @roseuid 42CB9160001A
     */
    public String getType() {
        return mType;
    }

    /**
     * Sets the value of the mType property.
     * 
     * @param pType the new value of the mType property
     * @roseuid 42CB9160006A
     */
    public void setType(String pType) {
        mType = pType;
    }

    /**
     * Access method for the mFileName property.
     * 
     * @return   the current value of the mFileName property
     */
    public String getFileName() {
        return mFileName;
    }

    /**
     * Sets the value of the mFileName property.
     * 
     * @param pFileName the new value of the mFileName property
     */
    public void setFileName(String pFileName) {
        mFileName = pFileName;
    }

    /**
     * Access method for the mID property.
     * 
     * @return   the current value of the mID property
     * @roseuid 42CB916000A6
     */
    public long getID() {
        return mID;
    }

    /**
     * Sets the value of the mID property.
     * 
     * @param pID the new value of the mID property
     * @roseuid 42CB91600100
     */
    public void setID(long pID) {
        mID = pID;
    }

    /**
     * Permet de construire un ComponentDTO par rapport � un id et un composant.
     * Doit etre fait pour manipuler des projets et sous-projets en tant que 
     * composant.
     * @param pID ID du composant
     * @param pName Nom du composant
     * @roseuid 42D4C6A90348
     */
    public ComponentDTO(long pID, String pName) {
        setID(pID);
        setName(pName);
    }

    /**
     * Access method for the mIDParent property.
     * 
     * @return   the current value of the mIDParent property
     */
    public long getIDParent() {
        return mIDParent;
    }

    /**
     * Sets the value of the mIDParent property.
     * 
     * @param pIDParent the new value of the mIDParent property
     */
    public void setIDParent(long pIDParent) {
        mIDParent = pIDParent;
    }

    /**
     * {@inheritDoc}
     * @param pObj le ComponentDTO � comparer
     * @return true si <code>this</code>et <code>pObj</code> sont �gaux 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object pObj) {
        boolean ret = false;
        EqualsBuilder eqBuilder = new EqualsBuilder();
        if (pObj instanceof ComponentDTO) {
            ComponentDTO comp = (ComponentDTO) pObj;
            eqBuilder.append(mID, comp.getID()); // pas beau
            eqBuilder.append(mIDParent, comp.getIDParent());
            eqBuilder.append(mName, comp.getName());
            eqBuilder.append(mType, comp.getType());

            ret = eqBuilder.isEquals();
        }
        return ret;
    }
    
    /**
     * Constructeur
     * @param pId l'id
     * @param pIdParent l'id du parent
     * @param pName le nom
     * @param pType le type
     */
    public ComponentDTO(long pId, long pIdParent, String pName, String pType) {
        mID = pId;
        mIDParent = pIdParent;
        mName = pName;
        mType = pType;
    }

    /**
     * Access method for the mNumberOfChildren property.
     * 
     * @return   the current value of the mNumberOfChildren property
     */
    public int getNumberOfChildren() {
        return mNumberOfChildren;
    }

    /**
     * Sets the value of the mNumberOfChildren property.
     * 
     * @param pNumberOfChildren the new value of the mNumberOfChildren property
     */
    public void setNumberOfChildren(int pNumberOfChildren) {
        mNumberOfChildren = pNumberOfChildren;
    }

    /**
     * @return technologie du composant s'il s'agit d'un projet
     */
    public String getTechnology() {
        return mTechnology;
    }

    /**
     * @param pTechnology du composant s'il s'agit d'un projet
     */
    public void setTechnology(String pTechnology) {
        mTechnology = pTechnology;
    }

    /** 
     * {@inheritDoc}
     * @see java.lang.Object#hashCode()
     * @return le hashcode
     */
    public int hashCode() {
        int result;
        if (getName() != null) {
            result = getName().hashCode();
        } else {
            result = super.hashCode();
        }
        return result;
    }

    /**
     * @return true si le composant a des r�sultats
     */
    public boolean getHasResults() {
        return mHasResults;
    }

    /**
     * @param pHasResults indique si le composant a des r�sultats
     */
    public void setHasResults(boolean pHasResults) {
        mHasResults = pHasResults;
    }

    /**
     * @return le nom complet
     */
    public String getFullName() {
        return mFullName;
    }

    /**
     * @param pFullName le nom complet
     */
    public void setFullName(String pFullName) {
        mFullName = pFullName;
    }

    /**
     * @return le dernier utilisateur ayant modifi� l'application
     */
    public String getLastUser() {
        return mLastUser;
    }

    /**
     * @param pUser le dernier utilisateur ayant modifi� l'application
     */
    public void setLastUser(String pUser) {
        mLastUser = pUser;
    }

    /**
     * @return la date de derni�re modification
     */
    public Date getLastUpdate() {
        return mLastUpdate;
    }

    /**
     * @param pDate la date de derni�re modification
     */
    public void setLastUpdate(Date pDate) {
        mLastUpdate = pDate;
    }

    /**
     * @return le num�ro de ligne de la m�thode dans le fichier
     */
    public String getStartLine() {
        return mStartLine;
    }

    /**
     * @param pLine le num�ro de ligne de la m�thode dans le fichier
     */
    public void setStartLine(String pLine) {
        mStartLine = pLine;
    }

}
