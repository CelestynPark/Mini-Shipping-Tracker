package policy;
import domain.Shipment;

/**
 * Strategy interface for fee computation.
 * 
 * Notes:
 * 	- This enables swapping policies without changing the service or shipment types.
 */
public interface FeePolicy {
	double compute(Shipment shipment);
}
