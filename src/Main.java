// 'import' is a compilation unit directive that makes the imported type name available without qualification.
import app.ConsoleApp;

/**
 * Entry point class.
 * 
 * Notes (grammar / syntax):
 * - 'public' makes the class visible from anywhere.
 * - 'class' defines a reference type.
 * - 'static' means this method belongs to the class, not to an instance.
 * - 'void' means the method returns nothing.
 * - 'String[] args' is the command-line argument array.
 */
public class Main {
	
	/**
	 * JVM entry method.
	 * 
	 * Grammar / syntax:
	 * - 'public static void main(String[] args)' is the conventional entry-point signature.
	 * - 'String[]' is an array type; 'args' is a parameter identifier (formal parameter).
	 */
	public static void main(String[] args) {
		// Creating an object with 'new' allocates an instance on the heap.
		ConsoleApp app = new ConsoleApp();
		// Method invocation expression: calls an instance method on the reference 'app'.
		app.run();
	}
}
