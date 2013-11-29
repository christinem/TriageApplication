package com.example.triageapplication;

import java.io.FileNotFoundException;

import android.content.Context;

public class Pharmacist extends StaffMember {

	/** Constructs a Pharmacist that is also a StaffMember with a username
	 * @param username This Pharmacist's username.
	 */
	public Pharmacist(String username) {
		super(username);
	}
	
	/** Fills a prescription from a doctor for a patient.
	 * @param context This activities' context.
	 * @return
	 */
	public String fillPerscription(Context context){
		String prescription = null;
		
		try {
			prescription = super.getPrescription(context);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return prescription;
	}
	
	/* Return's this user's type as a String  */
	@Override
	public String toString() {
		return "Pharmacist";
	}

}
