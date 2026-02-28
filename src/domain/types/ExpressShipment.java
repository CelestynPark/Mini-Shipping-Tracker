// 'package' declaration: this concrete subtype is declared in the 'domain.types' package.
package domain.types;

import domain.Address;
import domain.Shipment;

/**
 * Concrete subclass: Express shipment.
 */
public final class ExpressShipment extends Shipment {
	
	/**
	 * Constructor for this concrete subclass.
	 * 
	 * Grammar / syntax:
	 * - Constructor has no return type and matches class name.
	 * - 'super(...)' invokes the superclass constructor and must be the first statement in the constructor body.
	 */
	public ExpressShipment(String trackingId, String senderName, String receiverName, Address from, Address to, double weightKg) {
		super(trackingId, senderName, receiverName, from, to, weightKg);
	}
	
	@Override
	public double baseFee() {
		// Override of abstract method; returns primitive double literal.
		return 6.50;
	}
	
	@Override
	public double riskFactor() {
		// Override of abstract method; returns primitive double literal.
		return 1.10;
	}
	
	@Override
	public String typeLabel() {
		// Override of abstract method; returns a String literal.
		return "EXPRESS";
	}
}
