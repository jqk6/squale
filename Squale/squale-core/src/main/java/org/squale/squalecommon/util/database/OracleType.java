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
package org.squale.squalecommon.util.database;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Implementation of DatabaseType for Oracle
 */
public class OracleType
    implements DatabaseType
{

    /**
     * {@inheritDoc}
     */
    public String toDate( Date date )
    {
        SimpleDateFormat sdf = new SimpleDateFormat( JAVADATEPATTERN );
        String dateString = sdf.format( date );
        StringBuffer query = new StringBuffer( "to_date('" );
        query.append( dateString );
        query.append( "' , 'DD/MM/YYYY HH24:mi:ss') " );
        return query.toString();
    }

    /**
     * {@inheritDoc}
     */
    public String dateAddDay( String date, String day )
    {
        StringBuffer query = new StringBuffer( "(" );
        query.append( date );
        query.append( " + " );
        query.append( day );
        query.append( " ) " );
        query.append( " < " );
        query.append( toDate( new Date() ) );
        query.append( " " );
        return query.toString();
    }
}
