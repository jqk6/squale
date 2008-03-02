package com.airfrance.squaleweb.tagslib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;

import com.airfrance.squaleweb.util.SqualeWebActionUtils;

/**
 */
public class PictoTag
    extends TagSupport
{

    /** le param�tre "nom" du tag */
    private String name;

    /** le param�tre "propri�t�" du tag */
    private String property;

    /** permet de r�cup�rer la note si on la connait sert pour la page mark */
    private String mark;

    /**
     * Affiche l'image.
     * 
     * @param pNote la note ou l'index.
     * @param pRequest la requ�te
     * @return le chemin de l'image
     */
    private String generatePicto( String pNote, HttpServletRequest pRequest )
    {
        String imgTag = "";
        imgTag = SqualeWebActionUtils.generatePictoWithTooltip( pNote, pRequest );
        return imgTag;
    }

    /**
     * @see javax.servlet.jsp.tagext.TagSupport#doEndTag() {@inheritDoc} M�thode de lancement du tag
     */
    public int doStartTag()
        throws JspException
    {
        // Publie
        if ( mark != null )
        {
            ResponseUtils.write( pageContext, generatePicto( mark, (HttpServletRequest) pageContext.getRequest() ) );
        }
        else
        {
            ResponseUtils.write( pageContext, generatePicto( (String) RequestUtils.lookup( pageContext, name, property,
                                                                                           null ),
                                                             (HttpServletRequest) pageContext.getRequest() ) );
        }
        return SKIP_BODY;
    }

    /**
     * @see javax.servlet.jsp.tagext.TagSupport#doEndTag() {@inheritDoc} M�thode de lancement du tag
     */
    public int doEndTag()
        throws JspException
    {
        return EVAL_PAGE;
    }

    /**
     * @return le nom
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return la propri�t�
     */
    public String getProperty()
    {
        return property;
    }

    /**
     * change le nom
     * 
     * @param newName le nouveau nom
     */
    public void setName( String newName )
    {
        name = newName;
    }

    /**
     * change la propri�t�
     * 
     * @param newProperty la nouvelle propri�t�
     */
    public void setProperty( String newProperty )
    {
        property = newProperty;
    }

    /**
     * @return la note
     */
    public String getMark()
    {
        return mark;
    }

    /**
     * @param newMark la nouvelle note
     */
    public void setMark( String newMark )
    {
        mark = newMark;
    }
}
