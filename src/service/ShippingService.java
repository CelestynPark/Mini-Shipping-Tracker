package service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import domain.Address;
import domain.Shipment;
import domain.ShipmentStatus;
import domain.ShipmentType;
import domain.StatusEvent;
import domain.types.ExpressShipment;
import domain.types.FragileShipment;
import domain.types.StandardShipment;
import exception.InvalidStateException;
import exception.NotFoundException;
import exception.ValidationException;
import policy.FeePolicy;
import repository.ShipmentRepository;

/**
 * Service layer.
 * 
 * Notes:
 * 	- Coordinates repository + policy objects.
 * 	- Encapsulates "use cases" (create, update, calculate).
 *  - Depends on interfaces (DIP) rather than concrete implementations.
 */
public final class ShippingService {

	private final ShipmentRepository repository;
	private final FeePolicy feePolicy;
	
	public ShippingService(ShipmentRepository repository, FeePolicy feePolicy) {
		// Basic null checks to fail fast.
		if (repository == null) throw new ValidationException("Repository must be null.");
		if (feePolicy == null) throw new ValidationException("FeePolicy must not be null.");
		
		this.repository = repository;
		this.feePolicy = feePolicy;
	}
	
	public Shipment createShipment(
		ShipmentType type,
		String trackingId,
		String senderName,
		String receiverName,
		Address from,
		Address to,
		double weightKg
	) {
	  validateCreate(trackingId, senderName, receiverName, from, to, weightKg);
	  
	  // Prevent duplicates.
	  if (repository.findByTrackingId(trackingId) != null) {
		  throw new ValidationException("Tracking ID already exists: " + trackingId);
	  }
	  
	  Shipment s;
	  // Factory-like branching for learning (you could replace with a factory interface later).
	  if (type == ShipmentType.STANDARD) {
		  s = new StandardShipment(trackingId, senderName, receiverName, from, to, weightKg);
	  } else if (type == ShipmentType.EXPRESS) {
		  s = new ExpressShipment(trackingId, senderName, receiverName, from, to, weightKg);
	  } else if (type == ShipmentType.FRAGILE) {
		  s = new FragileShipment(trackingId, senderName, receiverName, from, to, weightKg);
	  } else {
		  throw new ValidationException("Unknown shipment type.");
	  }
	  
	  // Initial event (composition: Shipment owns StatusEvent objects).
	  s.appendEvent(new StatusEvent(LocalDateTime.now(), ShipmentStatus.CREATED, "Created"));
	  
	  repository.save(s);
	  return s;
	}
	
	public List<Shipment> listAll() {
		// Defensive copy + deterministic ordering.
		List<Shipment> list = new ArrayList<>(repository.findAll());
		list.sort(Comparator.comparing(Shipment::getTrackingId));
		return list;
	}
	
	public Shipment getByTrackingId(String trackingId) {
		Shipment s = repository.findByTrackingId(trackingId);
		if (s == null) throw new NotFoundException("Shipment not found: " + trackingId);
		return s;	
	}
	
	public void updateStatus(String trackingId, ShipmentStatus newStatus) {
		Shipment s = getByTrackingId(trackingId);
		
		// Rule example: DELIVERED or LOST is terminal (no further updates).
		if (s.getStatus() == ShipmentStatus.DELIVERED || s.getStatus() == ShipmentStatus.LOST) {
			throw new InvalidStateException("Cannot update terminal status: " + s.getStatus());
		}
		
		// Another rule: CREATED -> DELIVERED directly is not allowed (simple rule for practice).
		if (s.getStatus() == ShipmentStatus.CREATED && newStatus == ShipmentStatus.DELIVERED) {
			throw new InvalidStateException("CREATED -> DELIVERED is not allowed. Use IN_TRANSIT first.");
		}
		
		s.updateStatus(newStatus);
		
		String note = "Status changed to " + newStatus;
		s.appendEvent(new StatusEvent(LocalDateTime.now(), newStatus, note));
		
		repository.save(s); // In-memory repo replaces existing by key.
	}
	
	public double calculateFee(String trackingId) {
		Shipment s = getByTrackingId(trackingId);
		
		// Strategy pattern: feePolicy decides how to compute.
		return feePolicy.compute(s);
	}
	
	private void validateCreate(
		String trackingId,
		String senderName,
		String receiverName,
		Address from,
		Address to,
		double weightKg
	) {
		if (trackingId == null || trackingId.trim().isEmpty()) throw new ValidationException("Tracking ID is required.");
		if (senderName == null || senderName.trim().isEmpty()) throw new ValidationException("Sender name is required.");
		if (receiverName == null || receiverName.trim().isEmpty()) throw new ValidationException("Receiver name is required.");
		if (from == null) throw new ValidationException("From address is required.");
		if (to == null) throw new ValidationException("To address is required.");
		if (weightKg <= 0.0) throw new ValidationException("Weight must be > 0.");
	}
}











































