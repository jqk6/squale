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
package com.airfrance.squaleweb.transformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.airfrance.squalecommon.datatransfertobject.result.PracticeEvolutionDTO;
import com.airfrance.squaleweb.applicationlayer.formbean.results.ComponentForm;
import com.airfrance.squaleweb.applicationlayer.formbean.results.ComponentListForm;
import com.airfrance.squaleweb.applicationlayer.formbean.results.EvolutionForm;
import com.airfrance.squaleweb.applicationlayer.formbean.results.ResultForm;
import com.airfrance.squaleweb.applicationlayer.formbean.results.ResultListForm;
import com.airfrance.squaleweb.comparator.RuleNameComparator;
import com.airfrance.welcom.struts.bean.WActionForm;
import com.airfrance.welcom.struts.transformer.WITransformer;
import com.airfrance.welcom.struts.transformer.WTransformerException;
import com.airfrance.welcom.struts.transformer.WTransformerFactory;

/**
 * Transforme Collection PracticeEvolutionDto <-> Map
 */
public class EvolutionTransformer
    implements WITransformer
{

    /**
     * @see com.airfrance.welcom.struts.transformer.WITransformer#objToForm(java.lang.Object[]) {@inheritDoc}
     */
    public WActionForm objToForm( Object[] pObject )
        throws WTransformerException
    {
        EvolutionForm form = new EvolutionForm();
        objToForm( pObject, form );
        return form;
    }

    /**
     * @see com.airfrance.welcom.struts.transformer.WITransformer#objToForm(java.lang.Object[],
     *      com.airfrance.welcom.struts.bean.WActionForm) {@inheritDoc}
     */
    public void objToForm( Object[] pObject, WActionForm pForm )
        throws WTransformerException
    {
        EvolutionForm resultForm = (EvolutionForm) pForm;
        // On nettoie le seuil si le filtre ne correspond pas
        if ( !resultForm.getFilters()[PracticeEvolutionDTO.THRESHOLD_ID] )
        {
            resultForm.setThreshold( "" );
        }
        else
        {
            // On traduit le caract�re > ou <
            if ( ">".equals( resultForm.getComparisonSign() ) )
            {
                resultForm.setComparisonSign( "&gt;" );
            }
            else if ( "<".equals( resultForm.getComparisonSign() ) )
            {
                resultForm.setComparisonSign( "&lt;" );
            }
        }
        // On nettoie les pratiques si le filtre ne correspond pas
        if ( !resultForm.getFilters()[PracticeEvolutionDTO.ONLY_PRACTICES_ID] )
        {
            resultForm.setPractices( new String[0] );
        }
        // On r�cup�re la collection de PracticeEvolutionDTO
        Collection dtos = (Collection) pObject[0];
        // Cette table aura pour cl� un componentForm et comme valeur
        // un ResultListForm ou un ResultForm comme cl� et un
        // ComponentListForm comme valeur selon le tri du form
        List practiceNames = setResultsMap( resultForm, dtos );
        // On r�cup�re la locale pour trier les pratiques par leur nom internationalis�
        Locale locale = (Locale) pObject[1];
        RuleNameComparator comp = new RuleNameComparator( locale );
        Collections.sort( practiceNames, comp );
        // et on l'affecte au formulaire
        resultForm.setAvailablePractices( (String[]) practiceNames.toArray( new String[practiceNames.size()] ) );
    }

    /**
     * Modifie la table des r�sulats de la forme : si <code>sortType</code> = EvolutionForm.COMPONENT_FOR_KEY
     * <li>cl� : ResultForm (correspondant � la pratique)
     * <li>value : ComponentListForm sinon
     * <li>cl� : ComponentForm
     * <li>value : ResultListForm (correspondant aux pratiques) Ajoute le nom des pratiques dans la liste des pratiques
     * disponibles
     * 
     * @param pForm leformulaire � modifier
     * @param dtos la collection de PracticeEvolutionDTO
     * @return la liste des noms des pratiques concern�es par une �volution
     * @throws WTransformerException si erreur
     */
    private List setResultsMap( EvolutionForm pForm, Collection dtos )
        throws WTransformerException
    {
        Set practiceNames = new HashSet();
        Map results = new HashMap();
        Object[] paramComponent = new Object[1];
        // On parcours la collection
        for ( Iterator it = dtos.iterator(); it.hasNext(); )
        {
            PracticeEvolutionDTO evolutionDto = (PracticeEvolutionDTO) it.next();

            // On transforme le composant en formulaire
            paramComponent[0] = evolutionDto.getComponent();
            ComponentForm compForm =
                (ComponentForm) WTransformerFactory.objToForm( ComponentTransformer.class, paramComponent );

            // On transforme la pratique en formulaire
            ResultForm practiceForm =
                (ResultForm) WTransformerFactory.objToForm( ResultTransformer.class,
                                                            new Object[] { evolutionDto.getPractice() } );
            // On stock le nom de la pratique dans le set
            practiceNames.add( practiceForm.getName() );
            practiceForm.setCurrentMark( "" );
            practiceForm.setPredecessorMark( "" );
            Float currentMark = evolutionDto.getMark();
            if ( null != currentMark )
            {
                practiceForm.setCurrentMark( currentMark.toString() );
            }
            Float oldMark = evolutionDto.getPreviousMark();
            if ( null != oldMark )
            {
                practiceForm.setPredecessorMark( oldMark.toString() );
            }
            if ( pForm.getSortType().equals( EvolutionForm.COMPONENT_FOR_KEY ) )
            {
                // On veut le ComponentForm en cl�
                addPractice( results, compForm, practiceForm );
            }
            else
            {
                // On veut le ResultForm en cl�
                addComponent( results, practiceForm, compForm );
            }
        }
        pForm.setResults( results );
        return new ArrayList( practiceNames );
    }

    /**
     * @param results la table � compl�ter
     * @param practiceForm la pratique � ajouter
     * @param compForm le composant servant de cl�
     */
    private void addComponent( Map results, ResultForm practiceForm, ComponentForm compForm )
    {
        // On r�cup�re la liste des composants li�s � la pratique
        ComponentListForm components = (ComponentListForm) getByName( results, practiceForm.getName() );
        // On associe la note de la pratique au composant
        compForm.getPractices().getList().add( practiceForm );
        if ( null != components )
        {
            // On ajoute le composant � la liste des composants de practiceForm
            components.getList().add( compForm );
        }
        else
        {
            // On cr�e la liste
            ComponentListForm newComponents = new ComponentListForm();
            newComponents.getList().add( compForm );
            results.put( practiceForm, newComponents );
        }
    }

    /**
     * @param results la map des r�sultats
     * @param practiceName le nom de la pratique � rechercher
     * @return la valeur correspondant � la pratique ou null si elle n'existe pas dans la map
     */
    private ComponentListForm getByName( Map results, String practiceName )
    {
        ComponentListForm value = null;
        Set keys = results.keySet();
        for ( Iterator it = keys.iterator(); it.hasNext() && value == null; )
        {
            ResultForm key = (ResultForm) it.next();
            if ( practiceName.equals( key.getName() ) )
            {
                value = (ComponentListForm) results.get( key );
            }
        }
        return value;
    }

    /**
     * @param results la table � compl�ter
     * @param compForm le composant � ajouter
     * @param practiceForm la pratique servant de cl�
     */
    private void addPractice( Map results, ComponentForm compForm, ResultForm practiceForm )
    {
        // On r�cup�re la liste des r�sultats li�s au composant
        ResultListForm practices = (ResultListForm) getById( results, compForm.getId() );
        if ( null != practices )
        {
            // On ajoute la pratique � la liste des pratiques de compForm
            practices.getList().add( practiceForm );
        }
        else
        {
            // On cr�e la liste
            ResultListForm newPractices = new ResultListForm();
            newPractices.getList().add( practiceForm );
            results.put( compForm, newPractices );
        }

    }

    /**
     * @param results la map des r�sultats
     * @param id l'id du composant � rechercher
     * @return la valeur correspondant � la cl� dont l'id est <code>id</code>
     */
    private ResultListForm getById( Map results, long id )
    {
        ResultListForm value = null;
        Set keys = results.keySet();
        for ( Iterator it = keys.iterator(); it.hasNext() && value == null; )
        {
            ComponentForm key = (ComponentForm) it.next();
            if ( id == key.getId() )
            {
                value = (ResultListForm) results.get( key );
            }
        }
        return value;
    }

    /**
     * @see com.airfrance.welcom.struts.transformer.WITransformer#formToObj(com.airfrance.welcom.struts.bean.WActionForm)
     *      {@inheritDoc}
     */
    public Object[] formToObj( WActionForm pForm )
        throws WTransformerException
    {
        final int maxParams = 3;
        Object[] args = new Object[maxParams];
        formToObj( pForm, args );
        return args;
    }

    /**
     * @see com.airfrance.welcom.struts.transformer.WITransformer#formToObj(com.airfrance.welcom.struts.bean.WActionForm,
     *      java.lang.Object[]) {@inheritDoc}
     */
    public void formToObj( WActionForm form, Object[] object )
        throws WTransformerException
    {
        Object[] args = (Object[]) object;
        EvolutionForm evolutionForm = (EvolutionForm) form;
        // On construit les arguments du filtre
        boolean[] filters = evolutionForm.getFilters();
        if ( filters[PracticeEvolutionDTO.ONLY_UP_OR_DOWN_ID] )
        {
            args[PracticeEvolutionDTO.ONLY_UP_OR_DOWN_ID] = evolutionForm.getFilterOnlyUpOrDown();
        }
        if ( filters[PracticeEvolutionDTO.ONLY_PRACTICES_ID] )
        {
            args[PracticeEvolutionDTO.ONLY_PRACTICES_ID] = evolutionForm.getPractices();
        }
        if ( filters[PracticeEvolutionDTO.THRESHOLD_ID] )
        {
            String[] argsThreshold = new String[] { evolutionForm.getComparisonSign(), evolutionForm.getThreshold() };
            args[PracticeEvolutionDTO.THRESHOLD_ID] = argsThreshold;
        }
    }

}
