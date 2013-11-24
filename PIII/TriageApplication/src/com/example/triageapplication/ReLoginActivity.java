package com.example.triageapplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

/** This activity allows a staff member to reenter their login information if
 *  it was not found in the system.
 */
public class ReLoginActivity extends Activity {
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Sets the layout resource for this activity.
		setContentView(R.layout.activity_re_login);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to 
		// the action bar if it is present.
		getMenuInflater().inflate(R.menu.re_login, menu);
		return true;
	}
	
	
	/**
	 * Reads data from the UI, checks if username and password exist,
	 * information onto the next activity, HomePageActivity.
	 * @param view A user interface component.
	 * @throws FileNotFoundException 
	 * @throws Exception If FileNotFound or 
	 */
	public void logIn(View view) throws FileNotFoundException, IOException {
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);
		progressBar.setVisibility(View.VISIBLE);
		
		Intent intent;
		
		// This user's input, username and password.
		EditText ID = (EditText) findViewById(R.id.username);
		String username = ID.getText().toString();
		
		EditText password = (EditText) findViewById(R.id.pass);
		String pass = password.getText().toString();
		
		//Read from file containing usernames and passwords
		File passwdFile = new File (this.getApplicationContext().getFilesDir(), 
				"passwords.txt");
		
		StaffMember staff;
	
		
		try {
			   String[] acceptLogIn = findUsernameAndPassword(passwdFile, 
					   username, pass);
			   
			   if(acceptLogIn[0] == "true"){ // if login authenticated
				    
				    if (acceptLogIn[1].equalsIgnoreCase("doctor")) {
				       staff = new Doctor(username);
				       intent = new Intent(this, DoctorHomePageActivity.class);
				    } else {
				       staff = new Doctor(username);
				       intent = new Intent(this, DoctorHomePageActivity.class);
				       intent = new Intent(this, NurseHomePageActivity.class);
				    }
				
				    try {
						staff.createRecordManager(
								this.getApplicationContext().getFilesDir(),
								"PatientsAndRecords", 
								this.getApplicationContext());
					} catch (IOException e) {
						// TODO Auto-generated catch block
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
				// read past password, type of user, and blank line
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
					// read past type of user and blank line 
					line = scanner.nextLine();
					line = scanner.nextLine();
				}
			}
		}
		scanner.close();
		// make a String Array of authentication, and type of staff
		String[] values = {String.valueOf(authentication), staff};
		
		// If this staffmember is registered.
		if(authentication){
			return(values);
		}
		else {
		    throw new LogInNotAcceptedException("Log-In not authenticated");	
		}
    }
    }