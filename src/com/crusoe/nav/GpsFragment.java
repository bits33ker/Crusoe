package com.crusoe.nav;

import java.util.ArrayList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.crusoe.nav.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class GpsFragment extends Fragment {
    
	CategorySeries series = new CategorySeries("GpsStatus");

	private final IntentFilter msgFilter = new IntentFilter(CrusoeApplication.CRUSOE_GPS_STATUS_INTENT);
	private final CrusoeMessageReceiver msgReceiver = new CrusoeMessageReceiver();
	private boolean msgRegistered=false;
	//recibe mensajes de otras actividades y fragments
	private class CrusoeMessageReceiver extends BroadcastReceiver{
		//recibe mensajes como nuevo goto, ruta, ...
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//extraigo de Intent los datos enviados
    		Log.i("TAG", "GpsFragment.CrusoeMessageReceiver.onReceive");
			try{
				CrusoeApplication app = ((CrusoeApplication)getActivity().getApplication());
				String status = intent.getStringExtra("STATUS");
				if(status==null)
					return;
				String [] satellites = status.split(";");
				if(satellites==null)
					return;
				int count = satellites.length;
				if(count==0)
					return;
				List<Double>snr = new ArrayList<Double>();
				for(String s:satellites)
				{
					if(s!="")
						snr.add(Double.parseDouble(s));
				}
				Double [] array = new Double[count];
				snr.toArray(array);
				drawChart(array);
		}
			catch(Exception e)
			{
                Toast.makeText(getActivity().getBaseContext(), 
                		"StatFragment.CrusoeMessageReceiver: " + e.getMessage(), 
                        Toast.LENGTH_SHORT).show();
			}
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = null;
		try{
	        
			rootView = inflater.inflate(R.layout.gps_view, container, false);

			Double [] array = {0., 0., 0., 0., 0., 0., 0., 0., 0., 0.};
			
			drawChart(array);

		}
		catch(ExceptionInInitializerError ie)
		{
			Log.i("ERROR", "GpsFragment Init: " + ie.getMessage());
		}
		catch(Exception e)
		{
			Log.i("ERROR", "GpsFragment: " + e.getMessage());
		}
		return rootView;
	}
	void drawChart(Double []val)
	{
		try{
			if(val==null)
				return;
	        RelativeLayout layout = (RelativeLayout) getActivity().findViewById(R.id.chart);
	        if(layout==null)
	        	return;
	        //int y[] = {25,10,15,20};
			series.clear();
	        for(int i=0; i < val.length; i++){
	            series.add("Bar"+(i+1),val[i]);
	        }
	        
	        XYMultipleSeriesDataset dataSet = new XYMultipleSeriesDataset();  // collection of series under one object.,there could any
	        dataSet.addSeries(series.toXYSeries());                            // number of series
	        
	        //customization of the chart
	    
	        XYSeriesRenderer renderer = new XYSeriesRenderer();     // one renderer for one series
	        renderer.setColor(Color.RED);
	        renderer.setDisplayChartValues(true);
	        renderer.setChartValuesSpacing((float) 5.5d);
	        renderer.setLineWidth((float) 10.5d);
	            
	        
	        XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();   // collection multiple values for one renderer or series
	        mRenderer.addSeriesRenderer(renderer);
	        mRenderer.setChartTitle("Gps Status");
//	        mRenderer.setXTitle("xValues");
	        mRenderer.setYTitle("Snr");
	        mRenderer.setZoomButtonsVisible(false);    
	        mRenderer.setShowLegend(false);
	        mRenderer.setShowGridX(true);      // this will show the grid in  graph
	        mRenderer.setShowGridY(true);              
//	        mRenderer.setAntialiasing(true);
	        mRenderer.setBarSpacing(.5);   // adding spacing between the line or stacks
	        mRenderer.setApplyBackgroundColor(true);//true
	        mRenderer.setAxesColor(Color.BLACK);
	        mRenderer.setLabelsColor(Color.BLACK);
	        mRenderer.setBackgroundColor(getResources().getColor(R.color.background_color));
	        mRenderer.setMarginsColor(getResources().getColor(R.color.background_color));
	        mRenderer.setXAxisMin(0);
//	        mRenderer.setYAxisMin(.5);
	        mRenderer.setXAxisMax(10);
	        mRenderer.setYAxisMax(50);
	        mRenderer.setYLabelsColor(0, Color.BLACK);
	        mRenderer.setXLabelsColor(Color.BLACK);
	//    
	        mRenderer.setXLabels(0);
	        int i=0;
	        while(i<val.length)
	        	mRenderer.addXTextLabel(i+1, "" + i++);
	        //mRenderer.addXTextLabel(2,"GPS 2");
	        //mRenderer.addXTextLabel(3,"GPS 3");
	        //mRenderer.addXTextLabel(4,"GPS 4");
	        //mRenderer.setPanEnabled(true, true);    // will fix the chart position
	     //Intent intent = ChartFactory.getBarChartIntent(context, dataSet, mRenderer,Type.DEFAULT);
	        GraphicalView gv = ChartFactory.getBarChartView(getActivity(), dataSet, mRenderer, Type.DEFAULT); 
	        //gv.setBackgroundColor(getResources().getColor(R.color.background_color));
	       
	        layout.removeAllViewsInLayout();
	        layout.addView(gv);
	 	   			//bar = new GpsBarChart();
			
		}
		catch(Exception e)
		{
            Toast.makeText(getActivity().getBaseContext(), 
            		"GpsSmallFragment.drawChart: " + e.getMessage(), 
                    Toast.LENGTH_SHORT).show();
			
		}
	}
	@Override
	public void onResume()
	{
		super.onResume();
		if(!msgRegistered)
		{
			getActivity().registerReceiver(msgReceiver, msgFilter);
			msgRegistered = true;
			//else
			//	Log.i("ERROR", "StatFragment.onResume.CrusoeMessageReceiver");			
		}
		Double [] array = {0., 0., 0., 0., 0., 0., 0., 0., 0., 0.};
		
		drawChart(array);
	}
	@Override
	public void onPause()
	{
		super.onPause();
		if(msgRegistered)
			getActivity().unregisterReceiver(msgReceiver);
		msgRegistered = false;
	}
}
