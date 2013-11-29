package com.example.triageapplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

/** This is the very first activity, and 
 *  the staff member log-in screen for the application. 
 */
public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Sets the layout resource for this activity.
		setContentView(R.layout.activity_main);
		
	}
		
	/** Adds a menu title to this activity 
	 * @param menu This activity's heading, the text
	 *              in the action bar.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to 
		// the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	/** Reads data from the UI, checks if username and password exist,
	 * information onto the next activity, HomePageActivity.
	 * @param view A user interface component.
	 * @throws FileNotFoundException If this staff member is not found in the 
	 * 								  login registry.  
	 * @throws IOException  If this file is not found. 
	 */
	public void logIn(View view) throws FileNotFoundException, IOException {
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		progressBar.setVisibility(View.VISIBLE);
		
		// This user's input, username and password.
		EditText ID = (EditText) findViewById(R.id.username);
		String username = ID.getText().toString();
		
		EditText password = (EditText) findViewById(R.id.pass);
		String pass = password.getText().toString();
		
		//Read from file containing usernames and passwords
		File passwdFile = new File (this.getApplicationContext().getFilesDir(),
				"passwords.txt");
		
		StaffMember staff;
		Intent intent;
		
		try {
			   String[] acceptLogIn = findUsernameAndPassword(passwdFile, 
					   username, pass);
			   
			   if(acceptLogIn[0] == "true"){ // if login authenticated
				    
				    if (acceptLogIn[1].equalsIgnoreCase("doctor")) {
				       staff = new Doctor(username);
				       intent = new Intent(this, DoctorHomePageActivity.class);
				    }
				    
				    else if (acceptLogIn[1].equalsIgnoreCase("pharmacist")) {
				       staff = new Pharmacist(username);
				       intent = new Intent(this, 
				    		   		PharmacistHomePageActivity.class);
				    }
				    
					else {
				       staff = new Nurse(username);
				       intent = new Intent(this, NurseHomePageActivity.class);
				    }
				
				    try {
				    	File file1 = new File(
				    			this.getApplicationContext().getFilesDir(), 
				    			"PatientsAndRecords");
				    	
				    	File file2 = new File(
				    			this.getApplicationContext().getFilesDir(),
				    			"Prescriptions");
				    	
						if (!file1.exists()) {
							file1.createNewFile();
						}
						
						if (!file2.exists()) {
							file2.createNewFile();
						}
						
				    	staff.createRecordManager(
				    			this.getApplicationContext().getFilesDir(),
				    			"PatientsAndRecords", "Prescriptions", 
								this.getApplicationContext());
				    	
					} catch (IOException e) {
						e.printStackTrace();
					}
				    
					intent.putExtra("staff", staff);
					startActivity(intent);	
					finish();
			   } 
			   
			} catch (LogInNotAcceptedException e) { 
				// re-prompt for new username and password
				   Intent reenter = new Intent(this, ReLoginActivity.class);
				   startActivity(reenter);
				   finish();
			}	    
	}
	
    /**
     * Reads through file of usernames and passwords, and returns true iff the 
     * username and password exist and are correct.
     * @param file Name of file storing usernames and passwords.
     * @param username Username to authenticate.
     * @param password Password to authenticate.
     * @return True iff username and password are correct, False otherwise.
     * @throws Exception If username and password don't exist/aren't correct.
     */
    public String[] findUsernameAndPassword(File file, String username,
    		String password) throws FileNotFoundException, 
    		LogInNotAcceptedException {
    	String staff = "";
    	
		Scanner scanner = new Scanner(new FileInputStream(file));
		boolean authentication = false;

		
		while (scanner.hasNextLine()){
			String line = scanner.nextLine();
			if(!line.equals(username)){ //if you haven't found username in file
				// read past password, type of user, 
				// and blank line, to next username
				line = scanner.nextLine();
				line = scanner.nextLine();
				line = scanner.nextLine();
			}
			
			else { // if found username in file
				// read down to password
				line = scanner.nextLine();
				
				if(line.equals(password)) { // if right password for username
					authentication = true;
					line = scanner.nextLine();
					staff = line;
					break;
					
				} else {
					// read past blank line to next username
					line = scanner.nextLine();
					line = scanner.nextLine();
				}
			}
		}
		scanner.close();
		
		// make a String Array of authentication, and type of staff
		String[] values = {String.valueOf(authentication), staff};
		
		// If this staff member is registered.
		if(authentication){
			return(values);
		}
		
		else {
		    throw new LogInNotAcceptedException("Log-In not authenticated");	
		}
    }
}



