package com.example.activities;

import com.example.exceptions.NotCheckedInException;
import com.example.triageapplication.Doctor;
import com.example.triageapplication.Pharmacist;
import com.example.triageapplication.R;
import com.example.triageapplication.Record;
import com.example.triageapplication.StaffMember;
import com.example.triageapplication.R.id;
import com.example.triageapplication.R.layout;
import com.example.triageapplication.R.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * This activity displays a Patient's Record History
 */
public class DisplayInformationActivity extends Activity {
         
	/** This activities' Intent object.  */
	Intent intent;
	
	/** This activities' Record object. */
	Record record;
	
	/** This activities' Staff Member object. */
	StaffMember staff;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_information);
		
		 intent = getIntent();
	     record = (Record) intent.getSerializableExtra("record");
	     staff = (StaffMember) intent.getSerializableExtra("staff");

		
	      StringBuilder text = staff.getInfo(this.getApplicationContext(), 
	    		  record);
		    
		  TextView recordHistory = (TextView) findViewById(R.id.display);
		  recordHistory.setMovementMethod(new ScrollingMovementMethod());
		  recordHistory.setText(text);
		  
		  // Hide the discharge patient button if staff is a Doctor or Nurse
		  if (staff instanceof Doctor || staff instanceof Pharmacist) {
			  Button button = (Button) findViewById(R.id.discharge_patient);
			  button.setVisibility(View.INVISIBLE);
		  }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the 
		// action bar if it is present.
		getMenuInflater().inflate(R.menu.display_information, menu);
		return true;
	}
	
	/**	This removes a patient from the application. 
	 * @param view This activities display window.
	 */
	public void dischargePatient(View view) {
		
		try {
			staff.dischargePatient(record.getHealthCardNum(), 
					this.getApplicationContext());
			
			//StaffMember.getRecords().removePatient(record.getHealthCardNum());
			//StaffMember.getRecords().add(record);
			Intent backIntent = new Intent(this, 
					NurseHomePageActivity.class);
			
			backIntent.putExtra("staff", staff);
			startActivity(backIntent);
			
		} catch (NotCheckedInException e) {
		    TextView text = (TextView) findViewById(R.id.not_checked_in);
		    text.setVisibility(View.VISIBLE);
		}
		
	}
	  
}