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
package com.airfrance.squalecommon.datatransfertobject.result;

import com.airfrance.squalecommon.datatransfertobject.component.AuditDTO;
import com.airfrance.squalecommon.datatransfertobject.component.ComponentDTO;
import com.airfrance.squalecommon.datatransfertobject.rule.QualityRuleDTO;

/**
 * DTO pour les r�sultats qualit�s
 */
public class QualityResultDTO
{

    /**
     * Note moyenne du r�sultat qualit�
     */
    protected float mMeanMark;

    /**
     * Identifiant (au sens technique) de l'objet
     */
    protected long mId;

    /**
     * Projet sur lequel est calcul� le r�sultat qualit�.
     */
    protected ComponentDTO mProject;

    /**
     * Audit durant lequel a �t� g�n�r� le r�sultat
     */
    protected AuditDTO mAudit;

    /**
     * La r�gle
     */
    protected QualityRuleDTO mRule;

    /**
     * @return la r�gle
     */
    public QualityRuleDTO getRule()
    {
        return mRule;
    }

    /**
     * Sets the value of the mRule property.
     * 
     * @param pRule the new value of the mRule property
     */
    public void setRule( QualityRuleDTO pRule )
    {
        mRule = pRule;
    }

    /**
     * Access method for the mMeanMark property.
     * 
     * @return the current value of the mMeanMark property
     */
    public float getMeanMark()
    {
        return mMeanMark;
    }

    /**
     * Sets the value of the mMeanMark property.
     * 
     * @param pMeanMark the new value of the mMeanMark property
     */
    public void setMeanMark( float pMeanMark )
    {
        mMeanMark = pMeanMark;
    }

    /**
     * Access method for the mProject property.
     * 
     * @return the current value of the mProject property
     */
    public ComponentDTO getProject()
    {
        return mProject;
    }

    /**
     * Sets the value of the mProject property.
     * 
     * @param pProject the new value of the mProject property
     */
    public void setProject( ComponentDTO pProject )
    {
        mProject = pProject;
    }

    /**
     * Access method for the mAudit property.
     * 
     * @return the current value of the mAudit property
     */
    public AuditDTO getAudit()
    {
        return mAudit;
    }

    /**
     * Sets the value of the mAudit property.
     * 
     * @param pAudit the new value of the mAudit property
     */
    public void setAudit( AuditDTO pAudit )
    {
        mAudit = pAudit;
    }

    /**
     * Constructeur par d�faut
     */
    public QualityResultDTO()
    {
        mMeanMark = -1;
    }
}
