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
package org.squale.squaleweb.applicationlayer.action.grid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.squale.jraf.helper.AccessDelegateHelper;
import org.squale.jraf.spi.accessdelegate.IApplicationComponent;
import org.squale.squalecommon.datatransfertobject.component.ComponentDTO;
import org.squale.squalecommon.datatransfertobject.rule.PracticeRuleDTO;
import org.squale.squaleweb.applicationlayer.action.ActionUtils;
import org.squale.squaleweb.applicationlayer.action.accessRights.ReaderAction;
import org.squale.squaleweb.applicationlayer.formbean.results.ResultForm;
import org.squale.squaleweb.applicationlayer.formbean.results.ResultListForm;
import org.squale.squaleweb.applicationlayer.tracker.TrackerStructure;
import org.squale.squaleweb.comparator.ResultFormComparator;
import org.squale.squaleweb.resources.WebMessages;
import org.squale.squaleweb.transformer.ProjectTransformer;
import org.squale.welcom.struts.transformer.WTransformerFactory;

/**
 * 
 */
public class PracticesSummaryAction
    extends ReaderAction
{

    /**
     * Importation d'un fichier de grille
     * 
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward meteo( ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest,
                                HttpServletResponse pResponse )
    {
        ActionErrors errors = new ActionErrors();
        ActionForward forward = pMapping.findForward( "noPracticesDefined" );
        ArrayList list = new ArrayList( 0 );

        try
        {
            // ici la liste des audits est forc�ment != null
            List auditsDTO = ActionUtils.getCurrentAuditsAsDTO( pRequest );
            ComponentDTO projectDTO =
                (ComponentDTO) WTransformerFactory.formToObj( ProjectTransformer.class,
                                                              ActionUtils.getCurrentProject( pRequest ) )[0];

            // On va r�cup�rer la grille qualit� correspondant au dernier audit
            IApplicationComponent acResult = AccessDelegateHelper.getInstance( "Results" );
            Collection practicesAndMarks =
                (Collection) acResult.execute( "getAllPractices", new Object[] { auditsDTO.get( 0 ), projectDTO } );
            // Audit pr�c�dent si d�fini
            Collection practicesAndMarksOld = null;
            if ( auditsDTO.size() > 1 )
            {
                practicesAndMarksOld =
                    (Collection) acResult.execute( "getAllPractices", new Object[] { auditsDTO.get( 1 ), projectDTO } );

            }
            // d�finition et initialisation � null des diff�rents �l�ments
            // concernant les anciens r�sultats
            Collection practicesOld = null;
            Collection marksOld = null;
            // Si des anciens r�sultats existent, alors on initialise correctement
            // tous les �l�ments permettant de r�cup�rer ces r�sultats
            if ( practicesAndMarksOld != null )
            {
                Iterator practicesAndOldMarksIt = practicesAndMarksOld.iterator();
                practicesOld = (Collection) practicesAndOldMarksIt.next();
                marksOld = (Collection) practicesAndOldMarksIt.next();
            }
            // collection de practiceRuleDto
            Iterator practicesAndMarksIt = practicesAndMarks.iterator();
            Collection practices = (Collection) practicesAndMarksIt.next();
            Iterator practicesIt = practices.iterator();
            Collection marks = (Collection) practicesAndMarksIt.next();
            Iterator marksIt = marks.iterator();
            while ( practicesIt.hasNext() && marksIt.hasNext() )
            {
                // La pratique et la note associ�e ont le meme index
                Float mark = (Float) marksIt.next();
                PracticeRuleDTO practice = (PracticeRuleDTO) practicesIt.next();
                String practiceName = practice.getName();
                ResultForm f = new ResultForm();
                // R�cup�re la note pr�c�dente pour cette pratique si elle existe,
                // sinon on ne mettra rien
                if ( practicesAndMarksOld != null )
                {
                    Float oldMark = getPreviousMark( practicesOld, marksOld, practice.getId() );
                    if ( oldMark != null )
                    {
                        f.setPredecessorMark( oldMark.toString() );
                        // Si il y a une note pr�c�dente alors les audits sont comparables, on l'indique
                        ( (ResultListForm) pForm ).setComparableAudits( true );
                    }
                }
                // Positionne les champs pour cette pratique et pour la note courante
                if ( mark != null )
                {
                    f.setId( "" + practice.getId() );
                    f.setName( practiceName );
                    f.setCurrentMark( mark.toString() );
                    // si on n'a pas pu remplir le champ note pr�c�dente, la tendance sera �gale
                    // cf. TrendTag
                    list.add( f );
                }
            }
            Collections.sort( list, new ResultFormComparator() );
            ( (ResultListForm) pForm ).setList( list );
            // On est pass� par un menu donc on r�initialise le traceur
            // On ajoute le lien de la page pour avoir le lien lors de la s�lection d'une pratique
            updateHistTracker( WebMessages.getString( pRequest, "practices.meteo" ), "meteo.do?action=meteo",
                               TrackerStructure.TOP_VIEW, pRequest, true );
        }
        catch ( Exception e )
        {
            // Traitement des exceptions
            handleException( e, errors, pRequest );
            forward = pMapping.findForward( "total_failure" );
        }
        if ( !errors.isEmpty() )
        {
            saveMessages( pRequest, errors );
        }
        return forward;
    }

    /**
     * PRECONDITION: les 2 collections ne sont pas null et ont la meme taille, renvoie null sinon
     * 
     * @param pPractices les pratiques
     * @param pMarks les notes associ�s aux pratiques
     * @param pPracticeId l'id de la pratique dont on veut la note
     * @return la note de l'audit pr�c�dent pour la practice pPractice si elle existe, null sinon
     */
    private Float getPreviousMark( Collection pPractices, Collection pMarks, long pPracticeId )
    {
        Float result = null;
        // V�rifie que les 2 collections ont bien la meme taille car on incr�mente
        // les 2 a chaque fois
        // on ne v�rifie pas le fait qu'elles ne soient pas nulles
        if ( pPractices.size() == pMarks.size() )
        {
            Iterator practicesIt = pPractices.iterator();
            Iterator marksIt = pMarks.iterator();
            // Cherche dans l'ensemble des pratiques la pratique pass� en param�tre,
            // et si elle existe renvoie sa note (peut etre null)
            while ( result == null && practicesIt.hasNext() )
            {
                PracticeRuleDTO practiceOld = (PracticeRuleDTO) practicesIt.next();
                Float previousMark = (Float) marksIt.next();
                if ( practiceOld.getId() == pPracticeId )
                {
                    result = previousMark;
                }
            }
        }
        return result;
    }

}
