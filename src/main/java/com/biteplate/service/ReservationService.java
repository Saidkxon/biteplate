package com.biteplate.service;

import com.biteplate.domain.reservation.Reservation;
import com.biteplate.domain.reservation.ReservationStatus;
import com.biteplate.domain.table.RestaurantTable;
import com.biteplate.dto.CreateReservationRequest;
import com.biteplate.dto.CustomerReservationRequest;
import com.biteplate.pattern.observer.NotificationCenter;
import com.biteplate.repository.ReservationRepository;
import com.biteplate.repository.TableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;
    private final NotificationCenter notificationCenter;
    private final AuditService auditService;

    public ReservationService(
            ReservationRepository reservationRepository,
            TableRepository tableRepository,
            NotificationCenter notificationCenter,
            AuditService auditService
    ) {
        this.reservationRepository = reservationRepository;
        this.tableRepository = tableRepository;
        this.notificationCenter = notificationCenter;
        this.auditService = auditService;
    }

    public List<Reservation> all() {
        return reservationRepository.findAll();
    }

    public List<Reservation> pending() {
        return reservationRepository.findByStatusOrderByReservationTimeAsc(ReservationStatus.REQUESTED);
    }

    public List<Reservation> byPhone(String phone) {
        return reservationRepository.findByPhoneOrderByReservationTimeDesc(phone);
    }

    @Transactional
    public Reservation createByCustomer(CustomerReservationRequest request) {
        tableRepository.findById(request.tableNumber())
                .orElseThrow(() -> new IllegalArgumentException("Requested table does not exist."));

        Reservation reservation = new Reservation(
                request.customerName(),
                request.phone(),
                request.tableNumber(),
                request.reservationTime(),
                "CUSTOMER"
        );

        Reservation saved = reservationRepository.save(reservation);

        String message = "New customer reservation request #" + saved.getId()
                + " for " + request.customerName()
                + ", table " + request.tableNumber()
                + ", time " + request.reservationTime();

        notificationCenter.notifyObservers("RESERVATION_REQUESTED", message);
        auditService.record("CUSTOMER_RESERVATION_REQUESTED", String.valueOf(saved.getId()), message, "CUSTOMER");

        return saved;
    }

    @Transactional
    public Reservation createByStaff(CreateReservationRequest request) {
        tableRepository.findById(request.tableNumber())
                .orElseThrow(() -> new IllegalArgumentException("Table not found."));

        Reservation reservation = new Reservation(
                request.customerName(),
                request.phone(),
                request.tableNumber(),
                request.reservationTime(),
                request.staffId()
        );

        Reservation saved = reservationRepository.save(reservation);
        confirm(saved.getId(), request.staffId());
        return reservationRepository.findById(saved.getId()).orElse(saved);
    }

    @Transactional
    public Reservation confirm(Long id, String staffId) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found."));

        if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
            return reservation;
        }

        RestaurantTable table = tableRepository.findById(reservation.getTableNumber())
                .orElseThrow(() -> new IllegalArgumentException("Table not found."));

        table.reserve();
        tableRepository.save(table);

        reservation.confirm(staffId);
        Reservation saved = reservationRepository.save(reservation);

        String staffMessage = "Reservation #" + saved.getId() + " confirmed by " + staffId
                + ". Customer message: " + saved.getCustomerMessage();

        notificationCenter.notifyObservers("RESERVATION_CONFIRMED", staffMessage);
        auditService.record("RESERVATION_CONFIRMED", String.valueOf(saved.getId()), staffMessage, staffId);

        return saved;
    }

    @Transactional
    public Reservation reject(Long id, String staffId) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found."));

        reservation.reject(staffId);
        Reservation saved = reservationRepository.save(reservation);

        String message = "Reservation #" + saved.getId() + " rejected by " + staffId;
        notificationCenter.notifyObservers("RESERVATION_REJECTED", message);
        auditService.record("RESERVATION_REJECTED", String.valueOf(saved.getId()), message, staffId);

        return saved;
    }

    @Transactional
    public String sendReminder(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found."));

        reservation.markReminderSent();
        reservationRepository.save(reservation);

        String message = "Reminder sent for reservation #" + id + " (" + reservation.getCustomerName() + ")";
        notificationCenter.notifyObservers("RESERVATION_REMINDER", message);
        auditService.record("RESERVATION_REMINDER", String.valueOf(id), message, reservation.getStaffId());
        return message;
    }
}
