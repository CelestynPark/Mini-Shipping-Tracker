package domain.types;
import domain.Address;
import domain.Shipment;

/**
 * Concrete subclass: Express shipment.
 */
public final class ExpressShipment extends Shipment {
	
	public ExpressShipment(String trackingId, String senderName, String receiverName, Address from, Address to, double weightKg) {
		super(trackingId, senderName, receiverName, from, to, weightKg);
	}
	
	@Override
	public double baseFee() {
		return 6.50;
	}
	
	@Override
	public double riskFactor() {
		return 1.10;
	}
	
	@Override
	public String typeLabel() {
		return "EXPRESS";
	}
}
