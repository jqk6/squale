package com.airfrance.squaleweb.tagslib;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;

import com.airfrance.squaleweb.resources.WebMessages;

/**
 * Construit le d�but de la page de r�sultat d'une/un application/projet
 */
public class ResultsHeaderTag
    extends TagSupport
{

    /** Le nom du formulaire */
    private String mName;

    /**
     * Indique si il faut afficher l'information sur la compararaison des audits La comparaison entre audits n'est pas
     * possible si la grille sur le projet n'est pas l� m�me (i.e. pas le m�me id en base) ou si les audits n'ont pas
     * �t� fait sur les m�me projets.
     */
    private boolean mDisplayComparable;

    /**
     * @see javax.servlet.jsp.tagext.Tag#doStartTag() {@inheritDoc}
     */
    public int doStartTag()
        throws JspException
    {
        // On r�cup�re la requ�te pour la locale
        Locale locale = pageContext.getRequest().getLocale();
        // D�but du blockquote
        StringBuffer blockquote = new StringBuffer( "<blockquote>" );
        // On r�cup�re les diff�rents attributs
        String appliId = (String) RequestUtils.lookup( pageContext, mName, "applicationId", null );
        String projectId = (String) RequestUtils.lookup( pageContext, mName, "projectId", null );
        String auditId = (String) RequestUtils.lookup( pageContext, mName, "currentAuditId", null );
        String previousAuditId = (String) RequestUtils.lookup( pageContext, mName, "previousAuditId", null );
        String paramsLink = "&currentAuditId=" + auditId + "&previousAuditId=" + previousAuditId;
        String[] param = new String[1];
        // le nom de l'application :
        // lien comme si on venait du menu application->"nom de l'application"
        param[0] =
            "<a href='/squale/application.do?action=summary&applicationId=" + appliId + "'>"
                + (String) RequestUtils.lookup( pageContext, mName, "applicationName", null ) + "</a>";
        // On affiche le nom de l'application en gras
        blockquote.append( "<b>" );
        blockquote.append( WebMessages.getString( locale, "description.name.application", param ) );
        blockquote.append( "</b> <br />" );
        // Le nom du projet
        param[0] = (String) RequestUtils.lookup( pageContext, mName, "projectName", null );
        // On affiche le nom du projet si l'attribut est d�fini
        // lien comme si on venait du menu_application->projets->"nom du projet"
        if ( null != param[0] && param[0].length() > 0 )
        {
            param[0] =
                "<a href='/squale/project.do?action=select&projectId=" + projectId + paramsLink + "'>" + param[0]
                    + "</a>";
            blockquote.append( "<b>" );
            blockquote.append( WebMessages.getString( locale, "description.name.project", param ) );
            blockquote.append( "</b> <br />" );
        }
        // Le nom de l'audit courant
        param[0] =
            "<a href='/squale/audits.do?action=select&currentAuditId=" + auditId + "'>"
                + (String) RequestUtils.lookup( pageContext, mName, "auditName", null ) + "</a>";
        // On affiche le nom de l'audit courant
        // lien idem � la s�lection de l'audit
        blockquote.append( "<br/><b>" );
        blockquote.append( WebMessages.getString( locale, "description.name.audit", param ) );
        blockquote.append( "</b><br/>" );
        // Le nom de l'audit pr�c�dent
        param[0] = (String) RequestUtils.lookup( pageContext, mName, "previousAuditName", null );
        // On affiche le nom de l'audit pr�c�dent ou la cha�ne indiquant qu'il n'y en a pas
        if ( null == param[0] || param[0].length() == 0 )
        {
            param[0] =
                WebMessages.getString( ( (HttpServletRequest) pageContext.getRequest() ),
                                       "project.practices.list.previousAudit.unknown" );
            mDisplayComparable = false; // il n'y a pas d'audit pr�c�dent
        }
        else
        {
            // On ajoute un lien
            // lien idem � la s�lection de l'audit
            param[0] =
                "<a href='/squale/audits.do?action=select&currentAuditId=" + previousAuditId + "'>" + param[0] + "</a>";
        }
        blockquote.append( "<b>" );
        blockquote.append( WebMessages.getString( locale, "description.name.previous.audit", param ) );
        blockquote.append( "</b>" );
        // On informe sur la disponibilit� de comparaison des audits dans le cas o�
        // l'attribut "displayComparable" serait � true
        if ( mDisplayComparable )
        {
            // On r�cup�re l'information dans le formulaire
            Boolean isComparable = (Boolean) RequestUtils.lookup( pageContext, mName, "comparableAudits", null );
            // On affiche le message seulement si la comparaison n'est pas disponible
            if ( !isComparable.booleanValue() )
            {
                blockquote.append( WebMessages.getString( locale, "description.audits.no.comparison" ) );
            }
        }
        // Fin du blockquote
        blockquote.append( "</blockquote>" );
        // On �crit le block
        ResponseUtils.write( pageContext, blockquote.toString() );
        return SKIP_BODY;
    }

    /**
     * @see javax.servlet.jsp.tagext.TagSupport#doEndTag() {@inheritDoc} M�thode de terminaison du tag
     */
    public int doEndTag()
        throws JspException
    {
        return EVAL_PAGE;
    }

    /**
     * @return le nom du formulaire
     */
    public String getName()
    {
        return mName;
    }

    /**
     * @return true si il faut afficher l'information sur la comparaison des audits
     */
    public boolean isDisplayComparable()
    {
        return mDisplayComparable;
    }

    /**
     * @param pName le du formulaire
     */
    public void setName( String pName )
    {
        mName = pName;
    }

    /**
     * @param pDisplayComparable true si il faut afficher l'information sur la comparaison des audits
     */
    public void setDisplayComparable( boolean pDisplayComparable )
    {
        mDisplayComparable = pDisplayComparable;
    }

}
