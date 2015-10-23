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

public abstract class  CrusoeNavFragments extends Fragment {
	//clase base para todos los fragments de CrusoeNav
	protected WayPoint mLocation = null;
	private double speed = 0;
	private String spd_unit;
	protected float course = 0;//curso actual
	protected float bearing = 0;//curso a wpt
	protected String name ="";//nombre del GOTO
	private String distto ="";
	protected long transcurrido = 0L;//tiempo transcurrido en mseg.
	protected String travelled = "";//distancia recorrida

	private final IntentFilter locFilter = new IntentFilter(CrusoeNavActivity.CRUSOE_LOCATION_VIEW_INTENT);
	private final CrusoeLocationReceiver locReceiver = new CrusoeLocationReceiver();
	private boolean locRegistered=false;
	  
	abstract protected void UpdateMapView();
	
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
				course = intent.getFloatExtra("COURSE", (float)0.0);//mLocation.bearingTo(L);
				mLocation.setBearing(course);
				
				String s = intent.getStringExtra("SPEED");
				setSpeed_unit(intent.getStringExtra("SPD_UNIT"));
				setSpeed(Double.parseDouble(s));
				
				bearing = intent.getFloatExtra("BEARING", (float)0.0);//mLocation.bearingTo(L);
				name = intent.getStringExtra("NAME");  
				setDistTo(intent.getStringExtra("DISTTO"));
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
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public String getSpeed_unit() {
		return spd_unit;
	}
	public void setSpeed_unit(String spd_unit) {
		this.spd_unit = spd_unit;
	}
	public String getDistTo() {
		return distto;
	}
	public void setDistTo(String distto) {
		this.distto = distto;
	}
}
