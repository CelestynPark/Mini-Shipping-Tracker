package exception;
/**
 * Thrown when an entity cannot be found in repository.
 */
public final class NotFoundException extends ShippingException {
	public NotFoundException(String message) {
		super(message);
	}
}
