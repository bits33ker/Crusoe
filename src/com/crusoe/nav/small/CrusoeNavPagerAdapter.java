package com.crusoe.nav.small;

import com.crusoe.nav.CompassFragment;
import com.crusoe.nav.GpsFragment;
import com.crusoe.nav.MapViewFragment;
import com.crusoe.nav.StatFragment;
import com.crusoe.nav.TracksFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CrusoeNavPagerAdapter extends FragmentPagerAdapter {

	public CrusoeNavPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			return new GpsFragment();
		case 1:
			return new DataFragment();
		case 2:
			return new CompassFragment();
		case 3:
			return new StatFragment();
		case 4:
			return new MapViewFragment();
		case 5:
			return new TracksFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 6;//data y compass
	}

}
