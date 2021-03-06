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
package org.squale.squaleweb.applicationlayer.action.results;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.squale.jraf.commons.exception.JrafEnterpriseException;
import org.squale.jraf.helper.AccessDelegateHelper;
import org.squale.jraf.spi.accessdelegate.IApplicationComponent;
import org.squale.squalecommon.datatransfertobject.component.ComponentDTO;
import org.squale.squalecommon.datatransfertobject.result.SqualeReferenceDTO;
import org.squale.squaleweb.applicationlayer.action.accessRights.DefaultAction;
import org.squale.squaleweb.applicationlayer.formbean.LogonBean;
import org.squale.squaleweb.applicationlayer.formbean.reference.ReferenceForm;
import org.squale.squaleweb.applicationlayer.formbean.reference.ReferenceGridForm;
import org.squale.squaleweb.applicationlayer.formbean.reference.SetOfReferencesListForm;
import org.squale.squaleweb.resources.WebMessages;
import org.squale.squaleweb.transformer.ReferenceTransformer;
import org.squale.squaleweb.util.SqualeWebActionUtils;
import org.squale.welcom.struts.transformer.WTransformerException;
import org.squale.welcom.struts.transformer.WTransformerFactory;
import org.squale.welcom.struts.util.WConstants;

/**
 */
