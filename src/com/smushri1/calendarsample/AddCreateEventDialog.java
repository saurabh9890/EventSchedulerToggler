
package com.smushri1.calendarsample;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;


public class AddCreateEventDialog extends DialogFragment{

	static final String DEBUG_TAG = "AddCreateEventDialog";
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	 {	
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("EVENT TYPE");
		builder.setItems(R.array.add_event_dialog_array, new DialogInterface.OnClickListener() {
		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.d(DEBUG_TAG, " you selected " + which + " from list");
				switch (which) {
				case 0 : 
					startActivity(new Intent(getActivity(),CreateEventActivity.class));
					break;
				case 1 : // calls Existing_Calendar_Event class; not Toggle_event class.
					//startActivity(new Intent(getActivity(),ToggleEventActivity.class));
					break;
				}
				
			}
		});		
		return builder.create();
	}
}
