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

import org.squale.squalecommon.datatransfertobject.component.ComponentDTO;
import org.squale.squaleweb.applicationlayer.formbean.results.ComponentForm;
import org.squale.squaleweb.resources.WebMessages;
import org.squale.welcom.struts.bean.WActionForm;
import org.squale.welcom.struts.transformer.WITransformer;
import org.squale.welcom.struts.transformer.WTransformerException;

/**
 * Transformer ComponentDTO <-> ComponentForm
 * 
 * @author M400842
 */
public class ComponentTransformer
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
        ComponentForm form = new ComponentForm();
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
        ComponentDTO dto = (ComponentDTO) pObject[0];
        ComponentForm form = (ComponentForm) pForm;
        form.setId( dto.getID() );
        form.setName( dto.getName() );
        form.setParentId( dto.getIDParent() );
        form.setType( dto.getType() );
        form.setFileName( dto.getFileName() );
        form.setExcludedFromActionPlan( dto.getExcludedFromActionPlan() );
        form.setJustification( dto.getJustification() );
        form.setFullName( dto.getFullName() );
    }
    
    
    
    
    /**
     * Transforme le 1er componentDTO d'une liste dans un formulaire 
     * @param pObject le tableau de ComponentDTO � transformer
     * @param pLanguage le langage associ� au projet
     * @return le formulaire trait�
     */
    public static ComponentForm objToFormWithLanguage ( Object[] pObject, String pLanguage)
    {
    	ComponentForm form = new ComponentForm();
    	objToFormWithLanguage( pObject, pLanguage, form);
    	return form;
    }
    
    /**
     * Construit et modifie le ComponentForm suivant le langage
     * @param pObject la liste de ComponentDTO � traiter
     * @param pLanguage le langage associ� au projet
     * @param pForm le formulaire � traiter
     */
    public static void objToFormWithLanguage ( Object[] pObject, String pLanguage, ComponentForm pForm)
    {
    	ComponentForm form = (ComponentForm) pForm;
    	ComponentDTO dto = (ComponentDTO) pObject[0];
        form.setId( dto.getID() );
        form.setName( dto.getName() );
        form.setParentId( dto.getIDParent() );
        form.setFileName( dto.getFileName() );
        form.setExcludedFromActionPlan( dto.getExcludedFromActionPlan() );
        form.setJustification( dto.getJustification() );
        form.setFullName( dto.getFullName() );
        
        String type = dto.getType();
    	// On va v�rifier que la cl� d�bute par component. et qu'elle existe bien avec le langage en plus
    	if ( type.startsWith("component.") & 
    			WebMessages.existString( type + "." + pLanguage)  )
    	{
    		type = type + "." + pLanguage;
    	}
        
        form.setType( type );
    	
    }

    /**
     * @param pForm le formulaire � lire.
     * @throws WTransformerException si un pb apparait.
     * @return le tableaux des objets.
     */
    public Object[] formToObj( WActionForm pForm )
        throws WTransformerException
    {
        Object[] obj = { new ComponentDTO() };
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
        ComponentForm form = (ComponentForm) pForm;
        ComponentDTO dto = (ComponentDTO) pObject[0];
        dto.setID( form.getId() );
        dto.setIDParent( form.getParentId() );
        dto.setType( form.getType() );
        dto.setName( form.getName() );
        dto.setIDParent( form.getParentId() );
        dto.setFileName( form.getFileName() );
        dto.setExcludedFromActionPlan( form.getExcludedFromActionPlan() );
        dto.setJustification( form.getJustification() );
    }

    /**
     * Transforme un ComponentForm en ComponentDTO en modifiant le type suivant le langage
     * @param pForm le ComponentForm � traiter
     * @param pLanguage le langage associ� au projet
     * @return une liste contenant un ComponentDTO trait�
     */
    public static Object[] formToObjWithLanguage ( ComponentForm pForm, String pLanguage)
    {
    	Object[] obj = { new ComponentDTO() };
    	formToObjWithLanguage( obj, pLanguage, pForm);
    	return obj;
    }
    
    /**
     * Construit et modifie le ComponentDTO en retirant le langage du type de Component
     * @param pObject la liste d'objets cr��s dont on va modifier le 1er
     * @param pLanguage le langage associ� au projet
     * @param pForm le formulaire � traiter
     */
    public static void formToObjWithLanguage ( Object[] pObject, String pLanguage, ComponentForm pForm )
    {
    	ComponentForm form = (ComponentForm) pForm;
    	ComponentDTO dto = (ComponentDTO) pObject[0];
        dto.setID( form.getId() );
        dto.setIDParent( form.getParentId() );
        dto.setName( form.getName() );
        dto.setIDParent( form.getParentId() );
        dto.setFileName( form.getFileName() );
        dto.setExcludedFromActionPlan( form.getExcludedFromActionPlan() );
        dto.setJustification( form.getJustification() );
        
        String type = form.getType();
        if ( type.endsWith(pLanguage) )
        {
        	Integer end = type.length() - ( 1 + pLanguage.length() );
        	type=type.substring(0, end);
        }
        dto.setType( type );
    }
    
    
}
