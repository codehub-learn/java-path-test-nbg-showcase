package gr.codelearn.rentbnb.exception;

/**
 * Exception that should be thrown when a domain instance has incorrect/invalid values for its attributes
 */
public class InvalidObjectValuesException extends Exception {
	public InvalidObjectValuesException(String message) {
		super(message);
	}
}
