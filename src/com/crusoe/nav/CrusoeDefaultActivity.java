package com.crusoe.nav;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;

import com.crusoe.nav.ConfigDialog.ConfigDialogListener;
import com.crusoe.nav.StatWpt;
import com.crusoe.nav.RouteListActivity;
import com.crusoe.nav.FileChooser;
import com.crusoe.nav.CrusoeApplication.thread_status;
import com.crusoe.gpsfile.GpxWriter;
import com.crusoe.gpsfile.RoutePoint;
import com.crusoe.gpsfile.WayPoint;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

public class CrusoeDefaultActivity extends CrusoeNavActivity {
	/*
	 * Esta es la clase principal.
	 * Desde aqui se manejan los fragments y
	 * los menu's: Goto, Routes, Mark, Track, Quit
	 */
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
				setContentView(R.layout.main_port);
			else
				setContentView(R.layout.main_land);
		}
		catch(Exception e)
		{
			Log.i("ERROR CrusoeDefaultActivity ", e.getMessage());
		}
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		//cambia configuracion: landscape o portrait
	  super.onConfigurationChanged(newConfig);
	  try {
		int size = newConfig.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
		if(size != Configuration.SCREENLAYOUT_SIZE_SMALL)
		{
			//((LinearLayout)findViewById(R.id.compass_large)).removeAllViews();
		    if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
				setContentView(R.layout.main_port);
			  else
				setContentView(R.layout.main_land);
		}
	  }catch(Exception e)
	  {
          Toast.makeText(getBaseContext(), 
          		"Error: " + e.getMessage(), 
                  Toast.LENGTH_SHORT).show();
	  }
	}

}
