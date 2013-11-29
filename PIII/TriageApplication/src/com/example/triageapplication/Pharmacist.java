package com.example.triageapplication;

import java.io.FileNotFoundException;

import android.content.Context;

public class Pharmacist extends StaffMember {

	/** A unique ID for serialization. */
	private static final long serialVersionUID = -5484303895168366958L;

	/** Constructs a Pharmacist that is also a StaffMember with a username
	 * @param username This Pharmacist's username.
	 */
	public Pharmacist(String username) {
		super(username);
	}
	
	/** Fills a prescription from a doctor for a patient.
	 * @param context This activity's context.
	 * @return
	 */
	public String fillPrescription(Context context){
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
