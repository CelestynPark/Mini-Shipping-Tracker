// 'package' declaration sets the namespace of this compilation unit (so the binary name becomes app.ConsoleApp).
package app;

// 'import' for a single type: resolves ConsoleApp's usage or java.time API without fully qualified name.
import java.time.LocalDateTime;
// 'java.util.List' is a generic interface type (List<Shipment>) used for typed collections.
import java.util.List;
// 'Scanner' is a concrete class used for token/line parsing from an InputStream (System.in).
import java.util.Scanner;

import domain.Address;
import domain.Shipment;
import domain.ShipmentStatus;
import domain.ShipmentType;
import domain.StatusEvent;
import exception.ShippingException;
import exception.ValidationException;
import policy.FeePolicy;
import policy.StandardFeePolicy;
import repository.InMemoryShipmentRepository;
import repository.ShipmentRepository;
import service.ShippingService;

/**
 * Console UI layer.
 * 
 * Notes:
 * - This class has one responsibility: user interaction via console.
 * - No 'package' line is used so everything stays in the default (root) package.
 * // NOTE (syntax): A 'package app;' declaration exists above, so this type is in the 'app' package.
 */
public final class ConsoleApp {
	
	// 'final' means the reference cannot be reassigned after construction.
	private final Scanner scanner;
	
	// Service is injected (composition). This reduces coupling to data storage.
	private final ShippingService service;
	
	/**
	 * No-arg constructor (constructor has no return type).
	 * 
	 * Grammar / syntax:
	 * - Field initialization uses 'this.' to refer to the current instance.
	 * - Interface-typed variables (ShipmentRepository, FeePolicy) can hold concrete implementations (polymorphic assignment).
	 */
	public ConsoleApp() {
		// System.in is an InputStream; Scanner wraps it to parse tokens/lines.
		this.scanner = new Scanner(System.in);
		
		// Wiring concrete implementations (manual dependency injection).
		ShipmentRepository repo = new InMemoryShipmentRepository();
		FeePolicy feePolicy = new StandardFeePolicy();
		
		
		// 'new ShippingService(...)' calls a constructor with arguments (overload resolution by parameter types). 
		this.service = new ShippingService(repo, feePolicy);
		// Private helper method invocation.(메소드호출)
		seedDemoData();
	}
	
	
	/**
	 * Main loop for the console UI.
	 * 
	 * Grammar / syntax:
	 * - 'while (true)' is an infinite loop; termination is done via 'break'.
	 * - try/catch uses ordered catch blocks; the first matching catch executes.
	 */
	public void run() {
		while (true) {
			printHeader();
			printMenu();
			
			// Local variable declaration + initialization; 'String' is a reference type.
			String choice = readLine("Select: ");
			
			if ("0".equals(choice)) {
				// 'break' exits the nearest loop.
				System.out.println("Bye!");
				break;
			}
			
			try {
				// Method call with a String argument; control flow depends on 'choice'.
				handle(choice);
			} catch (ShippingException e) {
				// Catching domain-specific unchecked exception.
				System.out.println("[ERROR] " + e.getMessage());
			} catch (Exception e) {
				// Defensive catch for unexpected issues.
				System.out.println("[ERROR] Unexpected: " + e.getMessage());
			}
			
			System.out.println();
		}
	}
	
	private void printHeader() {
		System.out.println("===============================================");
		System.out.println("Mini Shipping Tracker (OOP Practice, CLI)");
		// Static method call: LocalDateTime.now() is a class-level factory that returns a LocalDateTime instance.
		System.out.println("Now: " + LocalDateTime.now());
		System.out.println("===============================================");
	}
	
	private void printMenu() {
		System.out.println("1) Create shipment");
		System.out.println("2) List shipments");
		System.out.println("3) Track shipment");
		System.out.println("4) Update status");
		System.out.println("5) Calculate fee");
		System.out.println("0) Exit");
	}
	
	private void handle(String choice) {
		// Classic switch works on String since Java 7.
		// switch/case labels are String literals; 'default' handles unmatched cases.
		switch (choice) {
		case "1":
			createShipment();
			break;
		case "2":
			listShipment();
			break;
		case "3":
			trackShipment();
			break;
		case "4":
			updateStatus();
			break;
		case "5":
			calcFee();
			break;
		default:
			System.out.println("Unknown command.");
			break;
		}
	}
	
