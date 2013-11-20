package com.example.triageapplication;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

/**
 * This activity also allows access to patient's record, but for displaying information, instead of updating.
 * @author Christine
 */
public class AccessDisplayRecordActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_access_display_record);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.access_display_record, menu);
		return true;
	}
	
	public void display(View view){
		Intent intentNurse = getIntent();
	    StaffMember nurse = (StaffMember) intentNurse.getSerializableExtra("nurse");
	    
	    EditText healthCardNum = (EditText) findViewById(R.id.idnumber_display);
	    String healthNum = healthCardNum.getText().toString();
	    
	//	try {
			//If this record exists retrieve it.  
			//record = nurse.getRecord(healthNum);
			Intent intent = new Intent(this, DisplayInformationActivity.class);
		    intent.putExtra("healthcardnumber", healthNum);
		    //intent.putExtra("nurse", nurse);
		    startActivity(intent);
		//FIX THIS - Add something when record does not exist
		/*} catch (NoRecordSpecifiedException e) {
			// Record doesn't exist try to retrieve one that does.
			   Intent reenter = new Intent(this, 
					   ForceAccessRecordActivity.class);
			   reenter.putExtra("nurse", nurse);
			   startActivity(reenter);
		}*/
	}

}
