package com.airfrance.squaleweb.transformer.stats;

import com.airfrance.squalecommon.datatransfertobject.stats.AuditsStatsDTO;
import com.airfrance.squaleweb.applicationlayer.formbean.stats.AuditsStatsForm;
import com.airfrance.welcom.struts.bean.WActionForm;
import com.airfrance.welcom.struts.transformer.WITransformer;
import com.airfrance.welcom.struts.transformer.WTransformerException;

/**
 */
public class AuditsStatsTransformer
    implements WITransformer
{

    /**
     * @param pObject le tableau d'objet contenant l'objet � transformer
     * @return le form r�sultat de la transformation
     * @throws WTransformerException en cas d'�chec
     */
    public WActionForm objToForm( Object[] pObject )
        throws WTransformerException
    {
        AuditsStatsForm form = new AuditsStatsForm();
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
        AuditsStatsForm form = (AuditsStatsForm) pForm;
        AuditsStatsDTO dto = (AuditsStatsDTO) pObject[0];
        form.setNbFailed( dto.getNbFailed() );
        form.setNbPartial( dto.getNbPartial() );
        form.setNbNotAttempted( dto.getNbNotAttempted() );
        form.setNbSuccessfuls( dto.getNbSuccessfuls() );
        form.setNbTotal( dto.getNbTotal() );
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
