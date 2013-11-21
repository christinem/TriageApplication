package com.example.triageapplication;

import java.io.Serializable;

public class Doctor extends StaffMember implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4659829461859306830L;

	public Doctor(String username) {
		super(username);
	}

}
