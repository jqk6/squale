/*
 * Cr�� le 16 janv. 06
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.taglib.zone;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.airfrance.welcom.struts.lazyLoading.WLazyLoadingPersistance;
import com.airfrance.welcom.struts.lazyLoading.WLazyLoadingType;

/**
 * @author M325379 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class ZoneTag
    extends BodyTagSupport
{
    /**
     * 
     */
    private static final long serialVersionUID = -5331108653338911374L;

    /** le nom de la zone */
    private String name = "";

    /**
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
     */
    public int doEndTag()
        throws JspException
    {
        // Recupere le body evalu�
        final BodyContent bodyContent = getBodyContent();

        if ( bodyContent != null )
        {
            final String value = bodyContent.getString().trim();
            bodyContent.clearBody();

            if ( value.length() > 0 )
            {
                WLazyLoadingPersistance.find( pageContext.getSession() ).add( WLazyLoadingType.ZONE, name, value );
            }
        }

        // Continue processing this page
        release();
        return ( EVAL_PAGE );
    }

    /**
     * @return name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param string name
     */
    public void setName( final String string )
    {
        name = string;
    }

}
