// 'package' declaration: this class is declared in the 'domain' package.
package domain;

import exception.ValidationException;

/**
 * Simple value object.
 * 
 * Notes:
 * 	- 'final' fields make the object immutable after construction.
 * 	- Immutability simplifies reasoning and prevents accidental change.
 */
public final class Address {
	
	// 'private final' fields: encapsulated state; assigned once in the constructor (no reassignment).
	private final String city;
	private final String line;
	
	
	/**
	 * Constructor for initializing all final fields.
	 * 
	 * Grammar / syntax:
	 * - Parameters are reference types (String).
	 * - Guard clauses are 'if' + 'throw' to validate inputs.
	 * - 'this.city' assigns to the field (disambiguation of field vs parameter/local name).
	 */
	public Address(String city, String line) {
		// String.trim() returns a String; isEmpty() checks length==0; '||' is logical OR.
		if (city == null || city.trim().isEmpty()) throw new ValidationException("City is required.");
		if (line == null || line.trim().isEmpty()) throw new ValidationException("Address line is required.");
		this.city = city;
		this.line = line;
	}
	
	/**
	 * Accessor (getter) method.
	 * 
	 * Grammar / syntax:
	 * - Returns a reference type (String).
	 */
	public String getCity() {
		return city;
	}
	
	
	/**
	 * Accessor (getter) method.
	 * 
	 * Grammar / syntax:
	 * - Returns a reference type (String).
	 */
	public String getLine() {
		return line;
	}
	
	/**
	 * Domain helper method producing a formatted String.
	 * 
	 * Grammar / syntax:
	 * - Uses '+' operator for String concatenation; operands are references types / String literals.
	 */
	
	public String toShortString() {
		return city + " / " + line;
	}
}
