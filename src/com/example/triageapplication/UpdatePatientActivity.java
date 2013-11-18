package com.example.triageapplication;

import java.io.FileNotFoundException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

/** Allows the user to update patient information and save 
 *  this to the disc.  
 */
public class UpdatePatientActivity extends Activity {

	/** This contains the laset activity's information. */
	private Intent intent;
	
	/** This nurse is logged in. */
	private Nurse nurse;
	
	/** This record is being updated. */
	private Record record;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_patient);
		
		// Get nurse and possibly record from last activity
	    intent = getIntent();
	    nurse = (Nurse) intent.getSerializableExtra("nurse");
		record = (Record) intent.getSerializableExtra("record");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.update_patient, menu);
		return true;
	}
	
	/** Passes into the an activity to access patient records.
	 * @param view 
	 */
	public void switchAccessRecord(View view) {
		Intent intent = new Intent(this, AccessRecordActivity.class);
		intent.putExtra("nurse", nurse);
		startActivity(intent);
		
	}
	
	/** Saves the record this nurse has inputed,
	 *  and then passes back to the Home Page Activity.  
	 * @param view A User interface type. 
	 * @throws FileNotFoundException 
	 */
	public void saveData(View view) throws FileNotFoundException {
		try {
			// Saves patient information
			nurse.updateVitals(record, this.getApplicationContext());
			
            
            Intent intent = new Intent(this, HomePageActivity.class);
    		intent.putExtra("nurse", nurse);
    		startActivity(intent);

		} catch (NoRecordSpecifiedException e) {
			// prompt for a record
			   Intent reenter = new Intent(this, ForceAccessRecordActivity.class);
			   reenter.putExtra("nurse", nurse);
			   startActivity(reenter);
		}		
	}

}
