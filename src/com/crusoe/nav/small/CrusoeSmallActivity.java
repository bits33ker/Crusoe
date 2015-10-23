package com.crusoe.nav.small;

import com.crusoe.nav.CrusoeApplication;
import com.crusoe.nav.CrusoeNavActivity;
import com.crusoe.nav.R;
import com.crusoe.nav.R.id;
import com.crusoe.nav.R.layout;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.TabHost;

public class CrusoeSmallActivity extends CrusoeNavActivity 
	{
	/*
	 * Esta es la clase principal.
	 * Desde aqui se manejan los fragments y
	 * los menu's: Goto, Routes, Mark, Track, Quit
	 */
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		//cambia configuracion: landscape o portrait
	  super.onConfigurationChanged(newConfig);
	  /*
		int size = newConfig.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
		if(size != Configuration.SCREENLAYOUT_SIZE_SMALL)
		{
			//((LinearLayout)findViewById(R.id.compass_large)).removeAllViews();
		    if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
				setContentView(R.layout.main_port);
			  else
				setContentView(R.layout.activity_main);
		}*/
	}
	
    protected void initialiseTabHost() {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        // TODO Put here your Tabs
        CrusoeSmallActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("DataTab").setIndicator("Data"));
        CrusoeSmallActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("CompassTab").setIndicator("Compass"));
        CrusoeSmallActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("StatTab").setIndicator("Stats"));
        CrusoeSmallActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("MapTab").setIndicator("MapView"));
        CrusoeSmallActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("TracksTab").setIndicator("TracksList"));
        CrusoeSmallActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("GpsTab").setIndicator("Gps"));

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
}
