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
package org.squale.squaleweb.applicationlayer.formbean.results;

/**
 * Bean repr�sentant les transgressions pour l'export PDF
 */
public class RuleCheckingPDFForm
    extends RulesCheckingForm
{

    /** D�tails de la r�gles */
    private RuleCheckingItemsListForm mDetails;

    /**
     * Constructeur par d�faut
     */
    public RuleCheckingPDFForm()
    {
        this( "", "", 0, new RuleCheckingItemsListForm() );
    }

    /**
     * @param pNameRule le nom de la r�gle
     * @param pSeverity la s�v�rit� de la r�gle
     * @param pTransgressionsNumber le nombre de transgressions
     * @param pItemsForm les d�tails de la r�gles
     */
    public RuleCheckingPDFForm( String pNameRule, String pSeverity, int pTransgressionsNumber,
                                RuleCheckingItemsListForm pItemsForm )
    {
        mNameRule = pNameRule;
        mSeverity = pSeverity;
        mTransgressionsNumber = pTransgressionsNumber;
        mDetails = pItemsForm;
    }

    /**
     * @return les d�tails de la r�gles
     */
    public RuleCheckingItemsListForm getDetails()
    {
        return mDetails;
    }

    /**
     * @param pItems les d�tails de la r�gles
     */
    public void setDetails( RuleCheckingItemsListForm pItems )
    {
        mDetails = pItems;
    }

}
