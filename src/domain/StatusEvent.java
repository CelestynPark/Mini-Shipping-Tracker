// 'package' declaration: this class is declared in the 'domain' package.
package domain;

import java.time.LocalDateTime;

import exception.ValidationException;

/**
 * Event record for status history.
 *
 * Notes: - This demonstrates composition: Shipment contains a list of
 * StatusEvent.
 */
public final class StatusEvent {
	
	// 'private final' fields: encapsulated immutable state assigned once in the constructor.
	private final LocalDateTime time;
	private final ShipmentStatus status;
	private final String note;
	
	
	/**
	 * Constructor initializes all final fields.
	 * 
	 * Grammar / syntax:
	 * - Parameters include reference types (LocalDateTime, ShipmentStatus, String).
	 * - Guard clauses use 'if' + 'throw' for validation.
	 * - Parameter reassignment 'note = ""' rebinds the local parameter variable (not the field).
	 */
	public StatusEvent(LocalDateTime time, ShipmentStatus status, String note) {
		if (time == null)
			throw new ValidationException("Time is required.");
		if (status == null)
			throw new ValidationException("Status is required.");
		// Assigning to the parameter variable 'note' (local) before storing into the field.
		if (note == null)
			note = ""; // safe default
		this.time = time;
		this.status = status;
		this.note = note;
	}
	
	/**
	 * Getter method returning a refernce type.
	 */
	public LocalDateTime getTime() {
		return time;
	}
	
	/**
	 * Getter method returning an enum (refernce type).
	 */
	public ShipmentStatus getStatus() {
		return status;
	}
	
	/**
	 * Getter method returning a String reference type.
	 */
	public String getNote() {
		return note;
	}
	
	/**
	 * Formats a display string.
	 * 
	 * Grammar / syntax:
	 * - Uses '+' operator for String concatenation; non-String operands are converted via String.valueOf(...).
	 */
	public String toDisplayLine() {
		return time + " | " + status + " | " + note;
	}
}
