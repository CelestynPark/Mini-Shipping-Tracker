// 'package' declaration: this concrete subtype is declared in the 'domain.types' package.
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
	
	/**
	 * Concrete subclass: Standard shipment.
	 * 
	 * Notes:
	 * - 'extends' creates an inheritance relationship.
	 * - 'final' prevents further subclassing (optional design choice).
	 */
	public StandardShipment(String trackingId, String senderName, String receiverName, Address from, Address to, double weightKg) {
		// 'super(...)' calls the parent constructor and must be first statement.
		super(trackingId, senderName, receiverName, from, to, weightKg);
	}
	
	@Override
	public double baseFee() {
		// Overriding an abstract method; returns primitive double literal.
		return 3.00;
	}
	
	@Override
	public double riskFactor() {
		// Overriding an abstract method; returns primitive double literal.
		return 1.00;
	}
	
	@Override
	public String typeLabel() {
		// Overriding an abstract method; returns a String literal.
		return "STANDARD";
	}
}
