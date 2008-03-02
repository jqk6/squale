package com.airfrance.squaleweb.transformer;

import com.airfrance.squalecommon.datatransfertobject.rule.QualityGridDTO;
import com.airfrance.squaleweb.applicationlayer.formbean.component.GridForm;
import com.airfrance.welcom.struts.bean.WActionForm;
import com.airfrance.welcom.struts.transformer.WITransformer;
import com.airfrance.welcom.struts.transformer.WTransformerException;

/**
 * Transformation d'une grille qualit�
 */
public class GridTransformer
    implements WITransformer
{

    /**
     * @param pObject l'objet � transformer
     * @throws WTransformerException si un pb apparait.
     * @return le formulaire.
     */
    public WActionForm objToForm( Object[] pObject )
        throws WTransformerException
    {
        GridForm form = new GridForm();
        objToForm( pObject, form );
        return form;
    }

    /**
     * @param pObject l'objet � transformer
     * @param pForm le formulaire � remplir.
     * @throws WTransformerException si un pb apparait.
     */
    public void objToForm( Object[] pObject, WActionForm pForm )
        throws WTransformerException
    {
        QualityGridDTO gridDTO = (QualityGridDTO) pObject[0];
        GridForm form = (GridForm) pForm;
        form.setId( gridDTO.getId() );
        form.setName( gridDTO.getName() );
        form.setUpdateDate( gridDTO.getUpdateDate() );
        // Texte pour un mail �ventuel signifiant les changement de grille,
        // pas de texte par d�faut (pourra etre rempli plus tard)
        form.setAdminText( "" );
    }

    /**
     * @param pForm le formulaire � lire.
     * @throws WTransformerException si un pb apparait.
     * @return le tableaux des objets.
     */
    public Object[] formToObj( WActionForm pForm )
        throws WTransformerException
    {
        Object[] obj = { new QualityGridDTO() };
        formToObj( pForm, obj );
        return obj;
    }

    /**
     * @param pObject l'objet � remplir
     * @param pForm le formulaire � lire.
     * @throws WTransformerException si un pb apparait.
     */
    public void formToObj( WActionForm pForm, Object[] pObject )
        throws WTransformerException
    {
        GridForm gridForm = (GridForm) pForm;
        QualityGridDTO dto = (QualityGridDTO) pObject[0];
        dto.setId( gridForm.getId() );
        dto.setName( gridForm.getName() );
    }

}
