package com.example.activities;

import com.example.exceptions.NoRecordSpecifiedException;
import com.example.triageapplication.Nurse;
import com.example.triageapplication.R;
import com.example.triageapplication.Record;
import com.example.triageapplication.StaffMember;
import com.example.triageapplication.R.id;
import com.example.triageapplication.R.layout;
import com.example.triageapplication.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/** Allows the user to try to access an existing record for modification
 * after failing to enter a valid health card number.
 */
public class ForceAccessRecordActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_force_access_record);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to 
		// the action bar if it is present.
		getMenuInflater().inflate(R.menu.force_access_record, menu);
		return true;
	}
	
	/**
	 * Attempts to access record of patient, if successful, 
	 * move to Update Patient Info Activity.
	 * @param view A user interface type.
	 */
	public void getRecord(View view){
		Record record = null;
		Intent intentStaff = getIntent();
	    StaffMember staff = (StaffMember) intentStaff.getSerializableExtra("staff");
	    
	    EditText healthCardNum = (EditText) findViewById(R.id.idnumber);
	    String healthNum = healthCardNum.getText().toString();
	    
	    try {
			//If this record exists retrieve it.  
			record = staff.getRecord(healthNum);
			
            if (staff instanceof Nurse) {
            	if (!record.isCheckedOut()){
	    			Intent intent = new Intent(this, EnterUpdateInfoActivity.class);
	    		    intent.putExtra("record", record);
	    		    intent.putExtra("staff", staff);
	    		    startActivity(intent);
            	} else {
            		TextView text = (TextView) findViewById(R.id.textView4);
         		    text.setVisibility(View.VISIBLE);
            	}
            }
            else {
            	if (!record.isCheckedOut()) {
	            	Intent intent = new Intent(this, EnterPrescriptionInfoActivity.class);
	    		    intent.putExtra("record", record);
	    			intent.putExtra("staff", staff);
	        		startActivity(intent);
            	} else {
            		TextView text = (TextView) findViewById(R.id.textView4);
         		    text.setVisibility(View.VISIBLE);
            	}
            }


		} catch (NoRecordSpecifiedException e) {
			// prompt for a record
			   Intent reenter = new Intent(this, ForceAccessRecordActivity.class);
			   reenter.putExtra("staff", staff);
			   startActivity(reenter);
		}
	}	
}
