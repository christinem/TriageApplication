package com.example.triageapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

/** This is the home page the appears once a staff member has logged in. */
public class HomePageActivity extends Activity {
	
/** This is a text field in this activity. */
private TextView tv;

/**This staff member has logged in.*/
private StaffMember staff;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Sets the layout resource for this activity.
		setContentView(R.layout.activity_home_page);
		
		// Creates the text area for the welcome message to be displayed in.
		tv = (TextView) findViewById(R.id.textView2);
		
		// Get the message from the intent
        Intent intent = getIntent();
        
        //String username = intent.getStringExtra("user");
        // Grabs the StaffMember object from the previous activity.
        StaffMember staff = 
        		(StaffMember) intent.getSerializableExtra("staff");
        
        //Provides a welcome message to the staff member that has logged in.
        String message = "Welcome " + staff.getUsername() + "!";
        
        //Displays the message in textview.
        tv.setTextSize(20);
        tv.setText(message);
        
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to
		// the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page, menu);
		return true;
	}
	
    /** Passes the current activity into the CreatePatientActiviy.
     * @param view This will the new window that the addPatient activity
     *              will be created in.
     */
    public void addPatient(View view) {
    	Intent intent = new Intent(this, CreatePatientActivity.class);
    	intent.putExtra("staff", staff);
    	startActivity(intent);		
	}
    
    /** Passes the current activity into the UpdatePatientActiviy.
     * @param view This will the new window that the addPatient activity
     *              will be created in.
     */
    public void switchUpdate(View view) {
    	Intent intent = new Intent(this, UpdatePatientActivity.class);
    	intent.putExtra("staff", staff);
    	startActivity(intent);
    }
    
    /**
     * Passes the current activity into the AccessDisplayRecordActivity
     * @param view A User Interface type
     */
    public void displayInfo(View view) {
    	Intent intent = new Intent(this, AccessDisplayRecordActivity.class);
    	intent.putExtra("staff", staff);
    	startActivity(intent);
    }
}
