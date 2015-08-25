package com.crusoe.nav;

import com.crusoe.gpsfile.WayPoint;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CompassFragment extends CrusoeNavFragments {
	ImageView compassView = null;
	ImageView sailboatView = null;
	TextView txtGoto;//nombre del wpt a donde me dirigjo
	//TextView txtLat;
	//TextView txtLong;
	//TextView txtAcc;
	TextView txtVel;//velocidad a la que navego
	TextView txtDir;//Direccion que debo seguir para llegar al wpt
	TextView txtDist;//distancia al wpt

	//WayPoint mLocation = null;
	float curAnterior = 0;//curso anterior
	float beaAnterior = 0;//curso a wpt

	//private final IntentFilter intentFilter = new IntentFilter(CrusoeNavActivity.CRUSOE_LOCATION_VIEW_INTENT);
	//private final CrusoeLocationReceiver mReceiver = new CrusoeLocationReceiver();
	//private boolean registered=false;
	@Override
	void UpdateMapView() {
		// TODO Auto-generated method stub
		CrusoeApplication app = ((CrusoeApplication)getActivity().getApplication());
		try{

			RotateAnimation rotCompass = null;
			rotCompass = new RotateAnimation(-curAnterior, -course, compassView.getWidth()/2, compassView.getHeight()/2);
			rotCompass.setFillAfter(true);
			rotCompass.setFillEnabled(true);
			compassView.startAnimation(rotCompass);

			RotateAnimation rotSailboat = null;
			rotSailboat = new RotateAnimation(bearing-course, bearing-course, sailboatView.getWidth()/2, sailboatView.getHeight()/2);
			rotSailboat.setFillAfter(true);
			rotSailboat.setFillEnabled(true);
			sailboatView.startAnimation(rotSailboat);
			beaAnterior = bearing;
			curAnterior = course;
			
			if(name==null)
			{
				setDataText();
			}
			else
			{
				setGotoText();
			}
		}
		catch(Exception e)
		{
            Toast.makeText(getActivity().getBaseContext(), 
            		"DataFragment.onReceived: " + e.getMessage(), 
                    Toast.LENGTH_SHORT).show();
		}
		
	}
	
	void setGoto(String g)
	{
		if(txtGoto!=null)
			txtGoto.setText(g);
	}
	void setSpeed(String dataSpeed)
	{
		if(txtVel!=null)
			txtVel.setText(dataSpeed);
	}
	void setDir(String g)
	{
		if(txtDir!=null)
			txtDir.setText(g);
	}
	void setDist(String g)
	{
		if(txtDist!=null)
			txtDist.setText(g);
	}
	void InitializeCompassUI(View rootView)
	{
		txtGoto = (TextView) rootView.findViewById(R.id.Renglon1);
		txtVel = (TextView) rootView.findViewById(R.id.Renglon2);
		txtDir = (TextView) rootView.findViewById(R.id.Renglon3);
		txtDist = (TextView) rootView.findViewById(R.id.Renglon4);
		compassView = (ImageView)rootView.findViewById(R.id.compassView);
		sailboatView = (ImageView)rootView.findViewById(R.id.sailboatImg);
		
		setGoto("     ");
		setSpeed(getResources().getString(R.string.data_speed));
		setDir(getResources().getString(R.string.data_direction));
		setDist(getResources().getString(R.string.data_distance));
	}
	public static String convCourse(float dec)
	{//convierto latitud
		String s="";
		if(dec<0)
			dec = 360 + dec;
		int dd = (int)dec;
		int mm = (int)((dec - dd)*60);
		int ss = (int)((dec - dd - ((double)mm)/60)*3600);
		String dms=String.format("%02d�%02d'%02d\"", dd, mm, ss);
		
		return dms;
	}
	
	public void setDataText()
	{
		setGoto("GOTO: -");
		setDist("DIST: -");
		setDir("CUR: " + convCourse(course));
		setSpeed("VEL: " + speed + " " + spd_unit);
	}
	public void setGotoText()
	{
		//cuando la distancia es menor a 50 y luego pasa a ser mayor a 100 suponer que se ha alcanzado el Waypoint.
		setGoto(name);
		setDist("DIST " + distto);
		setDir("DIR: " + bearing);
		setSpeed("VEL: " + speed + " " + spd_unit);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		View rootView = null;
		if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
			rootView = inflater.inflate(R.layout.compass_port, container, false);
		else
			rootView = inflater.inflate(R.layout.compass_land, container, false);
		InitializeCompassUI(rootView);
		
		return rootView;
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	  super.onConfigurationChanged(newConfig);
	  try
	  {
		int size = newConfig.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
		if(size == Configuration.SCREENLAYOUT_SIZE_SMALL)
		{
			LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	  		View v = null;
	    	ViewGroup g = (ViewGroup)getView();
	    	g.removeAllViewsInLayout();
	    	if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
	    		v = inflater.inflate(R.layout.compass_port, g);
		  	else
			  	v = inflater.inflate(R.layout.compass_land, g);
			InitializeCompassUI(v);
		}
		//if(!registered)
		//{
		//	getActivity().registerReceiver(mReceiver, intentFilter);
		//	registered = true;
		//}
	  }
	  catch(Exception e)
	  {
		  Toast.makeText(getActivity().getBaseContext(), "Compass Fragment " + e.getMessage(), Toast.LENGTH_LONG).show();
	  }
	}

	@Override
	public void onResume()
	{
		super.onResume();
		//if(!registered)
		//{
		//	getActivity().registerReceiver(mReceiver, intentFilter);
		//	registered = true;
		//}
	}
	@Override
	public void onPause()
	{
		super.onPause();
		//if(registered)
		//	getActivity().unregisterReceiver(mReceiver);
		//registered = false;
	}
}