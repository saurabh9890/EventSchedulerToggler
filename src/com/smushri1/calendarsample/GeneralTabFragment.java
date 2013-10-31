package com.smushri1.calendarsample;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

import com.smushri1.calendar.events.AdapterCalEvent;
import com.smushri1.calendar.events.localCalendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class GeneralTabFragment extends Fragment {
	
	static final String DEBUG_TAG = "GeneralTabFragment";
	static final String TAG = "Edit_TimelineEventActivity";
	static final String STARTTIME_TAG = "StartTimePicker";
	static final String ENDTIME_TAG = "EndTimePicker";
	static final String DATE_TAG = "datePicker";
	static final String DAY_TAG = "dayPicker";
	
	View rootView;
	static int choice;
	protected static final String ID_EXTRA = "com.smushri1.calendarsample.C_ID";
	public static final String CHOICE_SELECTED = "com.smushri1.calendarsample.choice_selected";
	static Button bfrom_date, bto_date, bstart_time, bend_time, bday_picker, b_next, b_cancel, b_cal_event;
	static EditText eventName;
	private static ArrayList<CharSequence> mSelectedItems;
	public static int prevData = 0; // 0=NewData & 1=EditData
	private static int pressedBtn = -1; // 0=from & 1=to
	static boolean[] checked;
	static boolean isRepeatEvent;
	static int days;
	static CharSequence[] daysOfWeek = new CharSequence[]{"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
	static String displayName;
	static String _id, title;
	
	static DataBean bdata;
	static StatusData statusData;
	static Bundle extra;
	static Context context;
	static Cursor cursor, eventcursor;
	static int position;
	static String id;
	

	public GeneralTabFragment() {
		Log.d("DEBUG_TAG", " GeneralTabFragment Constructor");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle extra) {
		
		extra = getActivity().getIntent().getExtras();
		choice = extra.getInt(CHOICE_SELECTED);
		
		Log.d(DEBUG_TAG, "onCreateView --> Intent putextra value received: choice = " + choice);
		
		if(choice == 0)
		{
		Log.d(DEBUG_TAG, "onCreateView() with choice = 0");
		rootView = inflater.inflate(R.layout.general_tab_create,
				container, false);

		return rootView;
		}
		else{
			Log.d(DEBUG_TAG, "onCreateView() with choice = 1");
			rootView = inflater.inflate(R.layout.choose_cal_event,
					container, false);

			return rootView;
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		
	if(choice == 0)
	 {
		Log.d("DEBUG_TAG", " onActivityCreated --> with choice = 0");
		
		checked = new boolean[daysOfWeek.length];
		mSelectedItems = new ArrayList<CharSequence>(); // Where we track the selected items
		
		/* DataBEan class object */
		bdata = new DataBean();	
		
		isRepeatEvent = false;
		bfrom_date = (Button) rootView.findViewById(R.id.button_from_date);
		bfrom_date.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pressedBtn = 0;
				new DatePickerFragment().show(getActivity().getFragmentManager(), DATE_TAG);
			}
		});
		
		
		bto_date = (Button) rootView.findViewById(R.id.button_to_date);
		bto_date.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pressedBtn = 1;
				new DatePickerFragment().show(getActivity().getFragmentManager(), DATE_TAG);
			}
		});
		
		
		bstart_time = (Button) rootView.findViewById(R.id.button_start_time);
		bstart_time.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pressedBtn = 0;
				new TimePickerFragment().show(getActivity().getFragmentManager(), STARTTIME_TAG);
				
			}
		});
		
		
		bend_time = (Button) rootView.findViewById(R.id.button_end_time);
		bend_time.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pressedBtn = 1;
				new TimePickerFragment().show(getActivity().getFragmentManager(), ENDTIME_TAG);
			}
		});
		
		bday_picker = (Button) rootView.findViewById(R.id.button_repeat_event);
		bday_picker.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			//	isRepeatEvent = true;
				new DayPickerDialog().show(getActivity().getFragmentManager(), DAY_TAG);
			}
		});
		
		eventName = (EditText)rootView.findViewById(R.id.enter_event_name);
		
		if((getActivity().getIntent().getExtras())!= null)
		{  
			prevData = 1;
			prevData();	
		}
		
	 }else{
		 Log.d("DEBUG_TAG", " onActivityCreated --> with choice = 1");
			
		 b_cal_event = (Button)rootView.findViewById(R.id.button_choose_cal_event);
		 b_cal_event.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			 cursor = localCalendar.readCalendar(getActivity());
			
			 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			 builder.setTitle("Choose A Calendar");
			 
			 if (cursor.moveToFirst()) 
			  { 
				 builder.setCursor(cursor, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					// read event data for specific calendar
						readCalData(which);  // read event data for specific calendar
						
					}
				}, cursor.getColumnName(1))
				 .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				
				AlertDialog alert = builder.create();
		        alert.show();
			   }
			 	else{
					AlertDialog.Builder alert21 = new AlertDialog.Builder(getActivity());
						alert21.setTitle("Oops !!")
							 .setMessage(" Calendar is empty.")
							 .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									}
							});
						AlertDialog alert11 = alert21.create();
				         alert11.show();
			      }
			 
			  }
		});
	 }
  }
	
	
	public void readCalData(int which)
	{
		 eventcursor = localCalendar.readEvents(getActivity(), which+1);
		
		 AdapterCalEvent calAdapter = new AdapterCalEvent(getActivity(), eventcursor, false);
			
		 AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
		 build.setTitle("Choose An Event");
		 
		 if(eventcursor != null && eventcursor.moveToFirst()) 
		 {
			 build.setAdapter(calAdapter, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
				
					String title = eventcursor.getString(eventcursor.getColumnIndex(localCalendar.EVENT_TITLE));
					  b_cal_event.setText(title);
					  Log.d("Inner Loop ", " Hurrey !!! Button Text is Set ");
				}
			 })
			 .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
			
			AlertDialog alert1 = build.create();
	        alert1.show();
		   }
		 else{	
				AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
					alert.setTitle("Oops !!")
						 .setMessage("No Event is scheduled.")
						 .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								}
						});
					
					AlertDialog alert11 = alert.create();
			         alert11.show();
		 }

	}

	
	
	@SuppressWarnings("static-access")
	public void prevData() {
		
		Log.d(DEBUG_TAG, "prevdata = 1");
		prevData = 1;
		try{
			extra = getActivity().getIntent().getExtras();	
			position = extra.getInt(ID_EXTRA, 1);
			Log.d(TAG, " checking intent position value : %s  " + position);
			} catch(Exception e) 
			  { e.printStackTrace(); }
		
		statusData = new StatusData(getActivity());
		statusData.open();
		cursor = statusData.query();
		cursor.moveToPosition(position);		
		
		Log.d(TAG, " Getting position = cursor data:  " + cursor.getString(cursor.getColumnIndex(statusData.C_ID)));
		Log.d(TAG, " Getting position = cursor data:  " + cursor.getString(cursor.getColumnIndex(statusData.C_EVENT_NAME)));
		
		id = cursor.getString(cursor.getColumnIndex(statusData.C_ID));
		
		eventName = (EditText)rootView.findViewById(R.id.enter_event_name);
		bstart_time = (Button)rootView.findViewById(R.id.button_start_time);
		bend_time = (Button) rootView.findViewById(R.id.button_end_time);
		bfrom_date = (Button) rootView.findViewById(R.id.button_from_date);
		bto_date = (Button) rootView.findViewById(R.id.button_to_date);
		bday_picker = (Button) rootView.findViewById(R.id.button_repeat_event);
		
		
		String evName = cursor.getString(cursor.getColumnIndex(statusData.C_EVENT_NAME));
		eventName.setText("" + evName);
		
		String startTime = cursor.getString(cursor.getColumnIndex(statusData.C_START_TIME));
		bstart_time.setText("" + startTime);
		
		String endTime = cursor.getString(cursor.getColumnIndex(statusData.C_END_TIME));
		bend_time.setText("" + endTime);
		
		String fromDate = cursor.getString(cursor.getColumnIndex(statusData.C_FROM_DATE));
		bfrom_date.setText("" + fromDate);
		
		String toDate = cursor.getString(cursor.getColumnIndex(statusData.C_TO_DATE));
		bto_date.setText("" + toDate);
		
		String multiDay = cursor.getString(cursor.getColumnIndex(statusData.C_REPEAT_EVENT));
		bday_picker.setText("" + multiDay);
		
		String ringer = cursor.getString(cursor.getColumnIndex(statusData.C_RINGER_MODE));
		int media_vol = cursor.getInt(cursor.getColumnIndex(statusData.C_MEDIA_VOL));
		int alarm_vol = cursor.getInt(cursor.getColumnIndex(statusData.C_ALARM_VOL));
		int wifi = cursor.getInt(cursor.getColumnIndex(statusData.C_WIFI_MODE));
		int blue = cursor.getInt(cursor.getColumnIndex(statusData.C_BLUETOOTH_MODE));
		int data = cursor.getInt(cursor.getColumnIndex(statusData.C_DATA_MODE));
		
		bdata.setEventName(evName);
		bdata.setStartTime(startTime);
		bdata.setEndTime(endTime);
		bdata.setFromDate(fromDate);
		bdata.setToDate(toDate);
		bdata.setRepeatEvent(multiDay);
		
		bdata.setRingerMode(ringer);
		bdata.setMediaVol(media_vol);
		bdata.setAlarmVol(alarm_vol);
		bdata.setWifi(wifi);
		bdata.setBluetooth(blue);
		bdata.setData(data);
			
		mSelectedItems.clear();
		if(multiDay != null)
		{
			StringTokenizer token = new StringTokenizer(multiDay," ");
			while(token.hasMoreElements()){
				mSelectedItems.add(token.nextToken());
				Log.d("Arrylist Items", "ArrayList mSelectedItems: " + mSelectedItems);
				}
			checked = parse(mSelectedItems);
		}
		
		statusData.close();
}
	

	private boolean[] parse(ArrayList<CharSequence> mItems) {
	Log.d("Boolean checked", "marking checked day with prev data: " + mItems);	
	
		if(mItems.contains("Monday")){
			checked[0] = true;
		}
		if(mItems.contains("Tuesday")){
			checked[1] = true;
		}
		if(mItems.contains("Wednesday")){
			checked[2] = true;
		}
		if(mItems.contains("Thursday")){
			checked[3] = true;
		}
		if(mItems.contains("Friday")){
			checked[4] = true;
		}
		if(mItems.contains("Saturday")){
			checked[5] = true;
		}
		if(mItems.contains("Sunday")){
			checked[6] = true;
		}		
		return checked;
	}	

	
	
	 /* 
	  * DayPicker class :- Multiple choice Day Picker Dialog
	  */
	 
	public static class DayPickerDialog extends DialogFragment implements
			DialogInterface {
		
	public Dialog onCreateDialog(Bundle savedInstanceState) {
			
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	
			// if Creating New event, then new Data	
			
			if(prevData == 0){
				Log.d("DayPicker", "prevData == 0");
			builder.setTitle(R.string.pick_toppings)
					.setMultiChoiceItems(daysOfWeek, checked,
							new DialogInterface.OnMultiChoiceClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which, boolean isChecked) {	
									if (isChecked) {									
										// If the user checked the item, add it
										// to the selected items
										   mSelectedItems.add(daysOfWeek[which]);
										   checked[which] = mSelectedItems.contains(daysOfWeek[which]);
									} else if (mSelectedItems.contains(daysOfWeek[which])) {
										// Else, if the item is already in the
										// array, remove it
										mSelectedItems.remove(daysOfWeek[which]);
										checked[which] = mSelectedItems.contains(daysOfWeek[which]);
										Log.d("DayPicker", "Remaining items:  " + mSelectedItems);
									}	
								}
							})
					// Set the action buttons
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,int id) {
									isRepeatEvent = true;
									
									String[] strPlace = new String[mSelectedItems.size()];
									for (int i = 0; i < mSelectedItems.size(); i++){
										strPlace[i] = mSelectedItems.get(i).toString();
									}
									bday_picker.setText("");
									for (int j = 0; j < strPlace.length; j++) {
										bday_picker.append(strPlace[j] + "  ");

										// DataBean class
										bdata.setRepeatEvent(bday_picker.getText().toString());
										Log.d("Repeat Event data by Bean class",
												" %s:  " + bdata.getRepeatEvent());
									}
								}
							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
								}
							});
			  }
			
			
			
	// if Editing an event, then fetched previous Data from DB	
			
			if(prevData == 1){
				Log.d("DayPicker", "prevData == 1 !! prevData is enabled");
				builder.setTitle(R.string.pick_toppings)
						.setMultiChoiceItems(daysOfWeek, checked,
								new DialogInterface.OnMultiChoiceClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which, boolean isChecked) {	
										if (isChecked) {									
											// If the user checked the item, add it
											// to the selected items
											   mSelectedItems.add(daysOfWeek[which]);
											   checked[which] = mSelectedItems.contains(daysOfWeek[which]);
										} else if (mSelectedItems.contains(daysOfWeek[which])) {
											// Else, if the item is already in the
											// array, remove it
											mSelectedItems.remove(daysOfWeek[which]);
											checked[which] = mSelectedItems.contains(daysOfWeek[which]);
											Log.d("onDayPicker", "Remaining items:  " + mSelectedItems);
										}	
									}
								})
						// Set the action buttons
						.setPositiveButton(R.string.ok,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										isRepeatEvent = true;
										
										String[] strPlace = new String[mSelectedItems.size()];
										for (int i = 0; i < mSelectedItems.size(); i++){
											strPlace[i] = mSelectedItems.get(i).toString();
										}
										bday_picker.setText("");
										for (int j = 0; j < strPlace.length; j++) {
											bday_picker.append(strPlace[j] + "  ");

											// DataBean class
											bdata.setRepeatEvent(bday_picker.getText().toString());
											Log.d("Repeat Event data by Bean class",
													" %s:  " + bdata.getRepeatEvent());
										}
									}
								})
						.setNegativeButton(R.string.cancel,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.dismiss();
									}
								});
				  }
			
			return builder.create();
		}

	@Override
		public void cancel() {
			// TODO Auto-generated method stub
		}
	}

	
	 /* DatePickerFragment class  */
	 
	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {
		static int year;
		static int month;
		static int day;
	
		@Override
		public Dialog onCreateDialog(Bundle savedState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
		
			
		// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker dpicker, int y, int m, int d) {
			// Do something with the date chosen by the user
			if (pressedBtn == 0) {
				bfrom_date.setText(new StringBuilder().append(m + 1)
						.append("-").append(d).append("-").append(y)
						.append(" "));
				bdata.setFromDate(bfrom_date.getText().toString());
				Log.d("From Date data by Bean class",
						" %s:  " + bdata.getFromDate());
			} else if (pressedBtn == 1) {
				bto_date.setText(new StringBuilder().append(m + 1).append("-")
						.append(d).append("-").append(y).append(" "));
				bdata.setToDate(bto_date.getText().toString());
				Log.d("To Date data by Bean class",
						" %s:  " + bdata.getToDate());
			}
		}	
	}

	
	/* TimePickerFragment class  */
	 
	public static class TimePickerFragment extends DialogFragment {
	//implements 	TimePickerDialog.OnTimeSetListener {
		static int hour;
		static int minute;
		TimePickerDialog mTimePicker;
				
		@Override
		public Dialog onCreateDialog(Bundle savedState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			hour = c.get(Calendar.HOUR_OF_DAY);
			minute = c.get(Calendar.MINUTE);
			
			 mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
	                @Override
	                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
	                	// Do something with the time chosen by the user
	                	
	        			if (pressedBtn == 0) {
	        				bstart_time.setText(new StringBuilder().append(pad(hour))
	        						.append(":").append(pad(minute)).append(" "));

	        				bdata.setStartTime(bstart_time.getText().toString());
	        				Log.d("Start Time data by Bean class",
	        						" %s:  " + bdata.getStartTime());

	        			} else if (pressedBtn == 1) {
	        				bend_time.setText(new StringBuilder().append(pad(hour))
	        						.append(":").append(pad(minute)).append(" "));

	        				bdata.setEndTime(bend_time.getText().toString());
	        				Log.d("End Time data by Bean class",
	        						" %s:  " + bdata.getEndTime());
	        			}
	                }
	            }, hour, minute, DateFormat.is24HourFormat(getActivity()));
			
			// Create a new instance of TimePickerDialog and return it
			 return mTimePicker;
			//return new TimePickerDialog(getActivity(), this, hour, minute,DateFormat.is24HourFormat(getActivity()));
		}

		/*public void onTimeSet(TimePicker view, int hr, int min) {
			// Do something with the time chosen by the user
			
			if (pressedBtn == 0) {
				
				bstart_time.setText(new StringBuilder().append(pad(hr))
						.append(":").append(pad(min)).append(" "));

				bdata.setStartTime(bstart_time.getText().toString());
				Log.d("Start Time data by Bean class",
						" %s:  " + bdata.getStartTime());

			} else if (pressedBtn == 1) {
				bend_time.setText(new StringBuilder().append(pad(hr))
						.append(":").append(pad(min)).append(" "));

				bdata.setEndTime(bend_time.getText().toString());
				Log.d("End Time data by Bean class",
						" %s:  " + bdata.getEndTime());
			}
		}*/

		private static String pad(int c) {
			if (c >= 10 || c < 0)
				return String.valueOf(c);
			else
				return "0" + String.valueOf(c);
		}		
	}	
}