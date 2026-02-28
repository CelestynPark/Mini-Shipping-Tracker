// 'package' declaration: places this enum type in the 'domain' package.
package domain;

/**
 * Enum for shipment status.
 * 
 * Notes:
 * 	- Enum constants are public static final implicitly.
 * 	- Enum provides type safety compared to raw strings/int.
 */
public enum ShipmentStatus {
	
	//Enum constants: comma-separated identifiers; each is an instance of ShipmentStatus.
	CREATED,
	IN_TRANSIT,
	DELIVERED,
	LOST
}
