package com.example.activities;

import java.io.FileNotFoundException;

import com.example.triageapplication.R;
import com.example.triageapplication.Record;
import com.example.triageapplication.StaffMember;
import com.example.triageapplication.R.id;
import com.example.triageapplication.R.layout;
import com.example.triageapplication.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class RetryUpdateInfo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retry_update_info);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present
		getMenuInflater().inflate(R.menu.retry_update_info, menu);
		return true;
	}
	
	/** Updates a given patient's record by taking input from the user
	 *  and modifying the record files saved on disk.
	 * @param view This is the window where this activity is created.
	 */
	public void updatePatient(View view) {
		boolean seenByDoctor;
		
		Intent intent = getIntent();

		// Gets the staff member from AccessRecordActivity
	    StaffMember staff = (StaffMember) intent.getSerializableExtra("staff");
	    
	    // Get Record from AccessRecordActivity
	    Record record = (Record) intent.getSerializableExtra("record");
	    
	    // Sets patient's new blood pressure measurement . 
	    EditText pressure = (EditText) findViewById(R.id.blood_pressure);
	    String bloodPressureString = pressure.getText().toString();
	    int bloodPressure = Integer.parseInt(bloodPressureString);
	    
	    // Sets patient's new heart rate measurement .
	    EditText rate = (EditText) findViewById(R.id.heart_rate);
	    String heartRateString = rate.getText().toString();
	    int heartRate = Integer.parseInt(heartRateString);
	    
	    // Sets patient's new blood pressure measurement .
	    EditText seenBy = (EditText) findViewById(R.id.prescription_name);
	    String seenByDoctorString = seenBy.getText().toString();
	    
	    // Sets in the record whether this patient has seen a doctor yet. 
	    seenByDoctor = seenByDoctorString.equals("Yes");
	    
	    // Sets the new patient's symptoms.
	    EditText symptom = (EditText) findViewById(
	    		R.id.prescription_instructions);
	    String symptoms = symptom.getText().toString();
	    
	    // Sets the new patient's temperature. 
	    EditText temp = (EditText) findViewById(R.id.temperature);
	    String temperatureString = temp.getText().toString();
	    double temperature = Double.parseDouble(temperatureString);
	     
	    try {
	    	// This patient's record exists add to it.
	    	staff.setTemperature(record, temperature);
	    	staff.setBloodPressure(record, bloodPressure);
		    staff.setHeartRate(record, heartRate);
		    staff.setSeenByDoctor(record, seenByDoctor, 
		    		this.getApplicationContext());
		    staff.setSymptoms(record, symptoms);
		    staff.updateUrgency(record);
		    
		} catch (Exception e) {
			// Prompt for a record
			   Intent reenter = new Intent(this, RetryUpdateInfo.class);
			   reenter.putExtra("staff", staff);
			   startActivity(reenter);
		}
	    
		try {
			StaffMember.getRecords().removePatient(record.getHealthCardNum());
			StaffMember.getRecords().add(record);
			StaffMember.getRecords().saveRecordsToFile("PatientsAndRecords", 
					openFileOutput(
					"PatientsAndRecords", Context.MODE_PRIVATE));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    
	    // With this patient dealt with the previous activity is returned to.
	    Intent intent1 = new Intent(this, UpdatePatientActivity.class);
	    intent1.putExtra("staff", staff);
	    intent1.putExtra("record", record);
	    startActivity(intent1);	     
	}
}
