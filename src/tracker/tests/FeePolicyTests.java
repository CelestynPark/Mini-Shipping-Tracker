// 'package' declaration: this test class is declared in the 'tracker.tests' package.
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
	
	// Private constructor prevents instantiation; tests are executed via static methods.
	private FeePolicyTests() {}
	
	/**
	 * Test entry method for this class.
	 * 
	 * Grammar / syntax:
	 * - 'public static' allows calling FeePolicyTests.runAll() without creating an instance.
	 * - Calls multiple private static test methods in sequence.
	 */
	public static void runAll() {
		testCompute_nullShipment_throwsValidationException();
		testCompute_shouldMatchFormula_standard();
		testCompute_shouldMatchFormula_express();
		testCompute_shouldMatchFormula_fragile();
	}
	
	private static void testCompute_nullShipment_throwsValidationException() {
		// Interface-typed variable FeePolicy holds a concrete implementation instance (polymorphism).
		FeePolicy p = new StandardFeePolicy();
		// Lambda '() -> p.compute(null)' implements Runnable; expectThrows verifies the thrown exception type.
		Assertions.expectThrows(ValidationException.class, () -> p.compute(null), "null shipment must fail");
	}
	
	private static void testCompute_shouldMatchFormula_standard() {
		FeePolicy p = new StandardFeePolicy();
		
		// Base type variable 'Shipment' holds a StandardShipment instance (upcast).
		Shipment s = new StandardShipment(
				"T-POL-1",
				"S",
				"R",
				new Address("A", "1"),
				new Address("B", "2"),
				2.0
		);
		
		// Local primitive 'double' computed using parentheses to force evaluation order.
		double expected = (3.00 + 2.0 * 1.20) * 1.00;
		// assertEqualDouble compares within epsilon (1e-9 is a double literal in scientific notation).
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
