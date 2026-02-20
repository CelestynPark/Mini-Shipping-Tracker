package tracker.tests;

import domain.Address;
import domain.Shipment;
import domain.types.StandardShipment;
import repository.InMemoryShipmentRepository;

public final class RepositoryTests {
	private RepositoryTests() {}
	
	public static void runAll() {
		testSaveAndFindByTrackingId_shouldWork();
		testFindByTrackingId_null_returnsNull();
		testFindAll_returnsAllSaved();
	}
	
	private static void testSaveAndFindByTrackingId_shouldWork() {
		InMemoryShipmentRepository repo = new InMemoryShipmentRepository();
		
		Shipment s = new StandardShipment(
			"T-REPO-1",
			"Sender",
			"Receiver",
			new Address("Seoul", "Line1"),
			new Address("Busan", "Line2"),
			1.5
		);
		
		repo.save(s);
		
		Shipment found = repo.findByTrackingId("T-REPO-1");
		Assertions.assertNotNull(found, "found must not be null");
		Assertions.assertTrue(found == s, "must return same instance from in-memory store");
	}
	
	private static void testFindByTrackingId_null_returnsNull() {
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
		
		Assertions.assertEquals(2, repo.findAll().size(), "findAll size must be 2");
	}
}
