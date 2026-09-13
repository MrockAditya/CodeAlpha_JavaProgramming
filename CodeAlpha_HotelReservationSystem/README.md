# CodeAlpha_HotelReservationSystem

A robust, console-based Hotel Reservation System built using Java. This application manages room bookings, handles room categorization with dynamic pricing, simulates credit card payment processing, and maintains booking records with file persistence.

Developed as part of the **CodeAlpha Java Programming Internship**.

---

## Features

- **Room Categorization**: Offers multiple room tiers (`Standard`, `Deluxe`, `Suite`) with fixed per-night rates using Java `Enum`.
- **Real-Time Availability**: Tracks room status to prevent double-booking.
- **Reservation Management**: Supports making new bookings and cancelling existing reservations to instantly free up room inventory.
- **Payment Processing Simulation**: Validates payment card input and generates an itemized receipt.
- **File Persistence**: Appends and stores confirmed reservation details locally in `bookings.txt`.

---

## Technical Specifications

- **Language**: Java (JDK 8 or higher)
- **Core Concepts**: Object-Oriented Programming (OOP), Java Enums, `UUID` generation, File I/O (`BufferedWriter`, `FileWriter`), Exception Handling

---

## Project Structure

```text
CodeAlpha_HotelReservationSystem/
├── README.md
├── HotelReservationSystem.java
└── bookings.txt (generated automatically upon booking)