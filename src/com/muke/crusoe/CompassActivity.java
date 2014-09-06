package com.muke.crusoe;

import com.muke.crusoe.gpsfile.WayPoint;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class CompassActivity extends Activity {
	ImageView compassView = null;
	private class CrusoeLocationReceiver extends BroadcastReceiver{
		WayPoint mLocation = null;//posicion actual
		float bearing = 0;//bearing actual
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//extraigo de Intent los datos enviados
			
	        //handler.sendEmptyMessage(1000);//envia mensaje. Atiende handler
			//txtLat.setText("Lat: " + intent.getDoubleExtra("LATITUD", 0.0));
			//txtLong.setText("Long " + intent.getDoubleExtra("LONGITUD", 0.0));
			//txtAcc.setText("Acc " + intent.getFloatExtra("ACCURACY", (float) 0.0));
			//txtDir.setText("Dir " + intent.getFloatExtra("BEARING", (float) 0.0));
			//txtVel.setText("Vel " + intent.getFloatExtra("SPEED", (float) 0.0));
			RotateAnimation rotate = null;
			if(!(mLocation!=null))
			{
				mLocation = new WayPoint("", intent.getExtras().getString("PROVIDER"));
				mLocation.setAccuracy(intent.getFloatExtra("ACCURACY", (float)0.0));
				mLocation.setLatitude(intent.getDoubleExtra("LATITUD", 0.0));
				mLocation.setLongitude(intent.getDoubleExtra("LONGITUD", 0.0));
				return;
			}
			WayPoint L = new WayPoint("", intent.getExtras().getString("PROVIDER"));
			L.setAccuracy(intent.getFloatExtra("ACCURACY", (float)0.0));
			L.setLatitude(intent.getDoubleExtra("LATITUD", 0.0));
			L.setLongitude(intent.getDoubleExtra("LONGITUD", 0.0));
			float angle = mLocation.bearingTo(L);
			rotate = new RotateAnimation(bearing, angle, compassView.getWidth()/2, compassView.getHeight()/2);
			rotate.setFillAfter(true);
			rotate.setFillEnabled(true);
			compassView.startAnimation(rotate);
			mLocation = L;
			bearing = angle;
			
		}

	};
	private final IntentFilter intentFilter = new IntentFilter(CrusoeApplication.CRUSOE_LOCATION_INTENT);
	private final CrusoeLocationReceiver mReceiver = new CrusoeLocationReceiver();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compass);
		this.registerReceiver(mReceiver, intentFilter);

		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.compass);

		//compassView = new ImageView(getApplicationContext());
		//compassView.setImageDrawable(getResources().getDrawable(R.drawable.compass2));
		//RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
		//params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		//compassView.setLayoutParams(params);
		//relativeLayout.addView(compassView);
		
		compassView = (ImageView)findViewById(R.id.compassImg);
	}
}