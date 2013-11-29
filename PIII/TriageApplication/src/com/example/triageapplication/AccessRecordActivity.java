package com.example.triageapplication;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/** This activity allows access to patient records for modification. */
public class AccessRecordActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Sets the layout resource for this activity.
		setContentView(R.layout.activity_access_record);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflates the menu; this adds items to 
		// the action bar if present.
		getMenuInflater().inflate(R.menu.access_record, menu);
		return true;
	}
	
	/** Allows a user to access a patient's record by inputing their 
	 * health card number. If this patient is in the system it
	 * retrieve's their record and proceeds to the next activity.
	 * If not it returns to the same activity.  
	 * @param view This is the window where this activity is created.
	 */
	public void getRecord(View view){
		Record record = null;
		Intent intentStaff = getIntent();
	    StaffMember staff = (StaffMember) intentStaff.getSerializableExtra(
	    		"staff");
	    
	    EditText healthCardNum = (EditText) findViewById(R.id.idnumber);
	    String healthNum = healthCardNum.getText().toString();
	    
		try {
			//If this record exists retrieve it.  
			record = staff.getRecord(healthNum);
			
            if (staff instanceof Nurse) {
            	if (!record.isCheckedOut()){
	    			Intent intent = new Intent(this, 
	    					EnterUpdateInfoActivity.class);
	    		    intent.putExtra("record", record);
	    		    intent.putExtra("staff", staff);
	    		    startActivity(intent);
	    		    
            	} else {
            		TextView text = (TextView) findViewById(R.id.textView4);
         		    text.setVisibility(View.VISIBLE);
            	}
            }
            else {
            	if (!record.isCheckedOut()) {
	            	Intent intent = new Intent(this,
	            			EnterPrescriptionInfoActivity.class);
	    		    intent.putExtra("record", record);
	    			intent.putExtra("staff", staff);
	        		startActivity(intent);
	        		
            	} else {
            		TextView text = (TextView) findViewById(R.id.textView4);
         		    text.setVisibility(View.VISIBLE);
            	}
            }

		} catch (NoRecordSpecifiedException e) {
			// Record doesn't exist try to retrieve one that does.
			   Intent reenter = new Intent(this, 
					   ForceAccessRecordActivity.class);
			   reenter.putExtra("staff", staff);
			   startActivity(reenter);
		}
	}
}
