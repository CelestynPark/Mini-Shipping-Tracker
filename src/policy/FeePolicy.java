// 'package' declaration: this compilation unit is in the 'policy' package (namespace).
package policy;

// Importing a project reference type used as a parameter type.
import domain.Shipment;

/**
 * Strategy interface for fee computation.
 * 
 * Notes:
 * 	- This enables swapping policies without changing the service or shipment types.
 */
public interface FeePolicy {
	
	// Interface method declaration is implicitly 'public abstract' (unless marked 'default'/'static').
	// Return type 'double' is a primitive; parameter type 'Shipment' is a reference type.
	double compute(Shipment shipment);
}
