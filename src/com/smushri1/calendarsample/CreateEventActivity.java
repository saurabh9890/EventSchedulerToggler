package com.smushri1.calendarsample;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class CreateEventActivity extends FragmentActivity implements
		ActionBar.TabListener {
	static final String DEBUG_TAG = "CreateEventActivity";
	
	SectionFragmentPagerAdapter mSectionsFragPagerAdapter;
	static ViewPager mViewPager;
	static DataBean bdata;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(DEBUG_TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Create the adapter that will return a fragment for each of the two
		// primary sections of the app.
		mSectionsFragPagerAdapter = new SectionFragmentPagerAdapter(getSupportFragmentManager());
		
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsFragPagerAdapter);

		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsFragPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsFragPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
		bdata = new DataBean();
	}
	

		@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_event, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		Log.d(DEBUG_TAG, "onTabSelected");

		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		Log.d(DEBUG_TAG, "onTabUnselected");
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		Log.d(DEBUG_TAG, "onTabReselected");
	}

	
	
	public void showNextView(View v) {
		
		GeneralTabFragment.bdata.setEventName(GeneralTabFragment.eventName.getText().toString());
		
		if(GeneralTabFragment.bdata.getEventName() == null)
		{	AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("ERROR !!")
				 .setMessage("Please Enter Event Name.")
				 .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						CreateEventActivity.mViewPager.setCurrentItem(0);
						}
				});
			
			AlertDialog alert11 = alert.create();
	         alert11.show();
	         
		 
		}else if(GeneralTabFragment.bdata.getStartTime() == null)
		{	AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("ERROR !!")
				 .setMessage("Please Enter Start Time.")
				 .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						CreateEventActivity.mViewPager.setCurrentItem(0);
						}
				});
			
			AlertDialog alert11 = alert.create();
	         alert11.show();
	         
		 
		}else if(GeneralTabFragment.bdata.getEndTime() == null)
			{
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setTitle("ERROR !!")
				.setMessage("Please Enter End Time.")
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					CreateEventActivity.mViewPager.setCurrentItem(0);
					}
			});
			
			AlertDialog alert11 = alert.create();
	         alert11.show();
		
		}else if(GeneralTabFragment.bdata.getFromDate() == null)
		  {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("ERROR !!")
			.setMessage("Please Enter From Date.")
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					CreateEventActivity.mViewPager.setCurrentItem(0);
					}
			});
			
			AlertDialog alert11 = alert.create();
	         alert11.show();
		
		}else if(GeneralTabFragment.bdata.getToDate() == null)
		  {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("ERROR !!")
			.setMessage("Please Enter To Date.")
			.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					CreateEventActivity.mViewPager.setCurrentItem(0);
					}
			});
			
			AlertDialog alert11 = alert.create();
	         alert11.show();
		}
		
		// show ToggleTabFragment
		CreateEventActivity.mViewPager.setCurrentItem(1);
	}
	
	
	
	public void showCancelView(View v) 
	{		
		finish();
	}

} // Main Activity Ends Here
