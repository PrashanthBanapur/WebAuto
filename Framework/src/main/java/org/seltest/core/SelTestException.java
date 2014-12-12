package org.seltest.core;

/**
 * Class for handling framework exceptions
 * 
 * @author adityas
 **/
public class SelTestException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8678402804445866044L;

	public SelTestException() {
		super();
		// TODO Auto-generated constructor stub in all methods
	}

	public SelTestException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super();
	}

	public SelTestException(String message, Throwable cause) {
		super(message, cause);
	}

	public SelTestException(String message) {
		super(message);
	}

	public SelTestException(Throwable cause) {
		super(cause);
	}

}
