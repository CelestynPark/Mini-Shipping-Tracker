// 'package' declaration: this interface type is declared in the 'domain' package.
package domain;

/**
 * Trackable behavior interface.
 * 
 * Notes:
 * 	- A class can 'implement' multiple interfaces.
 * 	- Interface methods are implicitly public abstract (unless default/static).
 */
public interface Trackable {
	
	// Return type 'String' is a reference type; interface method is implicitly 'public abstract'.
	String getTrackingId();
	
	// Return type is an enum (ShipmentStatus); enums are reference types.
	ShipmentStatus getStatus();
	
	// Parameter 'newStatus' is an enum; method returns void (no return value).
	void updateStatus(ShipmentStatus newStatus);
	
	// Method returns a String summary; signature is part of the interface contract.
	String trackingSummary();
}
