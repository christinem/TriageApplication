package com.example.triageapplication;

/** An exception to signal that a login has not been authenticated. */
public class LogInNotAcceptedException extends Exception {
	
	/**  A unique ID for serialization. */
	private static final long serialVersionUID = 4120180786280300376L;

    /** Constructs a LogInNotAcceptedException exception.
     * @param message This message is returned when this exception is raised.
     */
    public LogInNotAcceptedException(String message) {
    	super(message);
    }

}
