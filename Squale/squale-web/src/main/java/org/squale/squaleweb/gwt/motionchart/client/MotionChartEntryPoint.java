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
package org.squale.squaleweb.gwt.motionchart.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.squale.squaleweb.gwt.motionchart.client.data.Application;
import org.squale.squaleweb.gwt.motionchart.client.data.AuditValues;
import org.squale.squaleweb.gwt.motionchart.client.data.MotionChartData;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.MotionChart;
import com.google.gwt.visualization.client.visualizations.MotionChart.Options;

/**
 * Entry point for the Motion Chart displayed on the Homepage.
 * 
 * @author Fabrice BELLINGARD
 */
public class MotionChartEntryPoint
    implements EntryPoint
{
    final private Label label = new Label( "..." );

    final private DataServiceAsync motionChartService = GWT.create( DataService.class );

    final private AsyncCallback<MotionChartData> callback = new AsyncCallback<MotionChartData>()
    {

        public void onSuccess( final MotionChartData motionChartData )
        {
            // Create a callback to be called when the visualization API
            // has been loaded.
            Runnable onLoadCallback = new Runnable()
            {
                public void run()
                {
                    Panel panel = RootPanel.get( "motionchart" );
                    MotionChart motionChart = new MotionChart( updateTable( motionChartData ), createOptions() );
                    panel.add( motionChart );
                }

            };
            VisualizationUtils.loadVisualizationApi( onLoadCallback, MotionChart.PACKAGE );
        }

        public void onFailure( Throwable arg0 )
        {
            label.setText( new Date() + " - ECHEC ! " + arg0 );
        }
    };

    public void onModuleLoad()
    {
        motionChartService.getData( callback );
    }

    protected AbstractDataTable updateTable( MotionChartData motionChartData )
    {
        DataTable data = DataTable.create();
        data.addColumn( ColumnType.STRING, "Applications" );
        data.addColumn( ColumnType.DATE, "Date" );
        data.addColumn( ColumnType.NUMBER, "Cyclomatic Complexity" );
        data.addColumn( ColumnType.NUMBER, "Lines of Code" );
        Map<String, String> factors = motionChartData.getFactors();
        List<String> factorIdList = new ArrayList<String>( factors.keySet() );
        for ( String factorId : factorIdList )
        {
            data.addColumn( ColumnType.NUMBER, factors.get( factorId ) );
        }

        Collection<Application> computedApps = motionChartData.getApplications();
        int index = 0;
        for ( Application application : computedApps )
        {
            String appName = application.getName();
            Collection<AuditValues> audits = application.getAuditValues();
            for ( AuditValues auditValues : audits )
            {
                data.addRow();

                int rowIndex = 0;
                data.setValue( index, rowIndex++, appName );
                data.setValue( index, rowIndex++, auditValues.getDate() );
                data.setValue( index, rowIndex++, auditValues.getComplexity() );
                data.setValue( index, rowIndex++, auditValues.getLinesOfCode() );
                for ( String factorId : factorIdList )
                {
                    data.setValue( index, rowIndex++, auditValues.getFactorValue( factorId ) );
                }

                index++;
            }
        }
        return data;
    }

    protected Options createOptions()
    {
        Options options = Options.create();
        Panel panel = RootPanel.get( "motionchart" );
        options.setWidth( panel.getOffsetWidth() );
        options.setHeight( panel.getOffsetHeight() );
        options.setOption( "showXScalePicker", false );
        options.setOption( "showYScalePicker", false );
        return options;
    }

}