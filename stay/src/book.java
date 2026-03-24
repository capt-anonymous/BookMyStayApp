import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// 1. Core Model: Represents a confirmed booking
class Reservation {
    private String reservationId;
    private String guestName;
    private double price;

    public Reservation(String reservationId, String guestName, double price) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId + " | Guest: " + guestName + " | Price: $" + price;
    }
}

// 2. Booking History: Focuses ONLY on storing data in insertion order
class BookingHistory {
    // List Data Structure used to preserve insertion order
    private final List<Reservation> history;

    public BookingHistory() {
        this.history = new ArrayList<>();
    }

    // Adds a confirmed reservation to the history
    public void addReservation(Reservation reservation) {
        if (reservation != null) {
            history.add(reservation);
            System.out.println("System: Reservation added to history -> " + reservation);
        }
    }

    /**
     * Retrieves stored reservations.
     * CRITICAL: Returns an unmodifiable view of the list.
     * This ensures that reporting services can read the data, but cannot
     * modify, add, or delete historical records (Audit Trail Integrity).
     */
    public List<Reservation> getHistory() {
        return Collections.unmodifiableList(history);
    }
}

// 3. Booking Report Service: Focuses ONLY on reading and summarizing data
class BookingReportService {

    // Generates a chronological audit trail
    public void printAuditTrail(List<Reservation> reservations) {
        System.out.println("\n--- AUDIT TRAIL: CHRONOLOGICAL BOOKING HISTORY ---");
        if (reservations.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (int i = 0; i < reservations.size(); i++) {
            System.out.println((i + 1) + ". " + reservations.get(i));
        }
        System.out.println("--------------------------------------------------");
    }

    // Generates a summary report without modifying data
    public void printSummaryReport(List<Reservation> reservations) {
        System.out.println("\n--- OPERATIONAL SUMMARY REPORT ---");
        System.out.println("Total Confirmed Bookings: " + reservations.size());

        double totalRevenue = 0;
        for (Reservation res : reservations) {
            totalRevenue += res.getPrice();
        }

        System.out.println("Total Revenue Generated: $" + totalRevenue);
        System.out.println("----------------------------------");
    }
}

// 4. Admin Flow: Orchestrates the actors and tests the flow
public class book {
    public static void main(String[] args) {
        // Initialize the separate components
        BookingHistory bookingHistory = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        System.out.println("=== 1. Simulating Booking Confirmations ===");
        // Flow: A booking is successfully confirmed and added to history
        bookingHistory.addReservation(new Reservation("B-1001", "Alice Smith", 150.00));
        bookingHistory.addReservation(new Reservation("B-1002", "Bob Johnson", 220.50));
        bookingHistory.addReservation(new Reservation("B-1003", "Charlie Davis", 95.00));

        System.out.println("\n=== 2. Admin Requests Reports ===");
        // Flow: Admin requests information. Data is retrieved safely.
        List<Reservation> historicalData = bookingHistory.getHistory();

        // Generate the audit trail (preserves insertion order)
        reportService.printAuditTrail(historicalData);

        // Generate the summary report
        reportService.printSummaryReport(historicalData);

        /* * Testing the modification safeguard:
         * If the Admin or ReportService tried to do: historicalData.add(...) or historicalData.clear()
         * It would throw an UnsupportedOperationException, proving the data is secure.
         */
    }
}
