package com.example.triageapplication;

import java.io.FileNotFoundException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

/** If the input was invalid for creating a patient this allows
 *  the staff member to input valid information.
 */
public class RetryAddPatientActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Sets the layout resource for this activity.
		setContentView(R.layout.activity_retry_add_patient);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.retry_add_patient, menu);
		return true;
	}
	
	/**
	 * Creates a new Record for a patient.
	 * @param view A user interface type.
	 */
	public void createNewPatient(View view) {
		
		// Get the staff member from previous Activity
        Intent intentStaff = getIntent();
        StaffMember staff = (StaffMember) intentStaff.getSerializableExtra("staff");
        
		EditText first_name = (EditText)findViewById(R.id.first_name);
		String first = first_name.getText().toString();
		
		EditText last_name = (EditText)findViewById(R.id.last_name);
		String last = last_name.getText().toString();
		
		EditText day_num = (EditText)findViewById(R.id.day);
		String day = day_num.getText().toString();
		
		EditText month_num = (EditText)findViewById(R.id.month);
		String month = month_num.getText().toString();
		
		EditText year_num = (EditText)findViewById(R.id.year);
		String year = year_num.getText().toString();
		
		EditText healthNum = (EditText)findViewById(R.id.healthcard_number);
		String healthCardNumber = healthNum.getText().toString();
		
		String[] name = {first, last};
		String[] dob = {day, month, year};
		
		try {
			// All user input is valid, so let's create this patient.
			staff.addPatient(name, dob, healthCardNumber,
					this.getApplicationContext());
			
			// Save to file
			try {
				StaffMember.getRecords().saveToFile(openFileOutput(
						"PatientsAndRecords", Context.MODE_PRIVATE));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			// With the new patient dealt with the application
			// passes back to the home page activity.
			Intent intent = new Intent(this, HomePageActivity.class);
			intent.putExtra("staff", staff);
			startActivity(intent);
		
		} catch (InvalidDayOfBirthException e) {
			Intent intent = new Intent(this, RetryAddPatientActivity.class);
			intent.putExtra("staff", staff);
			startActivity(intent);
		
		} catch (InvalidMonthOfBirthException e) {
			Intent intent = new Intent(this, RetryAddPatientActivity.class);
			intent.putExtra("staff", staff);
			startActivity(intent);
		} catch (InvalidYearOfBirthException e) {
			Intent intent = new Intent(this, RetryAddPatientActivity.class);
			intent.putExtra("staff", staff);
			startActivity(intent);
		
		} catch (InvalidHealthCardNumberException e) {
			Intent intent = new Intent(this, RetryAddPatientActivity.class);
			intent.putExtra("staff", staff);
			startActivity(intent);
		}	
	}
}
