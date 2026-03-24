class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

class InvalidRoomTypeException extends InvalidBookingException {
    public InvalidRoomTypeException(String message) {
        super(message);
    }
}

class InsufficientInventoryException extends InvalidBookingException {
    public InsufficientInventoryException(String message) {
        super(message);
    }
}

import java.util.HashMap;
import java.util.Map;

class RoomInventory {
    private final Map<String, Integer> availableRooms;

    public RoomInventory() {
        this.availableRooms = new HashMap<>();
        availableRooms.put("SINGLE", 5);
        availableRooms.put("DOUBLE", 3);
        availableRooms.put("SUITE", 1);
    }

    public boolean isValidRoomType(String type) {
        return availableRooms.containsKey(type.toUpperCase());
    }

    public int getAvailableCount(String type) {
        return availableRooms.getOrDefault(type.toUpperCase(), 0);
    }

    public void deductInventory(String type, int count) {
        String upperType = type.toUpperCase();
        availableRooms.put(upperType, availableRooms.get(upperType) - count);
    }
}

class BookingService {
    private final RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void processBooking(String guestName, String roomType, int requestedRooms) {
        System.out.println("Processing booking for " + guestName + " -> Requested: " + requestedRooms + "x " + roomType);
        
        try {
            validateBookingRequest(roomType, requestedRooms);
            inventory.deductInventory(roomType, requestedRooms);
            System.out.println("  [SUCCESS] Booking confirmed! " + guestName + " booked " + requestedRooms + " " + roomType.toUpperCase() + " room(s).\n");
            
        } catch (InvalidBookingException e) {
            System.out.println("  [ERROR] Validation Failed: " + e.getMessage() + "\n");
        } catch (Exception e) {
            System.out.println("  [CRITICAL] Unexpected system error: " + e.getMessage() + "\n");
        }
    }

    private void validateBookingRequest(String roomType, int requestedRooms) throws InvalidBookingException {
        if (requestedRooms <= 0) {
            throw new InvalidBookingException("Requested room count must be at least 1.");
        }
        if (roomType == null || roomType.trim().isEmpty()) {
            throw new InvalidRoomTypeException("Room type cannot be blank.");
        }

        if (!inventory.isValidRoomType(roomType)) {
            throw new InvalidRoomTypeException("Room type '" + roomType + "' does not exist in our catalog.");
        }

        int available = inventory.getAvailableCount(roomType);
        if (available < requestedRooms) {
            throw new InsufficientInventoryException("Not enough '" + roomType.toUpperCase() + "' rooms. Requested: " + requestedRooms + ", Available: " + available);
        }
    }
}

public class UseCase9ErrorHandlingValidation {
    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        BookingService bookingService = new BookingService(inventory);

        System.out.println("=== Hotel Booking System: Validation & Error Handling ===\n");

        bookingService.processBooking("Alice Smith", "SINGLE", 2);
        bookingService.processBooking("Bob Jones", "PENTHOUSE", 1);
        bookingService.processBooking("Charlie Davis", "DOUBLE", 0);
        bookingService.processBooking("Diana Prince", "SUITE", 2);

        System.out.println("--- Verifying System Stability ---");
        System.out.println("System recovered from previous errors without crashing.");
        
        bookingService.processBooking("Eve Adams", "DOUBLE", 1);

        System.out.println("=== System Shutdown Safely ===");
    }
}
