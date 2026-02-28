// 'package' declaration: this test class is declared in the 'tracker.tests' package.
package tracker.tests;

import java.util.List;

import domain.Address;
import domain.Shipment;
import domain.ShipmentStatus;
import domain.ShipmentType;
import exception.InvalidStateException;
import exception.NotFoundException;
import exception.ValidationException;
import policy.FeePolicy;
import policy.StandardFeePolicy;
import repository.InMemoryShipmentRepository;
import repository.ShipmentRepository;
import service.ShippingService;

/**
 * ShippingService tests (no JUnit).
 * 
 * Assumption:
 * - Production code packages follow: tracker.domain / tracker.service / tracker.repository / tracker.policy / tracker.exception
 */
public final class ShippingServiceTests {
	
	// Private constructor prevents instantiation; tests are executed via static methods.
	private ShippingServiceTests() {}
	
	/**
	 * Test entry method for this class.
	 * 
	 * Grammar / syntax:
	 * - 'public static' allows calling ShippingServiceTests.runAll() without creating an instance.
	 * - Invokes multiple private static test methods in sequence.
	 */
	public static void runAll() {
		testCreateShipment_shouldCreateAndStore_withCreatedEvent();
		testCreateShipment_duplicateTrackingId_throwsValidationException();
		testGetByTrackingId_notFound_throwsNotFoundException();
		testUpdateStatus_createdToDelivered_directlyNotAllowed();
		testUpdateStatus_terminalStatusCannotUpdate();
		testUpdateStatus_validTransition_addsEvent();
		testCalculateFee_shouldFollowFormula_andUsePolymorphism();
		testListAll_shouldReturnSortedByTrackingId();
	}
	
	private static ShippingService newService() {
		// Interface-typed variable holds concrete implementation (polymorphism / DIP-friendly wiring).
		ShipmentRepository repo = new InMemoryShipmentRepository();
		FeePolicy policy = new StandardFeePolicy();
		// Constructor invocation with interface-typed dependencies (constructor injection).
		return new ShippingService(repo, policy);
	}
	
	private static void testCreateShipment_shouldCreateAndStore_withCreatedEvent() {
		ShippingService service = newService();
		
		Shipment created = service.createShipment(
			ShipmentType.STANDARD,
			"T-9000",
			"Alice",
			"Bob",
			new Address("Seoul", "Mapo 1"),
			new Address("Busan", "Haeundae 2"),
			2.5
		);
		
		Assertions.assertNotNull(created, "created shipment must not be null");
		Assertions.assertEquals("T-9000", created.getTrackingId(), "trackingId mismatch");
		Assertions.assertEquals(ShipmentStatus.CREATED, created.getStatus(), "initial status must be CREATED");
		
		Shipment loaded = service.getByTrackingId("T-9000");
		// '==' compares reference identity (same object instance), not logical equality.
		Assertions.assertTrue(loaded == created, "in-memory repo should return same instance reference");
		
		// Autoboxing: int literal 1 is boxed to Integer for assertEquals(Object,...).
		Assertions.assertEquals(1, loaded.getEvents().size(), "created shipment should have exactly one event");
		Assertions.assertEquals(ShipmentStatus.CREATED, loaded.getEvents().get(0).getStatus(), "first event status must be CREATED");
	}
	
	private static void testCreateShipment_duplicateTrackingId_throwsValidationException() {
		ShippingService service = newService();
		
		service.createShipment(
			ShipmentType.EXPRESS,
			"T-1111",
			"S1",
			"R1",
			new Address("Inchoen", "A"),
			new Address("Daegu", "B"),
			1.0
		);
		
		// expectThrows uses a block lambda '() -> {...}' implementing Runnable.
		Assertions.expectThrows(ValidationException.class, () -> {
			service.createShipment(
				ShipmentType.FRAGILE,
				"T-1111",
				"S2",
				"R2",
				new Address("Daejeon", "C"),
				new Address("Gwangju", "D"),
				2.0
			);
		}, "duplicate trackingId must throw ValidationException");
	}
	
	private static void testGetByTrackingId_notFound_throwsNotFoundException() {
		ShippingService service = newService();
		
		Assertions.expectThrows(NotFoundException.class, () -> {
			service.getByTrackingId("T-NOT-EXIST");
		}, "missing shipment must throw NotFoundException");
	}
	
	private static void testUpdateStatus_createdToDelivered_directlyNotAllowed() {
		ShippingService service = newService();
		
		service.createShipment(
			ShipmentType.STANDARD,
			"T-2001",
			"A",
			"B",
			new Address("Seoul", "X"),
			new Address("Busan", "Y"),
			1.0
		);
		
		Assertions.expectThrows(InvalidStateException.class, () -> {
			service.updateStatus("T-2001", ShipmentStatus.DELIVERED);
		}, "CREATED -> DELIVERED must throw InvalidStateException");
	}
	
