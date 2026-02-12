package exception;
/**
 * Base unchecked exception for this domain.
 * 
 * Notes:
 * 	- Extending RunTimeException makes it unchecked (no 'throws' required).
 * 	- Custom exceptions communicate intent and domain-level errors clearly.
 */
public class ShippingException extends RuntimeException {
	public ShippingException(String message) {
		super(message);
	}
}
