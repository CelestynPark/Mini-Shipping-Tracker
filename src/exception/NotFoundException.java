// 'package' declaration: this exception subtype is declared in the 'exception' package.
package exception;

/**
 * Thrown when an entity cannot be found in repository.
 */
public final class NotFoundException extends ShippingException {
	
	/**
	 * Constructor delegates to the superclass (ShippingException) constructor.
	 * 
	 * Grammar / syntax:
	 * - 'extends ShippingException' declares inheritance (subclass).
	 * - 'super(message)' forwards the argument to the parent constructor (constructor chaining).
	 */
	public NotFoundException(String message) {
		super(message);
	}
}
