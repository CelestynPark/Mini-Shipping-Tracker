// 'package' declaration: this compilation unit belongs to the 'service' package (namespace).
package service;

// Imports of JDK library types (java.*) used in this file.
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Imports of project domain types (domain.*) referenced by name in this compilation unit.
import domain.Address;
import domain.Shipment;
import domain.ShipmentStatus;
import domain.ShipmentType;
import domain.StatusEvent;
import domain.types.ExpressShipment;
import domain.types.FragileShipment;
import domain.types.StandardShipment;

// Imports of custom unchecked exceptions (application/domain-specific RunTimeException subclasses).
import exception.InvalidStateException;
import exception.NotFoundException;
import exception.ValidationException;

// Imports of policy/repository abstractions used as dependencies.
import policy.FeePolicy;
import repository.ShipmentRepository;

/**
 * Service layer.
 *
 * Notes:
 * - Coordinates repository + policy objects.
 * - Encapsulates "use cases" (create, update, calculate).
 * - Depends on interfaces (DIP) rather than concrete implementations.
 */
public final class ShippingService {
  
  // 'private final' fields: encapsulated state; assigned once (constructor) and not reassigned afterwards.
  private final ShipmentRepository repository;
  private final FeePolicy feePolicy;

  /**
   * Constructor injection.
   * 
   * Grammar / syntax:
   * - Constructor name matches the class name and has no return type.
   * - Parameters are reference types; null-checks are done with 'if' + 'throw'.
   */
  public ShippingService(ShipmentRepository repository, FeePolicy feePolicy) {
    // Basic null checks to fail fast.
    if (repository == null) throw new ValidationException("Repository must not be null.");
    if (feePolicy == null) throw new ValidationException("FeePolicy must not be null.");
    
    // 'this.' disambiguates fields from parameters with the same conceptual meaning (field assignment).
    this.repository = repository;
    this.feePolicy = feePolicy;
  }
  
  
  /**
   * Use-case method: create a Shipment and persist it.
   * 
   * Grammar / syntax:
   * - Method return type is a reference type (Shipment).
   * - Parameters include enums (ShipmentType), reference types (String, Address) and a primitive -> 기본형 type(double).
   */
  public Shipment createShipment(
      ShipmentType type,
      String trackingId,
      String senderName,
      String receiverName,
      Address from,
      Address to,
      double weightKg
  ) {
	// Private helper method call for validation; argument passing is by value (reference are copied).
    validateCreate(trackingId, senderName, receiverName, from, to, weightKg);

    // Prevent duplicates.
    // Reference comparison to null is valid for any reference type; repository method may return null.
    if (repository.findByTrackingId(trackingId) != null) {
      // String concatenation via '+' operator creates a new String at runtime.
      throw new ValidationException("Tracking ID already exists: " + trackingId);
    }

    Shipment s;
    // Factory-like branching for learning (you could replace with a factory interface later).
    // Enum comparison uses '==' because enums are singleton constants.
    if (type == ShipmentType.STANDARD) {
      // Concrete subtype instantiation; upcast to Shipment via polymorphism.
      s = new StandardShipment(trackingId, senderName, receiverName, from, to, weightKg);
    } else if (type == ShipmentType.EXPRESS) {
      s = new ExpressShipment(trackingId, senderName, receiverName, from, to, weightKg);
    } else if (type == ShipmentType.FRAGILE) {
      s = new FragileShipment(trackingId, senderName, receiverName, from, to, weightKg);
    } else {
      // Defensive branch for null or unexpected enum value.
      throw new ValidationException("Unknown shipment type.");
    }

    // Initial event (composition: Shipment owns StatusEvent objects).
    // Static call LocalDateTime.now() returns a time snapshot; passed as a constructor argument.
    s.appendEvent(new StatusEvent(LocalDateTime.now(), ShipmentStatus.CREATED, "Created"));
    
    // Side-effect method call: repository persists (or stores) the Shipment instance.
    repository.save(s);
    return s;
  }
  
