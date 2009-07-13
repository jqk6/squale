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
/*
 * Cr�� le 10 oct. 07
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package org.squale.welcom.taglib.util;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author 6361371 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class WDebugConsoleTag
    extends TagSupport
{

    public int doStartTag()
        throws JspException
    {
        JspWriter out = this.pageContext.getOut();
        try
        {

            out.print( "<SCRIPT>" );
            out.print( "function wJSDebug(msg){" );
            out.print( "	var content = document.getElementById(\"wDbgZone\").value;" );

            out.print( "	if (content.length > 5000){" );
            out.print( "		content = content.substring (100, content.length);" );
            out.print( "	}" );

            out.print( "	document.getElementById(\"wDbgZone\").value = content + \"\\n\" + msg;" );
            out.print( "}" );
            out.print( "</SCRIPT>" );
            out.print( "<div id=wDbgConsole style=\"z-index: 201; width:300; visibility: visible;  margin: 0 2px; font-size: 8px;	line-height: 1px \">" );
            out.print( "		<table>" );
            out.print( "		<tr>" );
            out.print( "			<textarea id=\"wDbgZone\" cols='80' rows='10'  style='font:Verdana, Sans-serif;' ></textarea>" );
            out.print( "		</tr>" );
            out.print( "		<tr>" );
            out.print( "			<td align='center'>" );
            out.print( "				<span><button onclick=\"javascript:document.getElementById('wDbgZone').value=''\">Clear</button></span>" );
            out.print( "			</td>" );
            out.print( "			<td align='center'>" );
            out.print( "				<span><button onclick=\"javascript:document.getElementById('wDbgConsole').style.visibility='hidden'\">Hide</button></span>" );
            out.print( "			</td>" );
            out.print( "		</tr>" );
            out.print( "		</table>" );
            out.print( "</div>" );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        return EVAL_PAGE;

    }

}
