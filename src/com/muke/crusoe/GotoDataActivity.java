package com.muke.crusoe;

import java.io.File;
import java.io.FileNotFoundException;

import com.muke.crusoe.CrusoeApplication.thread_status;
import com.muke.crusoe.gpsfile.GpxTrackWriter;
import com.muke.crusoe.gpsfile.GpxWriter;
import com.muke.crusoe.gpsfile.RoutePoint;
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
	TextView txtAcc;
	WayPoint mLocation = null;
	//WayPoint gotoWpt = null;
	WayPoint markWpt = null;
	boolean bQuit = false;
	final int MarkRC=1;//id para el mark
	final int GotoRC=2;//id del goto
	final int RouteRC=3;//id de las rutas
	
	//boolean goback=false;

	private final IntentFilter intentFilter = new IntentFilter(CrusoeApplication.CRUSOE_LOCATION_INTENT);
	private final CrusoeLocationReceiver mReceiver = new CrusoeLocationReceiver();
	private boolean registered=false;
	
	void InitializeGotoUI()
	{
		txtGoto = (TextView) findViewById(R.id.Renglon1);
		txtLat = (TextView) findViewById(R.id.Renglon2);
		txtLong = (TextView) findViewById(R.id.Renglon3);
		txtVel = (TextView) findViewById(R.id.Renglon4);
		txtDir = (TextView) findViewById(R.id.Renglon5);
		txtDist = (TextView) findViewById(R.id.Renglon6);
				
		txtLat.setText(R.string.data_latitud);
		txtLong.setText(R.string.data_longitud);
		txtVel.setText(R.string.data_speed);
		txtDir.setText(R.string.data_direction);
		txtDist.setText(R.string.data_distance);
	}
	void InitializeDataUI()
	{
		txtLat = (TextView) findViewById(R.id.Renglon1);
		txtLong = (TextView) findViewById(R.id.Renglon2);
		txtVel = (TextView) findViewById(R.id.Renglon3);
		txtDir = (TextView) findViewById(R.id.Renglon4);
		txtAcc = (TextView) findViewById(R.id.Renglon5);
		txtDist = (TextView) findViewById(R.id.Renglon6);
				
		txtLat.setText(R.string.data_latitud);
		txtLong.setText(R.string.data_longitud);
		txtVel.setText(R.string.data_speed);
		txtDir.setText(R.string.data_direction);
		txtAcc.setText(R.string.data_accuracy);
		txtDist.setText(R.string.data_distance);
	}
	private class CrusoeLocationReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//extraigo de Intent los datos enviados
			
	        //handler.sendEmptyMessage(1000);//envia mensaje. Atiende handler
			CrusoeApplication app = ((CrusoeApplication)getApplication());
			mLocation = new WayPoint("", "", intent.getExtras().getString("PROVIDER"));
			mLocation.setAccuracy(intent.getFloatExtra("ACCURACY", (float)0.0));
			mLocation.setLatitude(intent.getDoubleExtra("LATITUD", 0.0));
			mLocation.setLongitude(intent.getDoubleExtra("LONGITUD", 0.0));
			
			if(app.gotoWpt==null)
			{
				txtLat.setText("Lat: " + intent.getDoubleExtra("LATITUD", 0.0));
				txtLong.setText("Long: " + intent.getDoubleExtra("LONGITUD", 0.0));
				txtAcc.setText("Time: " + intent.getDoubleExtra("TRANSCURRIDO", 0.0));
				txtDir.setText("Dir: " + intent.getDoubleExtra("COURSE", 0.0));
				txtVel.setText("Vel: " + intent.getFloatExtra("SPEED", (float) 0.0));
			}
			else
			{
				//cuando la distancia es menor a 50 y luego pasa a ser mayor a 100 suponer que se ha alcanzado el Waypoint.
				txtGoto.setText(intent.getStringExtra("NAME"));
				txtLat.setText("Lat: " + intent.getDoubleExtra("LATITUD", 0.0));
				txtLong.setText("Long " + intent.getDoubleExtra("LONGITUD", 0.0));
				txtDist.setText("Dist " + intent.getFloatExtra("DISTTO", (float)0.0));
				txtDir.setText("ETA " + intent.getFloatExtra("ETA", (float)0.0));
				txtVel.setText("Vel " + intent.getFloatExtra("SPEED", (float) 0.0));
			}
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
				}
			}
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			Log.i("TAG", "GotoDataActivity.onCreate");
			setContentView(R.layout.goto_data);
			CrusoeApplication app = ((CrusoeApplication)getApplication());
			app.CargoWayPoints();
			//Disparo GotoActivity con el menu de waypoints a elegir
			if(app.gotoWpt==null)
			{
				InitializeDataUI();
			}
			else
			{
				InitializeGotoUI();
				txtGoto.setText("GOTO " + app.gotoWpt.getName());
			}
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
			CrusoeApplication app = ((CrusoeApplication)getApplication());
			if(!(app.gotoWpt == null))
			{
				InitializeGotoUI();
				txtGoto.setText("GOTO " + app.gotoWpt.getName());
			}
			else
			{
				InitializeDataUI();			
			}
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
			return;
		}
			
		CrusoeApplication app = ((CrusoeApplication)getApplication());
    	if(requestCode==RouteRC)
    	{
    		if(resultCode==RESULT_OK)
    		{
    			String res = data.getStringExtra("RESULT");
    			int i=0;
    			app.ruta_seguir = null;
    			while(i<app.routes.size())
    			{
    				String nombre = app.routes.get(i).getName();
    				if(res.compareTo(nombre)==0)
    					break;
    				i++;
    			}
    			if(i<app.routes.size())
    			{
    				RoutePoint R = app.routes.get(i);
    				app.ruta_seguir = new RoutePoint(R.getName()); 
					app.ruta_seguir.addAll(R.Locations());
					app.gotoWpt = (WayPoint)app.ruta_seguir.Locations().toArray()[0];
					app.ruta_seguir.Locations().remove(app.gotoWpt);
					app.track.StartSegment();
					InitializeGotoUI();								
					txtGoto.setText(app.gotoWpt.getName());
    			}
    		}
    	}
    	if(requestCode==GotoRC)
    	{
    		if(resultCode==RESULT_OK)
    		{
    			String res = data.getStringExtra("RESULT");
    			app.gotoWpt = null;
    			for(RoutePoint r: app.routes)
    			{
    				for(WayPoint w: r.Locations())
    				{
    					if(res.compareTo(w.getName())==0)
    					{
    						app.gotoWpt = w;
    						app.track.StartSegment();
    						return;
    					}
    				}
    			}
    		}
    	}
    	if(requestCode==MarkRC)
    	{
    		if(resultCode==RESULT_OK)
    		{
    			String wpt = data.getStringExtra("RESULT");
    			markWpt.setName(wpt);
    			RoutePoint ruta = app.getRoute("waypoints");
    			if(ruta==null)
    			{
    				Log.i("ERROR", this.getResources().getString(R.string.error_mark));
    	            Toast.makeText(getBaseContext(), 
    	                    R.string.error_mark, 
    	                    Toast.LENGTH_SHORT).show();
    				return;
    			}
    			ruta.addWayPoint(markWpt);
    			try {
    				File WaypointsDir =  new File(Environment.getExternalStorageDirectory() + "/Crusoe/Waypoints");
					File gpxfile = new File(WaypointsDir, "waypoints.gpx");
					GpxWriter csv = new GpxWriter(gpxfile);
					csv.writeHeader();
					int i=0;
					while(i<app.getRoute("waypoints").Locations().size())
					{
						csv.writeWaypoint(app.getRoute("waypoints").getWayPoint(i));
						i++;
					}
					csv.writeFooter();
					csv.close();
    			}
    			catch(FileNotFoundException e)
    			{
    				Log.i("ERROR", e.getMessage());
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
    	case R.id.action_track:
    	{
    		//grabo a disco el trayecto con nombre de la fecha actual.
    		//En ppio. guardo un trayecto por fecha.
    		//Cada archivo tiene varios trayectos con nombre del goto y la hora.
    		//los segmentos son por si apago el track o se pierde la señal.
    		//Crear la carpeta Crusoe
    		//Crear los subdirs Tracks y Waypoints.
    		//El archivo Waypoints guarda los waypoints generales en la carpeta Waypoints.
    		//Las rutas van en la carpeta Waypoints.
    		//Los nombre de las rutas corresponden al nombre del archivo.
    		//Cada ruta se compone de los waypoints que contiene el archivo.
    		if(mLocation!=null)
    		{
    			CrusoeApplication app = ((CrusoeApplication)getApplication());
    			app.SaveTrack();
    		}
    		else
	            Toast.makeText(getBaseContext(), 
	                    R.string.error_gps_pos, 
	                    Toast.LENGTH_SHORT).show();
    	}
    		break;
    	case R.id.action_compass:
       		if(mLocation!=null)
    		{
    			//waypoints.add(mLocation);
    			Intent markIntent = new Intent(this, CompassActivity.class);
    			this.startActivity(markIntent);
    		}
    		else
	            Toast.makeText(getBaseContext(), 
	                    R.string.error_gps_pos, 
	                    Toast.LENGTH_SHORT).show();
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
	                    R.string.error_gps_pos, 
	                    Toast.LENGTH_SHORT).show();
    	}		
    		break;
    	case R.id.action_goto:
    	{
    		CrusoeApplication app = ((CrusoeApplication)getApplication());
    		if(app.routes.size()>0)
    		{
    			String param="";
    			for(RoutePoint r : app.routes)
    			{
    				for(WayPoint w: r.Locations())
    				{
    					param = param + w.getName() + ";";
    				}
    			}
				Intent gotoIntent = new Intent(this, GotoActivity.class);
				gotoIntent.putExtra("NAMES", param);
				this.startActivityForResult(gotoIntent, GotoRC);
    		}
    		else
	            Toast.makeText(getBaseContext(), 
	                    R.string.error_no_wpt, 
	                    Toast.LENGTH_SHORT).show();
    	}
    		break;
    	case R.id.action_route:
    	{
    		if(mLocation!=null)
    		{
    			CrusoeApplication app = ((CrusoeApplication)getApplication());
    			if(app.routes.size()>0)
    			{
    				int i=0;
    				String param="";
    				while(i<app.routes.size())
    				{
    					if(app.routes.get(i).getName().toLowerCase()!="waypoints")
    						param = param + app.routes.get(i).getName() + ";";
    					i++;
    				}
					Intent routeIntent = new Intent(this, GotoActivity.class);
					routeIntent.putExtra("NAMES", param);
					this.startActivityForResult(routeIntent, RouteRC);
    			}
    			else
	            	Toast.makeText(getBaseContext(), 
	                    R.string.error_no_routes, 
	                    Toast.LENGTH_SHORT).show();
    		}
    		else
	            Toast.makeText(getBaseContext(), 
	                    R.string.error_gps_pos, 
	                    Toast.LENGTH_SHORT).show();
    		
    	}
    		break;
    	case R.id.action_settings:
    		Toast.makeText(this.getBaseContext(), 
	                "VERSION 0.0.0.4", 
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
