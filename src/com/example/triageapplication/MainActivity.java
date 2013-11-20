package com.example.triageapplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import android.app.ActionBar;

/** This is the very first activity, and 
 * the nurse log-in screen for the application. 
 */
public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Sets the layout resource for this activity.
		setContentView(R.layout.activity_main);
		
		String string = "mathias\n1234\n\nchristine\n2345\n\n";
        
		OutputStreamWriter outputStreamWriter;
		try {
			outputStreamWriter = new OutputStreamWriter(this.getApplicationContext().openFileOutput("passwords.txt", 
			Context.MODE_PRIVATE));
			outputStreamWriter.write(string);
			outputStreamWriter.close();	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	 * @throws FileNotFoundException If this nurse is not found in the login 
	 * 								  registry.  
	 * @throws IOException  If this file is not found. 
	 */
	public void logIn(View view) throws FileNotFoundException, IOException {
		Intent intent = new Intent(this, HomePageActivity.class);
		
		// This user's input, username and password.
		EditText ID = (EditText) findViewById(R.id.username);
		String username = ID.getText().toString();
		
		EditText password = (EditText) findViewById(R.id.pass);
		String pass = password.getText().toString();
		
		//Read from file containing usernames and passwords
		File passwdFile = new File (this.getApplicationContext().getFilesDir(), 
				"passwords.txt");
		
		try {
			   boolean acceptLogIn = findUsernameAndPassword(passwdFile, 
					   username, pass);
			   
			   if(acceptLogIn){ // if login authenticated
				    Nurse nurse = new Nurse(username);
				    
				    try {
						nurse.createRecordManager(
								this.getApplicationContext().getFilesDir(),
								"Records", 
								this.getApplicationContext());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    intent.putExtra("nurse", nurse);
					startActivity(intent);	
			   } 
			} catch (LogInNotAcceptedException e) { 
				// re-prompt for new username and password
				   Intent reenter = new Intent(this, ReLoginActivity.class);
				   startActivity(reenter);
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
    public boolean findUsernameAndPassword(File file, String username,
    		String password) throws FileNotFoundException, 
    		LogInNotAcceptedException {
    	
		Scanner scanner = new Scanner(new FileInputStream(file));
		boolean authentication = false;

		
		while (scanner.hasNextLine()){
			String line = scanner.nextLine();
			if(!line.equals(username)){ //if you haven't found username in file
				// read past password and blank line, to next username
				line = scanner.nextLine();
				line = scanner.nextLine();
			}
			else { // if found username in file
				// read down to password
				line = scanner.nextLine();
				if(line.equals(password)) { // if right password for username
					authentication = true;
					break;
				} else {
					// read past blank line to next username
					line = scanner.nextLine();
				}
			}
		}
		scanner.close();
        
		// If this nurse is registered.
		if(authentication){
			return authentication;
		}
		else {
		    throw new LogInNotAcceptedException("Log-In not authenticated");	
		}
    }
}



