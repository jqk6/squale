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
package com.airfrance.squaleweb.applicationlayer.formbean.results;

import com.airfrance.squaleweb.applicationlayer.formbean.ActionIdForm;

/**
 * Bean pour une transgression
 */
public class RulesCheckingForm
    extends ActionIdForm
{

    /**
     * Nom la R�gle
     */
    protected String mNameRule;

    /**
     * La s�v�rit� de la r�gle
     */
    protected String mSeverity;

    /**
     * La s�v�rit� de la r�gle pour l'affichage
     */
    protected String mSeverityLang;

    /**
     * La version de grille de r�gle
     */
    protected String mVersion;

    /**
     * Nombre de transgressions de la r�gle
     */
    protected int mTransgressionsNumber;

    /**
     * Id de la mesure
     */
    protected long mMeasureID;

    /**
     * Constructeur par d�faut
     */
    public RulesCheckingForm()
    {
        mMeasureID = -1;
    }

    /**
     * @return l'id de la r�gle
     */
    public long getId()
    {
        return mId;
    }

    /**
     * @return nom de la r�gle
     */
    public String getNameRule()
    {
        return mNameRule;
    }

    /**
     * @return sev�rit� de la r�gle
     */
    public String getSeverity()
    {
        return mSeverity;
    }

    /**
     * @return sev�rit� de la r�gle traduite
     */
    public String getSeverityLang()
    {
        return mSeverityLang;
    }

    /**
     * @return le nombre de trasgression de la r�gle
     */
    public int getTransgressionsNumber()
    {
        return mTransgressionsNumber;
    }

    /**
     * @return la version
     */
    public String getVersion()
    {
        return mVersion;
    }

    /**
     * @return l'id de la mesure
     */
    public long getMeasureID()
    {
        return mMeasureID;
    }

    /**
     * @param pId l'id de la r�gle
     */
    public void setId( long pId )
    {
        mId = pId;
    }

    /**
     * @param pNameRule nom de la r�gle
     */
    public void setNameRule( String pNameRule )
    {
        mNameRule = pNameRule;
    }

    /**
     * @param pSeverity sev�rit� de la r�gle
     */
    public void setSeverity( String pSeverity )
    {
        mSeverity = pSeverity;
    }

    /**
     * @param pSeverityLang sev�rit� de la r�gle traduite
     */
    public void setSeverityLang( String pSeverityLang )
    {
        mSeverityLang = pSeverityLang;
    }

    /**
     * @param pTrasgressionNumber le nombre de trasgression de la r�gle
     */
    public void setTransgressionsNumber( int pTrasgressionNumber )
    {
        mTransgressionsNumber = pTrasgressionNumber;
    }

    /**
     * @param pVersion la version
     */
    public void setVersion( String pVersion )
    {
        mVersion = pVersion;
    }

    /**
     * @param pID l'id de la mesure
     */
    public void setMeasureID( long pID )
    {
        mMeasureID = pID;
    }
}
