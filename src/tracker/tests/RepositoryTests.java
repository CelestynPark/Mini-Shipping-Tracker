// 'package' declaration: this test class is declared in the 'tracker.tests' package.
package tracker.tests;

import domain.Address;
import domain.Shipment;
import domain.types.StandardShipment;
import repository.InMemoryShipmentRepository;

public final class RepositoryTests {
	
	// Private constructor prevents instantiation; tests are executed via static methods.
	private RepositoryTests() {}
	
	/**
	 * Test entry method for this class.
	 * 
	 * Grammar / syntax:
	 * - 'public static' allows calling RepositoryTests.runAll() without creating an instance.
	 */
	public static void runAll() {
		testSaveAndFindByTrackingId_shouldWork();
		testFindByTrackingId_null_returnsNull();
		testFindAll_returnsAllSaved();
	}
	
	private static void testSaveAndFindByTrackingId_shouldWork() {
		// Local variable with concrete type InMemoryShipmentRepository; constructor invoked via 'new'.
		InMemoryShipmentRepository repo = new InMemoryShipmentRepository();
		
		// Base type variable 'Shipment' holds a StandardShipment instance (upcast / polymorphism).
		Shipment s = new StandardShipment(
			"T-REPO-1",
			"Sender",
			"Receiver",
			new Address("Seoul", "Line1"),
			new Address("Busan", "Line2"),
			1.5
		);
		
		// Repository method call with a reference-typed argument.
		repo.save(s);
		
		// Method call returning a reference type (Shipment); may be null depending on repository behavior.
		Shipment found = repo.findByTrackingId("T-REPO-1");
		Assertions.assertNotNull(found, "found must not be null");
		// '==' on references compares identity (same object instance), not logical equality.
		Assertions.assertTrue(found == s, "must return same instance from in-memory store");
	}
	
	private static void testFindByTrackingId_null_returnsNull() {
		// Passing null as an argument; repository method returns null for null key by contract in this implementation.
		InMemoryShipmentRepository repo = new InMemoryShipmentRepository();
		Assertions.assertNull(repo.findByTrackingId(null),"null key should return null");
	}
	
	private static void testFindAll_returnsAllSaved() {
		InMemoryShipmentRepository repo = new InMemoryShipmentRepository();
		
		Shipment s1 = new StandardShipment(
			"T-REPO-2",
			"S",
			"R",
			new Address("A", "1"),
			new Address("B", "2"),
			1.0
		);
		
		Shipment s2 = new StandardShipment(
			"T-REPO-3",
			"S",
			"R",
			new Address("A", "1"),
			new Address("B", "2"),
			1.0
		);
				
		
		repo.save(s1);
		repo.save(s2);
		
		// Autoboxing: literal 2 (int) is boxed to Integer when passed as Object parameter in assertEquals(Object,...).
		// Chained calls: repo.findAll() returns List<Shipment>, then size() returns primitive int.
		Assertions.assertEquals(2, repo.findAll().size(), "findAll size must be 2");
	}
}
