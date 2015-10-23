package com.crusoe.nav.small;

import com.crusoe.nav.CompassFragment;
import com.crusoe.nav.CrusoeNavActivity;
import com.crusoe.nav.CrusoeNavFragments;
import com.crusoe.nav.R;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DataFragment extends CrusoeNavFragments {
	//TextView txtGoto;
	TextView txtLat;
	TextView txtLong;
	TextView txtVel;
	TextView txtDist;
	TextView txtWpt;
	TextView txtCurso;//curso a seguir
	TextView txtDir;//direccion actual

	@Override
	protected void UpdateMapView() {
		// TODO Auto-generated method stub
		try{
				setDataText();
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
			Configuration cfg = getResources().getConfiguration();
			int size = cfg.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
			if(size == Configuration.SCREENLAYOUT_SIZE_SMALL)
			{
				if(cfg.orientation == Configuration.ORIENTATION_PORTRAIT)
					rootView = inflater.inflate(R.layout.data_small_port, container, false);
				else
					rootView = inflater.inflate(R.layout.data_small_land, container, false);
			}
			else{
				rootView = inflater.inflate(R.layout.goto_data, container, false);
			}
		InitializeDataUI(rootView);
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
				v = inflater.inflate(R.layout.data_small_port, g);
			else
				v = inflater.inflate(R.layout.data_small_land, g);
			InitializeDataUI(v);
		}
		//if(!registered)
		//{
		//	getActivity().registerReceiver(mReceiver, intentFilter);
		//	registered = true;
		//}
	}
	void setLat(String g)
	{
		if(txtLat!=null)
			txtLat.setText(g);
	}
	void setWpt(String g)
	{
		if(txtWpt!=null)
			txtWpt.setText(g);
	}
	void setLong(String g)
	{
		if(txtLong!=null)
			txtLong.setText(g);
	}
	void setDist(String g)
	{
		if(txtDist!=null)
			txtDist.setText(g);
	}
	void setCourse(String g)
	{
		if(txtCurso!=null)
			txtCurso.setText(g);
	}
	void setDir(String g)
	{
		if(txtDir!=null)
			txtDir.setText(g);
	}
	void setSpeed(String g)
	{
		if(txtVel!=null)
			txtVel.setText(g);
	}
	void InitializeDataUI(View rootView)
	{
		txtWpt = (TextView) rootView.findViewById(R.id.waypoint);
		txtLat = (TextView) rootView.findViewById(R.id.latitud);
		txtLong = (TextView) rootView.findViewById(R.id.longitud);
		txtVel = (TextView) rootView.findViewById(R.id.tiempo);
		txtDist = (TextView) rootView.findViewById(R.id.recorrido);
		txtCurso = (TextView) rootView.findViewById(R.id.curso);
		txtDir = (TextView) rootView.findViewById(R.id.Renglon3);
				
		setLat(getResources().getString(R.string.data_latitud));
		setLong(getResources().getString(R.string.data_longitud));
		setSpeed(getResources().getString(R.string.data_speed));
		setWpt("");
		setDist(getResources().getString(R.string.data_distance));
		setCourse("");
		setDir(getResources().getString(R.string.data_direction));
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
	public void setDataText()
	{
		//txtLat.setText("Lat: " + intent.getDoubleExtra("LATITUD", 0.0));
		//txtLong.setText("Long: " + intent.getDoubleExtra("LONGITUD", 0.0));
		if(name!=null && name!="")
			setWpt(name);
		setLat(convLat(mLocation.getLatitude()));
		setLong(convLong(mLocation.getLongitude()));
		//txtVel.setText(intent.getStringExtra("SPEED") + " KMh");
		setSpeed(CrusoeNavActivity.convMilliSec(transcurrido));
		setDist("DIST: " + travelled);
		setCourse(CompassFragment.convCourse(course));
		setDir("DIR: " + CompassFragment.convCourse(bearing));
	}
}
