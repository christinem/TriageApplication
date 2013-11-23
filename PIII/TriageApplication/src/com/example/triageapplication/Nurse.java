package com.example.triageapplication;

import java.io.Serializable;

public class Nurse extends StaffMember implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5644842279938541574L;

	public Nurse(String username) {
		super(username);
	}

	@Override
	public String toString() {
		return "Nurse";
	}

}
