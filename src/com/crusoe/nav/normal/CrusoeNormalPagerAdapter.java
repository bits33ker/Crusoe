package com.crusoe.nav.normal;

import com.crusoe.nav.GpsFragment;
import com.crusoe.nav.MapViewFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CrusoeNormalPagerAdapter extends FragmentPagerAdapter {

	public CrusoeNormalPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			return new DataNormalFragment();
		case 1:
			return new MapViewFragment();
		case 2:
			return new GpsFragment();
		//case 4:
			//return new TracksFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;//data y compass
	}

}
