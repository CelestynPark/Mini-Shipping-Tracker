// 'package' declaration: this exception subtype is declared in the 'exception' package.
package exception;

/**
 * Thrown when a state transition is invalid.
 */
public final class InvalidStateException extends ShippingException {
	
	/**
	 * Constructor delegates to the superclass (ShippingException) constructor.
	 * 
	 * Grammar / syntax:
	 * - 'extends ShippingException' declares inheritance (subclass).
	 * - 'super(message)' forwards the argument to the parent constructor (constructor chaining).
	 */
	public InvalidStateException(String message) {
		super(message);
	}
}
