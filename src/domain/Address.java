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
	
	private final String city;
	private final String line;
	
	public Address(String city, String line) {
		if (city == null || city.trim().isEmpty()) throw new ValidationException("City is required.");
		if (line == null || line.trim().isEmpty()) throw new ValidationException("Address line is required.");
		this.city = city;
		this.line = line;
	}
	
	public String getCity() {
		return city;
	}
	
	public String getLine() {
		return line;
	}
	
	public String toShortString() {
		return city + " / " + line;
	}
}
