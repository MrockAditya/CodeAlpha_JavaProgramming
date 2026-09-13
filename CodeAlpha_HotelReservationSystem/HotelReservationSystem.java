package codealpha_tasks.CodeAlpha_HotelReservationSystem;

import java.io.*;
import java.util.*;

enum RoomType {
    STANDARD(100.0),
    DELUXE(180.0),
    SUITE(300.0);

    private final double rate;

    RoomType(double rate) {
        this.rate = rate;
    }

    public double getRate() {
        return rate;
    }
}

class Room {
    private int roomNumber;
    private RoomType category;
    private boolean isBooked;

    public Room(int roomNumber, RoomType category) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.isBooked = false;
    }

    public int getRoomNumber() { return roomNumber; }
    public RoomType getCategory() { return category; }
    public boolean isBooked() { return isBooked; }
    public void setBooked(boolean booked) { isBooked = booked; }
}

class Booking {
    private String bookingId;
    private String guestName;
    private int roomNumber;
    private int nights;
    private double totalAmount;

    public Booking(String guestName, int roomNumber, int nights, double totalAmount) {
        this.bookingId = "BK-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.nights = nights;
        this.totalAmount = totalAmount;
    }

    public String getBookingId() { return bookingId; }
    public int getRoomNumber() { return roomNumber; }
    public double getTotalAmount() { return totalAmount; }

    @Override
    public String toString() {
        return String.format("Booking ID: %s | Guest: %s | Room: %d | Nights: %d | Total Paid: $%.2f",
                bookingId, guestName, roomNumber, nights, totalAmount);
    }
}

public class HotelReservationSystem {
    private static final List<Room> rooms = new ArrayList<>();
    private static final Map<String, Booking> reservations = new HashMap<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static final String FILE_NAME = "bookings.txt";

    public static void main(String[] args) {
        initRooms();

        boolean running = true;
        while (running) {
            System.out.println("\n=== HOTEL RESERVATION SYSTEM ===");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Make a Reservation");
            System.out.println("3. Cancel Reservation");
            System.out.println("4. View All Bookings");
            System.out.println("5. Exit");
            System.out.print("Select choice (1-5): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    displayAvailableRooms();
                    break;
                case 2:
                    bookRoom();
                    break;
                case 3:
                    cancelBooking();
                    break;
                case 4:
                    viewBookings();
                    break;
                case 5:
                    running = false;
                    System.out.println("Thank you for using the Hotel Reservation System.");
                    break;
                default:
                    System.out.println("Invalid selection.");
            }
        }
    }

    private static void initRooms() {
        rooms.add(new Room(101, RoomType.STANDARD));
        rooms.add(new Room(102, RoomType.STANDARD));
        rooms.add(new Room(201, RoomType.DELUXE));
        rooms.add(new Room(202, RoomType.DELUXE));
        rooms.add(new Room(301, RoomType.SUITE));
    }

    private static void displayAvailableRooms() {
        System.out.println("\n--- AVAILABLE ROOMS ---");
        System.out.printf("%-10s %-12s %-10s\n", "Room No", "Category", "Rate/Night");
        System.out.println("--------------------------------");
        for (Room r : rooms) {
            if (!r.isBooked()) {
                System.out.printf("%-10d %-12s $%-9.2f\n", r.getRoomNumber(), r.getCategory(), r.getCategory().getRate());
            }
        }
        System.out.println("--------------------------------");
    }

    private static void bookRoom() {
        displayAvailableRooms();
        System.out.print("Enter Room Number to book: ");
        int roomNum = scanner.nextInt();
        scanner.nextLine();

        Room selectedRoom = null;
        for (Room r : rooms) {
            if (r.getRoomNumber() == roomNum && !r.isBooked()) {
                selectedRoom = r;
                break;
            }
        }

        if (selectedRoom == null) {
            System.out.println("Room is either invalid or already booked.");
            return;
        }

        System.out.print("Enter Guest Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter number of nights: ");
        int nights = scanner.nextInt();
        scanner.nextLine();

        double total = selectedRoom.getCategory().getRate() * nights;
        System.out.printf("Total charge: $%.2f\n", total);

        // Payment simulation
        System.out.print("Enter 16-digit Card Number for payment: ");
        String card = scanner.nextLine();
        if (card.length() < 12) {
            System.out.println("Payment failed: Invalid card number.");
            return;
        }

        System.out.println("Processing payment...");
        System.out.println("Payment Approved!");

        selectedRoom.setBooked(true);
        Booking booking = new Booking(name, roomNum, nights, total);
        reservations.put(booking.getBookingId(), booking);
        saveBookingToFile(booking);

        System.out.println("\nBooking Confirmed!");
        System.out.println(booking);
    }

    private static void cancelBooking() {
        System.out.print("Enter Booking ID to cancel: ");
        String id = scanner.nextLine().trim();

        if (!reservations.containsKey(id)) {
            System.out.println("Booking ID not found.");
            return;
        }

        Booking booking = reservations.remove(id);
        for (Room r : rooms) {
            if (r.getRoomNumber() == booking.getRoomNumber()) {
                r.setBooked(false);
                break;
            }
        }
        System.out.println("Booking " + id + " has been cancelled. Room " + booking.getRoomNumber() + " is now free.");
    }

    private static void viewBookings() {
        System.out.println("\n--- CURRENT RESERVATIONS ---");
        if (reservations.isEmpty()) {
            System.out.println("No active bookings.");
            return;
        }
        for (Booking b : reservations.values()) {
            System.out.println(b);
        }
    }

    private static void saveBookingToFile(Booking b) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FILE_NAME, true)))) {
            out.println(b.toString());
        } catch (IOException e) {
            System.out.println("Error saving booking details: " + e.getMessage());
        }
    }
}
