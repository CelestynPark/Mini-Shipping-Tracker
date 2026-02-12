package domain.types;
import domain.Address;
import domain.Shipment;

/**
 * Concrete subclass: Standard shipment.
 * 
 * Notes:
 * 	- 'extends' creates an inheritance relationship.
 * 	- 'final' prevents further subclassing (optional design choice).
 */
public final class StandardShipment extends Shipment {
	
	public StandardShipment(String trackingId, String senderName, String receiverName, Address from, Address to, double weightKg) {
		// 'super(...)' calls the parent constructor and must be first statement.
		super(trackingId, senderName, receiverName, from, to, weightKg);
	}
	
	@Override
	public double baseFee() {
		return 3.00;
	}
	
	@Override
	public double riskFactor() {
		return 1.00;
	}
	
	@Override
	public String typeLabel() {
		return "STANDARD";
	}
}
