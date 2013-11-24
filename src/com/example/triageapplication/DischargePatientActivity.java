package com.example.triageapplication;

	import java.io.FileNotFoundException;

	import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

	/** This activity creates patients. */
	public class DischargePatientActivity extends Activity {	
		Context context;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			// Sets the layout resource for this activity.
			setContentView(R.layout.activity_create_patient);

		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to 
			// the action bar if it is present.
			getMenuInflater().inflate(R.menu.create_patient, menu);
			return true;
		}
	    
		/** Removes a Record for a patient.
		 * @param view A user interface type.
		 */
		public void dischargePatient(View view) {
			
			// Grabs the staff from the previous Activity.
	        Intent intentStaff = getIntent();
	        StaffMember staff = (StaffMember) intentStaff.getSerializableExtra("staff");
	        
		    EditText healthCardNum = (EditText) findViewById(R.id.idnumber);
		    String healthCard = healthCardNum.getText().toString();
		    
		    staff.getRecords().getRecord(healthCard).discharge(context);
	       			
			try {
				staff.getRecords().removePatient(healthCard);
						this.getApplicationContext();
						
				// Save to file
				try {
					StaffMember.getRecords().saveToFile(openFileOutput(
							"PatientsAndRecords",Context.MODE_PRIVATE));

					} catch (FileNotFoundException e) {
						e.printStackTrace();

					}
			}
			// Catch any exceptions... Needs to be implimented.
			catch (Exception e) {
				
			}
			
				// With the new patient dealt with the application
				// passes back to the home page activity.
				Intent intent = new Intent(this, NurseHomePageActivity.class);
				intent.putExtra("staff", staff);
				startActivity(intent);


		}
	}