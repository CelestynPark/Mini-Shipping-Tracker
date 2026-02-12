package domain.types;
import domain.Address;
import domain.Shipment;

/**
 * Concrete subclass: Fragile shipment.
 * 
 * Notes:
 * 	- Demonstrates a different risk factor.
 */
public final class FragileShipment extends Shipment {
	public FragileShipment(String trackingId, String senderName, String receiverName, Address from, Address to, double weightKg) {
		super(trackingId, senderName, receiverName, from, to, weightKg);
	}
	
	@Override
	public double baseFee() {
		return 5.00;
	}
	
	@Override
	public double riskFactor() {
		return 1.35;
	}
	
	@Override
	public String typeLabel() {
		return "FRAGILE";
	}
}
