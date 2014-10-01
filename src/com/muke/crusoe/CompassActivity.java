package com.muke.crusoe;

import com.muke.crusoe.gpsfile.WayPoint;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class CompassActivity extends Activity {
	ImageView compassView = null;
	boolean registered = false;
	TextView txtGoto;
	TextView txtVel;
	TextView txtDir;
	TextView txtDist;
	private class CrusoeLocationReceiver extends BroadcastReceiver{
		WayPoint mLocation = null;//posicion actual
		float bearing = 0;//bearing actual
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//falta agregar el manejo de gotoWpt y Rutas.
			//Para estos casos mostrar la distancia al punto.
			
			CrusoeApplication app = ((CrusoeApplication)getApplication());
			RotateAnimation rotate = null;
			if(!(mLocation!=null))
			{
				mLocation = new WayPoint("", "", intent.getExtras().getString("PROVIDER"));
				mLocation.setAccuracy(intent.getFloatExtra("ACCURACY", (float)0.0));
				mLocation.setLatitude(intent.getDoubleExtra("LATITUD", 0.0));
				mLocation.setLongitude(intent.getDoubleExtra("LONGITUD", 0.0));
				return;
			}
			WayPoint L = new WayPoint("", "", intent.getExtras().getString("PROVIDER"));
			L.setAccuracy(intent.getFloatExtra("ACCURACY", (float)0.0));
			L.setLatitude(intent.getDoubleExtra("LATITUD", 0.0));
			L.setLongitude(intent.getDoubleExtra("LONGITUD", 0.0));
			float angle = intent.getFloatExtra("COURSE", (float)0.0);//mLocation.bearingTo(L);
			rotate = new RotateAnimation(bearing, -angle, compassView.getWidth()/2, compassView.getHeight()/2);
			rotate.setFillAfter(true);
			rotate.setFillEnabled(true);
			compassView.startAnimation(rotate);
			bearing = angle;
			if(app.gotoWpt==null)
			{
				txtGoto.setText("GOTO: -");
				txtDist.setText("DIST: -");
				txtDir.setText("Dir: " + intent.getDoubleExtra("COURSE", 0.0));
				txtVel.setText("Vel: " + intent.getFloatExtra("SPEED", (float) 0.0));
			}
			else
			{
				//cuando la distancia es menor a 50 y luego pasa a ser mayor a 100 suponer que se ha alcanzado el Waypoint.
				float bear = mLocation.bearingTo(app.gotoWpt);
				txtGoto.setText(intent.getStringExtra("NAME"));
				txtDist.setText("Dist " + intent.getFloatExtra("DISTTO", (float)0.0));
				txtDir.setText("Dir " + intent.getDoubleExtra("BEARING", 0.0));
				txtVel.setText("Vel " + intent.getFloatExtra("SPEED", (float) 0.0));
			}
			mLocation = L;
			
		}

	};
	private final IntentFilter intentFilter = new IntentFilter(CrusoeApplication.CRUSOE_LOCATION_INTENT);
	private final CrusoeLocationReceiver mReceiver = new CrusoeLocationReceiver();

	void InitializeUI()
	{
		txtGoto = (TextView) findViewById(R.id.Renglon1);
		txtVel = (TextView) findViewById(R.id.Renglon2);
		txtDir = (TextView) findViewById(R.id.Renglon3);
		txtDist = (TextView) findViewById(R.id.Renglon4);
				
		txtGoto.setText("GOTO: -");
		txtVel.setText("VELOCIDAD: -");
		txtDir.setText("DIRECCION: -");
		txtDist.setText("DISTANCIA: -");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(registered==false)
			this.registerReceiver(mReceiver, intentFilter);
		registered = true;
		Configuration c = this.getResources().getConfiguration();
		if(c.orientation==c.ORIENTATION_PORTRAIT)
			setContentView(R.layout.compass);
		else
			setContentView(R.layout.compass_land);
		InitializeUI();
		//compassView = new ImageView(getApplicationContext());
		//compassView.setImageDrawable(getResources().getDrawable(R.drawable.compass2));
		//RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
		//params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		//compassView.setLayoutParams(params);
		//relativeLayout.addView(compassView);
		
		compassView = (ImageView)findViewById(R.id.compassImg);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	  super.onConfigurationChanged(newConfig);
	  if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
		  setContentView(R.layout.compass);
	  else
		  setContentView(R.layout.compass_land);
	  InitializeUI();
		if(registered==false)
			this.registerReceiver(mReceiver, intentFilter);
		registered = true;
		compassView = (ImageView)findViewById(R.id.compassImg);

	}
}