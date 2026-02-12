package tracker.tests;

import domain.Address;
import domain.Shipment;
import domain.types.ExpressShipment;
import domain.types.FragileShipment;
import domain.types.StandardShipment;
import exception.ValidationException;
import policy.FeePolicy;
import policy.StandardFeePolicy;

public final class FeePolicyTests {
	private FeePolicyTests() {}
	
	public static void runAll() {
		testCompute_nullShipment_throwsValidationException();
		testCompute_shouldMatchFormula_standard();
		testCompute_shouldMatchFormula_express();
		testCompute_shouldMatchFormula_fragile();
	}
	
	private static void testCompute_nullShipment_throwsValidationException() {
		FeePolicy p = new StandardFeePolicy();
		Assertions.expectThrows(ValidationException.class, () -> p.compute(null), "null shipment must fail");
	}
	
	private static void testCompute_shouldMatchFormula_standard() {
		FeePolicy p = new StandardFeePolicy();
		
		Shipment s = new StandardShipment(
				"T-POL-1",
				"S",
				"R",
				new Address("A", "1"),
				new Address("B", "2"),
				2.0
		);
		
		double expected = (3.00 + 2.0 * 1.20) * 1.00;
		Assertions.assertEqualsDouble(expected, p.compute(s), 1e-9, "standard compute mismatch");
	}
	
	private static void testCompute_shouldMatchFormula_express() {
		FeePolicy p = new StandardFeePolicy();
		
		Shipment s = new ExpressShipment(
				"T-POL-2",
				"S",
				"R",
				new Address("A", "1"),
				new Address("B", "2"),
				2.0
		);
		
		double expected = (6.50 + 2.0 * 1.20) * 1.10;
		Assertions.assertEqualsDouble(expected, p.compute(s), 1e-9, "express compute mismatch");
	}
	
	private static void testCompute_shouldMatchFormula_fragile() {
		FeePolicy p = new StandardFeePolicy();
		
		Shipment s = new FragileShipment(
				"T-POL-3",
				"S",
				"R",
				new Address("A", "1"),
				new Address("B", "2"),
				2.0
		);
		
		double expected = (5.00 + 2.0 * 1.20) * 1.35;
		Assertions.assertEqualsDouble(expected, p.compute(s), 1e-9, "fragile compute mismatch");
	}
}
