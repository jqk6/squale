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
package org.squale.squaleweb.applicationlayer.tracker;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.squale.squaleweb.util.SqualeWebConstants;

/**
 * Classe g�rant le traitement du traceur pseudo-historique
 */
public class Tracker
{

    /** le tracker courant */
    private List mTracker;

    /**
     * Affiche le traceur
     * 
     * @param ts l'objet repr�sentant l'�l�ment courant � ajouter au traceur
     * @param pRequest la requ�te HTTP.
     * @param pReset indique si on est repass� par un menu auquel cas on red�marre une s�rie historique
     */
    public void updateHistTracker( TrackerStructure ts, HttpServletRequest pRequest, boolean pReset )
    {
        // test pour savoir si le traceur doit etre r�initialis� ou pas,
        // il l'est si on repasse par un menu
        if ( pReset )
        {
            this.reset( pRequest );
        }
        // on r�cup�re le traceur courant
        ArrayList currentTracker = (ArrayList) pRequest.getSession().getAttribute( SqualeWebConstants.TRACKER_HIST );
        mTracker = new ArrayList( 0 );
        if ( null != currentTracker )
        {
            mTracker = currentTracker;
        }
        // Ajout
        // on v�rifie qu'on est pas d�j� pass� par l�,
        // si oui on ne fait que r�ajuster la liste du d�but jusqu'au chemin courant
        // en effa�ant les parcours post�rieurs.
        String aux = "";
        int j;
        boolean canExit = false;
        // premier parcours n�cessaire pour v�rifier qu'on a pas cliqu�
        // sur le traceur
        for ( int i = 0; i < mTracker.size() && !canExit; i++ )
        {
            canExit = false;
            aux = ( (String) ( (TrackerStructure) mTracker.get( i ) ).getDisplayName() );
            // on est d�j� pass� par l� (typiquement on a utilis� le traceur)
            // efface les �l�ments post�rieurs en effa�ant l'�l�ment courant
            // qui sera de toute fa�on ajout� � la fin (factorisation de code)
            if ( aux != null && aux.equals( ts.getDisplayName() ) )
            {
                for ( j = i; j < mTracker.size();/* la taille diminue par le remove */)
                {
                    mTracker.remove( j );
                }
                canExit = true;
            }
        }
        // si �a ne vient pas du traceur
        if ( !canExit )
        {
            for ( int i = 0; i < mTracker.size() && !canExit; i++ )
            {
                newTrackerAccessWay( ts, i );
            }
        }
        // r�ajuste la liste
        ( (ArrayList) mTracker ).trimToSize();
        // on ajoute l'�l�ment courant
        mTracker.add( ts );
        pRequest.getSession().setAttribute( SqualeWebConstants.TRACKER_HIST, mTracker );
    }

    /**
     * une m�thode qui met � jour le tracker pour un chemin in�dit
     * 
     * @param pTs la Structure du tracker
     * @param pIndex la position de l'�l�ment courant compar� dans le tracker courant
     */
    private void newTrackerAccessWay( TrackerStructure pTs, int pIndex )
    {
        boolean canExit = false;
        // les �l�ments n'ont pas le type ind�fini mais ont le meme type
        if ( pTs.getType() != TrackerStructure.UNDEFINED
            && pTs.getType() == ( (TrackerStructure) mTracker.get( pIndex ) ).getType() )
        {
            // il faut v�rifier qu'il y a d�j� un �l�ment d'un autre type pr�sent
            // sinon on efface lors de facteurs-criteres-pratiques...
            // car facteur et pratique ont le meme type
            if ( pTs.getType() == TrackerStructure.FACTOR_VIEW )
            {
                for ( int j = mTracker.size() - 1; j > -1; j-- )
                {
                    if ( TrackerStructure.COMPONENT_VIEW == ( (TrackerStructure) mTracker.get( j ) ).getType() )
                    {
                        // on doit v�rifier qu'il y a aussi un element de type facteur view d�ja pr�sent
                        // pour le cas o� on passe par les tops
                        for ( int k = j - 1; k > -1 && !canExit; k-- )
                        {
                            if ( TrackerStructure.FACTOR_VIEW == ( (TrackerStructure) mTracker.get( k ) ).getType() )
                            {
                                // si oui, on peut tout effacer
                                while ( j > -1 )
                                {
                                    mTracker.remove( j-- );
                                }
                                canExit = true;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @param pRequest la requ�te http
     */
    public void reset( HttpServletRequest pRequest )
    {
        pRequest.getSession().setAttribute( SqualeWebConstants.TRACKER_RESET, "false" );
        pRequest.getSession().setAttribute( SqualeWebConstants.TRACKER_HIST, new ArrayList() );
        pRequest.getSession().setAttribute( SqualeWebConstants.TRACKER_COMPONENT, new ArrayList() );
    }

}