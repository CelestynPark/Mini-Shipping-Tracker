// 'package' declaration: this exception type is declared in the 'exception' package.
package exception;

/**
 * Base unchecked exception for this domain.
 * 
 * Notes:
 * 	- Extending RunTimeException makes it unchecked (no 'throws' required).
 * 	- Custom exceptions communicate intent and domain-level errors clearly.
 */
public class ShippingException extends RuntimeException {
	
	/**
	 * Constructor delegates to the superclass constructor.
	 * 
	 * Grammar / syntax:
	 * - 'extends' establishes an inheritance relationship with RunTimeException.
	 * - 'super(message)' invokes the parent constructor as a constructor call statement.
	 */
	public ShippingException(String message) {
		super(message);
	}
}
