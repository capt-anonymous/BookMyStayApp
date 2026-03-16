import java.util.Scanner;

abstract class Room {
    private int numberOfBeds;
    private double size;
    private double pricePerNight;

    public Room(int numberOfBeds, double size, double pricePerNight) {
        this.numberOfBeds = numberOfBeds;
        this.size = size;
        this.pricePerNight = pricePerNight;
    }

    public int getNumberOfBeds() { return numberOfBeds; }
    public double getSize() { return size; }
    public double getPricePerNight() { return pricePerNight; }

    public abstract String getRoomType();

    public void displayDetails() {
        System.out.println(getRoomType() + ":");
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + (int) size + " sqft");
        System.out.println("Price per night: " + pricePerNight);
    }
}

class SingleRoom extends Room {
    public SingleRoom() { super(1, 250, 1500.0); }

    @Override
    public String getRoomType() { return "Single Room"; }
}

class DoubleRoom extends Room {
    public DoubleRoom() { super(2, 400, 2500.0); }

    @Override
    public String getRoomType() { return "Double Room"; }
}

class SuiteRoom extends Room {
    public SuiteRoom() { super(3, 750, 5000.0); }

    @Override
    public String getRoomType() { return "Suite Room"; }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        System.out.println("Welcome to BookMyStay!");
        System.out.println("Available Rooms:");
        System.out.println("1. Single Room");
        System.out.println("2. Double Room");
        System.out.println("3. Suite Room");
        System.out.print("Choose a room type (1-3): ");
        int choice = scanner.nextInt();

        Room selectedRoom = null;
        int available = 0;
        switch (choice) {
            case 1:
                selectedRoom = singleRoom;
                available = singleAvailable;
                break;
            case 2:
                selectedRoom = doubleRoom;
                available = doubleAvailable;
                break;
            case 3:
                selectedRoom = suiteRoom;
                available = suiteAvailable;
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        if (available > 0) {
            selectedRoom.displayDetails();
            System.out.println("Available: " + available);
            System.out.print("How many nights? ");
            int nights = scanner.nextInt();
            double total = selectedRoom.getPricePerNight() * nights;
            System.out.println("Total cost: " + total);
            System.out.print("Confirm booking? (yes/no): ");
            String confirm = scanner.next();
            if (confirm.equalsIgnoreCase("yes")) {
                System.out.println("Booking confirmed!");
                // Here you could decrement available, but for simplicity, just print
            } else {
                System.out.println("Booking cancelled.");
            }
        } else {
            System.out.println("No rooms available.");
        }

        scanner.close();
    }
}