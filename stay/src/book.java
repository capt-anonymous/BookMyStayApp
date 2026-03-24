import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

class Reservation {
    private String bookingId;
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean cancelled;

    public Reservation(String bookingId, String guestName, String roomType, String roomId) {
        this.bookingId = bookingId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.cancelled = false;
    }

    public String getBookingId() { return bookingId; }
    public String getRoomType() { return roomType; }
    public String getRoomId() { return roomId; }
    public boolean isCancelled() { return cancelled; }
    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
}

class InventoryManager {
    private Map<String, Integer> availableCounts;
    private Stack<String> releasedRoomIds;

    public InventoryManager() {
        availableCounts = new HashMap<>();
        availableCounts.put("SINGLE", 5);
        availableCounts.put("DOUBLE", 3);
        releasedRoomIds = new Stack<>();
    }

    public boolean checkAvailability(String type) {
        return availableCounts.getOrDefault(type, 0) > 0;
    }

    public void allocate(String type) {
        availableCounts.put(type, availableCounts.get(type) - 1);
    }

    public void rollback(String type, String roomId) {
        availableCounts.put(type, availableCounts.get(type) + 1);
        releasedRoomIds.push(roomId);
    }

    public String getReusedRoomId() {
        if (!releasedRoomIds.isEmpty()) {
            return releasedRoomIds.pop();
        }
        return null;
    }
    
    public void printState() {
        System.out.println("  Inventory Counts: " + availableCounts);
        System.out.println("  Released Rooms Stack (LIFO): " + releasedRoomIds);
    }
}

class BookingSystem {
    private InventoryManager inventory;
    private Map<String, Reservation> history;
    private int idCounter = 1;

    public BookingSystem() {
        inventory = new InventoryManager();
        history = new HashMap<>();
    }

    public String book(String guest, String type) {
        if (!inventory.checkAvailability(type)) {
            System.out.println("[FAILED] No " + type + " rooms available for " + guest);
            return null;
        }
        
        inventory.allocate(type);
        
        String reusedId = inventory.getReusedRoomId();
        String roomId = (reusedId != null) ? reusedId : (type + "-10" + idCounter);
        String bookingId = "BKG-" + idCounter++;
        
        Reservation res = new Reservation(bookingId, guest, type, roomId);
        history.put(bookingId, res);
        
        System.out.println("[BOOKED] " + guest + " booked " + type + ". Booking ID: " + bookingId + " | Room ID: " + roomId);
        return bookingId;
    }

    public void cancel(String bookingId) {
        Reservation res = history.get(bookingId);
        
        if (res == null) {
            System.out.println("[CANCEL ERROR] Booking ID " + bookingId + " does not exist.");
            return;
        }
        if (res.isCancelled()) {
            System.out.println("[CANCEL ERROR] Booking ID " + bookingId + " is already cancelled.");
            return;
        }
        
        res.setCancelled(true);
        inventory.rollback(res.getRoomType(), res.getRoomId());
        System.out.println("[CANCELLED] Booking " + bookingId + " successfully cancelled. Room " + res.getRoomId() + " pushed to rollback stack.");
    }
    
    public void printInventoryState() {
        inventory.printState();
    }
}

public class UseCase10BookingCancellation {
    public static void main(String[] args) {
        BookingSystem system = new BookingSystem();
        
        System.out.println("=== 1. Initial Bookings ===");
        String b1 = system.book("Alice", "SINGLE");
        String b2 = system.book("Bob", "DOUBLE");
        String b3 = system.book("Charlie", "SINGLE");
        
        System.out.println("\n=== 2. State Before Cancellations ===");
        system.printInventoryState();
        
        System.out.println("\n=== 3. Processing Cancellations ===");
        system.cancel(b2);
        system.cancel(b1);
        system.cancel("BKG-999");
        system.cancel(b2);
        
        System.out.println("\n=== 4. State After Cancellations ===");
        system.printInventoryState();
        
        System.out.println("\n=== 5. New Bookings (Demonstrating Stack LIFO Reuse) ===");
        system.book("Diana", "SINGLE");
        system.book("Eve", "DOUBLE");
        
        System.out.println("\n=== 6. Final System State ===");
        system.printInventoryState();
    }
}
