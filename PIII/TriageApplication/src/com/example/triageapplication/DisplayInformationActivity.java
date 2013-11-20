package com.example.triageapplication;

import java.io.File;
import java.io.FileInputStream;
import java.util.Scanner;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

/**
 * This activity displays a Patient's Record History
 */
public class DisplayInformationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_information);
		
		  Intent intent = getIntent();
		    String healthNum = (String) intent.getStringExtra("healthcardnumber");
		    
		    File file = new File(this.getApplicationContext().getFilesDir(), healthNum);
		    StringBuilder text = new StringBuilder();
		    
		    try {
		    	Scanner scanner = new Scanner(new FileInputStream(file));
		    	String line;
		    	
		    	while(scanner.hasNextLine()) {
		    		line = scanner.nextLine();
		    		text.append(line);
		    		text.append("\n");
		    	}
		    } catch (Exception e) { // FIX THIS LATER
		    	e.printStackTrace();
		    }
		    
		    TextView recordHistory = (TextView) findViewById(R.id.display);
		    recordHistory.setMovementMethod(new ScrollingMovementMethod());
		    recordHistory.setText(text);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_information, menu);
		return true;
	}
	  
}