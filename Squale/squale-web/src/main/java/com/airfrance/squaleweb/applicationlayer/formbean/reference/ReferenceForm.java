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
package com.airfrance.squaleweb.applicationlayer.formbean.reference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

import com.airfrance.squaleweb.applicationlayer.formbean.ActionIdFormSelectable;

/**
 * Contient une r�f�rence du portail Squale.
 * 
 * @author M400842
 */
public class ReferenceForm
    extends ActionIdFormSelectable
{

    // Constantes pour l'affichage

    /**
     * Masqu�
     */
    public final static String HIDDEN = "reference.hidden";

    /**
     * Affich�
     */
    public final static String DISPLAYED = "reference.displayed";

    /**
     * Caract�re public
     */
    private boolean mPublic;

    /** le type de l'audit */
    private String mAuditType;

    /**
     * Liste des facteurs du referentiel
     */
    private Collection mFactors = new ArrayList();

    /**
     * Liste des donn�es de volum�trie du r�f�rentiel
     */
    private TreeMap<String,Integer> mVolumetry = new TreeMap<String,Integer>();
    
    /**
     * Technologie de la r�f�rence
     */
    private String mTechnology;

    /**
     * Nom du la r�f�rence
     */
    private String mName = "";

    /**
     * Sp�cifie si la r�f�rence est cach�e
     */
    private boolean mHidden = false;
    
    /** Attribut inutilis� pour �viter des erreurs **/
    private String mValue="";
    
    /**
     * @return une valeur vide
     */
    public String getValue()
    {
    	return mValue;
    }

    /**
     * @return le nom
     */
    public String getName()
    {
        return mName;
    }

    /**
     * @return la technologie
     */
    public String getTechnology()
    {
        return mTechnology;
    }

    /**
     * @param pName le nom
     */
    public void setName( String pName )
    {
        mName = pName;
    }

    /**
     * @param pTechnology la technologie
     */
    public void setTechnology( String pTechnology )
    {
        mTechnology = pTechnology;
    }

    /**
     * @return l'�tat de validation de la r�f�rence
     */
    public boolean isHidden()
    {
        return mHidden;
    }

    /**
     * @param pHidden indique si la r�f�rence est masqu�e ou pas
     */
    public void setHidden( boolean pHidden )
    {
        mHidden = pHidden;
    }

    /**
     * @return Valeur courante de mFactors
     */
    public Collection getFactors()
    {
        return mFactors;
    }

    /**
     * @param pPublic caract�re public
     */
    public void setPublic( boolean pPublic )
    {
        mPublic = pPublic;
    }

    /**
     * @return caract�re public
     */
    public boolean isPublic()
    {
        return mPublic;
    }

    /**
     * @param pFactor facteur
     */
    public void addFactor( Object pFactor )
    {
        mFactors.add( pFactor );

    }

    /**
     * @return le type de l'audit
     */
    public String getAuditType()
    {
        return mAuditType;
    }

    /**
     * @param pType le type de l'audit
     */
    public void setAuditType( String pType )
    {
        mAuditType = pType;
    }

    /**
     * string d�crivant l'�tat, sert juste pour l'affichage
     */
    private String mState = "";

    /**
     * @param pState le nouvel �tat
     */
    public void setState( String pState )
    {
        mState = pState;
    }

    /**
     * M�thode renvoyant la string d�crivant l'�tat, sert juste pour l'affichage
     * 
     * @return l'�tat
     */
    public String getState()
    {
        return mState;
    }
    
    /**
     * M�thode renvoyant les donn�es de volum�trie
     * @return les donn�es (Nb Classes,M�thodes,etc...)
     */
    public TreeMap getVolumetry()
    {
    	return mVolumetry;
    }
    
    public void setVolumetry(TreeMap pVolumetry)
    {
    	mVolumetry = pVolumetry;
    }
    
    public Integer getVolume(String pKey)
    {
    	Integer volume = mVolumetry.get(pKey);
    	return volume;
    }
    
}
