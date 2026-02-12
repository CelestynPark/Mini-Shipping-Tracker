package exception;
/**
 * Thrown when user input or arguments are invalid.
 */
public final class ValidationException extends ShippingException{
	public ValidationException(String message) {
		super(message);
	}
}
