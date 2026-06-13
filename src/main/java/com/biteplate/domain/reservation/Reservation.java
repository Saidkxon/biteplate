package com.biteplate.domain.reservation;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String phone;
    private int tableNumber;
    private LocalDateTime reservationTime;

    // Staff member who created or confirmed the reservation.
    private String staffId;

    private String confirmedByStaffId;
    private LocalDateTime confirmationTime;

    @Column(length = 1200)
    private String customerMessage;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    protected Reservation() {
    }

    public Reservation(String customerName, String phone, int tableNumber, LocalDateTime reservationTime, String staffId) {
        if (customerName == null || customerName.isBlank()) throw new IllegalArgumentException("Customer name is required.");
        if (phone == null || phone.isBlank()) throw new IllegalArgumentException("Phone is required.");
        if (tableNumber <= 0) throw new IllegalArgumentException("Table number must be positive.");
        if (reservationTime == null) throw new IllegalArgumentException("Reservation time is required.");

        this.customerName = customerName;
        this.phone = phone;
        this.tableNumber = tableNumber;
        this.reservationTime = reservationTime;
        this.staffId = staffId;
        this.status = ReservationStatus.REQUESTED;
        this.customerMessage = "Your reservation request has been received. A waiter will confirm it shortly.";
    }

    public Long getId() { return id; }
    public String getCustomerName() { return customerName; }
    public String getPhone() { return phone; }
    public int getTableNumber() { return tableNumber; }
    public LocalDateTime getReservationTime() { return reservationTime; }
    public String getStaffId() { return staffId; }
    public String getConfirmedByStaffId() { return confirmedByStaffId; }
    public LocalDateTime getConfirmationTime() { return confirmationTime; }
    public String getCustomerMessage() { return customerMessage; }
    public ReservationStatus getStatus() { return status; }

    public void confirm(String waiterStaffId) {
        this.staffId = waiterStaffId;
        this.confirmedByStaffId = waiterStaffId;
        this.confirmationTime = LocalDateTime.now();
        this.status = ReservationStatus.CONFIRMED;
        this.customerMessage = "Your reservation is confirmed. Your table is being prepared by BitePlate staff.";
    }

    public void reject(String waiterStaffId) {
        this.staffId = waiterStaffId;
        this.confirmedByStaffId = waiterStaffId;
        this.confirmationTime = LocalDateTime.now();
        this.status = ReservationStatus.REJECTED;
        this.customerMessage = "Sorry, your requested reservation could not be confirmed. Please choose another time or table.";
    }

    public void cancel() {
        status = ReservationStatus.CANCELLED;
        customerMessage = "Your reservation has been cancelled.";
    }

    public void markReminderSent() {
        status = ReservationStatus.REMINDER_SENT;
        customerMessage = "Reminder sent. Your confirmed reservation is still active.";
    }
}
