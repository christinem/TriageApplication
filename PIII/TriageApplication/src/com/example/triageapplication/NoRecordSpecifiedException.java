package com.example.triageapplication;

public class NoRecordSpecifiedException extends Exception {

	/** A unique ID for serialization. */
	private static final long serialVersionUID = 7066967398209414114L;
		
		/** Constructs a NoRecordSpecifiedException exception that returns
		 *  a message when raised.
		 * @param message This message is returned if this exception is raised.
		 */
		public NoRecordSpecifiedException(String message) {
			super(message);
		
	}

}
