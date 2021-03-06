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
 * Cr�� le 4 mars 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package org.squale.welcom.taglib.table;

import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.validator.GenericValidator;
import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;
import org.squale.welcom.outils.Access;


/**
 * @author M327837 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class ColSingleSelectTag
    extends BodyTagSupport
{
    /**
     * 
     */
    private static final long serialVersionUID = 4409913627129484124L;

    /** parametre du tag */
    private String propertyValue = "";

    /** parametre du tag */
    private String key = "";

    /** parametre du tag */
    private String width = "20px";

    /** parametre du tag */
    private ColsTag colsTag;

    /** parametre du tag */
    private String toolTipKey = "";

    /** parametre du tag */
    private boolean forceReadWrite = false;

    /** parametre du tag */
    private String property = "";

    /** onclick */
    private String onclick = "";

    /** parametre du tag */
    private String disabledProperty = "";

    /** parametre du tag */
    private String name = "";

    /** parametre du tag */
    private String value = "";

    /**
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
     */
    public int doStartTag()
        throws JspException
    {
        final String pageAccess = (String) pageContext.getAttribute( "access" );

        if ( ( pageAccess != null ) && pageAccess.equals( Access.READONLY ) && !forceReadWrite )
        {
            return SKIP_BODY;
        }

        // Recherche si un parent est du bon type
        Tag curParent = null;

        for ( curParent = getParent(); ( curParent != null ) && !( curParent instanceof ColsTag ); )
        {
            curParent = curParent.getParent();
        }

        if ( curParent == null )
        {
            throw new JspException( "ColTag  must be used between Cols Tag." );
        }

        colsTag = (ColsTag) curParent;

        if ( !GenericValidator.isBlankOrNull( colsTag.getEmptyKey() )
            && ( pageContext.getAttribute( colsTag.getId() ) == null ) )
        {
            return SKIP_BODY;
        }
        else
        {
            return EVAL_PAGE;
        }
    }

    /**
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doEndTag()
        throws JspException
    {
        final String pageAccess = (String) pageContext.getAttribute( "access" );

        if ( ( pageAccess != null ) && pageAccess.equals( Access.READONLY ) && !forceReadWrite )
        {
            return SKIP_BODY;
        }

        // Recupere la locale de la page
        final Locale localeRequest = (Locale) pageContext.getSession().getAttribute( Globals.LOCALE_KEY );

        // Recuperer le fichier des Bundle
        final MessageResources resources =
            (MessageResources) pageContext.getServletContext().getAttribute( Globals.MESSAGES_KEY );

        final ColSingleSelect c = new ColSingleSelect();

        if ( key == null )
        {
            key = "selected";
        }

        c.setKey( key );
        c.setPropertyValue( propertyValue );
        c.setWidth( width );
        c.setPageContext( pageContext );
        c.setWriteTD( false );
        c.setOnclick( onclick );
        c.setValue( value );
        c.setDisabledProperty( disabledProperty );
        String val = (String) RequestUtils.lookup( super.pageContext, name, property, null );
        if ( val != null )
        {
            c.setFormbeanValue( val );
        }
        c.setProperty( property );
        c.setSpecialHeaderTitle( resources.getMessage( localeRequest, "welcom.internal.selectAll.tootip" ) );
        c.setToolTip( resources.getMessage( localeRequest, toolTipKey ) );
        if ( GenericValidator.isBlankOrNull( c.getToolTip() ) )
        {
            c.setToolTip( toolTipKey );
        }
        if ( getBodyContent() != null )
        {
            c.setCurrentValue( getBodyContent().getString().trim() );
        }

        colsTag.addCellule( c );

        return EVAL_PAGE;
    }

    /**
     * @see javax.servlet.jsp.tagext.BodyTagSupport#release()
     */
    public void release()
    {
        colsTag = null;
        property = "";
        key = "";
        width = "";
        toolTipKey = "";
        forceReadWrite = false;

        disabledProperty = "";
        onclick = "";
        super.release();
    }

    /**
     * @return colsTag
     */
    public ColsTag getColsTag()
    {
        return colsTag;
    }

    /**
     * @return key
     */
    public String getKey()
    {
        return key;
    }

    /**
     * @return property
     */
    public String getProperty()
    {
        return property;
    }

    /**
     * @return width
     */
    public String getWidth()
    {
        return width;
    }

    /**
     * @param tag le nouveau colsTag
     */
    public void setColsTag( final ColsTag tag )
    {
        colsTag = tag;
    }

    /**
     * @param string le nouveau key
     */
    public void setKey( final String string )
    {
        key = string;
    }

    /**
     * @param string le nouveau property
     */
    public void setProperty( final String string )
    {
        property = string;
    }

    /**
     * @param string le nouveau width
     */
    public void setWidth( final String string )
    {
        width = string;
    }

    /**
     * @return forceReadWrite
     */
    public boolean isForceReadWrite()
    {
        return forceReadWrite;
    }

    /**
     * @param b le nouveau forceReadWrite
     */
    public void setForceReadWrite( final boolean b )
    {
        forceReadWrite = b;
    }

    /**
     * @return accesseur
     */
    public String getToolTipKey()
    {
        return toolTipKey;
    }

    /**
     * @param string accesseur
     */
    public void setToolTipKey( final String string )
    {
        toolTipKey = string;
    }

    /**
     * @return accesseur
     */
    public String getOnclick()
    {
        return onclick;
    }

    /**
     * @param string accesseur
     */
    public void setOnclick( final String string )
    {
        onclick = string;
    }

    /**
     * @return disabledProperty
     */
    public String getDisabledProperty()
    {
        return disabledProperty;
    }

    /**
     * @param string disabledProperty
     */
    public void setDisabledProperty( final String string )
    {
        disabledProperty = string;
    }

    /**
     * @return value
     */
    public String getValue()
    {
        return value;
    }

    /**
     * @param string value
     */
    public void setValue( String string )
    {
        value = string;
    }

    /**
     * @return name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return PropertyValue
     */
    public String getPropertyValue()
    {
        return propertyValue;
    }

    /**
     * @param string name
     */
    public void setName( String string )
    {
        name = string;
    }

    /**
     * @param string PropertyValue
     */
    public void setPropertyValue( String string )
    {
        propertyValue = string;
    }

}