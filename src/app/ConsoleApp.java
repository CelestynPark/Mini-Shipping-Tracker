package app;
import java.time.LocalDateTime;
import java.util.List;
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
 */
public final class ConsoleApp {
	
	// 'final' means the reference cannot be reassigned after construction.
	private final Scanner scanner;
	
	// Service is injected (composition). This reduces coupling to data storage.
	private final ShippingService service;
	
	public ConsoleApp() {
		// System.in is an InputStream; Scanner wraps it to parse tokens/lines.
		this.scanner = new Scanner(System.in);
		
		// Wiring concrete implementations (manual dependency injection).
		ShipmentRepository repo = new InMemoryShipmentRepository();
		FeePolicy feePolicy = new StandardFeePolicy();
		
		this.service = new ShippingService(repo, feePolicy);
		seedDemoData();
	}
	
	public void run() {
		while (true) {
			printHeader();
			printMenu();
			
			String choice = readLine("Select: ");
			
			if ("0".equals(choice)) {
				// 'break' exits the nearest loop.
				System.out.println("Bye!");
				break;
			}
			
			try {
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
		ShipmentType type = parseType(typeRaw);
		
		String trackingId = readLine("Tracking ID (e.g., T-1000): ");
		String sender = readLine("Sender name: ");
		String receiver = readLine("Receiver name: ");
		
		String sCity = readLine("Sender city: ");
		String sLine = readLine("Sender address line: ");
		String rCity = readLine("Receiver city: ");
		String rLine = readLine("Receiver address line: ");
		
		double weightKg = readDouble("Weight (kg): ");
		
		Address from = new Address(sCity, sLine);
		Address to = new Address(rCity, rLine);
		
		Shipment created = service.createShipment(type, trackingId, sender, receiver, from, to, weightKg);
		
		System.out.println("Created: " + created.toDisplayLine());
	}
	
	private void listShipment() {
		System.out.println("---- List ----");
		List<Shipment> all = service.listAll();
		
		if (all.isEmpty()) {
			System.out.println("(empty)");
			return;
		}
		
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
		System.out.println("Fee: " + String.format("%.2f", fee));
	}
	
	private ShipmentType parseType(String raw) {
		// 'trim()' removes leading/trailing whitespace.
		String x = raw == null ? "" : raw.trim();
		
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
			return Double.parseDouble(raw.trim());
		} catch (NumberFormatException e) {
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
				2.5
		);
		
		service.createShipment(
				ShipmentType.FRAGILE,
				"T-3000",
				"Evan",
				"Frank",
				new Address("Daejeon", "Yuseoung 5"),
				new Address("Gwangju", "Buk-gu 6"),
				3.8
		);
	}
}

