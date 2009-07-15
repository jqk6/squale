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

import java.util.ArrayList;
import java.util.List;

/**
 * Bean pour les r�sultats des transgressions
 */
public class ResultRulesCheckingForm
    extends ResultForm
{

    /* Constantes de severite pour remplir le tableau de repartition */

    /** Cas info */
    public static final int INFO_INT = 0;

    /** Cas warning */
    public static final int WARNING_INT = 1;

    /** Cas error */
    public static final int ERROR_INT = 2;

    /**
     * Liste des r�sultats
     */
    private List mList = new ArrayList();

    /**
     * @return la liste des r�sultats
     */
    public List getList()
    {
        return mList;
    }

    /**
     * @param pList la liste des r�sultats
     */
    public void setList( List pList )
    {
        mList = pList;

    }
}
