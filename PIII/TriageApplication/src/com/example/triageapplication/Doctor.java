package com.example.triageapplication;

import java.io.Serializable;


/** This is a doctor class and is one of the user types for this
 *  application's login system */
public class Doctor extends StaffMember implements Serializable{

    /** A unique ID for serialization. */
	private static final long serialVersionUID = -4659829461859306830L;

	/** Constructs a doctor that is also a StaffMember and has a username. 
	 * @param username This doctor's username.
	 */
	public Doctor(String username) {
		super(username);
	}

	@Override
	public String toString() {
		return "Doctor";
	}
}