  /**
   * Retrieve all shipments.
   * 
   * Grammar / syntax:
   * - Generic return type: List<Shipment> indicates element type at compile time.
   */
  public List<Shipment> listAll() {
    // Defensive copy + deterministic ordering.
	// 'new ArrayList<>(...)' uses the copy-constructor; the argument is a Collection.
    List<Shipment> list = new ArrayList<>(repository.findAll());
    // Method reference 'Shipment::getTrackingId' is used as a Function key extractor for Comparator.comparing(...).
    list.sort(Comparator.comparing(Shipment::getTrackingId));
    return list;
  }
  
  /**
   * Find a Shipment by its tracking id.
   * 
   * Grammar / syntax:
   * - Null check then 'throw' is a common guard clause pattern.
   */
  public Shipment getByTrackingId(String trackingId) {
    Shipment s = repository.findByTrackingId(trackingId);
    if (s == null) throw new NotFoundException("Shipment not found: " + trackingId);
    return s;
  }
  
  /**
   * Update status + append an event, then persist.
   * 
   * Grammar / syntax:
   * - Parameter 'newStatus' is an enum type (ShipmentStatus).
   * - Control flow uses 'if' with compound boolean expressions (||, &&).
   */
  public void updateStatus(String trackingId, ShipmentStatus newStatus) {
    Shipment s = getByTrackingId(trackingId);

    // Rule example: DELIVERED or LOST is terminal (no further updates).
    // Enum comparison uses '==' and '||' combines boolean conditions.
    if (s.getStatus() == ShipmentStatus.DELIVERED || s.getStatus() == ShipmentStatus.LOST) {
    	// String concatenation includes enum-to-String via String.valueOf(...) / toString().
      throw new InvalidStateException("Cannot update terminal status: " + s.getStatus());
    }

    // Another rule: CREATED -> DELIVERED directly is not allowed (simple rule for practice).
    // '&&' requires both conditions to be true.
    if (s.getStatus() == ShipmentStatus.CREATED && newStatus == ShipmentStatus.DELIVERED) {
      throw new InvalidStateException("CREATED -> DELIVERED is not allowed. Use IN_TRANSIT first.");
    }

    // Domain method call; internal state transition happens inside Shipment.
    s.updateStatus(newStatus);
    
    // Local variable declaration; initialized with a String expression.
    String note = "Status changed to " + newStatus;
    // 'new' object creation; appendEvent records a new StatusEvent into shipment's internal collection.
    s.appendEvent(new StatusEvent(LocalDateTime.now(), newStatus, note));
    
    // Persist updated state; side-effect call.
    repository.save(s); // In-memory repo replaces existing by key.
  }
  
  /**
   * Calculate fee using a strategy (FeePolicy).
   * 
   * Grammar / syntax:
   * - Returns primitive double.
   * - Delegates to FeePolicy.compute(Shipment).
   */
  public double calculateFee(String trackingId) {
    Shipment s = getByTrackingId(trackingId);

    // Strategy pattern: feePolicy decides how to compute.
    return feePolicy.compute(s);
  }
  
  
  /*
   * Input validation helper.
   * 
   * Grammar / syntax:
   * - 'private' limits visibility to this class.
   * - Uses guard clauses with 'throw' to signal invalid arguments.
   */
  private void validateCreate(
      String trackingId,
      String senderName,
      String receiverName,
      Address from,
      Address to,
      double weightKg
  ) {
	// String.tirm() returns a new String; isEmpty() checks length==0.
    if (trackingId == null || trackingId.trim().isEmpty()) throw new ValidationException("Tracking ID is required.");
    if (senderName == null || senderName.trim().isEmpty()) throw new ValidationException("Sender name is required.");
    if (receiverName == null || receiverName.trim().isEmpty()) throw new ValidationException("Receiver name is required.");
    if (from == null) throw new ValidationException("From address is required.");
    if (to == null) throw new ValidationException("To address is required.");
    // Primitive comparison using relational operator '<='.
    if (weightKg <= 0.0) throw new ValidationException("Weight must be > 0.");
  }
}
