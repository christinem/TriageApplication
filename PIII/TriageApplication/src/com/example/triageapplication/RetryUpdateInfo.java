package com.example.triageapplication;

import android.os.Bundle;
import android.app.Activity;
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
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.retry_update_info, menu);
		return true;
	}
	
	 /** Updates a given patient's record by taking input from the user
	  *  and modify the record files saved on disk.
	 * @param view This is the window where this activity is created.
	 */
	public void updatePatient(View view) {
		boolean seenByDoctor;
		
		Intent intent = getIntent();

		// Get nurse from AccessRecordActivity
	    Nurse nurse = (Nurse) intent.getSerializableExtra("nurse");
	    
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
	    EditText seenBy = (EditText) findViewById(R.id.seen_by_doctor);
	    String seenByDoctorString = seenBy.getText().toString();
	    
	    //Sets in the record whether this patient has seen a doctor yet. 
	    seenByDoctor = seenByDoctorString.equals("Yes");
	    
	    /** Were not supposed to see things to booleans 
	    if (seenByDoctorString.equals("Yes")) {
	    	seenByDoctor = true;
	    } else {
	    	seenByDoctor = false;
	    }*/
	    
	    // Sets the new patient's symptoms.
	    EditText symptom = (EditText) findViewById(R.id.symptoms);
	    String symptoms = symptom.getText().toString();
	    
	    // Sets the new patient's temperature. 
	    EditText temp = (EditText) findViewById(R.id.temperature);
	    String temperatureString = temp.getText().toString();
	    double temperature = Double.parseDouble(temperatureString);
	     
	    try {
	    	//This patient's record exists add to it.
	    	nurse.setTemperature(record, temperature);
	    	nurse.setBloodPressure(record, bloodPressure);
		    nurse.setHeartRate(record, heartRate);
		    nurse.setSeenByDoctor(record, seenByDoctor, 
		    		this.getApplicationContext());
		    nurse.setSymptoms(record, symptoms);
		
	    } catch (Exception e) {
			// prompt for a record
			   Intent reenter = new Intent(this, RetryUpdateInfo.class);
			   reenter.putExtra("nurse", nurse);
			   startActivity(reenter);
		}
	    
	    //With this patient dealt with the previous activity is returned too.
	    Intent intent1 = new Intent(this, UpdatePatientActivity.class);
	    intent1.putExtra("nurse", nurse);
	    intent1.putExtra("record", record);
	    startActivity(intent1);	     
	}

}
