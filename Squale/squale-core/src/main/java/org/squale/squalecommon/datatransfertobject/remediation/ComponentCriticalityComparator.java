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
package org.squale.squalecommon.datatransfertobject.remediation;

import java.util.Comparator;
import java.util.Iterator;

/**
 * Comparator for the ComponentCriticalityDTO. It compares two component by their criticality
 * 
 * @author bfranchet
 */
public class ComponentCriticalityComparator
    implements Comparator<ComponentCriticalityDTO>
{

    /**
     * {@inheritDoc}
     */
    public int compare( ComponentCriticalityDTO o1, ComponentCriticalityDTO o2 )
    {

        float criticalityO1 = o1.getCriticality();

        float criticalityO2 = o2.getCriticality();

        int res = 0;

        if ( criticalityO1 < criticalityO2 )
        {
            // o2 is more critical than o1
            res = 1;
        }
        else if ( criticalityO1 > criticalityO2 )
        {
            // o1 is more critical than o2
            res = -1;
        }
        else
        {
            // we compare the criticality of their practice
            PracticeCriticalityComparator comp = new PracticeCriticalityComparator();
            Iterator<PracticeCriticalityDTO> it1 = o1.getPracticeList().iterator();
            Iterator<PracticeCriticalityDTO> it2 = o2.getPracticeList().iterator();
            boolean haveRes = false;
            while ( it1.hasNext() && it2.hasNext() && !haveRes )
            {
                PracticeCriticalityDTO p1 = it1.next();
                PracticeCriticalityDTO p2 = it2.next();
                // we compare the criticality of the practice of each component
                res = comp.compare( p1, p2 );
                if ( res != 0 )
                {
                    haveRes = true;
                }
            }
            if ( !haveRes )
            {
                // if no difference are found then we consider that o1 is as critical as o2
                res = 0;
            }
        }
        return res;
    }
}
