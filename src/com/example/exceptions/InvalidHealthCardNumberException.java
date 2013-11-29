package com.example.exceptions;

/** Exception to signal that a health card number is invalid. */
public class InvalidHealthCardNumberException extends Exception {

	/** A unique ID for serialization. */
	private static final long serialVersionUID = 8498321192328970175L;

	/** Constructs a InvalidHealthCardNumberException exception.
	 */
	public InvalidHealthCardNumberException() {
    }
	
}
