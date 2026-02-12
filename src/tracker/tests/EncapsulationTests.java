package tracker.tests;

import java.time.LocalDateTime;

import domain.Address;
import domain.ShipmentStatus;
import domain.StatusEvent;
import domain.Shipment;
import domain.types.StandardShipment;

public final class EncapsulationTests {
	private EncapsulationTests() {}
	
	public static void runAll() {
		testGetEvents_shouldBeUnmodifiableView();
	}
	
	private static void testGetEvents_shouldBeUnmodifiableView() {
		Shipment s = new StandardShipment(
				"T-EVENT-1",
				"S",
				"R",
				new Address("Seoul", "A"),
				new Address("Busan", "B"),
				1.0
		);
		
		s.appendEvent(new StatusEvent(LocalDateTime.now(), ShipmentStatus.CREATED, "Created"));
		
		Assertions.expectThrows(UnsupportedOperationException.class, () -> {
			s.getEvents().add(new StatusEvent(LocalDateTime.now(), ShipmentStatus.IN_TRANSIT, "Hack"));
		}, "getEvents must return unmodifiable list");
	}
}
