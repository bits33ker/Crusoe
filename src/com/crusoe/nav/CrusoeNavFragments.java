package com.crusoe.nav;

import com.crusoe.gpsfile.WayPoint;

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

public abstract class CrusoeNavFragments extends Fragment {
	//clase base para todos los fragments de CrusoeNav
	WayPoint mLocation = null;
	double speed = 0;
	String spd_unit;
	float course = 0;//curso actual
	float bearing = 0;//curso a wpt
	String name ="";//nombre del GOTO
	String distto ="";
	long transcurrido = 0L;//tiempo transcurrido en mseg.
	String travelled = "";//distancia recorrida

	private final IntentFilter locFilter = new IntentFilter(CrusoeNavActivity.CRUSOE_LOCATION_VIEW_INTENT);
	private final CrusoeLocationReceiver locReceiver = new CrusoeLocationReceiver();
	private boolean locRegistered=false;
	  
	abstract void UpdateMapView();
	
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
				
				String s = intent.getStringExtra("SPEED");
				spd_unit = intent.getStringExtra("SPD_UNIT");
				speed = Double.parseDouble(s);
				
				bearing = intent.getFloatExtra("BEARING", (float)0.0);//mLocation.bearingTo(L);
				course = intent.getFloatExtra("COURSE", (float)0.0);//mLocation.bearingTo(L);
				name = intent.getStringExtra("NAME");  
				distto = intent.getStringExtra("DISTTO");
				transcurrido = intent.getLongExtra("TRANSCURRIDO", 0L);
				travelled = intent.getStringExtra("TRAVELLED");
				//debo llamar a una funcion virtual de actualizar views
				UpdateMapView();
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
	public void onResume()
	{
		super.onResume();
		if(!locRegistered)
		{
			getActivity().registerReceiver(locReceiver, locFilter);
			locRegistered = true;
		}
	}
	@Override
	public void onPause()
	{
		super.onPause();
		if(locRegistered)
			getActivity().unregisterReceiver(locReceiver);
		locRegistered = false;
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	  super.onConfigurationChanged(newConfig);
	  
		if(!locRegistered)
		{
			getActivity().registerReceiver(locReceiver, locFilter);
			locRegistered = true;
		}
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
		String dms=String.format("%02d°%02d'%02d\" %s", dd, mm, ss, s);
		
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
		String dms=String.format("%02d°%02d'%02d\" %s", dd, mm, ss, s);
		
		return dms;
	}
}
