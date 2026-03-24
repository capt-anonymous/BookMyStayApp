import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class HotelState implements Serializable {
    private static final long serialVersionUID = 1L;
    Map<String, Integer> inventory;
    List<String> bookingHistory;

    public HotelState() {
        inventory = new HashMap<>();
        bookingHistory = new ArrayList<>();
        inventory.put("SINGLE", 10);
        inventory.put("DOUBLE", 5);
    }
}

class PersistenceService {
    private static final String FILE_NAME = "hotel_data.ser";

    public void saveState(HotelState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(state);
            System.out.println("[PERSISTENCE] System state saved successfully to " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("[ERROR] Failed to save state: " + e.getMessage());
        }
    }

    public HotelState loadState() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("[PERSISTENCE] No saved state found. Starting with fresh defaults.");
            return new HotelState();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            HotelState state = (HotelState) ois.readObject();
            System.out.println("[PERSISTENCE] System state recovered from " + FILE_NAME);
            return state;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[ERROR] Recovery failed (file may be corrupted). Starting fresh.");
            return new HotelState();
        }
    }
}

public class UseCase12DataPersistenceRecovery {
    public static void main(String[] args) {
        PersistenceService persistence = new PersistenceService();
        
        System.out.println("=== Phase 1: Application Startup & Recovery ===");
        HotelState currentState = persistence.loadState();
        
        System.out.println("Current Inventory: " + currentState.inventory);
        System.out.println("Current History: " + currentState.bookingHistory);

        System.out.println("\n=== Phase 2: Processing New Transactions ===");
        if (currentState.inventory.get("SINGLE") > 0) {
            String newBooking = "Guest_" + (currentState.bookingHistory.size() + 1) + " booked SINGLE";
            currentState.bookingHistory.add(newBooking);
            currentState.inventory.put("SINGLE", currentState.inventory.get("SINGLE") - 1);
            System.out.println("[ACTION] Processed: " + newBooking);
        }

        System.out.println("\n=== Phase 3: System Shutdown & Persistence ===");
        System.out.println("Updated Inventory: " + currentState.inventory);
        persistence.saveState(currentState);
        
        System.out.println("\n[INFO] Try running the program again to see the data persist!");
    }
}
