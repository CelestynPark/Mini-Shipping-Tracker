package repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import domain.Shipment;
import exception.ValidationException;

/**
 * In-memory repository implementation.
 *
 * Notes:
 * - Map<K,V> is a generic type: K=String, V=Shipment.
 * - ConcurrentHashMap is thread-safe for basic operations.
 */
public final class InMemoryShipmentRepository implements ShipmentRepository {

  // Encapsulation: private field prevents external direct modification.
  private final Map<String, Shipment> store = new ConcurrentHashMap<>();

  @Override
  public void save(Shipment shipment) {
    if (shipment == null) throw new ValidationException("Shipment must not be null.");
    store.put(shipment.getTrackingId(), shipment);
  }

  @Override
  public Shipment findByTrackingId(String trackingId) {
    if (trackingId == null) return null;
    return store.get(trackingId);
  }

  @Override
  public List<Shipment> findAll() {
    return new ArrayList<>(store.values());
  }
}
