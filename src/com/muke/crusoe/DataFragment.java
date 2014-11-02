package com.muke.crusoe;

import com.muke.crusoe.gpsfile.WayPoint;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DataFragment extends Fragment {
	//TextView txtGoto;
	TextView txtLat;
	TextView txtLong;
	TextView txtVel;
	TextView txtDist;
	TextView txtWpt;

	WayPoint mLocation = null;
	private final IntentFilter intentFilter = new IntentFilter(CrusoeNavActivity.CRUSOE_LOCATION_VIEW_INTENT);
	private final CrusoeLocationReceiver mReceiver = new CrusoeLocationReceiver();
	private boolean registered=false;
	  
	private class CrusoeLocationReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//extraigo de Intent los datos enviados
			
	        //handler.sendEmptyMessage(1000);//envia mensaje. Atiende handler
			try{
				mLocation = new WayPoint("", "", intent.getExtras().getString("PROVIDER"));
				mLocation.setAccuracy(intent.getFloatExtra("ACCURACY", (float)0.0));
				mLocation.setLatitude(intent.getDoubleExtra("LATITUD", 0.0));
				mLocation.setLongitude(intent.getDoubleExtra("LONGITUD", 0.0));
					setDataText(intent);
			}
			catch(Exception e)
			{
                Toast.makeText(getActivity().getBaseContext(), 
                		"DataFragment.onReceived: " + e.getMessage(), 
                        Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = null;
		if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
			rootView = inflater.inflate(R.layout.goto_data, container, false);
		else
			rootView = inflater.inflate(R.layout.data_land, container, false);
		InitializeDataUI(rootView);
		
		return rootView;
	}
	@Override
	public void onResume()
	{
		super.onResume();
		if(!registered)
		{
			getActivity().registerReceiver(mReceiver, intentFilter);
			registered = true;
		}
	}
	@Override
	public void onPause()
	{
		super.onPause();
		if(registered)
			getActivity().unregisterReceiver(mReceiver);
		registered = false;
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	  super.onConfigurationChanged(newConfig);
	  
	  	LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View v = null;
	    ViewGroup g = (ViewGroup)getView();
	    g.removeAllViewsInLayout();
	    if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
	    	v = inflater.inflate(R.layout.goto_data, g);
		  else
	    	v = inflater.inflate(R.layout.data_land, g);
		InitializeDataUI(v);
		if(!registered)
		{
			getActivity().registerReceiver(mReceiver, intentFilter);
			registered = true;
		}
	}
	void InitializeDataUI(View rootView)
	{
		txtWpt = (TextView) rootView.findViewById(R.id.waypoint);
		txtLat = (TextView) rootView.findViewById(R.id.latitud);
		txtLong = (TextView) rootView.findViewById(R.id.longitud);
		txtVel = (TextView) rootView.findViewById(R.id.tiempo);
		txtDist = (TextView) rootView.findViewById(R.id.recorrido);
				
		txtLat.setText(R.string.data_latitud);
		txtLong.setText(R.string.data_longitud);
		txtVel.setText(R.string.data_speed);
		txtWpt.setText("");
		txtDist.setText(R.string.data_distance);
	}
	String convLat(double dec)
	{//convierto latitud
		String s="";
		if(dec<0)
		{
			s = "O";
			dec = -dec;
		}
		else
			s = "E";
		int dd = (int)dec;
		int mm = (int)((dec - dd)*60);
		int ss = (int)((dec - dd - ((double)mm)/60)*3600);
		String dms=String.format("%d:%2d:%2d %s", dd, mm, ss, s);
		
		return dms;
	}
	String convLong(double dec)
	{//convierto latitud
		String s="";
		if(dec<0)
		{
			s = "S";
			dec = -dec;
		}
		else
			s = "N";
		int dd = (int)dec;
		int mm = (int)((dec - dd)*60);
		int ss = (int)((dec - dd - ((double)mm)/60)*3600);
		String dms=String.format("%d:%2d:%2d %s", dd, mm, ss, s);
		
		return dms;
	}
	public void setDataText(Intent intent)
	{
		//txtLat.setText("Lat: " + intent.getDoubleExtra("LATITUD", 0.0));
		//txtLong.setText("Long: " + intent.getDoubleExtra("LONGITUD", 0.0));
		String n = intent.getStringExtra("NAME");
		if(n!=null)
			txtWpt.setText("GOTO " + n);
		txtLat.setText( convLat(intent.getDoubleExtra("LATITUD", 0.0)));
		txtLong.setText(convLong(intent.getDoubleExtra("LONGITUD", 0.0)));
		txtVel.setText(intent.getStringExtra("SPEED"));
		//txtVel.setText("VEL: " + intent.getStringExtra("SPEED"));
		txtDist.setText("DIST: " + intent.getStringExtra("TRAVELLED"));
	}	
}
