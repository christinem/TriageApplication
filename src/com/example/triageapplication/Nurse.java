package com.example.triageapplication;

import java.io.Serializable;

public class Nurse extends StaffMember implements Serializable{

    /** A unique ID. for serialization */
	private static final long serialVersionUID = -5644842279938541574L;

	/** Constructs a Nurse that is also a StaffMember with a username.
	 * @param username This nurse's username.
	 */
	public Nurse(String username) {
		super(username);
	}

	/* Return's this user's type as a String  */
	@Override
	public String toString() {
		return "Nurse";
	}

}
