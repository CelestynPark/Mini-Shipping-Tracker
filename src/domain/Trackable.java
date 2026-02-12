package domain;
/**
 * Trackable behavior interface.
 * 
 * Notes:
 * 	- A class can 'implement' multiple interfaces.
 * 	- Interface methods are implicitly public abstract (unless default/static).
 */
public interface Trackable {
	String getTrackingId();
	ShipmentStatus getStatus();
	void updateStatus(ShipmentStatus newStatus);
	String trackingSummary();
}
