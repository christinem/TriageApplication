package com.example.triageapplication;

import java.io.FileNotFoundException;

import android.content.Context;

public class Pharmacist extends StaffMember {

	public Pharmacist(String username) {
		super(username);
	}
	
	public String fillPerscription(Context context){
		String prescription = null;
		try {
			prescription = super.getPrescription(context);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return prescription;
	}
	
	@Override
	public String toString() {
		return "Pharmacist";
	}

}
