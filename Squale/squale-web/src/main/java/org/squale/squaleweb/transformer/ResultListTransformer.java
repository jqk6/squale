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
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.squale.squalecommon.datatransfertobject.result.ResultsDTO;
import org.squale.squalecommon.datatransfertobject.rule.QualityRuleDTO;
import org.squale.squaleweb.applicationlayer.formbean.results.ResultForm;
import org.squale.squaleweb.applicationlayer.formbean.results.ResultListForm;
import org.squale.squaleweb.util.SqualeWebActionUtils;
import org.squale.welcom.struts.bean.WActionForm;
import org.squale.welcom.struts.transformer.WTransformerException;
import org.squale.squaleweb.resources.WebMessages;

/**
 * Transforme des listes de r�sultats en un formulaiire ad�quat.
 * 
 * @author M400842
 */
public class ResultListTransformer
    extends AbstractListTransformer
{

    /**
     * Logger
     */
    private static final Log LOGGER = LogFactory.getLog( ResultListTransformer.class );

    /**
     * @param pObject le tableau de ProjectDTO � transformer en formulaires.
     * @throws WTransformerException si un pb appara�t.
     * @return le formulaire associ�
     */
    public WActionForm objToForm( Object[] pObject )
        throws WTransformerException
    {
        ResultListForm form = new ResultListForm();
        objToForm( pObject, form );
        return form;
    }

    /**
     * @param pObject le tableau de ProjectDTO � transformer en formulaires.
     * @param pForm le formulaire � remplir.
     * @throws WTransformerException si un pb appara�t.
     */
    public void objToForm( Object[] pObject, WActionForm pForm )
        throws WTransformerException
    {
        List tre = (List) pObject[0];
        // On supprime les objets nuls
        tre.remove( null );
        // Concersion en liste de String
        List values = SqualeWebActionUtils.getAsStringsList( (List) pObject[1] );
        ArrayList result = new ArrayList();
        ResultListForm form = (ResultListForm) pForm;
        ListIterator it = tre.listIterator();
        ResultForm resultForm = null;
        // On r�cup�re les r�gles
        while ( it.hasNext() )
        {
            resultForm = new ResultForm();
            Object currentTre = it.next();
            // sous forme de TRE ou sous forme de QualityResultDTO
            if ( currentTre instanceof String )
            {
                resultForm.setName( (String) currentTre );
            }
            else
            {
                QualityRuleDTO rule = (QualityRuleDTO) currentTre;
                resultForm.setName( rule.getName() );
                // Dans ce cas on renseigne aussi l'id de la r�gle
                resultForm.setId( "" + rule.getId() );
            }
            try
            {
                String value = (String) values.get( it.previousIndex() );
                // Cas o� la r�gle n'a pa de note
                if ( value == null || value.startsWith( "-" ) )
                {
                    value = "-";
                }
                // formatage HTML
                value = value.replaceAll( " ", "&nbsp;" );
                resultForm.setCurrentMark( value.replaceAll( "\n", "<br/>" ) );
            }
            catch ( Exception e )
            {
                // TODO voir comment ce code est activ�
                // l'acc�s � it.previousIndex() est �trange
                LOGGER.debug( e, e );
            }
            result.add( resultForm );
        }
        form.setList( result );
    }

    /**
     * @param pForm le formulaire � lire.
     * @param pObject le tableau de ProjectDTO qui r�cup�re les donn�es du formulaire.
     * @throws WTransformerException si un pb appara�t.
     */
    public void formToObj( WActionForm pForm, Object[] pObject )
        throws WTransformerException
    {
        ArrayList listObject = (ArrayList) pObject[0];
        List listForm = ( (ResultListForm) pForm ).getList();
        ResultsDTO dto = null;
        for ( int i = 0; i < listForm.size(); i++ )
        {
            dto = new ResultsDTO();
            listObject.add( dto );
        }
    }
    
    /**
     * @param pObject le tableau de ProjectDTO � transformer en formulaires.
     * @param pLanguage le langage associ� au projet
     * @throws WTransformerException si un pb appara�t.
     * @return le formulaire associ�
     */
    public static ResultListForm objToFormWithLanguage( Object[] pObject, String pLanguage )
        throws WTransformerException
    {
        ResultListForm form = new ResultListForm();
        objToFormWithLanguage( pObject, form , pLanguage);
        return form;
    }
    
    /**
     * @param pObject le tableau de ProjectDTO � transformer en formulaires.
     * @param pForm le formulaire � remplir.
     * @param pLanguage le langage de programmation du projet
     * @throws WTransformerException si un pb appara�t.
     */
    public static void objToFormWithLanguage( Object[] pObject, WActionForm pForm, String pLanguage )
        throws WTransformerException
    {
        List tre = (List) pObject[0];
        // On supprime les objets nuls
        tre.remove( null );
        // Concersion en liste de String
        List values = SqualeWebActionUtils.getAsStringsList( (List) pObject[1] );
        ArrayList result = new ArrayList();
        ResultListForm form = (ResultListForm) pForm;
        ListIterator it = tre.listIterator();
        ResultForm resultForm = null;
        // On r�cup�re les r�gles
        while ( it.hasNext() )
        {
            resultForm = new ResultForm();
            Object currentTre = it.next();
            // sous forme de TRE ou sous forme de QualityResultDTO
            if ( currentTre instanceof String )
            {
            	String name = (String) currentTre;
            	if ( WebMessages.existString(currentTre + "." + pLanguage))
            	{
            		name = name + "." + pLanguage;
            	}
                resultForm.setName( (String) name );
            }
            else
            {
                QualityRuleDTO rule = (QualityRuleDTO) currentTre;
                String ruleName = rule.getName();
                if ( WebMessages.existString(ruleName + "." + pLanguage) )
                {
                	ruleName = ruleName + "." + pLanguage;
                }
                resultForm.setName( ruleName );
                // Dans ce cas on renseigne aussi l'id de la r�gle
                resultForm.setId( "" + rule.getId() );
            }
            try
            {
                String value = (String) values.get( it.previousIndex() );
                // Cas o� la r�gle n'a pa de note
                if ( value == null || value.startsWith( "-" ) )
                {
                    value = "-";
                }
                // formatage HTML
                value = value.replaceAll( " ", "&nbsp;" );
                resultForm.setCurrentMark( value.replaceAll( "\n", "<br/>" ) );
            }
            catch ( Exception e )
            {
                // TODO voir comment ce code est activ�
                // l'acc�s � it.previousIndex() est �trange
                LOGGER.debug( e, e );
            }
            result.add( resultForm );
        }
        form.setList( result );
    }

}