public class ReferenceListAction
    extends DefaultAction
{

    /**
     * Affichage des applications du r�f�rentiel
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward list( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                               HttpServletResponse pResponse )
    {

        // Initialisation
        ActionForward forward; // return value
        // ReferenceListForm renseign� ou non
        SetOfReferencesListForm form = (SetOfReferencesListForm) pForm;

        try
        {
            // Renseigne le form
            populateForm( form, pRequest );
            // met un nom de grille avec la date afin de distinguer les diff�rentes mises � jour
            // setDisplayedGridName(pRequest, form);
            // Provoquer le forward
            forward = pMapping.findForward( "success" );
        }
        catch ( Exception e )
        {
            ActionErrors errors = new ActionErrors();
            handleException( e, errors, pRequest );
            saveMessages( pRequest, errors );
            forward = pMapping.findForward( "failure" );

        }
        // Finish with
        return forward;

    }

    /**
     * Peuplement du form avec les liste des r�f�rences Le form pass� en param�tre est mis � jour pour contenir les
     * objets � afficher dans la JSP Les applications de l'utilisateur sont affich�es si celui-ci n'est pas
     * administrateur du portail
     * 
     * @param pForm form
     * @param pRequest requ�te
     * @throws JrafEnterpriseException exception
     * @throws WTransformerException exception
     */
    public void populateForm( SetOfReferencesListForm pForm, HttpServletRequest pRequest )
        throws WTransformerException, JrafEnterpriseException
    {
        List applicationsList = new ArrayList( 0 );
        // R�cup�ration de la liste des applications
        // Toutes si on est admin, les publiques et celles dont l'utilisateur est gestionnaire sinon
        if ( isUserAdmin( pRequest ) )
        {
            applicationsList = getUserAdminApplicationListAsDTO( pRequest );
        }
        else
        {
            applicationsList = getUserApplicationListAsDTO( pRequest );
        }

        // On v�rifie que l'on r�cup�re que les applications qui sont effectivement stock�es en base et non pas que
        // celles
        // qui ont des r�sultats (cas pouvant arriver dans des conditions particuli�res)
        IApplicationComponent ac = AccessDelegateHelper.getInstance( "Validation" );
        Collection referencedApplis = (Collection) ac.execute( "listReferentiel" );
        // les applications de l'utilisateur qui sont �galement r�f�renc�s
        List commonApplis = new ArrayList( 0 );
        for ( int i = 0; i < applicationsList.size(); i++ )
        {
            if ( referencedApplis.contains( ( (ComponentDTO) applicationsList.get( i ) ).getName() ) )
            {
                commonApplis.add( ( (ComponentDTO) applicationsList.get( i ) ).getName() );
            }
        }

        // Comme le form est en session, on le remet � jour
        SetOfReferencesListForm newForm = new SetOfReferencesListForm();
        // R�cup�re les r�f�rences et les structures
        getReferences( commonApplis, isUserAdmin( pRequest ), pRequest, newForm );
        // Remet � jour le form en session
        pRequest.getSession().removeAttribute( "setOfReferencesListForm" );
        pRequest.getSession().setAttribute( "setOfReferencesListForm", newForm );

    }

    /**
     * Permet de r�cup�rer tous les projets qui se trouvent dans le r�f�rentiel Les r�sultats renvoy�s sont rendus
     * anonymes en fonction du privil�ge administrateur, des projets connus par l'utilisateur et du caract�re public du
     * r�f�rentiel
     * 
     * @param pApplicationsList liste des applications de l'utilisateur
     * @param pAdmin indique un privil�ge administrateur
     * @param pRequest la requete http
     * @param pSetForm le form g�n�ral
     * @throws JrafEnterpriseException exception
     * @throws WTransformerException exception
     */
    private void getReferences( List pApplicationsList, boolean pAdmin, HttpServletRequest pRequest,
                                SetOfReferencesListForm pSetForm )
        throws JrafEnterpriseException, WTransformerException
    {
        // Obtention des r�sultats
        IApplicationComponent ac = AccessDelegateHelper.getInstance( "Results" );
        LogonBean sessionUser = (LogonBean) pRequest.getSession().getAttribute( WConstants.USER_KEY );
        Object[] paramIn = { null, null, new Boolean( pAdmin ), new Long( sessionUser.getId() ) };
        List references = ( (List) ac.execute( "getReference", paramIn ) );
        SqualeReferenceDTO squaleReference = null;
        ReferenceForm refForm = null;
        Iterator referencesIterator = references.iterator();
        while ( referencesIterator.hasNext() )
        {
            squaleReference = (SqualeReferenceDTO) referencesIterator.next();
            // Conversion welcom
            Object[] obj = { squaleReference };
            refForm = (ReferenceForm) WTransformerFactory.objToForm( ReferenceTransformer.class, obj );
            // On rend les projets anonymes si on n'est pas admin, qu'on a pas les droits de lecture
            // et s'il ne sont pas publics
            if ( !pAdmin && !refForm.isPublic() && !pApplicationsList.contains( refForm.getApplicationName() ) )
            {
                anonymize( refForm, pRequest );
            }
            // Met � jour le caract�re masqu� ou affich�
            if ( refForm.isHidden() )
            {
                refForm.setState( WebMessages.getString( pRequest, ReferenceForm.HIDDEN ) );
            }
            else
            {
                refForm.setState( WebMessages.getString( pRequest, ReferenceForm.DISPLAYED ) );
            }
            // On convertit la grille qualit� associ�e
            ReferenceGridForm gridForm = new ReferenceGridForm();
            gridForm.setName( squaleReference.getGrid().getName() );
            gridForm.setUpdateDate( squaleReference.getGrid().getUpdateDate() );
            gridForm.setFormattedDate( SqualeWebActionUtils.getFormattedDate(
                                                                              squaleReference.getGrid().getUpdateDate(),
                                                                              pRequest.getLocale() ) );
            pSetForm.add( gridForm, refForm );
        }
    }

    /**
     * Anonymisation du form de r�f�rence Les projets qui ne font pas partie des projets de l'utilisateur sont rendus
     * anonymes en effa�ant le nom de l'application et du projet
     * 
     * @param pReferenceForm form de la r�f�rence
     * @param pRequest la requete http
     */
    private void anonymize( ReferenceForm pReferenceForm, HttpServletRequest pRequest )
    {
        // Effacement des informations
        String textAnonym = WebMessages.getString( pRequest, "reference.project.anonyme" );
        pReferenceForm.setApplicationName( textAnonym );
        String projectName = textAnonym;
        pReferenceForm.setName( projectName );
    }
}
