import java.util.LinkedList;
import java.util.Queue;

class RoomInventory {
    private int availableRooms;

    public RoomInventory(int initialCount) {
        this.availableRooms = initialCount;
    }

    public synchronized boolean tryBookRoom(String guestName) {
        if (availableRooms > 0) {
            System.out.println("[PROCESSING] Thread " + Thread.currentThread().getName() + " for " + guestName + ": Room available. Allocating...");
            
            try { Thread.sleep(100); } catch (InterruptedException e) {}

            availableRooms--;
            System.out.println("[SUCCESS] Room allocated to " + guestName + ". Rooms left: " + availableRooms);
            return true;
        } else {
            System.out.println("[FAILED] Room unavailable for " + guestName + ". Inventory empty.");
            return false;
        }
    }

    public int getAvailableRooms() {
        return availableRooms;
    }
}

class BookingRequest {
    String guestName;

    public BookingRequest(String guestName) {
        this.guestName = guestName;
    }
}

class BookingProcessor extends Thread {
    private final Queue<BookingRequest> requestQueue;
    private final RoomInventory inventory;

    public BookingProcessor(String threadName, Queue<BookingRequest> queue, RoomInventory inventory) {
        super(threadName);
        this.requestQueue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {
            BookingRequest request = null;

            synchronized (requestQueue) {
                if (!requestQueue.isEmpty()) {
                    request = requestQueue.poll();
                } else {
                    break;
                }
            }

            if (request != null) {
                inventory.tryBookRoom(request.guestName);
            }
        }
    }
}

public class UseCase11ConcurrentBookingSimulation {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Use Case 11: Concurrent Booking (Thread Safety) ===");
        
        RoomInventory sharedInventory = new RoomInventory(3);
        Queue<BookingRequest> sharedQueue = new LinkedList<>();

        sharedQueue.add(new BookingRequest("Alice"));
        sharedQueue.add(new BookingRequest("Bob"));
        sharedQueue.add(new BookingRequest("Charlie"));
        sharedQueue.add(new BookingRequest("Diana"));
        sharedQueue.add(new BookingRequest("Eve"));

        System.out.println("Initial Inventory: " + sharedInventory.getAvailableRooms() + " rooms.");
        System.out.println("Total Requests: " + sharedQueue.size());
        System.out.println("--------------------------------------------------\n");

        BookingProcessor thread1 = new BookingProcessor("Processor-1", sharedQueue, sharedInventory);
        BookingProcessor thread2 = new BookingProcessor("Processor-2", sharedQueue, sharedInventory);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("\n--------------------------------------------------");
        System.out.println("Final System State:");
        System.out.println("Final Room Count: " + sharedInventory.getAvailableRooms());
        System.out.println("All threads finished. No double-bookings occurred.");
    }
}
