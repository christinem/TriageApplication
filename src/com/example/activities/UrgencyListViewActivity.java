package com.example.activities;

import com.example.triageapplication.R;
import com.example.triageapplication.StaffMember;
import com.example.triageapplication.R.id;
import com.example.triageapplication.R.layout;
import com.example.triageapplication.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.widget.TextView;

public class UrgencyListViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_urgency_list_view);
		
		Intent intent = getIntent();
        StaffMember staff = (StaffMember) intent.getSerializableExtra("staff");
        
        StringBuilder text = staff.getUrgencyInfo();
	    
	    TextView urgency = (TextView) findViewById(R.id.urgency_list);
	    urgency.setMovementMethod(new ScrollingMovementMethod());
	    urgency.setText(text);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items 
		// to the action bar if it is present.
		getMenuInflater().inflate(R.menu.urgency_list_view, menu);
		return true;
	}
}
