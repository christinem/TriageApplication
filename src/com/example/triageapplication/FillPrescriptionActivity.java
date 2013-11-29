package com.example.triageapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import java.io.FileNotFoundException;


/** Allows the user to access the next prescription.
 */
public class FillPrescriptionActivity extends Activity {
	
	/** This staff member is logged in. */
	private StaffMember staff;
	
	/** This activities Intent object. */
	private Intent intent;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fill_prescription);
		
		 intent = getIntent();
	     staff = (StaffMember) intent.getSerializableExtra("staff");

		
	      String text = "There currently aren't any prescriptions to be filled!";
	      
			try {
				text = staff.getPrescription(this.getApplicationContext());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		    
		  TextView fillPerscription = (TextView) findViewById(R.id.prescription);
		  fillPerscription.setMovementMethod(new ScrollingMovementMethod());
		  fillPerscription.setText(text);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fill_prescription, menu);
		return true;
	}
	
	  
}