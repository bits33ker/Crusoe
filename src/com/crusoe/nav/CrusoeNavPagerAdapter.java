package com.crusoe.nav;

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
			return new DataFragment();
		case 1:
			return new CompassFragment();
		case 2:
			return new StatFragment();
		case 3:
			return new MapViewFragment();
		case 4:
			return new TracksFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 5;//data y compass
	}

}
