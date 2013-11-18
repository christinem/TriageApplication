package com.example.triageapplication;

/** Exception to signal that a day of birth is invalid. */
public class InvalidDayOfBirthException extends Exception {

	/** A unique ID for serialization. */
	private static final long serialVersionUID = 6281869388434816931L;

	/** Constructs an InvalidDayOfBirthException exception. 
	 */
	public InvalidDayOfBirthException() {
    }

}
