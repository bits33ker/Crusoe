package com.muke.crusoe;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
/*
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

 * solo para ver 8 en adelante!!!
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
*/
import android.app.Activity;
import android.content.Context;
import android.os.*;
import android.os.Process;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.muke.crusoe.gpsfile.*;
import com.muke.crusoe.CrusoeApplication.*;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/* poner dentro de application en el manifiesto para un Broadcast estatico
 *         <receiver
            android:name="com.muke.crusoe.CrusoeLocationReceiver"
            android:exported="false" >
            <intent-filter>
                <action android:name="com.muke.crusoe.Crusoe" >
                </action>
            </intent-filter>
        </receiver>
*/
public class DataActivity extends Activity {
	TextView txtLat;
	TextView txtLong;
	TextView txtVel;
	TextView txtDir;
	TextView txtAcc;
	boolean bQuit = false;
	final int MarkRC=1;//id para el mark
	final int GotoRC=2;//id del goto
	void InitializeUI()
	{
		txtLat = (TextView) findViewById(R.id.txtLat);
		txtLong = (TextView) findViewById(R.id.txtLong);
		txtVel = (TextView) findViewById(R.id.txtVel);
		txtDir = (TextView) findViewById(R.id.txtDir);
		txtAcc = (TextView) findViewById(R.id.txtAcc);
				
	}
	WayPoint mLocation = null;
	WayPoint markWpt = null;
	//WayPoint gotoWpt = null;

	
	private class CrusoeLocationReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//extraigo de Intent los datos enviados
			
	        //handler.sendEmptyMessage(1000);//envia mensaje. Atiende handler
			txtLat.setText("Lat: " + intent.getDoubleExtra("LATITUD", 0.0));
			txtLong.setText("Long " + intent.getDoubleExtra("LONGITUD", 0.0));
			txtAcc.setText("Acc " + intent.getFloatExtra("ACCURACY", (float) 0.0));
			txtDir.setText("Dir " + intent.getFloatExtra("BEARING", (float) 0.0));
			txtVel.setText("Vel " + intent.getFloatExtra("SPEED", (float) 0.0));
			mLocation = new WayPoint("", intent.getExtras().getString("PROVIDER"));
			mLocation.setAccuracy(intent.getFloatExtra("ACCURACY", (float)0.0));
			mLocation.setLatitude(intent.getDoubleExtra("LATITUD", 0.0));
			mLocation.setLongitude(intent.getDoubleExtra("LONGITUD", 0.0));
			
	        //Toast.makeText(context, 
	        //        "BROADCAST RECEIVER", 
	        //        Toast.LENGTH_LONG).show();
			
		}

	};
	private final IntentFilter intentFilter = new IntentFilter(CrusoeApplication.CRUSOE_LOCATION_INTENT);
	private final CrusoeLocationReceiver mReceiver = new CrusoeLocationReceiver();
	@Override
	protected void onStop()
	{
		Log.i("TAG", "DataActivity.onStop");
		try{
			unregisterReceiver(mReceiver);
			CrusoeApplication app = (CrusoeApplication)getApplication();
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
					/*
					 * codigo a partir de la API 8.
					//prueba de GPX con SAX.
					try {
					DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();		            
		            // root elements
		            Document doc = docBuilder.newDocument();
		            Element rootElement = doc.createElement("Class");
		            doc.appendChild(rootElement);

		            DOMSource source=new DOMSource(doc);
		            FileOutputStream _stream=getApplicationContext().openFileOutput("waypoints2.gpx", getApplicationContext().MODE_WORLD_WRITEABLE);
		            StreamResult result=new StreamResult(_stream);

		            TransformerFactory transformerfactory = TransformerFactory.newInstance();
		            Transformer transformer = transformerfactory.newTransformer();
		            transformer.transform(source, result);
		            }
		catch(TransformerException e)
		{
            Toast.makeText(getBaseContext(), 
            		"Transformer Exception " + e.getMessage(), 
                    Toast.LENGTH_SHORT).show();			
		}
		catch(FileNotFoundException e)
		{
            Toast.makeText(getBaseContext(), 
            		"File Not Found!! " + e.getMessage(), 
                    Toast.LENGTH_SHORT).show();
		}
		catch(ParserConfigurationException e)
		{
            Toast.makeText(getBaseContext(), 
            		"Error creando factoria! " + e.getMessage(), 
                    Toast.LENGTH_SHORT).show();
		}
		            
		            */
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
		Log.i("TAG", "DataActivity.onCreate");
		setContentView(R.layout.activity_data);
		InitializeUI();
		this.registerReceiver(mReceiver, intentFilter);
		CrusoeApplication app = ((CrusoeApplication)getApplication());
		app.CargoWayPoints();
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
		this.registerReceiver(mReceiver, intentFilter);
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
    		if(mLocation!=null)
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
				Intent gotoIntent = new Intent(this, GotoDataActivity.class);
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==RESULT_CANCELED)
		{
			return;
		}
		CrusoeApplication app = ((CrusoeApplication)getApplication());

    	if(requestCode==MarkRC)
    	{
    		if(resultCode==RESULT_OK)
    		{
    			String wpt = data.getStringExtra("RESULT");
    			markWpt.setName(wpt);
    			app.waypoints.add(markWpt);
    		}    		
    	}
    	/*
    	if(requestCode==GotoRC)
    	{
    		if(resultCode==RESULT_OK)
    		{
    			String res = data.getStringExtra("RESULT");
    			int i=0;
    			gotoWpt = null;
    			while(i<app.waypoints.size())
    			{
    				String nombre = app.waypoints.get(i).getName();
    				if(res.compareTo(nombre)==0)
    					break;
    				i++;
    			}
    			if(i<app.waypoints.size())
    			{
    				gotoWpt = app.waypoints.get(i);
    				if(gotoWpt!=null)
    				{
    					//String tit = "GoTo " + gotoWpt.getName();
    					//this.setTitle(tit);
    					Log.i("TAG", "DataActivity.onRestart");
    					Intent markIntent = new Intent(this, GotoDataActivity.class);
    					markIntent.putExtra("GOTO", gotoWpt.getName());
    					markIntent.putExtra("PROVIDER", gotoWpt.getProvider());
    					this.startActivity(markIntent);
    					return;
    				}
    			}
    		}
    	}*/
    	return;
    }
}
