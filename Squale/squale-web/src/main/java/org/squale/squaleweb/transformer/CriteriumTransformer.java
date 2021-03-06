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
package org.squale.squaleweb.transformer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.squale.squalecommon.datatransfertobject.rule.CriteriumRuleDTO;
import org.squale.squalecommon.datatransfertobject.rule.PracticeRuleDTO;
import org.squale.squaleweb.applicationlayer.formbean.component.CriteriumRuleForm;
import org.squale.squaleweb.applicationlayer.formbean.component.PracticeRuleForm;
import org.squale.squaleweb.applicationlayer.formbean.component.QualityRuleForm;
import org.squale.welcom.struts.bean.WActionForm;
import org.squale.welcom.struts.transformer.WITransformer;
import org.squale.welcom.struts.transformer.WTransformerException;
import org.squale.welcom.struts.transformer.WTransformerFactory;

/**
 * Transformation d'un crit�re
 */
public class CriteriumTransformer
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
        CriteriumRuleForm form = new CriteriumRuleForm();
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
        Locale local = (Locale)pObject[1];
        CriteriumRuleDTO criteriumDTO = (CriteriumRuleDTO) pObject[0];
        CriteriumRuleForm form = (CriteriumRuleForm) pForm;
        form.setId( criteriumDTO.getId() );
        form.setName( criteriumDTO.getName() );
        String ponderation = "";
        form.setPonderation( ponderation );
        // Traitement de chaque pratique
        Iterator practicesIt = criteriumDTO.getPractices().keySet().iterator();
        while ( practicesIt.hasNext() )
        {
            PracticeRuleDTO currentDTO = (PracticeRuleDTO) practicesIt.next();
            form.addPractice( (PracticeRuleForm) WTransformerFactory.objToForm( PracticeTransformer.class, new Object[] {currentDTO,local} ) );
            ponderation +=
                QualityRuleForm.SEPARATOR + ( (Float) criteriumDTO.getPractices().get( currentDTO ) ).toString();
        }
        if ( ponderation.length() > 2 )
        {
            // On supprime la premi�re virgule inutile
            ponderation = ponderation.substring( 2 );
        }
        form.setPonderation( ponderation );
    }

    /**
     * @param pForm le formulaire � lire.
     * @throws WTransformerException si un pb apparait.
     * @return le tableaux des objets.
     */
    public Object[] formToObj( WActionForm pForm )
        throws WTransformerException
    {
        CriteriumRuleDTO dto = new CriteriumRuleDTO();
        formToObj( pForm, new Object[] { dto } );
        return new Object[] { dto };
    }

    /**
     * @param pObject l'objet � remplir
     * @param pForm le formulaire � lire.
     * @throws WTransformerException si un pb apparait.
     */
    public void formToObj( WActionForm pForm, Object[] pObject )
        throws WTransformerException
    {
        CriteriumRuleForm form = (CriteriumRuleForm) pForm;
        CriteriumRuleDTO dto = (CriteriumRuleDTO) pObject[0];
        dto.setId( form.getId() );
        dto.setName( form.getName() );
        ArrayList practices = (ArrayList) form.getPractices().getList();
        SortedMap practicesMap = new TreeMap();
        String ponderation = form.getPonderation();
        // on d�coupe les pond�rations qui sont sous la forme d'une String dans le form
        // les valeurs sont s�par�s par une ", " (QualityRuleForm.SEPARATOR)
        StringTokenizer st = new StringTokenizer( ponderation, QualityRuleForm.SEPARATOR );
        // Si le nombre de pond�rations est diff�rent du nombre de crit�res
        // on lance une exception
        if ( st.countTokens() != practices.size() )
        {
            throw new WTransformerException();
        }
        int i = 0;
        while ( st.hasMoreTokens() )
        {
            PracticeRuleForm practiceForm = (PracticeRuleForm) practices.get( i++ );
            PracticeRuleDTO practiceDTO =
                (PracticeRuleDTO) WTransformerFactory.formToObj( PracticeTransformer.class, practiceForm )[0];
            dto.addPractice( practiceDTO, new Float( Float.parseFloat( (String) st.nextElement() ) ) );
        }
    }

}
