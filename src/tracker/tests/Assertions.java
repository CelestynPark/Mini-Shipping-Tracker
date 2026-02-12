package tracker.tests;

/**
 * Minimal assertions (no JUnit).
 * 
 * Syntax notes:
 * - Methods are 'static' so you can call Assertions.assertTrue(...) without creating an object.
 * - On failure, we throw AssertionError (standard for assertion failures).
 */
public final class Assertions {
	private Assertions() {}
	
	public static void assertTrue(boolean condition, String message) {
		if (!condition) {
			throw new AssertionError(message);
		}
	}
	
	public static void assertFalse(boolean condition, String message) {
		if (!condition) {
			throw new AssertionError(message);
		}
	}
	
	public static void assertNotNull(Object value, String message) {
		if (value == null) {
			throw new AssertionError(message);
		}
	}
	
	public static void assertNull(Object value, String message) {
		if (value != null) {
			throw new AssertionError(message);
		}
	}
	
	public static void assertEquals(Object expected, Object actual, String message) {
		if (expected == null && actual == null) return;
		if (expected != null && expected.equals(actual)) return;
		throw new AssertionError(message + " | expected=" + expected + " actual=" + actual);
	}
	
	public static void assertEqualsDouble(double expected, double actual, double eps, String message) {
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
		try {
			action.run();
			throw new AssertionError(message + " | expected exception=" + expectedType.getSimpleName() + " but none was thrown");
		} catch (Throwable t) {
			if (!expectedType.isInstance(t)) {
				throw new AssertionError(message + " | expected=" + expectedType.getSimpleName()
				   + " actual=" + t.getClass().getSimpleName() + " msg=" + t.getMessage());
			}
		}
	}
}