	private static void testUpdateStatus_terminalStatusCannotUpdate() {
		ShippingService service = newService();
		
		service.createShipment(
			ShipmentType.STANDARD,
			"T-2002",
			"A",
			"B",
			new Address("Seoul", "X"),
			new Address("Busan", "Y"),
			1.0
		);
		
		// Sequential method calls mutate the Shipment's state via the service. 
		service.updateStatus("T-2002", ShipmentStatus.IN_TRANSIT);
		service.updateStatus("T-2002", ShipmentStatus.DELIVERED);
		
		Assertions.expectThrows(InvalidStateException.class, () -> {
			service.updateStatus("T-2002", ShipmentStatus.IN_TRANSIT);
		}, "terminal status must not be updatable");
	}
	
	private static void testUpdateStatus_validTransition_addsEvent() {
		ShippingService service = newService();
		
		service.createShipment(
			ShipmentType.EXPRESS,
			"T-2003",
			"A",
			"B",
			new Address("Incheon", "X"),
			new Address("Daegu", "Y"),
			1.0
		);
		
		Shipment before = service.getByTrackingId("T-2003");
		// Primitive int local variable; size() returns primitive int.
		int eventCountBefore = before.getEvents().size();
		
		service.updateStatus("T-2003", ShipmentStatus.IN_TRANSIT);
		
		Shipment after = service.getByTrackingId("T-2003");
		Assertions.assertEquals(ShipmentStatus.IN_TRANSIT, after.getStatus(), "status must be updated to IN_TRANSIT");
		Assertions.assertEquals(eventCountBefore + 1, after.getEvents().size(), "event count must increase by 1");
		// Index expression uses subtraction; get(int) accesses list element at runtime.
		Assertions.assertEquals(ShipmentStatus.IN_TRANSIT, after.getEvents().get(after.getEvents().size() - 1).getStatus(), "last event must match updated status");
	}
	
	private static void testCalculateFee_shouldFollowFormula_andUsePolymorphism() {
		ShippingService service = newService();
		
		service.createShipment(
			ShipmentType.STANDARD,
			"T-FEE-1",
			"S",
			"R",
			new Address("Seoul", "A"),
			new Address("Busan", "B"),
			2.0
		);
		
		service.createShipment(
			ShipmentType.EXPRESS,
			"T-FEE-2",
			"S",
			"R",
			new Address("Seoul", "A"),
			new Address("Busan", "B"),
			2.0
		);
		
		service.createShipment(
			ShipmentType.FRAGILE, 
			"T-FEE-3", 
			"S", 
			"R", 
			new Address("Seoul", "A"), 
			new Address("Busan", "B"), 
			2.0
		);
		
		// Local primitive computation; parentheses force evaluation order.
		double expectedStandard = (3.00 + 2.0 * 1.20) * 1.00;
		double expectedExpress = (6.50 + 2.0 * 1.20) * 1.10;
		double expectedFragile = (5.00 + 2.0 * 1.20) * 1.35;
		
		// Method calls returning primitive doubles.
		double fee1 = service.calculateFee("T-FEE-1");
		double fee2 = service.calculateFee("T-FEE-2");
		double fee3 = service.calculateFee("T-FEE-3");
		
		// Epsilon comparison using scientific notation literal 1e-9 (double).
		Assertions.assertEqualsDouble(expectedStandard, fee1, 1e-9, "standard fee mismatch");
		Assertions.assertEqualsDouble(expectedExpress, fee2, 1e-9, "express fee mismatch");
		Assertions.assertEqualsDouble(expectedFragile, fee3, 1e-9, "fragile fee mismatch");
		
		// '==' on primitives compares numeric equality (not reference identity).
		Assertions.assertFalse(fee1 == fee2, "fees should differ by type");
		Assertions.assertFalse(fee2 == fee3, "fees should differ by type");
		Assertions.assertFalse(fee1 == fee3, "fees should differ by type");
	}
	
	private static void testListAll_shouldReturnSortedByTrackingId() {
		ShippingService service = newService();
		
		service.createShipment(
			ShipmentType.STANDARD, 
			"T-3002", 
			"S", 
			"R", 
			new Address("A", "1"), 
			new Address("B", "2"), 
			1.0
		);
		
		service.createShipment(
			ShipmentType.STANDARD, 
			"T-3001", 
			"S", 
			"R", 
			new Address("A", "1"), 
			new Address("B", "2"), 
			1.0
		);
		
		// Generic type List<Shipment> indicates compile-time element type; assigned from service.listAll().
		List<Shipment> list = service.listAll();
		Assertions.assertEquals(2, list.size(), "list size must be 2");
		Assertions.assertEquals("T-3001", list.get(0).getTrackingId(), "must be sorted ascending by trackingId");
		Assertions.assertEquals("T-3002", list.get(1).getTrackingId(), "must be sorted ascending by trackingId");
	}
}
