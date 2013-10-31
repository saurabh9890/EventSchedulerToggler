package com.smushri1.calendarsample;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.smushri1.calendarsample.GeneralTabFragment;
import com.smushri1.calendarsample.ToggleTabFragment;

public class SectionFragmentPagerAdapter extends FragmentPagerAdapter{
	
	static final String DEBUG_TAG = "SectionFragmentPagerAdapter";
	
	 private GeneralTabFragment fragGeneral;
	 private ToggleTabFragment fragToggle;
	
	public SectionFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
		fragGeneral = new GeneralTabFragment();
		fragToggle = new ToggleTabFragment();
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			return fragGeneral;
		case 1:
			return fragToggle;
		default:
			break;
		}
		return null;

	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		switch (position) {
		case 0:
			//return fragGeneral.getString(R.string.general_tab).toUpperCase(loc);
			return "General";
		case 1:
			//return fragToggle.getString(R.string.toggle_tab).toUpperCase(loc);
			return "Toggles";
			
		}
		return null;
	}

}
