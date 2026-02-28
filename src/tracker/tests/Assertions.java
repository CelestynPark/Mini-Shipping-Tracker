// 'package' declaration: this test utility class is declared in the 'tracker.tests' package.
package tracker.tests;

/**
 * Minimal assertions (no JUnit).
 * 
 * Syntax notes:
 * - Methods are 'static' so you can call Assertions.assertTrue(...) without creating an object.
 * - On failure, we throw AssertionError (standard for assertion failures).
 */
public final class Assertions {
	
	// Private constructor prevents instantiation; empty body '{}' is a valid constructor body.
	private Assertions() {}
	
	/**
	 * Asserts that a boolean condition is true.
	 * 
	 * Grammar / syntax:
	 * - 'static' method belongs to the class, not an instance.
	 * - Parameter 'condition' is a primitive boolean; 'message' is a reference type (String).
	 */
	public static void assertTrue(boolean condition, String message) {
		// Unary '!' negates a boolean; if-block executes only when condition is false.
		if (!condition) {
			// 'throw new AssertionError(...)' creates and throws an Error subtype.
			throw new AssertionError(message);
		}
	}
	
	
	/**
	 * Asserts that a boolean condition is false.
	 */
	public static void assertFalse(boolean condition, String message) {
		if (condition) {
			throw new AssertionError(message);
		}
	}
	
	/**
	 * Asserts that an object reference is not null.
	 */
	public static void assertNotNull(Object value, String message) {
		// Reference comparison to null checks for absence of an object.
		if (value == null) {
			throw new AssertionError(message);
		}
	}
	
	/**
	 * Asserts that an object reference is null.
	 */
	public static void assertNull(Object value, String message) {
		if (value != null) {
			throw new AssertionError(message);
		}
	}
	
	/**
	 * Asserts equality using Object.equals(...), with null handling.
	 * 
	 * Grammar / syntax:
	 * - Early 'return' exits the method when assertion passes.
	 * - expected.equals(actual) uses dynamic dispatch based on runtime type of expected.
	 */
	public static void assertEquals(Object expected, Object actual, String message) {
		if (expected == null && actual == null) return;
		if (expected != null && expected.equals(actual)) return;
		throw new AssertionError(message + " | expected=" + expected + " actual=" + actual);
	}
	
	/**
	 * Asserts that two doubles are equal within epsilon.
	 * 
	 * Grammar / syntax:
	 * - Uses Math.abs(double) static method.
	 * - Comparison 'diff <= eps' is a relational operator on primitives.
	 */
	public static void assertEqualsDouble(double expected, double actual, double eps, String message) {
		// Local vaiable declaration; primitive arithmetic inside Math.abs(expected - actual).
		double diff = Math.abs(expected - actual);
		if (diff <= eps) return;
		throw new AssertionError(message + " | expected=" + expected + " actual=" + actual + "diff=" + diff);
	}
	
	/**
	 * Expects a runnable to throw an exception of a specific type.
	 * 
	 * Syntax notes:
	 * - 'Class<? extends Throwable>' is a generic type that represents an exception class.
	 * - We catch Throwable to include both Exception and Error types, but validate the type.
	 */
	public static void expectThrows(Class<? extends Throwable> expectedType, Runnable action, String message) {
		// try/ catch statement: catch block runs when an exception/error is thrown in try body.
		try {
			// Runnable is a functional interface with void run(); calling action.run() executes its implementation.
			action.run();
			// If no exception was thrown, explicitly fail by throwing AssertionError.
			throw new AssertionError(message + " | expected exception=" + expectedType.getSimpleName() + " but none was thrown");
		} catch (Throwable t) {
			// Reflection-like type check: isInstance(...) tests whether t is an instance of expectedType at runtime.
			if (!expectedType.isInstance(t)) {
				throw new AssertionError(message + " | expected=" + expectedType.getSimpleName()
				   + " actual=" + t.getClass().getSimpleName() + " msg=" + t.getMessage());
			}
		}
	}
}