	private void createShipment() {
		System.out.println("---- Create Shipment ----");
		System.out.println("Type: 1) STANDARD 2) EXPRESS 3) FRAGILE");
		
		String typeRaw = readLine("Type: ");
		// Enum-typed local variable; value is produced by a parsing helper.
		ShipmentType type = parseType(typeRaw);
		
		String trackingId = readLine("Tracking ID (e.g., T-1000): ");
		String sender = readLine("Sender name: ");
		String receiver = readLine("Receiver name: ");
		
		String sCity = readLine("Sender city: ");
		String sLine = readLine("Sender address line: ");
		String rCity = readLine("Receiver city: ");
		String rLine = readLine("Receiver address line: ");
		
		// Primitive type 'double' parsed from String input.
		double weightKg = readDouble("Weight (kg): ");
		
		// 'new' creates instances; constructor invocation binds arguments to parameters by position/type.
		Address from = new Address(sCity, sLine);
		Address to = new Address(rCity, rLine);
		
		// Returned reference type; method call returns a Shipment (polymorphism: actual runtime subtype may vary).
		Shipment created = service.createShipment(type, trackingId, sender, receiver, from, to, weightKg);
		
		System.out.println("Created: " + created.toDisplayLine());
	}
	
	private void listShipment() {
		System.out.println("---- List ----");
		// Generic type List<Shipment>: compile-time element type safety (no cast needed when iterating).
		List<Shipment> all = service.listAll();
		
		if (all.isEmpty()) {
			System.out.println("(empty)");
			return;
		}
		
		// Enhanced for-loop (for-each): iterates over Iterable without index management.
		for (Shipment s : all) {
			// Polymorphism: toDisplayLine() is inherited, but fee/labels are overridden in subclasses.
			System.out.println(s.toDisplayLine());
		}
	}
	
	private void trackShipment() {
		System.out.println("---- Track ----");
		String id = readLine("Tracking ID: ");
		
		Shipment s = service.getByTrackingId(id);
		System.out.println(s.trackingSummary());
		
		System.out.println("---- Events ----");
		// Method returns a collection; iterating over each StatusEvent element.
		for (StatusEvent ev : s.getEvents()) {
			System.out.println(ev.toDisplayLine());
		}
	}
	
	private void updateStatus() {
		System.out.println("---- Update Status ----");
		String id = readLine("Tracking ID: ");
		
		System.out.println("Status: 1) CREATED 2) IN_TRANSIT 3) DELIVERED 4) LOST");
		String raw = readLine("New status: ");
		
		ShipmentStatus st = parseStatus(raw);
		service.updateStatus(id, st);
		
		System.out.println("Updated.");
	}
	
	private void calcFee() {
		System.out.println("---- Calculate Fee ----");
		String id = readLine("Tracking ID: ");
		
		double fee = service.calculateFee(id);
		// String.format is a static varargs method; "%.2f" is a format specifier for 2 decimal places.
		System.out.println("Fee: " + String.format("%.2f", fee));
	}
	
	private ShipmentType parseType(String raw) {
		// 'trim()' removes leading/trailing whitespace.
		// Ternary operator (?:) chooses between two expressions based on a boolean condition.
		String x = raw == null ? "" : raw.trim();
		
		// Early returns: each 'return' exits the method immediately.
		if ("1".equals(x)) return ShipmentType.STANDARD;
		if ("2".equals(x)) return ShipmentType.EXPRESS;
		if ("3".equals(x)) return ShipmentType.FRAGILE;
		
		// Throwing a custom unchecked exception to signal invalid user input.
		throw new ValidationException("Invalid type selection.");
	}
	
	private ShipmentStatus parseStatus(String raw) {
		String x = raw == null ? "" : raw.trim();
		
		if ("1".equals(x)) return ShipmentStatus.CREATED;
		if ("2".equals(x)) return ShipmentStatus.IN_TRANSIT;
		if ("3".equals(x)) return ShipmentStatus.DELIVERED;
		if ("4".equals(x)) return ShipmentStatus.LOST;
		
		throw new ValidationException("Invalid status selection.");
	}
	
	private String readLine(String prompt) {
		System.out.print(prompt);
		// nextLine() reads until newline; it can return an empty string.
		return scanner.nextLine();
	}
	
	private double readDouble(String prompt) {
		System.out.print(prompt);
		String raw = scanner.nextLine();
		
		try {
			// Static parse method; may throw NumberFormatException (unchecked) if input is not a valid numeric literal.
			return Double.parseDouble(raw.trim());
		} catch (NumberFormatException e) {
			// Wrapping low-level exception into domain-speciifc unchecked exception (exception translation pattern).
			throw new ValidationException("Not a valid number: " + raw);
		}
	}
	
	private void seedDemoData() {
		// A few demo shipments to make the app immediately usable.
		service.createShipment(
			ShipmentType.STANDARD,
			"T-1000",
			"Alice",
			"Bob",
			new Address("Seoul", "Mapo-gu 1"),
			new Address("Busan", "Haeundae 2"),
			2.5);
		
		service.createShipment(
			ShipmentType.EXPRESS,
			"T-2000",
			"Chris",
			"Dana",
			new Address("Incheon", "Bupyeong 3"),
			new Address("Daegu", "Suseong 4"),
			1.2);
		
		service.createShipment(
			ShipmentType.FRAGILE,
			"T-3000",
			"Evan",
			"Frank",
			new Address("Daejeon", "Yuseoung 5"),
			new Address("Gwangju", "Buk-gu 6"),
			3.8);
	}
}

