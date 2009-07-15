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
package com.airfrance.squaleweb.util.graph;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Fabrique pour un histogramme
 */
public class BarMaker
    extends AbstractGraphMaker
{

    /**
     * Hauteur du diagramme par d�faut
     */
    public static final int DEFAULT_HEIGHT = 400;

    /**
     * Hauteur du diagramme par d�faut
     */
    public static final int DEFAULT_WIDTH = 400;

    /**
     * dataset contenant les valeurs � mettre dans le diagramme
     */
    private DefaultCategoryDataset mDataSet;

    /**
     * @see com.airfrance.squalecommon.util.graph.AbstractGraphMaker#getDefaultHeight()
     * @return la hauteur par d�faut
     */
    protected int getDefaultHeight()
    {
        return DEFAULT_HEIGHT;
    }

    /**
     * @see com.airfrance.squalecommon.util.graph.AbstractGraphMaker#getDefaultWidth()
     * @return la largeur par d�faut
     */
    protected int getDefaultWidth()
    {
        return DEFAULT_WIDTH;
    }

    /**
     * Constructeur avec le titre du diagramme et les titres des axes
     * 
     * @param pTitle titre du diagramme
     * @param pXLabel titre de l'axe des abscisses
     * @param pYLabel titre de l'axe des ordonn�es
     */
    public BarMaker( String pTitle, String pXLabel, String pYLabel )
    {
        mDataSet = new DefaultCategoryDataset();
        mTitle = pTitle;
        mXLabel = pXLabel;
        mYLabel = pYLabel;
    }

    /**
     * Ajoute une valeur � l'histogramme
     * 
     * @param pValue la valeur
     * @param pTitle1 le titre correspondant � l'abscisse.
     * @param pTitle2 le titre correspondant � l'ordonn�e.
     */
    public void addValue( Number pValue, String pTitle1, String pTitle2 )
    {
        mDataSet.setValue( pValue, pTitle1, pTitle2 );

    }

    /**
     * @return le diagramme JFreeChart
     */
    protected JFreeChart getChart()
    {
        JFreeChart retChart = super.getChart();
        if ( null == retChart )
        {
            retChart =
                ChartFactory.createBarChart3D( mTitle, mXLabel, mYLabel, mDataSet, PlotOrientation.VERTICAL, false,
                                               false, false );

            CategoryPlot plot = retChart.getCategoryPlot();

            // Les valeurs sont des entiers
            final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            rangeAxis.setStandardTickUnits( NumberAxis.createIntegerTickUnits() );

            // On effectue une rotation de 90� pour afficher le titre des abscisses � la verticale
            CategoryAxis domainAxis = plot.getDomainAxis();
            domainAxis.setCategoryLabelPositions( CategoryLabelPositions.createUpRotationLabelPositions( Math.PI / 2.0 ) );
            super.setChart( retChart );
        }
        return retChart;
    }

}
