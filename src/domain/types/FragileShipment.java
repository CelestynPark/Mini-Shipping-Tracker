// 'package' declaration: this concrete subtype is declared in the 'domain.types' package.
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
	
	/**
	 * Constructor for this concrete subclass.
	 * 
	 * Grammar / syntax:
	 * - Constructor has no return type and matches class name.
	 * - 'super(...)' invokes the superclass constructor and must be the first statement in the constructor body.
	 */
	public FragileShipment(String trackingId, String senderName, String receiverName, Address from, Address to, double weightKg) {
		// Superclass constructor call; binds arguments to the parent constructor parameters.
		super(trackingId, senderName, receiverName, from, to, weightKg);
	}
	
	@Override
	public double baseFee() {
		// Override of abstract method; returns primitive double literal.
		return 5.00;
	}
	
	@Override
	public double riskFactor() {
		// Override of abstract method; returns primitive double literal (risk multiplier).
		return 1.35;
	}
	
	@Override
	public String typeLabel() {
		// Override of abstract method; returns a String literal.
		return "FRAGILE";
	}
}
