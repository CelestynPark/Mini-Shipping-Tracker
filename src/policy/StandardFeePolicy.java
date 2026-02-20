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
    if (shipment == null) throw new ValidationException("Shipment must not be null.");

    double base = shipment.baseFee();
    double weightPart = shipment.getWeightKg() * 1.20;
    double risk = shipment.riskFactor();

    return (base + weightPart) * risk;
  }
}
