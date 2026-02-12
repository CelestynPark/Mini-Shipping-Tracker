package domain;
/**
 * Enum for shipment status.
 * 
 * Notes:
 * 	- Enum constants are public static final implicitly.
 * 	- Enum provides type safety compared to raw strings/int.
 */
public enum ShipmentStatus {
	CREATED,
	IN_TRANSIT,
	DELIVERED,
	LOST
}
