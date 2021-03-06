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
package org.squale.welcom.taglib.table.impl;

import org.apache.commons.validator.GenericValidator;
import org.squale.welcom.outils.TrimStringBuffer;
import org.squale.welcom.outils.Util;
import org.squale.welcom.taglib.button.ButtonBarTag;
import org.squale.welcom.taglib.table.ITableRenderer;
import org.squale.welcom.taglib.table.Table;


/**
 * @author M327837 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */

public class TableRendererV2002
    implements ITableRenderer
{

    /**
     * @see ITableRenderer#drawTableBottom(String, String)
     */
    public String drawTableBottom( String bottomValue, String width )
    {

        final TrimStringBuffer sb = new TrimStringBuffer();
        sb.append( "<table class=\"noborder\" width=\"" + width + "\"> \n" );
        sb.append( "\t <tr class=\"trpied\"> \n" );

        if ( Util.isTrimNonVide( ButtonBarTag.purgeBodyHidden( bottomValue ) ) )
        {
            sb.append( "\t\t <td align=\"left\" valign=\"top\" colspan=\"50\">  \n" );
            sb.append( "\t\t\t <table class=\"noborder\" width=\"" + width + "\">  \n" );
            sb.append( "\t\t\t\t <tr  valign=\"middle\" height=\"25\">  \n" );
            sb.append( "\t\t\t\t\t " + bottomValue + "  \n" );
            sb.append( "\t\t\t\t </tr>  \n" );
            sb.append( "\t\t\t </table>  \n" );
            sb.append( "\t\t </td>  \n" );
        }
        else
        {
            sb.append( "\t\t\t\t\t <td height=\"5\"></td> \n" );
        }

        sb.append( "\t </tr>  \n" );
        sb.append( "</table> \n" );

        return sb.toString();

    }

    /**
     * @see ITableRenderer#drawTableTitle(String)
     */
    public String drawTableTitle( String columns )
    {
        final TrimStringBuffer sb = new TrimStringBuffer();
        sb.append( "\t <tr class=\"trtete\"> \n" );
        sb.append( columns );
        sb.append( "</tr>\n" );

        return sb.toString();
    }

    /**
     * @see ITableRenderer#drawTableStart(String, String)
     */
    public String drawTableStart( String tableName, String width )
    {
        final TrimStringBuffer sb = new TrimStringBuffer();
        sb.append( "<table " );
        if ( !GenericValidator.isBlankOrNull( tableName ) )
        {
            sb.append( "id=\"" + tableName + "\" " );
        }
        sb.append( "border=\"0\" cellspacing=\"0\" class=\"liste\" width=\"" + width + "\"> \n" );

        return sb.toString();
    }

    /**
     * @see ITableRenderer#drawTableEnd()
     */
    public String drawTableEnd()
    {
        final StringBuffer sb = new StringBuffer();
        sb.append( Table.ligneRail() );
        sb.append( "</table>" );
        return sb.toString();
    }

    /**
     * @see ITableRenderer#drawNavigation(String, String, String)
     */
    public String drawNavigation( String totalLabel, String navigation, String width )
    {
        TrimStringBuffer sb = new TrimStringBuffer();
        sb.append( "<table class=\"noborder\" width=\"" + width + "\"> \n" );

        sb.append( "\t <tr >\n" );

        if ( !GenericValidator.isBlankOrNull( totalLabel ) )
        {
            sb.append( "\t\t <td valign=\"bottom\"><p class=\"encadreBleu gras\" style=\"padding-bottom:3\">"
                + totalLabel + "</p></td>" );
        }
        else
        {
            sb.append( "\t\t <td></td>" );
        }

        if ( !GenericValidator.isBlankOrNull( navigation ) )
        {
            sb.append( "\t\t <td align=\"right\" valign=\"bottom\"><p class=\"encadreBleu gras\">" );
            sb.append( navigation );
            sb.append( "\t\t </p></td>" );
        }
        else
        {
            sb.append( "\t\t <td></td>" );
        }

        sb.append( "\t </tr>\n" );
        sb.append( "</table>\n" );
        return sb.toString();
    }
}
