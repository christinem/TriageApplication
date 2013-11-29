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
public class EnterUpdateInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Sets the layout resource for this activity.
		setContentView(R.layout.activity_enter_update_info);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to
		// the action bar if it is present.
		getMenuInflater().inflate(R.menu.enter_update_info, menu);
		return true;
	}
	
	/** Updates a patients vital signs and whether 
	 * or not they've seen a doctor.
	 * @param view This activities window.
	 */
	public void updatePatient(View view) {
		boolean seenByDoctor;
		
		Intent intent = getIntent();

		// Get the staff member from AccessRecordActivity.
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
	    
	    //Sets in the record whether this patient has seen a doctor yet. 
	    seenByDoctor = seenByDoctorString.equalsIgnoreCase("Yes");
	    
	    // Sets the new patient's symptoms.
	    EditText symptom = 
	    		(EditText) findViewById(R.id.prescription_instructions);
	    String symptoms = symptom.getText().toString();
	    
	    // Sets the new patient's temperature. 
	    EditText temp = (EditText) findViewById(R.id.temperature);
	    String temperatureString = temp.getText().toString();
	    double temperature = Double.parseDouble(temperatureString);
	     
	    try {
	    	//This patient's record exists add to it.
	    	staff.setTemperature(record, temperature);
	    	staff.setBloodPressure(record, bloodPressure);
		    staff.setHeartRate(record, heartRate);
		    staff.setSeenByDoctor(record, seenByDoctor,
		    		this.getApplicationContext());
		    staff.setSymptoms(record, symptoms);
		    staff.updateUrgency(record);
		    
		} catch (Exception e) {
			// prompt for a record
			   Intent reenter = new Intent(this, RetryUpdateInfo.class);
			   reenter.putExtra("staff", staff);
			   startActivity(reenter);
		}
	    
		try {
			if (StaffMember.getRecords().getUrgencyRecords().
					contains(record.getHealthCardNum())){
			    StaffMember.getRecords().removePatientFromUrgency(record);
			}
			
			StaffMember.getRecords().removePatient(record.getHealthCardNum());
			StaffMember.getRecords().add(record);
			StaffMember.getRecords().saveRecordsToFile("PatientsAndRecords", 
					openFileOutput("PatientsAndRecords",
							Context.MODE_PRIVATE));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	    //With this patient dealt with the previous activity is returned too.
	    Intent intent1 = new Intent(this, UpdatePatientActivity.class);
	    intent1.putExtra("staff", staff);
	    intent1.putExtra("record", record);
	    startActivity(intent1);	     
	}
}
