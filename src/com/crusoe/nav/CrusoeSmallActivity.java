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

public class CrusoeSmallActivity extends CrusoeNavActivity implements
		OnTabChangeListener, OnPageChangeListener {
	/*
	 * Esta es la clase principal.
	 * Desde aqui se manejan los fragments y
	 * los menu's: Goto, Routes, Mark, Track, Quit
	 */
	
	
	//clases para el manejo de los Fragments para SMALL
	private CrusoeNavPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private TabHost mTabHost;
    
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		//cambia configuracion: landscape o portrait
	  super.onConfigurationChanged(newConfig);
		int size = newConfig.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
		if(size != Configuration.SCREENLAYOUT_SIZE_SMALL)
		{
			//((LinearLayout)findViewById(R.id.compass_large)).removeAllViews();
		    if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
				setContentView(R.layout.main_port);
			  else
				setContentView(R.layout.activity_main);
		}
	}
	
    // Method to add a TabHost
    private static void AddTab(CrusoeSmallActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec) {
        tabSpec.setContent(new CrusoeTabFactory(activity));
        tabHost.addTab(tabSpec);
    }
    private void initialiseTabHost() {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        // TODO Put here your Tabs
        CrusoeSmallActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("DataTab").setIndicator("Data"));
        CrusoeSmallActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("CompassTab").setIndicator("Compass"));
        CrusoeSmallActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("StatTab").setIndicator("Stats"));
        CrusoeSmallActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("MapTab").setIndicator("MapView"));
        CrusoeSmallActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("TracksTab").setIndicator("TracksList"));

        mTabHost.setOnTabChangedListener(this);
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			Log.i("TAG", "CrusoeNavActivity.onCreate");
			CrusoeApplication app = ((CrusoeApplication)getApplication());
			//if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
			//	setContentView(R.layout.main_port);
			//else
				setContentView(R.layout.activity_main);
			
	        mViewPager = (ViewPager) findViewById(R.id.viewpager);
	        if(mViewPager!=null)
	        {
	        	//version para mi cell
	        	initialiseTabHost();

	        	// Tab Initialization
	        	//initialiseTabHost();
	        	mAdapter = new CrusoeNavPagerAdapter(getSupportFragmentManager());
	        	// Fragments and ViewPager Initialization
	       	        
	        	mViewPager.setAdapter(mAdapter);
	        	mViewPager.setOnPageChangeListener(CrusoeSmallActivity.this);
	        }
		}
		catch(Exception e)
		{
			Log.i("ERROR CrusoeNavActivity ", e.getMessage());
		}
	}
	
    @Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
	       int pos = this.mViewPager.getCurrentItem();
	       this.mTabHost.setCurrentTab(pos);
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabChanged(String arg0) {
		// TODO Auto-generated method stub
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
	}
}
