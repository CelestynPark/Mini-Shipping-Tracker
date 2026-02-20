package tracker.tests;

import domain.Address;
import exception.ValidationException;

public final class AddressValidationTests {
	private AddressValidationTests() {}
	
	public static void runAll() {
		testCityRequired();
		testLineRequired();
		testValidConstruction();
	}
	
	private static void testCityRequired() {
		Assertions.expectThrows(ValidationException.class, () -> new Address(null, "Line"), "city null must fail");
		Assertions.expectThrows(ValidationException.class, () -> new Address("  ", "Line"), "city blank must fail");
	}
	
	private static void testLineRequired() {
		Assertions.expectThrows(ValidationException.class, () -> new Address("Seoul", null), "line null must fail");
		Assertions.expectThrows(ValidationException.class, () -> new Address("Seoul", "  "), "line blank must fail");
	}
	
	private static void testValidConstruction() {
		Address a = new Address("Seoul", "Mapo 1");
		Assertions.assertEquals("Seoul",  a.getCity(), "city mismatch");
		Assertions.assertEquals("Mapo 1", a.getLine(), "line mismatch");
	}
}
