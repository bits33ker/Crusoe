package com.muke.crusoe;

import com.muke.crusoe.gpsfile.WayPoint;

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

public class CompassFragment extends Fragment {
	ImageView compassView = null;
	TextView txtGoto;
	TextView txtLat;
	TextView txtLong;
	TextView txtVel;
	TextView txtDir;
	TextView txtDist;
	TextView txtAcc;

	WayPoint mLocation = null;
	float course = 0;//curso actual
	float bearing = 0;//curso a wpt

	private final IntentFilter intentFilter = new IntentFilter(CrusoeNavActivity.CRUSOE_LOCATION_VIEW_INTENT);
	private final CrusoeLocationReceiver mReceiver = new CrusoeLocationReceiver();
	private boolean registered=false;
	  
	private class CrusoeLocationReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//extraigo de Intent los datos enviados
			
	        //handler.sendEmptyMessage(1000);//envia mensaje. Atiende handler
			RotateAnimation rotate = null;
			CrusoeApplication app = ((CrusoeApplication)getActivity().getApplication());
			try{
				mLocation = new WayPoint("", "", intent.getExtras().getString("PROVIDER"));
				mLocation.setAccuracy(intent.getFloatExtra("ACCURACY", (float)0.0));
				mLocation.setLatitude(intent.getDoubleExtra("LATITUD", 0.0));
				mLocation.setLongitude(intent.getDoubleExtra("LONGITUD", 0.0));

				float angle = intent.getFloatExtra("COURSE", (float)0.0);//mLocation.bearingTo(L);
				float bangle = intent.getFloatExtra("BEARING", (float)0.0);//mLocation.bearingTo(L);
				rotate = new RotateAnimation(course, angle, compassView.getWidth()/2, compassView.getHeight()/2);
				rotate.setFillAfter(true);
				rotate.setFillEnabled(true);
				compassView.startAnimation(rotate);
				course = angle;
				bearing = bangle;
				
				String name = intent.getStringExtra("NAME");  
				if(name==null)
				{
					setDataText(intent);
				}
				else
				{
					setGotoText(intent);
				}
			}
			catch(Exception e)
			{
                Toast.makeText(getActivity().getBaseContext(), 
                		"DataFragment.onReceived: " + e.getMessage(), 
                        Toast.LENGTH_SHORT).show();
			}
		}
	};

	void InitializeCompassUI(View rootView)
	{
		txtGoto = (TextView) rootView.findViewById(R.id.Renglon1);
		txtVel = (TextView) rootView.findViewById(R.id.Renglon2);
		txtDir = (TextView) rootView.findViewById(R.id.Renglon3);
		txtDist = (TextView) rootView.findViewById(R.id.Renglon4);
		compassView = (ImageView)rootView.findViewById(R.id.compassImg);
		
		txtGoto.setText("GOTO: -");
		txtVel.setText("VELOCIDAD: -");
		txtDir.setText("DIRECCION: -");
		txtDist.setText("DISTANCIA: -");
	}
	public void setDataText(Intent intent)
	{
		txtGoto.setText("GOTO: -");
		txtDist.setText("DIST: -");
		txtDir.setText("Dir: " + intent.getFloatExtra("COURSE", course));
		txtVel.setText("Vel: " + intent.getStringExtra("SPEED"));
	}
	public void setGotoText(Intent intent)
	{
		//cuando la distancia es menor a 50 y luego pasa a ser mayor a 100 suponer que se ha alcanzado el Waypoint.
		txtGoto.setText(intent.getStringExtra("NAME"));
		txtDist.setText("Dist " + intent.getStringExtra("DISTTO"));
		txtDir.setText("Dir: " + intent.getFloatExtra("BEARING", (float)bearing));
		txtVel.setText("Vel " + intent.getStringExtra("SPEED"));
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		View rootView = null;
		if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
			rootView = inflater.inflate(R.layout.compass, container, false);
		else
			rootView = inflater.inflate(R.layout.compass_land, container, false);
		InitializeCompassUI(rootView);
		
		return rootView;
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	  super.onConfigurationChanged(newConfig);
	  
	  	LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View v = null;
	    ViewGroup g = (ViewGroup)getView();
	    g.removeAllViewsInLayout();
	    if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
	    	v = inflater.inflate(R.layout.compass, g);
		  else
	    	v = inflater.inflate(R.layout.compass_land, g);
		InitializeCompassUI(v);
		if(!registered)
		{
			getActivity().registerReceiver(mReceiver, intentFilter);
			registered = true;
		}
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
}