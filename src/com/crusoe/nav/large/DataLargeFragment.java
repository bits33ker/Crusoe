package com.crusoe.nav.large;

import com.crusoe.nav.CrusoeNavFragments;
import com.crusoe.nav.R;
import com.crusoe.nav.R.layout;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DataLargeFragment extends CrusoeNavFragments 
{
	//reemplaza a la antigua CrusoeDefaultActivity

	@Override
	protected void UpdateMapView() {
		// TODO Auto-generated method stub
		try
		{
		}
		catch(Exception e)
		{
            Toast.makeText(getActivity().getBaseContext(), 
            		"DataFragment.onReceived: " + e.getMessage(), 
                    Toast.LENGTH_SHORT).show();
		}
		
	}	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = null;
		try{
		if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
			rootView = inflater.inflate(R.layout.large_port, container, false);
		else
			rootView = inflater.inflate(R.layout.large_land, container, false);

		}
		catch(ExceptionInInitializerError ie)
		{
			Log.i("ERROR", "DataFragment Init: " + ie.getMessage());
		}
		catch(Exception e)
		{
			Log.i("ERROR", "DataFragment: " + e.getMessage());
		}
		return rootView;
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
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	  super.onConfigurationChanged(newConfig);
	  
		int size = newConfig.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
		if(size == Configuration.SCREENLAYOUT_SIZE_SMALL)
		{
			LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = null;
			ViewGroup g = (ViewGroup)getView();
			g.removeAllViewsInLayout();
			if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
				v = inflater.inflate(R.layout.large_port, g);
			else
				v = inflater.inflate(R.layout.large_land, g);
		}
		//if(!registered)
		//{
		//	getActivity().registerReceiver(mReceiver, intentFilter);
		//	registered = true;
		//}
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
