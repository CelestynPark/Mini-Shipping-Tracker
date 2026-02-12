package domain;
import java.time.LocalDateTime;

import exception.ValidationException;

/**
 * Event record for status history.
 * 
 * Notes:
 * 	- This demonstrates composition: Shipment contains a list of StatusEvent.
 */
public final class StatusEvent {
	
	private final LocalDateTime time;
	private final ShipmentStatus status;
	private final String note;
	
	public StatusEvent(LocalDateTime time, ShipmentStatus status, String note) {
		if (time == null) throw new ValidationException("Time is required.");
		if (status == null) throw new ValidationException("Status is required.");
		if (note == null) note = ""; // safe default
		this.time = time;
		this.status = status;
		this.note = note;
	}
	
	public LocalDateTime getTime() {
		return time;
	}
	
	public ShipmentStatus getStatus() {
		return status;
	}
	
	public String getNote() {
		return note;
	}
	
	public String toDisplayLine() {
		return time + " | " + status + " | " + note;
	}
}
