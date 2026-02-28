// 'package' declaration: this compilation unit belongs to the 'policy' package.
package policy;

import domain.Shipment;
import exception.ValidationException;

/**
 * Default fee policy.
 *
 * Formula (simple on purpose):
 * - fee = (baseFee + weightKg * 1.20) * riskFactor
 *
 * Notes:
 * - Uses polymorphism: baseFee() and riskFactor() are resolved at runtime based on subclass type.
 */
public final class StandardFeePolicy implements FeePolicy {

  @Override
  public double compute(Shipment shipment) {
	// Guard clause: null check + 'throw' to signal invalid argument (unchecked exception).
    if (shipment == null) throw new ValidationException("Shipment must not be null.");

    // Local variable declaration with primitive type 'double'.
    // Dynamic dispatch: method implementation is chosen at runtime based on the actual shipment subtype.
    double base = shipment.baseFee();
    // Primitive arithmetic: multiplication with a double literal (1.20) promotes computation in double precision.
    double weightPart = shipment.getWeightKg() * 1.20;
    double risk = shipment.riskFactor();
    
    // Parentheses force evaluation order for addition before multiplication.
    return (base + weightPart) * risk;
  }
}
