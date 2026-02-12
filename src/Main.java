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
	public static void main(String[] args) {
		// Creating an object with 'new' allocates an instance on the heap.
		ConsoleApp app = new ConsoleApp();
		app.run();
	}
}
