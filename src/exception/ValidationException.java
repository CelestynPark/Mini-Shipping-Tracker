// 'package' declaration: this exception subtype is declared in the 'exception' package.
package exception;

/**
 * Thrown when user input or arguments are invalid.
 */
public final class ValidationException extends ShippingException{
	
	/**
	 * Constructor delegates to the superclass (ShippingException) constructor.
	 * 
	 * Grammar / syntax:
	 * - 'extends ShippingException' establishes an inheritance relationship (subclassing).
	 * - 'super(message)' is a constructor invocation that forwards the argument to the parent constructor.
	 */
	public ValidationException(String message) {
		super(message);
	}
}
