package com.muke.crusoe;

import java.io.File;
import java.io.FileNotFoundException;

import com.muke.crusoe.CrusoeApplication.thread_status;
import com.muke.crusoe.gpsfile.GpxTrackWriter;
import com.muke.crusoe.gpsfile.WayPoint;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class GotoDataActivity extends Activity {
	TextView txtGoto;
	TextView txtLat;
	TextView txtLong;
	TextView txtVel;
	TextView txtDir;
	TextView txtDist;
	WayPoint mLocation = null;
	//WayPoint gotoWpt = null;
	WayPoint markWpt = null;
	boolean bQuit = false;
	final int MarkRC=1;//id para el mark
	final int GotoRC=2;//id del goto
	
	boolean goback=false;

	private final IntentFilter intentFilter = new IntentFilter(CrusoeApplication.CRUSOE_LOCATION_INTENT);
	private final CrusoeLocationReceiver mReceiver = new CrusoeLocationReceiver();
	private boolean registered=false;
	
	void InitializeUI()
	{
		txtGoto = (TextView) findViewById(R.id.txtGoto);
		txtLat = (TextView) findViewById(R.id.txtLat);
		txtLong = (TextView) findViewById(R.id.txtLong);
		txtVel = (TextView) findViewById(R.id.txtVel);
		txtDir = (TextView) findViewById(R.id.txtDir);
		txtDist = (TextView) findViewById(R.id.txtDist);
				
	}

	private class CrusoeLocationReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//extraigo de Intent los datos enviados
			
	        //handler.sendEmptyMessage(1000);//envia mensaje. Atiende handler
			CrusoeApplication app = ((CrusoeApplication)getApplication());
			mLocation = new WayPoint("", intent.getExtras().getString("PROVIDER"));
			mLocation.setAccuracy(intent.getFloatExtra("ACCURACY", (float)0.0));
			mLocation.setLatitude(intent.getDoubleExtra("LATITUD", 0.0));
			mLocation.setLongitude(intent.getDoubleExtra("LONGITUD", 0.0));
			
			float dist = mLocation.distanceTo(app.gotoWpt);
			float bear = mLocation.bearingTo(app.gotoWpt);			
			txtLat.setText("Lat: " + intent.getDoubleExtra("LATITUD", 0.0));
			txtLong.setText("Long " + intent.getDoubleExtra("LONGITUD", 0.0));
			txtDist.setText("Dist " + dist);
			txtDir.setText("Dir " + bear);
			txtVel.setText("Vel " + intent.getFloatExtra("SPEED", (float) 0.0));
	        //Toast.makeText(context, 
	        //        "BROADCAST RECEIVER", 
	        //        Toast.LENGTH_LONG).show();
			
		}
	};
	
	@Override
	protected void onStop()
	{
		Log.i("TAG", "DataActivity.onStop");
		try{
			CrusoeApplication app = (CrusoeApplication)getApplication();
			if(registered)
				unregisterReceiver(mReceiver);
			registered = false;
			if(app.getThreadStatus() == thread_status.thStop && app.getLooper()!=null)
			{
				//handler.sendEmptyMessage(1002);//envia mensaje. Atiende handler
				if(bQuit)
				{
					app.RemoveLocationListener();//apaga gps.
					Log.i("TAG", "DataActivity Looper.quit");
					//falta terminar al app
			        File root =  Environment.getExternalStorageDirectory();
					File gpxfile = new File(root, "waypoints.gpx");
					GpxTrackWriter csv = new GpxTrackWriter(gpxfile);
					csv.writeHeader();
					int i=0;
					while(i<app.waypoints.size())
					{
						csv.writeWaypoint(app.waypoints.get(i));
						i++;
					}
					csv.writeFooter();
					csv.close();
				}
			}
		}
		catch(FileNotFoundException e)
		{
			Log.i("ERROR", e.getMessage());
		}
		catch(Exception e)
		{
            Toast.makeText(getBaseContext(), 
            		"No se pudo parar thread " + e.getMessage(), 
                    Toast.LENGTH_SHORT).show();
		}
		finally
		{
			super.onStop();
		}
	}
	@Override
	protected void onDestroy()
	{	//cuando termina o es destruida por el sistema
		//http://developer.android.com/reference/android/app/Activity.html
			super.onDestroy();
			//CrusoeApplication app = (CrusoeApplication)getApplication();
			//app.RemoveLocationListener();
			Log.i("TAG", "DataActivity.onDestroy");
			if(bQuit)
			{
				//no es muy elegante pero por ahora es lo unico que hay 
				Process.killProcess(Process.myPid());				
			}
			//CrusoeApplication app = (CrusoeApplication)getApplication();
			//app.RemoveLocationListener();
	}
	/*
	@Override
	protected void onDestroy()
	{	//cuando termina o es destruida por el sistema
		//http://developer.android.com/reference/android/app/Activity.html
			super.onDestroy();
			Log.i("TAG", "DataActivity.onDestroy");
			CrusoeApplication app = (CrusoeApplication)getApplication();
			app.RemoveLocationListener();
	}*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			Log.i("TAG", "DataActivity.onCreate");
			setContentView(R.layout.goto_data);
			InitializeUI();
			CrusoeApplication app = ((CrusoeApplication)getApplication());
			//Disparo GotoActivity con el menu de waypoints a elegir
			if(app.gotoWpt==null)
				{
				Intent intent = this.getIntent();
				String p = intent.getStringExtra("WAYPOINTS");
				Intent gotoIntent = new Intent(this, GotoActivity.class);
				gotoIntent.putExtra("WAYPOINTS", p);
				this.startActivityForResult(gotoIntent, GotoRC);
				return;
			}	
			txtGoto.setText("GOTO " + app.gotoWpt.getName());
			if(!registered)
			{
				this.registerReceiver(mReceiver, intentFilter);
				registered = true;
			}
			
			if(app.getThreadStatus()==thread_status.thStart)
			{
				app.StartThread();
			}
		}
		catch(Exception e)
		{
			Log.i("ERROR", e.getMessage());
		}
	}
	@Override
	protected void onRestart()
	{
		super.onRestart();
		try {
			//todo a restart
			//Intent intent = this.getIntent();
			if(goback)
			{
				goback= false;
		    	Intent returnIntent = new Intent();
	        	setResult(RESULT_CANCELED,returnIntent);
	        	finish();
	        	return;
			}
			InitializeUI();
			CrusoeApplication app = ((CrusoeApplication)getApplication());
			if(!(app.gotoWpt == null))
				txtGoto.setText("GOTO " + app.gotoWpt.getName());
			//gotoWpt = new WayPoint(intent.getStringExtra("GOTO"), intent.getStringExtra("PROVIDER"));
			if(!registered)
			{
				this.registerReceiver(mReceiver, intentFilter);
				registered = true;
			}
			//CrusoeApplication app = ((CrusoeApplication)getApplication());
			if(app.getThreadStatus()==thread_status.thStart)
			{
				app.StartThread();
			}
		}
		catch(Exception e)
		{
			Log.i("ERROR", e.getMessage());
		}
	}
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==RESULT_CANCELED)
		{
	    	if(requestCode==GotoRC)
	    	{
	    		goback = true;
	    	}
			return;
		}
			
    	if(requestCode==GotoRC)
    	{
    		if(resultCode==RESULT_OK)
    		{
    			CrusoeApplication app = ((CrusoeApplication)getApplication());
    			String res = data.getStringExtra("RESULT");
    			int i=0;
    			app.gotoWpt = null;
    			while(i<app.waypoints.size())
    			{
    				String nombre = app.waypoints.get(i).getName();
    				if(res.compareTo(nombre)==0)
    					break;
    				i++;
    			}
    			if(i<app.waypoints.size())
    			{
    				app.gotoWpt = app.waypoints.get(i);
    			}
    		}
    	}
    	return;
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		Log.i("TAG", "DataActivity.onCreateOptionsMenu");
		getMenuInflater().inflate(R.menu.data, menu);
		return true;
	}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// Handle item selection
    	switch (item.getItemId()) {
    	case R.id.action_map:
    		break;
    	case R.id.action_mark:
    	{
    		if(!(mLocation == null))
    		{
    			//waypoints.add(mLocation);
    			markWpt = mLocation;
    			Intent markIntent = new Intent(this, MarkActivity.class);
    			markIntent.putExtra("LATITUD", mLocation.getLatitude());
    			markIntent.putExtra("LONGITUD", mLocation.getLongitude());
    			markIntent.putExtra("ACCURACY", mLocation.getAccuracy());
    			this.startActivityForResult(markIntent, MarkRC);
    		}
    		else
	            Toast.makeText(getBaseContext(), 
	                    "GPS no se encuentra en posicion", 
	                    Toast.LENGTH_SHORT).show();
    	}		
    		break;
    	case R.id.action_goto:
    	{
    		CrusoeApplication app = ((CrusoeApplication)getApplication());
    		if(app.waypoints.size()>0)
    		{
    			int i=0;
    			String param="";
    			while(i<app.waypoints.size())
    			{
    				param = param + app.waypoints.get(i).getName() + ";";
    				i++;
    			}
				Intent gotoIntent = new Intent(this, GotoActivity.class);
				gotoIntent.putExtra("WAYPOINTS", param);
				this.startActivityForResult(gotoIntent, GotoRC);
    		}
    		else
	            Toast.makeText(getBaseContext(), 
	                    "No hay Waypoints guardados", 
	                    Toast.LENGTH_SHORT).show();
    	}
    		break;
    	case R.id.action_settings:
    		Toast.makeText(this.getBaseContext(), 
	                "VERSION 0.0.0.1", 
	                Toast.LENGTH_LONG).show();
    		break;
    	case R.id.action_quit:
    	{
    		try{
			CrusoeApplication app = (CrusoeApplication)getApplication();
    		bQuit = true;
			app.getLooper().quit();
			app.getThread().join();
    		}catch(InterruptedException e)
    		{
    			Log.i("EXC", e.getMessage());
    		}
    		finally
    		{
    		this.finish();
    		}
    	}
    		break;
    	}
    	return true;
    }
}
