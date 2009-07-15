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
package org.squale.squaleweb.util.graph;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import org.squale.squaleweb.resources.WebMessages;

/**
 */
public class AuditTimeMaker
    extends AbstractStatsMaker
{

    /**
     * cl� pour le titre du diagramme par d�faut
     */
    private static final String DEFAULT_TITLE_KEY = "stats.time.title";

    /**
     * cl� pour l'axe des abscisses du diagramme par d�faut
     */
    private static final String DEFAULT_XAXIS_LABEL_KEY = "stats.time.xlabel";

    /**
     * cl� pour l'axe des ordonn�es du diagramme par d�faut
     */
    private static final String DEFAULT_YAXIS_LABEL_KEY = "stats.time.ylabel";

    /**
     * Constructeur avec les param�tres servant pour le graph cliquable
     * 
     * @param pRequest la requete pour avoir des cl�s internationalis�s
     */
    public AuditTimeMaker( HttpServletRequest pRequest )
    {
        this( WebMessages.getString( pRequest, DEFAULT_TITLE_KEY ), WebMessages.getString( pRequest,
                                                                                           DEFAULT_XAXIS_LABEL_KEY ),
              WebMessages.getString( pRequest, DEFAULT_YAXIS_LABEL_KEY ) );
    }

    /**
     * Constructeur
     * 
     * @param pTitle le titre
     * @param pXLabel le label des abscisses
     * @param pYLabel le label des ordonn�es
     */
    public AuditTimeMaker( String pTitle, String pXLabel, String pYLabel )
    {
        mTitle = pTitle;
        mXLabel = pXLabel;
        mYLabel = pYLabel;
        mDataSet = new DefaultCategoryDataset();
    }

    /** le renderer du graph */
    private StackedBarRenderer mRenderer;

    /**
     * @return le diagramme JFreeChart
     */
    public JFreeChart getChart()
    {
        JFreeChart retChart = super.getChart();
        if ( null == retChart )
        {
            // Appel � la factory pour cr�er le diagramme
            retChart =
                ChartFactory.createStackedBarChart( mTitle, mXLabel, mYLabel, mDataSet, PlotOrientation.VERTICAL, true,
                                                    false, false );
            CategoryPlot plot = retChart.getCategoryPlot();
            ValueAxis xAxis = plot.getRangeAxis();
            plot.setRangeAxisLocation( AxisLocation.BOTTOM_OR_LEFT );
            // Le renderer pour avoir un diagramme en baton avec empilement
            mRenderer = (StackedBarRenderer) plot.getRenderer();
            // Appel de la m�thode g�rant les couleurs du graphe
            // met le fond blanc
            retChart.setBackgroundPaint( Color.WHITE );
        }
        return retChart;
    }

    /**
     * Pour pouvoir affecter une couleur � une s�rie donn�e, permet d'avoir une coh�rence dans le graphe
     */
    private Map mapColumnDateToSeriesIndex = new HashMap();

    /**
     * @param date la date
     * @param pIndex l'index de la s�rie
     */
    private void manageMapping( String date, Integer pIndex )
    {
        Map map = (Map) mapColumnDateToSeriesIndex.get( date );
        if ( map == null )
        {
            map = new HashMap();
            map.put( pIndex, new Integer( 0 ) );
        }
        else
        {
            map.put( pIndex, new Integer( map.values().size() ) );
        }
        mapColumnDateToSeriesIndex.put( date, map );

    }

    /**
     * Affecte les valeurs du grah
     * 
     * @param pValues list des valeurs, une liste de tableaux d'objets � 3 �l�ments, le premier �tant la date de
     *            l'audit, la deuxi�me la dur�e de l'audit, le troisi�me le nom de l'application
     */
    public void setValues( List pValues )
    {
        // Pour convertir en pourcentage
        int total = 0;
        getFormattedList( pValues );
        for ( int i = 0; i < pValues.size(); i++ )
        {
            Object[] combo = (Object[]) pValues.get( i );
            String date = (String) combo[0];
            Number time = (Number) combo[1];
            Integer counterObject = new Integer( i );
            manageMapping( date, counterObject );
            ( (DefaultCategoryDataset) mDataSet ).addValue( time, (String) combo[2], date );
        }
    }

    /**
     * Formatte les valeurs rencontr�es La dur�e est pr�sent�e sous forme de chaine, on la convertit en nombre suivant
     * le template d�fini
     * 
     * @param pValues la liste des tableaux d'objets contenant la date et la dur�e sous forme de chaine
     */
    private void getFormattedList( List pValues )
    {
        int hours_in_mn = 0;
        int min = 0;
        Integer result = new Integer( -1 );
        for ( int i = 0; i < pValues.size(); i++ )
        {
            String stringDuration = (String) ( (Object[]) pValues.get( i ) )[1];
            if ( stringDuration != null )
            {
                // On v�rifie que la chaine a bien le format ad�quat, sinon on renverra 0
                if ( stringDuration.indexOf( ":" ) != -1 )
                {
                    String nbHours = stringDuration.substring( 0, stringDuration.indexOf( ":" ) );
                    final int NB_MINUTES_IN_AN_HOUR = 60;
                    hours_in_mn = Integer.parseInt( nbHours ) * NB_MINUTES_IN_AN_HOUR;
                    String nbMinutes =
                        stringDuration.substring( stringDuration.indexOf( ":" ) + 1, stringDuration.length() );
                    min = Integer.parseInt( nbMinutes );
                }
            }
            // Calcule la dur�e correspondant � la chaine
            result = new Integer( hours_in_mn + min );
            // insert � la place de la chaine le nombre correspondant � la dur�e
            ( (Object[]) pValues.get( i ) )[1] = result;
        }
    }
}