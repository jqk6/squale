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
package org.squale.squaleweb.transformer.component.parameters;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.squale.squalecommon.datatransfertobject.component.parameters.MapParameterDTO;
import org.squale.squalecommon.datatransfertobject.component.parameters.StringParameterDTO;
import org.squale.squalecommon.datatransfertobject.rulechecking.CppTestRuleSetDTO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.ParametersConstants;
import org.squale.squaleweb.applicationlayer.formbean.component.parameters.CppTestForm;
import org.squale.welcom.struts.bean.WActionForm;
import org.squale.welcom.struts.transformer.WITransformer;
import org.squale.welcom.struts.transformer.WTransformerException;

/**
 * Conversion des informations du formulaire de configuration CppTest
 */
public class CppTestProjectConfTransformer
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
        CppTestForm form = new CppTestForm();
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
        // R�cup�ration des param�tres
        MapParameterDTO projetParams = (MapParameterDTO) pObject[0];
        // Param�tres CppTest
        MapParameterDTO params = (MapParameterDTO) projetParams.getParameters().get( ParametersConstants.CPPTEST );
        Collection rulesets = (Collection) pObject[1];

        // On remplit le form
        CppTestForm cppTestForm = (CppTestForm) pForm;
        // R�cup�ration des rulesets
        HashSet set = new HashSet();
        for ( Iterator it = rulesets.iterator(); it.hasNext(); )
        {
            CppTestRuleSetDTO dto = (CppTestRuleSetDTO) it.next();
            set.add( dto.getName() );
        }
        cppTestForm.setRuleSets( set );
        if ( params != null )
        {
            // R�cup�ration du RuleSet
            StringParameterDTO ruleSetName =
                (StringParameterDTO) params.getParameters().get( ParametersConstants.CPPTEST_RULESET_NAME );
            // Ce cas est peu probable - il s'agirait d'un oubli en base de donn�es
            if ( ruleSetName != null )
            {
                cppTestForm.setSelectedRuleSet( ruleSetName.getValue() );
            }
            // R�cup�ration du script CppTest
            StringParameterDTO script =
                (StringParameterDTO) params.getParameters().get( ParametersConstants.CPPTEST_SCRIPT );
            // Ce cas est peu probable - il s'agirait d'un oubli en base de donn�es
            if ( script != null )
            {
                cppTestForm.setScript( script.getValue() );
            }
        }
    }

    /**
     * @param pForm le formulaire � lire.
     * @throws WTransformerException si un pb apparait.
     * @return le tableaux des objets.
     */
    public Object[] formToObj( WActionForm pForm )
        throws WTransformerException
    {
        Object[] obj = { new MapParameterDTO() };
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
        CppTestForm cppTestForm = (CppTestForm) pForm;
        MapParameterDTO params = (MapParameterDTO) pObject[0];
        // Param�tres CppTest
        MapParameterDTO cppTestParams = new MapParameterDTO();
        // Traitement du ruleSet
        StringParameterDTO selectedRuleSet = new StringParameterDTO();
        selectedRuleSet.setValue( cppTestForm.getSelectedRuleSet() );
        cppTestParams.getParameters().put( ParametersConstants.CPPTEST_RULESET_NAME, selectedRuleSet );
        // Traitement du script CppTest
        StringParameterDTO script = new StringParameterDTO();
        script.setValue( cppTestForm.getScript() );
        cppTestParams.getParameters().put( ParametersConstants.CPPTEST_SCRIPT, script );
        params.getParameters().put( ParametersConstants.CPPTEST, cppTestParams );
    }

}
