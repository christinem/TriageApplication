package com.example.triageapplication;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class ForceAccessRecordActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_force_access_record);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to 
		// the action bar if it is present.
		getMenuInflater().inflate(R.menu.force_access_record, menu);
		return true;
	}
	
	/**
	 * Attempts to access record of patient, if successful, 
	 * move to Update Patient Info Activity.
	 * @param view A user interface type.
	 */
	public void getRecord(View view){
		Record record = null;
		Intent intentNurse = getIntent();
	    StaffMember staff = (StaffMember) intentNurse.getSerializableExtra("staff");
	    
	    EditText healthCardNum = (EditText) findViewById(R.id.idnumber);
	    String healthNum = healthCardNum.getText().toString();
	    
		try {
			record = staff.getRecord(healthNum);
			Intent intent = new Intent(this, EnterUpdateInfoActivity.class);
		    intent.putExtra("record", record);
			intent.putExtra("staff", staff);
			startActivity(intent);
			
		} catch (NoRecordSpecifiedException e) {
			// prompt for a record
			   Intent reenter = new Intent(this, ForceAccessRecordActivity.class);
			   reenter.putExtra("staff", staff);
			   startActivity(reenter);
		}
	     
	   
	}	
	
	

}
