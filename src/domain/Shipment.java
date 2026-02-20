package domain;
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
  private final String trackingId;
  private final String senderName;
  private final String receiverName;

  // Composition: Shipment "has an" Address.
  private final Address from;
  private final Address to;

  private final double weightKg;

  // Enum is a type-safe set of constants.
  private ShipmentStatus status;

  // Generics: List<StatusEvent> stores StatusEvent objects.
  private final List<StatusEvent> events = new ArrayList<>();

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
    this.status = ShipmentStatus.CREATED;
  }

  // ----- Abstract methods (must be implemented in subclasses) -----

  /**
   * Abstract method: each shipment type defines its own base fee.
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
    return status;
  }

  @Override
  public void updateStatus(ShipmentStatus newStatus) {
    // Basic validation.
    if (newStatus == null) throw new ValidationException("Status must not be null.");
    this.status = newStatus;
  }

  public final void appendEvent(StatusEvent event) {
    if (event == null) throw new ValidationException("Event must not be null.");
    events.add(event);
  }

  public final List<StatusEvent> getEvents() {
    // Unmodifiable view prevents external code from mutating internal list.
    return Collections.unmodifiableList(events);
  }

  @Override
  public String trackingSummary() {
    // 'String' concatenation using '+' is fine for small strings.
    return "ID=" + trackingId
        + " | TYPE=" + typeLabel()
        + " | STATUS=" + status
        + " | FROM=" + from.toShortString()
        + " | TO=" + to.toShortString()
        + " | WEIGHT=" + weightKg + "kg";
  }

  public final String toDisplayLine() {
    return getTrackingId()
        + " | " + typeLabel()
        + " | " + getStatus()
        + " | " + getSenderName() + " -> " + getReceiverName()
        + " | " + String.format("%.1fkg", getWeightKg());
  }
}
