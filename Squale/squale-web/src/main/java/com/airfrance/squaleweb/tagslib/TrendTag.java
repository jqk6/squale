package com.airfrance.squaleweb.tagslib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;

import com.airfrance.squaleweb.util.SqualeWebActionUtils;

/**
 */
public class TrendTag
    extends TagSupport
{

    /** le param�tre "nom" du tag */
    private String name;

    /** le param�tre "current" du tag qui d�signe la note actuelle */
    private String current;

    /** le param�tre "predecessor" du tag qui d�signe la note pr�c�dente */
    private String predecessor;

    /**
     * @see javax.servlet.jsp.tagext.TagSupport#doEndTag() {@inheritDoc} M�thode de lancement du tag
     */
    public int doStartTag()
        throws JspException
    {
        // Publie
        ResponseUtils.write( pageContext, getImageForTrend( (String) RequestUtils.lookup( pageContext, name, current,
                                                                                          null ),
                                                            (String) RequestUtils.lookup( pageContext, name,
                                                                                          predecessor, null ) ) );
        return SKIP_BODY;
    }

    /**
     * Obtention de l'image associ�e � une tendance
     * 
     * @param pCurrentMark la note actuelle
     * @param pPreviousMark l'ancienne note
     * @return l'url avec l'image correspondant � l'�volution
     */
    private String getImageForTrend( String pCurrentMark, String pPreviousMark )
    {
        String result = SqualeWebActionUtils.getImageForTrend( pCurrentMark, pPreviousMark );
        return "<img src=\"" + result + "\" border=\"0\" />";
    }

    /**
     * @return la note courante
     */
    public String getCurrent()
    {
        return current;
    }

    /**
     * @return le nom
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return la note pr�c�dente
     */
    public String getPredecessor()
    {
        return predecessor;
    }

    /**
     * change la note courante
     * 
     * @param newCurrent la nouvelle note
     */
    public void setCurrent( String newCurrent )
    {
        current = newCurrent;
    }

    /**
     * change le nom
     * 
     * @param newName la nouvelle note
     */
    public void setName( String newName )
    {
        name = newName;
    }

    /**
     * change la note courante
     * 
     * @param newPredecessor la nouvelle note
     */
    public void setPredecessor( String newPredecessor )
    {
        predecessor = newPredecessor;
    }

}
