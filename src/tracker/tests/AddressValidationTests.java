// 'package' declaration: this test class is declared in the 'tracker.tests' package.
package tracker.tests;

import domain.Address;
import exception.ValidationException;

public final class AddressValidationTests {
	
	// Private constructor prevents instantiation; tests are executed via static methods.
	private AddressValidationTests() {}
	
	
	/**
	 * Test entry method for this class.
	 * 
	 * Grammar / syntax:
	 * - 'public static' allows calling AddressValidationTests.runAll() without creating an instance.
	 * - Sequentially invokes private static test methods. 
	 */
	public static void runAll() {
		testCityRequired();
		testLineRequired();
		testValidConstruction();
	}
	
	private static void testCityRequired() {
		// Lambda expression '() -> ...' implements Runnable; used to defer execution for expectThrows.
		Assertions.expectThrows(ValidationException.class, () -> new Address(null, "Line"), "city null must fail");
		Assertions.expectThrows(ValidationException.class, () -> new Address("  ", "Line"), "city blank must fail");
	}
	
	private static void testLineRequired() {
		Assertions.expectThrows(ValidationException.class, () -> new Address("Seoul", null), "line null must fail");
		Assertions.expectThrows(ValidationException.class, () -> new Address("Seoul", "  "), "line blank must fail");
	}
	
	private static void testValidConstruction() {
		// Local variable declaration with reference type Address; constructor invocation via 'new'.
		Address a = new Address("Seoul", "Mapo 1");
		// Assertions.assertEquals(Object,Object,String): relies on equals(...) semantics for reference types. 
		Assertions.assertEquals("Seoul",  a.getCity(), "city mismatch");
		Assertions.assertEquals("Mapo 1", a.getLine(), "line mismatch");
	}
}
