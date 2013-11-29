package com.example.triageapplication;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

/** Allows the user to try to access an existing record for viewing
 * after failing to enter a valid health card number.
 */
public class ForceAccessDisplayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_force_access_display);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items 
		// to the action bar if it is present.
		getMenuInflater().inflate(R.menu.force_access_display, menu);
		return true;
	}
	 
	/** Accessing a patient's record. If it exists passes into 
	 *  DisplayInformationActivity. 
	 * @param view This activities display window.
	 */
	public void display(View view){
		Intent intentStaff = getIntent();
	    StaffMember staff = 
	    		(StaffMember) intentStaff.getSerializableExtra("staff");
	    
	    EditText healthCardNum = 
	    		(EditText) findViewById(R.id.idnumber_display);
	    String healthNum = healthCardNum.getText().toString();
	    
	     try {
			//If this record exists retrieve it.  
			Record record = staff.getRecord(healthNum);
			Intent intent = new Intent(this, 
					DisplayInformationActivity.class);
			
		    //intent.putExtra("healthcardnumber", healthNum);
		    intent.putExtra("record", record);
			intent.putExtra("staff", staff);
		    startActivity(intent);
		    
		} catch (NoRecordSpecifiedException e) {
			   Intent reenter = new Intent(this, 
					   ForceAccessDisplayActivity.class);
			   
			   reenter.putExtra("staff", staff);
			   startActivity(reenter);
		}
	}

}
