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
	
	public static void main(String[] args) {
		int passed = 0;
		int failed = 0;
		
		// Each test suite is executed as a plain method call (no framework).
		passed += run("ShippingServiceTests", () -> ShippingServiceTests.runAll());
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
		System.out.println("TOTAL: " + (passed + failed) + " | PASSED: " + passed + " | FAILED: " + failed);
		System.out.println("=====================================");
		
		// Common convention: non-zero exit code on failure.
		if (failed > 0) {
			System.exit(1);
		}
	}
	
	// 'private' hides implementation detail from other classes.
	private static boolean lastRunFailed = false;
	
	/**
	 * Runs one suite and prints its result.
	 */
	private static int run(String name, Runnable suite) {
		try {
			lastRunFailed = false;
			suite.run();
			System.out.println("[PASS] " + name);
			return 1;
		} catch (AssertionError e) {
			lastRunFailed = true;
			System.out.println("[FAIL] " + name + " -> " + e.getMessage());
			return 0;
		} catch (Exception e) {
			lastRunFailed = true;
			System.out.println("[Fail] " + name + " -> Unexpected: " + e.getClass().getSimpleName() + ": " + e.getMessage());
			return 0;
		}
	}
}
