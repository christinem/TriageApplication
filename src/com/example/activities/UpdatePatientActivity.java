package com.example.activities;

import java.io.FileNotFoundException;

import com.example.exceptions.NoRecordSpecifiedException;
import com.example.triageapplication.Nurse;
import com.example.triageapplication.R;
import com.example.triageapplication.Record;
import com.example.triageapplication.StaffMember;
import com.example.triageapplication.R.layout;
import com.example.triageapplication.R.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

/** Allows the user to update patient information and save this to the disc. */
public class UpdatePatientActivity extends Activity {
	
	/** This staff member is logged in. */
	private StaffMember staff;
	
	/** This record is being updated. */
	private Record record;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_patient);
		
		// Gets the staff member and possibly the record from the last activity
	    Intent intent = getIntent();
	    staff = (StaffMember) intent.getSerializableExtra("staff");
		record = (Record) intent.getSerializableExtra("record");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present
		getMenuInflater().inflate(R.menu.update_patient, menu);
		return true;
	}
	
	/** Passes into the activity to access patient records.
	 * @param view 
	 */
	public void switchAccessRecord(View view) {
		Intent intent = new Intent(this, AccessRecordActivity.class);
		intent.putExtra("staff", staff);
		startActivity(intent);
		
	}
	
	/** Saves the record active in this activity,
	 * and then passes back to the Home Page Activity.  
	 * @param view A User interface type. 
	 * @throws FileNotFoundException 
	 */
	public void saveData(View view) throws FileNotFoundException {
		try {
			// Saves patient information
            if (staff instanceof Nurse) {
            	staff.updateVitals(record, this.getApplicationContext());
                Intent intent = new Intent(this, NurseHomePageActivity.class);
    			intent.putExtra("staff", staff);
        		startActivity(intent);
            }
            
            else {
            	staff.updatePrescription(record, this.getApplicationContext());
            	Intent intent = new Intent(this, DoctorHomePageActivity.class);
    			intent.putExtra("staff", staff);
        		startActivity(intent);
            }

		} catch (NoRecordSpecifiedException e) {
			// prompt for a record
			   Intent reenter = new Intent(this, 
					   ForceAccessRecordActivity.class);
			   reenter.putExtra("staff", staff);
			   startActivity(reenter);
		}		
	}
}
