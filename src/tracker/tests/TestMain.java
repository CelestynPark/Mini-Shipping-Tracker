// 'package' declaration: this compilation unit belongs to the 'tracker.tests' package.
package tracker.tests;

/**
 * Test runner (not JUnit).
 * 
 * Syntax notes:
 * - 'public static void main' is the JVM entry method.
 * - We run each test class and count PASS/FAIL.
 * - Failures throw AssertionError; we catch to continue running other tests.
 */
public final class TestMain {
	
	/**
	 * JVM entry method.
	 * 
	 * Grammar / syntax:
	 * - 'public static' means callable without an instance; JVM looks for this signature.
	 * - 'String[] args' is an array parameter for command-line arguments.
	 */
	public static void main(String[] args) {
		
		// Local variables with primitive type 'int'.
		int passed = 0;
		int failed = 0;
		
		// Each test suite is executed as a plain method call (no framework).
		// Lambda expression '() -> ShippingServiceTests.runAll()' implements Runnable (functional interface).
		passed += run("ShippingServiceTests", () -> ShippingServiceTests.runAll());
		// Ternary operator (?:) chooses 1 or 0 based on the boolean field value.
		failed += lastRunFailed ? 1 : 0;
		
		passed += run("RepositoryTests", () -> RepositoryTests.runAll());
		failed += lastRunFailed ? 1 : 0;
		
		passed += run("EncapsulationTest", () -> EncapsulationTests.runAll());
		failed += lastRunFailed ? 1 : 0;
		
		passed += run("AddressValidationTests", () -> AddressValidationTests.runAll());
		failed += lastRunFailed ? 1 : 0;
		
		passed += run("FeePolicyTests", () -> FeePolicyTests.runAll());
		failed += lastRunFailed ? 1 : 0;
		
		System.out.println("=====================================");
		// Parantheses force evaluation order: (passed + failed) computed before concatenation.
		System.out.println("TOTAL: " + (passed + failed) + " | PASSED: " + passed + " | FAILED: " + failed);
		System.out.println("=====================================");
		
		// Common convention: non-zero exit code on failure.
		if (failed > 0) {
			// System.exit(int) terminates the JVM with a status code.
			System.exit(1);
		}
	}
	
	// 'private' hides implementation detail from other classes.
	// 'static' field: one shared variable for the class (no per-instance storage).
	private static boolean lastRunFailed = false;
	
	/**
	 * Runs one suite and prints its result.
	 * 
	 * Grammar / syntax:
	 * - Parameter 'suite' has type Runnable (functional interface); suite.run() invokes the provided lambda body.
	 * - try/catch handles control flow when exceptions/errors are thrown.
	 */
	private static int run(String name, Runnable suite) {
		try {
		    // Assignment updates the static field before executing the suite.
			lastRunFailed = false;
			suite.run();
			System.out.println("[PASS] " + name);
			// Return statement exits the method with an int value.
			return 1;
		} catch (AssertionError e) {
			// Catching AssertionError (an Error subtype) for test assertion failures.
			lastRunFailed = true;
			System.out.println("[FAIL] " + name + " -> " + e.getMessage());
			return 0;
		} catch (Exception e) {
			// Catching checked/unchecked exceptions that are subclasses of Exception  (but not Errors).
			lastRunFailed = true;
			System.out.println("[Fail] " + name + " -> Unexpected: " + e.getClass().getSimpleName() + ": " + e.getMessage());
			return 0;
		}
	}
}
