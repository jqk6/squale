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
package org.squale.squaleweb.applicationlayer.formbean.messages;

import java.util.Collection;

import org.squale.squaleweb.applicationlayer.formbean.RootForm;

/**
 * 
 */
public class NewsListForm
    extends RootForm
{

    /** la liste des nouveaut�s */
    private Collection newsList;

    /**
     * @return la liste des nouveaut�s
     */
    public Collection getNewsList()
    {
        return newsList;
    }

    /**
     * @param newList la nouvelle liste
     */
    public void setNewsList( Collection newList )
    {
        newsList = newList;
    }

}
