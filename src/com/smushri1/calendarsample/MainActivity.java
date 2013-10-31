package com.smushri1.calendarsample;

import com.smushri1.calendar.events.MyCalendarAdapter;
import com.smushri1.calendar.events.localCalendar;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;


public class MainActivity extends Activity implements OnItemLongClickListener {
	static final String TAG = "MainActivity";
	static final String DEBUG_TAG = "AddCreateEventDialog";
	
	ListView list, list_calendar;
	Cursor cursor;
	static MyAdapter adapter;
	StatusData statusData;
	
	String str;
	public static final String CHOICE_SELECTED = "com.smushri1.calendarsample.choice_selected";
	protected static final String ID_EXTRA = "com.smushri1.calendarsample.C_ID";
	public static final String DELETE_EVENT = "delete_event";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_event);
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
	
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onStart");
	
		statusData = new StatusData(getApplicationContext());
		list = (ListView)findViewById(R.id.listView_timeline);
	
		statusData.open();
		cursor = statusData.query();
		Log.d(TAG, "onResume cursor: returned from DB");
		
		adapter = new MyAdapter(this, cursor, false);
		list.setAdapter(adapter);
		list.setBackgroundColor(color.darker_gray);
		
		Log.d(TAG, "onResume Adapter added to ListView");
		list.setLongClickable(true);
		list.setOnItemLongClickListener(this);
	
		updatelist();
		statusData.close();
		
		Log.d(TAG, "onResume Exit");
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_event, menu);
		return true;
	}
	
	public void showAddEventDialog() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("EVENT TYPE");
		builder.setIcon(R.drawable.ic_action_event_dark);
		builder.setItems(R.array.add_event_dialog_array, new DialogInterface.OnClickListener() {
		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d(DEBUG_TAG, " you selected " + which + " from list");
				int choice;
				
				switch (which) {
				case 0 : 
					choice = 0;
					Intent addEvent = new Intent(getApplicationContext(),CreateEventActivity.class);
					addEvent.putExtra(CHOICE_SELECTED, choice);
					startActivity(addEvent);
					break;
				case 1 : // calls Existing_Calendar_Event class; 
					choice = 1;
					Intent addCalEvent = new Intent(getApplicationContext(),CreateEventActivity.class);
					addCalEvent.putExtra(CHOICE_SELECTED, choice);
					startActivity(addCalEvent);
					
					
				/*	Cursor eventcursor = localCalendar.readCalendar(getApplicationContext());
					list_calendar = (ListView) findViewById(R.id.listView_calendar_events);
					
					Log.d("Option 2", " event cursor returned: " + eventcursor);
					MyCalendarAdapter cal_adapter = new MyCalendarAdapter(getApplicationContext(), eventcursor, false);
					list_calendar.setAdapter(cal_adapter);
					list_calendar.setBackgroundColor(color.darker_gray);
				*/
					break;
				}
				
			}
		});
		
		AlertDialog aceDialog = builder.create();
		aceDialog.show();
	
	}
	
	
	public void onAddEventClick(View v) {
		Log.d(TAG, "onAddEventDialog");
		showAddEventDialog();
	}


	
	public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
		final long ITEM = id;
		
		 // Ask the user if they want to delete
		Log.d(TAG, "onItemLongClicked...!!!");
		  AlertDialog.Builder confirmation = new AlertDialog.Builder(view.getContext());
		  confirmation.setTitle("SELECT OPTIONS");
		  confirmation.setIcon(android.R.drawable.ic_dialog_alert);		  
		  confirmation.setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {
		  
		  @Override
		  public void onClick(DialogInterface dialog, int which) {
			  Log.d(TAG, "OnClick of delete");
		    try {
		    		AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
					alert.setTitle("DELETE")
					.setIcon(R.drawable.ic_action_discard_dark)
					.setMessage("Do You Want To Delete Event ?")
					.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							  statusData.open();
						      UnScheduleBroadcastReceiver.setDeleteEvent(view.getContext(), position);
						      Log.d(TAG, "Delete intent fired");
						      
					    	  statusData.delete(ITEM);
					    	  updatelist();
							}
					})
					.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							  dialog.dismiss();
							}
					});
					
					AlertDialog alert11 = alert.create();
			         alert11.show();
			 } catch (Exception ex) { }
		      finally {
		    	  statusData.close();
		      }
		  }
		 });
		  confirmation.setNeutralButton(R.string.button_edit, new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {   	
		      Intent intent = new Intent(view.getContext(), CreateEventActivity.class);
		      intent.putExtra(ID_EXTRA, position);
		      startActivity(intent);
		    }
		  });
		   confirmation.show();
		   return true;
		}


	protected void updatelist() {
		cursor = statusData.query();
		adapter.changeCursor(cursor);
	}
	 
}
