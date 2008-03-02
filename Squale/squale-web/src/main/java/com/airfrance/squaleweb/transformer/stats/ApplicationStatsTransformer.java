package com.airfrance.squaleweb.transformer.stats;

import java.util.Date;
import java.util.Locale;

import com.airfrance.squalecommon.datatransfertobject.stats.ApplicationStatsDTO;
import com.airfrance.squaleweb.applicationlayer.formbean.stats.ApplicationStatsForm;
import com.airfrance.squaleweb.resources.WebMessages;
import com.airfrance.squaleweb.util.SqualeWebActionUtils;
import com.airfrance.welcom.struts.bean.WActionForm;
import com.airfrance.welcom.struts.transformer.WITransformer;
import com.airfrance.welcom.struts.transformer.WTransformerException;

/**
 * Transforme les statistiques niveau application dto <-> form
 */
public class ApplicationStatsTransformer
    implements WITransformer
{

    /** La cl� utilis�es pour formatter les dates */
    private static final String FORMAT_DATE_KEY = "date.format.simple.score";

    /**
     * @param pObject le tableau d'objet contenant l'objet � transformer
     * @return le form r�sultat de la transformation
     * @throws WTransformerException en cas d'�chec
     */
    public WActionForm objToForm( Object[] pObject )
        throws WTransformerException
    {
        ApplicationStatsForm form = new ApplicationStatsForm();
        objToForm( pObject, form );
        return form;
    }

    /**
     * @param pObject le tableau d'objet contenant l'objet � transformer
     * @param pForm le form r�sultat
     * @throws WTransformerException en cas d'�chec
     */
    public void objToForm( Object[] pObject, WActionForm pForm )
        throws WTransformerException
    {
        ApplicationStatsForm form = (ApplicationStatsForm) pForm;
        ApplicationStatsDTO dto = (ApplicationStatsDTO) pObject[0];
        Locale locale = (Locale) pObject[1];
        // On modifie tous les attributs du fromulaire renseign� par le DTO
        // On transforme en String ici pour l'export Excel
        form.setActivatedApplication( WebMessages.getString( locale, "stats.active." + dto.isActivatedApplication() ) );
        form.setApplicationName( dto.getApplicationName() );
        form.setFirstTerminatedAuditDate( getFormattedDate( dto.getFirstTerminatedAuditDate(), locale ) );
        form.setLastAccess( getFormattedDate( dto.getLastAccess(), locale ) );
        form.setLastAuditDuration( dto.getLastAuditDuration() );
        // On transforme en String ici pour l'export Excel
        form.setLastAuditIsTerminated( WebMessages.getString( locale, "stats.state." + dto.isLastAuditIsTerminated() ) );
        form.setLastFailedAuditDate( getFormattedDate( dto.getLastFailedAuditDate(), locale ) );
        form.setLastTerminatedAuditDate( getFormattedDate( dto.getLastTerminatedAuditDate(), locale ) );
        form.setNbAudits( dto.getNbAudits() );
        form.setNbPartialOrFaliedAudits( dto.getNbPartialOrFaliedAudits() );
        form.setNbTerminatedAudits( dto.getNbTerminatedAudits() );
        form.setServerName( dto.getServerName() );
        form.setValidatedApplication( dto.isValidatedApplication() );
        form.setValidatedApplicationStr( WebMessages.getString( locale, "stats.application.validated."
            + dto.isValidatedApplication() ) );
        form.setPurgeFrequency( dto.getPurgeFrequency() );
    }

    /**
     * Retourne la date format�e sous la forme d�finie dans le fichier de properties par la cl� d�finie dans la
     * constante <code>FORMAT_DATE_KEY</code>
     * 
     * @param pDate la date � formatter
     * @param pLocale la locale
     * @return la date formatt�e
     */
    private String getFormattedDate( Date pDate, Locale pLocale )
    {
        String formattedDate = "-";
        if ( pDate != null )
        {
            formattedDate = SqualeWebActionUtils.getFormattedDate( pLocale, pDate, FORMAT_DATE_KEY );
        }
        return formattedDate;
    }

    /**
     * @deprecated n'a pas de sens
     * @param pForm le formulaire
     * @throws WTransformerException si un pb apparait.
     * @return rien mais lance syst�matiquement une exception
     */
    public Object[] formToObj( WActionForm pForm )
        throws WTransformerException
    {
        throw new WTransformerException( "deprecated" );
    }

    /**
     * @deprecated n'a pas de sens
     * @param pForm le formulaire
     * @param pTab les param�tres
     * @throws WTransformerException si un pb apparait.
     */
    public void formToObj( WActionForm pForm, Object[] pTab )
        throws WTransformerException
    {
        throw new WTransformerException( "deprecated" );
    }

}
