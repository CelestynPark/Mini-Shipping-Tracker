// 'package' declaration: this abstract base type is declared in the 'domain' package.
package domain;

// Imports for concrete list implementation and collection utilities.
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import exception.ValidationException;

/**
 * Abstract base class for all shipments.
 *
 * Notes:
 * - 'abstract' means you cannot instantiate this class directly.
 * - It can contain both concrete methods and abstract methods.
 * - Subclasses must implement abstract methods.
 */
public abstract class Shipment implements Trackable {

  // 'private' enforces encapsulation. Access via getters/setters (or controlled methods).
  // 'final' fields are assigned once (constructor) and are not reassigned afterwards.
  private final String trackingId;
  private final String senderName;
  private final String receiverName;

  // Composition: Shipment "has an" Address.
  private final Address from;
  private final Address to;
  
  // Primitive field: 'fianl' prevents reassignment after construction.
  private final double weightKg;

  // Enum is a type-safe set of constants.
  // Non-final field: mutable state that can change via updateStatus(...).
  private ShipmentStatus status;

  // Generics: List<StatusEvent> stores StatusEvent objects.
  // Field initialization expression creates the ArrayList instance at object construction time.
  private final List<StatusEvent> events = new ArrayList<>();
  
  /**
   * Protected constructor.
   * 
   * Grammar/ syntax:
   * - 'protected' allows access from subclasses (and same package), but not from unrelated external code.
   * - Parameter list includes reference types (String, Address) and a primitive (double).
   * - 'this.field = param' assigns constructor parameters into instance fields.
   */
  protected Shipment(
      String trackingId,
      String senderName,
      String receiverName,
      Address from,
      Address to,
      double weightKg
  ) {
    this.trackingId = trackingId;
    this.senderName = senderName;
    this.receiverName = receiverName;
    this.from = from;
    this.to = to;
    this.weightKg = weightKg;

    // Default initial status.
    // Enum constant access uses 'Type.CONSTANT' (static-like access syntax).
    this.status = ShipmentStatus.CREATED;
  }

  // ----- Abstract methods (must be implemented in subclasses) -----

  /**
   * Abstract method: each shipment type defines its own base fee.
   * 
   * Grammar / syntax:
   * - 'abstract' method has no body; subclasses must provide an implementation (override).
   */
  public abstract double baseFee();

  /**
   * Abstract method: each shipment type can define a risk multiplier.
   */
  public abstract double riskFactor();

  /**
   * Abstract method: each shipment type provides a type label.
   */
  public abstract String typeLabel();

  // ----- Concrete methods (shared implementation) -----

  @Override
  public final String getTrackingId() {
	// Returning a reference type (String).
    return trackingId;
  }

  public final String getSenderName() {
    return senderName;
  }

  public final String getReceiverName() {
    return receiverName;
  }

  public final Address getFrom() {
    return from;
  }

  public final Address getTo() {
    return to;
  }

  public final double getWeightKg() {
    return weightKg;
  }

  @Override
  public final ShipmentStatus getStatus() {
	// Returning an enum reference.
    return status;
  }

  @Override
  public void updateStatus(ShipmentStatus newStatus) {
    // Basic validation.
	// Enum comparison to null is a reference null-check; 'throw' raises an unchecked exception.
    if (newStatus == null) throw new ValidationException("Status must not be null.");
    // Assignment updates mutable instance state.
    this.status = newStatus;
  }

  public final void appendEvent(StatusEvent event) {
	// Guard clause for reference parameter.
    if (event == null) throw new ValidationException("Event must not be null.");
    // List.add(E) mutates the internal collection (events).
    events.add(event);
  }

  public final List<StatusEvent> getEvents() {
    // Unmodifiable view prevents external code from mutating internal list.
	// Collections.unmodifiableList(...) returns a read-only wrapper view of original reference.
    return Collections.unmodifiableList(events);
  }

  @Override
  public String trackingSummary() {
    // 'String' concatenation using '+' is fine for small strings.
	// Multi-line concatenation: the expression continues across lines with '+' operators.
    return "ID=" + trackingId
        + " | TYPE=" + typeLabel()
        + " | STATUS=" + status
        + " | FROM=" + from.toShortString()
        + " | TO=" + to.toShortString()
        + " | WEIGHT=" + weightKg + "kg";
  }

  public final String toDisplayLine() {
	// Method calls inside concatenation: getters are invoked, return values are converted to String as needed.
    return getTrackingId()
        + " | " + typeLabel()
        + " | " + getStatus()
        + " | " + getSenderName() + " -> " + getReceiverName()
        + " | " + String.format("%.1fkg", getWeightKg());
  }
}
