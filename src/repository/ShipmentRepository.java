package repository;
import java.util.List;

import domain.Shipment;

/**
 * Repository interface.
 * 
 * Notes:
 * 	- Interface defines a contract without implementation.
 * 	- Service depends on this abstraction (DIP).
 */
public interface ShipmentRepository {
	
	// 'void' indicates no return value.
	void save(Shipment shipment);
	
	// Returns null if not found (simple approach for learning).
	Shipment findByTrackingId(String trackingId);
	
	List<Shipment> findAll();
}
