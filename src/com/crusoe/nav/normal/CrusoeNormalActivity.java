package com.crusoe.nav.normal;

import com.crusoe.nav.CrusoeApplication;
import com.crusoe.nav.CrusoeNavActivity;
import com.crusoe.nav.R;
import com.crusoe.nav.R.id;
import com.crusoe.nav.R.layout;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

public class CrusoeNormalActivity extends CrusoeNavActivity 
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
				setContentView(R.layout.normal_port);
			  else
				setContentView(R.layout.normal_land);
		}*/
	}
	
    protected void initialiseTabHost() {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        // TODO Put here your Tabs
        CrusoeNormalActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("DataTab").setIndicator("Data"));
        CrusoeNormalActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("MapTab").setIndicator("MapView"));

        mTabHost.setOnTabChangedListener(this);
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try{
			Log.i("TAG", "CrusoeNavActivity.onCreate");
			CrusoeApplication app = ((CrusoeApplication)getApplication());
			setContentView(R.layout.activity_main);
			
	        mViewPager = (ViewPager) findViewById(R.id.viewpager);
	        if(mViewPager!=null)
	        {
	        	//version para mi cell
	        	initialiseTabHost();

	        	// Tab Initialization
	        	//initialiseTabHost();
	        	mAdapter = new CrusoeNormalPagerAdapter(getSupportFragmentManager());
	        	// Fragments and ViewPager Initialization
	       	        
	        	mViewPager.setAdapter(mAdapter);
	        	mViewPager.setOnPageChangeListener(CrusoeNormalActivity.this);
	        }
		}
		catch(Exception e)
		{
			Log.i("ERROR CrusoeNavActivity ", e.getMessage());
		}
	}
}
