// 'package' declaration: this test class is declared in the 'tracker.tests' package.
package tracker.tests;

import java.time.LocalDateTime;

import domain.Address;
import domain.ShipmentStatus;
import domain.StatusEvent;
import domain.Shipment;
import domain.types.StandardShipment;

public final class EncapsulationTests {
	
	// Private constructor prevents instantiation; tests are executed via static methods.
	private EncapsulationTests() {}
	
	
	/**
	 * Test entry method for this class.
	 * 
	 * Grammar / syntax:
	 * - 'public static' allows calling EncapsulationTests.runAll() without creating an instance.
	 */
	public static void runAll() {
		testGetEvents_shouldBeUnmodifiableView();
	}
	
	private static void testGetEvents_shouldBeUnmodifiableView() {
		// Declares a variable of interface/base type Shipment and assigns a concrete subtype (polymorphic assignment->대입,할당하다).
		Shipment s = new StandardShipment(
				"T-EVENT-1",
				"S",
				"R",
				new Address("Seoul", "A"),
				new Address("Busan", "B"),
				1.0
		);
		
		// Method invocation with a newly constructed argument (new StatusEvent(...)).
		s.appendEvent(new StatusEvent(LocalDateTime.now(), ShipmentStatus.CREATED, "Created"));
		
		// Expect an exception when attempting to mutate an unmodifiable view.
		// Lambda expression '() -> {...}' implements Runnable; body is a block lambda with statements.
		Assertions.expectThrows(UnsupportedOperationException.class, () -> {
			// Chained call: getEvents() returns a List, then add(...) attempts to mutate it.
			s.getEvents().add(new StatusEvent(LocalDateTime.now(), ShipmentStatus.IN_TRANSIT, "Hack"));
		}, "getEvents must return unmodifiable list");
	}
}
