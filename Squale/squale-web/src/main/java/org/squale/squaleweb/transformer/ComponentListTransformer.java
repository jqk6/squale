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
import java.util.ListIterator;

import org.squale.squalecommon.datatransfertobject.component.ComponentDTO;
import org.squale.squaleweb.applicationlayer.formbean.results.ComponentForm;
import org.squale.squaleweb.applicationlayer.formbean.results.ComponentListForm;
import org.squale.welcom.struts.bean.WActionForm;
import org.squale.welcom.struts.transformer.WTransformerException;
import org.squale.welcom.struts.transformer.WTransformerFactory;

/**
 * @author M400842
 */
public class ComponentListTransformer
    extends AbstractListTransformer
{

    /**
     * @param pObject le tableau de ComponentDTO � transformer en formulaires.
     * @throws WTransformerException si un pb appara�t.
     * @return le formulaire associ�
     */
    public WActionForm objToForm( Object[] pObject )
        throws WTransformerException
    {
        ComponentListForm form = new ComponentListForm();
        objToForm( pObject, form );
        return form;
    }

    /**
     * @param pObject le tableau de ComponentDTO � transformer en formulaires.
     * @param pForm le formulaire � remplir.
     * @throws WTransformerException si un pb appara�t.
     */
    public void objToForm( Object[] pObject, WActionForm pForm )
        throws WTransformerException
    {
        ArrayList listDTO = (ArrayList) pObject[0];
        ArrayList result = new ArrayList();
        ListIterator it = listDTO.listIterator();
        while ( it.hasNext() )
        {
            result.add( WTransformerFactory.objToForm( ComponentTransformer.class, (ComponentDTO) it.next() ) );
        }
        ComponentListForm form = (ComponentListForm) pForm;
        form.setList( result );
    }

    /**
     * @param pForm le formulaire � lire.
     * @param pObject le tableau deComponentDTO qui r�cup�re les donn�es du formulaire.
     * @throws WTransformerException si un pb appara�t.
     */
    public void formToObj( WActionForm pForm, Object[] pObject )
        throws WTransformerException
    {
        ArrayList listObject = (ArrayList) pObject[0];
        if ( null != ( (ComponentListForm) pForm ).getList() )
        {
            ListIterator it = ( (ComponentListForm) pForm ).getList().listIterator();
            while ( it.hasNext() )
            {
                listObject.add( WTransformerFactory.formToObj( ComponentTransformer.class, (ComponentForm) it.next() )[0] );
            }
        }
    }
    
    
    /**
     * Construit la liste des composants en ofrmulaire en modifiant le type suivant le langage
     * @param pObject le tableau de ComponentDTO � transformer
     * @param pLanguage le langage associ� au projet
     * @return le formulaire trait�
     */
    public static ComponentListForm objToFormWithLanguage ( Object[] pObject, String pLanguage)
    {
    	ComponentListForm form = new ComponentListForm();
    	objToFormWithLanguage(pObject, pLanguage, form);
    	return form;
    }
    
    /**
     * Construit la liste des composants en modifiant les types suivant le langage
     * @param pObject le tableau de ComponentDTO � transformer en formulaires
     * @param pLanguage le langage du projet
     * @param le formulaire � traiter
     */
    public static void objToFormWithLanguage ( Object[] pObject, String pLanguage, ComponentListForm pForm)
    {
    	ComponentListForm form = pForm;
    	ArrayList listDTO = (ArrayList) pObject[0];
    	ArrayList result = new ArrayList();
    	ListIterator it = listDTO.listIterator();
    	while ( it.hasNext() )
        {
            result.add( ComponentTransformer.objToFormWithLanguage( new Object[] {it.next()}, pLanguage ));
        }
        form.setList( result );
    }
    
}
