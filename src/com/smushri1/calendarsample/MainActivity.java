package com.smushri1.calendarsample;

import com.smushri1.calendar.events.CalDataBase;
import com.smushri1.calendar.events.MyCalendarAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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
	Cursor cursor, calCursor;
	static MyAdapter adapter;
	static MyCalendarAdapter calAdapter;
	StatusData statusData;
	CalDataBase calDB;
	static boolean isCalEvent;
	
	public static final String CHOICE_SELECTED = "com.smushri1.calendarsample.choice_selected";
	protected static final String ID_EXTRA = "com.smushri1.calendarsample.C_ID";
	protected static final String EXTRA_BOOLEAN = "com.smushri1.calendarsample.EDIT";
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
		
		// updating Calendar List View
				calDB = new CalDataBase(getApplicationContext());
				list_calendar = (ListView)findViewById(R.id.listView_calendar_events);
				
				calDB.open();
				calCursor = calDB.query();
				
				calAdapter = new MyCalendarAdapter(this, calCursor, false);
				list_calendar.setAdapter(calAdapter);
			//	list_calendar.setBackgroundColor(Color.GRAY);
				list_calendar.setLongClickable(true);
				isCalEvent = true;
				list_calendar.setOnItemLongClickListener(this);
				
				updateCallist();
				calDB.close();
		
	
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onStart");
	
		statusData = new StatusData(getApplicationContext());
		list = (ListView)findViewById(R.id.listView_timeline);
	
		statusData.open();
		cursor = statusData.query();
		
		adapter = new MyAdapter(this, cursor, false);
		list.setAdapter(adapter);
		//list.setBackgroundColor(Color.GRAY);
		list.setLongClickable(true);
		isCalEvent = false;
		list.setOnItemLongClickListener(this);
	
		updatelist();
		statusData.close();
		
		
		/*// updating Calendar List View
		calDB = new CalDataBase(getApplicationContext());
		list_calendar = (ListView)findViewById(R.id.listView_calendar_events);
		
		calDB.open();
		calCursor = calDB.query();
		
		calAdapter = new MyCalendarAdapter(this, calCursor, false);
		list_calendar.setAdapter(calAdapter);
		list_calendar.setBackgroundColor(Color.GRAY);
		list_calendar.setLongClickable(true);
		isCalEvent = true;
		list_calendar.setOnItemLongClickListener(this);
		
		updateCallist();
		calDB.close();*/
		
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_event, menu);
		return true;
	}
	
	
	
	public void onAddEventClick(View v) {
		Log.d(TAG, "onAddEventDialog");
		showAddEventDialog();
	}
	
	
	public void showAddEventDialog() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("EVENT TYPE");
		builder.setIcon(R.drawable.ic_action_event_dark);
		builder.setItems(R.array.add_event_dialog_array, new DialogInterface.OnClickListener() {
		
			@Override
			public void onClick(DialogInterface dialog, int which) {
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
					break;
				}
				
			}
		});
		
		AlertDialog aceDialog = builder.create();
		aceDialog.show();
	
	}
	


	
	public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
		final long ITEM = id;
		
		Log.d(TAG, "onItemLongClicked...!!!");
		  AlertDialog.Builder confirmation = new AlertDialog.Builder(view.getContext());
		  confirmation.setTitle("SELECT OPTIONS");
		  confirmation.setIcon(android.R.drawable.ic_dialog_alert);		  
		  confirmation.setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {
		  
		  @Override
		  public void onClick(DialogInterface dialog, int which) {
			 try {
		    		AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
					alert.setTitle("DELETE")
					.setIcon(R.drawable.ic_action_discard_dark)
					.setMessage("Do You Want To Delete Event ?")
					.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if(isCalEvent == false)
							{ statusData.open();
						      UnScheduleBroadcastReceiver.setDeleteEvent(view.getContext(), position);
						      Log.d(TAG, "Delete intent fired");
						      
					    	  statusData.delete(ITEM);
					    	  updatelist();
						     }
							else{
								calDB.open();
								UnScheduleBroadcastReceiver.setDeleteEvent(view.getContext(), position);
								Log.d(TAG, "Delete Cal Event intent fired");
							      
						    	calDB.delete(ITEM);
						    	updateCallist();
							}
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
		    	  if(isCalEvent == false)
		    	  statusData.close();
		    	  calDB.close();
		      }
		  }
		 });
		  confirmation.setNeutralButton(R.string.button_edit, new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {   	
		      Intent intent = new Intent(view.getContext(), CreateEventActivity.class);
		      intent.putExtra(ID_EXTRA, position);
		      intent.putExtra(EXTRA_BOOLEAN, true);
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
	
	
	protected void updateCallist() {
		calCursor = calDB.query();
		calAdapter.changeCursor(calCursor);
	}
	 
}
