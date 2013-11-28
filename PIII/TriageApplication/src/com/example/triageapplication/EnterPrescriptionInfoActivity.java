package com.example.triageapplication;

import java.io.FileNotFoundException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

/** This activity modifies existing patient's records. */
public class EnterPrescriptionInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Sets the layout resource for this activity.
		setContentView(R.layout.activity_enter_prescription_info);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to
		// the action bar if it is present.
		getMenuInflater().inflate(R.menu.enter_prescription_info, menu);
		return true;
	}
	
	public void updatePatient(View view) {
		Intent intent = getIntent();

		// Get the staff member from AccessRecordActivity.
	    StaffMember staff = (StaffMember) intent.getSerializableExtra("staff");
	    
	    // Get Record from AccessRecordActivity
	    Record record = (Record) intent.getSerializableExtra("record");
	    
	    // Sets patient's new prescription name. 
	    EditText prescriptionName = (EditText) findViewById(R.id.prescription_name);
	    String prescriptionNameString = prescriptionName.getText().toString();
	    
	    // Sets patient's new prescription instructions. 
	    EditText prescriptionInstructions = (EditText)
	    		findViewById(R.id.prescription_instructions);
	    String prescriptionInstructionsString = 
	    		prescriptionInstructions.getText().toString();
	     
	    try {
	    	//This patient's record exists add to it.
	    	staff.setPrescription(record, prescriptionNameString, prescriptionInstructionsString,
	    			this.getApplicationContext());
		    
		} catch (Exception e) {
			// prompt for a record
			   Intent reenter = new Intent(this, RetryUpdateInfo.class);
			   reenter.putExtra("staff", staff);
			   startActivity(reenter);
		}
	    
		StaffMember.getRecords().removePatient(record.getHealthCardNum());
		StaffMember.getRecords().add(record);

		
	    //With this patient dealt with the previous activity is returned too.
	    Intent intent1 = new Intent(this, UpdatePatientActivity.class);
	    intent1.putExtra("staff", staff);
	    intent1.putExtra("record", record);
	    startActivity(intent1);	     
	}
}
