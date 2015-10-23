package com.crusoe.nav.xlarge;

import com.crusoe.nav.MapViewFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CrusoeNav10PagerAdapter extends FragmentPagerAdapter {

	public CrusoeNav10PagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			return new Data10Fragment();
		case 1:
			return new MapViewFragment();
		//case 4:
			//return new TracksFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 2;//data y compass
	}

}
