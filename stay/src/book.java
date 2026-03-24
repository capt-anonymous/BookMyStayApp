
import java.util.*;

class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private double baseCost;

    public Reservation(String reservationId, String guestName, String roomType, double baseCost) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.baseCost = baseCost;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getBaseCost() {
        return baseCost;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Base Cost: " + baseCost;
    }
}

class AddOnService {
    private String serviceId;
    private String serviceName;
    private double serviceCost;

    public AddOnService(String serviceId, String serviceName, double serviceCost) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.serviceCost = serviceCost;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getServiceCost() {
        return serviceCost;
    }

    @Override
    public String toString() {
        return serviceName + " (ID: " + serviceId + ", Cost: " + serviceCost + ")";
    }
}

class AddOnServiceManager {
    private Map<String, List<AddOnService>> reservationServicesMap;

    public AddOnServiceManager() {
        reservationServicesMap = new HashMap<>();
    }

    public void addServiceToReservation(String reservationId, AddOnService service) {
        reservationServicesMap.putIfAbsent(reservationId, new ArrayList<>());
        reservationServicesMap.get(reservationId).add(service);
    }

    public void addMultipleServicesToReservation(String reservationId, List<AddOnService> services) {
        reservationServicesMap.putIfAbsent(reservationId, new ArrayList<>());
        reservationServicesMap.get(reservationId).addAll(services);
    }

    public List<AddOnService> getServicesForReservation(String reservationId) {
        return reservationServicesMap.getOrDefault(reservationId, new ArrayList<>());
    }

    public double calculateAdditionalCost(String reservationId) {
        double total = 0;
        List<AddOnService> services = reservationServicesMap.getOrDefault(reservationId, new ArrayList<>());

        for (AddOnService service : services) {
            total += service.getServiceCost();
        }

        return total;
    }

    public double calculateFinalCost(Reservation reservation) {
        return reservation.getBaseCost() + calculateAdditionalCost(reservation.getReservationId());
    }

    public void displayServicesForReservation(String reservationId) {
        List<AddOnService> services = getServicesForReservation(reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services selected for reservation " + reservationId);
            return;
        }

        System.out.println("Add-on services for reservation " + reservationId + ":");
        for (AddOnService service : services) {
            System.out.println(service);
        }
    }
}

public class book {
    public static void main(String[] args) {
        Reservation reservation1 = new Reservation("R101", "Sri", "Deluxe", 5000.0);

        AddOnService breakfast = new AddOnService("S01", "Breakfast", 500.0);
        AddOnService airportPickup = new AddOnService("S02", "Airport Pickup", 1200.0);
        AddOnService spa = new AddOnService("S03", "Spa Access", 1500.0);

        AddOnServiceManager serviceManager = new AddOnServiceManager();

        serviceManager.addServiceToReservation("R101", breakfast);
        serviceManager.addServiceToReservation("R101", airportPickup);
        serviceManager.addServiceToReservation("R101", spa);

        System.out.println(reservation1);
        serviceManager.displayServicesForReservation("R101");

        double additionalCost = serviceManager.calculateAdditionalCost("R101");
        double finalCost = serviceManager.calculateFinalCost(reservation1);

        System.out.println("Total Additional Cost: " + additionalCost);
        System.out.println("Final Reservation Cost: " + finalCost);
    }
}
