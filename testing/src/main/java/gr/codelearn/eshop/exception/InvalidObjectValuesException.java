package gr.codelearn.eshop.exception;

/**
 * Exception that should be thrown when a Customer instance has incorrect/invalid values for its attributes
 */
public class InvalidObjectValuesException extends Exception {
	public InvalidObjectValuesException(String message) {
		super(message);
	}
}
