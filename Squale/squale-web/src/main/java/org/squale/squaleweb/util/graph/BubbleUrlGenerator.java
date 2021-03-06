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
/*
 * Cr�� le 24 janv. 07
 *
 * Personnalisation des liens URL du graphe de type Bubble
 */
package org.squale.squaleweb.util.graph;

import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.data.xy.XYDataset;

import org.squale.squaleweb.applicationlayer.action.results.project.TopAction;

/**
 * @author 6370258 Classe de g�n�ration de liens sp�cifiques � chaque point du graphe de type Bubble
 */
public class BubbleUrlGenerator
    extends AbstractURLGenerator
    implements XYURLGenerator
{
    /**
     * Array of vg values
     */
    private double[] tbVgs;

    /**
     * Array of evg values
     */
    private double[] tbEvgs;

    /** vg metrics tres */
    private String mVgTre;

    /** evg metrics tres */
    private String mEvgTre;

    /**
     * Constructeur par d�faut
     */
    public BubbleUrlGenerator()
    {
    }

    /**
     * Constructeur
     * 
     * @param pProjectId Id du projet
     * @param pAuditId Id de l'audit courant
     * @param pPreviousAuditId l'id de l'audit pr�c�dent
     * @param pVgs array of vg values
     * @param pEvgs array of evg values
     * @param vgTre vg tre
     * @param evgTre evg tre
     */
    public BubbleUrlGenerator( String pProjectId, String pAuditId, String pPreviousAuditId, double[] pVgs,
                               double[] pEvgs, String vgTre, String evgTre)
    {
        // Initialisation des attributs
        mProjectId = pProjectId;
        mCurrentAuditId = pAuditId;
        mPreviousAuditId = pPreviousAuditId;
        mVgTre = vgTre;
        mEvgTre = evgTre;
        tbVgs = pVgs;
        tbEvgs = pEvgs;

    }

    /**
     * G�n�ration du lien URL
     * 
     * @param pDataset le DataSet du graphe
     * @param pSeries la s�rie du graphe
     * @param pItem le point du graphe
     * @return url l'url de la portion du Scatterplott
     */
    public String generateURL( XYDataset pDataset, int pSeries, int pItem )
    {
        String url =
            "component_tres.do?action=displayComponents&projectId=" + mProjectId + "&currentAuditId=" + mCurrentAuditId
                + "&" + TopAction.TRE_KEYS_KEYWORD + "=" + mVgTre + "," + mEvgTre + "&" + TopAction.TRE_VALUES_KEYWORD + "=" + tbVgs[pItem] + "," + tbEvgs[pItem];
        // On ne rajout l'audit pr�c�dent � l'url que si il est valide
        if ( mPreviousAuditId != null && !"-1".equals( mPreviousAuditId ) )
        {
            url += "&previousAuditId=" + mPreviousAuditId;
        }
        return url;
    }
}
