package exception;
/**
 * Thrown when a state transition is invalid.
 */
public final class InvalidStateException extends ShippingException {
	public InvalidStateException(String message) {
		super(message);
	}
}
